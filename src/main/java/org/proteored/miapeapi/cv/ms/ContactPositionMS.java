package org.proteored.miapeapi.cv.ms;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularySet;

public class ContactPositionMS extends ControlVocabularySet {

	/*
	 * ROLE_TYPE ("MS:1001266", "role type"), SOFTWARE_VENDOR
	 * ("MS:1001267","software vendor"), RESEARCHER ("MS:1001271",
	 * "researcher"), LAB_PERSONNEL("MS:1001270", "lab personnel"), PROGRAMMER
	 * ("MS:1001268", "programmer"), INSTRUMENT_VENDOR("MS:1001269",
	 * "instrument vendor") ;
	 */

	public static final String SOFTWARE_VENDOR = "software vendor";
	private static ContactPositionMS instance;

	public static ContactPositionMS getInstance(ControlVocabularyManager cvManager) {
		if (instance == null)
			instance = new ContactPositionMS(cvManager);
		return instance;
	}

	private ContactPositionMS(ControlVocabularyManager cvManager) {
		super(cvManager);
		String[] parentAccessionsTMP = { "MS:1001266" };// role type
		this.parentAccessions = parentAccessionsTMP;
		this.miapeSection = 204;

	}
}
