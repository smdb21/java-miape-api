package org.proteored.miapeapi.factories.gi;

import org.proteored.miapeapi.factories.AlgorithmBuilder;
import org.proteored.miapeapi.interfaces.gi.ImageGelInformatics;
import org.proteored.miapeapi.interfaces.gi.Matching;

public class MatchingBuilder  extends AlgorithmBuilder{
	final String name;
	ImageGelInformatics image_reference;
	String landmarks;
	String editing;
	String stepOrder;



	public MatchingBuilder(String name) {
		super(name);
		this.name = name;
	}

	public MatchingBuilder image_reference(ImageGelInformatics value) {
		image_reference = value;
		return this;
	}

	public MatchingBuilder landmarks(String value) {
		landmarks = value;
		return this;
	}
	public MatchingBuilder stepOrder(String value) {
		stepOrder = value;
		return this;
	}
	public MatchingBuilder editing(String value) {
		editing = value;
		return this;
	}

	@Override
	public Matching build() {
		return new MatchingImpl(this);
	}
}