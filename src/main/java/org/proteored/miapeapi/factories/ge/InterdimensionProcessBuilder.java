package org.proteored.miapeapi.factories.ge;

import java.util.Set;

import org.proteored.miapeapi.interfaces.Buffer;
import org.proteored.miapeapi.interfaces.Equipment;
import org.proteored.miapeapi.interfaces.ge.Agent;
import org.proteored.miapeapi.interfaces.ge.InterdimensionProcess;

public class InterdimensionProcessBuilder {
	final String name;

	String protocol;
	Set<Equipment> interdimensionEquipments;
	Set<Agent> interdimensionReagents;
	Set<Buffer> interdimensionBuffers;

	InterdimensionProcessBuilder(String name) {
		this.name = name;
	}

	public InterdimensionProcessBuilder protocol(String value) {
		protocol = value;
		return this;
	}

	public InterdimensionProcessBuilder interdimensionEquipments(Set<Equipment> value) {
		interdimensionEquipments = value;
		return this;
	}

	public InterdimensionProcessBuilder interdimensionBuffers(Set<Buffer> value) {
		interdimensionBuffers = value;
		return this;
	}

	public InterdimensionProcessBuilder interdimensionReagents(Set<Agent> value) {
		interdimensionReagents = value;
		return this;
	}

	public InterdimensionProcess build() {
		return new InterdimensionProcessImpl(this);
	}
}