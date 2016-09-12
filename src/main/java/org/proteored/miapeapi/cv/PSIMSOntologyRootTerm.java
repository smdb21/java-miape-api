package org.proteored.miapeapi.cv;


/**
 * This CV set represents to the whole PSI MS ontology
 * @author Salva
 *
 */
public class PSIMSOntologyRootTerm extends ControlVocabularySet {

	private static PSIMSOntologyRootTerm instance;

	public static PSIMSOntologyRootTerm getInstance(
			ControlVocabularyManager cvManager) {
		if (instance == null)
			instance = new PSIMSOntologyRootTerm(cvManager);
		return instance;
	}

	private PSIMSOntologyRootTerm(ControlVocabularyManager cvManager) {
		super(cvManager);
		String[] parentTMP = { "MS:0000000" };
		this.parentAccessions = parentTMP;

		setExcludeParents(false);

	}

}
