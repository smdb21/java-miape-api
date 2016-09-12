package org.proteored.miapeapi.factories.gi;

import org.proteored.miapeapi.interfaces.gi.AnalysisDesign;

public class AnalysisDesignImpl implements AnalysisDesign {
	private final String name;
	private final String type;
	private final String replicates;
	private final String groups;
	private final String standard;
	private final String externalStandard;
	private final String withinSampleStandard;
	public AnalysisDesignImpl(AnalysisDesignBuilder analysisDesignBuilder) {
		this.name = analysisDesignBuilder.name;
		this.type = analysisDesignBuilder.type;
		this.replicates = analysisDesignBuilder.replicates;
		this.groups = analysisDesignBuilder.groups;
		this.standard = analysisDesignBuilder.standard;
		this.withinSampleStandard = analysisDesignBuilder.withinSampleStandard;
		this.externalStandard = analysisDesignBuilder.externalStandard;
	}

	@Override
	public String getExternalStandard() {
		return externalStandard;
	}

	@Override
	public String getGroups() {
		return groups;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getReplicates() {
		return replicates;
	}

	@Override
	public String getStandard() {
		return standard;
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public String getWithinSampleStandard() {
		return withinSampleStandard;
	}

}
