package org.proteored.miapeapi.factories;

import org.proteored.miapeapi.interfaces.Equipment;

public class EquipmentBuilder {
	// Required parameters
	protected String name;

	String catalogNumber;
	String comments;
	String description;
	protected String manufacturer;
	protected String model;
	String parameters;
	String uri;
	String version;
	int id;

	protected EquipmentBuilder(String name) {
		this.name = name;
	}

	public EquipmentBuilder comments(String value) {
		this.comments = value;
		return this;
	}

	public EquipmentBuilder catalogNumber(String value) {
		this.catalogNumber = value;
		return this;
	}

	public EquipmentBuilder description(String value) {
		this.description = value;
		return this;
	}

	public EquipmentBuilder manufacturer(String value) {
		this.manufacturer = value;
		return this;
	}

	public EquipmentBuilder model(String value) {
		this.model = value;
		return this;
	}

	public EquipmentBuilder parameters(String value) {
		this.parameters = value;
		return this;
	}

	public EquipmentBuilder uri(String value) {
		this.uri = value;
		return this;
	}

	public EquipmentBuilder version(String value) {
		this.version = value;
		return this;
	}

	public EquipmentBuilder id(int value) {
		this.id = value;
		return this;
	}

	public Equipment build() {
		return new EquipmentImpl(this);
	}
}