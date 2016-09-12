package org.proteored.miapeapi.xml.gi;

import java.io.File;
import java.io.IOException;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.exceptions.MiapeDatabaseException;
import org.proteored.miapeapi.exceptions.MiapeSecurityException;
import org.proteored.miapeapi.interfaces.gi.MiapeGIDocument;
import org.proteored.miapeapi.interfaces.persistence.PersistenceManager;
import org.proteored.miapeapi.interfaces.xml.MiapeXmlFile;
import org.proteored.miapeapi.spring.SpringHandler;

public class MIAPEGIXmlFile extends MiapeXmlFile<MiapeGIDocument> {

	private String userName;
	private String password;
	private PersistenceManager dbManager;
	private ControlVocabularyManager cvUtil;

	public MIAPEGIXmlFile(byte[] bytes) throws IOException {
		super(bytes);
	}

	public MIAPEGIXmlFile(File file) {
		super(file);
	}

	public MIAPEGIXmlFile(String fileName) {
		super(fileName);
	}

	public void initDefault() throws MiapeDatabaseException, MiapeSecurityException {
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

	public void setDbManager(PersistenceManager dbManager) {
		this.dbManager = dbManager;
	}

	public void setCvUtil(ControlVocabularyManager cvUtil) {
		this.cvUtil = cvUtil;
	}

	@Override
	public MiapeGIDocument toDocument() throws MiapeDatabaseException, MiapeSecurityException {
		return MiapeGIXmlFactory.getFactory().toDocument(this, cvUtil, dbManager, userName,
				password);
	}

}
