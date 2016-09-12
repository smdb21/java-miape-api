package org.proteored.miapeapi.interfaces.ge;

import java.util.Set;


public interface IndirectDetection extends Detection {

	public Set<IndirectDetectionAgent> getAdditionalAgents();

	public Set<IndirectDetectionAgent> getAgents();

	public String getTransferMedium();

	public String getDetectionMedium();

}
