package org.proteored.miapeapi.factories.gi;

import org.proteored.miapeapi.factories.AlgorithmImpl;
import org.proteored.miapeapi.interfaces.gi.ImageProcessingStep;

public class ImageProcessingStepImpl extends AlgorithmImpl implements ImageProcessingStep {
	private final String stepOrder;


	public ImageProcessingStepImpl(ImageProcessingStepBuilder analysisProcessingStepBuilder) {
		super(analysisProcessingStepBuilder);

		this.stepOrder = analysisProcessingStepBuilder.stepOrder;

	}

	@Override
	public String getStepOrder() {
		return stepOrder;
	}


}
