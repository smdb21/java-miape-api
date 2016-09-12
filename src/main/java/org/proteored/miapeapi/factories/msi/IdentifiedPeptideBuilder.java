package org.proteored.miapeapi.factories.msi;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.proteored.miapeapi.interfaces.msi.IdentifiedPeptide;
import org.proteored.miapeapi.interfaces.msi.IdentifiedProtein;
import org.proteored.miapeapi.interfaces.msi.InputData;
import org.proteored.miapeapi.interfaces.msi.PeptideModification;
import org.proteored.miapeapi.interfaces.msi.PeptideScore;

public class IdentifiedPeptideBuilder {
	String sequence;
	Set<PeptideScore> scores;
	Set<PeptideModification> modifications;
	String spectrumRef;
	String charge;
	String massDesviation;
	InputData inputData;
	int rank;
	List<IdentifiedProtein> proteins;
	int id;
	String rt;

	IdentifiedPeptideBuilder(String sequence) {
		this.sequence = sequence;
	}

	public IdentifiedPeptideBuilder score(Set<PeptideScore> value) {
		scores = value;
		return this;
	}

	public IdentifiedPeptideBuilder modifications(Set<PeptideModification> value) {
		modifications = value;
		return this;
	}

	public IdentifiedPeptideBuilder spectrumRef(String value) {
		spectrumRef = value;
		return this;
	}

	public IdentifiedPeptideBuilder charge(String value) {
		charge = value;
		return this;
	}

	public IdentifiedPeptideBuilder massDesviation(String value) {
		massDesviation = value;
		return this;
	}

	public IdentifiedPeptideBuilder inputData(InputData value) {
		inputData = value;
		return this;
	}

	public IdentifiedPeptideBuilder rank(int value) {
		rank = value;
		return this;
	}

	public IdentifiedPeptideBuilder identifiedProteins(
			List<IdentifiedProtein> value) {
		proteins = value;
		return this;
	}

	public IdentifiedPeptideBuilder identifiedProtein(IdentifiedProtein value) {
		if (proteins == null)
			proteins = new ArrayList<IdentifiedProtein>();
		proteins.add(value);
		return this;
	}

	public IdentifiedPeptideBuilder retentionTimeInSeconds(String value) {
		rt = value;
		return this;
	}

	public IdentifiedPeptideBuilder id(int value) {
		id = value;
		return this;
	}

	public IdentifiedPeptide build() {
		return new IdentifiedPeptideImpl(this);
	}
}