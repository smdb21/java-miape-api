package org.proteored.miapeapi.factories.msi;

import org.proteored.miapeapi.interfaces.msi.AdditionalParameter;

public class AdditionalParameterImpl implements AdditionalParameter {
	private final String name;
	private final String value;

	public AdditionalParameterImpl(AdditionalParameterBuilder additionalParameterBuilder) {
		this.name = additionalParameterBuilder.name; 
		this.value = additionalParameterBuilder.value;
	}


	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getValue() {
		return value;
	}

}
