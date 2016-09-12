package org.proteored.miapeapi.factories.ms;

import org.proteored.miapeapi.cv.ms.LaserType;
import org.proteored.miapeapi.interfaces.ms.Maldi;

public class MaldiBuilder {

	String name;
	String extraction;
	String laser;
	String matrix;
	String parameters;
	String plateType;
	String dissociation;
	String dissociationSummary;
	String laserWaveLength;

	MaldiBuilder(String name) {
		this.name = name;
	}

	public MaldiBuilder extraction(String value) {
		this.extraction = value;
		return this;
	}

	/**
	 * Set the laser type. It should be one of the possible values from
	 * {@link LaserType}
	 **/
	public MaldiBuilder laser(String value) {
		this.laser = value;
		return this;
	}

	public MaldiBuilder matrix(String value) {
		this.matrix = value;
		return this;
	}

	public MaldiBuilder laserParameters(String value) {
		this.parameters = value;
		return this;
	}

	public MaldiBuilder plateType(String value) {
		this.plateType = value;
		return this;
	}

	public MaldiBuilder dissociation(String value) {
		this.dissociation = value;
		return this;
	}

	public MaldiBuilder dissociationSummary(String value) {
		this.dissociationSummary = value;
		return this;
	}

	public MaldiBuilder laserWaveLength(String value) {
		this.laserWaveLength = value;
		return this;
	}

	public Maldi build() {
		return new MaldiImpl(this);
	}
}