package org.proteored.miapeapi.cv.ms;

import org.proteored.miapeapi.cv.Accession;
import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularySet;
import org.proteored.miapeapi.cv.ControlVocabularyTerm;
import org.proteored.miapeapi.cv.ControlVocabularyTermImpl;

public class InstrumentVendor extends ControlVocabularySet {
	public static Accession INSTRUMENT_VENDOR_ACCESSION = new Accession("MS:1001269");
	public static String INSTRUMENT_VENDOR_PREFERRED_NAME = "instrument vendor";

	private static InstrumentVendor instance;

	public static InstrumentVendor getInstance(ControlVocabularyManager cvManager) {
		if (instance == null)
			instance = new InstrumentVendor(cvManager);
		return instance;
	}

	// "MS:1001269" -> instrument vendor
	private InstrumentVendor(ControlVocabularyManager cvManager) {
		super(cvManager);
		String[] parentAccessionsTMP = { INSTRUMENT_VENDOR_ACCESSION.toString() };
		this.parentAccessions = parentAccessionsTMP;
		String[] explicitAccessionsTMP = { "MS:1000121", "MS:1000490", "MS:1000495", "MS:1000122",
				"MS:1000491", "MS:1000488", "MS:1001800", "MS:1000124", "MS:1000483", "MS:1000489",
				"MS:1000126" };
		this.explicitAccessions = explicitAccessionsTMP;

		this.miapeSection = 224;

	}

	public static ControlVocabularyTerm getInstrumentVendorTerm() {
		return new ControlVocabularyTermImpl(INSTRUMENT_VENDOR_ACCESSION,
				INSTRUMENT_VENDOR_PREFERRED_NAME);
	}

}
