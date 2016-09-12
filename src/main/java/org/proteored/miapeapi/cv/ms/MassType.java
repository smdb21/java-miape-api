package org.proteored.miapeapi.cv.ms;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularySet;

public class MassType extends ControlVocabularySet {

	/*
	 * AVERAGE("MS:1000208", "average mass"), MONOISOTOPIC("MS:1000225",
	 * "monoisotopic mass"), NOMINAL("MS:1000229", "nominal mass"),
	 * ACURATE_MASS("MS:1000207", "accurate mass"), EXACT_MASS("MS:1000215",
	 * "exact mass");
	 */

	private static MassType instance;

	public static MassType getInstance(ControlVocabularyManager cvManager) {
		if (instance == null)
			instance = new MassType(cvManager);
		return instance;
	}

	private MassType(ControlVocabularyManager cvManager) {
		super(cvManager);
		String[] explicitAccessionsTMP = { "MS:1000208", "MS:1000225", "MS:1000229", "MS:1000207",
				"MS:1000215" };
		this.explicitAccessions = explicitAccessionsTMP;

		this.miapeSection = 214;

	}
}
