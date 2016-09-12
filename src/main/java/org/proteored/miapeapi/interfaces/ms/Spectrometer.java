package org.proteored.miapeapi.interfaces.ms;

import org.proteored.miapeapi.cv.ms.InstrumentModel;
import org.proteored.miapeapi.cv.ms.SpectrometerName;
import org.proteored.miapeapi.interfaces.Equipment;

public interface Spectrometer extends Equipment {
	/**
	 * Gets the name of the spectrometer. It should be one of the possible
	 * values from {@link SpectrometerName}
	 * 
	 * @return the name
	 */
	public String getName();

	/**
	 * Gets the model of the spectrometer. It should be one of the possible
	 * values from {@link InstrumentModel}
	 * 
	 * @return the model
	 */
	public String getModel();

	/**
	 * Gets any significant deviation from the manufacturer's specification for
	 * the mass spectrometer
	 * 
	 * @return the deviations/customizations
	 */
	public String getCustomizations();
}
