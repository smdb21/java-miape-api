package org.proteored.miapeapi.factories.ge;

import org.proteored.miapeapi.cv.ge.IndirectDetectionAgentName;
import org.proteored.miapeapi.factories.SubstanceBuilder;
import org.proteored.miapeapi.interfaces.ge.IndirectDetectionAgent;

public class IndirectDetectionAgentBuilder extends SubstanceBuilder {
	/**
	 * Set the name of the agent used in the indirect detection. It should be
	 * one of the possible values from {@link IndirectDetectionAgentName}
	 */
	IndirectDetectionAgentBuilder(String name) {
		super(name);
	}

	@Override
	public IndirectDetectionAgent build() {
		return new IndirectDetectionAgentImpl(this);
	}
}