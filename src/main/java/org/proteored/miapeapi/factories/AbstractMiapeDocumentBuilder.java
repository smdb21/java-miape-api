package org.proteored.miapeapi.factories;

import java.util.Date;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.interfaces.MiapeDate;
import org.proteored.miapeapi.interfaces.MiapeDocument;
import org.proteored.miapeapi.interfaces.Project;
import org.proteored.miapeapi.interfaces.User;
import org.proteored.miapeapi.interfaces.persistence.PersistenceManager;
import org.proteored.miapeapi.interfaces.xml.XmlManager;
import org.proteored.miapeapi.spring.SpringHandler;

public abstract class AbstractMiapeDocumentBuilder {
	// Required parameters
	protected final Project project;
	protected String name;
	protected final User owner;

	protected PersistenceManager dbManager;
	protected XmlManager xmlManager;
	protected MiapeDate date;
	protected Date modificationDate;
	protected String prideUrl;
	protected Boolean template;
	protected String version;
	protected ControlVocabularyManager cvUtil;

	/**
	 * Create a AbstractMiapeDocumentBuilder
	 * 
	 * @param project
	 * @param name
	 * @param owner
	 */
	protected AbstractMiapeDocumentBuilder(Project project, String name,
			User owner, PersistenceManager db) {
		this.project = project;
		this.name = name;
		this.owner = owner;
		this.dbManager = db;
		this.xmlManager = SpringHandler.getInstance().getXmlManager();
		this.cvUtil = SpringHandler.getInstance().getCVManager();
	}

	/**
	 * 
	 * @param project
	 * @param name
	 * @param owner
	 * @param db
	 * @param xmlManager
	 * @param cvUtil
	 */
	protected AbstractMiapeDocumentBuilder(Project project, String name,
			User owner, PersistenceManager db, XmlManager xmlManager,
			ControlVocabularyManager cvUtil) {
		this.project = project;
		this.name = name;
		this.owner = owner;
		this.dbManager = db;
		this.xmlManager = xmlManager;
		this.cvUtil = cvUtil;
	}

	protected AbstractMiapeDocumentBuilder(Project project, String name,
			User owner) {
		this.project = project;
		this.name = name;
		this.xmlManager = SpringHandler.getInstance().getXmlManager();
		this.owner = owner;
		this.cvUtil = SpringHandler.getInstance().getCVManager();
	}

	public AbstractMiapeDocumentBuilder date(MiapeDate value) {
		date = value;
		return this;
	}

	public AbstractMiapeDocumentBuilder modificationDate(Date value) {
		modificationDate = value;
		return this;
	}

	public AbstractMiapeDocumentBuilder prideUrl(String value) {
		prideUrl = value;
		return this;
	}

	public AbstractMiapeDocumentBuilder template(Boolean value) {
		template = value;
		return this;
	}

	public AbstractMiapeDocumentBuilder version(String value) {
		version = value;
		return this;
	}

	public AbstractMiapeDocumentBuilder cvManager(
			ControlVocabularyManager cvManager) {
		this.cvUtil = cvManager;
		return this;
	}

	public AbstractMiapeDocumentBuilder dbManager(PersistenceManager dbManager) {
		this.dbManager = dbManager;
		return this;
	}

	public abstract MiapeDocument build();
}