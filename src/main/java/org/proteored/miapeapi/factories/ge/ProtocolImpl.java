package org.proteored.miapeapi.factories.ge;

import java.util.Set;

import org.proteored.miapeapi.interfaces.ge.Dimension;
import org.proteored.miapeapi.interfaces.ge.InterdimensionProcess;
import org.proteored.miapeapi.interfaces.ge.Protocol;

public class ProtocolImpl implements Protocol {
	private final String name;
	private final String description;
	private final Set<Dimension> dimensions;
	private final Set<InterdimensionProcess> interdimensionProcesses;

	public ProtocolImpl(ProtocolBuilder miapeGEProtocolBuilder) {
		this.name = miapeGEProtocolBuilder.name;
		this.description = miapeGEProtocolBuilder.description;
		this.dimensions = miapeGEProtocolBuilder.dimensions;
		this.interdimensionProcesses = miapeGEProtocolBuilder.interdimensionProcesses;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public Set<Dimension> getDimensions() {
		return dimensions;
	}

	@Override
	public Set<InterdimensionProcess> getInterdimensionProcesses() {
		return interdimensionProcesses;
	}

	@Override
	public String getName() {
		return name;
	}

}
