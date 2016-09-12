package org.proteored.miapeapi.xml.dtaselect;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.exceptions.MiapeDatabaseException;
import org.proteored.miapeapi.exceptions.MiapeSecurityException;
import org.proteored.miapeapi.interfaces.msi.MiapeMSIDocument;
import org.proteored.miapeapi.interfaces.persistence.PersistenceManager;
import org.proteored.miapeapi.interfaces.xml.MiapeXmlFile;
import org.proteored.miapeapi.spring.SpringHandler;
import org.proteored.miapeapi.xml.dtaselect.msi.MSIMiapeFactory;
import org.xml.sax.SAXException;

import edu.scripps.yates.dtaselectparser.DTASelectParser;

public class MiapeDTASelectFile extends MiapeXmlFile<MiapeMSIDocument> {
	private String userName;
	private String password;
	private String projectName;
	private PersistenceManager dbManager;
	private ControlVocabularyManager cvUtil;

	public MiapeDTASelectFile(byte[] bytes) throws IOException {
		super(bytes);
	}

	public MiapeDTASelectFile(File file) {
		super(file);
	}

	public MiapeDTASelectFile(String fileName) {
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

	public void setCvUtil(ControlVocabularyManager cvUtil) {
		this.cvUtil = cvUtil;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	@Override
	public MiapeMSIDocument toDocument() throws MiapeDatabaseException, MiapeSecurityException {
		return MSIMiapeFactory.getFactory().toDocument(this, dbManager, cvUtil, userName, password, projectName);

	}

	public DTASelectParser toDTASelectFile() throws SAXException, MalformedURLException, IOException {
		return new DTASelectParser(file.toURL());
	}

}
