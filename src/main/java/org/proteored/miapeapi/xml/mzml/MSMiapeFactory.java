package org.proteored.miapeapi.xml.mzml;

import java.io.File;

import org.apache.log4j.Logger;
import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.exceptions.IllegalMiapeArgumentException;
import org.proteored.miapeapi.exceptions.MiapeDatabaseException;
import org.proteored.miapeapi.exceptions.MiapeSecurityException;
import org.proteored.miapeapi.exceptions.WrongXMLFormatException;
import org.proteored.miapeapi.interfaces.ms.MiapeMSDocument;
import org.proteored.miapeapi.interfaces.persistence.PersistenceManager;

import edu.scripps.yates.utilities.files.ZipManager;
import uk.ac.ebi.jmzml.model.mzml.MzML;
import uk.ac.ebi.jmzml.xml.io.MzMLUnmarshaller;

/**
 * @author Salva
 * 
 */
public class MSMiapeFactory {
	private static MSMiapeFactory instance;
	private static Logger log = Logger.getLogger("log4j.logger.org.proteored");

	private MSMiapeFactory() {
	}

	/**
	 * Gets the MSMiapeFactory instance
	 * 
	 * @return the instance
	 */
	public static MSMiapeFactory getFactory() {
		if (instance == null) {
			instance = new MSMiapeFactory();
		}
		return instance;
	}

	/**
	 * Create a MIAPE MS document from a mzML file <
	 * 
	 * @param miapeMzMLFile
	 * @param databaseManager
	 * @param userName
	 * @param password
	 * @param projectName
	 * @return the MIAPE MS document
	 * @throws MiapeDatabaseException
	 * @throws MiapeSecurityException
	 * @throws IllegalMiapeArgumentException
	 */
	public MiapeMSDocument toDocument(MiapeMzMLFile miapeMzMLFile, PersistenceManager databaseManager,
			ControlVocabularyManager cvManager, String userName, String password, String projectName)
			throws MiapeDatabaseException, MiapeSecurityException, IllegalMiapeArgumentException {
		if (cvManager == null)
			throw new IllegalMiapeArgumentException("ControlVocabularyManager is not set");
		MiapeMSDocument result = null;
		try {
			log.info("before unmarshall");
			final File file = ZipManager.decompressFileIfNeccessary(miapeMzMLFile.toFile(), false);
			final MzMLUnmarshaller mzUnmarshaller = new MzMLUnmarshaller(file);
			log.info("after unmarshall");
			if (databaseManager == null) {
				result = new MiapeMSDocumentImpl(mzUnmarshaller, cvManager, file.getName(), projectName);
			} else {
				result = new MiapeMSDocumentImpl(mzUnmarshaller, databaseManager, cvManager, userName, password,
						file.getName(), projectName);
			}

		} catch (final IllegalStateException e) {
			e.printStackTrace();
			log.info(e.getMessage());
			throw new WrongXMLFormatException(e);
		}

		return result;
	}

	/**
	 * Create a MIAPE MS document from a mzML object
	 * 
	 * @param mzML
	 * @param databaseManager
	 * @param userName
	 * @param password
	 * @param projectName
	 * @return the MIAPE MS document
	 * @throws MiapeDatabaseException
	 * @throws MiapeSecurityException
	 * @throws IllegalMiapeArgumentException
	 */
	public MiapeMSDocument toDocument(MzML mzML, PersistenceManager databaseManager, ControlVocabularyManager cvManager,
			String userName, String password, String mzMLFileName, String projectName)
			throws MiapeDatabaseException, MiapeSecurityException, IllegalMiapeArgumentException {
		MiapeMSDocument result = null;
		try {

			if (databaseManager == null) {
				result = new MiapeMSDocumentImpl(mzML, cvManager, mzMLFileName, projectName);
			} else {
				result = new MiapeMSDocumentImpl(mzML, databaseManager, cvManager, userName, password, mzMLFileName,
						projectName);
			}

		} catch (final IllegalStateException e) {
			e.printStackTrace();
			log.info(e.getMessage());
			throw new WrongXMLFormatException(e);
		}

		return result;
	}

}
