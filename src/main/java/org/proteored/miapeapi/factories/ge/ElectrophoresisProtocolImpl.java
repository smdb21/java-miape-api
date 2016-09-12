package org.proteored.miapeapi.factories.ge;

import java.util.Set;

import org.proteored.miapeapi.interfaces.Buffer;
import org.proteored.miapeapi.interfaces.ge.ElectrophoresisProtocol;

public class ElectrophoresisProtocolImpl implements ElectrophoresisProtocol {
	private final String name;
	private final String electrophoresisConditions;
	private final String url;
	private final Set<Buffer> additionalBuffers;
	private final Set<Buffer> runningBuffers;
	private final int id;

	public ElectrophoresisProtocolImpl(ElectrophoresisProtocolBuilder miapeGEElectrophoresisProtocolBuilder) {
		this.additionalBuffers = miapeGEElectrophoresisProtocolBuilder.additionalBuffers;
		this.name = miapeGEElectrophoresisProtocolBuilder.name;
		this.url = miapeGEElectrophoresisProtocolBuilder.url;
		this.electrophoresisConditions = miapeGEElectrophoresisProtocolBuilder.electrophoresisConditions;
		this.runningBuffers = miapeGEElectrophoresisProtocolBuilder.runningBuffers;
		this.id = miapeGEElectrophoresisProtocolBuilder.id;
	}

	@Override
	public Set<Buffer> getAdditionalBuffers() {
		return additionalBuffers;
	}

	@Override
	public String getElectrophoresisConditions() {
		return electrophoresisConditions;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Set<Buffer> getRunningBuffers() {
		return runningBuffers;
	}

	@Override
	public int getId() {
		return id;
	}

}
