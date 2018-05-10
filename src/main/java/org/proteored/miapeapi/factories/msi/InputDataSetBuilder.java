package org.proteored.miapeapi.factories.msi;

import java.util.Set;

import org.proteored.miapeapi.interfaces.msi.InputData;
import org.proteored.miapeapi.interfaces.msi.InputDataSet;

import gnu.trove.set.hash.THashSet;

public class InputDataSetBuilder {

	public int id;
	public Set<InputData> inputDatas;
	public String name;

	InputDataSetBuilder(String name) {
		this.name = name;
	}

	public InputDataSetBuilder id(int value) {
		id = value;
		return this;
	}

	public InputDataSetBuilder inputDatas(Set<InputData> value) {
		inputDatas = value;
		return this;
	}

	public InputDataSet build() {
		return new InputDataSetImpl(this);
	}

	public InputDataSetBuilder inputData(InputData inputData) {
		if (inputData == null) {
			return this;
		}
		if (inputDatas == null) {
			inputDatas = new THashSet<InputData>();
		}
		inputDatas.add(inputData);
		return this;
	}

}