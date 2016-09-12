package org.proteored.miapeapi.interfaces.gi;

import org.proteored.miapeapi.interfaces.Algorithm;

/**
 * Describe the process by which the features have been matched on the image(s).
 * In terms of algorithm name, version, parameters and match editing
 * 
 * @author Salvador
 * 
 */
public interface Matching extends Algorithm {

	/**
	 * Reference image used. Name and identifier of the gel image used as a
	 * reference for the matching.
	 * 
	 * @return the reference image
	 */
	public ImageGelInformatics getReferenceImage();

	/**
	 * LAndmarkes. Describe if landmarks have been manually and/or automatically
	 * assigned.
	 * 
	 */
	public String getLandmarks();

	/**
	 * Match editing Specify if manual and/or automatic match editing has been
	 * performed by user-guided process.
	 * 
	 */
	public String getEditing();

	/**
	 * The step order of the matching process
	 * 
	 * @return the step order
	 */
	public String getStepOrder();

}
