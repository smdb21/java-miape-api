package org.proteored.miapeapi.factories.ge;

import java.util.Set;

import org.proteored.miapeapi.interfaces.ge.DirectDetection;
import org.proteored.miapeapi.interfaces.ge.DirectDetectionAgent;

public class DirectDetectionBuilder extends AbstractDetectionBuilder {
	Set<DirectDetectionAgent> additionalAgents;
	Set<DirectDetectionAgent> agents;

	DirectDetectionBuilder(String name) {
		super(name);
	}

	public DirectDetectionBuilder additionalAgents(Set<DirectDetectionAgent> additionalAgents) {
		this.additionalAgents = additionalAgents;
		return this;
	}

	public DirectDetectionBuilder detectionAgents(Set<DirectDetectionAgent> agents) {
		this.agents = agents;
		return this;
	}

	@Override
	public DirectDetection build() {
		return new DirectDetectionImpl(this);
	}
}