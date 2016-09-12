package org.proteored.miapeapi.spring;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.exceptions.IllegalMiapeArgumentException;
import org.proteored.miapeapi.exceptions.MiapeDatabaseException;
import org.proteored.miapeapi.exceptions.MiapeSecurityException;
import org.proteored.miapeapi.interfaces.User;
import org.proteored.miapeapi.interfaces.persistence.PersistenceManager;
import org.proteored.miapeapi.interfaces.xml.XmlManager;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;

/**
 * Reads miape-api.properties file and spring.xml file in order to load some
 * classes using Spring
 * 
 * @author Salva
 * 
 */
public class SpringHandler {
	private static SpringHandler instance;

	protected final XmlBeanFactory factory;
	private final String userName;
	private final String password;

	private static ControlVocabularyManager cvManager;
	private static XmlManager xmlManager;
	private static PersistenceManager persistenceManager;

	private static Logger log = Logger.getLogger("log4j.logger.org.proteored");
	private static final String SPRING_FILE = "spring.xml";
	private static final String SPRING_FILE_TEST = "spring-test.xml";
	private static final String PROPERTIES_FILE = "miape-api.properties";
	private static final String PROPERTIES_FILE_TEST = "miape-api-test.properties";

	private SpringHandler(String springFileName, String propertiesFileName) {

		ClassPathResource propertiesFile;
		ClassPathResource springXML;

		springXML = new ClassPathResource(springFileName);
		try {
			log.info("Using spring file: " + springFileName + " "
					+ springXML.getInputStream());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		propertiesFile = new ClassPathResource(propertiesFileName);
		log.info("Using properties file: " + propertiesFileName + " "
				+ propertiesFile);
		if (springXML.exists() == false) {
			log.info("The spring file is missing");
			throw new IllegalMiapeArgumentException(
					"The spring file is missing");
		}
		if (propertiesFile.exists() == false) {
			log.info("The properties file is missing");
			throw new IllegalMiapeArgumentException(
					"The properties file is missing");
		}
		factory = new XmlBeanFactory(springXML);
		log.info("XmlBean factory: " + factory);
		PropertyPlaceholderConfigurer cfg = new PropertyPlaceholderConfigurer();

		Properties properties = new Properties();
		try {
			properties.load(propertiesFile.getInputStream());
		} catch (IOException e) {
			throw new IllegalMiapeArgumentException(e.getMessage());
		}
		userName = properties.getProperty("miape.user");
		password = properties.getProperty("miape.password");

		cfg.setLocation(propertiesFile);
		cfg.postProcessBeanFactory(factory);
		// try {
		// log.info("looking for a databasemanager");
		// if (factory.containsBean("databaseManager")) {
		// log.info("getting databasemanager from bean");
		// dbManager = (PersistenceManager) factory.getBean("databaseManager");
		// } else {
		// log.info("there is not a configured databasemanager");
		// dbManager = null;
		// }
		//
		// log.info("looking for a xmlmanager");
		// if (factory.containsBean("xmlManager")) {
		// log.info("getting xmlmanager from bean");
		// xmlManager = (XmlManager) factory.getBean("xmlManager");
		// } else {
		// log.info("there is not a configured xmlmanager");
		// xmlManager = null;
		// }
		//
		// log.info("looking for a cvManager");
		// if (factory.containsBean("cvManager")) {
		// log.info("getting cvManager from bean");
		// cvManager = (ControlVocabularyManager) factory.getBean("cvManager");
		// } else {
		// log.info("there is not a configured cvManager");
		// cvManager = null;
		// }
		//
		// } catch (BeansException e) {
		// throw new IllegalMiapeArgumentException(e);
		// }

	}

	public static synchronized SpringHandler getInstance() {
		if (instance == null) {
			// if (isServerTest()) {
			// log.info("SERVER_TEST = TRUE -> using miape-api-test.properties");
			// log.info("SERVER_TEST = TRUE -> using " + SPRING_FILE_TEST);
			// instance = new SpringHandler(SPRING_FILE_TEST,
			// PROPERTIES_FILE_TEST);
			// } else {
			// log.info("SERVER_TEST = FALSE -> using miape-api.properties");
			// log.info("SERVER_TEST = FALSE -> using " + SPRING_FILE);
			instance = new SpringHandler(SPRING_FILE, PROPERTIES_FILE);
			// }
		}
		return instance;
	}

	public static synchronized SpringHandler getInstance(String springFile) {
		if (instance == null) {
			instance = new SpringHandler(springFile, PROPERTIES_FILE);
		}
		return instance;
	}

	public PersistenceManager getDBManager() {
		try {
			if (SpringHandler.persistenceManager != null)
				return SpringHandler.persistenceManager;
			log.info("looking for a databasemanager");
			if (factory.containsBean("databaseManager")) {
				log.info("getting databasemanager from bean");
				SpringHandler.persistenceManager = (PersistenceManager) factory
						.getBean("databaseManager");
			} else {
				log.info("there is not a configured databasemanager");
				SpringHandler.persistenceManager = null;
			}
			return SpringHandler.persistenceManager;
		} catch (BeansException e) {
			throw new IllegalMiapeArgumentException(e);
		}
		// if (dbManager == null) {
		// throw new
		// IllegalMiapeArgumentException("The database manager is not properly set");
		// }
		// return dbManager;
	}

	public XmlManager getXmlManager() {
		try {
			if (SpringHandler.xmlManager != null)
				return SpringHandler.xmlManager;

			log.info("looking for a xmlmanager");
			if (factory.containsBean("xmlManager")) {
				log.info("getting xmlmanager from bean");
				SpringHandler.xmlManager = (XmlManager) factory
						.getBean("xmlManager");
			} else {
				log.info("there is not a configured xmlmanager");
				SpringHandler.xmlManager = null;
			}
			return SpringHandler.xmlManager;
		} catch (BeansException e) {
			throw new IllegalMiapeArgumentException(e);
		}
		// if (xmlManager == null) {
		// throw new
		// IllegalMiapeArgumentException("The xml manager is not properly set");
		// }
		// return xmlManager;
	}

	public User getUser() throws MiapeDatabaseException, MiapeSecurityException {
		return getDBManager().getUser(userName, password);
	}

	public String getPassword() {
		return password;
	}

	public String getUserName() {
		return userName;
	}

	public ControlVocabularyManager getCVManager() {
		try {
			log.info("looking for a cvManager");
			if (SpringHandler.cvManager != null)
				return SpringHandler.cvManager;

			if (factory.containsBean("cvManager")) {
				log.info("getting cvManager from bean");
				SpringHandler.cvManager = (ControlVocabularyManager) factory
						.getBean("cvManager");

			} else {
				log.info("there is not a configured cvManager");
				SpringHandler.cvManager = null;
			}
			return SpringHandler.cvManager;
		} catch (BeansException e) {
			throw new IllegalMiapeArgumentException(e);
		}
		// if (cvManager == null) {
		// throw new IllegalMiapeArgumentException(
		// "The Control Vocabulary manager is not properly set");
		// }
		// return cvManager;
	}

	private static boolean isServerTest() {
		log.info("Checking environment variable SERVER_TEST in SpringHandler");

		Map<String, String> env = System.getenv();
		ClassPathResource propertiesFile;
		ClassPathResource springXML;
		if (env.get("SERVER_TEST") != null
				&& env.get("SERVER_TEST").equals("true")) {
			return true;
		} else {
			return false;
		}
	}
}
