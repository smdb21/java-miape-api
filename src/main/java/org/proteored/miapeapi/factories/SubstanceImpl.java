package org.proteored.miapeapi.factories;

import org.proteored.miapeapi.interfaces.Substance;
import org.proteored.miapeapi.xml.util.MiapeXmlUtil;

public class SubstanceImpl implements Substance {
	private final String name;
	private final String description;
	private final String type;
	private final String volume;
	private final String volumeUnit;
	private final String concentration;
	private final String concentrationUnit;
	private final String mass;
	private final String massUnit;

	public SubstanceImpl(SubstanceBuilder substanceBuilder) {
		this.name = substanceBuilder.name;
		this.description = substanceBuilder.description;
		this.type = substanceBuilder.type;
		this.volume = substanceBuilder.volume;
		this.volumeUnit = substanceBuilder.volumeUnit;
		this.concentration = substanceBuilder.concentration;
		this.concentrationUnit = substanceBuilder.concentrationUnit;
		this.mass = substanceBuilder.mass;
		this.massUnit = substanceBuilder.massUnit;
	}

	@Override
	public String getConcentration() {
		return concentration;
	}

	@Override
	public String getConcentrationUnit() {
		return concentrationUnit;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public String getMass() {
		return mass;
	}

	@Override
	public String getMassUnit() {
		return massUnit;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public String getVolume() {
		return volume;
	}

	@Override
	public String getVolumeUnit() {
		return volumeUnit;
	}

	@Override
	public int getId() {
		return MiapeXmlUtil.SubstanceCounter.increaseCounter();
	}

}
