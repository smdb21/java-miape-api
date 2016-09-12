package org.proteored.miapeapi.factories.ge;

import org.proteored.miapeapi.interfaces.ge.Lane;
import org.proteored.miapeapi.interfaces.ge.Sample;

public class LaneBuilder {
	final String name;
	String description;
	String laneNumber;
	Sample sample;

	LaneBuilder(String name) {
		this.name = name;
	}

	public LaneBuilder description(String value) {
		description = value;
		return this;
	}

	public LaneBuilder laneNumber(String value) {
		laneNumber = value;
		return this;
	}

	public LaneBuilder sample(Sample value) {
		sample = value;
		return this;
	}

	public Lane build() {
		return new LaneImpl(this);
	}
}