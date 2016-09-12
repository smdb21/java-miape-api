package org.proteored.miapeapi.factories.ge;


import java.util.Set;

import org.proteored.miapeapi.interfaces.ge.DirectDetection;
import org.proteored.miapeapi.interfaces.ge.DirectDetectionAgent;

public class DirectDetectionImpl extends AbstractDetectionImpl implements DirectDetection {
	private final Set<DirectDetectionAgent> agents;
	private final Set<DirectDetectionAgent> additionalAgents;

	public DirectDetectionImpl(DirectDetectionBuilder directDetectionBuilder) {
		super(directDetectionBuilder);
		this.additionalAgents = directDetectionBuilder.additionalAgents;
		this.agents = directDetectionBuilder.agents;
	}

	@Override
	public Set<DirectDetectionAgent> getAdditionalAgents() {
		return additionalAgents;
	}

	@Override
	public Set<DirectDetectionAgent> getAgents() {
		return agents;
	}


}
