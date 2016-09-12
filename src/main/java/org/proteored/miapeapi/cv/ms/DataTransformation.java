package org.proteored.miapeapi.cv.ms;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularySet;

public class DataTransformation extends ControlVocabularySet {

	private static DataTransformation instance;

	public static DataTransformation getInstance(
			ControlVocabularyManager cvManager) {
		if (instance == null)
			instance = new DataTransformation(cvManager);
		return instance;
	}

	private DataTransformation(ControlVocabularyManager cvManager) {
		super(cvManager);
		// data processing action
		String[] parentAccessionsTMP = { "MS:1000452" };
		parentAccessions = parentAccessionsTMP;
		setExcludeParents(true);
		miapeSection = -1;

	}

}
