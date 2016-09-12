package org.proteored.miapeapi.factories.gi;

import java.util.Set;

import org.proteored.miapeapi.interfaces.gi.DataExtraction;
import org.proteored.miapeapi.interfaces.gi.FeatureDetection;
import org.proteored.miapeapi.interfaces.gi.FeatureQuantitation;
import org.proteored.miapeapi.interfaces.gi.ImageGelInformatics;
import org.proteored.miapeapi.interfaces.gi.Matching;

public class DataExtractionBuilder {
	final String name;
	Set<FeatureDetection> featureDetections;
	Set<Matching> matchings;
	Set<FeatureQuantitation> featureQuantitations;
	Set<String> inputImageUris;
	Set<ImageGelInformatics> inputImages;

	DataExtractionBuilder(String name) {
		this.name = name;
	}

	public DataExtractionBuilder inputImageUris(Set<String> value) {
		inputImageUris = value;
		return this;
	}

	public DataExtractionBuilder inputImages(Set<ImageGelInformatics> value) {
		inputImages = value;
		return this;
	}

	public DataExtractionBuilder featureDetections(Set<FeatureDetection> value) {
		featureDetections = value;
		return this;
	}

	public DataExtractionBuilder matchings(Set<Matching> value) {
		matchings = value;
		return this;
	}

	public DataExtractionBuilder featureQuantitations(Set<FeatureQuantitation> value) {
		featureQuantitations = value;
		return this;
	}

	public DataExtraction build() {
		return new DataExtractionImpl(this);
	}
}