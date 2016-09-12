package org.proteored.miapeapi.factories.ge;

import java.util.Set;

import org.proteored.miapeapi.interfaces.Buffer;
import org.proteored.miapeapi.interfaces.ge.ElectrophoresisProtocol;

public class ElectrophoresisProtocolBuilder {
	final String name;
	String electrophoresisConditions;
	String url;
	Set<Buffer> additionalBuffers;
	Set<Buffer> runningBuffers;
	int id;

	ElectrophoresisProtocolBuilder(String name) {
		this.name = name;
	}

	public ElectrophoresisProtocolBuilder electrophoresisConditions(String value) {
		electrophoresisConditions = value;
		return this;
	}

	public ElectrophoresisProtocolBuilder url(String value) {
		url = value;
		return this;
	}

	public ElectrophoresisProtocolBuilder additionalBuffers(Set<Buffer> value) {
		additionalBuffers = value;
		return this;
	}

	public ElectrophoresisProtocolBuilder runningBuffers(Set<Buffer> value) {
		runningBuffers = value;
		return this;
	}

	public ElectrophoresisProtocolBuilder id(int value) {
		id = value;
		return this;
	}

	public ElectrophoresisProtocol build() {
		return new ElectrophoresisProtocolImpl(this);
	}
}