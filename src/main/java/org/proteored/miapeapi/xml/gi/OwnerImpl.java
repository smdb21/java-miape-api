package org.proteored.miapeapi.xml.gi;

import java.util.Date;

import org.proteored.miapeapi.interfaces.User;

public class OwnerImpl implements User {
	private final User user;
	private final String ownerName;

	public OwnerImpl(User owner, String ownerName) {
		this.ownerName = ownerName;
		this.user = owner;
		;
	}

	@Override
	public String getName() {
		return ownerName;
	}

	@Override
	public String getLastName() {
		return user.getLastName();
	}

	@Override
	public String getTelephoneNumber() {
		return user.getTelephoneNumber();
	}

	@Override
	public String getFaxNumber() {
		return user.getFaxNumber();
	}

	@Override
	public String getEmail() {
		return user.getEmail();
	}

	@Override
	public String getAddress() {
		return user.getAddress();
	}

	@Override
	public String getUserName() {
		return user.getUserName();
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getStatus() {
		return user.getStatus();
	}

	@Override
	public String getWeb() {
		return user.getWeb();
	}

	@Override
	public Integer getContactPerson() {
		return user.getContactPerson();
	}

	@Override
	public Integer getManager() {
		return user.getManager();
	}

	@Override
	public String getManagerRole() {
		return user.getManagerRole();
	}

	@Override
	public String getCode() {
		return user.getCode();
	}

	@Override
	public String getCity() {
		return user.getCity();
	}

	@Override
	public String getCountry() {
		return user.getCountry();
	}

	@Override
	public String getPostCode() {
		return user.getPostCode();
	}

	@Override
	public String getDepartment() {
		return user.getDepartment();
	}

	@Override
	public String getInstitution() {
		return user.getInstitution();
	}

	@Override
	public String getInstitutionName() {
		return user.getInstitutionName();
	}

	@Override
	public String getNif() {
		return user.getNif();
	}

	@Override
	public String getAccount() {
		return user.getAccount();
	}

	@Override
	public Date getDateSignedUp() {
		return user.getDateSignedUp();
	}

	@Override
	public String getMainResearcher() {
		return user.getMainResearcher();
	}

}
