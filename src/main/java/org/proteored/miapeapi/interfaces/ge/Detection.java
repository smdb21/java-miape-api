package org.proteored.miapeapi.interfaces.ge;

import java.util.Set;

import org.proteored.miapeapi.interfaces.Buffer;
import org.proteored.miapeapi.interfaces.Equipment;

public interface Detection {

	/**
	 * Identifier needed by gelML exporter
	 * 
	 */
	public int getId();

	public String getName();

	public String getProtocol();

	public Set<Buffer> getBuffers();

	public Set<Equipment> getDetectionEquipments();

}
