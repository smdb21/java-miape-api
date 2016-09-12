package org.proteored.miapeapi.cv;

public class SampleInformation extends ControlVocabularySet {
	public static Accession SAMPLE_BATCH_ACC = new Accession("MS:1000053");
	public static Accession SAMPLE_VOLUME_ACC = new Accession("MS:1000005");

	/* ; */
	private static SampleInformation instance;

	public static SampleInformation getInstance(ControlVocabularyManager cvManager) {

		if (instance == null)
			instance = new SampleInformation(cvManager);
		return instance;
	}

	private SampleInformation(ControlVocabularyManager cvManager) {
		super(cvManager);

		String[] tmpParentAccessions = { "MS:1000548", // SAMPLE ATTRIBUTE
				"BTO:0001489", // whole body
				"DOID:4" // Disease
		};
		this.parentAccessions = tmpParentAccessions;
		this.setExcludeParents(true);
	}

	public ControlVocabularyTerm getSampleBatchTerm() {
		return this.getCVTermByAccession(SAMPLE_BATCH_ACC);
	}

	public ControlVocabularyTerm getSampleVolumenTerm() {
		return this.getCVTermByAccession(SAMPLE_VOLUME_ACC);
	}
}
