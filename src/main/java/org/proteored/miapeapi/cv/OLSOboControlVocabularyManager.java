package org.proteored.miapeapi.cv;

/**
 * Ontology manager that reads the controlv vocabularies from OBO files
 * integrated in the ProteoRed Java MIAPE API. They may not be up to date, but
 * instantiate it should be faster that using
 * {@link OboControlVocabularyManager}
 * 
 * @author Salva
 * 
 */
public class OLSOboControlVocabularyManager extends OboControlVocabularyManager implements
		ControlVocabularyManager {

	public OLSOboControlVocabularyManager() {
		super(OboControlVocabularyManager.OLS_OBO, true);
	}
}
