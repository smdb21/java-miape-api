package org.proteored.miapeapi.xml.pepxml;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.exceptions.IllegalMiapeArgumentException;
import org.proteored.miapeapi.exceptions.MiapeDatabaseException;
import org.proteored.miapeapi.exceptions.MiapeSecurityException;
import org.proteored.miapeapi.exceptions.WrongXMLFormatException;
import org.proteored.miapeapi.interfaces.msi.MiapeMSIDocument;
import org.proteored.miapeapi.interfaces.persistence.PersistenceManager;

import umich.ms.fileio.exceptions.FileParsingException;

public class MSIMiapeFactory {
	private static MSIMiapeFactory instance;
	private static Logger log = Logger.getLogger("log4j.logger.org.proteored");

	private MSIMiapeFactory() {
	}

	public static MSIMiapeFactory getFactory() {
		if (instance == null) {
			instance = new MSIMiapeFactory();
		}
		return instance;
	}

	public MiapeMSIDocument toDocument(MiapePepXMLFile xmlFile, PersistenceManager databaseManager,
			ControlVocabularyManager cvManager, String userName, String password, String projectName)
			throws MiapeDatabaseException, MiapeSecurityException, IllegalMiapeArgumentException {
		if (cvManager == null)
			throw new IllegalMiapeArgumentException("ControlVocabularyManager is not set");
		MiapeMSIDocument result = null;
		try {
			log.debug("toDocument");

			final String miapeName = FilenameUtils.getBaseName(xmlFile.toFile().getAbsolutePath());
			if (databaseManager == null) {
				result = new MiapeMsiDocumentImpl(xmlFile.toFile(), cvManager, projectName);
			} else {
				result = new MiapeMsiDocumentImpl(xmlFile.toFile(), databaseManager, cvManager, userName, password,
						projectName);
			}
			log.info("after unmarshall");
		} catch (FileParsingException e) {
			e.printStackTrace();
			log.info("Input file format error. Check if the input file is a well formed pepXML file");
			throw new WrongXMLFormatException(
					"Input file format error. Check if the input file is a well formed pepXML file. " + e.getMessage());

		} catch (Exception e) {
			e.printStackTrace();
			log.info(e.getMessage());
			throw new WrongXMLFormatException(e);
		}

		return result;
	}

}
