package org.proteored.miapeapi.factories.msi;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.proteored.miapeapi.exceptions.MiapeDatabaseException;
import org.proteored.miapeapi.exceptions.MiapeSecurityException;
import org.proteored.miapeapi.factories.AbstractMiapeDocumentImpl;
import org.proteored.miapeapi.interfaces.MiapeDate;
import org.proteored.miapeapi.interfaces.Project;
import org.proteored.miapeapi.interfaces.Software;
import org.proteored.miapeapi.interfaces.User;
import org.proteored.miapeapi.interfaces.ms.MSContact;
import org.proteored.miapeapi.interfaces.msi.IdentifiedPeptide;
import org.proteored.miapeapi.interfaces.msi.IdentifiedProteinSet;
import org.proteored.miapeapi.interfaces.msi.InputDataSet;
import org.proteored.miapeapi.interfaces.msi.InputParameter;
import org.proteored.miapeapi.interfaces.msi.MSIAdditionalInformation;
import org.proteored.miapeapi.interfaces.msi.MiapeMSIDocument;
import org.proteored.miapeapi.interfaces.msi.Validation;
import org.proteored.miapeapi.interfaces.xml.MiapeXmlFile;
import org.proteored.miapeapi.validation.ValidationReport;
import org.proteored.miapeapi.validation.msi.MiapeMSIValidator;

public class MiapeMSIDocumentImpl extends AbstractMiapeDocumentImpl implements MiapeMSIDocument {
	private final String generatedFilesUrl;
	private final String generatedFilesDescription;
	private final Set<Software> msiSoftwares;
	private final Set<InputDataSet> inputDataSets;
	private final Set<InputParameter> inputParameters;
	private final Set<Validation> validations;
	private final Set<IdentifiedProteinSet> identifiedProteinSets;
	private int msDocument;
	private final MSContact contact;
	private final Set<MSIAdditionalInformation> additionalInformation;
	private final List<IdentifiedPeptide> identifiedPeptides;

	public MiapeMSIDocumentImpl(MiapeMSIDocumentBuilder miapeMSIBuilder) {
		super(miapeMSIBuilder);
		generatedFilesUrl = miapeMSIBuilder.generatedFilesUrl;
		generatedFilesDescription = miapeMSIBuilder.generatedFilesDescription;
		msiSoftwares = miapeMSIBuilder.msiSoftwares;
		inputDataSets = miapeMSIBuilder.inputDataSets;
		inputParameters = miapeMSIBuilder.inputParameters;
		validations = miapeMSIBuilder.validations;
		identifiedProteinSets = miapeMSIBuilder.identifiedProteinSets;
		msDocument = miapeMSIBuilder.msDocument;
		contact = miapeMSIBuilder.contact;
		additionalInformation = miapeMSIBuilder.additionalInformations;
		identifiedPeptides = miapeMSIBuilder.identifiedPeptides;
	}

	@Override
	public String getGeneratedFilesDescription() {
		return generatedFilesDescription;
	}

	@Override
	public String getGeneratedFilesURI() {
		return generatedFilesUrl;
	}

	@Override
	public Set<InputDataSet> getInputDataSets() {
		return inputDataSets;
	}

	@Override
	public Set<InputParameter> getInputParameters() {
		return inputParameters;
	}

	@Override
	public Set<Software> getSoftwares() {
		return msiSoftwares;
	}

	@Override
	public Set<Validation> getValidations() {
		return validations;
	}

	@Override
	public MSContact getContact() {
		return contact;
	}

	@Override
	public MiapeDate getDate() {
		return date;
	}

	@Override
	public Date getModificationDate() {
		return modificationDate;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public User getOwner() {
		return owner;
	}

	@Override
	public String getAttachedFileLocation() {
		return prideUrl;
	}

	@Override
	public Project getProject() {
		return project;
	}

	@Override
	public Boolean getTemplate() {
		return template;
	}

	@Override
	public String getVersion() {
		return version;
	}

	@Override
	public Set<IdentifiedProteinSet> getIdentifiedProteinSets() {
		return identifiedProteinSets;
	}

	@Override
	public void delete(String user, String password) throws MiapeDatabaseException, MiapeSecurityException {
		if (dbManager != null) {
			dbManager.getMiapeMSIPersistenceManager().deleteById(getId(), user, password);
		} else
			throw new MiapeDatabaseException("The document is not persisted in the database just yet!");
	}

	@Override
	public int store() throws MiapeDatabaseException, MiapeSecurityException {
		if (dbManager != null) {
			id = dbManager.getMiapeMSIPersistenceManager().store(this);
			return id;
		} else {
			throw new MiapeDatabaseException("The persistance is not Configured");
		}
	}

	@Override
	public MiapeXmlFile<MiapeMSIDocument> toXml() {
		return xmlManager.getMiapeMSIXmlFactory().toXml(this, cvUtil);
	}

	@Override
	public int getMSDocumentReference() {
		return msDocument;
	}

	@Override
	public Set<MSIAdditionalInformation> getAdditionalInformations() {
		return additionalInformation;
	}

	@Override
	public ValidationReport getValidationReport() {
		return MiapeMSIValidator.getInstance().getReport(this);
	}

	@Override
	public void setReferencedMSDocument(int msDocumentID) {
		msDocument = msDocumentID;

	}

	@Override
	public List<IdentifiedPeptide> getIdentifiedPeptides() {
		return identifiedPeptides;
	}

	@Override
	public void setId(int id) {
		this.id = id;
	}

}
