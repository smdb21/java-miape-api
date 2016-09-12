package org.proteored.miapeapi.cv.msi;

import org.proteored.miapeapi.cv.Accession;
import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularySet;
import org.proteored.miapeapi.cv.ControlVocabularyTerm;

public class ValidationType extends ControlVocabularySet {
	/*
	 * NO_THRESHOLD("MS:1001494", "no threshold"),
	 * PEP_FDR_THRESHOLD("MS:1001448", "pep:FDR threshold"),
	 * PROT_FDR_THRESHOLD("MS:1001447","prot:FDR threshold"),
	 * QUALITY_ESTIMATION_BY_MANUAL_VALIDATION
	 * ("MS:1001058","quality estimation by manual validation"),
	 * QUALITY_ESTIMATION_METHOD_DETAILS("MS:1001060",
	 * "quality estimation method details"),
	 * QUALITY_ESTIMATION_WITH_DECOY_DATABASE("MS:1001194",
	 * "quality estimation with decoy database"),
	 * QUALITY_ESTIMATION_WITH_IMPLICITE_DECOY_SEQUENCES("MS:1001454",
	 * "quality estimation with implicite decoy sequences"),
	 * REPORT_ONLY_SPECTRA_ASSIGNED_TO_IDENTIFIED_PROTEINS("MS:1001574",
	 * "Report Only Spectra Assigned to Identified Proteins");
	 */
	private static ValidationType instance;
	private static Accession LOCAL_FDR_ACC = new Accession("MS:1001250");

	public static ValidationType getInstance(ControlVocabularyManager cvManager) {
		if (instance == null)
			instance = new ValidationType(cvManager);
		return instance;
	}

	private ValidationType(ControlVocabularyManager cvManager) {
		super(cvManager);
		String[] parentAccessionsTMP = { "MS:1001060" };
		this.parentAccessions = parentAccessionsTMP;

		this.miapeSection = 303;

	}

	public static ControlVocabularyTerm getLocalFDRTerm(ControlVocabularyManager cvManager) {
		return getInstance(cvManager).getCVTermByAccession(LOCAL_FDR_ACC);
	}

}
