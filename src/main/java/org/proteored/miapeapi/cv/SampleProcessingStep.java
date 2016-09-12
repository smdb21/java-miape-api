package org.proteored.miapeapi.cv;

public class SampleProcessingStep extends ControlVocabularySet {

	private static SampleProcessingStep instance;
	private static Accession ENZYME_DIGESTION_ACC = new Accession(
			"PRIDE:0000024");

	public static SampleProcessingStep getInstance(
			ControlVocabularyManager cvManager) {

		if (instance == null)
			instance = new SampleProcessingStep(cvManager);
		return instance;
	}

	private SampleProcessingStep(ControlVocabularyManager cvManager) {
		super(cvManager);
		String[] tmpParentAccessions = {

		"PRIDE:0000001", // Protocol step description additional
							// parameter
				"sep:00101" // protocol

		};
		this.parentAccessions = tmpParentAccessions;
		this.setExcludeParents(true);
	}

	public ControlVocabularyTerm getEnzymeDigestionTerm() {
		return this.getCVTermByAccession(ENZYME_DIGESTION_ACC);
	}
}
