package org.proteored.miapeapi.interfaces.ge;

public interface Lane {

	/**
	 * Gets the identifier that can be referenced from Sample
	 * 
	 * @return the identifier
	 */
	public int getId();

	public String getName();

	public String getLaneNumber();

	public String getDescription();

	/**
	 * @return the sample loaded in this lane
	 */
	public Sample getReferencedSample();
}
