package org.proteored.miapeapi.xml.xtandem;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.exceptions.MiapeDatabaseException;
import org.proteored.miapeapi.exceptions.MiapeSecurityException;
import org.proteored.miapeapi.interfaces.msi.MiapeMSIDocument;
import org.proteored.miapeapi.interfaces.persistence.PersistenceManager;
import org.proteored.miapeapi.interfaces.xml.MiapeXmlFile;
import org.proteored.miapeapi.spring.SpringHandler;
import org.proteored.miapeapi.xml.xtandem.msi.MSIMiapeFactory;
import org.xml.sax.SAXException;

import de.proteinms.xtandemparser.xtandem.XTandemFile;

public class MiapeXTandemFile extends MiapeXmlFile<MiapeMSIDocument> {
	private String userName;
	private String password;
	private String projectName;
	private PersistenceManager dbManager;
	private ControlVocabularyManager cvUtil;

	public MiapeXTandemFile(byte[] bytes) throws IOException {
		super(bytes);
	}

	public MiapeXTandemFile(File file) {
		super(file);
	}

	public MiapeXTandemFile(String fileName) {
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

	public void setCvManager(ControlVocabularyManager cvUtil) {
		this.cvUtil = cvUtil;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	@Override
	public MiapeMSIDocument toDocument() throws MiapeDatabaseException, MiapeSecurityException {
		return MSIMiapeFactory.getFactory().toDocument(this, dbManager, cvUtil, userName, password, projectName);

	}

	public XTandemFile toXTandemFile() throws ParserConfigurationException, SAXException {
		return new XTandemFile(this.getPath());
	}

}
