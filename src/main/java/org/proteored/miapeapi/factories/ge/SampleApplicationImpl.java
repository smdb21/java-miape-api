package org.proteored.miapeapi.factories.ge;

import java.util.Set;

import org.proteored.miapeapi.interfaces.ge.Lane;
import org.proteored.miapeapi.interfaces.ge.SampleApplication;

public class SampleApplicationImpl implements SampleApplication {
	private final String name;
	private final String description;
	private final Set<Lane> lanes; 

	public SampleApplicationImpl(
			SampleApplicationBuilder miapeSampleApplicationBuilder) {
		this.name = miapeSampleApplicationBuilder.name;
		this.description = miapeSampleApplicationBuilder.description;
		this.lanes = miapeSampleApplicationBuilder.lanes;
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
	public Set<Lane> getLanes() {
		return lanes;
	}

}
