package org.proteored.miapeapi.cv.ms;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularySet;

public class RetentionTime extends ControlVocabularySet {
	// Children of "laser attribute" MS:1000841

	public static final String RetentionTimes = "MS:1001114";
	public static final String RetentionTime = "MS:1000894";
	public static final String RetentionTimeMinutes = "PRIDE:0000203";

	private static RetentionTime instance;

	public static RetentionTime getInstance(ControlVocabularyManager cvManager) {
		if (instance == null)
			instance = new RetentionTime(cvManager);
		return instance;
	}

	private RetentionTime(ControlVocabularyManager cvManager) {
		super(cvManager);
		String[] parentAccessionsTMP = { RetentionTime };
		this.parentAccessions = parentAccessionsTMP;
		String[] explicitAccessionsTMP = { RetentionTimes, RetentionTimeMinutes };
		this.explicitAccessions = explicitAccessionsTMP;
		this.miapeSection = -1;

	}
}
