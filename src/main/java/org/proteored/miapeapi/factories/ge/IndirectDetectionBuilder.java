package org.proteored.miapeapi.factories.ge;

import java.util.Set;

import org.proteored.miapeapi.interfaces.ge.IndirectDetection;
import org.proteored.miapeapi.interfaces.ge.IndirectDetectionAgent;

public class IndirectDetectionBuilder extends AbstractDetectionBuilder {
	String transferMedium;
	String detectionMedium;
	Set<IndirectDetectionAgent> agents;
	Set<IndirectDetectionAgent> additionalAgents;

	IndirectDetectionBuilder(String name) {
		super(name);
	}

	public IndirectDetectionBuilder transferMedium(String value) {
		this.transferMedium = value;
		return this;
	}

	public IndirectDetectionBuilder detectionMedium(String value) {
		this.detectionMedium = value;
		return this;
	}

	public IndirectDetectionBuilder agents(Set<IndirectDetectionAgent> value) {
		this.agents = value;
		return this;
	}

	public IndirectDetectionBuilder additionalAgents(Set<IndirectDetectionAgent> value) {
		this.additionalAgents = value;
		return this;
	}

	@Override
	public IndirectDetection build() {
		return new IndirectDetectionImpl(this);
	}
}