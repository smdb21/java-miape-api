package org.proteored.miapeapi.factories.msi;

import org.proteored.miapeapi.interfaces.msi.InputData;

public class InputDataBuilder {
	String name;
	String description;
	String sourceDataUrl;
	String msFileType;
	int id;

	 InputDataBuilder(String name) {
		this.name = name;
	}

	public InputDataBuilder description(String value) {
		description = value;
		return this;
	}

	public InputDataBuilder sourceDataUrl(String value) {
		sourceDataUrl = value;
		return this;
	}

	public InputDataBuilder msFileType(String value) {
		msFileType = value;
		return this;
	}

	public InputDataBuilder id(int value) {
		this.id = value;
		return this;
	}

	public InputData build() {
		return new InputDataImpl(this);
	}
}