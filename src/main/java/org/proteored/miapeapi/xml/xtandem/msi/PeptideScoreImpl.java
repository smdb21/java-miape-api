package org.proteored.miapeapi.xml.xtandem.msi;

import org.proteored.miapeapi.interfaces.msi.PeptideScore;

public class PeptideScoreImpl implements PeptideScore {
	private final String name;
	private final String value;

	public PeptideScoreImpl(String scoreName, double scoreValue) {
		this.name = scoreName;
		this.value = String.valueOf(scoreValue);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getValue() {
		return value;
	}

}
