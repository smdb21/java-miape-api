package org.proteored.miapeapi.interfaces.ge;

import java.util.Set;

import org.proteored.miapeapi.interfaces.Buffer;
import org.proteored.miapeapi.interfaces.Equipment;

public interface InterdimensionProcess {

	public String getName();

	public String getProtocol();

	public Set<Equipment> getInterdimensionEquipments();

	public Set<Agent> getInterdimensionReagents();

	public Set<Buffer> getInterdimensionBuffers();

}
