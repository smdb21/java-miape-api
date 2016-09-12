package org.proteored.miapeapi.cv;

/**
 * Ontology manager that reads the controlv vocabularies from OBO files
 * integrated in the ProteoRed Java MIAPE API. They may not be up to date, but
 * instantiate it should be faster that using.
 * It is the same as {@link LocalOboControlVocabularyManager} but using just one
 * ontology. {@link OboControlVocabularyManager}
 * 
 * @author Salva
 * 
 */
public class LocalOboTestControlVocabularyManager extends OboControlVocabularyManager implements
		ControlVocabularyManager {

	public LocalOboTestControlVocabularyManager() {
		super(OboControlVocabularyManager.LOCAL_OBO_TEST, false);
	}
}
