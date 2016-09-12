package org.proteored.miapeapi.factories.ge;

import java.util.Set;

import org.proteored.miapeapi.interfaces.Buffer;
import org.proteored.miapeapi.interfaces.Equipment;
import org.proteored.miapeapi.interfaces.ge.Detection;

public abstract class AbstractDetectionBuilder {
	final String name;
	String protocol;
	Set<Buffer> buffers;
	Set<Equipment> equipments;
	int id;

	AbstractDetectionBuilder(String name) {
		this.name = name;
	}

	public AbstractDetectionBuilder protocol(String value) {
		protocol = value;
		return this;
	}

	public AbstractDetectionBuilder buffers(Set<Buffer> value) {
		buffers = value;
		return this;
	}

	public AbstractDetectionBuilder equipments(Set<Equipment> value) {
		equipments = value;
		return this;
	}

	public AbstractDetectionBuilder id(int value) {
		id = value;
		return this;
	}

	public abstract Detection build();
}