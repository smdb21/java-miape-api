package org.proteored.miapeapi.cv.ms;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularySet;

public class SourceInterface extends ControlVocabularySet {
	/*
	 * LASER_ACCESION("MS:1000840", "laser"), LASER_TYPE ("MS:1000842",
	 * "laser type"), GAS ("MS:1000850", "gas laser"), SOLID_STATE
	 * ("MS:1000851", "solid-state laser"), DYE("MS:1000852", "dye-laser"),
	 * FREE__ELECTRON ("MS:1000853", "free electron laser") ;
	 */

	public static final String SOURCE_INTERFACE_ACCESSION = "MS:1001931";
	public static final String SOURCE_INTERFACE_MODEL_ACCESSION = "MS:1001932";
	public static final String SOURCE_INTERFACE_MANUFACTURER_ACCESSION = "MS:1001953";
	private static SourceInterface instance;

	public static SourceInterface getInstance(ControlVocabularyManager cvManager) {
		if (instance == null)
			instance = new SourceInterface(cvManager);
		return instance;
	}

	private SourceInterface(ControlVocabularyManager cvManager) {
		super(cvManager);
		String[] parentAccessionsTMP = { SOURCE_INTERFACE_ACCESSION }; // SOURCE
																		// INTERFACE
		this.parentAccessions = parentAccessionsTMP;
		this.miapeSection = -1;
	}

}
