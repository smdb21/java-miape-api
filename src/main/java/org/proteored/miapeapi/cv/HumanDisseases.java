package org.proteored.miapeapi.cv;

public class HumanDisseases extends ControlVocabularySet {

	/* ; */
	private static HumanDisseases instance;

	public static HumanDisseases getInstance(ControlVocabularyManager cvManager) {

		if (instance == null)
			instance = new HumanDisseases(cvManager);
		return instance;
	}

	private HumanDisseases(ControlVocabularyManager cvManager) {
		super(cvManager);
		String[] tmpParentAccessions = { "DOID:4" // Disease
		};
		this.parentAccessions = tmpParentAccessions;
		this.setExcludeParents(true);
	}
}
