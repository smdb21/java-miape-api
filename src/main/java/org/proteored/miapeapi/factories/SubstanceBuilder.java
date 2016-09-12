package org.proteored.miapeapi.factories;

import org.proteored.miapeapi.cv.ge.SubstanceConcentrationUnit;
import org.proteored.miapeapi.cv.ge.SubstanceMassUnit;
import org.proteored.miapeapi.cv.ge.SubstanceVolumeUnit;
import org.proteored.miapeapi.interfaces.Substance;

public class SubstanceBuilder {
	// Required parameters
	public final String name;

	public String description;
	public String type;
	public String volume;
	public String volumeUnit;
	public String concentration;
	public String concentrationUnit;
	public String mass;
	public String massUnit;

	protected SubstanceBuilder(String name) {
		this.name = name;
	}

	public SubstanceBuilder type(String value) {
		this.type = value;
		return this;
	}

	public SubstanceBuilder volume(String value) {
		this.volume = value;
		return this;
	}

	/**
	 * Gets the volume unit. It should be one of the possible values of
	 * {@link SubstanceVolumeUnit}
	 */
	public SubstanceBuilder volumeUnit(String value) {
		this.volumeUnit = value;
		return this;
	}

	public SubstanceBuilder concentration(String value) {
		this.concentration = value;
		return this;
	}

	/**
	 * Sets the concentration unit. It should be one of the possible values of
	 * {@link SubstanceConcentrationUnit}
	 */
	public SubstanceBuilder concentrationUnit(String value) {
		this.concentrationUnit = value;
		return this;
	}

	public SubstanceBuilder mass(String value) {
		this.mass = value;
		return this;
	}

	/**
	 * Sets the mass unit. It should be one of the possible values of
	 * {@link SubstanceMassUnit}
	 */

	public SubstanceBuilder massUnit(String value) {
		this.massUnit = value;
		return this;
	}

	public SubstanceBuilder description(String value) {
		this.description = value;
		return this;
	}

	public Substance build() {
		return new SubstanceImpl(this);
	}
}