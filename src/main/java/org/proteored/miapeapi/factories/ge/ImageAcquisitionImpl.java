package org.proteored.miapeapi.factories.ge;

import java.util.Set;

import org.proteored.miapeapi.interfaces.ge.GelMatrix;
import org.proteored.miapeapi.interfaces.ge.ImageAcquisition;
import org.proteored.miapeapi.interfaces.ge.ImageAcquisitionEquipment;
import org.proteored.miapeapi.interfaces.ge.ImageAcquisitionSoftware;

public class ImageAcquisitionImpl implements ImageAcquisition {
	private final String name;
	private final GelMatrix gelMatrix;
	private final String protocol;
	private final Set<ImageAcquisitionSoftware> equipmentSoftwares;	 
	private final Set<ImageAcquisitionEquipment> equipments;

	public ImageAcquisitionImpl(ImageAcquisitionBuilder imageAcquisitionBuilder) {
		this.name = imageAcquisitionBuilder.name;
		this.gelMatrix = imageAcquisitionBuilder.gelMatrix;
		this.protocol = imageAcquisitionBuilder.protocol;
		this.equipmentSoftwares = imageAcquisitionBuilder.equipmentSoftwares;
		this.equipments = imageAcquisitionBuilder.equipments;
	}

	@Override
	public Set<ImageAcquisitionSoftware> getImageAcquisitionSoftwares() {
		return equipmentSoftwares;
	}



	@Override
	public GelMatrix getReferencedGelMatrix() {
		return gelMatrix;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getProtocol() {
		return protocol;
	}

	@Override
	public Set<ImageAcquisitionEquipment> getImageAcquisitionEquipments() {
		return equipments;
	}

}
