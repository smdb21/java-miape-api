package org.proteored.miapeapi.xml.mzidentml;

import java.util.Set;

import org.proteored.miapeapi.interfaces.msi.InputData;
import org.proteored.miapeapi.interfaces.msi.InputDataSet;

import gnu.trove.set.hash.THashSet;

public class InputDataSetImpl implements InputDataSet {
	private final Integer identifier;
	private final Set<InputData> inputDatas = new THashSet<InputData>();
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
		StringBuilder sb = new StringBuilder();
		for (InputData inputData : inputDatas) {
			if (!"".equals(sb.toString())) {
				sb.append(", ");
			}
			sb.append(inputData.getName());
		}
		if (!"".equals(sb.toString())) {
			name = sb.toString();
			return name;
		}
		name = "Input data set " + identifier;
		return name;
	}

	public void addInputData(InputData inputData) {
		if (inputData != null)
			this.inputDatas.add(inputData);

	}

}
