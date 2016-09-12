package org.proteored.miapeapi.factories.gi;

import java.util.Set;

import org.proteored.miapeapi.interfaces.gi.ImageAnalysisSoftware;
import org.proteored.miapeapi.interfaces.gi.ImageGelInformatics;
import org.proteored.miapeapi.interfaces.gi.ImageProcessing;
import org.proteored.miapeapi.interfaces.gi.ImageProcessingStep;

public class ImageProcessingImpl implements ImageProcessing {
	private final String name;
	private final Set<ImageGelInformatics> inputImages;
	private final Set<ImageProcessingStep> analysisProcessingSteps;
	private final Set<ImageAnalysisSoftware> analysisProcessingSoftwares;
	public ImageProcessingImpl(
			ImageProcessingBuilder imageProcessingBuilder) {
		this.name = imageProcessingBuilder.name;
		this.inputImages = imageProcessingBuilder.inputImages;
		this.analysisProcessingSteps = imageProcessingBuilder.analysisProcessingSteps;
		this.analysisProcessingSoftwares = imageProcessingBuilder.analysisProcessingSoftwares;
	}

	@Override
	public Set<ImageAnalysisSoftware> getImageProcessingSoftwares() {
		return analysisProcessingSoftwares;
	}

	@Override
	public Set<ImageProcessingStep> getImageProcessingSteps() {
		return analysisProcessingSteps;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Set<ImageGelInformatics> getInputImages() {
		return inputImages;
	}


}
