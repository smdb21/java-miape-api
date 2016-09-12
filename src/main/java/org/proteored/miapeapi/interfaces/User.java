package org.proteored.miapeapi.interfaces;

import java.util.Date;

/**
 * The Interface User. Include the details about the user
 */
public interface User {

	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName();

	/**
	 * Gets the last name.
	 * 
	 * @return the last name
	 */
	public String getLastName();

	/**
	 * Gets the telephone number.
	 * 
	 * @return the telephone number
	 */
	public String getTelephoneNumber();

	/**
	 * Gets the fax number.
	 * 
	 * @return the fax number
	 */
	public String getFaxNumber();

	/**
	 * Gets the email.
	 * 
	 * @return the email
	 */
	public String getEmail();

	/**
	 * Gets the address.
	 * 
	 * @return the address
	 */
	public String getAddress();

	/**
	 * Gets the user.
	 * 
	 * @return the user
	 */
	public String getUserName();

	/**
	 * Gets the password
	 * 
	 * @return the password
	 */
	public String getPassword();

	/**
	 * Gets the status.
	 * 
	 * @return the status
	 */
	public String getStatus();

	/**
	 * Gets the web.
	 * 
	 * @return the web
	 */
	public String getWeb();

	/**
	 * Gets the contact person.
	 * 
	 * @return the contact person
	 */
	public Integer getContactPerson();

	/**
	 * Gets the manager.
	 * 
	 * @return the manager
	 */
	public Integer getManager();

	/**
	 * Gets the manager role.
	 * 
	 * @return the manager role
	 */
	public String getManagerRole();

	// Fields for Clientes

	public String getCode();

	public String getCity();

	public String getCountry();

	public String getPostCode();

	public String getDepartment();

	public String getInstitution();

	public String getInstitutionName();

	public String getNif();

	public String getAccount();

	public Date getDateSignedUp();

	public String getMainResearcher();

}
