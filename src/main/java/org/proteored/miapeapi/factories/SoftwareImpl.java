package org.proteored.miapeapi.factories;

import org.proteored.miapeapi.interfaces.Software;

public class SoftwareImpl implements Software {

	private final String name;
	private final String catalogNumber;
	private final String comments;
	private final String customizations;
	private final String description;
	private final String manufacturer;
	private final String model;
	private final String parameters;
	private final String uri;
	private final String version;
	private final int id;

	@SuppressWarnings("unused")
	protected SoftwareImpl() {
		this(null);
	}

	public SoftwareImpl(org.proteored.miapeapi.factories.SoftwareBuilder softwareBuilder) {
		this.name = softwareBuilder.name;
		this.catalogNumber = softwareBuilder.catalogNumber;
		this.comments = softwareBuilder.comments;
		this.customizations = softwareBuilder.customizations;
		this.description = softwareBuilder.description;
		this.manufacturer = softwareBuilder.manufacturer;
		this.model = softwareBuilder.model;
		this.parameters = softwareBuilder.parameters;
		this.uri = softwareBuilder.uri;
		this.version = softwareBuilder.version;
		this.id = softwareBuilder.id;
	}

	@Override
	public String getName() {
		return name;
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
	public String getCustomizations() {
		return customizations;
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
	public String getParameters() {
		return parameters;
	}

	@Override
	public String getURI() {
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
