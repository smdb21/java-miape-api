package org.proteored.miapeapi.cv.ms;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularySet;

public class BinaryDataType extends ControlVocabularySet {

	private static BinaryDataType instance;

	public static BinaryDataType getInstance(ControlVocabularyManager cvManager) {
		if (instance == null)
			instance = new BinaryDataType(cvManager);
		return instance;
	}

	private BinaryDataType(ControlVocabularyManager cvManager) {
		super(cvManager);
		String[] parentAccessionsTMP = { "MS:1000518" };
		this.parentAccessions = parentAccessionsTMP;

		this.miapeSection = -1;

	}
}
