package org.proteored.miapeapi.cv.ms;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularySet;

public class DataProcessingSoftware extends ControlVocabularySet {

	private static DataProcessingSoftware instance;

	public static DataProcessingSoftware getInstance(ControlVocabularyManager cvManager) {
		if (instance == null)
			instance = new DataProcessingSoftware(cvManager);
		return instance;
	}

	private DataProcessingSoftware(ControlVocabularyManager cvManager) {
		super(cvManager);
		String[] parentAccessionsTMP = { "MS:1001457" }; // data processing
															// software
		this.parentAccessions = parentAccessionsTMP;

		this.miapeSection = 227;

	}

}
