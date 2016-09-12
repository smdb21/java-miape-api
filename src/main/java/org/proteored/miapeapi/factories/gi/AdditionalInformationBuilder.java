package org.proteored.miapeapi.factories.gi;

import org.proteored.miapeapi.cv.AdditionalInformationName;
import org.proteored.miapeapi.interfaces.gi.GIAdditionalInformation;

public class AdditionalInformationBuilder {
	String name; // it is not mandatory
	String value;

	/**
	 * Set the additional information. It should be one of the possible values
	 * from {@link AdditionalInformationName}
	 * 
	 */
	AdditionalInformationBuilder(String name) {
		this.name = name;
	}

	public AdditionalInformationBuilder value(String value) {
		this.value = value;
		return this;
	}

	public GIAdditionalInformation build() {
		return new AdditionalInformationImpl(this);
	}
}