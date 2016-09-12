package org.proteored.miapeapi.cv.msi;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularySet;

public class PrideProject extends ControlVocabularySet {

	private static PrideProject instance;

	public static PrideProject getInstance(ControlVocabularyManager cvManager) {
		if (instance == null)
			instance = new PrideProject(cvManager);
		return instance;
	}

	private PrideProject(ControlVocabularyManager cvManager) {
		super(cvManager);
		String[] explicitAccessionsTMP = { "PRIDE:0000097" };
		this.explicitAccessions = explicitAccessionsTMP;

	}
}
