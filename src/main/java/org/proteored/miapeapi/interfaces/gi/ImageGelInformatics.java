package org.proteored.miapeapi.interfaces.gi;

import org.proteored.miapeapi.interfaces.Image;

public interface ImageGelInformatics extends Image {



	/**
	 * Gets the type of the image (for example optical scan, 
	 * synthetic image or DIGE scans).
	 * 
	 * @return the type
	 **/
	public String getType();
}
