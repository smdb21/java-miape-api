package org.proteored.miapeapi.factories.ge;

import org.proteored.miapeapi.factories.SubstanceImpl;
import org.proteored.miapeapi.interfaces.ge.DirectDetectionAgent;

public class DirectDetectionAgentImpl extends SubstanceImpl implements DirectDetectionAgent {

	public DirectDetectionAgentImpl(DirectDetectionAgentBuilder substanceBuilder) {
		super(substanceBuilder);
	}
}
