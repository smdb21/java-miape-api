package org.proteored.miapeapi.cv.ms;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularySet;

public class ChromatogramType extends ControlVocabularySet {
	private static ChromatogramType instance;

	public static ChromatogramType getInstance(ControlVocabularyManager cvManager) {
		if (instance == null)
			instance = new ChromatogramType(cvManager);
		return instance;
	}

	private ChromatogramType(ControlVocabularyManager cvManager) {
		super(cvManager);
		String[] parentAccessionsTMP = { "MS:1000626" }; // chromatogram type
		this.parentAccessions = parentAccessionsTMP;
		this.miapeSection = 225;

	}

}
