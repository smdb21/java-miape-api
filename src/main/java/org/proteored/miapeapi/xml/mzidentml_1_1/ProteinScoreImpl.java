package org.proteored.miapeapi.xml.mzidentml_1_1;

import org.proteored.miapeapi.cv.ControlVocabularyTerm;
import org.proteored.miapeapi.interfaces.msi.ProteinScore;

import uk.ac.ebi.jmzidml.model.mzidml.AbstractParam;

public class ProteinScoreImpl implements ProteinScore {
	private String name;
	private String value;

	public ProteinScoreImpl(AbstractParam param) {
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
