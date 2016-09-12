package org.proteored.miapeapi.cv.ms;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularySet;

public class SpectrometerName extends ControlVocabularySet {
	public static final String LTQ_ORBITRAP_XL_NAME = "LTQ Orbitrap XL";
	private static SpectrometerName instance;

	public static SpectrometerName getInstance(ControlVocabularyManager cvManager) {
		if (instance == null)
			instance = new SpectrometerName(cvManager);
		return instance;
	}

	private SpectrometerName(ControlVocabularyManager cvManager) {
		super(cvManager);
		// grandchildrens of intrument model (MS:1000031)
		String[] parentAccessionsTMP = { "MS:1000121", "MS:1000490", "MS:1000495", "MS:1000122",
				"MS:1000491", "MS:1000488", "MS:1001800", "MS:1000124", "MS:1000483", "MS:1000489",
				"MS:1000126" };
		this.parentAccessions = parentAccessionsTMP;

		this.miapeSection = 201;
		// Exclude parents
		this.setExcludeParents(true);

	}

}
