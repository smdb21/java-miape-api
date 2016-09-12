package org.proteored.miapeapi.interfaces.ge;

import org.proteored.miapeapi.cv.ge.IndirectDetectionAgentName;
import org.proteored.miapeapi.interfaces.Substance;

public interface IndirectDetectionAgent extends Substance {
	/**
	 * Gets the name of the agent used in the indirent detection. It should be
	 * one of the possible values of {@link IndirectDetectionAgentName}
	 */
	public String getName();

}
