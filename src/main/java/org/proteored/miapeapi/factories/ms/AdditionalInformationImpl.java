package org.proteored.miapeapi.factories.ms;

import org.proteored.miapeapi.interfaces.ms.MSAdditionalInformation;

public class AdditionalInformationImpl implements MSAdditionalInformation {

	private final String name;
	private final String value;

	public AdditionalInformationImpl(AdditionalInformationBuilder builder) {
		this.name = builder.name;
		this.value = builder.value;
	}
	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getValue() {
		return this.value;
	}

}
