package org.proteored.miapeapi.cv.ge;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularySet;

public class MatrixDimensionUnit extends ControlVocabularySet {
	/*
	 * CENTIMETER("UO:0000015", "centimeter"), DIMENSIONLESS_UNIT("UO:0000186",
	 * "dimensionless unit"), MILLIMETER("UO:0000016", "millimeter"),
	 * RATIO("UO:0000190", "ratio"), PH("UO:0000196", "pH"), PI("UO:0000188",
	 * "pi"), COUNT("UO:0000189", "count")
	 */
	private static MatrixDimensionUnit instance;

	public static MatrixDimensionUnit getInstance(ControlVocabularyManager cvManager) {
		if (instance == null)
			instance = new MatrixDimensionUnit(cvManager);
		return instance;
	}

	private MatrixDimensionUnit(ControlVocabularyManager cvManager) {
		super(cvManager);
		String[] explicitAccessionsTMP = { "UO:0000015", "UO:0000186", "UO:0000016", "UO:0000190",
				"UO:0000196", "UO:0000188", "UO:0000189" };
		this.explicitAccessions = explicitAccessionsTMP;
		this.miapeSection = 25;

	}
}
