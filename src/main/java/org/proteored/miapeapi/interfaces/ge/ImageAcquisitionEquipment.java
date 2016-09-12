package org.proteored.miapeapi.interfaces.ge;

import org.proteored.miapeapi.cv.ge.AcquisitionEquipmentType;
import org.proteored.miapeapi.interfaces.Equipment;

public interface ImageAcquisitionEquipment extends Equipment {

	/**
	 * Gets the image acquisition type. It should be one of the possible values
	 * from {@link AcquisitionEquipmentType}
	 * 
	 * @return the type of the equipment
	 */
	public String getType();

	public Integer getCalibration();

	public String getParametersUrl();
}
