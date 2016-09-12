package org.proteored.miapeapi.cv.ms;

import org.proteored.miapeapi.cv.Accession;
import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularySet;
import org.proteored.miapeapi.cv.ControlVocabularyTerm;
import org.proteored.miapeapi.cv.ControlVocabularyTermImpl;

public class MOverZ extends ControlVocabularySet {
	private static final Accession M_OVER_Z_ACCESSION = new Accession("MS:1000040");
	private static final Accession SELECTED_ION_M_OVER_Z_ACCESSION = new Accession("MS:1000744");

	private static MOverZ instance;

	public static MOverZ getInstance(ControlVocabularyManager cvManager) {
		if (instance == null)
			instance = new MOverZ(cvManager);
		return instance;
	}

	private MOverZ(ControlVocabularyManager cvManager) {
		super(cvManager);
		String[] explicitAccessionsTMP = { M_OVER_Z_ACCESSION.toString(),
				SELECTED_ION_M_OVER_Z_ACCESSION.toString() };
		this.explicitAccessions = explicitAccessionsTMP;

		this.miapeSection = -1;

	}

	public static ControlVocabularyTerm getMOverZTerm(ControlVocabularyManager cvManager) {
		final String preferredName = MOverZ.getInstance(cvManager)
				.getCVTermByAccession(M_OVER_Z_ACCESSION).getPreferredName();
		return new ControlVocabularyTermImpl(M_OVER_Z_ACCESSION, preferredName);
	}

	public static ControlVocabularyTerm getSelected_Ion_MOverZTerm(
			ControlVocabularyManager cvManager) {
		final String preferredName = MOverZ.getInstance(cvManager)
				.getCVTermByAccession(SELECTED_ION_M_OVER_Z_ACCESSION).getPreferredName();
		return new ControlVocabularyTermImpl(SELECTED_ION_M_OVER_Z_ACCESSION, preferredName);
	}
}
