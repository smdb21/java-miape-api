package org.proteored.miapeapi.factories.gi;

import java.util.Set;

import org.proteored.miapeapi.interfaces.gi.DataExtraction;
import org.proteored.miapeapi.interfaces.gi.FeatureDetection;
import org.proteored.miapeapi.interfaces.gi.FeatureQuantitation;
import org.proteored.miapeapi.interfaces.gi.ImageGelInformatics;
import org.proteored.miapeapi.interfaces.gi.Matching;

public class DataExtractionImpl implements DataExtraction {
	private final String name;
	private final Set<FeatureDetection> featureDetections;
	private final Set<Matching> matchings;
	private final Set<FeatureQuantitation> featureQuantitations;
	private final Set<String> inputImageUris;
	private final Set<ImageGelInformatics> inputImages;

	public DataExtractionImpl(DataExtractionBuilder dataExtractionBuilder) {
		this.name = dataExtractionBuilder.name;
		this.inputImageUris = dataExtractionBuilder.inputImageUris;
		this.inputImages = dataExtractionBuilder.inputImages;
		this.featureDetections = dataExtractionBuilder.featureDetections;
		this.matchings = dataExtractionBuilder.matchings;
		this.featureQuantitations = dataExtractionBuilder.featureQuantitations;
	}

	@Override
	public Set<FeatureDetection> getFeatureDetections() {
		return featureDetections;
	}

	@Override
	public Set<FeatureQuantitation> getFeatureQuantitations() {
		return featureQuantitations;
	}

	@Override
	public Set<Matching> getMatchings() {
		return matchings;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Set<String> getInputImageUris() {
		return inputImageUris;
	}

	@Override
	public Set<ImageGelInformatics> getInputImages() {
		return inputImages;
	}

}
