package org.proteored.miapeapi.factories.msi;

import java.util.Set;

import org.proteored.miapeapi.interfaces.msi.InputData;
import org.proteored.miapeapi.interfaces.msi.InputDataSet;

public class InputDataSetBuilder {

	public int id;
	public Set<InputData> inputDatas;
	public String name;

	InputDataSetBuilder(String name) {
		this.name = name;
	}

	public InputDataSetBuilder id(int value) {
		this.id = value;
		return this;
	}

	public InputDataSetBuilder inputDatas(Set<InputData> value) {
		this.inputDatas = value;
		return this;
	}

	public InputDataSet build() {
		return new InputDataSetImpl(this);
	}

}