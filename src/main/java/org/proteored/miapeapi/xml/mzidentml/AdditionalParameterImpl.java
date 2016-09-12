package org.proteored.miapeapi.xml.mzidentml;

import org.proteored.miapeapi.interfaces.msi.AdditionalParameter;

public class AdditionalParameterImpl implements AdditionalParameter {
	private final String name;
	private final String value;
	
	public AdditionalParameterImpl(String name, String value){
		this.name = name;
		this.value = value;
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
