package org.proteored.miapeapi.cv.ms;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularySet;

public class MassToleranceUnit extends ControlVocabularySet {

	/*
	 * DALTON("UO:0000221", "dalton"), PARTS_PER_MILLION("UO:0000169",
	 * "parts per million"), PERCENT("UO:0000187", "percent");
	 */
	private static MassToleranceUnit instance;

	public static MassToleranceUnit getInstance(ControlVocabularyManager cvManager) {
		if (instance == null)
			instance = new MassToleranceUnit(cvManager);
		return instance;
	}

	private MassToleranceUnit(ControlVocabularyManager cvManager) {
		super(cvManager);
		String[] explicitAccessionsTMP = { "UO:0000221", "UO:0000169", "UO:0000187" };
		this.explicitAccessions = explicitAccessionsTMP;

		this.miapeSection = 210;

	}
}
