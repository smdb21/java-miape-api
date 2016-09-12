package org.proteored.miapeapi.cv.ge;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularySet;

public class IndirectDetectionAgentName extends ControlVocabularySet {

	/* ; */
	private static IndirectDetectionAgentName instance;

	public static IndirectDetectionAgentName getInstance(ControlVocabularyManager cvManager) {
		if (instance == null)
			instance = new IndirectDetectionAgentName(cvManager);
		return instance;
	}

	private IndirectDetectionAgentName(ControlVocabularyManager cvManager) {
		super(cvManager);
		this.miapeSection = 39;

	}

}
