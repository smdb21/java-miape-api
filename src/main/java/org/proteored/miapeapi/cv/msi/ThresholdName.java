package org.proteored.miapeapi.cv.msi;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularySet;

public class ThresholdName extends ControlVocabularySet {
	public static String PEP_FDR_THRESHOLD = "MS:1001448";
	public static String PROT_FDR_THRESHOLD = "MS:1001447";
	public static String PEP_GLOBAL_FDR_THRESHOLD = "MS:1001364";
	public static String PROT_GLOBAL_FDR_THRESHOLD = "MS:1001214";

	public static String MASCOT_IDENTITY_THRESHOLD = "MS:1001371";
	/*
	 * MASCOT_IDENTITITY_THRESHOLD("MS:1001371","mascot:identity threshold"),
	 * NO_THRESHOLD("MS:1001494","no threshold"),
	 * OMSSA_EVALUE_THRESHOLD("MS:1001449","OMSSA e-value threshold"),
	 * PEP_FDR_THRESHOLD("MS:1001448","pep:FDR threshold"),
	 * PROT_FDR_THRESHOLD("MS:1001447","prot:FDR threshold"),
	 * MASCOT_HOMOLOGY_THRESHOLD("MS:1001370", "Mascot:homology threshold"),
	 * MASCOT_IDENTITY_THRESHOLD("MS:1001371", "Mascot:identity threshold"),
	 * PEP_FDR("MS:1001364",
	 * "pep:global FDR"), PROT_FDR("MS:1001214", "prot:global FDR") ;
	 */

	private static ThresholdName instance;

	public static ThresholdName getInstance(ControlVocabularyManager cvManager) {
		if (instance == null)
			instance = new ThresholdName(cvManager);
		return instance;
	}

	private ThresholdName(ControlVocabularyManager cvManager) {
		super(cvManager);
		String[] parentAccessionsTMP = { "MS:1001060" }; // quality estimation
															// method details
		this.parentAccessions = parentAccessionsTMP;
		String[] explicitAccessionsTMP = { MASCOT_IDENTITY_THRESHOLD, "MS:1001449"// OMSSA
																					// e-value
																					// threshold
				, "MS:1001370",// Mascot:homology threshold
				PROT_GLOBAL_FDR_THRESHOLD, PEP_GLOBAL_FDR_THRESHOLD };
		this.explicitAccessions = explicitAccessionsTMP;

		this.miapeSection = 306;

	}
}
