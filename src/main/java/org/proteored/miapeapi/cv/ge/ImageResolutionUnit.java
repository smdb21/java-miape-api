package org.proteored.miapeapi.cv.ge;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularySet;

public class ImageResolutionUnit extends ControlVocabularySet {
	private static ImageResolutionUnit instance;

	/*
	 * PIXELS_PER_INCH("UO:0000242", "pixels per inch"),
	 * PIXELS_PER_MILLIMETER("UO:0000243", "pixels per millimeter"),
	 * DOTS_PER_INCH("UO:0000240", "dots per inch"), MICROMETER("UO:0000017",
	 * "micrometer");
	 */
	public static ImageResolutionUnit getInstance(ControlVocabularyManager cvManager) {
		if (instance == null)
			instance = new ImageResolutionUnit(cvManager);
		return instance;
	}

	private ImageResolutionUnit(ControlVocabularyManager cvManager) {
		super(cvManager);
		this.miapeSection = 31;
		String[] explicitAccessionsTMP = { "UO:0000242", "UO:0000243", "UO:0000240", "UO:0000017" };
		this.explicitAccessions = explicitAccessionsTMP;

	}
}
