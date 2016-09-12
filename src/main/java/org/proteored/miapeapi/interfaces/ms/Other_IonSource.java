package org.proteored.miapeapi.interfaces.ms;

/**
 * The Interface other ion trap
 * Description of the ion source and relevant relevant parameters
 **/
public interface Other_IonSource {

	/**
	 * Gets the name of the ion source
	 * @return the name
	 */
	public String getName();

	/**
	 * Gets the relevant and discriminating parameters for its use.
	 * @return the parameters
	 */
	public String getParameters();
}
