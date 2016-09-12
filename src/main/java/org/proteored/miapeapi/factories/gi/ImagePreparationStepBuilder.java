package org.proteored.miapeapi.factories.gi;

import org.proteored.miapeapi.interfaces.gi.ImagePreparationStep;

public class ImagePreparationStepBuilder {
	final String name;
	String type;
	String stepOrder;
	String parameters;

	 ImagePreparationStepBuilder(String name) {
		this.name = name;
	}

	public ImagePreparationStepBuilder type(String value) {
		type = value;
		return this;
	}

	public ImagePreparationStepBuilder stepOrder(String value) {
		stepOrder = value;
		return this;
	}

	public ImagePreparationStepBuilder parameters(String value) {
		parameters = value;
		return this;
	}

	public ImagePreparationStep build() {
		return new ImagePreparationStepImpl(this);
	}
}