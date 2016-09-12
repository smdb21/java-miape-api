package org.proteored.miapeapi.interfaces.persistence;

import org.proteored.miapeapi.exceptions.MiapeDatabaseException;
import org.proteored.miapeapi.exceptions.MiapeSecurityException;
import org.proteored.miapeapi.interfaces.ge.MiapeGEDocument;
import org.proteored.miapeapi.interfaces.gi.MiapeGIDocument;
import org.proteored.miapeapi.interfaces.ms.MiapeMSDocument;
import org.proteored.miapeapi.interfaces.msi.MiapeMSIDocument;

/**
 * A interface that defines the methods that a DBEntity must have. They are
 * 'store' and 'delete'. {@link MiapeGEDocument}, {@link MiapeGIDocument},
 * {@link MiapeMSDocument} and {@link MiapeMSIDocument} will extend from this
 * interface
 * 
 * @author Salva
 * 
 */
public interface DBEntity {

	public int store() throws MiapeDatabaseException, MiapeSecurityException;

	public void delete(final String userName, final String password) throws MiapeDatabaseException,
			MiapeSecurityException;
}
