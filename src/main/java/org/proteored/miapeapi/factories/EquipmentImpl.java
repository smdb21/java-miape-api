package org.proteored.miapeapi.factories;

import org.proteored.miapeapi.interfaces.Equipment;

public class EquipmentImpl implements Equipment {

	private final String catalogNumber;
	private final String comments;
	private final String description;
	private final String manufacturer;
	private final String model;
	private final String name;
	private final String parameters;
	private final String uri;
	private final String version;
	private final int id;

	@SuppressWarnings("unused")
	private EquipmentImpl() {
		this(null);
	}

	public EquipmentImpl(EquipmentBuilder equipmentBuilder) {
		this.catalogNumber = equipmentBuilder.catalogNumber;
		this.comments = equipmentBuilder.comments;
		this.description = equipmentBuilder.description;
		this.manufacturer = equipmentBuilder.manufacturer;
		this.model = equipmentBuilder.model;
		this.name = equipmentBuilder.name;
		this.parameters = equipmentBuilder.parameters;
		this.uri = equipmentBuilder.uri;
		this.version = equipmentBuilder.version;
		this.id = equipmentBuilder.id;
	}

	@Override
	public String getCatalogNumber() {
		return catalogNumber;
	}

	@Override
	public String getComments() {
		return comments;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public String getManufacturer() {
		return manufacturer;
	}

	@Override
	public String getModel() {
		return model;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getParameters() {
		return parameters;
	}

	@Override
	public String getUri() {
		return uri;
	}

	@Override
	public String getVersion() {
		return version;
	}

	@Override
	public int getId() {
		return id;
	}

}
