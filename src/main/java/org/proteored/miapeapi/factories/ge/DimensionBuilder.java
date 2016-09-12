package org.proteored.miapeapi.factories.ge;

import java.util.Set;

import org.proteored.miapeapi.interfaces.Buffer;
import org.proteored.miapeapi.interfaces.ge.Dimension;
import org.proteored.miapeapi.interfaces.ge.ElectrophoresisProtocol;
import org.proteored.miapeapi.interfaces.ge.GelMatrix;

public class DimensionBuilder {
	final String name;
	String dimension;
	String separationMethod;
	Set<Buffer> loadingBuffers;
	Set<ElectrophoresisProtocol> electrophoresisProtocols;
	Set<GelMatrix> gelMatrix;

	DimensionBuilder(String name) {
		this.name = name;
	}

	public DimensionBuilder dimension(String value) {
		dimension = value;
		return this;
	}

	public DimensionBuilder separationMethod(String value) {
		separationMethod = value;
		return this;
	}

	public DimensionBuilder loadingBuffers(Set<Buffer> value) {
		loadingBuffers = value;
		return this;
	}

	public DimensionBuilder electrophoresisProtocols(Set<ElectrophoresisProtocol> value) {
		electrophoresisProtocols = value;
		return this;
	}

	public DimensionBuilder gelMatrix(Set<GelMatrix> value) {
		gelMatrix = value;
		return this;
	}

	public Dimension build() {
		return new DimensionImpl(this);
	}
}