package org.proteored.miapeapi.xml.pride.msi;

import org.proteored.miapeapi.interfaces.msi.ProteinScore;

public class ProteinScoreImpl implements ProteinScore {
	private final Double score;

	public ProteinScoreImpl(Double score) {
		this.score = score;
	}

	@Override
	public String getName() {
		return "Protein Score";
	}

	@Override
	public String getValue() {
		if (score == null)
			return null;
		return String.valueOf(score);
	}
}
