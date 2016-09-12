package org.proteored.miapeapi.factories;

import org.proteored.miapeapi.cv.ge.ContactPositionGE;
import org.proteored.miapeapi.interfaces.ge.GEContact;

public class GEContactBuilder {

	final String lastName;
	final String email;
	final String name;

	String address;
	String cp;
	String country;
	String department;

	String fax;
	String institution;

	String locality;
	String telephone;
	String position;

	GEContactBuilder(String name, String lastName, String email) {
		this.lastName = lastName;
		this.name = name;
		this.email = email;
	}

	public GEContactBuilder address(String value) {
		this.address = value;
		return this;
	}

	public GEContactBuilder cp(String value) {
		this.cp = value;
		return this;
	}

	public GEContactBuilder country(String value) {
		this.country = value;
		return this;
	}

	public GEContactBuilder department(String value) {
		this.department = value;
		return this;
	}

	public GEContactBuilder fax(String value) {
		this.fax = value;
		return this;
	}

	public GEContactBuilder institution(String value) {
		this.institution = value;
		return this;
	}

	public GEContactBuilder locality(String value) {
		this.locality = value;
		return this;
	}

	public GEContactBuilder telephone(String value) {
		this.telephone = value;
		return this;
	}

	/**
	 * Sets the contact position. It should be one of the possible values from
	 * {@link ContactPositionGE}
	 **/
	public GEContactBuilder position(String value) {
		this.position = value;
		return this;
	}

	public GEContact build() {
		return new GEContactImpl(this);
	}
}