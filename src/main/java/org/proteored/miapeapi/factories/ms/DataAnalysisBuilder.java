package org.proteored.miapeapi.factories.ms;

import org.proteored.miapeapi.cv.msi.DataTransformation;
import org.proteored.miapeapi.factories.SoftwareBuilder;
import org.proteored.miapeapi.interfaces.ms.DataAnalysis;

public class DataAnalysisBuilder extends SoftwareBuilder {

	String parametersLocation;

	DataAnalysisBuilder(String name) {
		super(name);

	}

	public DataAnalysisBuilder parametersLocation(String value) {
		this.parametersLocation = value;
		return this;
	}

	/**
	 * Set the description of the data analysis sofware. It should be one of the
	 * possible values from {@link DataTransformation}
	 * 
	 * @param value
	 */
	@Override
	public DataAnalysisBuilder description(String value) {
		super.description = value;
		return this;
	}

	@Override
	public DataAnalysis build() {
		return new DataAnalysisImpl(this);
	}
}