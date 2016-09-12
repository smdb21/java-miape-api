package org.proteored.miapeapi.factories.gi;

import java.util.Set;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.factories.AbstractMiapeDocumentBuilder;
import org.proteored.miapeapi.interfaces.Project;
import org.proteored.miapeapi.interfaces.User;
import org.proteored.miapeapi.interfaces.ge.GEContact;
import org.proteored.miapeapi.interfaces.gi.AnalysisDesign;
import org.proteored.miapeapi.interfaces.gi.DataAnalysis;
import org.proteored.miapeapi.interfaces.gi.DataExtraction;
import org.proteored.miapeapi.interfaces.gi.DataReporting;
import org.proteored.miapeapi.interfaces.gi.GIAdditionalInformation;
import org.proteored.miapeapi.interfaces.gi.ImageAnalysisSoftware;
import org.proteored.miapeapi.interfaces.gi.ImageGelInformatics;
import org.proteored.miapeapi.interfaces.gi.ImagePreparationStep;
import org.proteored.miapeapi.interfaces.gi.ImageProcessing;
import org.proteored.miapeapi.interfaces.gi.MiapeGIDocument;
import org.proteored.miapeapi.interfaces.persistence.PersistenceManager;
import org.proteored.miapeapi.interfaces.xml.XmlManager;

public class MiapeGIDocumentBuilder extends AbstractMiapeDocumentBuilder {

	String electrophoresisType;
	String reference;
	int geDocument;
	GEContact contact;
	Set<DataReporting> dataReportings;
	Set<ImageProcessing> analysisProcessings;
	Set<AnalysisDesign> analysisDesigns;
	Set<DataExtraction> dataExtractions;
	Set<ImageAnalysisSoftware> analysisSoftwares;
	Set<ImageAnalysisSoftware> imagePreparationSoftwares;
	Set<ImageGelInformatics> images;
	Set<DataAnalysis> dataAnalysises;
	Set<ImagePreparationStep> imagePreparations;
	Set<GIAdditionalInformation> additionalInformations;

	MiapeGIDocumentBuilder(Project project, String documentName, User owner) {
		super(project, documentName, owner);
	}

	MiapeGIDocumentBuilder(Project project, String name, User owner, PersistenceManager db) {
		super(project, name, owner, db);
	}

	MiapeGIDocumentBuilder(Project project, String name, User owner, PersistenceManager db,
			XmlManager xmlManager, ControlVocabularyManager cvUtil) {
		super(project, name, owner, db, xmlManager, cvUtil);
	}

	public MiapeGIDocumentBuilder electrophoresisType(String value) {
		electrophoresisType = value;
		return this;
	}

	public MiapeGIDocumentBuilder reference(String value) {
		reference = value;
		return this;
	}

	public MiapeGIDocumentBuilder geDocumentReference(int value) {
		geDocument = value;
		return this;
	}

	public MiapeGIDocumentBuilder dataReportings(Set<DataReporting> value) {
		dataReportings = value;
		return this;
	}

	public MiapeGIDocumentBuilder analysisProcessings(Set<ImageProcessing> value) {
		analysisProcessings = value;
		return this;
	}

	public MiapeGIDocumentBuilder analysisDesigns(Set<AnalysisDesign> value) {
		analysisDesigns = value;
		return this;
	}

	public MiapeGIDocumentBuilder dataExtractions(Set<DataExtraction> value) {
		dataExtractions = value;
		return this;
	}

	public MiapeGIDocumentBuilder analysisSoftwares(Set<ImageAnalysisSoftware> value) {
		analysisSoftwares = value;
		return this;
	}

	public MiapeGIDocumentBuilder imagePreparationSoftwares(Set<ImageAnalysisSoftware> value) {
		imagePreparationSoftwares = value;
		return this;
	}

	public MiapeGIDocumentBuilder imagePreparationSteps(Set<ImagePreparationStep> value) {
		imagePreparations = value;
		return this;
	}

	public MiapeGIDocumentBuilder images(Set<ImageGelInformatics> value) {
		images = value;
		return this;
	}

	public MiapeGIDocumentBuilder dataAnalysises(Set<DataAnalysis> value) {
		dataAnalysises = value;
		return this;
	}

	public MiapeGIDocumentBuilder contact(GEContact value) {
		contact = value;
		return this;
	}

	public MiapeGIDocumentBuilder additionalInformations(Set<GIAdditionalInformation> value) {
		additionalInformations = value;
		return this;
	}

	@Override
	public MiapeGIDocument build() {
		return new MiapeGIDocumentImpl(this);
	}
}