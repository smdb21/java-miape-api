package org.proteored.miapeapi.xml.gi;

import org.proteored.miapeapi.exceptions.MiapeDatabaseException;
import org.proteored.miapeapi.exceptions.MiapeSecurityException;
import org.proteored.miapeapi.interfaces.MiapeDate;
import org.proteored.miapeapi.interfaces.Project;
import org.proteored.miapeapi.interfaces.User;
import org.proteored.miapeapi.interfaces.persistence.PersistenceManager;
import org.proteored.miapeapi.xml.gi.autogenerated.MIAPEProject;

public class ProjectImpl implements Project {
	private final MIAPEProject xmlProject;
	private final User owner;
	private final PersistenceManager dbManager;
	private int id = -1;

	public ProjectImpl(MIAPEProject xmlProject, User owner, PersistenceManager dbManager) {
		this.xmlProject = xmlProject;
		this.owner = new OwnerImpl(owner, xmlProject.getOwnerName());
		this.dbManager = dbManager;

	}

	@Override
	public String getComments() {
		return xmlProject.getComments();
	}

	@Override
	public MiapeDate getDate() {
		return new MiapeDate(xmlProject.getDate());
	}

	@Override
	public String getName() {
		return xmlProject.getName();
	}

	@Override
	public User getOwner() {
		return owner;
	}

	@Override
	public int getId() {
		if (xmlProject.getId() != null) {
			try {
				return Integer.valueOf(xmlProject.getId());
			} catch (Exception e) {
				// do nothing
			}
		}
		return id;
	}

	@Override
	public int store() throws MiapeDatabaseException, MiapeSecurityException {
		if (this.dbManager != null) {
			id = dbManager.saveProject(this);
		}
		throw new MiapeDatabaseException("The persistance method is not defined.");
	}

	@Override
	public void delete(String userName, String password) throws MiapeDatabaseException, MiapeSecurityException {
		if (this.dbManager != null) {
			dbManager.deleteProjectById(this.getId(), userName, password);
		}
		throw new MiapeDatabaseException("The persistance method is not defined.");

	}

}
