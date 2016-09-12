package org.proteored.miapeapi.cv.ms;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularySet;

public class EsiName extends ControlVocabularySet {

	/*
	 * ELECTRO_SPRAY_IONIZATION("MS:1000073", "electrospray ionization"),
	 * NANOELECTROSPRAY("MS:1000398", "nanoelectrospray"),
	 * MICROELECTROSPRAY("MS:1000397", "microelectrospray"); ;
	 */

	private static EsiName instance;

	public static EsiName getInstance(ControlVocabularyManager cvManager) {
		if (instance == null)
			instance = new EsiName(cvManager);
		return instance;
	}

	private EsiName(ControlVocabularyManager cvManager) {
		super(cvManager);
		// electrospray ionization
		String[] parentAccessionsTMP = { "MS:1000073" };
		this.parentAccessions = parentAccessionsTMP;
		this.miapeSection = 202;

	}
}
