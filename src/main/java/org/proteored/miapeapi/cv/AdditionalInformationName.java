package org.proteored.miapeapi.cv;

import java.util.List;

import org.proteored.miapeapi.cv.ms.ExperimentType;
import org.proteored.miapeapi.cv.msi.DataTransformation;

public class AdditionalInformationName extends ControlVocabularySet {
	/* ; */
	public static final Accession PRIDE_GENERATION_SOFTWARE_ACCESSION = new Accession(
			"PRIDE:0000175");
	public static final Accession PROJECT = new Accession("PRIDE:0000097");
	public static final Accession EXPERIMENT_DESCRIPTION = new Accession(
			"PRIDE:000004");
	public static final Accession NO_PTMS = new Accession("PRIDE:0000398");
	public static final Accession GEL_BASED_EXPERIMENT = new Accession(
			"PRIDE:0000305");

	private static AdditionalInformationName instance;
	private static boolean firstGetPossibleValues = false;

	public static AdditionalInformationName getInstance(
			ControlVocabularyManager cvManager) {

		if (instance == null)
			instance = new AdditionalInformationName(cvManager);
		return instance;
	}

	private AdditionalInformationName(ControlVocabularyManager cvManager) {
		super(cvManager);

		String[] tmpParentAccessions = { "MS:1000524", // DATA FILE CONTENT
				"MS:1000548", // SAMPLE ATTRIBUTE,
				"PRIDE:0000006", // Experiment additional parameter
				"PRIDE:0000017", // Sample description additional parameter
				"PRIDE:0000000" // Reference additional parameter
		// SEE ALSO getPsossibleValues below!!!!!!!!!!!!!!!!!!

		};
		miapeSection = 1001;
		parentAccessions = tmpParentAccessions;
		setExcludeParents(true);
	}

	// Override in order to add the MainTaxonomies values to the total of
	// possible values
	@Override
	public List<ControlVocabularyTerm> getPossibleValues() {
		final List<ControlVocabularyTerm> cachedPossibleValues = getCachedPossibleValues();
		if (firstGetPossibleValues && cachedPossibleValues != null
				&& !cachedPossibleValues.isEmpty())
			return cachedPossibleValues;
		final List<ControlVocabularyTerm> possibleValues = super
				.getPossibleValues();

		// MAIN TAXONOMIES
		final List<ControlVocabularyTerm> toAdd = MainTaxonomies.getInstance(
				null).getPossibleValues();
		for (ControlVocabularyTerm controlVocabularyTerm : toAdd) {
			possibleValues.add(controlVocabularyTerm);
		}
		// SAMPLE PROCESSING STEP
		final List<ControlVocabularyTerm> sampleProcessingTerms = SampleProcessingStep
				.getInstance(super.getCvManager()).getPossibleValues();
		for (ControlVocabularyTerm controlVocabularyTerm : sampleProcessingTerms) {
			possibleValues.add(controlVocabularyTerm);
		}
		// SAMPLE INFORMATION
		final List<ControlVocabularyTerm> sampleInformationTerms = SampleInformation
				.getInstance(super.getCvManager()).getPossibleValues();
		for (ControlVocabularyTerm controlVocabularyTerm : sampleInformationTerms) {
			possibleValues.add(controlVocabularyTerm);
		}

		// DATA TRANSFORMATION
		final List<ControlVocabularyTerm> dataTransformationTerms = DataTransformation
				.getInstance(super.getCvManager()).getPossibleValues();
		for (ControlVocabularyTerm controlVocabularyTerm : dataTransformationTerms) {
			possibleValues.add(controlVocabularyTerm);
		}

		// EXPERIMENT TYPE
		final List<ControlVocabularyTerm> experimentTypes = ExperimentType
				.getInstance(super.getCvManager()).getPossibleValues();
		for (ControlVocabularyTerm controlVocabularyTerm : experimentTypes) {
			possibleValues.add(controlVocabularyTerm);
		}

		// CELL TYPE
		final List<ControlVocabularyTerm> cellTypes = CellTypes.getInstance(
				getCvManager()).getPossibleValues();
		for (ControlVocabularyTerm controlVocabularyTerm : cellTypes) {
			possibleValues.add(controlVocabularyTerm);
		}

		// tissues
		final List<ControlVocabularyTerm> tissues = TissuesTypes.getInstance(
				getCvManager()).getPossibleValues();
		for (ControlVocabularyTerm controlVocabularyTerm2 : tissues) {
			possibleValues.add(controlVocabularyTerm2);
		}

		firstGetPossibleValues = true;
		setCachedPossibleValues(possibleValues);
		return possibleValues;
	}
}
