package org.proteored.miapeapi.xml.msi;

import java.util.Map;
import java.util.Set;

import org.proteored.miapeapi.interfaces.msi.IdentifiedProtein;
import org.proteored.miapeapi.interfaces.msi.IdentifiedProteinSet;
import org.proteored.miapeapi.interfaces.msi.InputDataSet;
import org.proteored.miapeapi.interfaces.msi.InputParameter;
import org.proteored.miapeapi.xml.msi.autogenerated.InputDataSetReferences;
import org.proteored.miapeapi.xml.msi.autogenerated.MSIIdentifiedProtein;
import org.proteored.miapeapi.xml.msi.autogenerated.MSIIdentifiedProteinSet;
import org.proteored.miapeapi.xml.msi.autogenerated.MSIInputData;
import org.proteored.miapeapi.xml.msi.autogenerated.MSIInputDataSet;
import org.proteored.miapeapi.xml.msi.autogenerated.MSIInputParameters;
import org.proteored.miapeapi.xml.msi.autogenerated.MSISoftwareType;

import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;

public class IdentifiedProteinSetImpl implements IdentifiedProteinSet {
	private final MSIIdentifiedProteinSet xmlProteinSet;

	Map<String, MSIInputData> mapInputData;
	Map<String, MSIInputDataSet> mapInputDataSet;
	Map<String, MSIInputParameters> mapInputParameter;
	Map<String, MSISoftwareType> mapSoftware;
	private final Map<String, IdentifiedProtein> proteinList;

	public IdentifiedProteinSetImpl(MSIIdentifiedProteinSet xmlProteinSet, Map<String, MSIInputData> mapInputData,
			Map<String, MSIInputDataSet> mapInputDataSet, Map<String, MSIInputParameters> mapInputParameter,
			Map<String, MSISoftwareType> mapSoftware, Map<String, IdentifiedProtein> proteinList) {
		this.xmlProteinSet = xmlProteinSet;

		this.mapInputData = mapInputData;
		this.mapInputDataSet = mapInputDataSet;
		this.mapInputParameter = mapInputParameter;
		this.mapSoftware = mapSoftware;
		this.proteinList = proteinList;
	}

	@Override
	public String getFileLocation() {
		return xmlProteinSet.getFileURL();
	}

	@Override
	public Map<String, IdentifiedProtein> getIdentifiedProteins() {

		Map<String, IdentifiedProtein> result = new THashMap<String, IdentifiedProtein>();
		for (MSIIdentifiedProtein protein : xmlProteinSet.getMSIIdentifiedProtein()) {

			// final IdentifiedProteinImpl proteinImpl = new
			// IdentifiedProteinImpl(
			// protein, mapInputData, mapProteinPeptides,
			// mapPeptideProteins);
			if (this.proteinList.containsKey(protein.getId()))
				result.put(protein.getAC(), this.proteinList.get(protein.getId()));
		}

		return result;
	}

	@Override
	public String getName() {
		return xmlProteinSet.getName();
	}

	@Override
	public Set<InputDataSet> getInputDataSets() {
		Set<InputDataSet> inputDataSet = new THashSet<InputDataSet>();
		InputDataSetReferences inputDataSetReferences = xmlProteinSet.getInputDataSetReferences();
		if (inputDataSetReferences != null) {
			for (String inputDataSetRef : inputDataSetReferences.getInputDataSetRef()) {
				inputDataSet.add(new InputDataSetImpl(this.mapInputDataSet.get(inputDataSetRef)));
			}
		}
		return inputDataSet;
	}

	@Override
	public InputParameter getInputParameter() {
		String parametersRef = xmlProteinSet.getParametersRef();
		if (parametersRef != null) {
			if (mapInputParameter.containsKey(parametersRef)) {
				return new InputParameterImpl(mapInputParameter.get(parametersRef), mapSoftware);

			}
		}
		return null;
	}

}
