package org.proteored.miapeapi.cv.ms;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularySet;

public class ExperimentType extends ControlVocabularySet {

	private static ExperimentType instance;

	public static ExperimentType getInstance(ControlVocabularyManager cvManager) {
		if (instance == null)
			instance = new ExperimentType(cvManager);
		return instance;
	}

	private ExperimentType(ControlVocabularyManager cvManager) {
		super(cvManager);
		// data processing action
		String[] parentAccessionsTMP = { "PRIDE:0000426" // Name: Mass
															// spectrometry
															// proteomics
															// experimental
															// strategy

				, "PRIDE:0000309" // gel free quantification method
		};
		parentAccessions = parentAccessionsTMP;
		setExcludeParents(true);
		miapeSection = -1;

	}

}
