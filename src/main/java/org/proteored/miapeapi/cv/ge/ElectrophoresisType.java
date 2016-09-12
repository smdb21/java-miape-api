package org.proteored.miapeapi.cv.ge;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularySet;

public class ElectrophoresisType extends ControlVocabularySet {
	/*
	 * ONE_DIMENSIONAL("sep:00150", "one dimensional gel electrophoresis"),
	 * TWO_DIMENSIONAL("sep:00155", "two dimensional gel electrophoresis"),
	 * DIFFERENCE("sep:00180", "difference gel electrophoresis"),
	 * POLYACRYLAMIDE("sep:00172", "polyacrylamide gel electrohoresis"),
	 * AGAROSE("sep:00171", "agarose gel electrophoresis"),
	 * SODIUM_DODECYL_SULFATE_POLYACRYLAMIDE("sep:00173",
	 * "sodium dodecyl sulfate polyacrylamide gel electrophoresis");
	 */
	private static ElectrophoresisType instance;

	public static ElectrophoresisType getInstance(ControlVocabularyManager cvManager) {
		if (instance == null)
			instance = new ElectrophoresisType(cvManager);
		return instance;
	}

	private ElectrophoresisType(ControlVocabularyManager cvManager) {
		super(cvManager);
		String[] explicitAccessionsTMP = { "sep:00150", "sep:00155", "sep:00180", "sep:00172",
				"sep:00171", "sep:00173" };
		this.explicitAccessions = explicitAccessionsTMP;

		this.miapeSection = 1;
	}

}
