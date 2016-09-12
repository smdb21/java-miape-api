package org.proteored.miapeapi.factories.ge;

import java.util.Set;

import org.proteored.miapeapi.interfaces.ge.Lane;
import org.proteored.miapeapi.interfaces.ge.SampleApplication;

public class SampleApplicationBuilder {
	final String name;
	String description;
	Set<Lane> lanes;

	SampleApplicationBuilder(String name) {
		this.name = name;
	}

	public SampleApplicationBuilder description(String value) {
		description = value;
		return this;
	}

	public SampleApplicationBuilder lanes(Set<Lane> value) {
		lanes = value;
		return this;
	}

	public SampleApplication build() {
		return new SampleApplicationImpl(this);
	}
}