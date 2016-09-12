package org.proteored.miapeapi.factories.msi;

import org.proteored.miapeapi.interfaces.msi.PeptideScore;

public class PeptideScoreImpl implements PeptideScore {
	private final String scoreName;
	private final String scoreValue;

	public PeptideScoreImpl(PeptideScoreBuilder peptideScoreBuilder) {
		this.scoreName = peptideScoreBuilder.name;
		this.scoreValue = peptideScoreBuilder.value;
	}

	@Override
	public String getName() {
		return scoreName;
	}

	@Override
	public String getValue() {
		return scoreValue;
	}

}
