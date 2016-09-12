package org.proteored.miapeapi.interfaces.persistence;

import java.util.List;

import org.proteored.miapeapi.exceptions.MiapeDatabaseException;
import org.proteored.miapeapi.exceptions.MiapeSecurityException;
import org.proteored.miapeapi.exceptions.NoEntryRetrievedMiapeException;
import org.proteored.miapeapi.interfaces.Permission;
import org.proteored.miapeapi.interfaces.Project;
import org.proteored.miapeapi.interfaces.User;
import org.proteored.miapeapi.interfaces.ge.MiapeGEDocument;
import org.proteored.miapeapi.interfaces.gi.MiapeGIDocument;
import org.proteored.miapeapi.interfaces.ms.MiapeMSDocument;
import org.proteored.miapeapi.interfaces.msi.MiapeMSIDocument;

/**
 * Interface that defines the methods of a persistence manager, whatever it was.
 * These methods will be able to retrieve, store and delete MIAPE documents,
 * MIAPE projects and Users.
 * 
 * @author Salvador
 * 
 */
public interface PersistenceManager {

	// User related functions
	/**
	 * Gets a {@link User} from the database by user name and password
	 * 
	 * @param userName
	 * @param password
	 * @return the User object
	 * @throws MiapeDatabaseException
	 *             if any error occurs accessing to the database
	 * @throws MiapeSecurityException
	 *             if the userName and password are wrong
	 */
	public User getUser(final String userName, final String password)
			throws MiapeDatabaseException, MiapeSecurityException;

	/**
	 * Gets a {@link User} from the database by its identifier and its password
	 * 
	 * @param userID
	 * @param password
	 * @return the User object
	 * @throws MiapeDatabaseException
	 *             if any error occurs accessing to the database
	 * @throws MiapeSecurityException
	 *             if the password is wrong
	 */
	public User getUser(final int userID, final String password)
			throws MiapeDatabaseException, MiapeSecurityException;

	/**
	 * Stores a {@link User} in the database
	 * 
	 * @param user
	 * @return the identifier of the user stored in the database
	 * @throws MiapeDatabaseException
	 *             if any error occurs in the database or if the user name or
	 *             password in the User object are empty or null
	 * @throws MiapeSecurityException
	 *             if already a user exists in the database with the same user
	 *             name
	 */
	public int saveUser(final User user) throws MiapeDatabaseException,
			MiapeSecurityException;

	/**
	 * Check if a user name and password corresponds with a certain user in the
	 * database
	 * 
	 * @param userName
	 * @param password
	 * @throws MiapeDatabaseException
	 *             if any error occurs in the database.
	 * @throws MiapeSecurityException
	 *             if the user name or password are wrong.
	 */
	public void checkUserCredentials(final String userName,
			final String password) throws MiapeDatabaseException,
			MiapeSecurityException;

	// Project related functions

	/**
	 * Gets a list of MIAPE projects available for the user
	 * 
	 * @param userName
	 * @param password
	 * @return the list of MIAPE projects
	 * @throws MiapeDatabaseException
	 *             if any error occurs in the database
	 * @throws MiapeSecurityException
	 *             if user name or password are wrong
	 * @throws NoEntryRetrievedMiapeException
	 *             if there is not any project available for the user
	 */
	public List<Project> getProjectsOnScope(final String userName,
			final String password) throws MiapeDatabaseException,
			MiapeSecurityException, NoEntryRetrievedMiapeException;

	/**
	 * Gets a list of MIAPE projects looking by project name
	 * 
	 * @param projectName
	 * @param matchMode
	 *            EXACT or ANYWHERE
	 * @param userName
	 * @param password
	 * @return the list of MIAPE projects
	 * @throws MiapeDatabaseException
	 *             if any error occurs in the database
	 * @throws MiapeSecurityException
	 *             if user name or password are wrong
	 * @throws NoEntryRetrievedMiapeException
	 *             if there is not any project available for the user
	 */
	public List<Project> getProjectsByName(final String projectName,
			String matchMode, final String userName, String password)
			throws MiapeDatabaseException, MiapeSecurityException,
			NoEntryRetrievedMiapeException;

	/**
	 * Gets a MIAPE project from the database looking by its identifier
	 * 
	 * @param projectID
	 * @param userName
	 * @param password
	 * @return the list of MIAPE projects
	 * @throws MiapeDatabaseException
	 *             if any error occurs in the database
	 * @throws MiapeSecurityException
	 *             if user name or password are wrong
	 * @throws NoEntryRetrievedMiapeException
	 *             if there is not any project available for the user
	 */
	public Project getProjectById(final int projectID, final String userName,
			final String password) throws MiapeDatabaseException,
			MiapeSecurityException, NoEntryRetrievedMiapeException;

	/**
	 * Store a project in the database
	 * 
	 * @param project
	 * @return the project identifier in the database
	 * @throws MiapeDatabaseException
	 *             if any error occurs in the database
	 * @throws MiapeSecurityException
	 *             if the owner of the project is not registered in the database
	 */
	public int saveProject(final Project project)
			throws MiapeDatabaseException, MiapeSecurityException;

	/**
	 * Gets the permissions of a certain user over a MIAPE project
	 * 
	 * @param idProject
	 * @param userName
	 * @param password
	 * @return a {@link Permission} object
	 * @throws MiapeDatabaseException
	 *             if any error occurs in the database
	 * @throws MiapeSecurityException
	 *             if the user name or password are wrong
	 */
	public Permission getProjectPermissions(int idProject, String userName,
			String password) throws MiapeDatabaseException,
			MiapeSecurityException;

	/**
	 * Delete a project in the database by its identifier
	 * 
	 * @param projectID
	 * @param userName
	 * @param password
	 * @throws MiapeDatabaseException
	 *             if nay error occurs in the database or if there is not any
	 *             project with that identifier
	 * @throws MiapeSecurityException
	 *             if the user name and/or password are wrong or if the user has
	 *             not enough permissions to delete the project
	 */
	public void deleteProjectById(final int projectID, final String userName,
			final String password) throws MiapeDatabaseException,
			MiapeSecurityException;

	/**
	 * Delete a project in the database by its name. Note that if there is more
	 * than one project containing that name, a MiapeDatabaseException will be
	 * thrown.
	 * 
	 * @param projectName
	 * @param matchMode
	 *            EXACT or ANYWHERE
	 * @param userName
	 * @param password
	 * @throws MiapeDatabaseException
	 *             if nay error occurs in the database or if there is not any
	 *             project with that name or if there is more than one project
	 *             with that name
	 * @throws MiapeSecurityException
	 *             if the user name and/or password are wrong or if the user has
	 *             not enough permissions to delete the project
	 */
	public void deleteProjectByName(final String projectName, String matchMode,
			final String userName, final String password)
			throws MiapeDatabaseException, MiapeSecurityException;

	/**
	 * Gets the persistence manager for MIAPE MS documents
	 * 
	 * @return the persistence manager
	 */
	public MiapePersistenceManager<MiapeMSDocument> getMiapeMSPersistenceManager();

	/**
	 * Gets the persistence manager for MIAPE MSI documents
	 * 
	 * @return the persistence manager
	 */
	public MiapePersistenceManager<MiapeMSIDocument> getMiapeMSIPersistenceManager();

	/**
	 * Gets the persistence manager for MIAPE GE documents
	 * 
	 * @return the persistence manager
	 */
	public MiapePersistenceManager<MiapeGEDocument> getMiapeGEPersistenceManager();

	/**
	 * Gets the persistence manager for MIAPE GI documents
	 * 
	 * @return the persistence manager
	 */
	public MiapePersistenceManager<MiapeGIDocument> getMiapeGIPersistenceManager();

}
