package org.proteored.miapeapi.cv.ge;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularySet;

public class MwRangeType extends ControlVocabularySet {
	/*
	 * STATISTICAL_DISTRIBUTION("sep:00009", "distribution"),
	 * LINEAR_DISTRIBUTION("sep:00018", "linear distribution"),
	 * LOGARITHMIC_DISTRIBUTION("sep:00019", "logarithmic distribution");
	 */
	private static MwRangeType instance;

	public static MwRangeType getInstance(ControlVocabularyManager cvManager) {
		if (instance == null)
			instance = new MwRangeType(cvManager);
		return instance;
	}

	private MwRangeType(ControlVocabularyManager cvManager) {
		super(cvManager);
		String[] parentAccessionsTMP = { "sep:00009" };
		this.parentAccessions = parentAccessionsTMP;
		this.miapeSection = 50;

	}

}
