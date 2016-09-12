package org.proteored.miapeapi.factories;

import org.proteored.miapeapi.interfaces.Algorithm;

public class AlgorithmImpl implements Algorithm {

	private final String catalogNumber;
	private final String comments;
	private final String description;
	private final String manufacturer;
	private final String model;
	private final String parameters;
	private final String name;
	private final String uri;
	private final String version;
	
	@SuppressWarnings("unused")
	private AlgorithmImpl() {
		this(null);
	}
	
	public AlgorithmImpl(AlgorithmBuilder algorithmBuilder) {
		this.catalogNumber = algorithmBuilder.catalogNumber;
		this.comments = algorithmBuilder.comments;
		this.description = algorithmBuilder.description;
		this.manufacturer = algorithmBuilder.manufacturer;
		this.model = algorithmBuilder.model;
		this.parameters = algorithmBuilder.parameters;
		this.name = algorithmBuilder.name;
		this.uri = algorithmBuilder.uri;
		this.version = algorithmBuilder.version;
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
	public String getURI() {
		return uri;
	}

	@Override
	public String getVersion() {
		return version;
	}

}
