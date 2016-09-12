package org.proteored.miapeapi.cv.ms;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularySet;

public class BackgroundName extends ControlVocabularySet {

	/*
	 * DATA_FILTERING("MS:1001486", "data filtering"),
	 * LOW_DATA_POINT_REMOVAL("MS:1000594", "low intensity data point removal"),
	 * HIGH_DATA_POINT_REMOVAL("MS:1000746",
	 * "high intensity data point removal") ;
	 */
	private static BackgroundName instance;

	public static BackgroundName getInstance(ControlVocabularyManager cvManager) {
		if (instance == null)
			instance = new BackgroundName(cvManager);
		return instance;
	}

	private BackgroundName(ControlVocabularyManager cvManager) {
		super(cvManager);
		String[] parentAccessionsTMP = { "MS:1001486" };
		this.parentAccessions = parentAccessionsTMP;

		this.miapeSection = 211;

	}
}
