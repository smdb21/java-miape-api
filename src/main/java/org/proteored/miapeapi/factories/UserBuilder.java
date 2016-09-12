package org.proteored.miapeapi.factories;

import java.util.Date;

import org.proteored.miapeapi.exceptions.MiapeDatabaseException;
import org.proteored.miapeapi.exceptions.MiapeSecurityException;
import org.proteored.miapeapi.interfaces.User;
import org.proteored.miapeapi.interfaces.persistence.PersistenceManager;

public class UserBuilder {
	// Required parameters
	String name;
	String password;

	String address;
	Integer contactPerson = Integer.valueOf(-1);
	String email;
	String faxNumber;
	String lastName;
	Integer manager = Integer.valueOf(-1);
	String managerRole;
	String status;
	String telephoneNumber;
	String userName;
	String web;
	String code;
	String city;
	String country;
	String department;
	String institution;
	String nif;
	String account;
	Date dateSignedUp = new Date();
	String mainResearcher;
	String postCode;
	String institutionName;
	PersistenceManager dbManager;

	/**
	 * Create a UserBuilder with a PersistenceManager
	 * 
	 * @param userName
	 * @param databaseManager
	 *            : this will be used in the build method to check if userName
	 *            and password are not wrong in the database
	 */
	UserBuilder(String userName, String password, PersistenceManager databaseManager) {
		this.userName = userName;
		this.password = password;
		this.dbManager = databaseManager;
	}

	public UserBuilder address(String value) {
		this.address = value;
		return this;
	}

	public UserBuilder contactPerson(Integer value) {
		this.contactPerson = value;
		return this;
	}

	public UserBuilder email(String value) {
		this.email = value;
		return this;
	}

	public UserBuilder faxNumber(String value) {
		this.faxNumber = value;
		return this;
	}

	public UserBuilder lastName(String value) {
		this.lastName = value;
		return this;
	}

	public UserBuilder manager(Integer value) {
		this.manager = value;
		return this;
	}

	public UserBuilder managerRole(String value) {
		this.managerRole = value;
		return this;
	}

	public UserBuilder status(String value) {
		this.status = value;
		return this;
	}

	public UserBuilder telephoneNumber(String value) {
		this.telephoneNumber = value;
		return this;
	}

	public UserBuilder name(String value) {
		this.name = value;
		return this;
	}

	public UserBuilder code(String value) {
		this.code = value;
		return this;
	}

	public UserBuilder web(String value) {
		this.web = value;
		return this;
	}

	public UserBuilder city(String value) {
		this.city = value;
		return this;
	}

	public UserBuilder country(String value) {
		this.country = value;
		return this;
	}

	public UserBuilder department(String value) {
		this.department = value;
		return this;
	}

	public UserBuilder institution(String value) {
		this.institution = value;
		return this;
	}

	public UserBuilder nif(String value) {
		this.nif = value;
		return this;
	}

	public UserBuilder account(String value) {
		this.account = value;
		return this;
	}

	public UserBuilder dateSignedUp(Date value) {
		this.dateSignedUp = value;
		return this;
	}

	public UserBuilder mainResearcher(String value) {
		this.mainResearcher = value;
		return this;
	}

	public UserBuilder postCode(String value) {
		this.postCode = value;
		return this;
	}

	public UserBuilder institutionName(String value) {
		this.institutionName = value;
		return this;
	}

	public User build() {
		try {

			dbManager.checkUserCredentials(userName, password);

			User user = dbManager.getUser(this.userName, password);

			return user;

		} catch (MiapeDatabaseException e) {
			e.printStackTrace();
		} catch (MiapeSecurityException e) {
			e.printStackTrace();

			User user = new UserImpl(this);
			// comentado para no dejar que se registre un usuario de esta
			// manera.
			// dbManager.saveUser(user);
			return user;

		}
		return null;

	}
}