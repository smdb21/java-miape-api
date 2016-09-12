package org.proteored.miapeapi.cv.ms;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularySet;

public class ChargeCalculationMethod extends ControlVocabularySet {
	/*
	 * CHARGE_STATE ("MS:1000778", "charge state calculation"),
	 * BELOW_PRECURSOR_INTENSITY_DOMINANCE("MS:1000779",
	 * "below precursor intensity dominance charge state calculation") ;
	 */
	private static ChargeCalculationMethod instance;

	public static ChargeCalculationMethod getInstance(ControlVocabularyManager cvManager) {
		if (instance == null)
			instance = new ChargeCalculationMethod(cvManager);
		return instance;
	}

	private ChargeCalculationMethod(ControlVocabularyManager cvManager) {
		super(cvManager);
		String[] parentAccessionsTMP = { "MS:1000778" };
		this.parentAccessions = parentAccessionsTMP;

		this.miapeSection = 212;

	}
}
