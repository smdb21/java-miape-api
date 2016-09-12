package org.proteored.miapeapi.cv.ms;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularySet;

public class MatrixComposition extends ControlVocabularySet {

	private static MatrixComposition instance;

	public static MatrixComposition getInstance(ControlVocabularyManager cvManager) {
		if (instance == null)
			instance = new MatrixComposition(cvManager);
		return instance;
	}

	private MatrixComposition(ControlVocabularyManager cvManager) {
		super(cvManager);

		String[] parentAccessionsTMP = { "MS:1000834" };
		this.parentAccessions = parentAccessionsTMP;
		this.setExcludeParents(false);
		this.miapeSection = -1;
	}
}
