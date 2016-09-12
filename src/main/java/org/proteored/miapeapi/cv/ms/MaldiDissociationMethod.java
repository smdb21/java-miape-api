package org.proteored.miapeapi.cv.ms;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularySet;

public class MaldiDissociationMethod extends ControlVocabularySet {

	private static MaldiDissociationMethod instance;

	public static MaldiDissociationMethod getInstance(ControlVocabularyManager cvManager) {
		if (instance == null)
			instance = new MaldiDissociationMethod(cvManager);
		return instance;
	}

	private MaldiDissociationMethod(ControlVocabularyManager cvManager) {
		super(cvManager);
		// laser-induced decomposition (LID)
		// post-source decay (PSD)
		// in-source dissociation (ISD)
		String[] explicitAccessionsTMP = { "MS:1000135", "MS:1000393", "MS:1001880" };
		this.explicitAccessions = explicitAccessionsTMP;

		this.miapeSection = 226;

	}

}
