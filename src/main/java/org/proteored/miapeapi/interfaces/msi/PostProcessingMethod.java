package org.proteored.miapeapi.interfaces.msi;

import org.proteored.miapeapi.cv.msi.ThresholdName;
import org.proteored.miapeapi.interfaces.Algorithm;

/**
 * Description of the method for assessment and confidence given to the
 * identification and quantitation
 * 
 * @author Salvador
 * 
 */
public interface PostProcessingMethod extends Algorithm {
	/**
	 * Gets the name. It should be one of the possible values from
	 * {@link ThresholdName}
	 * 
	 * @return the name
	 */
	public String getName();
}
