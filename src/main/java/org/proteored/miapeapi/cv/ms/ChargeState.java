package org.proteored.miapeapi.cv.ms;

import org.proteored.miapeapi.cv.Accession;
import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularySet;
import org.proteored.miapeapi.cv.ControlVocabularyTerm;
import org.proteored.miapeapi.cv.ControlVocabularyTermImpl;

public class ChargeState extends ControlVocabularySet {

	/* CHARGE_STATE("MS:1000041", "charge state", "MS"); */

	public static final Accession CHARGE_STATE_ACCESSION = new Accession("MS:1000041");
	public static final String CHARGE_STATE_PREFERRED_NAME = "charge state";

	private static ChargeState instance;

	public static ChargeState getInstance(ControlVocabularyManager cvManager) {
		if (instance == null)
			instance = new ChargeState(cvManager);
		return instance;
	}

	private ChargeState(ControlVocabularyManager cvManager) {
		super(cvManager);
		String[] explicitAccessionsTMP = { CHARGE_STATE_ACCESSION.toString() };
		this.explicitAccessions = explicitAccessionsTMP;
		String[] parentAccessionsTMP = { "MS:1000778" };
		this.parentAccessions = parentAccessionsTMP;

		this.miapeSection = 212;

	}

	public ControlVocabularyTerm getChargeStateTerm() {
		return new ControlVocabularyTermImpl(CHARGE_STATE_ACCESSION, CHARGE_STATE_PREFERRED_NAME);
	}
}
