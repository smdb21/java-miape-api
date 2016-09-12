package org.proteored.miapeapi.cv.ms;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularySet;

public class InletType extends ControlVocabularySet {

	private static InletType instance;

	public static InletType getInstance(ControlVocabularyManager cvManager) {
		if (instance == null)
			instance = new InletType(cvManager);
		return instance;
	}

	private InletType(ControlVocabularyManager cvManager) {
		super(cvManager);
		// inlet type
		String[] parentAccessionsTMP = { "MS:1000007" };
		this.parentAccessions = parentAccessionsTMP;
		this.miapeSection = -1;

	}
}
