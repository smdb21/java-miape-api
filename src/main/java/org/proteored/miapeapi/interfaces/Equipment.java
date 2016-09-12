package org.proteored.miapeapi.interfaces;

import org.proteored.miapeapi.cv.ms.InstrumentVendor;

/**
 * The Equipment describes the equipment used in the experiment. Where the
 * interface was from, plus its name and catalogue number
 */

public interface Equipment {

	/**
	 * Identifier needed by gelML exporter
	 * 
	 */
	public int getId();

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
	 * Gets the manufacturer of the instrument. It should be one of the possible
	 * values from {@link InstrumentVendor}
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
	public String getUri();
}
