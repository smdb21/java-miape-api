package org.proteored.miapeapi.xml.pride.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.interfaces.ms.Analyser;
import org.proteored.miapeapi.interfaces.ms.Esi;
import org.proteored.miapeapi.interfaces.ms.Maldi;
import org.proteored.miapeapi.interfaces.ms.Spectrometer;
import org.proteored.miapeapi.xml.pride.autogenerated.CvParamType;
import org.proteored.miapeapi.xml.pride.autogenerated.InstrumentDescriptionType;
import org.proteored.miapeapi.xml.pride.autogenerated.ParamType;
import org.proteored.miapeapi.xml.pride.ms.AnalyzerImpl;
import org.proteored.miapeapi.xml.pride.ms.EsiImpl;
import org.proteored.miapeapi.xml.pride.ms.MaldiImpl;
import org.proteored.miapeapi.xml.pride.ms.SpectrometerImpl;
import org.proteored.miapeapi.xml.util.MiapeXmlUtil;

import gnu.trove.map.hash.THashMap;

public class InstrumentManager {

	private final List<Maldi> maldis = new ArrayList<Maldi>();
	private final List<Esi> esis = new ArrayList<Esi>();
	private final List<Analyser> analyzers = new ArrayList<Analyser>();
	private final Spectrometer spectrometer;
	private final Map<String, CvParamType> sourceCVParams;
	private final ControlVocabularyManager cvManager;

	public InstrumentManager(InstrumentDescriptionType instrument, ControlVocabularyManager cvManager) {
		this.sourceCVParams = initSourceParams(instrument);
		this.cvManager = cvManager;
		createInstrument(instrument);
		createAnalyzers(instrument);
		this.spectrometer = createSpectrometer(instrument);

	}

	public InstrumentManager(ControlVocabularyManager cvManager) {
		this.spectrometer = null;
		this.sourceCVParams = null;
		this.cvManager = cvManager;
	}

	private Spectrometer createSpectrometer(InstrumentDescriptionType instrument) {
		Integer idenfifier = MiapeXmlUtil.EquipmentCounter.increaseCounter();
		return new SpectrometerImpl(instrument, idenfifier);
	}

	private void createInstrument(InstrumentDescriptionType instrument) {
		if (instrument.getSource() != null) {
			if (Utils.isMaldi(instrument.getSource().getCvParamOrUserParam(), cvManager)) {
				Maldi maldi = new MaldiImpl(instrument, sourceCVParams, cvManager);
				maldis.add(maldi);
			} else {
				Esi esi = new EsiImpl(instrument, sourceCVParams, cvManager);
				esis.add(esi);
			}
		}
	}

	private void createAnalyzers(InstrumentDescriptionType instrument) {
		if (instrument.getAnalyzerList() != null && instrument.getAnalyzerList().getAnalyzer() != null) {
			for (ParamType analizerPRIDE : instrument.getAnalyzerList().getAnalyzer()) {
				analyzers.add(new AnalyzerImpl(analizerPRIDE, cvManager));
			}
		}
	}

	private Map<String, CvParamType> initSourceParams(InstrumentDescriptionType instrument) {
		Map<String, CvParamType> result = new THashMap<String, CvParamType>();
		ParamType source = instrument.getSource();

		if (source != null && source.getCvParamOrUserParam() != null) {
			Utils.initParams(source.getCvParamOrUserParam(), result);
		}
		return result;
	}

	public List<Maldi> getMaldis() {
		if (!maldis.isEmpty())
			return maldis;
		return null;
	}

	public List<Esi> getEsis() {
		if (!esis.isEmpty())
			return esis;
		return null;
	}

	public List<Analyser> getAnalyzers() {
		if (!analyzers.isEmpty())
			return analyzers;
		return null;
	}

	public Spectrometer getSpectrometer() {
		return spectrometer;
	}

}
