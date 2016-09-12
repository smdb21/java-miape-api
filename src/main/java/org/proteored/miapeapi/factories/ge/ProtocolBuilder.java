package org.proteored.miapeapi.factories.ge;

import java.util.Set;

import org.proteored.miapeapi.interfaces.ge.Dimension;
import org.proteored.miapeapi.interfaces.ge.InterdimensionProcess;
import org.proteored.miapeapi.interfaces.ge.Protocol;

public class ProtocolBuilder {
	final String name;
	String description;
	Set<Dimension> dimensions;
	Set<InterdimensionProcess> interdimensionProcesses;

	ProtocolBuilder(String name) {
		this.name = name;
	}

	public ProtocolBuilder description(String value) {
		description = value;
		return this;
	}

	public ProtocolBuilder dimensions(Set<Dimension> value) {
		dimensions = value;
		return this;
	}

	public ProtocolBuilder interdimensionProcesses(Set<InterdimensionProcess> value) {
		interdimensionProcesses = value;
		return this;
	}

	public Protocol build() {
		return new ProtocolImpl(this);
	}
}