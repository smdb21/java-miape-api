package org.proteored.miapeapi.interfaces.ge;

import org.proteored.miapeapi.interfaces.Image;

public interface ImageGelElectrophoresis extends Image {

	/**
	 * @return the gel matrix that was scanned to obtain this image
	 */
	public GelMatrix getReferencedGelMatrix();

}
