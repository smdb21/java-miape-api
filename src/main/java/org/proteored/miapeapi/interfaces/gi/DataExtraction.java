package org.proteored.miapeapi.interfaces.gi;

import java.util.Set;

/**
 * State the name of the data extraction process, including the steps and order.
 * For the following possible processes, describe whenever used.
 * 
 * @author Salvador
 * 
 */
public interface DataExtraction {

	/**
	 * The name of the data extraction process
	 * 
	 */
	public String getName();

	/**
	 * The input image(s) URI or a Digital Object Identifier (DOI) if processing
	 * has been performed (as the output image of processing)URI)
	 * 
	 * @return the URI or DOI
	 */
	public Set<String> getInputImageUris();

	/**
	 * The input image(s) for data extraction as a reference to the image
	 * specified in Section 1
	 * 
	 */
	public Set<ImageGelInformatics> getInputImages();

	/**
	 * Describe the process by which the features have been assigned on the
	 * image(s). In terms of algorithm name, version and feature editing
	 * (automatic or manual)
	 * 
	 */
	public Set<FeatureDetection> getFeatureDetections();

	/**
	 * Describe the process by which the features have been matched on the
	 * image(s). In terms of algorithm name, version, parameters and match
	 * editing
	 * 
	 */
	public Set<Matching> getMatchings();

	/**
	 * Describe the process by which the features detected have been quantified.
	 * In terms of measurement used, quantitation method, background
	 * substraction (if used), normalization method.
	 * 
	 */
	public Set<FeatureQuantitation> getFeatureQuantitations();
}
