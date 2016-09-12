package org.proteored.miapeapi.cv.msi;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularySet;

public class DataTransformation extends ControlVocabularySet {
	/*
	 * AREA_PEAK_PICKING("MS:1000801", "area peak picking"),
	 * BASELINE_REDUCTION("MS:1000593", "baseline reduction"),
	 * BELOW_PRECURSOR("MS:1000779",
	 * "below precursor intensity dominance charge state calculation"),
	 * CHARGE_DECONVOLUTION("MS:1000034", "charge deconvolution"),
	 * CHARGE_STATE_CALCULATION("MS:1000778", "charge state calculation"),
	 * DATA_FILTERING("MS:1001486", "data filtering"),
	 * DATA_PROCESSING_ACTION("MS:1000543", "data processing action"),
	 * DEISOTOPING("MS:1000033", "deisotoping"),
	 * GAUSSIAN_SMOOTHING("MS:1000784", "Gaussian smoothing"),
	 * HEIGHT_PEAK_PICKING("MS:1000802", "height peak picking"),
	 * HIGH_INTENSITY_DATA_POINT_REMOVAL("MS:1000746",
	 * "high intensity data point removal"),
	 * INTENSITY_NORMALIZATION("MS:1001484", "intensity normalization"),
	 * LOW_INTENSITY_DATA_POINT_REMOVAL("MS:1000594",
	 * "low intensity data point removal"), LOWESS_SMOOTHING("MS:1000783",
	 * "LOWESS smoothing"), MZ_CALIBRATION("MS:1001485", "m/z calibration"),
	 * MOVING_AVERAGE_SMOOTHING("MS:1000785", "moving average smoothing"),
	 * MSPREFIX_PRECURSOR_CALCULATION("MS:1000781",
	 * "msPrefix precursor recalculation"), PEAK_PICKING("MS:1000035",
	 * "peak picking"), PRECURSOR_RECALCULATION("MS:1000780",
	 * "precursor recalculation"), RT_ALIGMENT("MS:1000745",
	 * "retention time alignment"), SAVITZKY_GOLAY_SMOOTHING("MS:1000782",
	 * "Savitzky-Golay smoothing"), SMOOTHING("MS:1000592", "smoothing"),
	 * FILEFORMATCONVERSION("MS:1000530", "file format conversion"),
	 * CONVERSION_TO_DTA("MS:1000741", "Conversion to dta"),
	 * CONVERSION_TO_MZDATA("MS:1000546", "Conversion to mzData"),
	 * CONVERSION_TO_MZML("MS:1000544", "Conversion to mzML"),
	 * CONVERSION_TO_MZXML("MS:1000545", "Conversion to mzXML");
	 */

	private static DataTransformation instance;

	public static DataTransformation getInstance(ControlVocabularyManager cvManager) {
		if (instance == null)
			instance = new DataTransformation(cvManager);
		return instance;
	}

	private DataTransformation(ControlVocabularyManager cvManager) {
		super(cvManager);
		String[] parentAccessionsTMP = { "MS:1000452" };
		this.parentAccessions = parentAccessionsTMP;

		this.miapeSection = 304; // data processing action

	}

}
