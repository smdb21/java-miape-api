package org.proteored.miapeapi.xml.mzidentml_1_1;

import java.util.Set;

import org.proteored.miapeapi.interfaces.msi.InputData;
import org.proteored.miapeapi.interfaces.msi.InputDataSet;

import gnu.trove.set.hash.THashSet;

public class InputDataSetImpl implements InputDataSet {
	private final Integer identifier;
	private final Set<InputData> inputDatas = new THashSet<InputData>();

	public InputDataSetImpl(Integer inputDataSetID) {
		this.identifier = inputDataSetID;
	}

	@Override
	public int getId() {
		if (identifier != null)
			return identifier;
		return -1;
	}

	@Override
	public Set<InputData> getInputDatas() {
		return inputDatas;
	}

	@Override
	public String getName() {
		return "Input data set " + identifier;
	}

	public void addInputData(InputData inputData) {
		if (inputData != null)
			this.inputDatas.add(inputData);

	}

}
