package org.proteored.miapeapi.xml.pride;

import java.io.File;
import java.io.IOException;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.exceptions.MiapeDatabaseException;
import org.proteored.miapeapi.exceptions.MiapeSecurityException;
import org.proteored.miapeapi.interfaces.ms.MiapeMSDocument;
import org.proteored.miapeapi.interfaces.msi.MiapeMSIDocument;
import org.proteored.miapeapi.interfaces.persistence.PersistenceManager;
import org.proteored.miapeapi.interfaces.xml.MiapePrideXmlFile;
import org.proteored.miapeapi.interfaces.xml.MiapeXmlFile;
import org.proteored.miapeapi.spring.SpringHandler;

public class MiapeMsiPrideXmlFile extends MiapeXmlFile<MiapeMSIDocument> implements
		MiapePrideXmlFile {

	private String user;
	private String password;
	private PersistenceManager dbManager;
	private String url;
	private ControlVocabularyManager cvManager;
	private String projectName;

	public MiapeMsiPrideXmlFile(byte[] bytes) throws IOException {
		super(bytes);
	}

	public MiapeMsiPrideXmlFile(File file) {
		super(file);
	}

	public MiapeMsiPrideXmlFile(String name) {
		super(name);
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
		// return MSMiapeFactory.getFactory().create(this, dbManager, user,
		// password);
		return MSMiapeFactory.getFactory().toDocument(this, dbManager, cvManager, user, password,
				projectName);
	}

	@Override
	public MiapeMSIDocument toMiapeMSI() throws MiapeDatabaseException, MiapeSecurityException {
		return MSIMiapeFactory.getFactory().create(this, dbManager, cvManager, user, password,
				projectName);

	}

	@Override
	public String getFileUrl() {
		return url;
	}

	public void setFileUrl(String url) {
		this.url = url;
	}

	@Override
	public MiapeMSIDocument toDocument() throws MiapeDatabaseException, MiapeSecurityException {
		return this.toMiapeMSI();
	}
}
