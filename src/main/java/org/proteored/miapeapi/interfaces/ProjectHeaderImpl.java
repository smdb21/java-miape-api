package org.proteored.miapeapi.interfaces;

import org.proteored.miapeapi.exceptions.MiapeDatabaseException;
import org.proteored.miapeapi.exceptions.MiapeSecurityException;

public class ProjectHeaderImpl implements Project {
	private final int idProject;
	private final String projectName;

	public ProjectHeaderImpl(int idProject, String projectName) {
		this.idProject = idProject;
		this.projectName = projectName;
	}

	@Override
	public int getId() {
		return idProject;
	}

	@Override
	public String getName() {
		return projectName;
	}

	@Override
	public MiapeDate getDate() {
		return null;
	}

	@Override
	public String getComments() {
		return null;
	}

	@Override
	public User getOwner() {
		return null;
	}

	@Override
	public int store()
	throws MiapeDatabaseException, MiapeSecurityException {
		return -1;
	}

	@Override
	public void delete(String user, String password)
	throws MiapeDatabaseException, MiapeSecurityException {		
	}

}
