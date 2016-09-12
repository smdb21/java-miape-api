package org.proteored.miapeapi.cv.ms;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularySet;

public class LaserAttribute extends ControlVocabularySet {
	// Children of "laser attribute" MS:1000841

	public static final String LASER_ACCESION = "MS:1000840";
	public static final String LASER_WAVELENGTH = "MS:1000843";
	public static final String LASER_ATTRIBUTE = "MS:1000841";
	private static LaserAttribute instance;

	public static LaserAttribute getInstance(ControlVocabularyManager cvManager) {
		if (instance == null)
			instance = new LaserAttribute(cvManager);
		return instance;
	}

	private LaserAttribute(ControlVocabularyManager cvManager) {
		super(cvManager);
		String[] parentAccessionsTMP = { LASER_ATTRIBUTE }; // LASER ATTRIBUTE
		this.parentAccessions = parentAccessionsTMP;
		this.miapeSection = -1;

	}
}
