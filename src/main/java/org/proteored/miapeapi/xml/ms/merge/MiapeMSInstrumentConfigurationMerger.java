package org.proteored.miapeapi.xml.ms.merge;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.proteored.miapeapi.factories.ms.InstrumentConfigurationBuilder;
import org.proteored.miapeapi.factories.ms.MiapeMSDocumentFactory;
import org.proteored.miapeapi.interfaces.ms.ActivationDissociation;
import org.proteored.miapeapi.interfaces.ms.Analyser;
import org.proteored.miapeapi.interfaces.ms.Esi;
import org.proteored.miapeapi.interfaces.ms.InstrumentConfiguration;
import org.proteored.miapeapi.interfaces.ms.Maldi;
import org.proteored.miapeapi.interfaces.ms.Other_IonSource;
import org.proteored.miapeapi.xml.merge.MergerUtil;
import org.proteored.miapeapi.xml.merge.MiapeMerger;

public class MiapeMSInstrumentConfigurationMerger implements
		MiapeMerger<List<InstrumentConfiguration>> {
	private static MiapeMSInstrumentConfigurationMerger instance;

	public static MiapeMSInstrumentConfigurationMerger getInstance() {
		if (instance == null)
			instance = new MiapeMSInstrumentConfigurationMerger();
		return instance;
	}

	@Override
	public List<InstrumentConfiguration> merge(List<InstrumentConfiguration> miapeDataOriginal,
			List<InstrumentConfiguration> miapeDataMetadata) {
		if (miapeDataMetadata == null)
			return miapeDataOriginal;
		if (miapeDataOriginal == null)
			return miapeDataMetadata;
		Object object;
		List<InstrumentConfiguration> ret = new ArrayList<InstrumentConfiguration>();
		int maxIndex = Math.max(miapeDataMetadata.size(), miapeDataOriginal.size());
		for (int i = 0; i < maxIndex; i++) {
			InstrumentConfiguration instrumentConfigurationOriginal = null;
			InstrumentConfiguration instrumentConfigurationMetadata = null;
			if (i < miapeDataOriginal.size())
				instrumentConfigurationOriginal = miapeDataOriginal.get(i);
			if (i < miapeDataMetadata.size())
				instrumentConfigurationMetadata = miapeDataMetadata.get(i);
			String name = null;
			object = MergerUtil.getNonNullValue(instrumentConfigurationOriginal,
					instrumentConfigurationMetadata, "getName");
			if (object != null)
				name = (String) object;

			List<Esi> originalEsis = null;
			if (instrumentConfigurationOriginal != null)
				originalEsis = instrumentConfigurationOriginal.getEsis();
			List<Esi> metadataEsis = null;
			if (instrumentConfigurationMetadata != null)
				metadataEsis = instrumentConfigurationMetadata.getEsis();
			List<Esi> esis = MiapeMSEsiMerger.getInstance().merge(originalEsis, metadataEsis);

			List<Maldi> originalMaldis = null;
			if (instrumentConfigurationOriginal != null)
				originalMaldis = instrumentConfigurationOriginal.getMaldis();
			List<Maldi> metadataMaldis = null;
			if (instrumentConfigurationMetadata != null)
				metadataMaldis = instrumentConfigurationMetadata.getMaldis();
			List<Maldi> maldis = MiapeMSMaldiMerger.getInstance().merge(originalMaldis,
					metadataMaldis);

			List<Analyser> originalAnalyser = null;
			if (instrumentConfigurationOriginal != null)
				originalAnalyser = instrumentConfigurationOriginal.getAnalyzers();
			List<Analyser> metadataAnalyzers = null;
			if (instrumentConfigurationMetadata != null)
				metadataAnalyzers = instrumentConfigurationMetadata.getAnalyzers();
			List<Analyser> analyzers = MiapeMSAnalyzerMerger.getInstance().merge(originalAnalyser,
					metadataAnalyzers);

			Set<ActivationDissociation> activationOriginal = null;
			if (instrumentConfigurationOriginal != null)
				activationOriginal = instrumentConfigurationOriginal.getActivationDissociations();
			Set<ActivationDissociation> activationMetadata = null;
			if (instrumentConfigurationMetadata != null)
				activationMetadata = instrumentConfigurationMetadata.getActivationDissociations();
			Set<ActivationDissociation> activations = MiapeMSActivationsMerger.getInstance().merge(
					activationOriginal, activationMetadata);

			List<Other_IonSource> other_IonSourcesOriginal = null;
			if (instrumentConfigurationOriginal != null)
				other_IonSourcesOriginal = instrumentConfigurationOriginal.getOther_IonSources();
			List<Other_IonSource> other_IonSourcesMetadata = null;
			if (instrumentConfigurationMetadata != null)
				other_IonSourcesMetadata = instrumentConfigurationMetadata.getOther_IonSources();
			List<Other_IonSource> otherIonSources = MiapeMSOtherIonSourcesMerger.getInstance()
					.merge(other_IonSourcesOriginal, other_IonSourcesMetadata);

			final InstrumentConfigurationBuilder builder = MiapeMSDocumentFactory
					.createInstrumentConfigurationBuilder(name).esis(esis).maldis(maldis)
					.analysers(analyzers).activationDissociations(activations)
					.otherIonSources(otherIonSources);

			ret.add(builder.build());
		}
		return ret;

	}
}
