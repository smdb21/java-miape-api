package org.proteored.miapeapi.cv.ge;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularySet;

public class BufferType extends ControlVocabularySet {
	private static BufferType instance;

	/*
	 * TRANSFER_BUFFER("sep:00125", "transfer buffer"),
	 * LAEMELLI_BUFFER("sep:00126", "Laemelli buffer"),
	 * CARRIER_AMPHOLYTE_BUFFER("sep:00120", "carrier ampholyte buffer"),
	 * REHYDRATION_BUFFER("sep:00123", "rehydration buffer"),
	 * SAMPLE_BUFFER("sep:00124", "sample buffer"),
	 * ELECTROPHORESIS_BUFFER("sep:00121", "electrophoresis buffer"),
	 * BUFFER_SOLUTION("sep:00118", "buffer solution"),
	 * LOADING_BUFFER("sep:00122", "loading buffer"),
	 * EQUILIBRATION_BUFFER("sep:00198", "equilibration buffer"),
	 * SOLUTION("sep:00115", "solution"), AGAROSE_SEALING_SOLUTION("sep:00117",
	 * "agarose sealing solution"), FIXATION_SOLUTION("sep:00119",
	 * "fixation solution");
	 */
	public static BufferType getInstance(ControlVocabularyManager cvManager) {
		if (instance == null)
			instance = new BufferType(cvManager);
		return instance;
	}

	private BufferType(ControlVocabularyManager cvManager) {
		super(cvManager);
		String[] parentAccessionsTMP = { "sep:00115" };
		this.parentAccessions = parentAccessionsTMP;
		this.miapeSection = 47;

	}

}
