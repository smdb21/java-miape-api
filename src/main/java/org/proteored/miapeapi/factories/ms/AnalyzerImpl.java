package org.proteored.miapeapi.factories.ms;

import org.proteored.miapeapi.interfaces.ms.Analyser;

public class AnalyzerImpl implements Analyser {

	private final String name;
	private final String reflectron;
	private final String description;

	@SuppressWarnings("unused")
	private AnalyzerImpl() {
		this(null);
	}

	public AnalyzerImpl(AnalyserBuilder analyzerBuilder) {
		this.name = analyzerBuilder.name;
		this.reflectron = analyzerBuilder.reflectron;
		this.description = analyzerBuilder.description;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getReflectron() {
		return reflectron;
	}

	@Override
	public String getDescription() {
		return description;
	}

}
