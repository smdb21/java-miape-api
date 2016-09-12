package org.proteored.miapeapi.factories.ge;

import org.proteored.miapeapi.factories.SubstanceImpl;
import org.proteored.miapeapi.interfaces.BufferComponent;

public class BufferComponentImpl extends SubstanceImpl implements BufferComponent {

	public BufferComponentImpl(BufferComponentBuilder bufferComponentBuilder) {
		super(bufferComponentBuilder);
	}
}
