package org.proteored.miapeapi.xml.mzidentml;

import java.io.File;
import java.io.IOException;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.exceptions.MiapeDatabaseException;
import org.proteored.miapeapi.exceptions.MiapeSecurityException;
import org.proteored.miapeapi.interfaces.msi.MiapeMSIDocument;
import org.proteored.miapeapi.interfaces.persistence.PersistenceManager;
import org.proteored.miapeapi.interfaces.xml.MiapeXmlFile;
import org.proteored.miapeapi.spring.SpringHandler;

public class MiapeMzIdentMLFile extends MiapeXmlFile<MiapeMSIDocument> {
	private String userName;
	private String password;
	private PersistenceManager dbManager;
	private ControlVocabularyManager cvUtil;
	private String projectName;
	private boolean processInParallel;

	public MiapeMzIdentMLFile(byte[] bytes) throws IOException {
		super(bytes);
	}

	public MiapeMzIdentMLFile(File file) {
		super(file);
	}

	public MiapeMzIdentMLFile(String name) {
		super(name);
	}

	public MiapeMzIdentMLFile(String name, boolean processInParallel) {
		super(name);
		this.processInParallel = processInParallel;
	}

	public void initDefault() throws MiapeDatabaseException,
			MiapeSecurityException {
		this.userName = SpringHandler.getInstance().getUserName();
		this.password = SpringHandler.getInstance().getPassword();
		this.cvUtil = SpringHandler.getInstance().getCVManager();
	}

	public void setUser(String userName) {
		this.userName = userName;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setProcessInParallel(boolean processInParallel) {
		this.processInParallel = processInParallel;
	}

	public void setDbManager(PersistenceManager dbManager) {
		this.dbManager = dbManager;
	}

	public void setCvUtil(ControlVocabularyManager cvUtil) {
		this.cvUtil = cvUtil;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	@Override
	public MiapeMSIDocument toDocument() throws MiapeDatabaseException,
			MiapeSecurityException {
		return MSIMiapeFactory.getFactory().toDocument(this, dbManager, cvUtil,
				userName, password, projectName, processInParallel);

	}

}
