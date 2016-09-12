package org.proteored.miapeapi.xml.ms.merge;

import java.util.ArrayList;
import java.util.List;

import org.proteored.miapeapi.factories.ms.AnalyserBuilder;
import org.proteored.miapeapi.factories.ms.MiapeMSDocumentFactory;
import org.proteored.miapeapi.interfaces.ms.Analyser;
import org.proteored.miapeapi.xml.merge.MergerUtil;
import org.proteored.miapeapi.xml.merge.MiapeMerger;

public class MiapeMSAnalyzerMerger implements MiapeMerger<List<Analyser>> {
	private static MiapeMSAnalyzerMerger instance;

	public static MiapeMSAnalyzerMerger getInstance() {
		if (instance == null)
			instance = new MiapeMSAnalyzerMerger();
		return instance;
	}

	@Override
	public List<Analyser> merge(List<Analyser> miapeDataOriginal, List<Analyser> miapeDataMetadata) {
		if (miapeDataMetadata == null)
			return miapeDataOriginal;
		if (miapeDataOriginal == null)
			return miapeDataMetadata;

		List<Analyser> ret = new ArrayList<Analyser>();
		int maxIndex = Math.max(miapeDataMetadata.size(), miapeDataOriginal.size());
		for (int i = 0; i < maxIndex; i++) {
			Analyser AnalyserOriginal = null;
			Analyser AnalyserMetadata = null;
			if (i < miapeDataOriginal.size())
				AnalyserOriginal = miapeDataOriginal.get(i);
			if (i < miapeDataMetadata.size())
				AnalyserMetadata = miapeDataMetadata.get(i);

			Object object;
			String name = null;
			object = MergerUtil.getNonNullValue(AnalyserOriginal, AnalyserMetadata, "getName");
			if (object != null)
				name = (String) object;

			String description = null;
			object = MergerUtil.getNonNullValue(AnalyserOriginal, AnalyserMetadata,
					"getDescription");
			if (object != null)
				description = (String) object;

			String reflectron = null;
			object = MergerUtil
					.getNonNullValue(AnalyserOriginal, AnalyserMetadata, "getReflectron");
			if (object != null)
				reflectron = (String) object;
			final AnalyserBuilder builder = MiapeMSDocumentFactory.createAnalyserBuilder(name)
					.description(description).reflectron(reflectron);
			ret.add(builder.build());
		}
		return ret;
	}
}
