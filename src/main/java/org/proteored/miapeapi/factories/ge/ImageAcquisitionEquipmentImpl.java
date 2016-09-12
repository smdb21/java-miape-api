package org.proteored.miapeapi.factories.ge;

import org.proteored.miapeapi.factories.EquipmentImpl;
import org.proteored.miapeapi.interfaces.ge.ImageAcquisitionEquipment;

public class ImageAcquisitionEquipmentImpl extends EquipmentImpl implements ImageAcquisitionEquipment {
	private final String type;
	private final Integer calibration;
	private final String parametersUrl;
	public ImageAcquisitionEquipmentImpl(
			ImageAcquisitionEquipmentBuilder imageAcquisitionEquipmentBuilder) {
		super(imageAcquisitionEquipmentBuilder);
		this.type = imageAcquisitionEquipmentBuilder.type;
		this.calibration = imageAcquisitionEquipmentBuilder.calibration;
		this.parametersUrl = imageAcquisitionEquipmentBuilder.parametersUrl;
	}

	@Override
	public Integer getCalibration() {
		return calibration;
	}

	@Override
	public String getParametersUrl() {
		return parametersUrl;
	}

	@Override
	public String getType() {
		return type;
	}

}
