package org.proteored.miapeapi.factories.gi;

import org.proteored.miapeapi.factories.AlgorithmImpl;
import org.proteored.miapeapi.interfaces.gi.ImageGelInformatics;
import org.proteored.miapeapi.interfaces.gi.Matching;

public class MatchingImpl extends AlgorithmImpl implements Matching {
	private final String name;
	private final String landmarks;
	private final String editing;
	private final String stepOrder;
	private final ImageGelInformatics image_reference;

	public MatchingImpl(MatchingBuilder matchingBuilder) {
		super(matchingBuilder);
		this.name = matchingBuilder.name;
		this.image_reference = matchingBuilder.image_reference;
		this.landmarks = matchingBuilder.landmarks;
		this.editing = matchingBuilder.editing;
		this.stepOrder = matchingBuilder.stepOrder;
	}

	@Override
	public String getEditing() {
		return editing;
	}

	@Override
	public String getLandmarks() {
		return landmarks;
	}



	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getStepOrder() {
		return stepOrder;
	}

	@Override
	public ImageGelInformatics getReferenceImage() {
		return image_reference;
	}

}
