package org.proteored.miapeapi.cv.ge;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularySet;

public class AcquisitionEquipmentType extends ControlVocabularySet {
	private static AcquisitionEquipmentType instance;

	/*
	 * IMAGE_ACQUISITION_DEVICE("sep:00096", "image acquisition device"),
	 * SCANNER("sep:00100", "scanner"), CAMERA("sep:00099", "camera");
	 */
	public static AcquisitionEquipmentType getInstance(ControlVocabularyManager cvManager) {
		if (instance == null)
			instance = new AcquisitionEquipmentType(cvManager);
		return instance;
	}

	private AcquisitionEquipmentType(ControlVocabularyManager cvManager) {
		super(cvManager);
		String[] parentAccessionsTMP = { "sep:00096" };
		this.parentAccessions = parentAccessionsTMP;
		this.miapeSection = 48;

	}
}
