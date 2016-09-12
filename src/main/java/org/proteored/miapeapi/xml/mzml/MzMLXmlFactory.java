package org.proteored.miapeapi.xml.mzml;

import java.io.FileOutputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;

import org.apache.log4j.Logger;
import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.exceptions.IllegalMiapeArgumentException;
import org.proteored.miapeapi.exceptions.MiapeDatabaseException;
import org.proteored.miapeapi.exceptions.MiapeSecurityException;
import org.proteored.miapeapi.exceptions.WrongXMLFormatException;
import org.proteored.miapeapi.interfaces.ms.MiapeMSDocument;
import org.proteored.miapeapi.interfaces.persistence.PersistenceManager;
import org.proteored.miapeapi.interfaces.xml.MiapeXmlFile;
import org.proteored.miapeapi.interfaces.xml.XmlMiapeFactory;
import org.proteored.miapeapi.xml.mzml.adapter.MzMLXmlAdapter;

import uk.ac.ebi.jmzml.model.mzml.MzML;
import uk.ac.ebi.jmzml.xml.io.MzMLMarshaller;

public class MzMLXmlFactory implements XmlMiapeFactory<MiapeMSDocument> {
	private static MzMLXmlFactory instance;
	private static Logger log = Logger.getLogger("log4j.logger.org.proteored");
	private JAXBContext jc;

	private MzMLXmlFactory() {
	}

	public static MzMLXmlFactory getFactory() {
		if (instance == null) {
			instance = new MzMLXmlFactory();
		}
		return instance;
	}

	@Override
	public MiapeXmlFile<MiapeMSDocument> toXml(MiapeMSDocument miapeMS,
			ControlVocabularyManager cvManager) {
		if (cvManager == null)
			throw new IllegalMiapeArgumentException("ControlVocabularyManager is not set");

		MiapeMzMLFile xmlFile = new MiapeMzMLFile("mzIdentML_temp");
		try {
			log.info("creating XML");
			MzML mzML = new MzMLXmlAdapter(miapeMS, cvManager).adapt();

			MzMLMarshaller marshaller = new MzMLMarshaller();
			log.info("adapted to XML: " + mzML);
			OutputStream os = new FileOutputStream(xmlFile.toFile());
			marshaller.marshall(mzML, os);
			log.info("created file");
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
			throw new WrongXMLFormatException(e);
		}
		return xmlFile;
	}

	@Override
	public MiapeMSDocument toDocument(MiapeXmlFile<MiapeMSDocument> xmlFile,
			ControlVocabularyManager controlVocabularyUtil, PersistenceManager db, String userName,
			String password) throws MiapeDatabaseException, MiapeSecurityException {
		return xmlFile.toDocument();
	}

}
