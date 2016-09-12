package org.proteored.miapeapi.xml.ms;

import org.proteored.miapeapi.interfaces.ms.MSAdditionalInformation;
import org.proteored.miapeapi.xml.ms.autogenerated.ParamType;
import org.proteored.miapeapi.xml.ms.util.MsControlVocabularyXmlFactory;

public class AdditionalInformationImpl implements MSAdditionalInformation {

	private final ParamType additionalInformationXML;

	public AdditionalInformationImpl(ParamType msAdditionalInformation) {
		this.additionalInformationXML = msAdditionalInformation;
	}

	@Override
	public String getName() {
		return MsControlVocabularyXmlFactory.getName(this.additionalInformationXML);
	}

	@Override
	public String getValue() {
		return MsControlVocabularyXmlFactory.getValue(this.additionalInformationXML);
	}

}
