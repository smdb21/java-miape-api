package org.proteored.miapeapi.xml.xtandem.msi;

import java.util.HashSet;
import java.util.Set;

import org.proteored.miapeapi.interfaces.msi.InputData;
import org.proteored.miapeapi.interfaces.msi.InputDataSet;
import org.proteored.miapeapi.xml.util.MiapeXmlUtil;

public class InputDataSetImpl implements InputDataSet {
	private final Integer identifier;
	private final Set<InputData> inputDatas = new HashSet<InputData>();

	public InputDataSetImpl(String spectrumPath, Integer inputDataSetID) {
		this.identifier = inputDataSetID;
		if (spectrumPath != null && !"".equals(spectrumPath)) {
			Integer inputDataID = MiapeXmlUtil.InputDataCounter.increaseCounter();
			this.inputDatas.add(new InputDataImpl(spectrumPath, inputDataID));
		}
	}

	@Override
	public int getId() {
		if (identifier != null)
			return identifier;
		return -1;
	}

	@Override
	public String getName() {
		if (identifier != null)
			return "Input data set " + identifier;
		return "Input data set";
	}

	public void addInputData(InputData inputData) {
		inputDatas.add(inputData);
	}

	@Override
	public Set<InputData> getInputDatas() {
		if (!inputDatas.isEmpty())
			return inputDatas;
		return null;
	}

}
