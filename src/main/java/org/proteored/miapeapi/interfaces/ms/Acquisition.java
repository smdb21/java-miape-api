package org.proteored.miapeapi.interfaces.ms;

import org.proteored.miapeapi.interfaces.Software;

/**
 * The Interface Acquisition. The instrument management and data analysis
 * package name, and version; where there are several pieces of software
 * involved, give name, version and role for each one. Mention also upgrades not
 * reflected in the version number. The information on how the MS data have been
 * generated. It describes the Mass spectrometer's parameter settings /
 * acquisition method file or information describing the acquisition conditions
 * and settings of the MS run. Ideally this should be a URI+filename, for
 * example an export of the acquisition method. An explicit text description of
 * the acquisition process is also desirable.
 */
public interface Acquisition extends Software {

	/**
	 * Gets the acquisition parameters file location.
	 * 
	 * @return the location of the acquisition parameters file
	 */
	public String getParameterFile();

	/**
	 * For MRM experiments, get the location of the transition list file(s)
	 * 
	 * @return the location of the transition list files
	 */
	public String getTransitionListFile();

	/**
	 * For MRM experiments, target list (or inclusion list) configured prior to
	 * the run
	 * 
	 * @return the target list
	 */
	public String getTargetList();
}
