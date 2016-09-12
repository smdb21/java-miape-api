package org.proteored.miapeapi.cv.gi;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularySet;

public class ImageAnalysisSoftwareName extends ControlVocabularySet {
	private static ImageAnalysisSoftwareName instance;

	/*
	 * SAMESPOTS("sep:00069", "SameSpots"), PROGENISIS_PG240("sep:00068",
	 * "progenisis PG240"), PROGENISIS_PG200("sep:00066", "progenisis PG200"),
	 * PROGENISIS("sep:00063", "progenisis"), PROGENISS_PG220("sep:00067",
	 * "progeniss PG220"), DECYDER("sep:00070", "DeCyder"), DELTA2D("sep:00071",
	 * "Delta2D"), IMAGE_ANALYSIS_SOFTWARE("sep:00062",
	 * "image analysis software"), ;
	 */
	public static ImageAnalysisSoftwareName getInstance(ControlVocabularyManager cvManager) {
		if (instance == null)
			instance = new ImageAnalysisSoftwareName(cvManager);
		return instance;
	}

	private ImageAnalysisSoftwareName(ControlVocabularyManager cvManager) {
		super(cvManager);
		String[] parentAccessionsTMP = { "sep:00062" };
		this.parentAccessions = parentAccessionsTMP;
		this.miapeSection = 101;

	}
}
