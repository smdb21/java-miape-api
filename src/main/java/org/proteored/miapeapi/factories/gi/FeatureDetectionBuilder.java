package org.proteored.miapeapi.factories.gi;

import org.proteored.miapeapi.factories.AlgorithmBuilder;
import org.proteored.miapeapi.interfaces.gi.FeatureDetection;

public class FeatureDetectionBuilder extends AlgorithmBuilder {
	String editing;
	String stepOrder;

	FeatureDetectionBuilder(String name) {
		super(name);
	}

	public FeatureDetectionBuilder editing(String value) {
		editing = value;
		return this;
	}

	public FeatureDetectionBuilder stepOrder(String value) {
		stepOrder = value;
		return this;
	}

	@Override
	public FeatureDetection build() {
		return new FeatureDetectionImpl(this);
	}
}