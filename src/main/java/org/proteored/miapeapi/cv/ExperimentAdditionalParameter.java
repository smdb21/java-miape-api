package org.proteored.miapeapi.cv;

public class ExperimentAdditionalParameter extends ControlVocabularySet {

	private static ExperimentAdditionalParameter instance;
	private static Accession TISSUE_NOT_APPLICABLE = new Accession(
			"PRIDE:0000442");

	public static ExperimentAdditionalParameter getInstance(
			ControlVocabularyManager cvManager) {

		if (instance == null)
			instance = new ExperimentAdditionalParameter(cvManager);
		return instance;
	}

	private ExperimentAdditionalParameter(ControlVocabularyManager cvManager) {
		super(cvManager);
		String[] tmpParentAccessions = {

		"PRIDE:0000006" // Experiment additional parameter

		};
		parentAccessions = tmpParentAccessions;
		setExcludeParents(true);
	}

	public ControlVocabularyTerm getTissueNotApplicableTerm() {
		return getCVTermByAccession(TISSUE_NOT_APPLICABLE);
	}
}
