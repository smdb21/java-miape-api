package org.proteored.miapeapi.factories.ge;

import java.util.Set;

import org.proteored.miapeapi.exceptions.MiapeDatabaseException;
import org.proteored.miapeapi.exceptions.MiapeSecurityException;
import org.proteored.miapeapi.factories.AbstractMiapeDocumentImpl;
import org.proteored.miapeapi.interfaces.ge.DirectDetection;
import org.proteored.miapeapi.interfaces.ge.GEAdditionalInformation;
import org.proteored.miapeapi.interfaces.ge.GEContact;
import org.proteored.miapeapi.interfaces.ge.ImageAcquisition;
import org.proteored.miapeapi.interfaces.ge.ImageGelElectrophoresis;
import org.proteored.miapeapi.interfaces.ge.IndirectDetection;
import org.proteored.miapeapi.interfaces.ge.MiapeGEDocument;
import org.proteored.miapeapi.interfaces.ge.Protocol;
import org.proteored.miapeapi.interfaces.ge.Sample;
import org.proteored.miapeapi.interfaces.xml.MiapeXmlFile;
import org.proteored.miapeapi.validation.ValidationReport;
import org.proteored.miapeapi.validation.ge.MiapeGEValidator;

public class MiapeGEDocumentImpl extends AbstractMiapeDocumentImpl implements
		MiapeGEDocument {

	private final String electrophoresisType;
	private final Set<IndirectDetection> indirectDetections;
	private final Set<DirectDetection> directDetections;
	private final Set<Sample> samples;
	private final Set<Protocol> protocols;
	private final Set<ImageGelElectrophoresis> images;
	private final Set<ImageAcquisition> imageAcquisitions;
	private final GEContact contact;
	private final Set<GEAdditionalInformation> additionalInformations;

	public MiapeGEDocumentImpl(MiapeGEDocumentBuilder miapeGEBuilder) {
		super(miapeGEBuilder);
		electrophoresisType = miapeGEBuilder.electrophoresisType;
		indirectDetections = miapeGEBuilder.indirectDetections;
		directDetections = miapeGEBuilder.directDetections;
		samples = miapeGEBuilder.samples;
		protocols = miapeGEBuilder.protocols;
		images = miapeGEBuilder.images;
		imageAcquisitions = miapeGEBuilder.imageAcquisitions;
		contact = miapeGEBuilder.contact;
		additionalInformations = miapeGEBuilder.additionalInformations;
	}

	@Override
	public GEContact getContact() {
		return contact;
	}

	@Override
	public Set<DirectDetection> getDirectDetections() {
		return directDetections;
	}

	@Override
	public String getElectrophoresisType() {
		return electrophoresisType;
	}

	@Override
	public Set<ImageGelElectrophoresis> getImages() {
		return images;
	}

	@Override
	public Set<IndirectDetection> getIndirectDetections() {
		return indirectDetections;
	}

	@Override
	public Set<Protocol> getProtocols() {
		return protocols;
	}

	@Override
	public Set<Sample> getSamples() {
		return samples;
	}

	@Override
	public void delete(String user, String password)
			throws MiapeDatabaseException, MiapeSecurityException {
		if (dbManager != null) {
			dbManager.getMiapeGEPersistenceManager().deleteById(getId(), user,
					password);
		} else
			throw new MiapeDatabaseException(
					"The document is not persisted in the database just yet!");
	}

	@Override
	public int store() throws MiapeDatabaseException, MiapeSecurityException {
		if (dbManager != null) {
			id = dbManager.getMiapeGEPersistenceManager().store(this);
			return id;
		} else {
			throw new MiapeDatabaseException(
					"The persistance is not Configured");
		}
	}

	@Override
	public Set<ImageAcquisition> getImageAcquisitions() {
		return imageAcquisitions;
	}

	@Override
	public MiapeXmlFile<MiapeGEDocument> toXml() {
		return xmlManager.getMiapeGEXmlFactory().toXml(this, cvUtil);
	}

	@Override
	public Set<GEAdditionalInformation> getAdditionalInformations() {
		return additionalInformations;
	}

	@Override
	public ValidationReport getValidationReport() {
		return MiapeGEValidator.getInstance().getReport(this);
	}
}
