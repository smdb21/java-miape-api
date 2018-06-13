package org.proteored.miapeapi.factories.msi;

import org.proteored.miapeapi.cv.msi.Score;
import org.proteored.miapeapi.interfaces.msi.PeptideScore;

public class PeptideScoreBuilder {
	// mandatory
	String name;
	String value;

	/**
	 * Set the score name. It should be one of the possible values from
	 * {@link Score}
	 */
	public PeptideScoreBuilder(String scoreName, String scoreValue) {
		name = scoreName;
		value = scoreValue;
	}

	public PeptideScore build() {
		return new PeptideScoreImpl(this);
	}
}
