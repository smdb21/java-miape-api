package org.proteored.miapeapi.xml.xtandem.msi;

import org.proteored.miapeapi.interfaces.msi.AdditionalParameter;

public class AdditionalParameterImpl implements AdditionalParameter {

	private final String paramName;
	private final String paramValue;

	public AdditionalParameterImpl(String paramName, String paramValue) {
		this.paramName = paramName;
		this.paramValue = paramValue;
	}

	@Override
	public String getName() {
		return paramName;
	}

	@Override
	public String getValue() {
		return paramValue;
	}

}
