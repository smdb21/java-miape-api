package org.proteored.miapeapi.interfaces.ms;

import org.proteored.miapeapi.cv.ms.LaserType;

/**
 * The Interface Maldi.
 */
public interface Maldi {

	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName();

	/**
	 * Gets other laser related parameters, if discriminating for the experiment
	 * (such as pulse energy, attenuation, focus diameter, pulse duration at
	 * full-width half maximum (FWHM), frequency of shots (Hz) and average
	 * number of shots fired to generate each combined mass spectrum
	 * 
	 * 
	 * @return the laser parameters
	 */
	public String getLaserParameters();

	/**
	 * Gets the plate type. The material of which the target plate is made
	 * (usually stainless steel, or coated glass); if the plate has a special
	 * construction then that should be briefly described and catalogue and lot
	 * numbers given where available
	 * 
	 * @return the plate type
	 */
	public String getPlateType();

	/**
	 * Gets the material in shich the sample is embedded on the target (e.g.
	 * alpha-cyano-4-hidroxycinnamic acid)
	 * 
	 * @return the matrix
	 */
	public String getMatrix();

	/**
	 * Confirms whether post-source decay, laser-induced decomposition, or
	 * in-source dissociation was performed;
	 * 
	 * @return the dissociation method performed
	 */
	public String getDissociation();

	/**
	 * If post-source decay, laser-induced decomposition, or in-source
	 * dissociation was performed, provide a brief description of the process
	 * (for example, summarise the stepwise reduction of reflector voltage).
	 * 
	 * @return the summary
	 */
	public String getDissociationSummary();

	/**
	 * Operation with or without delayed extraction. Gets whether a delay
	 * between laser shot and ion acceleration is employed.
	 * 
	 * @return the extraction
	 */
	public String getExtraction();

	/**
	 * Gets the type of laser of the generated pulse. It should be one of the
	 * possible values from {@link LaserType}
	 * 
	 * @return the laser
	 */
	public String getLaser();

	/**
	 * Gets the the laser wavelength of the generated pulse (in nanometres).
	 * 
	 * @return the laser wave length
	 */
	public String getLaserWaveLength();

}
