package org.proteored.miapeapi.cv.msi;

import org.proteored.miapeapi.cv.Accession;
import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularySet;
import org.proteored.miapeapi.cv.ControlVocabularyTerm;

public class PeptideModificationName extends ControlVocabularySet {

	public static final Accession FRAGMENT_NEUTRAL_LOSS_ACCESSION = new Accession("MS:1001524");
	public static final Accession PEPTIDE_MODIFICATION_DETAILS_ACC = new Accession("MS:1001471");

	public static final String UNIMOD35 = "UNIMOD:35";
	public static final String UNIMOD21 = "UNIMOD:21";
	public static final String UNIMOD28 = "UNIMOD:28";
	public static final String UNIMOD4 = "UNIMOD:4";
	public static final String UNIMOD1 = "UNIMOD:1";
	public static final String UNIMOD214 = "UNIMOD:214";
	public static final String UNIMOD39 = "UNIMOD:39";
	public static final String UNIMOD7 = "UNIMOD:7";
	public static final String UNIMOD730 = "UNIMOD:730";
	public static final String UNIMOD364 = "UNIMOD:364";
	public static final String UNIMOD29 = "UNIMOD:29";
	public static final String UNIMOD27 = "UNIMOD:27";

	public static final String MOD_01528 = "MOD:01528";
	public static final String MOD_00061 = "MOD:00061";
	public static final String MOD_00420 = "MOD:00420";
	public static final String MOD_00564 = "MOD:00564";
	public static final String MOD_00042 = "MOD:00042";
	public static final String MOD_00040 = "MOD:00040";
	public static final String MOD_00422 = "MOD:00422";
	public static final String MOD_00412 = "MOD:00412";
	public static final String MOD_00789 = "MOD:00789";
	public static final String MOD_00110 = "MOD:00110";
	public static final String MOD_00397 = "MOD:00397";
	public static final String MOD_00137 = "MOD:00137";

	// All modification that you add here, has to be also present in the table
	// MIAPE_CV of the database.
	// To do it, go to MIAPE CV Manager
	/*
	 * OXIDATION("UNIMOD:35", "Oxidation", "UNIMOD"),
	 * FRAGMENT_NEUTRAL_LOSS_ACC("MS:1001524", "fragment neutral loss", "MS"),
	 * GLN_PYRO_GLU("UNIMOD:28", "Gln->pyro-Glu", "UNIMOD"),
	 * CARBAMIDOMETHYL("UNIMOD:4", "Carbamidomethyl", "UNIMOD"),
	 * ACETYL("UNIMOD:1", "Acetyl", "UNIMOD"), ITRAQ4PLEX("UNIMOD:214",
	 * "iTRAQ4plex", "UNIMOD"), METHYLTHIO("UNIMOD:39", "Methylthio", "UNIMOD"),
	 * DEAMIDATED("UNIMOD:7", "Deamidated", "UNIMOD"), ITRAQ8PLEX("UNIMOD:730",
	 * "iTRAQ8plex", "UNIMOD"), ICPL_13C_6("UNIMOD:364", "ICPL:13C(6)",
	 * "UNIMOD"), PHOSPHO("UNIMOD:21", "Phospho", "UNIMOD"), SMA("UNIMOD:29",
	 * "SMA", "UNIMOD");
	 */
	private static PeptideModificationName instance;

	public static PeptideModificationName getInstance(ControlVocabularyManager cvManager) {
		if (instance == null)
			instance = new PeptideModificationName(cvManager);
		return instance;
	}

	private PeptideModificationName(ControlVocabularyManager cvManager) {
		super(cvManager);
		String[] explicitAccessionsTMP = { /* UNIMOD35, */FRAGMENT_NEUTRAL_LOSS_ACCESSION
				.toString() /*
							 * , UNIMOD28, UNIMOD4, UNIMOD1, UNIMOD214,
							 * UNIMOD39, UNIMOD7, UNIMOD730, UNIMOD364,
							 * UNIMOD21, UNIMOD29, UNIMOD27, MOD_00040,
							 * MOD_00042, MOD_00061, MOD_00110, MOD_00137,
							 * MOD_00397, MOD_00412, MOD_00420, MOD_00422,
							 * MOD_00564, MOD_00789, MOD_01528
							 */};
		this.explicitAccessions = explicitAccessionsTMP;
		String[] parentAccessionsTMP = { PEPTIDE_MODIFICATION_DETAILS_ACC.toString(), "UNIMOD:0",
				"MOD:00000" };
		this.parentAccessions = parentAccessionsTMP;

		this.miapeSection = 309;

	}

	public static ControlVocabularyTerm getPepModifDetailsTerm(ControlVocabularyManager cvManager) {
		return getInstance(cvManager).getCVTermByAccession(PEPTIDE_MODIFICATION_DETAILS_ACC);
	}

	/**
	 * Get the preferred name of certain accession that are not real accessions
	 * in the obo file
	 * 
	 * Add here more accession of modifications
	 * 
	 * @param acc
	 * @return the preferred name
	 */
	// public static String getPreferredName(String acc) {
	// if (acc.equals(UNIMOD1) || acc.equals(MOD_00061))
	// return "Acetylation";
	// if (acc.equals(UNIMOD21) || acc.equals(MOD_00042))
	// return "Phosphorylation";
	// if (acc.equals(UNIMOD214) || acc.equals(MOD_00564))
	// return "iTRAQ4plex";
	// if (acc.equals(UNIMOD27) || acc.equals(MOD_00420))
	// return "Glu->pyro-Glu";
	// if (acc.equals(UNIMOD28) || acc.equals(MOD_00040))
	// return "Gln->pyro-Glu";
	// if (acc.equals(UNIMOD29) || acc.equals(MOD_00422))
	// return "SMA";
	// if (acc.equals(UNIMOD35) || acc.equals(MOD_00412))
	// return "Oxidation";
	// if (acc.equals(UNIMOD364) || acc.equals(MOD_00789))
	// return "ICPL:13C(6)";
	// if (acc.equals(UNIMOD39) || acc.equals(MOD_00110))
	// return "Methylthio";
	// if (acc.equals(UNIMOD4) || acc.equals(MOD_00397))
	// return "Carbamidomethyl";
	// if (acc.equals(UNIMOD7) || acc.equals(MOD_00137))
	// return "Deamidated";
	// if (acc.equals(UNIMOD730) || acc.equals(MOD_01528))
	// return "iTRAQ8plex";
	// return "";
	// }

}
