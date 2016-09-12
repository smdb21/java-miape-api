package org.proteored.miapeapi.cv.ms;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularySet;

public class DelayedExtraction extends ControlVocabularySet {

	private static DelayedExtraction instance;
	public static final String DELAYED_EXTRACTION_ACCESSION = "MS:1000246";

	public static DelayedExtraction getInstance(ControlVocabularyManager cvManager) {
		if (instance == null)
			instance = new DelayedExtraction(cvManager);
		return instance;
	}

	private DelayedExtraction(ControlVocabularyManager cvManager) {
		super(cvManager);

		String[] parentAccessionsTMP = { DELAYED_EXTRACTION_ACCESSION };
		this.parentAccessions = parentAccessionsTMP;
		this.setExcludeParents(false);

	}
}
