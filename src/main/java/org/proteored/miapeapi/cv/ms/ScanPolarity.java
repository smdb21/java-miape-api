package org.proteored.miapeapi.cv.ms;

import org.proteored.miapeapi.cv.Accession;
import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularySet;

public class ScanPolarity extends ControlVocabularySet {
	/* SCAN_POLARITY("MS:1000465", "scan polarity", "MS"); */
	public static Accession SCAN_POLARITY_ACC = new Accession("MS:1000465");
	private static ScanPolarity instance;

	public static ScanPolarity getInstance(ControlVocabularyManager cvManager) {
		if (instance == null)
			instance = new ScanPolarity(cvManager);
		return instance;
	}

	private ScanPolarity(ControlVocabularyManager cvManager) {
		super(cvManager);
		// scan polarity
		String[] parentAccessionsTMP = { SCAN_POLARITY_ACC.toString() };
		this.parentAccessions = parentAccessionsTMP;

		this.miapeSection = -1;

	}
}
