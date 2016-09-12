package org.proteored.miapeapi.cv.msi;

import org.proteored.miapeapi.cv.Accession;
import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularySet;

public class ProteinGroupRelationship extends ControlVocabularySet {

	public static final Accession SEQUENCE_SUB_SET_PROTEIN = new Accession(
			"MS:1001596");

	public static final Accession SEQUENCE_SUBSUMABLE_PROTEIN = new Accession(
			"MS:1001598");

	public static final Accession SEQUENCE_SAME_SET_PROTEIN = new Accession(
			"MS:1001594");

	public static final Accession PROTEIN_INFERENCE_CONFIDENCE_CATEGORY = new Accession(
			"MS:1001600");

	public static final Accession PANALYZER_CONCLUSIVE_PROTEIN = new Accession(
			"MS:1002213");
	public static final Accession PANALYZER_INDISTINGUISABLE_PROTEIN = new Accession(
			"MS:1002214");
	public static final Accession PANALYZER_NON_CONCLUSIVE_PROTEIN = new Accession(
			"MS:1002215");
	public static final Accession PANALYZER_AMBIGUOUS_GROUP_MEMBER_PROTEIN = new Accession(
			"MS:1002216");

	private static ProteinGroupRelationship instance;

	public static ProteinGroupRelationship getInstance(
			ControlVocabularyManager cvManager) {
		if (instance == null)
			instance = new ProteinGroupRelationship(cvManager);
		return instance;
	}

	private ProteinGroupRelationship(ControlVocabularyManager cvManager) {
		super(cvManager);

		String[] parentAccessionsTMP = { "MS:1001101" };// protein group/subset
														// relationship
		this.parentAccessions = parentAccessionsTMP;

		String[] explicitAccessionsTMP = { PROTEIN_INFERENCE_CONFIDENCE_CATEGORY
				.toString() };
		this.explicitAccessions = explicitAccessionsTMP;
		this.setExcludeParents(true);
		this.miapeSection = -1;

	}
}
