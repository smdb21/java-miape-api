package org.proteored.miapeapi.cv;

public class TissuesTypes extends ControlVocabularySet {

	/* ; */
	private static TissuesTypes instance;

	public static TissuesTypes getInstance(ControlVocabularyManager cvManager) {

		if (instance == null)
			instance = new TissuesTypes(cvManager);
		return instance;
	}

	private TissuesTypes(ControlVocabularyManager cvManager) {
		super(cvManager);

		String[] tmpParentAccessions = { "BTO:0001489" // whole body

		};
		this.parentAccessions = tmpParentAccessions;
		this.setExcludeParents(true);
	}
}
