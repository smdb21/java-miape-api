package org.proteored.miapeapi.cv.ms;

import org.proteored.miapeapi.cv.Accession;
import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularySet;

public class AcquisitionParameters extends ControlVocabularySet {
	public static Accession CUSTOMIZATION_CV = new Accession("MS:1000032");
	private static AcquisitionParameters instance;
	public static final String ACQUISITION_PARAMETER_ACCESSION = "MS:1001954";

	public static AcquisitionParameters getInstance(ControlVocabularyManager cvManager) {
		if (instance == null)
			instance = new AcquisitionParameters(cvManager);
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
	private AcquisitionParameters(ControlVocabularyManager cvManager) {
		super(cvManager);
		String[] parentAccessionsTMP = { "MS:1000441", "MS:1000480", "MS:1000487", "MS:1000481",
				"MS:1000027", "MS:1000482", ACQUISITION_PARAMETER_ACCESSION };
		this.parentAccessions = parentAccessionsTMP;
		String[] explicitAccessionsTMP = { "MS:1000032" };
		this.explicitAccessions = explicitAccessionsTMP;

	}

}
