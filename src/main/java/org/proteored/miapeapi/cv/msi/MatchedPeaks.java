package org.proteored.miapeapi.cv.msi;

import org.proteored.miapeapi.cv.Accession;
import org.proteored.miapeapi.cv.ControlVocabularyTerm;

public enum MatchedPeaks implements ControlVocabularyTerm {
	NUMBER_OF_MATCHED_PEAKS("MS:1001121", "number of matched peaks"), NUMBER_OF_UNMATCHED_PEAKS(
			"MS:1001362",
			"number of unmatched peaks");

	private String cvId;
	private String description;

	private MatchedPeaks(String cvId, String description) {
		this.cvId = cvId;
		this.description = description;
	}

	@Override
	public String getCVRef() {
		return "MS";
	}

	@Override
	public Accession getTermAccession() {
		return new Accession(cvId);
	}

	@Override
	public String getPreferredName() {
		return description;
	}

	public static MatchedPeaks getByID(String id) {
		for (MatchedPeaks item : MatchedPeaks.values()) {
			if (item.cvId.equals(id)) {
				return item;
			}
		}
		return null;
	}

	public static MatchedPeaks getByDescription(String description) {
		for (MatchedPeaks item : MatchedPeaks.values()) {
			if (item.description.equals(description)) {
				return item;
			}
		}
		return null;
	}
}
