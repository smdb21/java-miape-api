package org.proteored.miapeapi.factories.gi;

import org.proteored.miapeapi.factories.AlgorithmImpl;
import org.proteored.miapeapi.interfaces.gi.FeatureDetection;

public class FeatureDetectionImpl extends AlgorithmImpl implements FeatureDetection {
	private final String editing;
	private final String stepOrder;
	public FeatureDetectionImpl(FeatureDetectionBuilder featureDetectionBuilder) {
		super(featureDetectionBuilder);
		this.editing = featureDetectionBuilder.editing;
		this.stepOrder = featureDetectionBuilder.stepOrder;
	}

	@Override
	public String getEditing() {
		return editing;
	}

	@Override
	public String getStepOrder() {
		return stepOrder;
	}

}
