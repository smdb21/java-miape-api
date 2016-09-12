package org.proteored.miapeapi.interfaces.gi;

/**
 * Type and description of editing performed, for example rotation, 
 * scaling, resizing or resolution change, inverting gray level, 
 * format change, defining an inclusion region (regular or irregular cropping).
 * @author Salvador
 *
 */
public interface ImagePreparationStep {

	/**
	 * The name of the editing
	 * @return the name of the editing
	 */
	public String getName();

	/**
	 * Type and description of editing performed, for example rotation, scaling, resizing or resolution change, inverting gray level, format change, defining an inclusion region (regular or irregular cropping).
	 * @return type of the editing performed
	 */
	public String getType();

	/** 
	 * The order step
	 * @return the order step
	 */
	public String getStepOrder();

	/**
	 * Any parameters used in the editing step.
	 * @return any parameters
	 */
	public String getParameters();

}
