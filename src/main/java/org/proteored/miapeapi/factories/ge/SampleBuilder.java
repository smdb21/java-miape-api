package org.proteored.miapeapi.factories.ge;

import org.proteored.miapeapi.interfaces.ge.Sample;

public class SampleBuilder {
	final String name;
	String description;

	SampleBuilder(String name) {
		this.name = name;
	}

	public SampleBuilder description(String value) {
		description = value;
		return this;
	}

	public Sample build() {
		return new SampleImpl(this);
	}
}