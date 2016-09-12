package org.proteored.miapeapi.interfaces.ge;

import org.proteored.miapeapi.cv.ge.SubstanceName;
import org.proteored.miapeapi.interfaces.Substance;

public interface Agent extends Substance {
	/**
	 * Gets the name of the agent. It should be one of the possible values in
	 * {@link SubstanceName}
	 */
	public String getName();
}
