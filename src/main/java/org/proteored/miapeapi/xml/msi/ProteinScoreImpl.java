package org.proteored.miapeapi.xml.msi;

import org.proteored.miapeapi.interfaces.msi.ProteinScore;
import org.proteored.miapeapi.xml.msi.autogenerated.ParamType;
import org.proteored.miapeapi.xml.msi.util.MSIControlVocabularyXmlFactory;

public class ProteinScoreImpl implements ProteinScore {
	private final ParamType param;

	public ProteinScoreImpl(ParamType xmlScore) {
		this.param = xmlScore;
	}

	@Override
	public String getName() {
		return MSIControlVocabularyXmlFactory.getName(param);
	}

	@Override
	public String getValue() {
		return MSIControlVocabularyXmlFactory.getValue(param);
	}

}
