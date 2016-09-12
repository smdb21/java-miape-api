package org.proteored.miapeapi.factories.ge;

import java.util.Set;

import org.proteored.miapeapi.interfaces.Buffer;
import org.proteored.miapeapi.interfaces.Equipment;
import org.proteored.miapeapi.interfaces.ge.Detection;

public class AbstractDetectionImpl implements Detection {
	private final String name;
	private final String protocol;
	// private final Set<Substance> additionalAgents;
	// private final Set<Substance> detectionAgents;
	private final Set<Buffer> buffers;
	private final Set<Equipment> equipments;
	private final int id;

	public AbstractDetectionImpl(AbstractDetectionBuilder directDetectionBuilder) {
		this.name = directDetectionBuilder.name;
		this.protocol = directDetectionBuilder.protocol;
		this.buffers = directDetectionBuilder.buffers;
		this.equipments = directDetectionBuilder.equipments;
		this.id = directDetectionBuilder.id;
	}

	@Override
	public Set<Buffer> getBuffers() {
		return buffers;
	}

	@Override
	public Set<Equipment> getDetectionEquipments() {
		return equipments;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getProtocol() {
		return protocol;
	}

	@Override
	public int getId() {
		return id;
	}

}
