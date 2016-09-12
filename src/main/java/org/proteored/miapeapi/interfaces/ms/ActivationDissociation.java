package org.proteored.miapeapi.interfaces.ms;

import org.proteored.miapeapi.cv.ms.DissociationMethod;
import org.proteored.miapeapi.cv.ms.GasType;
import org.proteored.miapeapi.cv.ms.PressureUnit;

/**
 * The Interface CollisionCell. The composition and pressure of the gas used to
 * fragment ions in the collision cell
 * 
 */
public interface ActivationDissociation {

	/**
	 * Gets the hardware element where activation and/or dissociation occurs.
	 * For instance a quadrupole collision cell, a 3D ion trap, the ion source
	 * (for ISD, PSD, LID)
	 * 
	 * @return the instrument component name
	 */
	public String getName();

	/**
	 * Gets the composition of the gas. It should be one of the possible values
	 * from {@link GasType}
	 * 
	 * @return the gas type
	 */
	public String getGasType();

	/**
	 * Gets the gas pressure.
	 * 
	 * @return the gas pressure
	 */
	public String getGasPressure();

	/**
	 * Gets the gas pressure. It should be one of the possible values from
	 * {@link PressureUnit}
	 * 
	 * @return the gas pressure
	 */

	public String getPressureUnit();

	/**
	 * Gets the type of the activation and/or dissociation used in the
	 * fragmentation process. It should be one of the possible values from
	 * {@link DissociationMethod}
	 * 
	 * @return the activation and/or dissociation type
	 */

	public String getActivationType();
}
