package org.proteored.miapeapi.factories.gi;

import java.util.Set;

import org.proteored.miapeapi.interfaces.gi.ImageAnalysisSoftware;
import org.proteored.miapeapi.interfaces.gi.ImageGelInformatics;
import org.proteored.miapeapi.interfaces.gi.ImageProcessing;
import org.proteored.miapeapi.interfaces.gi.ImageProcessingStep;

public class ImageProcessingBuilder {
	final String name;
	Set<ImageGelInformatics> inputImages;
	Set<ImageProcessingStep> analysisProcessingSteps;
	Set<ImageAnalysisSoftware> analysisProcessingSoftwares;

	 ImageProcessingBuilder(String name) {
		this.name = name;
	}

	public ImageProcessingBuilder inputImages(Set<ImageGelInformatics> value) {
		inputImages = value;
		return this;
	}

	public ImageProcessingBuilder analysisProcessingSteps(Set<ImageProcessingStep> value) {
		analysisProcessingSteps = value;
		return this;
	}

	public ImageProcessingBuilder analysisProcessingSoftwares(Set<ImageAnalysisSoftware> value) {
		analysisProcessingSoftwares = value;
		return this;
	}

	public ImageProcessing build() {
		return new ImageProcessingImpl(this);
	}
}