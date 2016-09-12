package org.proteored.miapeapi.cv.ms;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularySet;

public class SmoothingType extends ControlVocabularySet {
	/*
	 * SMOOTHING("MS:1000592", "smoothing"), MOVING_AVERAGE("MS:1000785",
	 * "moving average smoothing"), GAUSSIAN("MS:1000784",
	 * "Gaussian smoothing"), SAVITZKY_GOLAY("MS:1000782",
	 * "Savitzky-Golay smoothing"), LOWESS("MS:1000783", "LOWESS smoothing") ;
	 */
	private static SmoothingType instance;

	public static SmoothingType getInstance(ControlVocabularyManager cvManager) {
		if (instance == null)
			instance = new SmoothingType(cvManager);
		return instance;
	}

	private SmoothingType(ControlVocabularyManager cvManager) {
		super(cvManager);
		String[] parentAccessionsTMP = { "MS:1000592" };
		this.parentAccessions = parentAccessionsTMP;

		this.miapeSection = 208;

	}
}
