package org.proteored.miapeapi.xml.xtandem.msi;

import org.proteored.miapeapi.interfaces.msi.ProteinScore;

public class ProteinScoreImpl implements ProteinScore {

	private final String value;
	private final String name;

	public ProteinScoreImpl(String name, double value) {
		this.name = name;
		this.value = String.valueOf(value);
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getValue() {
		return this.value;
	}

}
