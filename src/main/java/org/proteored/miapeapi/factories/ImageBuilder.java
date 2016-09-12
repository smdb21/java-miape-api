package org.proteored.miapeapi.factories;

import org.proteored.miapeapi.cv.ge.ImageDimensionUnit;
import org.proteored.miapeapi.cv.ge.ImageFormat;
import org.proteored.miapeapi.cv.ge.ImageResolutionUnit;
import org.proteored.miapeapi.interfaces.Image;

public class ImageBuilder {
	final String name;
	String format;
	String dimensionX;
	String dimensionY;
	String dimensionUnit;
	String resolution;
	String resolutionUnit;
	String bitDepth;
	String location;
	String orientation;

	protected ImageBuilder(String name) {
		this.name = name;

	}

	/**
	 * Sets the image format. It should be one of the possible values of
	 * {@link ImageFormat}
	 */
	public ImageBuilder format(String value) {
		format = value;
		return this;
	}

	public ImageBuilder dimensionX(String value) {
		dimensionX = value;
		return this;
	}

	public ImageBuilder dimensionY(String value) {
		dimensionY = value;
		return this;
	}

	/**
	 * 
	 * Sets the image dimension unit. Teh value should be one of the possible
	 * values in {@link ImageDimensionUnit}
	 * 
	 */
	public ImageBuilder dimensionUnit(String value) {
		dimensionUnit = value;
		return this;
	}

	public ImageBuilder resolution(String value) {
		resolution = value;
		return this;
	}

	/**
	 * 
	 * Sets the image resolution unit. It should be one of the possible values
	 * in {@link ImageResolutionUnit}
	 * 
	 */

	public ImageBuilder resolutionUnit(String value) {
		resolutionUnit = value;
		return this;
	}

	public ImageBuilder bitDepth(String value) {
		bitDepth = value;
		return this;
	}

	public ImageBuilder location(String value) {
		location = value;
		return this;
	}

	public ImageBuilder orientation(String value) {
		orientation = value;
		return this;
	}

	public Image build() {
		return new ImageImpl(this);
	}
}