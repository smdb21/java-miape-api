package org.proteored.miapeapi.text.tsv;

import java.io.File;
import java.io.IOException;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.exceptions.MiapeDatabaseException;
import org.proteored.miapeapi.exceptions.MiapeSecurityException;
import org.proteored.miapeapi.interfaces.msi.MiapeMSIDocument;
import org.proteored.miapeapi.interfaces.persistence.PersistenceManager;
import org.proteored.miapeapi.interfaces.xml.MiapeXmlFile;
import org.proteored.miapeapi.spring.SpringHandler;
import org.proteored.miapeapi.text.tsv.msi.MSIMiapeFactory;
import org.proteored.miapeapi.text.tsv.msi.TableTextFileSeparator;

public class MiapeTSVFile extends MiapeXmlFile<MiapeMSIDocument> {
	private String userName;
	private String password;
	private String projectName;
	private final TableTextFileSeparator separator;
	private PersistenceManager dbManager;
	private ControlVocabularyManager cvUtil;

	public MiapeTSVFile(byte[] bytes, TableTextFileSeparator separator) throws IOException {
		super(bytes);
		this.separator = separator;
	}

	public MiapeTSVFile(File file, TableTextFileSeparator separator) {
		super(file);
		this.separator = separator;
	}

	public MiapeTSVFile(String fileName, TableTextFileSeparator separator) {
		super(fileName);
		this.separator = separator;
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

	public TableTextFileSeparator getSeparator() {
		return this.separator;
	}

}
