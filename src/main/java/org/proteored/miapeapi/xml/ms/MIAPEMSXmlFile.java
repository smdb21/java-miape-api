package org.proteored.miapeapi.xml.ms;

import java.io.File;
import java.io.IOException;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.exceptions.MiapeDatabaseException;
import org.proteored.miapeapi.exceptions.MiapeSecurityException;
import org.proteored.miapeapi.interfaces.ms.MiapeMSDocument;
import org.proteored.miapeapi.interfaces.persistence.PersistenceManager;
import org.proteored.miapeapi.interfaces.xml.MiapeXmlFile;
import org.proteored.miapeapi.spring.SpringHandler;

public class MIAPEMSXmlFile extends MiapeXmlFile<MiapeMSDocument> {

	private String userName;
	private String password;
	private PersistenceManager dbManager;
	private ControlVocabularyManager cvUtil;

	public MIAPEMSXmlFile(byte[] bytes) throws IOException {
		super(bytes);
	}

	public MIAPEMSXmlFile(File file) {
		super(file);
	}

	public MIAPEMSXmlFile(String fileName) {
		super(fileName);
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
	public MiapeMSDocument toDocument() throws MiapeDatabaseException, MiapeSecurityException {
		return MiapeMSXmlFactory.getFactory().toDocument(this, cvUtil, dbManager, userName,
				password);
	}

}
