package org.proteored.miapeapi.cv.ms;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularySet;

public class PressureUnit extends ControlVocabularySet {
	/* PASCAL("UO:0000110", "pascal"); */

	private static PressureUnit instance;

	public static PressureUnit getInstance(ControlVocabularyManager cvManager) {
		if (instance == null)
			instance = new PressureUnit(cvManager);
		return instance;
	}

	private PressureUnit(ControlVocabularyManager cvManager) {
		super(cvManager);
		String[] explicitAccessionsTMP = { "UO:0000110" };
		this.explicitAccessions = explicitAccessionsTMP;
		this.miapeSection = 223;

	}
}
