package org.proteored.miapeapi.interfaces;

import java.util.Set;

import org.proteored.miapeapi.cv.ge.BufferType;

public interface Buffer {

	/**
	 * Identifier needed by gelML exporter
	 * 
	 */
	public int getId();

	public String getName();

	public String getDescription();

	/**
	 * Gets the buffer type. It should be one of the possible values from
	 * {@link BufferType}
	 * 
	 * @return the buffer type
	 */
	public String getType();

	public Set<BufferComponent> getComponents();
}
