package org.proteored.miapeapi.interfaces.gi;

import org.proteored.miapeapi.cv.gi.ImageAnalysisSoftwareName;
import org.proteored.miapeapi.interfaces.Software;

/**
 * Image analysis software information. Software name and version number, vendor
 * (or if not available a literature reference or a URI).
 * 
 * @author Salvador
 * 
 */
public interface ImageAnalysisSoftware extends Software {
	/**
	 * Gets the name of the image analysis software. It should be one of the
	 * possible values of {@link ImageAnalysisSoftwareName}
	 * 
	 * @return the name
	 */
	public String getName();
}
