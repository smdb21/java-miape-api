package org.proteored.miapeapi.factories.ge;

import org.proteored.miapeapi.cv.ge.BufferComponentName;
import org.proteored.miapeapi.factories.SubstanceBuilder;
import org.proteored.miapeapi.interfaces.BufferComponent;

public class BufferComponentBuilder extends SubstanceBuilder {

	/**
	 * Sets the buffer component name. It should be one of the possible values
	 * in {@link BufferComponentName}
	 * 
	 * @param name
	 */

	BufferComponentBuilder(String name) {
		super(name);
	}

	@Override
	public BufferComponent build() {
		return new BufferComponentImpl(this);
	}
}