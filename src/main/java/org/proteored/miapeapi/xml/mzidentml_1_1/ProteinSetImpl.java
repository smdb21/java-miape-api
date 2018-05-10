package org.proteored.miapeapi.xml.mzidentml_1_1;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.proteored.miapeapi.interfaces.msi.IdentifiedProtein;
import org.proteored.miapeapi.interfaces.msi.IdentifiedProteinSet;
import org.proteored.miapeapi.interfaces.msi.InputDataSet;
import org.proteored.miapeapi.interfaces.msi.InputParameter;

import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;

public class ProteinSetImpl implements IdentifiedProteinSet {
	private final InputParameter inputParameter;
	private final Set<InputDataSet> inputDataSets = new THashSet<InputDataSet>();
	private final Map<String, IdentifiedProtein> identifiedProteinList = new THashMap<String, IdentifiedProtein>();

	public ProteinSetImpl(InputParameter inputParameter, Collection<InputDataSet> inputDataSets) {
		this.inputParameter = inputParameter;
		if (inputDataSets != null) {
			this.inputDataSets.addAll(inputDataSets);
		}
	}

	public ProteinSetImpl(InputParameter inputParameter, InputDataSet inputDataSet) {
		this.inputParameter = inputParameter;
		if (inputDataSet != null) {
			inputDataSets.add(inputDataSet);
		}
	}

	@Override
	public String getFileLocation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, IdentifiedProtein> getIdentifiedProteins() {
		if (!identifiedProteinList.isEmpty())
			return identifiedProteinList;
		return null;
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
		return "identified proteins";
	}

	public void addIdentifiedProtein(IdentifiedProtein protein) {
		identifiedProteinList.put(protein.getAccession(), protein);
	}

}
