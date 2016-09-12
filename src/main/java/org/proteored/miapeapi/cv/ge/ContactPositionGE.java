package org.proteored.miapeapi.cv.ge;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularySet;

public class ContactPositionGE extends ControlVocabularySet {
	/*
	 * POSITION("sep:00030", "position"), READER("sep:00033", "reader"),
	 * RESEARCHER("sep:00034", "researcher"),
	 * PRINCIPLE_INVESTIGATOR("sep:00035", "principle investigator"),
	 * LECTURER("sep:00031", "lecturer"), PROFESSOR("sep:00032", "professor");
	 */
	private static ContactPositionGE instance;

	public static ContactPositionGE getInstance(ControlVocabularyManager cvManager) {
		if (instance == null)
			instance = new ContactPositionGE(cvManager);
		return instance;
	}

	private ContactPositionGE(ControlVocabularyManager cvManager) {
		super(cvManager);
		String[] parentAccessionsTMP = { "sep:00030" };
		this.parentAccessions = parentAccessionsTMP;
		this.miapeSection = 1000;

	}
}
