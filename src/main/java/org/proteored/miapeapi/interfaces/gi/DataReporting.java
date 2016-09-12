package org.proteored.miapeapi.interfaces.gi;

public interface DataReporting {

	public String getName();

	/**
	 * List of all the quantified values and location of image features.
	 * @return the list of all quantified values (in plain text format)
	 */
	public String getFeatureList();

	/**
	 * List of all the quantified values and location of image features,
	 * provided by a reference, such as a URI or in a file.
	 * @return the URI
	 */
	public String getFeatureURI();

	/**
	 * List of all the matched feature relationships if feature matching 
	 * has been performed (in plain text format)
	 * @return the Matches list
	 */
	public String getMatchesList();

	/**
	 * List of all the matched feature relationships if feature matching 
	 * has been performed, provided by a reference, such as URI or in a file. 
	 * @return the URI
	 */
	public String getMatchesURI();

	/**
	 * Description of the results of each Data Analysis recorded in Section 6, such as 
	 * the set of features that displays significant differential expression (in plain text format)
	 * @return a description of the results
	 */
	public String getResultsDescription();

	/**
	 * List the results of each Data Analysis recorded in Section 6, such as 
	 * the set of features that displays significant differential expression,
	 * provided by a reference, such as URI or in a file. 
	 * @return the URI
	 */
	public String getResultsURI();


}
