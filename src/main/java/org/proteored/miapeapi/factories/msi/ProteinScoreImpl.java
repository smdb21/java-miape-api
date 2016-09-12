package org.proteored.miapeapi.factories.msi;

import org.proteored.miapeapi.interfaces.msi.ProteinScore;

public class ProteinScoreImpl implements ProteinScore {
	private final String scoreName;
	private final String scoreValue;

	public ProteinScoreImpl(ProteinScoreBuilder proteinScoreBuilder) {
		this.scoreName = proteinScoreBuilder.scoreName;
		this.scoreValue = proteinScoreBuilder.scoreValue;
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
