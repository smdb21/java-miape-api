package org.proteored.miapeapi.factories.ge;

import org.proteored.miapeapi.cv.ge.DirectDetectionAgentName;
import org.proteored.miapeapi.factories.SubstanceBuilder;
import org.proteored.miapeapi.interfaces.ge.DirectDetectionAgent;

public class DirectDetectionAgentBuilder extends SubstanceBuilder {
	/**
	 * Sets the agent name used in teh direct detection. It should be one of the
	 * possible values in {@link DirectDetectionAgentName}
	 * 
	 * @param name
	 */
	DirectDetectionAgentBuilder(String name) {
		super(name);
	}

	@Override
	public DirectDetectionAgent build() {
		return new DirectDetectionAgentImpl(this);
	}
}