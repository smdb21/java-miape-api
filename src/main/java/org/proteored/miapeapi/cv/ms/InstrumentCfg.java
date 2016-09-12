package org.proteored.miapeapi.cv.ms;

import org.proteored.miapeapi.cv.Accession;
import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularySet;
import org.proteored.miapeapi.cv.ControlVocabularyTerm;
import org.proteored.miapeapi.cv.ControlVocabularyTermImpl;

public class InstrumentCfg extends ControlVocabularySet {
	private static Accession INSTRUMENT_CONFIGURATION_ACC = new Accession("MS:1000463");
	private static String INSTRUMENT_CONFIGURATION_PREFERRED_NAME = "instrument configuration";

	private static InstrumentCfg instance;

	public static InstrumentCfg getInstance(ControlVocabularyManager cvManager) {
		if (instance == null)
			instance = new InstrumentCfg(cvManager);
		return instance;
	}

	// "MS:1000031" -> instrument model
	private InstrumentCfg(ControlVocabularyManager cvManager) {
		super(cvManager);
		String[] explicitAccessionsTMP = { INSTRUMENT_CONFIGURATION_ACC.toString() };
		this.explicitAccessions = explicitAccessionsTMP;

		this.miapeSection = -1;

	}

	public static ControlVocabularyTerm getInstrumentConfigurationTerm() {
		return new ControlVocabularyTermImpl(INSTRUMENT_CONFIGURATION_ACC,
				INSTRUMENT_CONFIGURATION_PREFERRED_NAME);
	}
}
