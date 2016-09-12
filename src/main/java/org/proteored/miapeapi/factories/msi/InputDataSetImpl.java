package org.proteored.miapeapi.factories.msi;

import java.util.Set;

import org.proteored.miapeapi.interfaces.msi.InputData;

public class InputDataSetImpl implements
org.proteored.miapeapi.interfaces.msi.InputDataSet {

	private final String name;
	private final int id;
	private final Set<InputData> inputDatas;

	public InputDataSetImpl(InputDataSetBuilder builder) {
		this.id = builder.id;
		this.inputDatas = builder.inputDatas;
		this.name = builder.name;
	}
	@Override
	public int getId() {
		return id;
	}

	@Override
	public Set<InputData> getInputDatas() {
		return inputDatas;
	}

	@Override
	public String getName() {
		return name;
	}

}
