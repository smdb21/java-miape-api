package org.proteored.miapeapi.factories.ge;

import org.proteored.miapeapi.cv.ge.AcquisitionEquipmentType;
import org.proteored.miapeapi.factories.EquipmentBuilder;
import org.proteored.miapeapi.interfaces.ge.ImageAcquisitionEquipment;

public class ImageAcquisitionEquipmentBuilder extends EquipmentBuilder {
	String type;
	Integer calibration;
	String equipmentParameters;
	String parametersUrl;

	ImageAcquisitionEquipmentBuilder(String name) {
		super(name);
	}

	/**
	 * Set the image acquisition equipment type. It should be one of the
	 * possible values from {@link AcquisitionEquipmentType}
	 */
	public ImageAcquisitionEquipmentBuilder type(String value) {
		type = value;
		return this;
	}

	public ImageAcquisitionEquipmentBuilder calibration(Integer value) {
		calibration = value;
		return this;
	}

	public ImageAcquisitionEquipmentBuilder equipmentParameters(String value) {
		equipmentParameters = value;
		return this;
	}

	public ImageAcquisitionEquipmentBuilder parametersUrl(String value) {
		parametersUrl = value;
		return this;
	}

	@Override
	public ImageAcquisitionEquipment build() {
		return new ImageAcquisitionEquipmentImpl(this);
	}
}