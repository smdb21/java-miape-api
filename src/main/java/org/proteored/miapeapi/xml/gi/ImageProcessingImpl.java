package org.proteored.miapeapi.xml.gi;

import java.util.Map;
import java.util.Set;

import org.proteored.miapeapi.interfaces.gi.ImageAnalysisSoftware;
import org.proteored.miapeapi.interfaces.gi.ImageGelInformatics;
import org.proteored.miapeapi.interfaces.gi.ImageProcessing;
import org.proteored.miapeapi.interfaces.gi.ImageProcessingStep;
import org.proteored.miapeapi.xml.gi.autogenerated.GIImage;
import org.proteored.miapeapi.xml.gi.autogenerated.GIImageProcessing;
import org.proteored.miapeapi.xml.gi.autogenerated.GIImageProcessingAlgorithmType;
import org.proteored.miapeapi.xml.gi.autogenerated.GIProcessingInputImage;
import org.proteored.miapeapi.xml.gi.autogenerated.MIAPESoftwareType;

import gnu.trove.set.hash.THashSet;

public class ImageProcessingImpl implements ImageProcessing {
	private final GIImageProcessing imageProcessingXML;
	private final Map<String, GIImage> imageMap;

	public ImageProcessingImpl(GIImageProcessing xmlImageProcessing, Map<String, GIImage> imageMap) {
		this.imageProcessingXML = xmlImageProcessing;
		this.imageMap = imageMap;
	}

	@Override
	public Set<ImageAnalysisSoftware> getImageProcessingSoftwares() {
		Set<ImageAnalysisSoftware> imageProcessingSofware = new THashSet<ImageAnalysisSoftware>();
		if (imageProcessingXML.getGIImageProcessingSoftware() != null) {
			for (MIAPESoftwareType xmlSoftware : imageProcessingXML.getGIImageProcessingSoftware()) {
				imageProcessingSofware.add(new ImageAnalysisSoftwareImpl(xmlSoftware));
			}
		}
		return imageProcessingSofware;
	}

	@Override
	public Set<ImageProcessingStep> getImageProcessingSteps() {
		Set<ImageProcessingStep> imageProcessingSteps = new THashSet<ImageProcessingStep>();
		if (imageProcessingXML.getGIImageProcessingStep() != null) {
			for (GIImageProcessingAlgorithmType imageProcessingStepXML : imageProcessingXML
					.getGIImageProcessingStep()) {
				imageProcessingSteps.add(new ImageProcessingStepImpl(imageProcessingStepXML));
			}
		}
		return imageProcessingSteps;
	}

	@Override
	public String getName() {
		return imageProcessingXML.getName();
	}

	@Override
	public Set<ImageGelInformatics> getInputImages() {
		Set<ImageGelInformatics> images = new THashSet<ImageGelInformatics>();
		if (imageProcessingXML.getGIProcessingInputImage() != null) {
			for (GIProcessingInputImage processingInputImage : imageProcessingXML.getGIProcessingInputImage()) {
				if (imageMap.containsKey(processingInputImage.getImageRef()))
					images.add(new ImageImpl(imageMap.get(processingInputImage.getImageRef())));
			}
		}
		return images;
	}

}
