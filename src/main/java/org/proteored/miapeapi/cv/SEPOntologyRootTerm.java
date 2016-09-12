package org.proteored.miapeapi.cv;

/**
 * This CV set represents to the whole SEP ontology
 * 
 * @author Salva
 * 
 */
public class SEPOntologyRootTerm extends ControlVocabularySet {

	private static SEPOntologyRootTerm instance;

	public static SEPOntologyRootTerm getInstance(
			ControlVocabularyManager cvManager) {
		if (instance == null)
			instance = new SEPOntologyRootTerm(cvManager);
		return instance;
	}

	private SEPOntologyRootTerm(ControlVocabularyManager cvManager) {
		super(cvManager);
		String[] parentTMP = { "sep:00001" };
		this.parentAccessions = parentTMP;

		setExcludeParents(false);

	}

}
