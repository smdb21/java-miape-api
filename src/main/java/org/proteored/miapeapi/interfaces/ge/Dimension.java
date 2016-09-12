package org.proteored.miapeapi.interfaces.ge;

import java.util.Set;

import org.proteored.miapeapi.interfaces.Buffer;

public interface Dimension {
	
	public String getName();

	public String getDimension();

	public String getSeparationMethod();

	public Set<Buffer> getLoadingBuffers();
	
	public Set<ElectrophoresisProtocol> getElectrophoresisProtocols();
	
	public Set<GelMatrix> getMatrixes();
}
