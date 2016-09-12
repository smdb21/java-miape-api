package org.proteored.miapeapi.interfaces;

import org.proteored.miapeapi.interfaces.persistence.DBEntity;


/**
 * The Interface Project.
 * Describes the Project in the MIAPE documents are stored.
 */
public interface Project extends DBEntity {

	/**
	 * Gets the id (only if it comes from a database)
	 * 
	 * @return the id
	 */
	public int getId();

	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName();

	/**
	 * Gets the date in "YYYY-MM-DD"
	 * 
	 * @return the date
	 */
	public MiapeDate getDate();

	/**
	 * Gets the comments.
	 * 
	 * @return the comments
	 */
	public String getComments();

	/**
	 * Gets the owner of the project (the person that have create it)
	 * @return the owner as a User object
	 */
	public User getOwner();


}
