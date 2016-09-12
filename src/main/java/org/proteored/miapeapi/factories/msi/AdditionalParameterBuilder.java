package org.proteored.miapeapi.factories.msi;

import org.proteored.miapeapi.cv.msi.AdditionalParameterName;
import org.proteored.miapeapi.interfaces.msi.AdditionalParameter;

public class AdditionalParameterBuilder {
	String name;
	String value;

	/**
	 * Set the additional parameter name. It should be one of the possible
	 * values from {@link AdditionalParameterName}
	 **/
	public AdditionalParameterBuilder(String name) {
		this.name = name;
	}

	public AdditionalParameterBuilder value(String value) {
		this.value = value;
		return this;
	}

	public AdditionalParameter build() {
		return new AdditionalParameterImpl(this);
	}
}