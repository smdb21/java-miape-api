package org.proteored.miapeapi.factories.ms;

import org.proteored.miapeapi.cv.ms.InstrumentModel;
import org.proteored.miapeapi.cv.ms.InstrumentVendor;
import org.proteored.miapeapi.factories.EquipmentBuilder;
import org.proteored.miapeapi.interfaces.ms.Spectrometer;

public class SpectrometerBuilder extends EquipmentBuilder {
	String customizations;

	/**
	 * Set the spectrometer name. It should be one of the possible values from
	 * {@link InstrumentName}
	 **/
	SpectrometerBuilder(String name) {
		super(name);
	}

	/**
	 * Set the spectrometer model. It should be one of the possible values from
	 * {@link InstrumentModel}
	 **/
	@Override
	public SpectrometerBuilder model(String value) {
		this.model = value;
		return this;
	}

	/**
	 * Set the manufacturer name. It should be one of the possible values from
	 * {@link InstrumentVendor}
	 **/
	@Override
	public SpectrometerBuilder manufacturer(String value) {
		this.manufacturer = value;
		return this;
	}

	public SpectrometerBuilder customizations(String value) {
		this.customizations = value;
		return this;
	}

	@Override
	public Spectrometer build() {
		return new SpectrometerImpl(this);
	}

}