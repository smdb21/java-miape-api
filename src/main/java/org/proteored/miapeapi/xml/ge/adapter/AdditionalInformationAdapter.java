package org.proteored.miapeapi.xml.ge.adapter;

import org.proteored.miapeapi.cv.AdditionalInformationName;
import org.proteored.miapeapi.interfaces.Adapter;
import org.proteored.miapeapi.interfaces.ge.GEAdditionalInformation;
import org.proteored.miapeapi.xml.ge.autogenerated.ObjectFactory;
import org.proteored.miapeapi.xml.ge.autogenerated.ParamType;
import org.proteored.miapeapi.xml.ge.util.GEControlVocabularyXmlFactory;

public class AdditionalInformationAdapter implements Adapter<ParamType> {
	private final GEAdditionalInformation addInfo;
	private final ObjectFactory factory;
	private final GEControlVocabularyXmlFactory cvFactory;

	public AdditionalInformationAdapter(GEAdditionalInformation addInfo, ObjectFactory factory,
			GEControlVocabularyXmlFactory cvFactory) {
		this.addInfo = addInfo;
		this.cvFactory = cvFactory;
		this.factory = factory;

	}

	@Override
	public ParamType adapt() {

		return cvFactory.createCV(this.addInfo.getName(), this.addInfo.getValue(),
				AdditionalInformationName.getInstance(cvFactory.getCvManager()));

	}

}
