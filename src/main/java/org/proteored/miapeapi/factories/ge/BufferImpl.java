package org.proteored.miapeapi.factories.ge;

import java.util.Set;

import org.proteored.miapeapi.interfaces.Buffer;
import org.proteored.miapeapi.interfaces.BufferComponent;
import org.proteored.miapeapi.xml.util.MiapeXmlUtil;

public class BufferImpl implements Buffer {

	private final String name;
	private final String description;
	private final String type;
	private final Set<BufferComponent> components;

	public BufferImpl(BufferBuilder bufferBuilder) {
		this.name = bufferBuilder.name;
		this.description = bufferBuilder.description;
		this.type = bufferBuilder.type;
		this.components = bufferBuilder.components;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public Set<BufferComponent> getComponents() {
		return components;
	}

	@Override
	public int getId() {
		return MiapeXmlUtil.SubstanceCounter.increaseCounter();
	}
}
