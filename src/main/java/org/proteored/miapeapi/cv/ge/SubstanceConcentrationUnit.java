package org.proteored.miapeapi.cv.ge;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularySet;

public class SubstanceConcentrationUnit extends ControlVocabularySet {
	private static SubstanceConcentrationUnit instance;

	/*
	 * MOLAR("UO:0000062", "molar"), PERCENT("UO:0000187", "percent"),
	 * MILLIMOLAR("UO:0000063", "millimolar"),
	 * MILLIGRAM_PER_MILLILITER("UO:0000176", "milligram per milliliter"),
	 * MICROLITER("UO:0000101", "microliter"), GRAM_PER_MILLILITER("UO:0000173",
	 * "gram per milliliter"), VOLUME_PERCENTAGE("UO:0000165",
	 * "volume percentage"), MASS_VOLUME_PERCENTAGE("UO:0000164",
	 * "mass volume percentage"), MASS_PERCENTAGE("UO:0000163",
	 * "mass percentage"), NORMAL("UO:0000075", "normal");
	 */
	public static SubstanceConcentrationUnit getInstance(ControlVocabularyManager cvManager) {
		if (instance == null)
			instance = new SubstanceConcentrationUnit(cvManager);
		return instance;
	}

	private SubstanceConcentrationUnit(ControlVocabularyManager cvManager) {
		super(cvManager);
		this.miapeSection = 28;
		String[] explicitAccessionsTMP = { "UO:0000062", "UO:0000187", "UO:0000063", "UO:0000176",
				"UO:0000101", "UO:0000173", "UO:0000165", "UO:0000164", "UO:0000163", "UO:0000075",
				"UO:0000098" };
		this.explicitAccessions = explicitAccessionsTMP;

	}

}
