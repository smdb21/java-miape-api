package org.proteored.miapeapi.factories;

import org.proteored.miapeapi.interfaces.Algorithm;

public class AlgorithmBuilder {
	// Required parameters

	String name;

	String catalogNumber;
	String comments;
	String description;
	String manufacturer;
	String model;
	String parameters;
	String uri;
	String version;

	protected AlgorithmBuilder(String name) {
		this.name = name;
	}

	public AlgorithmBuilder catalogNumber(String value) {
		this.catalogNumber = value;
		return this;
	}

	public AlgorithmBuilder comments(String value) {
		this.comments = value;
		return this;
	}

	public AlgorithmBuilder description(String value) {
		this.description = value;
		return this;
	}

	public AlgorithmBuilder manufacturer(String value) {
		this.manufacturer = value;
		return this;
	}

	public AlgorithmBuilder model(String value) {
		this.model = value;
		return this;
	}

	public AlgorithmBuilder parameters(String value) {
		this.parameters = value;
		return this;
	}

	public AlgorithmBuilder uri(String value) {
		this.uri = value;
		return this;
	}

	public AlgorithmBuilder version(String value) {
		this.version = value;
		return this;
	}

	public Algorithm build() {
		return new AlgorithmImpl(this);
	}
}