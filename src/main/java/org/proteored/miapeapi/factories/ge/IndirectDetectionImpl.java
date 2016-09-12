package org.proteored.miapeapi.factories.ge;

import java.util.Set;

import org.proteored.miapeapi.interfaces.ge.IndirectDetection;
import org.proteored.miapeapi.interfaces.ge.IndirectDetectionAgent;

public class IndirectDetectionImpl extends AbstractDetectionImpl implements IndirectDetection {
	private final String detectionMedium;
	private final String transferMedium;
	private final Set<IndirectDetectionAgent> indirectDetectionAgent;
	private final Set<IndirectDetectionAgent> additionalIndirectDetectionAgent;

	public IndirectDetectionImpl(IndirectDetectionBuilder indirectDetectionBuilder) {
		super(indirectDetectionBuilder);
		this.detectionMedium = indirectDetectionBuilder.detectionMedium;
		this.transferMedium = indirectDetectionBuilder.transferMedium;
		this.indirectDetectionAgent = indirectDetectionBuilder.agents;
		this.additionalIndirectDetectionAgent = indirectDetectionBuilder.additionalAgents;
	}

	@Override
	public String getDetectionMedium() {
		return detectionMedium;
	}

	@Override
	public String getTransferMedium() {
		return transferMedium;
	}

	@Override
	public Set<IndirectDetectionAgent> getAdditionalAgents() {
		return this.indirectDetectionAgent;
	}

	@Override
	public Set<IndirectDetectionAgent> getAgents() {
		return this.indirectDetectionAgent;
	}
}
