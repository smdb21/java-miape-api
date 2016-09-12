package org.proteored.miapeapi.interfaces.xml;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.exceptions.IllegalMiapeArgumentException;
import org.proteored.miapeapi.exceptions.MiapeDatabaseException;
import org.proteored.miapeapi.exceptions.MiapeSecurityException;
import org.proteored.miapeapi.interfaces.MiapeDocument;
import org.proteored.miapeapi.interfaces.persistence.PersistenceManager;

/**
 * Interface that defines the methods that a XmlMiapeFactory must have, that is,
 * the toXml and toDocument methods.
 * 
 * @author Salva
 * 
 * @param <T>
 *            a class that extends from {@link MiapeDocument}
 */
public interface XmlMiapeFactory<T extends MiapeDocument> {
	/**
	 * Function that converts a MIAPE document to an XML file
	 * 
	 * @param document
	 * @param cvManager
	 * @return the XML file
	 */
	public MiapeXmlFile<T> toXml(T document, ControlVocabularyManager cvManager)
			throws IllegalMiapeArgumentException;

	/**
	 * Function that converts a XML file to a MIAPE document
	 * 
	 * @param xmlFile
	 *            the input file
	 * @param cvManager
	 * @param db
	 *            database manager if available
	 * @param userName
	 * @param password
	 * @return the MIAPE document
	 * @throws MiapeDatabaseException
	 * @throws MiapeSecurityException
	 */
	public T toDocument(MiapeXmlFile<T> xmlFile, ControlVocabularyManager cvManager,
			PersistenceManager db, String userName, String password) throws MiapeDatabaseException,
			MiapeSecurityException, IllegalMiapeArgumentException;

}
