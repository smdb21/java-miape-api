package org.proteored.miapeapi.text.proteinpilot.msi;

import java.io.File;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.exceptions.IllegalMiapeArgumentException;
import org.proteored.miapeapi.exceptions.MiapeDatabaseException;
import org.proteored.miapeapi.exceptions.MiapeSecurityException;
import org.proteored.miapeapi.exceptions.WrongXMLFormatException;
import org.proteored.miapeapi.interfaces.msi.MiapeMSIDocument;
import org.proteored.miapeapi.interfaces.persistence.PersistenceManager;
import org.proteored.miapeapi.text.proteinpilot.MiapeProteinPilotFile;

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

	public MiapeMSIDocument toDocument(MiapeProteinPilotFile proteinPilotFiles, PersistenceManager databaseManager,
			ControlVocabularyManager cvManager, String userName, String password, String projectName)
			throws MiapeDatabaseException, MiapeSecurityException, IllegalMiapeArgumentException {
		if (cvManager == null)
			throw new IllegalMiapeArgumentException("ControlVocabularyManager is not set");
		MiapeMSIDocument result = null;
		try {
			log.info("toDocument");

			final File peptideFile = proteinPilotFiles.toFile();
			String miapeName = FilenameUtils.getBaseName(peptideFile.getAbsolutePath());
			if (miapeName.contains("_PeptideSummary")) {
				miapeName = miapeName.replace("_PeptideSummary", "");
			}
			if (databaseManager == null) {
				result = new MiapeMsiDocumentImpl(peptideFile, cvManager, miapeName, projectName);
			} else {
				result = new MiapeMsiDocumentImpl(peptideFile, databaseManager, cvManager, userName, password,
						miapeName, projectName);
			}
			log.info("after parsing tsv file");
		} catch (final Exception e) {
			e.printStackTrace();
			log.info(e.getMessage());
			throw new WrongXMLFormatException(e);
		}

		return result;
	}

}
