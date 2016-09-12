package org.proteored.miapeapi.interfaces.ge;

import java.util.Set;

public interface ImageAcquisition {

	public GelMatrix getReferencedGelMatrix();

	public String getName();

	public String getProtocol();

	public Set<ImageAcquisitionSoftware> getImageAcquisitionSoftwares();

	public Set<ImageAcquisitionEquipment> getImageAcquisitionEquipments();

}
