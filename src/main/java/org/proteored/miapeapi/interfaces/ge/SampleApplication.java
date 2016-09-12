package org.proteored.miapeapi.interfaces.ge;

import java.util.Set;

public interface SampleApplication {

	public String getName();

	public String getDescription();
	
	public Set<Lane> getLanes();
}
