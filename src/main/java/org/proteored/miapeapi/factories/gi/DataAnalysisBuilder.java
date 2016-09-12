package org.proteored.miapeapi.factories.gi;

import java.util.Set;

import org.proteored.miapeapi.interfaces.Algorithm;
import org.proteored.miapeapi.interfaces.gi.DataAnalysis;
import org.proteored.miapeapi.interfaces.gi.ImageAnalysisSoftware;

public class DataAnalysisBuilder {
	final String name;
	String type;
	String intent;
	String parameters;
	String inputData;
	Set<Algorithm> dataAnalysisTransformations;
	Set<ImageAnalysisSoftware> dataAnalysisSoftwares;

	DataAnalysisBuilder(String name) {
		this.name = name;
	}

	public DataAnalysisBuilder type(String value) {
		type = value;
		return this;
	}

	public DataAnalysisBuilder intent(String value) {
		intent = value;
		return this;
	}

	public DataAnalysisBuilder parameters(String value) {
		parameters = value;
		return this;
	}

	public DataAnalysisBuilder inputData(String value) {
		inputData = value;
		return this;
	}

	public DataAnalysisBuilder dataAnalysisTransformations(Set<Algorithm> value) {
		dataAnalysisTransformations = value;
		return this;
	}

	public DataAnalysisBuilder dataAnalysisSoftwares(Set<ImageAnalysisSoftware> value) {
		dataAnalysisSoftwares = value;
		return this;
	}

	public DataAnalysis build() {
		return new DataAnalysisImpl(this);
	}
}