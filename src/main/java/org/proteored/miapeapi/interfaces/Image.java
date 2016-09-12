package org.proteored.miapeapi.interfaces;

import org.proteored.miapeapi.cv.ge.ImageDimensionUnit;
import org.proteored.miapeapi.cv.ge.ImageFormat;
import org.proteored.miapeapi.cv.ge.ImageResolutionUnit;

public abstract interface Image {

	/**
	 * Gets the identifier that can be referenced from several protocol steps in
	 * MIAPE GI
	 * 
	 * @return the identifier
	 */
	public int getId();

	public String getName();

	/**
	 * Gets the image format. It should be one of the possible values of
	 * {@link ImageFormat}
	 * 
	 * @return the format
	 */
	public String getFormat();

	public String getDimensionX();

	public String getDimensionY();

	/**
	 * Gets the image dimensions. It should be one of the possible values of
	 * {@link ImageDimensionUnit}
	 * 
	 * @return the image dimensions
	 */
	public String getDimensionUnit();

	public String getResolution();

	/**
	 * Gets the image resolution unit. It should be one of the possible values
	 * in {@link ImageResolutionUnit}
	 * 
	 * @return the image resolution unit
	 */
	public String getResolutionUnit();

	public String getBitDepth();

	public String getLocation();

	public String getOrientation();

}
