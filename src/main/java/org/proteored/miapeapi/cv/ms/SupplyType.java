package org.proteored.miapeapi.cv.ms;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularySet;

public class SupplyType extends ControlVocabularySet {

	private static SupplyType instance;

	public static SupplyType getInstance(ControlVocabularyManager cvManager) {
		if (instance == null)
			instance = new SupplyType(cvManager);
		return instance;
	}

	private SupplyType(ControlVocabularyManager cvManager) {
		super(cvManager);

		String[] parentAccessionsTMP = { "MS:1001941" };
		this.parentAccessions = parentAccessionsTMP;
		this.setExcludeParents(true);
		this.miapeSection = 228;

	}
}
