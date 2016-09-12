package org.proteored.miapeapi.interfaces.msi;

import java.util.HashMap;
import java.util.Set;

/**
 * A set of proteins that comes from the output of a search engine
 * 
 * @author Salvador
 * 
 */
public interface IdentifiedProteinSet {

	/**
	 * Gets the name of the protein set
	 * 
	 * @return the name of the protein set
	 */
	public String getName();

	/**
	 * Gets the location of the file thats contains all identified proteins
	 * informations. Can be a MASCOT .dat file, a SEQUEST .srf file, etc...
	 * 
	 * @return the URI to the file.
	 */
	public String getFileLocation();

	/**
	 * Gets the list of identified proteins that belongs to this protein set
	 * 
	 * @return the list of proteins
	 */
	public HashMap<String, IdentifiedProtein> getIdentifiedProteins();

	/**
	 * Gets the input parameters that were used to obtain this protein set
	 * 
	 * @return the input parameter
	 */
	public InputParameter getInputParameter();

	/**
	 * Gets the input data sets that were searched to obtain this protein set
	 * 
	 * @return the input data sets
	 */
	public Set<InputDataSet> getInputDataSets();
}
