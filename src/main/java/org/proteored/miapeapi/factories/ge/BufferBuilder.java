package org.proteored.miapeapi.factories.ge;

import java.util.Set;

import org.proteored.miapeapi.cv.ge.BufferType;
import org.proteored.miapeapi.interfaces.Buffer;
import org.proteored.miapeapi.interfaces.BufferComponent;

public class BufferBuilder {
	// Required parameters
	final String name;

	String description;
	String type;
	Set<BufferComponent> components;

	BufferBuilder(String name) {
		this.name = name;
	}

	public BufferBuilder description(String value) {
		this.description = value;
		return this;
	}

	/**
	 * Sets the buffer type. It should be one of the values from
	 * {@link BufferType}
	 */
	public BufferBuilder type(String value) {
		this.type = value;
		return this;
	}

	public BufferBuilder components(Set<BufferComponent> value) {
		this.components = value;
		return this;
	}

	public Buffer build() {
		return new BufferImpl(this);
	}
}