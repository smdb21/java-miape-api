package org.proteored.miapeapi.cv.ge;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularySet;

public class SubstanceMassUnit extends ControlVocabularySet {
	private static SubstanceMassUnit instance;

	/*
	 * GRAM("UO:0000021", "gram"), DALTON("UO:0000221", "dalton"),
	 * MILLIGRAM("UO:0000022", "milligram"), MICROGRAM("UO:0000023",
	 * "microgram"), KILODALTON("UO:0000222", "kilodalton");
	 */
	public static SubstanceMassUnit getInstance(ControlVocabularyManager cvManager) {
		if (instance == null)
			instance = new SubstanceMassUnit(cvManager);
		return instance;
	}

	private SubstanceMassUnit(ControlVocabularyManager cvManager) {
		super(cvManager);
		this.miapeSection = 29;
		String[] explicitAccessionsTMP = { "UO:0000021", "UO:0000221", "UO:0000022", "UO:0000023",
				"UO:0000222" };
		this.explicitAccessions = explicitAccessionsTMP;

	}

}
