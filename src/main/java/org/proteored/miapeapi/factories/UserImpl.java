package org.proteored.miapeapi.factories;

import java.util.Date;

import org.proteored.miapeapi.interfaces.User;

public class UserImpl implements User {

	private final String address;
	private final Integer contactPerson;
	private final String email;
	private final String faxNumber;
	private final String lastName;
	private final Integer manager;
	private final String managerRole;
	private final String name;
	private final String status;
	private final String telephoneNumber;
	private final String user;
	private final String web;
	private final String code;
	private final String city;
	private final String country;
	private final String department;
	private final String institution;
	private final String nif;
	private final String account;
	private final Date dateSignedUp;
	private final String mainResearcher;
	private final String postCode;
	private final String institutionName;
	private final String password;

	@SuppressWarnings("unused")
	private UserImpl() {
		this(null);
	}

	public UserImpl(UserBuilder userBuilder) {
		this.address = userBuilder.address;
		this.contactPerson = userBuilder.contactPerson;
		this.email = userBuilder.email;
		this.faxNumber = userBuilder.faxNumber;
		this.lastName = userBuilder.lastName;
		this.manager = userBuilder.manager;
		this.managerRole = userBuilder.managerRole;
		this.name = userBuilder.name;
		this.status = userBuilder.status;
		this.telephoneNumber = userBuilder.telephoneNumber;
		this.user = userBuilder.userName;
		this.web = userBuilder.web;
		this.code = userBuilder.code;
		this.city = userBuilder.city;
		this.country = userBuilder.country;
		this.department = userBuilder.department;
		this.institution = userBuilder.institution;
		this.nif = userBuilder.nif;
		this.postCode = userBuilder.postCode;
		this.account = userBuilder.account;
		this.dateSignedUp = userBuilder.dateSignedUp;
		this.mainResearcher = userBuilder.mainResearcher;
		this.institutionName = userBuilder.institutionName;
		this.password = userBuilder.password;
	}

	@Override
	public String getAddress() {
		return address;
	}

	@Override
	public Integer getContactPerson() {
		return contactPerson;
	}

	@Override
	public String getEmail() {
		return email;
	}

	@Override
	public String getFaxNumber() {
		return faxNumber;
	}

	@Override
	public String getLastName() {
		return lastName;
	}

	@Override
	public Integer getManager() {
		return manager;
	}

	@Override
	public String getManagerRole() {
		return managerRole;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getStatus() {
		return status;
	}

	@Override
	public String getTelephoneNumber() {
		return telephoneNumber;
	}

	@Override
	public String getUserName() {
		return user;
	}

	@Override
	public String getWeb() {
		return web;
	}

	@Override
	public String getAccount() {
		return account;
	}

	@Override
	public String getCity() {
		return city;
	}

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public String getCountry() {
		return country;
	}

	@Override
	public Date getDateSignedUp() {
		return dateSignedUp;
	}

	@Override
	public String getDepartment() {
		return department;
	}

	@Override
	public String getInstitution() {
		return institution;
	}

	@Override
	public String getMainResearcher() {
		return mainResearcher;
	}

	@Override
	public String getNif() {
		return nif;
	}

	@Override
	public String getPostCode() {
		return postCode;
	}

	@Override
	public String getInstitutionName() {
		return institutionName;
	}

	@Override
	public String getPassword() {
		return password;
	}

}
