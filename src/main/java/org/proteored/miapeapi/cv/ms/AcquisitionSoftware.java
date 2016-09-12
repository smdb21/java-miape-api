package org.proteored.miapeapi.cv.ms;

import org.proteored.miapeapi.cv.Accession;
import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularySet;

public class AcquisitionSoftware extends ControlVocabularySet {
	private static AcquisitionSoftware instance;
	public static Accession ACQUISITION_SOFTWARE_ACC = new Accession(
			"MS:1001455");

	public static AcquisitionSoftware getInstance(
			ControlVocabularyManager cvManager) {
		if (instance == null)
			instance = new AcquisitionSoftware(cvManager);
		return instance;
	}

	// MS:1000441 scan
	// MS:1000480 mass analyzer attribute
	// MS:1000487 ion optics attribute
	// MS:1000032 customization (of an instrument)
	// MS:1000481 detector attribute
	// MS:1000027 detector acquisition mode
	// MS:1000482 source attribute

	private AcquisitionSoftware(ControlVocabularyManager cvManager) {
		super(cvManager);
		String[] parentAccessionsTMP = { ACQUISITION_SOFTWARE_ACC.toString() };
		this.parentAccessions = parentAccessionsTMP;
		this.setExcludeParents(true);
		this.miapeSection = 502;
	}
}
