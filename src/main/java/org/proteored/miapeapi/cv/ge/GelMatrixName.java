package org.proteored.miapeapi.cv.ge;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularySet;

public class GelMatrixName extends ControlVocabularySet {
	/*
	 * GEL_MATRIX("sep:00127", "gel matrix"),
	 * AGAROSE_GEL("sep:00128","agarose gel"),
	 * POLYACRYLAMIDE_GEL("sep:00129","polyacrylamide gel"),
	 * IMMOBILIZED_PH_GRADIENT_GEL("sep:00130","immobilized pH gradient gel"),
	 * GEL("sep:00110", "gel"), IMMOBILINE_DRY_STRIP("sep:00131",
	 * "immobiline dry strip");
	 */
	private static GelMatrixName instance;

	public static GelMatrixName getInstance(ControlVocabularyManager cvManager) {
		if (instance == null)
			instance = new GelMatrixName(cvManager);
		return instance;
	}

	private GelMatrixName(ControlVocabularyManager cvManager) {
		super(cvManager);
		String[] parentAccessionsTMP = { "sep:00110" };
		this.parentAccessions = parentAccessionsTMP;
		this.miapeSection = 3;

	}
}
