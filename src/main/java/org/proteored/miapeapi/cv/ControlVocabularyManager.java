package org.proteored.miapeapi.cv;

import java.util.HashMap;
import java.util.List;

/**
 * Interface that defines the functions that a CV manager, that manage CV terms
 * from MIAPE sections
 * 
 * @author Salva
 * 
 */
public interface ControlVocabularyManager {

	/**
	 * Gets the identifier of a CV looking in the possible CVs allowed in a CV
	 * set
	 * 
	 * @param name
	 *            the name of a CV
	 * @param cvSet
	 * @return the identifier of the CV or null if it is not found
	 */
	public Accession getControlVocabularyId(String name, ControlVocabularySet cvSet);

	/**
	 * Gets the name of a CV looking in the possible CVs allowed in a CV set
	 * 
	 * @param id
	 *            identifier of the CV
	 * @param cvSet
	 * @return the name of the CV
	 */
	public String getControlVocabularyName(Accession id, ControlVocabularySet cvSet);

	/**
	 * Gets the ontology identifier from a CV looking in the possible CVs
	 * allowed in a CV set
	 * 
	 * @param id
	 *            identifier of the CV
	 * @param cvSet
	 * @return the ontology identifier from a CV
	 */
	public String getCVRef(Accession id, ControlVocabularySet cvSet);

	/**
	 * Check is the term is a CV or not looking in the possible values of a CV
	 * Set
	 * 
	 * @param cvDescription
	 * @param cvSet
	 * @return true if the term is a CV
	 */
	public boolean isCV(String cvDescription, ControlVocabularySet cvSet);

	/**
	 * Gets all accessions from a certain CV Set
	 * 
	 * @param cvSet
	 * @return a list of accessions
	 */
	public List<ControlVocabularyTerm> getAllCVsFromCVSet(ControlVocabularySet cvSet);

	/**
	 * Gets the {@link ControlVocabularyTerm} from the CV Set searching by
	 * {@link Accession}
	 * 
	 * @param accession
	 * @param cvSet
	 * @return the cv term
	 */
	public ControlVocabularyTerm getCVTermByAccession(Accession accession,
			ControlVocabularySet cvSet);

	/**
	 * Get the parents of a cv term a hash map with the String accession and the
	 * term
	 * 
	 * @param accession
	 * @return The has map
	 * @throws UnsupportedOperationException
	 */
	public HashMap<String, ControlVocabularyTerm> getAccesionParents(Accession accession)
			throws UnsupportedOperationException;

	/**
	 * Check if a CV term is son of other one
	 * 
	 * @param accession
	 * @param potentialParent
	 * @return true if it is son of the other or false if not
	 * @throws UnsupportedOperationException
	 */
	public boolean isSonOf(Accession accession, Accession potentialParent)
			throws UnsupportedOperationException;

}
