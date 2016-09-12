package org.proteored.miapeapi.cv.ge;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularySet;

public class SubstanceVolumeUnit extends ControlVocabularySet {
	private static SubstanceVolumeUnit instance;

	/*
	 * VOLUME_PERCENTAGE("UO:0000165", "volume percentage"),
	 * CUBIC_CENTIMETER("UO:0000097", "cubic centimeter"),
	 * DECILITER("UO:0000209", "deciliter"), MICROLITER("UO:0000101",
	 * "microliter"), MILLILITER("UO:0000098", "milliliter"),
	 * PERCENT("UO:0000187", "percent"), LITER("UO:0000099", "liter"),
	 * VOLUME_PER_UNIT_VOLUME("UO:0000205", "volume per unit volume"),
	 * MASS_VOLUME_PERCENTAGE("UO:0000164", "mass volume percentage");
	 */
	public static SubstanceVolumeUnit getInstance(ControlVocabularyManager cvManager) {
		if (instance == null)
			instance = new SubstanceVolumeUnit(cvManager);
		return instance;
	}

	private SubstanceVolumeUnit(ControlVocabularyManager cvManager) {
		super(cvManager);
		this.miapeSection = 27;
		String[] explicitAccessionsTMP = { "UO:0000165", "UO:0000097", "UO:0000209", "UO:0000101",
				"UO:0000098", "UO:0000187", "UO:0000099", "UO:0000205", "UO:0000164" };
		this.explicitAccessions = explicitAccessionsTMP;

	}

}
