package org.proteored.miapeapi.cv.msi;

import org.proteored.miapeapi.cv.Accession;
import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularySet;

public class DatabaseName extends ControlVocabularySet {
	private static DatabaseName instance;

	/*
	 * DATABASE_NAME("MS:1001013", "database name"),
	 * DATABASE_IPI_ZEBRAFISH("MS:1001287", "database IPI_zebrafish"),
	 * DATABASE_IPI_RAT("MS:1001286", "database IPI_rat"),
	 * DATABASE_SWISSPROT("MS:1001104", "database SwissProt"),
	 * DATABASE_IPI_COW("MS:1001289", "database IPI_cow"),
	 * DATABASE_IPI_CHICKEN("MS:1001288", "database IPI_chicken"),
	 * DATABASE_IPI_MOUSE("MS:1001285", "database IPI_mouse"),
	 * DATABASE_IPI_ARABIDOPSIS("MS:1001290", "database IPI_arabidopsis"),
	 * DATABASE_IPI_HUMAN("MS:1001142", "database IPI_human"),
	 * DATABASE_NR("MS:1001084", "database nr"),
	 * DECOY_DB_DERIVED_FROM("MS:1001284", "decoy DB derived from"),
	 * DECOY_DB_FROM_IPI_HUMAN("MS:1001300", "decoy DB from IPI_human"),
	 * DECOY_DB_FROM_NR("MS:1001291", "decoy DB from nr"),
	 * DECOY_DB_FROM_IPI_RAT("MS:1001292", "decoy DB from IPI_rat"),
	 * DECOY_DB_FROM_EST("MS:1001295", "decoy DB from EST"),
	 * DECOY_DB_FROM_IPI_ZEBRAFISH("MS:1001296", "decoy DB from IPI_zebrafish"),
	 * DECOY_DB_FROM_IPI_MOUSE("MS:1001293", "decoy DB from IPI_mouse"),
	 * DECOY_DB_FROM_IPI_ARABIDOPSIS("MS:1001294",
	 * "decoy DB from IPI_arabidopsis"), DECOY_DB_FROM_IPI_COW("MS:1001299",
	 * "decoy DB from IPI_cow"), DECOY_DB_FROM_SWISSPROT("MS:1001297",
	 * "decoy DB from SwissProt"), DECOY_DB_FROM_IPI_CHICKEN("MS:1001298",
	 * "decoy DB from IPI_chicken");
	 */
	public static final Accession DECOY_DB_DETAILS = new Accession("MS:1001450");
	public static final Accession DATABASE_NAME = new Accession("MS:1001013");

	public static DatabaseName getInstance(ControlVocabularyManager cvManager) {
		if (instance == null)
			instance = new DatabaseName(cvManager);
		return instance;
	}

	private DatabaseName(ControlVocabularyManager cvManager) {
		super(cvManager);

		String[] parentAccessionsTMP = { DATABASE_NAME.toString(), DECOY_DB_DETAILS.toString() };
		parentAccessions = parentAccessionsTMP;
		String[] explicitAccessionsTMP = {};
		explicitAccessions = explicitAccessionsTMP;
		miapeSection = 301;

	}

}
