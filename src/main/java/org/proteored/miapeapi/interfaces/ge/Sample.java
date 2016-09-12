package org.proteored.miapeapi.interfaces.ge;


public interface Sample {

	/**
	 * Gets the identifier that can be referenced from Lane
	 * @return the identifier
	 */
	public int getId();

	public String getName();

	public String getDescription();

}
