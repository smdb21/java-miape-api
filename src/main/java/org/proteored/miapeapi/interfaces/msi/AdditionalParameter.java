package org.proteored.miapeapi.interfaces.msi;

import org.proteored.miapeapi.cv.msi.AdditionalParameterName;

/**
 * Additional parameters described as Name and Value pairs
 * 
 * @author Salvador
 * 
 */
public interface AdditionalParameter {
	/**
	 * Gets the name of the additional parameter. It should be one of the
	 * possible values from {@link AdditionalParameterName}
	 * 
	 * @return the name
	 */
	public String getName();

	/**
	 * Gets the value of the parameter
	 * 
	 * @return the value
	 */
	public String getValue();

}
