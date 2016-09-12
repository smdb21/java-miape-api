package org.proteored.miapeapi.interfaces;

import org.proteored.miapeapi.cv.ge.BufferComponentName;

public interface BufferComponent extends Substance {
	/**
	 * Gets the name of the buffer component. It should be one of the possible
	 * values in {@link BufferComponentName}
	 */
	public String getName();
}
