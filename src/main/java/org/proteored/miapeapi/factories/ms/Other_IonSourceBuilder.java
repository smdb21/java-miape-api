package org.proteored.miapeapi.factories.ms;

import org.proteored.miapeapi.interfaces.ms.Other_IonSource;

public class Other_IonSourceBuilder {

	final String name;
	String parameters;

	 Other_IonSourceBuilder(String name) {
		this.name = name;
	}

	public Other_IonSourceBuilder parameters(String value) {
		this.parameters = value;
		return this;
	}

	public Other_IonSource build() {
		return new Other_IonSourceImpl(this);
	}
}