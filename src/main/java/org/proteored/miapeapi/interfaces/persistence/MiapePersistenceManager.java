package org.proteored.miapeapi.interfaces.persistence;

import java.util.List;

import org.proteored.miapeapi.exceptions.MiapeDatabaseException;
import org.proteored.miapeapi.exceptions.MiapeSecurityException;
import org.proteored.miapeapi.exceptions.NoEntryRetrievedMiapeException;
import org.proteored.miapeapi.interfaces.Permission;
import org.proteored.miapeapi.interfaces.ge.MiapeGEDocument;
import org.proteored.miapeapi.interfaces.gi.MiapeGIDocument;
import org.proteored.miapeapi.interfaces.ms.MiapeMSDocument;
import org.proteored.miapeapi.interfaces.msi.MiapeMSIDocument;

/**
 * This interface defines which methods should have a MiapePersistenceManager
 * that manage the persistence of a MiapeDocument (that is, a {@link DBEntity}).
 * It is able to store, retrieve, and delete that {@link DBEntity}. The
 * {@link DBEntity} will be: {@link MiapeGEDocument}, {@link MiapeGIDocument},
 * {@link MiapeMSDocument} or {@link MiapeMSIDocument}
 * 
 * @author Salva
 * 
 * @param <T>
 *            a class that extends from {@link DBEntity}
 */
public interface MiapePersistenceManager<T extends DBEntity> {

	/**
	 * Stores a MIAPE document in the database
	 * 
	 * @param miapeDocument
	 *            : the MIAPE document
	 * @return the identifier of the document if stored in the database
	 * @throws MiapeDatabaseException
	 *             if any error occurs accessing to the database
	 * @throws MiapeSecurityException
	 *             if the owner of the document is not registered in the
	 *             database with these login data
	 */
	public int store(T miapeDocument) throws MiapeDatabaseException, MiapeSecurityException;

	/**
	 * Gets a MIAPE document from the database looking by its identifier
	 * 
	 * @param miapeId
	 * @param userName
	 * @param password
	 * @return the MIAPE document
	 * @throws MiapeDatabaseException
	 *             if any error occurs accessing to the database
	 * @throws MiapeSecurityException
	 *             if the userName and password are wrong
	 * @throws NoEntryRetrievedMiapeException
	 *             if there is not a MIAPE document in the database with that
	 *             identifier
	 */
	public T getMiapeById(int miapeId, String userName, String password) throws MiapeDatabaseException,
			MiapeSecurityException, NoEntryRetrievedMiapeException;

	/**
	 * Gets the MIAPE documents from the database that have a name containing a
	 * certain string
	 * 
	 * @param miapeDocumentName
	 * @param matchMode
	 *            EXACT or ANYWHERE (by default, EXACT)
	 * @param userName
	 * @param password
	 * @return a List of MIAPE documents
	 * @throws MiapeDatabaseException
	 *             if any error occurs accessing to the database
	 * @throws MiapeSecurityException
	 *             if the userName and password are wrong
	 * @throws NoEntryRetrievedMiapeException
	 *             if there is not a MIAPE document in the database with
	 *             containing that name
	 */
	public List<T> getMiapesByName(String miapeDocumentName, String matchMode, String userName, String password)
			throws MiapeDatabaseException, MiapeSecurityException, NoEntryRetrievedMiapeException;

	/**
	 * Gets a list of MIAPE documents that are available to a certain user
	 * (their own MIAPE documents plus the documents shared in his scope
	 * 
	 * @param userName
	 * @param password
	 * @return a list of MIAPE documents
	 * @throws MiapeDatabaseException
	 *             if any error occurs accessing to the database
	 * @throws MiapeSecurityException
	 *             if the userName and password are wrong
	 * @throws NoEntryRetrievedMiapeException
	 *             if there is not any MIAPE document in the database available
	 *             for the user
	 */
	public List<T> getMiapesOnScope(String userName, String password) throws MiapeDatabaseException,
			MiapeSecurityException, NoEntryRetrievedMiapeException;

	/**
	 * Delete a MIAPE document in the database looking by its identifier
	 * 
	 * @param miapeId
	 * @param userName
	 * @param password
	 * @throws MiapeDatabaseException
	 *             if any error occurs accessing to the database
	 * @throws MiapeSecurityException
	 *             if the userName and password are wrong
	 */
	public void deleteById(int miapeId, String userName, String password) throws MiapeDatabaseException,
			MiapeSecurityException;

	/**
	 * Delete a MIAPE document in the database looking that have a name
	 * containing a certain string. Note that if there is more than one MIAPE
	 * document containing that string a MiapeDatabaseException will be thrown
	 * 
	 * @param miapeDocumentName
	 * @param matchMode
	 *            EXACT or ANYWHERE (by default, EXACT)
	 * @param userName
	 * @param password
	 * @throws MiapeDatabaseException
	 *             if any error occurs accessing to the database, or if more
	 *             than one MIAPE document contains that string
	 * @throws MiapeSecurityException
	 *             if the userName and password are wrong
	 */
	public void deleteByName(String miapeDocumentName, String matchMode, String userName, String password)
			throws MiapeDatabaseException, MiapeSecurityException;

	/**
	 * Gets the permissions of a certain user over a MIAPE document
	 * 
	 * @param miapeId
	 * @param userName
	 * @param password
	 * @return a {@link Permission} object
	 * @throws MiapeDatabaseException
	 *             if any error occurs accessing to the database
	 * @throws MiapeSecurityException
	 *             if the userName and password are wrong
	 */
	public Permission getMiapePermissions(int miapeId, String userName, String password) throws MiapeDatabaseException,
			MiapeSecurityException;

}
