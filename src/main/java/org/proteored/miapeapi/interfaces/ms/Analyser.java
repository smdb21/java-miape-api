package org.proteored.miapeapi.interfaces.ms;

import org.proteored.miapeapi.cv.ms.MassAnalyzerType;
import org.proteored.miapeapi.cv.ms.ReflectronState;

/**
 * The Interface Analyzer. Ion optics, 'simple' quadrupole, ,hexapole, Paul trap, linear trap,
 * magnetic sector
 */
public interface Analyser {

	/**
	 * Gets the analyzer name. It should be one of the possible values from {@link MassAnalyzerType}
	 * 
	 * @return the name
	 */
	public String getName();

	/**
	 * Describe the analyser(s) used in the MS experiment. Example might be MS1 survey scans in an
	 * Orbitrap and MSn analysed in a linear trap;
	 * 
	 * @return
	 */
	public String getDescription();

	/**
	 * Gets the reflectron status. It should be one of the possible values from
	 * {@link ReflectronState}
	 * 
	 * @return the reflectron status
	 */
	public String getReflectron();

}
