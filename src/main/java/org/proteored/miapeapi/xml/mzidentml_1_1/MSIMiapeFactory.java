package org.proteored.miapeapi.xml.mzidentml_1_1;

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

import edu.scripps.yates.utilities.dates.DatesUtil;
import uk.ac.ebi.jmzidml.xml.io.MzIdentMLUnmarshaller;

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

	public MiapeMSIDocument toDocument(MiapeMzIdentMLFile xmlFile, PersistenceManager databaseManager,
			ControlVocabularyManager cvManager, String userName, String password, String projectName,
			boolean processInParallel)
			throws MiapeDatabaseException, MiapeSecurityException, IllegalMiapeArgumentException {
		MiapeMSIDocument result = null;
		if (cvManager == null)
			throw new IllegalMiapeArgumentException("ControlVocabularyManager is not set");
		try {
			log.info("before unmarshall");
			final File file = xmlFile.toFile();
			long t1 = System.currentTimeMillis();

			MzIdentMLUnmarshaller unmarshaller = new MzIdentMLUnmarshaller(file);
			log.info("after unmarshall: it took: "
					+ DatesUtil.getDescriptiveTimeFromMillisecs(System.currentTimeMillis() - t1));
			final String miapeName = FilenameUtils.getBaseName(file.getAbsolutePath());
			if (databaseManager == null) {

				result = new MiapeMSIDocumentImpl(unmarshaller, cvManager, miapeName, projectName, processInParallel);

			} else {

				result = new MiapeMSIDocumentImpl(unmarshaller, databaseManager, cvManager, userName, password,
						miapeName, projectName, processInParallel);

			}

		} catch (Exception e) {
			e.printStackTrace();
			log.info(e.getMessage());
			throw new WrongXMLFormatException(e);
		}

		return result;
	}

}
