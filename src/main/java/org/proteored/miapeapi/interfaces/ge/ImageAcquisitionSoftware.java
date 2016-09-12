package org.proteored.miapeapi.interfaces.ge;

import org.proteored.miapeapi.cv.ge.ImageAcquisitionSoftwareName;
import org.proteored.miapeapi.interfaces.Software;

public interface ImageAcquisitionSoftware extends Software {

	/**
	 * Gets the name of the image acquisition software. It should be one of the
	 * possible values from {@link ImageAcquisitionSoftwareName}
	 */
	public String getName();
}
