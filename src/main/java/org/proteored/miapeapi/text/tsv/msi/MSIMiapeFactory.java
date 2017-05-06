package org.proteored.miapeapi.text.tsv.msi;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.exceptions.IllegalMiapeArgumentException;
import org.proteored.miapeapi.exceptions.MiapeDatabaseException;
import org.proteored.miapeapi.exceptions.MiapeSecurityException;
import org.proteored.miapeapi.exceptions.WrongXMLFormatException;
import org.proteored.miapeapi.interfaces.msi.MiapeMSIDocument;
import org.proteored.miapeapi.interfaces.persistence.PersistenceManager;
import org.proteored.miapeapi.text.tsv.MiapeTSVFile;

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

	public MiapeMSIDocument toDocument(MiapeTSVFile tsvFile, PersistenceManager databaseManager,
			ControlVocabularyManager cvManager, String userName, String password, String projectName)
			throws MiapeDatabaseException, MiapeSecurityException, IllegalMiapeArgumentException {
		if (cvManager == null)
			throw new IllegalMiapeArgumentException("ControlVocabularyManager is not set");
		MiapeMSIDocument result = null;
		try {
			log.info("toDocument");

			final String miapeName = FilenameUtils.getBaseName(tsvFile.toFile().getAbsolutePath());
			if (databaseManager == null) {
				result = new MiapeMsiDocumentImpl(tsvFile.toFile(), tsvFile.getSeparator(), cvManager, miapeName,
						projectName);
			} else {
				result = new MiapeMsiDocumentImpl(tsvFile.toFile(), tsvFile.getSeparator(), databaseManager, cvManager,
						userName, password, miapeName, projectName);
			}
			log.info("after parsing tsv file");
		} catch (Exception e) {
			e.printStackTrace();
			log.info(e.getMessage());
			throw new WrongXMLFormatException(e);
		}

		return result;
	}

}
