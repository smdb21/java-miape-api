package org.proteored.miapeapi.xml.pride;

import java.io.File;
import java.io.IOException;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.exceptions.MiapeDatabaseException;
import org.proteored.miapeapi.exceptions.MiapeSecurityException;
import org.proteored.miapeapi.interfaces.MiapeDocument;
import org.proteored.miapeapi.interfaces.ms.MiapeMSDocument;
import org.proteored.miapeapi.interfaces.msi.MiapeMSIDocument;
import org.proteored.miapeapi.interfaces.persistence.PersistenceManager;
import org.proteored.miapeapi.interfaces.xml.MiapePrideXmlFile;
import org.proteored.miapeapi.interfaces.xml.MiapeXmlFile;
import org.proteored.miapeapi.spring.SpringHandler;

/**
 * Class that represents a PRIDE file from a MIAPE MS and/or MSI documents
 * 
 * @author Salvador
 * 
 */
public class MiapeFullPrideXMLFile extends MiapeXmlFile<MiapeDocument> implements MiapePrideXmlFile {

	private String user;
	private String password;
	private PersistenceManager dbManager;
	private ControlVocabularyManager cvManager;
	private String projectName;

	public MiapeFullPrideXMLFile() {
		super();
	}

	public MiapeFullPrideXMLFile(File file) {
		super(file);
	}

	public MiapeFullPrideXMLFile(String name) {
		super(name);
	}

	public MiapeFullPrideXMLFile(byte[] bytes) throws IOException {
		super(bytes);
	}

	@Override
	public String getFileUrl() {
		return super.getPath();
	}

	@Override
	public File toFile() {
		return super.toFile();
	}

	/**
	 * Initialize the user, password, {@link ControlVocabularyManager} and
	 * {@link PersistenceManager} using the {@link SpringHandler}
	 * 
	 * @throws MiapeDatabaseException
	 * @throws MiapeSecurityException
	 */
	public void initDefault() throws MiapeDatabaseException, MiapeSecurityException {
		this.user = SpringHandler.getInstance().getUserName();
		this.password = SpringHandler.getInstance().getPassword();
		this.cvManager = SpringHandler.getInstance().getCVManager();
	}

	public void setUser(String user) {
		this.user = user;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setDbManager(PersistenceManager dbManager) {
		this.dbManager = dbManager;
	}

	public void setCVManager(ControlVocabularyManager cvManager) {
		this.cvManager = cvManager;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	@Override
	public MiapeMSDocument toMiapeMS() throws MiapeDatabaseException, MiapeSecurityException {
		return MSMiapeFactory.getFactory().toDocument(this, dbManager, cvManager, user, password,
				projectName);
	}

	@Override
	public MiapeMSIDocument toMiapeMSI() throws MiapeDatabaseException, MiapeSecurityException {
		return MSIMiapeFactory.getFactory()
				.create(this, dbManager, cvManager, user, password, null);
	}

	@Override
	public MiapeDocument toDocument() throws MiapeDatabaseException, MiapeSecurityException {
		throw new UnsupportedOperationException();
	}

}
