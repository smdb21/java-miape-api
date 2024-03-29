package org.proteored.miapeapi.xml.msi.adapter;

import org.proteored.miapeapi.interfaces.Adapter;
import org.proteored.miapeapi.interfaces.msi.InputData;
import org.proteored.miapeapi.interfaces.msi.InputDataSet;
import org.proteored.miapeapi.xml.msi.autogenerated.MSIInputDataSet;
import org.proteored.miapeapi.xml.msi.autogenerated.ObjectFactory;
import org.proteored.miapeapi.xml.msi.util.MSIControlVocabularyXmlFactory;
import org.proteored.miapeapi.xml.util.MiapeXmlUtil;

public class InputDataSetAdapter implements Adapter<MSIInputDataSet> {
	private final InputDataSet inputDataSet;
	private final ObjectFactory factory;
	private final MSIControlVocabularyXmlFactory cvFactory;

	public InputDataSetAdapter(InputDataSet inputDataSet2,
			ObjectFactory factory, MSIControlVocabularyXmlFactory cvFactory) {
		this.cvFactory = cvFactory;
		this.factory = factory;
		this.inputDataSet = inputDataSet2;
	}

	@Override
	public MSIInputDataSet adapt() {
		MSIInputDataSet inputDataSetXML = factory.createMSIInputDataSet();
		inputDataSetXML.setId(MiapeXmlUtil.IdentifierPrefixes.INPUTDATASET.getPrefix() + inputDataSet.getId());

		inputDataSetXML.setName(inputDataSet.getName());

		if (inputDataSet.getInputDatas() != null) {
			for (InputData inputData : inputDataSet.getInputDatas()) {
				inputDataSetXML.getMSIInputData().add(new InputDataAdapter(inputData, factory, cvFactory).adapt());
			}
		}
		return inputDataSetXML;
	}

}
