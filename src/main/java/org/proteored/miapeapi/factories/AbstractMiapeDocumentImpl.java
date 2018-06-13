package org.proteored.miapeapi.factories;

import java.util.Date;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.exceptions.MiapeDatabaseException;
import org.proteored.miapeapi.exceptions.MiapeSecurityException;
import org.proteored.miapeapi.interfaces.MiapeDate;
import org.proteored.miapeapi.interfaces.MiapeDocument;
import org.proteored.miapeapi.interfaces.Project;
import org.proteored.miapeapi.interfaces.User;
import org.proteored.miapeapi.interfaces.persistence.PersistenceManager;
import org.proteored.miapeapi.interfaces.xml.XmlManager;

public abstract class AbstractMiapeDocumentImpl implements MiapeDocument {

	protected final MiapeDate date;
	protected int id = -1;
	protected final User owner;
	protected final Date modificationDate;
	protected final String name;
	protected final String prideUrl;
	protected final Project project;
	protected final Boolean template;
	protected final String version;
	protected final PersistenceManager dbManager;
	protected final XmlManager xmlManager;
	protected final ControlVocabularyManager cvUtil;

	public AbstractMiapeDocumentImpl(AbstractMiapeDocumentBuilder builder) {
		date = builder.date;
		owner = builder.owner;
		modificationDate = builder.modificationDate;
		name = builder.name;
		prideUrl = builder.prideUrl;
		project = builder.project;
		template = builder.template;
		version = builder.version;
		dbManager = builder.dbManager;
		xmlManager = builder.xmlManager;
		cvUtil = builder.cvUtil;
		id = builder.id;
	}

	@Override
	public MiapeDate getDate() {
		return date;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public Date getModificationDate() {
		return modificationDate;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getAttachedFileLocation() {
		return prideUrl;
	}

	@Override
	public Project getProject() {
		return project;
	}

	@Override
	public Boolean getTemplate() {
		return template;
	}

	@Override
	public String getVersion() {
		return version;
	}

	@Override
	public abstract int store() throws MiapeDatabaseException, MiapeSecurityException;

	@Override
	public abstract void delete(String user, String password) throws MiapeDatabaseException, MiapeSecurityException;

	@Override
	public User getOwner() {
		return owner;
	}

}
