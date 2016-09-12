package org.proteored.miapeapi.xml.mzml;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ms.MassAnalyzerType;
import org.proteored.miapeapi.cv.ms.ReflectronState;
import org.proteored.miapeapi.interfaces.MatchMode;
import org.proteored.miapeapi.interfaces.ms.Analyser;
import org.proteored.miapeapi.xml.mzml.util.MzMLControlVocabularyXmlFactory;

import uk.ac.ebi.jmzml.model.mzml.AnalyzerComponent;
import uk.ac.ebi.jmzml.model.mzml.ParamGroup;
import uk.ac.ebi.jmzml.model.mzml.ReferenceableParamGroupList;

public class AnalyzerImpl implements Analyser {
	private String name = null;
	private String reflectronStatus = null;
	private final String[] NAME_TEXT_LIST = { "name" };

	public AnalyzerImpl(AnalyzerComponent analyzerComponent,
			ReferenceableParamGroupList referenceableParamGroupList,
			ControlVocabularyManager cvManager) {

		if (analyzerComponent != null) {
			// Create a paramGroup
			ParamGroup paramGroup = MzMLControlVocabularyXmlFactory.createParamGroup(
					analyzerComponent.getCvParam(), analyzerComponent.getUserParam(),
					analyzerComponent.getReferenceableParamGroupRef());

			// Search the name
			this.name = MzMLControlVocabularyXmlFactory.getValueFromParamGroup(paramGroup,
					referenceableParamGroupList, MassAnalyzerType.getInstance(cvManager));
			if (name == null) {
				this.name = MzMLControlVocabularyXmlFactory
						.getValueFromParamGroupByName(paramGroup, referenceableParamGroupList,
								NAME_TEXT_LIST, MatchMode.ANYWHERE);
			}
			if (name == null) {
				this.name = "Analyzer";
			}
			// Search the reflectron status
			this.reflectronStatus = MzMLControlVocabularyXmlFactory
					.getValueFromParamGroup(paramGroup, referenceableParamGroupList,
							ReflectronState.getInstance(cvManager));
		}
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getReflectron() {
		return this.reflectronStatus;
	}

	@Override
	public String getDescription() {
		// TODO
		return null;
	}

}
