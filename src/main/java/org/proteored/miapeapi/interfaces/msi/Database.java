package org.proteored.miapeapi.interfaces.msi;

import org.proteored.miapeapi.cv.msi.DatabaseName;

/**
 * Database queried
 * 
 * @author Salvador
 * 
 */
public interface Database {

	/**
	 * The name of the sequence databank queried. The value should be one of the
	 * possible values in {@link DatabaseName}
	 * 
	 * @return the name
	 */
	public String getName();

	/**
	 * version of the sequence databank queried
	 * 
	 * @return the version
	 */
	public String getNumVersion();

	/**
	 * Associated date to the databank (if available)
	 * 
	 * @return the date
	 */
	public String getDate();

	/**
	 * Description of the databank. IF the databank is not available on the web,
	 * describe the content of this databank.
	 * 
	 * @return the description
	 */
	public String getDescription();

	/**
	 * Number of sequences
	 * 
	 * @return the number of sequences
	 */
	public String getSequenceNumber();

	/**
	 * Availability of the databank
	 * 
	 * @return the URI
	 */
	public String getUri();
}
