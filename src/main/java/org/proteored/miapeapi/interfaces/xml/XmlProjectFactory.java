package org.proteored.miapeapi.interfaces.xml;

import javax.xml.bind.JAXBException;

import org.proteored.miapeapi.exceptions.MiapeDatabaseException;
import org.proteored.miapeapi.exceptions.MiapeSecurityException;
import org.proteored.miapeapi.interfaces.Project;
import org.proteored.miapeapi.interfaces.persistence.PersistenceManager;

public interface XmlProjectFactory {
	/**
	 * Function that converts a Project to a XML file
	 * 
	 * @param project
	 * @return an XML file representing the project
	 */
	public AbstractProjectXmlFile toXml(Project project);

	/**
	 * Function that converts a XML file representing the MIAPE project in a
	 * Project object
	 * 
	 * @param xmlFile
	 * @param db
	 *            is needed in order to set the owner to the project checking
	 *            the username and password in the database. If db is null, the
	 *            owner will be null.
	 * @param userName
	 * @param password
	 * @return the Project object
	 * @throws MiapeDatabaseException
	 * @throws MiapeSecurityException
	 * @throws JAXBException
	 */
	public Project toProject(AbstractProjectXmlFile xmlFile, PersistenceManager db, String userName, String password)
			throws MiapeDatabaseException, MiapeSecurityException, JAXBException;

}
