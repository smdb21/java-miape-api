package org.proteored.miapeapi.cv.ge;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularySet;

public class ImageAcquisitionSoftwareName extends ControlVocabularySet {
	/* IMAGE_ACQUISITION_SOFTWARE("sep:00062", "image analysis software"); */

	private static ImageAcquisitionSoftwareName instance;

	public static ImageAcquisitionSoftwareName getInstance(ControlVocabularyManager cvManager) {
		if (instance == null)
			instance = new ImageAcquisitionSoftwareName(cvManager);
		return instance;
	}

	private ImageAcquisitionSoftwareName(ControlVocabularyManager cvManager) {
		super(cvManager);
		String[] parentAccessionsTMP = { "sep:00062", "sep:00061" };
		this.parentAccessions = parentAccessionsTMP;
		this.miapeSection = 51;

	}

}
