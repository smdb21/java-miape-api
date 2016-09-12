package org.proteored.miapeapi.xml.xtandem.msi;

import java.util.Set;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularyTerm;
import org.proteored.miapeapi.cv.msi.ValidationType;
import org.proteored.miapeapi.interfaces.Software;
import org.proteored.miapeapi.interfaces.msi.PostProcessingMethod;
import org.proteored.miapeapi.interfaces.msi.Validation;

public class ValidationImpl implements Validation {
	private final long falsePositives;
	private final double maxValidExpectValue;
	private final ControlVocabularyManager cvManager;

	public ValidationImpl(long falsePositives2, double maxValidExpectValue,
			ControlVocabularyManager cvManager) {
		this.falsePositives = falsePositives2;
		this.maxValidExpectValue = maxValidExpectValue;
		this.cvManager = cvManager;
	}

	@Override
	public String getName() {
		return "Validation method";
	}

	@Override
	public String getStatisticalAnalysisResults() {
		StringBuilder sb = new StringBuilder();
		final ControlVocabularyTerm localFDRTerm = ValidationType.getLocalFDRTerm(cvManager);
		if (localFDRTerm != null) {
			sb.append(localFDRTerm.getPreferredName());
		} else {
			sb.append("Estimated false positives");
		}
		sb.append("=" + falsePositives);
		return sb.toString();
	}

	@Override
	public Set<PostProcessingMethod> getPostProcessingMethods() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Software> getPostProcessingSoftwares() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getGlobalThresholds() {
		return "output, maximum valid expectation value=" + maxValidExpectValue;
	}

}
