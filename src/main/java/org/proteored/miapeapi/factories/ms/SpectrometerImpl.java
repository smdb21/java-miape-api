package org.proteored.miapeapi.factories.ms;

import org.proteored.miapeapi.factories.EquipmentImpl;
import org.proteored.miapeapi.interfaces.ms.Spectrometer;

public class SpectrometerImpl extends EquipmentImpl implements Spectrometer {
	private final String customizations;

	public SpectrometerImpl(SpectrometerBuilder spectrometerBuilder) {
		super(spectrometerBuilder);
		this.customizations = spectrometerBuilder.customizations;
	}

	@Override
	public String getCustomizations() {
		return customizations;
	}



}
