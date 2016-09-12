package org.proteored.miapeapi.factories.msi;

import org.proteored.miapeapi.interfaces.msi.InputData;

public class InputDataImpl implements InputData {
	private final String name;
	private final String description;
	private final String sourceDataUrl;
	private final String msFileType;
	private final int id;

	public InputDataImpl(InputDataBuilder inputDataBuilder) {
		this.name = inputDataBuilder.name;
		this.description = inputDataBuilder.description;
		this.sourceDataUrl = inputDataBuilder.sourceDataUrl;
		this.msFileType = inputDataBuilder.msFileType;
		this.id = inputDataBuilder.id;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getSourceDataUrl() {
		return sourceDataUrl;
	}

	@Override
	public String getMSFileType() {
		return msFileType;
	}

	@Override
	public int getId() {

		return id;
	}

}
