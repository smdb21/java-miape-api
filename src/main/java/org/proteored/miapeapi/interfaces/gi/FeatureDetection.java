package org.proteored.miapeapi.interfaces.gi;

import org.proteored.miapeapi.cv.gi.FeatureDetectionAlgorithmName;
import org.proteored.miapeapi.interfaces.Algorithm;

/**
 * Describe the process by which the features have been assigned on the
 * image(s). In terms of algorithm name, version and feature editing (automatic
 * or manual)
 * 
 * @author Salvador
 * 
 */
public interface FeatureDetection extends Algorithm {

	/**
	 * Gets the name of the feature detection algorithm. It should be one of the
	 * possible values of {@link FeatureDetectionAlgorithmName}
	 */
	public String getName();

	public String getEditing();

	public String getStepOrder();

}
