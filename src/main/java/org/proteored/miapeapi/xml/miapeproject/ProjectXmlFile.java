package org.proteored.miapeapi.xml.miapeproject;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.proteored.miapeapi.exceptions.MiapeDatabaseException;
import org.proteored.miapeapi.exceptions.MiapeSecurityException;
import org.proteored.miapeapi.interfaces.Project;
import org.proteored.miapeapi.interfaces.persistence.PersistenceManager;
import org.proteored.miapeapi.interfaces.xml.AbstractProjectXmlFile;
import org.proteored.miapeapi.spring.SpringHandler;
import org.proteored.miapeapi.xml.ProjectXmlFactory;

public class ProjectXmlFile extends AbstractProjectXmlFile {
	private String user;
	private String password;
	private PersistenceManager dbManager;

	public ProjectXmlFile(byte[] bytes) throws IOException {
		super(bytes);
	}

	public ProjectXmlFile(File file) {
		super(file);
	}

	public ProjectXmlFile(String name) {
		super(name);
	}

	public void initDefault() throws MiapeDatabaseException, MiapeSecurityException {
		this.user = SpringHandler.getInstance().getUserName();
		this.password = SpringHandler.getInstance().getPassword();
	}

	public void setUser(String user) {
		this.user = user;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setDbManager(PersistenceManager dbManager) {
		this.dbManager = dbManager;
	}

	@Override
	public Project toProject() throws MiapeDatabaseException, MiapeSecurityException, JAXBException {
		return ProjectXmlFactory.getFactory().toProject(this, dbManager, user, password);
	}

}
