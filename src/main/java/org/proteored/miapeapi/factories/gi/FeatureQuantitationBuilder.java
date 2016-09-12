package org.proteored.miapeapi.factories.gi;

import java.util.Set;

import org.proteored.miapeapi.interfaces.Algorithm;
import org.proteored.miapeapi.interfaces.gi.FeatureQuantitation;

public class FeatureQuantitationBuilder {
	final String name;
	String type;
	String stepOrder;
	Set<Algorithm> featureQuantitationAlgorithms;
	Set<Algorithm> featureQuantitationBackgrounds;
	Set<Algorithm> featureQuantitationNormalizations;

	FeatureQuantitationBuilder(String name) {
		this.name = name;
	}

	public FeatureQuantitationBuilder type(String value) {
		type = value;
		return this;
	}

	public FeatureQuantitationBuilder stepOrder(String value) {
		stepOrder = value;
		return this;
	}

	public FeatureQuantitationBuilder featureQuantitationAlgorithms(Set<Algorithm> value) {
		featureQuantitationAlgorithms = value;
		return this;
	}

	public FeatureQuantitationBuilder featureQuantitationBackgrounds(Set<Algorithm> value) {
		featureQuantitationBackgrounds = value;
		return this;
	}

	public FeatureQuantitationBuilder featureQuantitationNormalizations(Set<Algorithm> value) {
		featureQuantitationNormalizations = value;
		return this;
	}

	public FeatureQuantitation build() {
		return new FeatureQuantitationImpl(this);
	}
}