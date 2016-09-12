package org.proteored.miapeapi.cv.ge;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularySet;

public class EquipmentName extends ControlVocabularySet {
	/* ; */
	private static EquipmentName instance;

	public static EquipmentName getInstance(ControlVocabularyManager cvManager) {
		if (instance == null)
			instance = new EquipmentName(cvManager);
		return instance;
	}

	private EquipmentName(ControlVocabularyManager cvManager) {
		super(cvManager);
		this.miapeSection = 53;

	}
}
