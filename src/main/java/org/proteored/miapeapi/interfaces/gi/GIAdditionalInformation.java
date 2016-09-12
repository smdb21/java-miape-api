package org.proteored.miapeapi.interfaces.gi;

import org.proteored.miapeapi.cv.AdditionalInformationName;

/**
 * Gets the additional information: Any other relevant information that you want
 * to add to the document, and that anyhow has an impact on the interpretation
 * of the result for that experiment.
 * 
 * @author Salvador
 * 
 */
public interface GIAdditionalInformation {
	/**
	 * Gets the name of the additional information. It should be one of the
	 * possible values of {@link AdditionalInformationName}
	 */
	public String getName();

	/**
	 * Gets the value of the additional information
	 * 
	 * @return additional information value
	 */
	public String getValue();
}
