package org.proteored.miapeapi.factories.gi;

import org.proteored.miapeapi.cv.gi.ImageAnalysisSoftwareName;
import org.proteored.miapeapi.factories.SoftwareBuilder;
import org.proteored.miapeapi.interfaces.Software;

public class ImageAnalysisSoftwareBuilder extends SoftwareBuilder {

	@Override
	public Software build() {
		return new ImageAnalysisSoftwareImpl(this);
	}

	/**
	 * Set the name of the image analysis software. It should be one of the
	 * possible values from {@link ImageAnalysisSoftwareName}
	 */
	ImageAnalysisSoftwareBuilder(String name) {
		super(name);
	}

}