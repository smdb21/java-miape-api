package org.proteored.miapeapi.cv.ms;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularySet;

public class TargetList extends ControlVocabularySet {

	private static TargetList instance;
	public static final String TARGET_LIST_ACCESSION = "MS:1000918";

	public static TargetList getInstance(ControlVocabularyManager cvManager) {
		if (instance == null)
			instance = new TargetList(cvManager);
		return instance;
	}

	private TargetList(ControlVocabularyManager cvManager) {
		super(cvManager);

		String[] parentAccessionsTMP = { TARGET_LIST_ACCESSION };
		this.parentAccessions = parentAccessionsTMP;
		this.setExcludeParents(false);
		this.miapeSection = -1;
	}
}
