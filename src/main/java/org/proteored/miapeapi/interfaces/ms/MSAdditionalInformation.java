package org.proteored.miapeapi.interfaces.ms;

import org.proteored.miapeapi.cv.AdditionalInformationName;

/**
 * Gets the additional information: Any other relevant information that you want
 * to add to the document, and that anyhow has an impact on the interpretation
 * of the result for that experiment.
 * 
 * @author Salvador
 * 
 */
public interface MSAdditionalInformation {
	/**
	 * Gets the name of the additional information. It should be one of the
	 * possible values from {@link AdditionalInformationName}
	 * 
	 * @return additional information name
	 */
	public String getName();

	/**
	 * Gets the value of the additional information
	 * 
	 * @return additional information value
	 */
	public String getValue();
}
