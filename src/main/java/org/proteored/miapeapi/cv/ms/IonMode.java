package org.proteored.miapeapi.cv.ms;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularySet;

/**
 * The Enum IonMode. Postive or Negative charge.
 */
public class IonMode extends ControlVocabularySet {

	/*
	 * SCAN_POLARITY("MS:1000465", "scan polarity"), POSITIVE("MS:1000130",
	 * "positive scan"), NEGATIVE("MS:1000129", "negative scan");
	 */

	private static IonMode instance;

	public static IonMode getInstance(ControlVocabularyManager cvManager) {
		if (instance == null)
			instance = new IonMode(cvManager);
		return instance;
	}

	private IonMode(ControlVocabularyManager cvManager) {
		super(cvManager);
		String[] parentAccessionsTMP = { "MS:1000465" };
		this.parentAccessions = parentAccessionsTMP;

		this.miapeSection = 209;

	}
}
