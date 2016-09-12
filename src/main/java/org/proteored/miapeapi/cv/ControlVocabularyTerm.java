package org.proteored.miapeapi.cv;

/**
 * Defines which information have a CV term
 * 
 * @author Salva
 * 
 */
public interface ControlVocabularyTerm {

	public String getPreferredName();

	public Accession getTermAccession();

	public String getCVRef();

}
