package org.proteored.miapeapi.factories.gi;

import org.proteored.miapeapi.factories.SoftwareImpl;
import org.proteored.miapeapi.interfaces.gi.ImageAnalysisSoftware;

public class ImageAnalysisSoftwareImpl extends SoftwareImpl implements ImageAnalysisSoftware {

	public ImageAnalysisSoftwareImpl(ImageAnalysisSoftwareBuilder softwareBuilder) {
		super(softwareBuilder);
	}
}
