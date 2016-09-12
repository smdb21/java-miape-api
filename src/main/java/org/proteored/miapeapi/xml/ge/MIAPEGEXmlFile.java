package org.proteored.miapeapi.xml.ge;

import java.io.File;
import java.io.IOException;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.exceptions.MiapeDatabaseException;
import org.proteored.miapeapi.exceptions.MiapeSecurityException;
import org.proteored.miapeapi.interfaces.ge.MiapeGEDocument;
import org.proteored.miapeapi.interfaces.persistence.PersistenceManager;
import org.proteored.miapeapi.interfaces.xml.MiapeXmlFile;
import org.proteored.miapeapi.spring.SpringHandler;

public class MIAPEGEXmlFile extends MiapeXmlFile<MiapeGEDocument> {

	private String userName;
	private String password;
	private PersistenceManager dbManager;
	private ControlVocabularyManager cvUtil;

	public MIAPEGEXmlFile(byte[] bytes) throws IOException {
		super(bytes);
	}

	public MIAPEGEXmlFile(File file) {
		super(file);
	}

	public MIAPEGEXmlFile(String name) {
		super(name);
	}

	public void initDefault() throws MiapeDatabaseException, MiapeSecurityException {
		this.userName = SpringHandler.getInstance().getUserName();
		this.password = SpringHandler.getInstance().getPassword();
		this.cvUtil = SpringHandler.getInstance().getCVManager();
	}

	public void setUserName(String userName) {
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
	public MiapeGEDocument toDocument() throws MiapeDatabaseException, MiapeSecurityException {
		return MiapeGEXmlFactory.getFactory().toDocument(this, cvUtil, dbManager, userName,
				password);
	}

}
