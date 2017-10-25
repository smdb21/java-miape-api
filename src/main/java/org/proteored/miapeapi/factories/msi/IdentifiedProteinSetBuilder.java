package org.proteored.miapeapi.factories.msi;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.proteored.miapeapi.interfaces.msi.IdentifiedProtein;
import org.proteored.miapeapi.interfaces.msi.IdentifiedProteinSet;
import org.proteored.miapeapi.interfaces.msi.InputDataSet;
import org.proteored.miapeapi.interfaces.msi.InputParameter;

public class IdentifiedProteinSetBuilder {

	public String fileLocation;
	public Map<String, IdentifiedProtein> identifiedProteins;
	public InputParameter inputParameter;
	public Set<InputDataSet> inputDataSets;
	public String name;

	IdentifiedProteinSetBuilder(String name) {
		this.name = name;
	}

	public IdentifiedProteinSetBuilder fileLocation(String value) {
		this.fileLocation = value;
		return this;
	}

	public IdentifiedProteinSetBuilder identifiedProteins(Map<String, IdentifiedProtein> value) {
		this.identifiedProteins = value;
		return this;
	}

	public IdentifiedProteinSetBuilder inputDataSets(Set<InputDataSet> value) {
		this.inputDataSets = value;
		return this;
	}

	public IdentifiedProteinSetBuilder name(String value) {
		this.name = value;
		return this;
	}

	public IdentifiedProteinSet build() {
		return new IdentifiedProteinSetImpl(this);
	}

	public IdentifiedProteinSetBuilder identifiedProtein(IdentifiedProtein protein) {
		if (identifiedProteins == null) {
			identifiedProteins = new HashMap<String, IdentifiedProtein>();
		}
		identifiedProteins.put(protein.getAccession(), protein);
		return this;
	}

}