package org.proteored.miapeapi.factories;

import org.proteored.miapeapi.interfaces.ge.GEContact;

public class GEContactImpl implements GEContact {

	private final String address;
	private final String cp;
	private final String country;
	private final String department;
	private final String email;
	private final String fax;
	private final String institution;
	private final String lastName;
	private final String name;
	private final String locality;
	private final String telephone;
	private final String position;


	public GEContactImpl(GEContactBuilder contactBuilder) {
		this.address = contactBuilder.address;
		this.cp = contactBuilder.cp;
		this.country = contactBuilder.country;
		this.department = contactBuilder.department;
		this.email = contactBuilder.email;
		this.fax = contactBuilder.fax;
		this.institution = contactBuilder.institution;
		this.lastName = contactBuilder.lastName;
		this.name = contactBuilder.name;
		this.locality = contactBuilder.locality;
		this.telephone = contactBuilder.telephone;
		this.position = contactBuilder.position;
	}

	@Override
	public String getAddress() {
		return address;
	}

	@Override
	public String getCP() {
		return cp;
	}

	@Override
	public String getCountry() {
		return country;
	}

	@Override
	public String getDepartment() {
		return department;
	}

	@Override
	public String getEmail() {
		return email;
	}

	@Override
	public String getFax() {
		return fax;
	}

	@Override
	public String getInstitution() {
		return institution;
	}

	@Override
	public String getLastName() {
		return lastName;
	}

	@Override
	public String getLocality() {
		return locality;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getTelephone() {
		return telephone;
	}

	@Override
	public String getPosition() {
		return position;
	}
}