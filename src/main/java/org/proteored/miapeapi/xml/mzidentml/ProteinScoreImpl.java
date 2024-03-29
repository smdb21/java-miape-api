package org.proteored.miapeapi.xml.mzidentml;

import org.proteored.miapeapi.cv.ControlVocabularyTerm;
import org.proteored.miapeapi.interfaces.msi.ProteinScore;
import org.proteored.miapeapi.xml.mzidentml.autogenerated.FuGECommonOntologyParamType;

public class ProteinScoreImpl implements ProteinScore {
	private String name;
	private String value;

	public ProteinScoreImpl(FuGECommonOntologyParamType param) {
		if (param != null) {
			name = param.getName();
			value = param.getValue();
		}
	}

	public ProteinScoreImpl(ControlVocabularyTerm cvTerm, String value) {
		if (cvTerm != null) {
			this.name = cvTerm.getPreferredName();
			this.value = value;
		}
	}

	@Override
	public String getName() {
		return name;

	}

	@Override
	public String getValue() {
		return value;
	}

}
