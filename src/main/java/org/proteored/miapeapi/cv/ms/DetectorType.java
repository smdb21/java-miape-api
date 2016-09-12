/**
 * 
 */
package org.proteored.miapeapi.cv.ms;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularySet;

public class DetectorType extends ControlVocabularySet {
	public static final String DETECTOR_TYPE_ACCESSION = "MS:1000026";
	/*
	 * DETECTOR_TYPE_ACCESSION("MS:1000026", "detector type"),
	 * PHOTO_ARRAY_DETECTOR("MS:1000621", "photodiode array detector"),
	 * ELECTRON_MULTIPLIER("MS:1000253", "electron multiplier"),
	 * FARADAY_CUP("MS:1000112", "faraday cup"),
	 * ELECTRON_MULTIPLIER_TUBE("MS:1000111", "electron multiplier tube"),
	 * INDUCTIVE_DETECTOR("MS:1000624", "inductive detector"),
	 * DALY_DETECTOR("MS:1000110", "daly detector"),
	 * ARRAY_DETECTOR("MS:1000345", "array detector"), DYNODE("MS:1000347",
	 * "dynode"), CONVERSION_DYNODE("MS:1000346", "conversion dynode"),
	 * ION_PHOTON_DETECTOR("MS:1000349", "ion-to-photon detector"),
	 * FOCAL_PLANE_COLLECTOR("MS:1000348", "focal plane collector"),
	 * POINT_COLLECTOR("MS:1000350", "point collector"),
	 * POSTACCELERATION_DETECTOR("MS:1000351", "postacceleration detector"),
	 * MICROCHANNEL_PLATE_DETECTOR("MS:1000114", "microchannel plate detector"),
	 * FOCAL_PLANE_ARRAY("MS:1000113", "focal plane array"),
	 * PHOTOMULTIPLIER("MS:1000116","photomultiplier"),
	 * MULTICOLLECTOR("MS:1000115","multi-collector"),
	 * CONVERSION_DYNODE_ELECTRON_MULTIPLIER("MS:1000108",
	 * "conversion dynode electron multiplier"),
	 * CONVERSION_DYNODE_PHOTOMULTIPLIER("MS:1000109",
	 * "conversion dynode photomultiplier"), CHANNELTRON("MS:1000107",
	 * "channeltron");
	 */
	private static DetectorType instance;

	public static DetectorType getInstance(ControlVocabularyManager cvManager) {
		if (instance == null)
			instance = new DetectorType(cvManager);
		return instance;
	}

	private DetectorType(ControlVocabularyManager cvManager) {
		super(cvManager);
		String[] parentAccessionsTMP = { "MS:1000026" };
		this.parentAccessions = parentAccessionsTMP;

		this.miapeSection = 206;

	}

}
