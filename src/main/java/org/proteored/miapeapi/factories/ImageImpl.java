package org.proteored.miapeapi.factories;

import org.proteored.miapeapi.interfaces.Image;
import org.proteored.miapeapi.xml.util.MiapeXmlUtil;

public class ImageImpl implements Image {

	private final String name;
	private final String format;
	private final String dimensionX;
	private final String dimensionY;
	private final String dimensionUnit;
	private final String resolution;
	private final String resolutionUnit;
	private final String bitDepth;
	private final String location;
	private final String orientation;

	public ImageImpl(ImageBuilder imageBuilder) {
		this.name = imageBuilder.name;
		this.format = imageBuilder.format;
		this.dimensionX = imageBuilder.dimensionX;
		this.dimensionY = imageBuilder.dimensionY;
		this.dimensionUnit = imageBuilder.dimensionUnit;
		this.resolution = imageBuilder.resolution;
		this.resolutionUnit = imageBuilder.resolutionUnit;
		this.bitDepth = imageBuilder.bitDepth;
		this.location = imageBuilder.location;
		this.orientation = imageBuilder.orientation;
	}

	@Override
	public String getBitDepth() {
		return bitDepth;
	}

	@Override
	public String getDimensionUnit() {
		return dimensionUnit;
	}

	@Override
	public String getDimensionX() {
		return dimensionX;
	}

	@Override
	public String getDimensionY() {
		return dimensionY;
	}

	@Override
	public String getFormat() {
		return format;
	}

	@Override
	public String getLocation() {
		return location;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getOrientation() {
		return orientation;
	}

	@Override
	public String getResolution() {
		return resolution;
	}

	@Override
	public String getResolutionUnit() {
		return resolutionUnit;
	}

	@Override
	public int getId() {
		return MiapeXmlUtil.ImageCounter.increaseCounter();
	}
}
