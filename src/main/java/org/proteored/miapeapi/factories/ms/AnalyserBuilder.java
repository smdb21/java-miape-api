package org.proteored.miapeapi.factories.ms;

import org.proteored.miapeapi.cv.ms.MassAnalyzerType;
import org.proteored.miapeapi.cv.ms.ReflectronState;
import org.proteored.miapeapi.interfaces.ms.Analyser;

public class AnalyserBuilder {

	final String name;
	String reflectron;
	String description;

	/**
	 * Set the analyzer name. It should be one of the possible values from {@link MassAnalyzerType}
	 * 
	 */
	AnalyserBuilder(String name) {
		this.name = name;
	}

	/**
	 * Set the reflectron state. It should be one of the possible values from
	 * {@link ReflectronState}
	 * 
	 */
	public AnalyserBuilder reflectron(String reflectron) {
		this.reflectron = reflectron;
		return this;
	}

	/**
	 * Set the description of the analyzer.
	 * 
	 */
	public AnalyserBuilder description(String description) {
		this.description = description;
		return this;
	}

	public Analyser build() {
		return new AnalyzerImpl(this);
	}
}