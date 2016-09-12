package org.proteored.miapeapi.factories.msi;

import org.proteored.miapeapi.cv.msi.Score;
import org.proteored.miapeapi.interfaces.msi.ProteinScore;

public class ProteinScoreBuilder {
	String scoreName;
	String scoreValue;

	/**
	 * Set the score name and value. The name should be one of the possible
	 * values from {@link Score}
	 */
	public ProteinScoreBuilder(String scoreName, String scoreValue) {
		this.scoreName = scoreName;
		this.scoreValue = scoreValue;
	}

	public ProteinScore build() {
		return new ProteinScoreImpl(this);
	}

}
