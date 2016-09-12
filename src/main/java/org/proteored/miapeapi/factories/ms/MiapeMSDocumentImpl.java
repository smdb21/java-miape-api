package org.proteored.miapeapi.factories.ms;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.proteored.miapeapi.exceptions.MiapeDatabaseException;
import org.proteored.miapeapi.exceptions.MiapeSecurityException;
import org.proteored.miapeapi.factories.AbstractMiapeDocumentImpl;
import org.proteored.miapeapi.interfaces.MiapeDate;
import org.proteored.miapeapi.interfaces.Project;
import org.proteored.miapeapi.interfaces.User;
import org.proteored.miapeapi.interfaces.ms.Acquisition;
import org.proteored.miapeapi.interfaces.ms.DataAnalysis;
import org.proteored.miapeapi.interfaces.ms.InstrumentConfiguration;
import org.proteored.miapeapi.interfaces.ms.MSAdditionalInformation;
import org.proteored.miapeapi.interfaces.ms.MSContact;
import org.proteored.miapeapi.interfaces.ms.MiapeMSDocument;
import org.proteored.miapeapi.interfaces.ms.ResultingData;
import org.proteored.miapeapi.interfaces.ms.Spectrometer;
import org.proteored.miapeapi.interfaces.xml.MiapeXmlFile;
import org.proteored.miapeapi.interfaces.xml.XmlMiapeFactory;
import org.proteored.miapeapi.validation.ValidationReport;
import org.proteored.miapeapi.validation.ms.MiapeMSValidator;

public class MiapeMSDocumentImpl extends AbstractMiapeDocumentImpl implements
		MiapeMSDocument {

	private final Set<Acquisition> controlAnalysisSoftwares;
	private final Set<DataAnalysis> dataAnalysis;
	/* private final Set<Quantitation> quantitations; */
	private final List<ResultingData> resultingData;
	private final Set<Spectrometer> spectrometers;
	private final MSContact contact;
	private final List<MSAdditionalInformation> additionalInformation;
	private final List<InstrumentConfiguration> instrumentConfigurations;

	public MiapeMSDocumentImpl(MiapeMSDocumentBuilder builder) {
		super(builder);
		instrumentConfigurations = builder.instrumentConfigurations;
		controlAnalysisSoftwares = builder.acquistions;
		dataAnalysis = builder.dataAnalysis;
		/* this.quantitations = builder.quantitations; */
		resultingData = builder.resultingData;
		spectrometers = builder.spectrometers;
		contact = builder.contact;
		additionalInformation = builder.additionalInformations;

	}

	@Override
	public Set<Acquisition> getAcquisitions() {
		return controlAnalysisSoftwares;
	}

	@Override
	public Set<DataAnalysis> getDataAnalysis() {
		return dataAnalysis;
	}

	/*
	 * @Override public Set<Quantitation> getQuantitations() { return
	 * quantitations; }
	 */

	@Override
	public List<ResultingData> getResultingDatas() {
		return resultingData;
	}

	@Override
	public Set<Spectrometer> getSpectrometers() {
		return spectrometers;
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
	public User getOwner() {
		return owner;
	}

	@Override
	public void delete(String user, String password)
			throws MiapeDatabaseException, MiapeSecurityException {
		if (dbManager != null) {
			dbManager.getMiapeMSPersistenceManager().deleteById(getId(), user,
					password);
		} else {
			throw new MiapeDatabaseException(
					"The document is not persisted in the database just yet!");
		}
	}

	@Override
	public int store() throws MiapeDatabaseException, MiapeSecurityException {
		if (dbManager != null) {
			return id = dbManager.getMiapeMSPersistenceManager().store(this);
		} else {
			throw new MiapeDatabaseException(
					"The persistance is not Configured");
		}
	}

	@Override
	public MiapeXmlFile<MiapeMSDocument> toXml() {
		final XmlMiapeFactory<MiapeMSDocument> msXmlFactory = xmlManager
				.getMiapeMSXmlFactory();
		final MiapeXmlFile<MiapeMSDocument> xml = msXmlFactory.toXml(this,
				cvUtil);
		return xml;

	}

	@Override
	public String toString() {
		return "MiapeMSDocumentImpl [controlAnalysisSoftwares="
				+ controlAnalysisSoftwares + ", peakListGenerations="
				+ dataAnalysis + ", resultingData=" + resultingData
				+ ", spectrometers=" + spectrometers + ", contact=" + contact
				+ ", additionalInformation=" + additionalInformation
				+ ", instrumentConfigurations=" + instrumentConfigurations
				+ "]";
	}

	@Override
	public List<MSAdditionalInformation> getAdditionalInformations() {
		return additionalInformation;
	}

	@Override
	public ValidationReport getValidationReport() {
		return MiapeMSValidator.getInstance().getReport(this);
	}

	@Override
	public List<InstrumentConfiguration> getInstrumentConfigurations() {
		return instrumentConfigurations;
	}

}
