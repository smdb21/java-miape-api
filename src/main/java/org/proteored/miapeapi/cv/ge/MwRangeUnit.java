package org.proteored.miapeapi.cv.ge;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularySet;

public class MwRangeUnit extends ControlVocabularySet {

	/* KILODALTON("UO:0000222", "kilodalton"); */
	private static MwRangeUnit instance;

	public static MwRangeUnit getInstance(ControlVocabularyManager cvManager) {
		if (instance == null)
			instance = new MwRangeUnit(cvManager);
		return instance;
	}

	private MwRangeUnit(ControlVocabularyManager cvManager) {
		super(cvManager);
		String[] explicitAccessionsTMP = { "UO:0000222" };
		this.explicitAccessions = explicitAccessionsTMP;
		this.miapeSection = 49;

	}
}
