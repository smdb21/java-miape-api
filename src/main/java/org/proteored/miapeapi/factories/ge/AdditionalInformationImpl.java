package org.proteored.miapeapi.factories.ge;

import org.proteored.miapeapi.interfaces.ge.GEAdditionalInformation;

public class AdditionalInformationImpl implements GEAdditionalInformation {
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
