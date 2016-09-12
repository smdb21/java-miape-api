package org.proteored.miapeapi.interfaces.msi;

import java.util.Set;

/**
 * The output from the procedure: Information about a set of identified peptides
 * @author Salvador
 *
 */
public interface IdentifiedPeptideSet {
	/**
	 * The name of the set of identified peptides
	 * @return the name
	 */
	public String getName();
	/**
	 * For a large number of identified peptides, the information about the peptides can be 
	 * attached in a file here.
	 * @return the URI
	 */
	public String getFileURI();
	/**
	 * Gets the set of identified peptides
	 * @return a set of IdentifiedPeptides
	 */
	public Set <IdentifiedPeptide> getIdentifiedPeptides();
}
