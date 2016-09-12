package org.proteored.miapeapi.interfaces;

import java.util.Date;

import org.proteored.miapeapi.interfaces.persistence.DBEntity;
import org.proteored.miapeapi.validation.ValidationReport;

/**
 * The Interface MiapeDocument. represents a document which identifies the
 * minimum information required to report the use of a mass spectrometer in a
 * proteomics experiment, sufficient to support both the effective
 * interpretation and assessment of the data and the potential recreation of the
 * work that generated it..
 */

public interface MiapeDocument extends DBEntity {

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public int getId();

	/**
	 * Gets the version.
	 * 
	 * @return the version
	 */
	public String getVersion();

	/**
	 * Gets the contact.
	 * 
	 * @return the contact
	 */
	public Contact getContact();

	/**
	 * Gets the project.
	 * 
	 * @return the project
	 */
	public Project getProject();

	/**
	 * Gets the owner of the document (the person who has created it).
	 * 
	 * @return the owner
	 */
	public User getOwner();

	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName();

	/**
	 * Gets the date in which the work described was initiated; given in the
	 * standard 'YYYY-MM-DD' format.
	 * 
	 * @return the date
	 */
	public MiapeDate getDate();

	/**
	 * Gets the last date in which this document has been updated
	 * 
	 * @return the creation date
	 */
	public Date getModificationDate();

	/**
	 * Gets a boolean value that indicates if the document is available as a
	 * template or not
	 * 
	 * @return the template
	 */
	public Boolean getTemplate();

	/**
	 * Gets an URL pointing to a standard data file, such as a PRIDE file, a
	 * mzML file, a mzIdentML file or a gelML file.
	 * 
	 * @return the URI
	 */
	public String getAttachedFileLocation();

	public ValidationReport getValidationReport();

}
