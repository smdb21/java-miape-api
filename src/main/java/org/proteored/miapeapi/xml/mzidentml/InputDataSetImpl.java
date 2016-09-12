package org.proteored.miapeapi.xml.mzidentml;

import java.util.HashSet;
import java.util.Set;

import org.proteored.miapeapi.interfaces.msi.InputData;
import org.proteored.miapeapi.interfaces.msi.InputDataSet;

public class InputDataSetImpl implements InputDataSet {
	private final Integer identifier;
	private final Set<InputData> inputDatas = new HashSet<InputData>();
	private String name;

	public InputDataSetImpl(Integer inputDataSetID) {
		this.identifier = inputDataSetID;
	}

	public InputDataSetImpl(Integer inputDataSetID, String name) {
		this.identifier = inputDataSetID;
		this.name = name;
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
		if (this.name != null)
			return name;
		return "Input data set " + identifier;
	}

	public void addInputData(InputData inputData) {
		if (inputData != null)
			this.inputDatas.add(inputData);

	}

}
