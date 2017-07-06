package org.proteored.miapeapi.xml.mzml;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.interfaces.ms.ActivationDissociation;
import org.proteored.miapeapi.interfaces.ms.Analyser;
import org.proteored.miapeapi.interfaces.ms.Esi;
import org.proteored.miapeapi.interfaces.ms.InstrumentConfiguration;
import org.proteored.miapeapi.interfaces.ms.Maldi;
import org.proteored.miapeapi.interfaces.ms.Other_IonSource;

import gnu.trove.set.hash.THashSet;
import uk.ac.ebi.jmzml.model.mzml.AnalyzerComponent;
import uk.ac.ebi.jmzml.model.mzml.ReferenceableParamGroupList;
import uk.ac.ebi.jmzml.model.mzml.SourceComponent;

public class InstrumentConfigurationImpl implements InstrumentConfiguration {
	private final uk.ac.ebi.jmzml.model.mzml.InstrumentConfiguration mzMLInstrumentConfiguration;
	private final ReferenceableParamGroupList referenceableParamGroupList;
	private List<Maldi> maldis = new ArrayList<Maldi>();
	private List<Esi> esis = new ArrayList<Esi>();
	private List<Other_IonSource> otherIonSources = new ArrayList<Other_IonSource>();
	private Set<ActivationDissociation> activationDissociation = new THashSet<ActivationDissociation>();
	// private Set<IonOptic> ionOptics = new THashSet<IonOptic>();
	private List<Analyser> analyzers = new ArrayList<Analyser>();
	private static Logger log = Logger.getLogger("log4j.logger.org.proteored");
	private final ControlVocabularyManager cvManager;

	public InstrumentConfigurationImpl(uk.ac.ebi.jmzml.model.mzml.InstrumentConfiguration instrumentConfiguration,
			ReferenceableParamGroupList referenceableParamGroupList, ControlVocabularyManager cvManager) {
		this.mzMLInstrumentConfiguration = instrumentConfiguration;
		this.referenceableParamGroupList = referenceableParamGroupList;
		this.cvManager = cvManager;
		processInstrumentConfiguration();

	}

	private void processInstrumentConfiguration() {
		// parse analyzers and sources
		if (mzMLInstrumentConfiguration.getComponentList() != null) {
			if (mzMLInstrumentConfiguration.getComponentList().getAnalyzer() != null) {
				processAnalyzers(mzMLInstrumentConfiguration.getComponentList().getAnalyzer(),
						referenceableParamGroupList);
			}
			if (mzMLInstrumentConfiguration.getComponentList().getSource() != null) {
				processSources(mzMLInstrumentConfiguration.getComponentList().getSource(), referenceableParamGroupList);
			}
		}

	}

	private void processSources(List<SourceComponent> source, ReferenceableParamGroupList referenceableParamGroupList) {
		log.info("processing sources");

		if (source != null) {
			for (SourceComponent sourceComponent : source) {
				Maldi maldi = new MaldiImpl(sourceComponent, referenceableParamGroupList, cvManager);
				if (maldi.getName() != null) {
					this.maldis.add(maldi);
				}
				Esi esi = new EsiImpl(sourceComponent, referenceableParamGroupList, cvManager);
				if (esi.getName() != null)
					this.esis.add(esi);

				// other ion source if it is not a maldi or an esi
				if (esi.getName() == null && maldi.getName() == null) {
					Other_IonSource otherIonSource = new OtherIonSourceImpl(sourceComponent,
							referenceableParamGroupList, cvManager);
					this.otherIonSources.add(otherIonSource);
				}
			}
		}
		log.info("processing sources");

	}

	private void processAnalyzers(List<AnalyzerComponent> analyzers,
			ReferenceableParamGroupList referenceableParamGroupList) {
		log.info("processing analyzers");

		if (analyzers != null) {
			for (AnalyzerComponent analyzerComponent : analyzers) {

				this.analyzers.add(new AnalyzerImpl(analyzerComponent, referenceableParamGroupList, cvManager));
			}
		}
		log.info("end processing analyzers");

	}

	@Override
	public String getName() {
		return mzMLInstrumentConfiguration.getId();
	}

	@Override
	public List<Maldi> getMaldis() {
		if (maldis != null && !maldis.isEmpty())
			return this.maldis;
		return null;
	}

	@Override
	public List<Esi> getEsis() {
		if (esis != null && !esis.isEmpty())
			return this.esis;
		return null;
	}

	@Override
	public List<Other_IonSource> getOther_IonSources() {
		if (this.otherIonSources != null && !this.otherIonSources.isEmpty())
			return this.otherIonSources;
		return null;
	}

	@Override
	public Set<ActivationDissociation> getActivationDissociations() {
		if (activationDissociation != null && !activationDissociation.isEmpty())
			return activationDissociation;
		return null;
	}

	// @Override
	// public Set<IonOptic> getIonOptics() {
	// if (this.ionOptics != null && !this.ionOptics.isEmpty())
	// return this.ionOptics;
	// return null;
	// }

	@Override
	public List<Analyser> getAnalyzers() {
		if (this.analyzers != null && !this.analyzers.isEmpty())
			return this.analyzers;
		return null;
	}

}
