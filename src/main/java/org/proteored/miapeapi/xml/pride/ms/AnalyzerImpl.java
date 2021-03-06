package org.proteored.miapeapi.xml.pride.ms;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ms.MassAnalyzerType;
import org.proteored.miapeapi.cv.ms.ReflectronState;
import org.proteored.miapeapi.interfaces.ms.Analyser;
import org.proteored.miapeapi.xml.pride.autogenerated.CvParamType;
import org.proteored.miapeapi.xml.pride.autogenerated.ParamType;
import org.proteored.miapeapi.xml.pride.util.PrideControlVocabularyXmlFactory;

public class AnalyzerImpl implements Analyser {
	private String name = null;
	private String reflectron = null;

	public AnalyzerImpl(ParamType analizerPRIDE, ControlVocabularyManager cvManager) {
		CvParamType cvParam = PrideControlVocabularyXmlFactory.getCvFromParamType(analizerPRIDE,
				MassAnalyzerType.getInstance(cvManager));
		if (cvParam != null) {
			name = PrideControlVocabularyXmlFactory.writeParam(cvParam);
		} else {
			name = "Analyzer";
		}

		cvParam = PrideControlVocabularyXmlFactory.getCvFromParamType(analizerPRIDE,
				ReflectronState.getInstance(cvManager));
		if (cvParam != null) {
			reflectron = PrideControlVocabularyXmlFactory.writeParam(cvParam);
		}
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getReflectron() {
		return reflectron;
	}

	@Override
	public String getDescription() {
		return null;
	}

}
