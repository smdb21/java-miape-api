package org.proteored.miapeapi.factories;

import org.proteored.miapeapi.interfaces.MiapeDate;
import org.proteored.miapeapi.interfaces.Project;
import org.proteored.miapeapi.interfaces.User;
import org.proteored.miapeapi.interfaces.persistence.PersistenceManager;

public class ProjectBuilder {
	// Required parameters
	final String name;

	String comments;
	MiapeDate date;
	User owner;

	PersistenceManager dbManager;

	ProjectBuilder(String name, User owner, PersistenceManager dbManager) {
		this.dbManager = dbManager;
		this.owner = owner;
		this.name = name;
	}

	ProjectBuilder(String name) {
		this.name = name;

	}

	public ProjectBuilder comments(String value) {
		this.comments = value;
		return this;
	}

	public ProjectBuilder date(MiapeDate value) {
		this.date = value;
		return this;
	}

	public ProjectBuilder owner(User value) {
		this.owner = value;
		return this;
	}

	public Project build() {
		return new ProjectImpl(this);
	}
}