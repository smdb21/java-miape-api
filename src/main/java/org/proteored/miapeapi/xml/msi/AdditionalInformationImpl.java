package org.proteored.miapeapi.xml.msi;

import org.proteored.miapeapi.interfaces.msi.MSIAdditionalInformation;
import org.proteored.miapeapi.xml.msi.autogenerated.ParamType;
import org.proteored.miapeapi.xml.msi.util.MSIControlVocabularyXmlFactory;

public class AdditionalInformationImpl implements MSIAdditionalInformation {
	private final ParamType additionalInformationXML;

	public AdditionalInformationImpl(ParamType msiAdditionalInformation) {
		this.additionalInformationXML = msiAdditionalInformation;
	}

	@Override
	public String getName() {
		return MSIControlVocabularyXmlFactory.getName(this.additionalInformationXML);
	}

	@Override
	public String getValue() {
		return MSIControlVocabularyXmlFactory.getValue(this.additionalInformationXML);
	}

}
