package org.proteored.miapeapi.factories.ms;

import org.proteored.miapeapi.interfaces.ms.ResultingData;

public class ResultingDataImpl implements ResultingData {

	private final String additionalURI;
	private final String name;
	private final String dataFileType;
	private final String dataFileURI;
	private final String srmDescriptor;
	private final String srmType;
	private final String srmURI;

	public ResultingDataImpl(ResultingDataBuilder builder) {
		this.additionalURI = builder.additionalURI;
		this.name = builder.name;
		this.dataFileType = builder.rawFileType;
		this.dataFileURI = builder.rawFileURI;
		this.srmDescriptor = builder.srmDescriptor;
		this.srmType = builder.srmType;
		this.srmURI = builder.srmURI;
	}

	@Override
	public String getAdditionalUri() {
		return additionalURI;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getDataFileType() {
		return dataFileType;
	}

	@Override
	public String getDataFileUri() {
		return dataFileURI;
	}

	@Override
	public String getSRMDescriptor() {
		return srmDescriptor;
	}

	@Override
	public String getSRMType() {
		return srmType;
	}

	@Override
	public String getSRMUri() {
		return srmURI;
	}

	/*
	 * @Override public List<SpectrumDescription> getSpectrumDescriptions() {
	 * return spectrumDescriptions; }
	 */

}
