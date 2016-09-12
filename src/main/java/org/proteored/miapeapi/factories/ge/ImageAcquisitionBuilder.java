package org.proteored.miapeapi.factories.ge;

import java.util.Set;

import org.proteored.miapeapi.interfaces.ge.GelMatrix;
import org.proteored.miapeapi.interfaces.ge.ImageAcquisition;
import org.proteored.miapeapi.interfaces.ge.ImageAcquisitionEquipment;
import org.proteored.miapeapi.interfaces.ge.ImageAcquisitionSoftware;

public class ImageAcquisitionBuilder {
	final String name;
	GelMatrix gelMatrix;
	String protocol;
	Set<ImageAcquisitionSoftware> equipmentSoftwares;
	Set<ImageAcquisitionEquipment> equipments;

	ImageAcquisitionBuilder(String name) {
		this.name = name;
	}

	public ImageAcquisitionBuilder gelMatrix(GelMatrix value) {
		gelMatrix = value;
		return this;
	}

	public ImageAcquisitionBuilder protocol(String value) {
		protocol = value;
		return this;
	}

	public ImageAcquisitionBuilder equipmentSoftwares(Set<ImageAcquisitionSoftware> value) {
		equipmentSoftwares = value;
		return this;
	}

	public ImageAcquisitionBuilder equipments(Set<ImageAcquisitionEquipment> value) {
		equipments = value;
		return this;
	}

	public ImageAcquisition build() {
		return new ImageAcquisitionImpl(this);
	}
}