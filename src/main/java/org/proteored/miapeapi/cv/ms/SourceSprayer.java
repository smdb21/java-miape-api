package org.proteored.miapeapi.cv.ms;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularySet;

public class SourceSprayer extends ControlVocabularySet {
	/*
	 * LASER_ACCESION("MS:1000840", "laser"), LASER_TYPE ("MS:1000842",
	 * "laser type"), GAS ("MS:1000850", "gas laser"), SOLID_STATE
	 * ("MS:1000851", "solid-state laser"), DYE("MS:1000852", "dye-laser"),
	 * FREE__ELECTRON ("MS:1000853", "free electron laser") ;
	 */

	public static final String SOURCE_SPRAYER_ACCESSION = "MS:1001933";
	public static final String SOURCE_SPRAYER_TYPE_ACCESSION = "MS:1001934";
	public static final String SOURCE_SPRAYER_MODEL_ACCESSION = "MS:1001936";
	public static final String SOURCE_SPRAYER_MANUFACTURER_ACCESSION = "MS:1001935";
	private static SourceSprayer instance;

	public static SourceSprayer getInstance(ControlVocabularyManager cvManager) {
		if (instance == null)
			instance = new SourceSprayer(cvManager);
		return instance;
	}

	private SourceSprayer(ControlVocabularyManager cvManager) {
		super(cvManager);
		String[] parentAccessionsTMP = { "MS:1001933" }; // SOURCE SPRAYER
		this.parentAccessions = parentAccessionsTMP;
		this.miapeSection = -1;
	}

}
