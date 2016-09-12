package org.proteored.miapeapi.cv.ms;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularySet;

public class DataType extends ControlVocabularySet {
	/*
	 * SPECTRUM_REPRESENTATION("MS:1000525", "spectrum representation"),
	 * CENTROID_SPECTRUM("MS:1000127", "centroid spectrum"),
	 * PROFILE_SPECTRUM("MS:1000128", "profile spectrum"),
	 * ZOOM_SCAN("MS:1000497", "zoom scan") ;
	 */
	private static DataType instance;

	public static DataType getInstance(ControlVocabularyManager cvManager) {
		if (instance == null)
			instance = new DataType(cvManager);
		return instance;
	}

	private DataType(ControlVocabularyManager cvManager) {
		super(cvManager);
		String[] explicitAccessionsTMP = { "MS:1000497" };
		this.explicitAccessions = explicitAccessionsTMP;
		String[] parentAccessionsTMP = { "MS:1000525" };
		this.parentAccessions = parentAccessionsTMP;

		this.miapeSection = 215;

	}
}
