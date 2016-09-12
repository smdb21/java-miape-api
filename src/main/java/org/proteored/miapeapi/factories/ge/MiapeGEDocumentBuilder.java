package org.proteored.miapeapi.factories.ge;

import java.util.Set;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ge.ElectrophoresisType;
import org.proteored.miapeapi.factories.AbstractMiapeDocumentBuilder;
import org.proteored.miapeapi.interfaces.Project;
import org.proteored.miapeapi.interfaces.User;
import org.proteored.miapeapi.interfaces.ge.DirectDetection;
import org.proteored.miapeapi.interfaces.ge.GEAdditionalInformation;
import org.proteored.miapeapi.interfaces.ge.GEContact;
import org.proteored.miapeapi.interfaces.ge.ImageAcquisition;
import org.proteored.miapeapi.interfaces.ge.ImageGelElectrophoresis;
import org.proteored.miapeapi.interfaces.ge.IndirectDetection;
import org.proteored.miapeapi.interfaces.ge.MiapeGEDocument;
import org.proteored.miapeapi.interfaces.ge.Protocol;
import org.proteored.miapeapi.interfaces.ge.Sample;
import org.proteored.miapeapi.interfaces.persistence.PersistenceManager;
import org.proteored.miapeapi.interfaces.xml.XmlManager;

public class MiapeGEDocumentBuilder extends AbstractMiapeDocumentBuilder {

	String electrophoresisType;
	GEContact contact;
	Set<IndirectDetection> indirectDetections;
	Set<DirectDetection> directDetections;
	Set<Sample> samples;
	Set<Protocol> protocols;
	Set<ImageGelElectrophoresis> images;
	Set<ImageAcquisition> imageAcquisitions;
	Set<GEAdditionalInformation> additionalInformations;

	MiapeGEDocumentBuilder(Project project, String documentName, User owner) {
		super(project, documentName, owner);
	}

	MiapeGEDocumentBuilder(Project project, String name, User owner, PersistenceManager db) {
		super(project, name, owner, db);
	}

	MiapeGEDocumentBuilder(Project project, String name, User owner, PersistenceManager db,
			XmlManager xmlManager, ControlVocabularyManager cvUtil) {
		super(project, name, owner, db, xmlManager, cvUtil);
	}

	public MiapeGEDocumentBuilder contact(GEContact value) {
		contact = value;
		return this;
	}

	/**
	 * Gets the electrophoresis type. It should be a possible value of the
	 * {@link ElectrophoresisType}
	 */
	public MiapeGEDocumentBuilder electrophoresisType(String value) {
		electrophoresisType = value;
		return this;
	}

	public MiapeGEDocumentBuilder indirectDetections(Set<IndirectDetection> value) {
		indirectDetections = value;
		return this;
	}

	public MiapeGEDocumentBuilder directDetections(Set<DirectDetection> value) {
		directDetections = value;
		return this;
	}

	public MiapeGEDocumentBuilder samples(Set<Sample> value) {
		samples = value;
		return this;
	}

	public MiapeGEDocumentBuilder images(Set<ImageGelElectrophoresis> value) {
		images = value;
		return this;
	}

	public MiapeGEDocumentBuilder protocols(Set<Protocol> value) {
		protocols = value;
		return this;
	}

	public MiapeGEDocumentBuilder imageAcquisitions(Set<ImageAcquisition> value) {
		imageAcquisitions = value;
		return this;
	}

	public MiapeGEDocumentBuilder additionalInformations(Set<GEAdditionalInformation> value) {
		additionalInformations = value;
		return this;
	}

	@Override
	public MiapeGEDocument build() {
		return new MiapeGEDocumentImpl(this);
	}
}