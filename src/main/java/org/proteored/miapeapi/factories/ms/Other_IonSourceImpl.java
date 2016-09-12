package org.proteored.miapeapi.factories.ms;

import org.proteored.miapeapi.interfaces.ms.Other_IonSource;

public class Other_IonSourceImpl implements Other_IonSource {
	private final String parameters;
	private final String name;

	@SuppressWarnings("unused")
	private Other_IonSourceImpl() {
		this(null);
	}

	public Other_IonSourceImpl(Other_IonSourceBuilder otherIonSourcesBuilder) {
		this.parameters = otherIonSourcesBuilder.parameters;
		this.name = otherIonSourcesBuilder.name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getParameters() {
		return parameters;
	}

}
