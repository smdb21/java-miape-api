package org.proteored.miapeapi.interfaces;

import org.proteored.miapeapi.cv.ge.SubstanceConcentrationUnit;
import org.proteored.miapeapi.cv.ge.SubstanceMassUnit;
import org.proteored.miapeapi.cv.ge.SubstanceVolumeUnit;

public interface Substance {

	/**
	 * Identifier need by gelML exporter
	 * 
	 */
	public int getId();

	public String getName();

	public String getType();

	public String getVolume();

	/**
	 * Gets the volumen unit. It should be one of the possible values of
	 * {@link SubstanceVolumeUnit}
	 **/
	public String getVolumeUnit();

	public String getConcentration();

	/**
	 * Gets the volumen unit. It should be one of the possible values of
	 * {@link SubstanceConcentrationUnit}
	 **/
	public String getConcentrationUnit();

	public String getMass();

	/**
	 * Gets the mass unit. It should be one of the possible values of
	 * {@link SubstanceMassUnit}
	 **/
	public String getMassUnit();

	public String getDescription();
}
