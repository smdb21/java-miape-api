package org.proteored.miapeapi.factories.ge;

import java.util.Set;

import org.proteored.miapeapi.interfaces.Buffer;
import org.proteored.miapeapi.interfaces.ge.Dimension;
import org.proteored.miapeapi.interfaces.ge.ElectrophoresisProtocol;
import org.proteored.miapeapi.interfaces.ge.GelMatrix;

public class DimensionImpl implements Dimension {
	private final String name;
	private final String dimension;
	private final String separationMethod;
	private final Set<Buffer> loadingBuffers;
	private final Set<ElectrophoresisProtocol> electrophoresisProtocols;
	private final Set<GelMatrix> gelMatrix;
	
	public DimensionImpl(DimensionBuilder miapeGEDimensionBuilder) {
		this.name = miapeGEDimensionBuilder.name;
		this.dimension = miapeGEDimensionBuilder.dimension;
		this.separationMethod = miapeGEDimensionBuilder.separationMethod;
		this.loadingBuffers = miapeGEDimensionBuilder.loadingBuffers;
		this.electrophoresisProtocols = miapeGEDimensionBuilder.electrophoresisProtocols;
		this.gelMatrix = miapeGEDimensionBuilder.gelMatrix;
	}

	@Override
	public String getDimension() {
		return dimension;
	}

	@Override
	public Set<Buffer> getLoadingBuffers() {
		return loadingBuffers;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getSeparationMethod() {
		return separationMethod;
	}

	@Override
	public Set<ElectrophoresisProtocol> getElectrophoresisProtocols() {
		return electrophoresisProtocols;
	}

	@Override
	public Set<GelMatrix> getMatrixes() {
		return gelMatrix;
	}

}
