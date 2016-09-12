package org.proteored.miapeapi.factories;

import org.proteored.miapeapi.cv.ms.ContactPositionMS;
import org.proteored.miapeapi.interfaces.ms.MSContact;

public class MSContactBuilder {

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

	MSContactBuilder(String name, String lastName, String email) {
		this.lastName = lastName;
		this.name = name;
		this.email = email;
	}

	public MSContactBuilder address(String value) {
		this.address = value;
		return this;
	}

	public MSContactBuilder cp(String value) {
		this.cp = value;
		return this;
	}

	public MSContactBuilder country(String value) {
		this.country = value;
		return this;
	}

	public MSContactBuilder department(String value) {
		this.department = value;
		return this;
	}

	public MSContactBuilder fax(String value) {
		this.fax = value;
		return this;
	}

	public MSContactBuilder institution(String value) {
		this.institution = value;
		return this;
	}

	public MSContactBuilder locality(String value) {
		this.locality = value;
		return this;
	}

	public MSContactBuilder telephone(String value) {
		this.telephone = value;
		return this;
	}

	/**
	 * Set the contact position. It should be one of the possible values from
	 * {@link ContactPositionMS}
	 **/
	public MSContactBuilder position(String value) {
		this.position = value;
		return this;
	}

	public MSContact build() {
		return new MSContactImpl(this);
	}
}