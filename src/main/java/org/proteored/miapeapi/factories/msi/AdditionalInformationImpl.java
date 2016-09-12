package org.proteored.miapeapi.factories.msi;

import org.proteored.miapeapi.interfaces.msi.MSIAdditionalInformation;

public class AdditionalInformationImpl implements MSIAdditionalInformation {

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
