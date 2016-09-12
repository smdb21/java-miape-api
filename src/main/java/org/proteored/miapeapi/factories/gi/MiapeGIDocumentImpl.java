package org.proteored.miapeapi.factories.gi;

import java.util.Set;

import org.proteored.miapeapi.exceptions.MiapeDatabaseException;
import org.proteored.miapeapi.exceptions.MiapeSecurityException;
import org.proteored.miapeapi.factories.AbstractMiapeDocumentImpl;
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
import org.proteored.miapeapi.interfaces.xml.MiapeXmlFile;
import org.proteored.miapeapi.validation.ValidationReport;
import org.proteored.miapeapi.validation.gi.MiapeGIValidator;

public class MiapeGIDocumentImpl extends AbstractMiapeDocumentImpl implements
		MiapeGIDocument {
	private final String electrophoresisType;
	private final String reference;
	private final GEContact contact;
	private final int geDocument;
	private final Set<DataReporting> dataReportings;
	private final Set<ImageProcessing> analysisProcessings;
	private final Set<AnalysisDesign> analysisDesigns;
	private final Set<DataExtraction> dataExtractions;
	private final Set<ImageAnalysisSoftware> analysisSoftwares;
	private final Set<ImageAnalysisSoftware> imagePreparationSoftwares;
	private final Set<ImageGelInformatics> images;
	private final Set<DataAnalysis> dataAnalysises;
	private final Set<ImagePreparationStep> imagePreparations;
	private final Set<GIAdditionalInformation> additionalInformation;

	public MiapeGIDocumentImpl(MiapeGIDocumentBuilder miapeGIBuilder) {
		super(miapeGIBuilder);
		electrophoresisType = miapeGIBuilder.electrophoresisType;
		reference = miapeGIBuilder.reference;
		geDocument = miapeGIBuilder.geDocument;
		dataReportings = miapeGIBuilder.dataReportings;
		analysisProcessings = miapeGIBuilder.analysisProcessings;
		analysisDesigns = miapeGIBuilder.analysisDesigns;
		dataExtractions = miapeGIBuilder.dataExtractions;
		analysisSoftwares = miapeGIBuilder.analysisSoftwares;
		imagePreparationSoftwares = miapeGIBuilder.imagePreparationSoftwares;
		images = miapeGIBuilder.images;
		dataAnalysises = miapeGIBuilder.dataAnalysises;
		imagePreparations = miapeGIBuilder.imagePreparations;
		contact = miapeGIBuilder.contact;
		additionalInformation = miapeGIBuilder.additionalInformations;
	}

	@Override
	public Set<AnalysisDesign> getAnalysisDesigns() {
		return analysisDesigns;
	}

	@Override
	public Set<ImageProcessing> getImageProcessings() {
		return analysisProcessings;
	}

	@Override
	public Set<ImageAnalysisSoftware> getImageAnalysisSoftwares() {
		return analysisSoftwares;
	}

	@Override
	public Set<DataAnalysis> getDataAnalysises() {
		return dataAnalysises;
	}

	@Override
	public Set<DataExtraction> getDataExtractions() {
		return dataExtractions;
	}

	@Override
	public Set<DataReporting> getDataReportings() {
		return dataReportings;
	}

	@Override
	public String getElectrophoresisType() {
		return electrophoresisType;
	}

	@Override
	public Set<ImageGelInformatics> getImages() {
		return images;
	}

	@Override
	public Set<ImageAnalysisSoftware> getImagePreparationSoftwares() {
		return imagePreparationSoftwares;
	}

	@Override
	public Set<ImagePreparationStep> getImagePreparationSteps() {
		return imagePreparations;
	}

	@Override
	public String getReference() {
		return reference;
	}

	@Override
	public void delete(String user, String password)
			throws MiapeDatabaseException, MiapeSecurityException {
		if (dbManager != null) {
			dbManager.getMiapeGIPersistenceManager().deleteById(getId(), user,
					password);
		} else
			throw new MiapeDatabaseException(
					"The document is not persisted in the database just yet!");
	}

	@Override
	public MiapeXmlFile<MiapeGIDocument> toXml() {
		return xmlManager.getMiapeGIXmlFactory().toXml(this, cvUtil);
	}

	@Override
	public GEContact getContact() {
		return contact;
	}

	@Override
	public Set<GIAdditionalInformation> getAdditionalInformations() {
		return additionalInformation;
	}

	@Override
	public ValidationReport getValidationReport() {
		return MiapeGIValidator.getInstance().getReport(this);
	}

	@Override
	public int getGEDocumentReference() {
		return geDocument;
	}

	@Override
	public int store() throws MiapeDatabaseException, MiapeSecurityException {
		if (dbManager != null) {
			id = dbManager.getMiapeGIPersistenceManager().store(this);
			return id;
		} else {
			throw new MiapeDatabaseException(
					"The persistance is not Configured");
		}
	}
}
