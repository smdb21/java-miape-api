package org.proteored.miapeapi.factories.ge;

import org.proteored.miapeapi.interfaces.ge.Lane;
import org.proteored.miapeapi.interfaces.ge.Sample;

public class LaneImpl implements Lane {
	private final String name;
	private final String description;
	private final String laneNumber;
	private final Sample sample;


	public LaneImpl(LaneBuilder miapeLaneBuilder) {
		this.name = miapeLaneBuilder.name;
		this.description = miapeLaneBuilder.description;
		this.laneNumber = miapeLaneBuilder.laneNumber;
		this.sample = miapeLaneBuilder.sample;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public String getLaneNumber() {
		return laneNumber;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Sample getReferencedSample() {
		return sample;
	}

	@Override
	public int getId() {
		return -1;
	}

}
