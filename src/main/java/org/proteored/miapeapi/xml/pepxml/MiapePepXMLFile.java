package org.proteored.miapeapi.xml.pepxml;

import java.io.File;
import java.io.IOException;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.exceptions.MiapeDatabaseException;
import org.proteored.miapeapi.exceptions.MiapeSecurityException;
import org.proteored.miapeapi.interfaces.msi.MiapeMSIDocument;
import org.proteored.miapeapi.interfaces.persistence.PersistenceManager;
import org.proteored.miapeapi.interfaces.xml.MiapeXmlFile;
import org.proteored.miapeapi.spring.SpringHandler;

public class MiapePepXMLFile extends MiapeXmlFile<MiapeMSIDocument> {
	private String userName;
	private String password;
	private String projectName;
	private PersistenceManager dbManager;
	private ControlVocabularyManager cvUtil;

	public MiapePepXMLFile(byte[] bytes) throws IOException {
		super(bytes);
	}

	public MiapePepXMLFile(File file) {
		super(file);
	}

	public MiapePepXMLFile(String fileName) {
		super(fileName);
	}

	public void initDefault() throws MiapeDatabaseException, MiapeSecurityException {
		userName = SpringHandler.getInstance().getUserName();
		password = SpringHandler.getInstance().getPassword();
		cvUtil = SpringHandler.getInstance().getCVManager();
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

	public void setCvManager(ControlVocabularyManager cvUtil) {
		this.cvUtil = cvUtil;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	@Override
	public MiapeMSIDocument toDocument() throws MiapeDatabaseException, MiapeSecurityException {
		// return MSIMiapeFactory.getFactory().toDocument(this, dbManager,
		// cvUtil, userName, password, projectName);
		return null;

	}

	// public DTASelectParser toPepXMLFile() throws SAXException,
	// MalformedURLException, IOException {
	// return new DTASelectParser(file.toURL());
	// }

}
