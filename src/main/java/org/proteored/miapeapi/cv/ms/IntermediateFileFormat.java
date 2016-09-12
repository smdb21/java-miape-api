package org.proteored.miapeapi.cv.ms;

import org.proteored.miapeapi.cv.Accession;
import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularySet;
import org.proteored.miapeapi.cv.ControlVocabularyTerm;

public class IntermediateFileFormat extends ControlVocabularySet {

	private static final Accession XTANDEM_XML_FILE_CV = new Accession("MS:1001401");
	private static IntermediateFileFormat instance;

	public static IntermediateFileFormat getInstance(ControlVocabularyManager cvManager) {
		if (instance == null)
			instance = new IntermediateFileFormat(cvManager);
		return instance;
	}

	private IntermediateFileFormat(ControlVocabularyManager cvManager) {
		super(cvManager);
		// "intermediate analysis format"
		String[] parentAccessionsTMP = { "MS:1001040" };
		this.parentAccessions = parentAccessionsTMP;

		this.miapeSection = -1;

	}

	public static ControlVocabularyTerm getXtandemFileTerm(ControlVocabularyManager cvManager) {
		return getInstance(cvManager).getCVTermByAccession(XTANDEM_XML_FILE_CV);
	}
}
