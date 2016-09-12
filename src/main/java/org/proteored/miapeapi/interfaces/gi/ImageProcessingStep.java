package org.proteored.miapeapi.interfaces.gi;

import org.proteored.miapeapi.interfaces.Algorithm;

/**
 * Information about the analysis processing steps, for example denoising, 
 * background subtraction (only when used as an image processing step),
 * image alignment and defining an irregular region of interest (basic image 
 * cropping is reported under Image Preparation). 
 * @author Salvador
 *
 */
public interface ImageProcessingStep extends Algorithm {

	/**
	 * The step order of the image processing step
	 * @return the step order
	 */
	public String getStepOrder();

}
