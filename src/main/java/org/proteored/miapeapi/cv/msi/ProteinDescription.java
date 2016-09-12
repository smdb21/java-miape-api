package org.proteored.miapeapi.cv.msi;

import org.proteored.miapeapi.cv.Accession;
import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularySet;
import org.proteored.miapeapi.cv.ControlVocabularyTerm;
import org.proteored.miapeapi.cv.ControlVocabularyTermImpl;

public class ProteinDescription extends ControlVocabularySet {
	private static final Accession PRIDE_PROTEIN_DESCRIPTION_LINE_ACC = new Accession(
			"PRIDE:0000063");
	private static final Accession PSI_PROTEIN_DESCRIPTION_ACC = new Accession("MS:1001088");
	public static final ControlVocabularyTerm PRIDE_PROTEIN_DESCRIPTION_LINE = new ControlVocabularyTermImpl(
			PRIDE_PROTEIN_DESCRIPTION_LINE_ACC, "protein description line");
	public static final ControlVocabularyTerm PSI_PROTEIN_DESCRIPTION = new ControlVocabularyTermImpl(
			PSI_PROTEIN_DESCRIPTION_ACC, "protein description");

	private static ProteinDescription instance;

	public static ProteinDescription getInstance(ControlVocabularyManager cvManager) {
		if (instance == null)
			instance = new ProteinDescription(cvManager);
		return instance;
	}

	private ProteinDescription(ControlVocabularyManager cvManager) {
		super(cvManager);
		// protein description (MS:1001088) and protein description line
		// (PRIDE:0000063)

		String[] explicitAccessionsTMP = { PSI_PROTEIN_DESCRIPTION_ACC.toString(),
				PRIDE_PROTEIN_DESCRIPTION_LINE_ACC.toString() };
		this.explicitAccessions = explicitAccessionsTMP;

		this.miapeSection = -1;

	}
}
