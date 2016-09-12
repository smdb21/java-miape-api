package org.proteored.miapeapi.cv.ms;

import org.proteored.miapeapi.cv.Accession;
import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularySet;

public class InstrumentCustomization extends ControlVocabularySet {
	public static Accession INSTRUMENT_CUSTOMIZATION_ACC = new Accession("MS:1000032");

	private static InstrumentCustomization instance;

	public static InstrumentCustomization getInstance(ControlVocabularyManager cvManager) {
		if (instance == null)
			instance = new InstrumentCustomization(cvManager);
		return instance;
	}

	// "MS:1000031" -> instrument model
	private InstrumentCustomization(ControlVocabularyManager cvManager) {
		super(cvManager);
		String[] explicitAccessionsTMP = { INSTRUMENT_CUSTOMIZATION_ACC.toString() };
		this.explicitAccessions = explicitAccessionsTMP;

		this.miapeSection = -1;

	}

}
