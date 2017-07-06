package org.proteored.miapeapi.factories.msi;

import java.util.Map;
import java.util.Set;

import org.proteored.miapeapi.interfaces.msi.IdentifiedProtein;
import org.proteored.miapeapi.interfaces.msi.IdentifiedProteinSet;
import org.proteored.miapeapi.interfaces.msi.InputDataSet;
import org.proteored.miapeapi.interfaces.msi.InputParameter;

public class IdentifiedProteinSetImpl implements IdentifiedProteinSet {

	private final String name;
	private final InputParameter inputParameter;
	private final Set<InputDataSet> inputDataSets;
	private final Map<String, IdentifiedProtein> identifiedProteins;
	private final String fileLocation;

	public IdentifiedProteinSetImpl(IdentifiedProteinSetBuilder builder) {
		this.fileLocation = builder.fileLocation;
		this.identifiedProteins = builder.identifiedProteins;
		this.inputDataSets = builder.inputDataSets;
		this.inputParameter = builder.inputParameter;
		this.name = builder.name;
	}

	@Override
	public String getFileLocation() {
		return fileLocation;
	}

	@Override
	public Map<String, IdentifiedProtein> getIdentifiedProteins() {
		return identifiedProteins;
	}

	@Override
	public Set<InputDataSet> getInputDataSets() {
		return inputDataSets;
	}

	@Override
	public InputParameter getInputParameter() {
		return inputParameter;
	}

	@Override
	public String getName() {
		return name;
	}

}
