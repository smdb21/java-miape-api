package org.proteored.miapeapi.factories.ge;

import org.proteored.miapeapi.factories.ImageBuilder;
import org.proteored.miapeapi.interfaces.ge.GelMatrix;
import org.proteored.miapeapi.interfaces.ge.ImageGelElectrophoresis;

public class ImageGelElectrophoresisBuilder extends ImageBuilder {

	GelMatrix matrixRef;

	ImageGelElectrophoresisBuilder(String name) {
		super(name);
	}

	public ImageGelElectrophoresisBuilder matrixRef(GelMatrix value) {
		matrixRef = value;
		return this;
	}

	@Override
	public ImageGelElectrophoresis build() {
		return new ImageGelElectrophoresisImpl(this);
	}
}