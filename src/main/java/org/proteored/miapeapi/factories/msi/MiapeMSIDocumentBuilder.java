package org.proteored.miapeapi.factories.msi;

import java.util.List;
import java.util.Set;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.factories.AbstractMiapeDocumentBuilder;
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
import org.proteored.miapeapi.interfaces.persistence.PersistenceManager;
import org.proteored.miapeapi.interfaces.xml.XmlManager;

public class MiapeMSIDocumentBuilder extends AbstractMiapeDocumentBuilder {

	String generatedFilesUrl;
	String generatedFilesDescription;
	Set<Software> msiSoftwares;
	Set<InputDataSet> inputDataSets;
	Set<InputParameter> inputParameters;
	Set<Validation> validations;
	Set<IdentifiedProteinSet> identifiedProteinSets;
	int msDocument;
	MSContact contact;
	Set<MSIAdditionalInformation> additionalInformations;
	List<IdentifiedPeptide> identifiedPeptides;

	MiapeMSIDocumentBuilder(Project project, String documentName, User owner) {
		super(project, documentName, owner);
	}

	MiapeMSIDocumentBuilder(Project project, String name, User owner,
			PersistenceManager db) {
		super(project, name, owner, db);
	}

	MiapeMSIDocumentBuilder(Project project, String name, User owner,
			PersistenceManager db, XmlManager xmlManager,
			ControlVocabularyManager cvUtil) {
		super(project, name, owner, db, xmlManager, cvUtil);
	}

	public MiapeMSIDocumentBuilder name(String name) {
		super.name = name;
		return this;
	}

	public MiapeMSIDocumentBuilder generatedFilesUrl(String value) {
		generatedFilesUrl = value;
		return this;
	}

	public MiapeMSIDocumentBuilder generatedFilesDescription(String value) {
		generatedFilesDescription = value;
		return this;
	}

	public MiapeMSIDocumentBuilder msiSoftwares(Set<Software> value) {
		msiSoftwares = value;
		return this;
	}

	public MiapeMSIDocumentBuilder inputDataSets(Set<InputDataSet> value) {
		inputDataSets = value;
		return this;
	}

	public MiapeMSIDocumentBuilder inputParameters(Set<InputParameter> value) {
		inputParameters = value;
		return this;
	}

	public MiapeMSIDocumentBuilder validations(Set<Validation> value) {
		validations = value;
		return this;
	}

	public MiapeMSIDocumentBuilder identifiedProteinSets(
			Set<IdentifiedProteinSet> value) {
		identifiedProteinSets = value;
		return this;
	}

	public MiapeMSIDocumentBuilder identifiedPeptides(
			List<IdentifiedPeptide> value) {
		identifiedPeptides = value;
		return this;
	}

	public MiapeMSIDocumentBuilder msDocumentReference(int value) {
		msDocument = value;
		return this;
	}

	public MiapeMSIDocumentBuilder contact(MSContact value) {
		contact = value;
		return this;
	}

	public MiapeMSIDocumentBuilder additionalInformations(
			Set<MSIAdditionalInformation> value) {
		additionalInformations = value;
		return this;
	}

	public MiapeMSIDocumentBuilder dbManager(PersistenceManager value) {
		dbManager = value;
		return this;
	}

	@Override
	public MiapeMSIDocument build() {
		return new MiapeMSIDocumentImpl(this);
	}
}