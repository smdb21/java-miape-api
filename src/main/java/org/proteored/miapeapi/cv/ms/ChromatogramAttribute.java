package org.proteored.miapeapi.cv.ms;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularySet;

public class ChromatogramAttribute extends ControlVocabularySet {
	private static ChromatogramAttribute instance;

	public static ChromatogramAttribute getInstance(ControlVocabularyManager cvManager) {
		if (instance == null)
			instance = new ChromatogramAttribute(cvManager);
		return instance;
	}

	private ChromatogramAttribute(ControlVocabularyManager cvManager) {
		super(cvManager);
		String[] parentAccessionsTMP = { "MS:1000808" }; // chromatogram
															// attribute
		this.parentAccessions = parentAccessionsTMP;
		this.miapeSection = -1;

	}

}
