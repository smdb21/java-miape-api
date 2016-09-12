package org.proteored.miapeapi.factories.gi;

import org.proteored.miapeapi.interfaces.gi.AnalysisDesign;

public class AnalysisDesignBuilder {
	final String name;
	String type;
	String replicates;
	String groups;
	String standard;
	String externalStandard;
	String withinSampleStandard;

	AnalysisDesignBuilder(String name) {
		this.name = name;
	}

	public AnalysisDesignBuilder type(String value) {
		type = value;
		return this;
	}

	public AnalysisDesignBuilder replicates(String value) {
		replicates = value;
		return this;
	}

	public AnalysisDesignBuilder groups(String value) {
		groups = value;
		return this;
	}

	public AnalysisDesignBuilder standard(String value) {
		standard = value;
		return this;
	}

	public AnalysisDesignBuilder externalStandard(String value) {
		externalStandard = value;
		return this;
	}

	public AnalysisDesignBuilder withinSampleStandard(String value) {
		withinSampleStandard = value;
		return this;
	}

	public AnalysisDesign build() {
		return new AnalysisDesignImpl(this);
	}
}