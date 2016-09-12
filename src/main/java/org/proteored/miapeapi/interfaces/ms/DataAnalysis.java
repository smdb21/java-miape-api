package org.proteored.miapeapi.interfaces.ms;

import org.proteored.miapeapi.cv.msi.DataTransformation;
import org.proteored.miapeapi.interfaces.Software;

/**
 * The Interface DataAnalysis. The MS data analysis package name, and version;
 * where there are several pieces of software involved, give name, version and
 * role for each one. Mention also upgrades not reflected in the version number.
 * The information on how the spectra have been processed. This include the list
 * of parameters triggering the generation of peak lists, chromatograms, images
 * from raw data or already processed data and the order in which they have been
 * used.This can be a list or a parameters file
 */
public interface DataAnalysis extends Software {

	/**
	 * Gets the URL of the parameters file used in the generation of peak lists
	 * or processed spectra
	 * 
	 * @return the URL
	 */
	public String getParametersLocation();

	/**
	 * Gets the description of the data analysis software. It should be one of
	 * the possible values from {@link DataTransformation}
	 */
	@Override
	public String getDescription();

}
