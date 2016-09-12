package org.proteored.miapeapi.factories;

import org.proteored.miapeapi.exceptions.MiapeDatabaseException;
import org.proteored.miapeapi.exceptions.MiapeSecurityException;
import org.proteored.miapeapi.interfaces.MiapeDate;
import org.proteored.miapeapi.interfaces.Project;
import org.proteored.miapeapi.interfaces.User;
import org.proteored.miapeapi.interfaces.persistence.PersistenceManager;

public class ProjectImpl implements Project {
	private int id = -1;
	private final String comments;
	private final MiapeDate date;
	private final String name;
	private final User owner;
	private final PersistenceManager dbManager;

	@SuppressWarnings("unused")
	private ProjectImpl() {
		this(null);
	}
	public ProjectImpl(ProjectBuilder projectBuilder) {
		this.comments = projectBuilder.comments;
		this.date = projectBuilder.date;
		this.name = projectBuilder.name;
		this.owner = projectBuilder.owner;	
		this.dbManager = projectBuilder.dbManager;
	}

	@Override
	public String getComments() {
		return comments;
	}

	@Override
	public MiapeDate getDate() {
		return date;
	}

	@Override
	public String getName() {
		return name;
	}
	@Override
	public User getOwner() {
		return owner;
	}
	@Override
	public int getId() {
		return id;
	}
	@Override
	public int store()	throws MiapeDatabaseException, MiapeSecurityException {
		if (dbManager != null) {
			return id = dbManager.saveProject(this);
		} else {
			throw new MiapeDatabaseException("The persistance is not Configured");
		}
	}
	@Override
	public void delete(String userName, String password)
	throws MiapeDatabaseException, MiapeSecurityException {
		if (dbManager != null) {
			dbManager.deleteProjectById(this.getId(), userName, password);
		} else {
			throw new MiapeDatabaseException("The persistance is not Configured");
		}

	}

}
