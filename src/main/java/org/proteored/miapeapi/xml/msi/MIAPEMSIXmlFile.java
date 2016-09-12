package org.proteored.miapeapi.xml.msi;

import java.io.File;
import java.io.IOException;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.exceptions.MiapeDatabaseException;
import org.proteored.miapeapi.exceptions.MiapeSecurityException;
import org.proteored.miapeapi.interfaces.msi.MiapeMSIDocument;
import org.proteored.miapeapi.interfaces.persistence.PersistenceManager;
import org.proteored.miapeapi.interfaces.xml.MiapeXmlFile;
import org.proteored.miapeapi.spring.SpringHandler;

public class MIAPEMSIXmlFile extends MiapeXmlFile<MiapeMSIDocument> {

	private String user;
	private String password;
	private PersistenceManager dbManager;
	private ControlVocabularyManager cvUtil;
	private final boolean processInParallel;

	public MIAPEMSIXmlFile(byte[] bytes) throws IOException {
		this(bytes, false);
	}

	public MIAPEMSIXmlFile(byte[] bytes, boolean processInParallel)
			throws IOException {
		super(bytes);
		this.processInParallel = processInParallel;
	}

	public MIAPEMSIXmlFile(File file) {
		this(file, false);
	}

	public MIAPEMSIXmlFile(File file, boolean processInParallel) {
		super(file);
		this.processInParallel = processInParallel;
	}

	public MIAPEMSIXmlFile(String name) {
		this(name, false);
	}

	public MIAPEMSIXmlFile(String name, boolean processInParallel) {
		super(name);
		this.processInParallel = processInParallel;
	}

	public void initDefault() throws MiapeDatabaseException,
			MiapeSecurityException {
		this.user = SpringHandler.getInstance().getUserName();
		this.password = SpringHandler.getInstance().getPassword();
		this.cvUtil = SpringHandler.getInstance().getCVManager();
	}

	public void setUserName(String user) {
		this.user = user;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setDbManager(PersistenceManager dbManager) {
		this.dbManager = dbManager;
	}

	public void setCvUtil(ControlVocabularyManager cvUtil) {
		this.cvUtil = cvUtil;
	}

	@Override
	public MiapeMSIDocument toDocument() throws MiapeDatabaseException,
			MiapeSecurityException {
		return MiapeMSIXmlFactory.getFactory(this.processInParallel)
				.toDocument(this, cvUtil, dbManager, user, password);
	}

}
