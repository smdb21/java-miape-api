package org.proteored.miapeapi.interfaces.gi;

import java.util.Set;

/**
 * Any processing performed by the bioinformatics software to prepare the images for 
 * data extraction. List the image processing steps performed with parameters and 
 * their relative order.
 * @author Salvador
 *
 */
public interface ImageProcessing {

	public String getName();

	/**
	 * The image(s) on which processing is performed, 
	 * given by reference to images specified in Section 1.
	 * @return the input images
	 */
	public Set<ImageGelInformatics> getInputImages();

	/**
	 * Information about the analysis processing steps, for example denoising, 
	 * background subtraction (only when used as an image processing step), 
	 * image alignment and defining an irregular region of interest (basic image 
	 * cropping is reported under Image Preparation). 
	 * @return the image processing step information
	 */
	public Set<ImageProcessingStep> getImageProcessingSteps();

	/**
	 * Software used for the processing, if different from the analysis software 
	 * defined in Section 1. Software name and version number, vendor (or if not 
	 * available, provide a literature reference or a URI). 
	 * @return the image analysis software
	 */
	public Set<ImageAnalysisSoftware> getImageProcessingSoftwares();
}
