package org.proteored.miapeapi.xml.ms.merge;

import java.util.ArrayList;
import java.util.List;

import org.proteored.miapeapi.factories.ms.MaldiBuilder;
import org.proteored.miapeapi.factories.ms.MiapeMSDocumentFactory;
import org.proteored.miapeapi.interfaces.ms.Maldi;
import org.proteored.miapeapi.xml.merge.MergerUtil;
import org.proteored.miapeapi.xml.merge.MiapeMerger;

public class MiapeMSMaldiMerger implements MiapeMerger<List<Maldi>> {
	private static MiapeMSMaldiMerger instance;

	public static MiapeMSMaldiMerger getInstance() {
		if (instance == null)
			instance = new MiapeMSMaldiMerger();
		return instance;
	}

	@Override
	public List<Maldi> merge(List<Maldi> miapeDataOriginal, List<Maldi> miapeDataMetadata) {
		if (miapeDataMetadata == null)
			return miapeDataOriginal;
		if (miapeDataOriginal == null)
			return miapeDataMetadata;

		List<Maldi> ret = new ArrayList<Maldi>();
		int maxIndex = Math.max(miapeDataMetadata.size(), miapeDataOriginal.size());
		for (int i = 0; i < maxIndex; i++) {
			Maldi maldiOriginal = null;
			Maldi maldiMetadata = null;
			if (i < miapeDataOriginal.size())
				maldiOriginal = miapeDataOriginal.get(i);
			if (i < miapeDataMetadata.size())
				maldiMetadata = miapeDataMetadata.get(i);

			Object object;
			String name = null;
			object = MergerUtil.getNonNullValue(maldiOriginal, maldiMetadata, "getName");
			if (object != null)
				name = (String) object;

			String parameters = null;
			object = MergerUtil.getNonNullValue(maldiOriginal, maldiMetadata, "getLaserParameters");
			if (object != null)
				parameters = (String) object;
			String dissociation = null;
			object = MergerUtil.getNonNullValue(maldiOriginal, maldiMetadata, "getDissociation");
			if (object != null)
				dissociation = (String) object;
			String summary = null;
			object = MergerUtil.getNonNullValue(maldiOriginal, maldiMetadata,
					"getDissociationSummary");
			if (object != null)
				summary = (String) object;
			String extraction = null;
			object = MergerUtil.getNonNullValue(maldiOriginal, maldiMetadata, "getExtraction");
			if (object != null)
				extraction = (String) object;
			String laser = null;
			object = MergerUtil.getNonNullValue(maldiOriginal, maldiMetadata, "getLaser");
			if (object != null)
				laser = (String) object;
			String laserWavelenght = null;
			object = MergerUtil.getNonNullValue(maldiOriginal, maldiMetadata, "getLaserWaveLength");
			if (object != null)
				laserWavelenght = (String) object;
			String plateType = null;
			object = MergerUtil.getNonNullValue(maldiOriginal, maldiMetadata, "getPlateType");
			if (object != null)
				plateType = (String) object;
			String matrix = null;
			object = MergerUtil.getNonNullValue(maldiOriginal, maldiMetadata, "getMatrix");
			if (object != null)
				matrix = (String) object;
			final MaldiBuilder builder = MiapeMSDocumentFactory.createMaldiBuilder(name)
					.laserParameters(parameters).dissociation(dissociation)
					.dissociationSummary(summary).extraction(extraction).laser(laser)
					.laserWaveLength(laserWavelenght).plateType(plateType).matrix(matrix);
			ret.add(builder.build());
		}
		return ret;
	}
}
