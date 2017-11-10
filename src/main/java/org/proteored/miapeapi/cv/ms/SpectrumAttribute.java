package org.proteored.miapeapi.cv.ms;

import org.proteored.miapeapi.cv.Accession;
import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularySet;

public class SpectrumAttribute extends ControlVocabularySet {
	public static final Accession SPECTRUM_TITLE_ACC = new Accession("MS:1000796");
	private static SpectrumAttribute instance;

	public static SpectrumAttribute getInstance(ControlVocabularyManager cvManager) {
		if (instance == null)
			instance = new SpectrumAttribute(cvManager);
		return instance;
	}

	private SpectrumAttribute(ControlVocabularyManager cvManager) {
		super(cvManager);
		String[] parentAccessionsTMP = { "MS:1000499" }; // spectrum
															// attribute
		this.parentAccessions = parentAccessionsTMP;
		this.miapeSection = -1;

	}

}
