package org.proteored.miapeapi.factories.gi;

import java.util.Set;

import org.proteored.miapeapi.interfaces.Algorithm;
import org.proteored.miapeapi.interfaces.gi.FeatureQuantitation;

public class FeatureQuantitationImpl implements FeatureQuantitation {
	private final String name;
	private final String type;
	private final String stepOrder;
	private final Set<Algorithm> featureQuantitationAlgorithms;
	private final Set<Algorithm> featureQuantitationBackgrounds;
	private final Set<Algorithm> featureQuantitationNormalizations;
	
	public FeatureQuantitationImpl(
			FeatureQuantitationBuilder featureQuantitationBuilder) {
		this.name = featureQuantitationBuilder.name;
		this.type = featureQuantitationBuilder.type;
		this.stepOrder = featureQuantitationBuilder.stepOrder;
		this.featureQuantitationAlgorithms = featureQuantitationBuilder.featureQuantitationAlgorithms;
		this.featureQuantitationBackgrounds = featureQuantitationBuilder.featureQuantitationBackgrounds;
		this.featureQuantitationNormalizations = featureQuantitationBuilder.featureQuantitationNormalizations;

	}

	@Override
	public Set<Algorithm> getFeatureQuantitationAlgorithms() {
		return featureQuantitationAlgorithms;
	}

	@Override
	public Set<Algorithm> getFeatureQuantitationBackgrounds() {
		return featureQuantitationBackgrounds;
	}

	@Override
	public Set<Algorithm> getFeatureQuantitationNormalizations() {
		return featureQuantitationNormalizations;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getStepOrder() {
		return stepOrder;
	}

	@Override
	public String getType() {
		return type;
	}

}
