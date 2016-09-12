package org.proteored.miapeapi.factories.ge;

import org.proteored.miapeapi.factories.ImageImpl;
import org.proteored.miapeapi.interfaces.ge.GelMatrix;
import org.proteored.miapeapi.interfaces.ge.ImageGelElectrophoresis;

public class ImageGelElectrophoresisImpl extends ImageImpl implements ImageGelElectrophoresis {

	private final GelMatrix gelMatrixRef;

	public ImageGelElectrophoresisImpl(ImageGelElectrophoresisBuilder imageBuilder) {
		super(imageBuilder);
		this.gelMatrixRef = imageBuilder.matrixRef;
	}

	@Override
	public GelMatrix getReferencedGelMatrix() {
		return gelMatrixRef;
	}

}
