package org.proteored.miapeapi.factories.msi;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.proteored.miapeapi.cv.msi.ValidationType;
import org.proteored.miapeapi.interfaces.msi.IdentifiedPeptide;
import org.proteored.miapeapi.interfaces.msi.IdentifiedProtein;
import org.proteored.miapeapi.interfaces.msi.ProteinScore;

public class IdentifiedProteinBuilder {
	String accession;
	String description;
	Set<ProteinScore> scores;
	Boolean validationStatus;
	String peptideNumber;
	String coverage;
	String additionalInformation;
	String matchedNumber;
	String unmatchedNumber;
	String validationType;
	String validationValue;
	List<IdentifiedPeptide> identifiedPeptides;
	int id;

	IdentifiedProteinBuilder(String accession) {
		this.accession = accession;
	}

	public IdentifiedProteinBuilder description(String value) {
		description = value;
		return this;
	}

	public IdentifiedProteinBuilder scores(Set<ProteinScore> value) {
		scores = value;
		return this;
	}

	public IdentifiedProteinBuilder peptideNumber(String value) {
		peptideNumber = value;
		return this;
	}

	public IdentifiedProteinBuilder coverage(String value) {
		coverage = value;
		return this;
	}

	public IdentifiedProteinBuilder matchedNumber(String value) {
		matchedNumber = value;
		return this;
	}

	public IdentifiedProteinBuilder unmatchedNumber(String value) {
		unmatchedNumber = value;
		return this;
	}

	public IdentifiedProteinBuilder additionalInformation(String value) {
		additionalInformation = value;
		return this;
	}

	public IdentifiedProteinBuilder validationStatus(Boolean value) {
		validationStatus = value;
		return this;
	}

	/**
	 * Set the validation type of the protein. It should be one of the possible
	 * values from {@link ValidationType}
	 **/
	public IdentifiedProteinBuilder validationType(String value) {
		validationType = value;
		return this;
	}

	public IdentifiedProteinBuilder validationValue(String value) {
		validationValue = value;
		return this;
	}

	public IdentifiedProteinBuilder identifiedPeptides(
			List<IdentifiedPeptide> value) {
		identifiedPeptides = value;
		return this;
	}

	public IdentifiedProteinBuilder identifiedPeptide(IdentifiedPeptide value) {
		if (identifiedPeptides == null)
			identifiedPeptides = new ArrayList<IdentifiedPeptide>();
		identifiedPeptides.add(value);
		return this;
	}

	public IdentifiedProteinBuilder id(int value) {
		id = value;
		return this;
	}

	public IdentifiedProtein build() {
		return new IdentifiedProteinImpl(this);
	}
}