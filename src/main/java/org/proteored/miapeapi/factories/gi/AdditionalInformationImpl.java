package org.proteored.miapeapi.factories.gi;

import org.proteored.miapeapi.interfaces.gi.GIAdditionalInformation;

public class AdditionalInformationImpl implements GIAdditionalInformation {
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
