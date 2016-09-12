package org.proteored.miapeapi.interfaces;

/**
 * The Interface Algorithm, which describes the Algorithm used
 */

public interface Algorithm {
	
	/**
	 * Gets the version.
	 * 
	 * @return the version
	 */
	public String getVersion();

	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName();

	/**
	 * Gets the manufacturer.
	 * 
	 * @return the manufacturer
	 */
	public String getManufacturer();

	/**
	 * Gets the model.
	 * 
	 * @return the model
	 */
	public String getModel();

	/**
	 * Gets the description.
	 * 
	 * @return the description
	 */
	public String getDescription();

	/**
	 * Gets the parameters.
	 * 
	 * @return the parameters
	 */
	public String getParameters();

	/**
	 * Gets the comments.
	 * 
	 * @return the comments
	 */
	public String getComments();

	/**
	 * Gets the catalog number.
	 * 
	 * @return the catalog number
	 */
	public String getCatalogNumber();

	/**
	 * Gets the uri.
	 * 
	 * @return the uri
	 */
	public String getURI();
}
