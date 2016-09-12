package org.proteored.miapeapi.factories.gi;

import org.proteored.miapeapi.factories.AlgorithmBuilder;
import org.proteored.miapeapi.interfaces.gi.ImageProcessingStep;

public class ImageProcessingStepBuilder extends AlgorithmBuilder {
	String stepOrder;

	 ImageProcessingStepBuilder(String name) {
		super(name);
	}

	public ImageProcessingStepBuilder stepOrder(String value) {
		stepOrder = value;
		return this;
	}

	@Override
	public ImageProcessingStep build() {
		return new ImageProcessingStepImpl(this);
	}
}