package org.proteored.miapeapi.cv.ms;

import org.proteored.miapeapi.cv.Accession;
import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularySet;

public class GasType extends ControlVocabularySet {
	/*
	 * HELIUM_ATOM("CHEBI", "CHEBI:30217", "helium atom"), ARGON_ATOM("CHEBI",
	 * "CHEBI:49475", "argon atom"), NITROGEN_ATOM("CHEBI", "CHEBI:25555",
	 * "nitrogen atom"), DINITROGEN("CHEBI", "CHEBI:17997", "dinitrogen"),
	 * COLLISION_GAS("MS", "MS:1000419", "collision gas");
	 */

	public static final Accession COLLISION_GAS = new Accession("MS:1000419");
	public static final Accession GAS_PRESSURE_CV = new Accession("MS:1000869");
	private static GasType instance;

	public static GasType getInstance(ControlVocabularyManager cvManager) {
		if (instance == null)
			instance = new GasType(cvManager);
		return instance;
	}

	private GasType(ControlVocabularyManager cvManager) {
		super(cvManager);
		String[] explicitAccessionsTMP = { "CHEBI:30217", "CHEBI:49475", "CHEBI:25555",
				"CHEBI:17997", COLLISION_GAS.toString() };
		this.explicitAccessions = explicitAccessionsTMP;

		this.miapeSection = 216;

	}

}
