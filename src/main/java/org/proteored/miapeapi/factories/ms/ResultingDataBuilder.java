package org.proteored.miapeapi.factories.ms;

import org.proteored.miapeapi.cv.ms.ChromatogramType;
import org.proteored.miapeapi.cv.ms.MSFileType;
import org.proteored.miapeapi.interfaces.ms.ResultingData;

public class ResultingDataBuilder {

	String additionalURI;
	String name;
	String rawFileType;
	String srmDescriptor;
	String srmType;
	String srmURI;
	String rawFileURI;

	ResultingDataBuilder(String name) {
		this.name = name;
	}

	public ResultingDataBuilder additionalURI(String value) {
		this.additionalURI = value;
		return this;
	}

	/**
	 * Set the data file type. It should be one of the possible values from
	 * {@link MSFileType}
	 **/
	public ResultingDataBuilder dataFileType(String value) {
		this.rawFileType = value;
		return this;
	}

	public ResultingDataBuilder dataFileURI(String value) {
		this.rawFileURI = value;
		return this;
	}

	public ResultingDataBuilder srmDescriptor(String value) {
		this.srmDescriptor = value;
		return this;
	}

	/**
	 * Set the chromatogram type. It should be one of the possible values from
	 * {@link ChromatogramType}
	 **/
	public ResultingDataBuilder srmType(String value) {
		this.srmType = value;
		return this;
	}

	public ResultingDataBuilder srmURI(String value) {
		this.srmURI = value;
		return this;
	}

	public ResultingData build() {
		return new ResultingDataImpl(this);
	}

}