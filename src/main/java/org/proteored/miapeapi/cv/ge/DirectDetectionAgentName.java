package org.proteored.miapeapi.cv.ge;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularySet;

public class DirectDetectionAgentName extends ControlVocabularySet {
	private static DirectDetectionAgentName instance;

	/**
	 * BIOLOGICAL_STAIN_PRODUCT("sep:00136", "biological stain product"),
	 * SIMPLYBLUE_SAFESTAIN("sep:00137", "SimplyBlue SafeStain"),
	 * SYPRO_RUBY("sep:00188", "SYPRO Ruby"),
	 * SILVERQUEST_SILVER_STAIN("sep:00138", "SilverQuest Silver Stain"),
	 * CHEMICAL_SUBSTANCE("sep:00104", "chemical substance"), CY5("sep:00185",
	 * "Cy5"), CY3("sep:00184", "Cy3"), CY2("sep:00183", "Cy2"),
	 * BISACRYLAMIDE("sep:00190", "bisacrylamide"), COOMASSIE_BLUE("sep:00114",
	 * "Coomassie blue");
	 */
	public static DirectDetectionAgentName getInstance(ControlVocabularyManager cvManager) {
		if (instance == null)
			instance = new DirectDetectionAgentName(cvManager);
		return instance;
	}

	private DirectDetectionAgentName(ControlVocabularyManager cvManager) {
		super(cvManager);
		String[] parentAccessionsTMP = { "sep:00136", "sep:00104", "sep:00103" };
		String[] explicitAccessionsTMP = {};
		this.parentAccessions = parentAccessionsTMP;
		this.explicitAccessions = explicitAccessionsTMP;
		this.miapeSection = 36;

	}

}
