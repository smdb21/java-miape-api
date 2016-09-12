package org.proteored.miapeapi.cv.ms;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularySet;

public class SamplePlateType extends ControlVocabularySet {
	private static SamplePlateType instance;

	public static SamplePlateType getInstance(ControlVocabularyManager cvManager) {
		if (instance == null)
			instance = new SamplePlateType(cvManager);
		return instance;
	}

	// MS:1000441 scan
	// MS:1000480 mass analyzer attribute
	// MS:1000487 ion optics attribute
	// MS:1000032 customization (of an instrument)
	// MS:1000481 detector attribute
	// MS:1000027 detector acquisition mode
	// MS:1000482 source attribute
	// MS:1001954 acquisition parameters
	private SamplePlateType(ControlVocabularyManager cvManager) {
		super(cvManager);
		String[] parentAccessionsTMP = { "MS:1001938" };
		this.parentAccessions = parentAccessionsTMP;
		setExcludeParents(true);
		this.miapeSection = 229;
	}

}
