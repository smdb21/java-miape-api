package org.proteored.miapeapi.cv.msi;

import org.proteored.miapeapi.cv.Accession;
import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularySet;
import org.proteored.miapeapi.cv.ControlVocabularyTerm;

public class SearchType extends ControlVocabularySet {
	/*
	 * PMF_SEARCH("MS:1001081", "pmf search"), DE_NOVO_SEARCH("MS:1001010",
	 * "de novo search"), MS_MS_SEARCH("MS:1001083", "ms-ms search"),
	 * TAG_SEARCH("MS:1001082", "tag search"),
	 * SPECTRAL_LIBRARY_SEARCH("MS:1001031", "spectral library search"),
	 * COMBINED_PMF_MS_MS_SEARCH("MS:1001584", "combined pmf + ms-ms search"),
	 * SEARCH_TYPR("MS:1001080", "search type");
	 */

	private static final Accession PMF_SEARCH = new Accession("MS:1001081");
	private static final Accession COMBINED_PMF_MS_MS_SEARCH = new Accession("MS:1001584");
	private static final Accession MS_MS_SEARCH = new Accession("MS:1001083");
	private static SearchType instance;

	public static SearchType getInstance(ControlVocabularyManager cvManager) {
		if (instance == null)
			instance = new SearchType(cvManager);
		return instance;
	}

	private SearchType(ControlVocabularyManager cvManager) {
		super(cvManager);
		String[] parentAccessionsTMP = { "MS:1001080", "PRIDE:0000157" };
		this.parentAccessions = parentAccessionsTMP;

		this.miapeSection = 307;

	}

	public static ControlVocabularyTerm getMSMSSearchTerm(ControlVocabularyManager cvManager) {
		return getInstance(cvManager).getCVTermByAccession(MS_MS_SEARCH);
	}

	public static ControlVocabularyTerm getCombinedPMFMSMSSearchTerm(
			ControlVocabularyManager cvManager) {
		return getInstance(cvManager).getCVTermByAccession(COMBINED_PMF_MS_MS_SEARCH);
	}

	public static ControlVocabularyTerm getPMFSearchTerm(ControlVocabularyManager cvManager) {
		return getInstance(cvManager).getCVTermByAccession(PMF_SEARCH);
	}
}
