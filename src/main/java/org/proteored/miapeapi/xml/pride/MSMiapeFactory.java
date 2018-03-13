package org.proteored.miapeapi.xml.pride;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.UnmarshalException;

import org.apache.log4j.Logger;
import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.exceptions.IllegalMiapeArgumentException;
import org.proteored.miapeapi.exceptions.MiapeDatabaseException;
import org.proteored.miapeapi.exceptions.MiapeSecurityException;
import org.proteored.miapeapi.exceptions.WrongXMLFormatException;
import org.proteored.miapeapi.interfaces.ms.MiapeMSDocument;
import org.proteored.miapeapi.interfaces.persistence.PersistenceManager;
import org.proteored.miapeapi.interfaces.xml.MiapePrideXmlFile;
import org.proteored.miapeapi.xml.pride.autogenerated.ExperimentCollection;
import org.proteored.miapeapi.xml.pride.ms.MiapeMSDocumentImpl;

import edu.scripps.yates.utilities.files.ZipManager;

public class MSMiapeFactory {
	private static MSMiapeFactory instance;
	private static Logger log = Logger.getLogger("log4j.logger.org.proteored");

	private MSMiapeFactory() {
	}

	public static MSMiapeFactory getFactory() {
		if (instance == null) {
			instance = new MSMiapeFactory();
		}
		return instance;
	}

	public MiapeMSDocument toDocument(MiapePrideXmlFile xmlFile, PersistenceManager databaseManager,
			ControlVocabularyManager cvManager, String user, String password, String projectName)
					throws MiapeDatabaseException, MiapeSecurityException, IllegalMiapeArgumentException {
		if (cvManager == null)
			throw new IllegalMiapeArgumentException("ControlVocabularyManager is not set");
		MiapeMSDocumentImpl result = null;
		try {
			log.info("Converting PRIDE file in a MIAPE MS document");
			JAXBContext jc = JAXBContext.newInstance("org.proteored.miapeapi.xml.pride.autogenerated");
			File file = ZipManager.decompressFileIfNeccessary(xmlFile.toFile());
			ExperimentCollection experimentCollection = (ExperimentCollection) jc.createUnmarshaller().unmarshal(file);
			log.info("PRIDE file unmarshalled");
			if (databaseManager == null) {
				result = new MiapeMSDocumentImpl(experimentCollection.getExperiment().get(0), null, file.getName(),
						projectName, cvManager);
			} else {
				result = new MiapeMSDocumentImpl(experimentCollection.getExperiment().get(0), databaseManager,
						cvManager, user, password, file.getName(), projectName);
			}

		} catch (UnmarshalException e) {
			e.printStackTrace();
			log.info(
					"Input file format error. Check if the input file is PRIDE schema compliant here: http://www.ebi.ac.uk/pride/validateXML.do");
			throw new WrongXMLFormatException(
					"Input file format error. Check if the input file is PRIDE schema compliant here: http://www.ebi.ac.uk/pride/validateXML.do");
		} catch (JAXBException e) {
			log.info(e.getMessage());
			e.printStackTrace();
			throw new WrongXMLFormatException(e);
		} catch (Exception e) {
			log.info(e.getMessage());
			e.printStackTrace();
			throw new WrongXMLFormatException(e);
		}
		return result;
	}

}
