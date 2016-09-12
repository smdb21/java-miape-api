package org.proteored.miapeapi.interfaces;

import org.proteored.miapeapi.cv.ms.SoftwareName;

/**
 * The Interface Software. The instrument management and data analysis package
 * name, and version; where there are several pieces of software involved, give
 * name, version and role for each one. Mention also upgrades not reflected in
 * the version number
 */
public interface Software {

	/**
	 * Gets the identifier that can be referenced from MSIInputParameter
	 * 
	 * @return the identifier
	 */
	public int getId();

	/**
	 * Gets the version.
	 * 
	 * @return the version
	 */
	public String getVersion();

	/**
	 * Gets the name of the software. It should be one of the possible values
	 * from {@link SoftwareName}.
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

	/**
	 * Gets the customizations.
	 * 
	 * @return the customizations
	 */
	public String getCustomizations();
}
