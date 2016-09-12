package org.proteored.miapeapi.factories.ge;

import org.proteored.miapeapi.interfaces.ge.Sample;

public class SampleImpl implements Sample {
	private final String name;
	private final String description;
	public SampleImpl(SampleBuilder miapeSampleBuilder) {
		this.name = miapeSampleBuilder.name;
		this.description = miapeSampleBuilder.description;
	}


	@Override
	public String getDescription() {
		return description;
	}


	@Override
	public String getName() {
		return name;
	}


	@Override
	public int getId() {
		return -1;
	}
}
