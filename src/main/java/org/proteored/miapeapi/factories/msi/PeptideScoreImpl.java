package org.proteored.miapeapi.factories.msi;

import org.proteored.miapeapi.interfaces.msi.PeptideScore;

import edu.scripps.yates.utilities.staticstorage.StaticStrings;

public class PeptideScoreImpl implements PeptideScore {
	private final String scoreName;
	private final String scoreValue;

	public PeptideScoreImpl(PeptideScoreBuilder peptideScoreBuilder) {
		scoreName = StaticStrings.getUniqueInstance(peptideScoreBuilder.name);
		scoreValue = StaticStrings.getUniqueInstance(peptideScoreBuilder.value);
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
