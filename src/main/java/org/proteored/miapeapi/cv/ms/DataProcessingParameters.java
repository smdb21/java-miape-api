package org.proteored.miapeapi.cv.ms;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularySet;

public class DataProcessingParameters extends ControlVocabularySet {

	private static DataProcessingParameters instance;
	public static final String DATA_PROCESSING_PARAMETERS_ACCESSION = "MS:1000630";

	public static DataProcessingParameters getInstance(ControlVocabularyManager cvManager) {
		if (instance == null)
			instance = new DataProcessingParameters(cvManager);
		return instance;
	}

	public DataProcessingParameters(ControlVocabularyManager cvManager) {
		super(cvManager);
		String[] parentAccessionsTMP = { DATA_PROCESSING_PARAMETERS_ACCESSION };
		this.parentAccessions = parentAccessionsTMP;

		this.miapeSection = -1;

	}

}
