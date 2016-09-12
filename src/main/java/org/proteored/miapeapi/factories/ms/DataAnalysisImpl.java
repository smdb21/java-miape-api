package org.proteored.miapeapi.factories.ms;

import org.proteored.miapeapi.factories.SoftwareImpl;
import org.proteored.miapeapi.interfaces.ms.DataAnalysis;

public class DataAnalysisImpl extends SoftwareImpl implements DataAnalysis {

	private final String parametersLocation;

	@SuppressWarnings("unused")
	private DataAnalysisImpl() {
		this(null);
	}

	public DataAnalysisImpl(DataAnalysisBuilder peakListGenerationBuilder) {
		super(peakListGenerationBuilder);

		this.parametersLocation = peakListGenerationBuilder.parametersLocation;

	}

	@Override
	public String getParametersLocation() {
		return parametersLocation;
	}

}
