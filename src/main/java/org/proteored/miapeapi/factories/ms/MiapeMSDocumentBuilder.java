package org.proteored.miapeapi.factories.ms;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.factories.AbstractMiapeDocumentBuilder;
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
import org.proteored.miapeapi.interfaces.persistence.PersistenceManager;
import org.proteored.miapeapi.interfaces.xml.XmlManager;

public class MiapeMSDocumentBuilder extends AbstractMiapeDocumentBuilder {

	MSContact contact;

	Set<Acquisition> acquistions;

	Set<DataAnalysis> dataAnalysis;
	/* Set<Quantitation> quantitations; */
	List<ResultingData> resultingData;
	Set<Spectrometer> spectrometers;
	List<InstrumentConfiguration> instrumentConfigurations;

	List<MSAdditionalInformation> additionalInformations;

	MiapeMSDocumentBuilder(Project project, String documentName, User owner) {
		super(project, documentName, owner);
	}

	MiapeMSDocumentBuilder(Project project, String documentName, User owner, PersistenceManager db) {
		super(project, documentName, owner, db);
	}

	MiapeMSDocumentBuilder(Project project, String documentName, User owner, PersistenceManager db,
			XmlManager xml, ControlVocabularyManager cvUtil) {
		super(project, documentName, owner, db, xml, cvUtil);

	}

	public MiapeMSDocumentBuilder instrumentConfigurations(List<InstrumentConfiguration> value) {
		this.instrumentConfigurations = value;
		return this;
	}

	public MiapeMSDocumentBuilder instrumentConfiguration(InstrumentConfiguration value) {
		if (this.instrumentConfigurations == null)
			this.instrumentConfigurations = new ArrayList<InstrumentConfiguration>();
		this.instrumentConfigurations.add(value);
		return this;
	}

	public MiapeMSDocumentBuilder acquisitions(Set<Acquisition> value) {
		acquistions = value;
		return this;
	}

	public MiapeMSDocumentBuilder dataAnalyses(Set<DataAnalysis> value) {
		dataAnalysis = value;
		return this;
	}

	/*
	 * public MiapeMSDocumentBuilder quantitations(Set<Quantitation> value) {
	 * quantitations = value; return this; }
	 */

	public MiapeMSDocumentBuilder resultingDatas(List<ResultingData> value) {
		resultingData = value;
		return this;
	}

	public MiapeMSDocumentBuilder spectrometers(Set<Spectrometer> value) {
		spectrometers = value;
		return this;
	}

	public MiapeMSDocumentBuilder spectrometer(Spectrometer value) {
		if (spectrometers == null)
			spectrometers = new HashSet<Spectrometer>();
		spectrometers.add(value);
		return this;
	}

	public MiapeMSDocumentBuilder contact(MSContact value) {
		contact = value;
		return this;
	}

	public MiapeMSDocumentBuilder additionalInformations(List<MSAdditionalInformation> value) {
		additionalInformations = value;
		return this;
	}

	@Override
	public MiapeMSDocument build() {
		return new MiapeMSDocumentImpl(this);
	}
}