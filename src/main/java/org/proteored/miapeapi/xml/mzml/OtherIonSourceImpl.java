package org.proteored.miapeapi.xml.mzml;

import java.util.HashMap;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ms.IonSourceName;
import org.proteored.miapeapi.interfaces.MatchMode;
import org.proteored.miapeapi.interfaces.ms.Other_IonSource;
import org.proteored.miapeapi.xml.mzml.util.MzMLControlVocabularyXmlFactory;

import uk.ac.ebi.jmzml.model.mzml.ParamGroup;
import uk.ac.ebi.jmzml.model.mzml.ReferenceableParamGroupList;
import uk.ac.ebi.jmzml.model.mzml.SourceComponent;

public class OtherIonSourceImpl implements Other_IonSource {
	private final String[] NAME_TEXT_LIST = { "name" };

	private String name = null;
	private String parameters = null;

	public OtherIonSourceImpl(SourceComponent sourceComponent,
			ReferenceableParamGroupList referenceableParamGroupList,
			ControlVocabularyManager cvManager) {
		HashMap<String, String> dicc = new HashMap<String, String>();

		if (sourceComponent != null) {
			// Create a paramGroup
			ParamGroup paramGroup = MzMLControlVocabularyXmlFactory.createParamGroup(
					sourceComponent.getCvParam(), sourceComponent.getUserParam(),
					sourceComponent.getReferenceableParamGroupRef());

			// name
			this.name = MzMLControlVocabularyXmlFactory.getValueFromParamGroup(paramGroup,
					referenceableParamGroupList, IonSourceName.getInstance(cvManager));
			if (name == null)
				this.name = MzMLControlVocabularyXmlFactory
						.getValueFromParamGroupByName(paramGroup, referenceableParamGroupList,
								NAME_TEXT_LIST, MatchMode.ANYWHERE);
			if (this.name == null)
				this.name = "Other Ion source";
			else
				dicc.put(name, name);

			this.parameters = MzMLControlVocabularyXmlFactory.parseAllParams(paramGroup,
					referenceableParamGroupList, dicc);
		}
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getParameters() {
		return this.parameters;
	}

}
