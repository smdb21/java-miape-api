package org.proteored.miapeapi.factories.gi;

import org.proteored.miapeapi.interfaces.gi.ImagePreparationStep;

public class ImagePreparationStepImpl implements ImagePreparationStep {
	private final String name;
	private final String type;
	private final String stepOrder;
	private final String parameters;
	public ImagePreparationStepImpl(ImagePreparationStepBuilder preparationStepBuilder) {
		this.name = preparationStepBuilder.name;
		this.type = preparationStepBuilder.type;
		this.stepOrder = preparationStepBuilder.stepOrder;
		this.parameters = preparationStepBuilder.parameters;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getParameters() {
		return parameters;
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
