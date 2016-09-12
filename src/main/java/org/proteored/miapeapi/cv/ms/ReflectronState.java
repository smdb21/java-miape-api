package org.proteored.miapeapi.cv.ms;

import org.proteored.miapeapi.cv.Accession;
import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularySet;

public class ReflectronState extends ControlVocabularySet {
	public static final Accession REFLECTRON_STATE_ACCESSION = new Accession("MS:1000021");
	/*
	 * REFLECTRON_OFF ("MS:1000105", "reflectron off"),
	 * REFLECTRON_ON("MS:1000106", "reflectron on"),
	 * REFLECTRON_STATE("MS:1000021", "reflectron state") ;
	 */
	private static ReflectronState instance;

	public static ReflectronState getInstance(ControlVocabularyManager cvManager) {
		if (instance == null)
			instance = new ReflectronState(cvManager);
		return instance;
	}

	private ReflectronState(ControlVocabularyManager cvManager) {
		super(cvManager);
		String[] parentAccessionsTMP = { REFLECTRON_STATE_ACCESSION.toString() };
		this.parentAccessions = parentAccessionsTMP;

		this.miapeSection = 222;

	}

}
