package org.proteored.miapeapi.cv.ge;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularySet;

public class PhRangeType extends ControlVocabularySet {
	// STATISTICAL_DISTRIBUTION("sep:00009", "statistical distribution"),
	/*
	 * LINEAR_DISTRIBUTION("sep:00018", "linear distribution"),
	 * LOGARITHMIC_DISTRIBUTION("sep:00019", "logarithmic distribution");
	 */
	private static PhRangeType instance;

	public static PhRangeType getInstance(ControlVocabularyManager cvManager) {
		if (instance == null)
			instance = new PhRangeType(cvManager);
		return instance;
	}

	public PhRangeType(ControlVocabularyManager cvManager) {
		super(cvManager);
		String[] parentAccessionsTMP = { "sep:00009" };
		this.parentAccessions = parentAccessionsTMP;
		this.miapeSection = 26;

	}
}
