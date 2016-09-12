package org.proteored.miapeapi.factories.msi;

import org.proteored.miapeapi.cv.AdditionalInformationName;
import org.proteored.miapeapi.interfaces.msi.MSIAdditionalInformation;

public class AdditionalInformationBuilder {
	String name; // it is not mandatory
	String value;

	/**
	 * Set the additional information name. It should be one of the possible
	 * values from {@link AdditionalInformationName}
	 * 
	 */
	AdditionalInformationBuilder(String name) {
		this.name = name;
	}

	public MSIAdditionalInformation build() {
		return new AdditionalInformationImpl(this);
	}

	public AdditionalInformationBuilder value(String addinfovalue) {
		this.value = addinfovalue;
		return this;
	}
}