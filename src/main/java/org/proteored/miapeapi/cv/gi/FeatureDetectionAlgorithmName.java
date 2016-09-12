package org.proteored.miapeapi.cv.gi;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularySet;

public class FeatureDetectionAlgorithmName extends ControlVocabularySet {
	private static FeatureDetectionAlgorithmName instance;

	/* ; */
	public static FeatureDetectionAlgorithmName getInstance(ControlVocabularyManager cvManager) {
		if (instance == null)
			instance = new FeatureDetectionAlgorithmName(cvManager);
		return instance;
	}

	private FeatureDetectionAlgorithmName(ControlVocabularyManager cvManager) {
		super(cvManager);
		this.miapeSection = 102;

	}

}
