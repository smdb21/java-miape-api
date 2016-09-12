package org.proteored.miapeapi.xml.pride.msi;

import org.proteored.miapeapi.interfaces.Software;

public class SoftwareImpl implements Software {
	private final String name;
	private final Integer identifier;
	public SoftwareImpl(String name, Integer identifier) {
		this.name = name;
		this.identifier = identifier;
	}

	@Override
	public String getCatalogNumber() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getComments() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCustomizations() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getManufacturer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getModel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getParameters() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getURI() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getVersion() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getId() {
		if ( identifier != null)
			return identifier;
		return -1;
	}

}
