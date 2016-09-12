package org.proteored.miapeapi.factories.ge;

import org.proteored.miapeapi.cv.ge.ImageAcquisitionSoftwareName;
import org.proteored.miapeapi.factories.SoftwareBuilder;

public class ImageAcquisitionSoftwareBuilder extends SoftwareBuilder {

	/**
	 * Set the image acquisition software name. It should be one of the possible
	 * values from {@link ImageAcquisitionSoftwareName}
	 * 
	 * @param name
	 */
	ImageAcquisitionSoftwareBuilder(String name) {
		super(name);
	}

}