package org.proteored.miapeapi.interfaces.ms;

import java.util.Set;

import org.proteored.miapeapi.cv.ms.EsiName;
import org.proteored.miapeapi.interfaces.Equipment;

/**
 * The Interface Esi.
 */
public interface Esi {

	/**
	 * Gets the name. It should be one of the possible values from
	 * {@link EsiName}
	 * 
	 * @return the name
	 */
	public String getName();

	/**
	 * Gets Other parameters if discriminant for the experiment (such as
	 * nebulising gas and pressure ). Where appropriate, and if considered as
	 * discriminating elements of the source parameters, describe these values
	 * 
	 * @return the parameters
	 */
	public String getParameters();

	/**
	 * Supply type (static, or fed)
	 * 
	 * @return the supply type
	 */
	public String getSupplyType();

	/**
	 * Gets the interfaces. The manufacturing company and model name for the
	 * interface; list any modifications made to the standard specification. If
	 * the interface is entirely custom-built, describe it or provide a
	 * reference if available.
	 * 
	 * @return the interfaces
	 */
	public Set<Equipment> getInterfaces();

	/**
	 * Gets the sprayers. The manufacturing company and model name for the
	 * sprayer; list any modifications made to the standard specification. If
	 * the sprayer is entirely custom-built, describe it briefly or provide a
	 * reference if available.
	 * 
	 * @return the sprayers
	 */
	public Set<Equipment> getSprayers();

}
