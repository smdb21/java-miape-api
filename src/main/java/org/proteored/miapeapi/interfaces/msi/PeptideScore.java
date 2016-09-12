package org.proteored.miapeapi.interfaces.msi;

import org.proteored.miapeapi.cv.msi.Score;

/**
 * Peptide score derived from a database search described as Name and Value
 * pairs
 * 
 * @author Salvador
 * 
 */
public interface PeptideScore {
	/**
	 * Gets the name of the score. It should be one of the possible values from
	 * {@link Score}
	 * 
	 * @return the name of the score
	 */
	public String getName();

	/**
	 * Gets the value of the score
	 * 
	 * @return the value
	 */
	public String getValue();

}
