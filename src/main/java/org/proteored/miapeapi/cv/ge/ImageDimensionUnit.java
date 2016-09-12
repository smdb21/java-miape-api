package org.proteored.miapeapi.cv.ge;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularySet;

public class ImageDimensionUnit extends ControlVocabularySet {
	private static ImageDimensionUnit instance;

	/*
	 * CENTIMETER("UO:0000015", "centimeter"), MILLIMETER("UO:0000016",
	 * "millimeter"), WIDTH_IN_PIXELS("PRIDE:0000105", "Width in pixels",
	 * "PRIDE"), HEIGHT_IN_PIXELS("PRIDE:0000106", "Height in pixels", "PRIDE")
	 * ;
	 */
	public static ImageDimensionUnit getInstance(ControlVocabularyManager cvManager) {
		if (instance == null)
			instance = new ImageDimensionUnit(cvManager);
		return instance;
	}

	private ImageDimensionUnit(ControlVocabularyManager cvManager) {
		super(cvManager);
		this.miapeSection = 30;
		String[] explicitAccessionsTMP = { "UO:0000015", "UO:0000016", "PRIDE:0000105",
				"PRIDE:0000106" };
		this.explicitAccessions = explicitAccessionsTMP;
	}
}
