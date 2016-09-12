package org.proteored.miapeapi.factories.ge;

import java.util.Set;

import org.proteored.miapeapi.interfaces.Buffer;
import org.proteored.miapeapi.interfaces.Equipment;
import org.proteored.miapeapi.interfaces.ge.Agent;
import org.proteored.miapeapi.interfaces.ge.InterdimensionProcess;

public class InterdimensionProcessImpl implements InterdimensionProcess {
	private final String name;
	private final String protocol;
	private final Set<Equipment> interdimensionEquipments;
	private final Set<Agent> interdimensionReagents;
	private final Set<Buffer> interdimensionBuffers;

	public InterdimensionProcessImpl(InterdimensionProcessBuilder miapeGEInterdimensionProcessBuilder) {
		this.name = miapeGEInterdimensionProcessBuilder.name;
		this.protocol = miapeGEInterdimensionProcessBuilder.protocol;
		this.interdimensionEquipments = miapeGEInterdimensionProcessBuilder.interdimensionEquipments;
		this.interdimensionReagents = miapeGEInterdimensionProcessBuilder.interdimensionReagents;
		this.interdimensionBuffers = miapeGEInterdimensionProcessBuilder.interdimensionBuffers;
	}

	@Override
	public Set<Buffer> getInterdimensionBuffers() {
		return interdimensionBuffers;
	}

	@Override
	public Set<Equipment> getInterdimensionEquipments() {
		return interdimensionEquipments;
	}

	@Override
	public Set<Agent> getInterdimensionReagents() {
		return interdimensionReagents;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getProtocol() {
		return protocol;
	}

}
