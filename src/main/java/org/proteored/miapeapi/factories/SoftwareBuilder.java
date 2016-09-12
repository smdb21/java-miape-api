package org.proteored.miapeapi.factories;

import org.proteored.miapeapi.interfaces.Software;

public class SoftwareBuilder {
	String name;
	String catalogNumber;
	String comments;
	String customizations;
	protected String description;
	String manufacturer;
	String model;
	String parameters;
	String uri;
	String version;
	int id;

	protected SoftwareBuilder(String name) {
		this.name = name;
	}

	public SoftwareBuilder catalogNumber(String value) {
		this.catalogNumber = value;
		return this;
	}

	public SoftwareBuilder comments(String value) {
		this.comments = value;
		return this;
	}

	public SoftwareBuilder customizations(String value) {
		this.customizations = value;
		return this;
	}

	public SoftwareBuilder description(String value) {
		this.description = value;
		return this;
	}

	public SoftwareBuilder manufacturer(String value) {
		this.manufacturer = value;
		return this;
	}

	public SoftwareBuilder model(String value) {
		this.model = value;
		return this;
	}

	public SoftwareBuilder parameters(String value) {
		this.parameters = value;
		return this;
	}

	public SoftwareBuilder uri(String value) {
		this.uri = value;
		return this;
	}

	public SoftwareBuilder version(String value) {
		this.version = value;
		return this;
	}

	public SoftwareBuilder id(int value) {
		this.id = value;
		return this;
	}

	public Software build() {
		return new org.proteored.miapeapi.factories.SoftwareImpl(this);
	}
}