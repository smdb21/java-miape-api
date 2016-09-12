package org.proteored.miapeapi.xml.ms.merge;

import java.util.ArrayList;
import java.util.List;

import org.proteored.miapeapi.factories.ms.MiapeMSDocumentFactory;
import org.proteored.miapeapi.factories.ms.Other_IonSourceBuilder;
import org.proteored.miapeapi.interfaces.ms.Other_IonSource;
import org.proteored.miapeapi.xml.merge.MergerUtil;
import org.proteored.miapeapi.xml.merge.MiapeMerger;

public class MiapeMSOtherIonSourcesMerger implements MiapeMerger<List<Other_IonSource>> {
	private static MiapeMSOtherIonSourcesMerger instance;

	public static MiapeMSOtherIonSourcesMerger getInstance() {
		if (instance == null)
			instance = new MiapeMSOtherIonSourcesMerger();
		return instance;
	}

	@Override
	public List<Other_IonSource> merge(List<Other_IonSource> miapeDataOriginal,
			List<Other_IonSource> miapeDataMetadata) {
		if (miapeDataMetadata == null)
			return miapeDataOriginal;
		if (miapeDataOriginal == null)
			return miapeDataMetadata;

		List<Other_IonSource> ret = new ArrayList<Other_IonSource>();
		int maxIndex = Math.max(miapeDataMetadata.size(), miapeDataOriginal.size());
		for (int i = 0; i < maxIndex; i++) {
			Other_IonSource Other_IonSourceOriginal = null;
			Other_IonSource Other_IonSourceMetadata = null;
			if (i < miapeDataOriginal.size())
				Other_IonSourceOriginal = miapeDataOriginal.get(i);
			if (i < miapeDataMetadata.size())
				Other_IonSourceMetadata = miapeDataMetadata.get(i);

			Object object;
			String name = null;
			object = MergerUtil.getNonNullValue(Other_IonSourceOriginal, Other_IonSourceMetadata,
					"getName");
			if (object != null)
				name = (String) object;

			String parameters = null;
			object = MergerUtil.getNonNullValue(Other_IonSourceOriginal, Other_IonSourceMetadata,
					"getParameters");
			if (object != null)
				parameters = (String) object;

			final Other_IonSourceBuilder builder = MiapeMSDocumentFactory
					.createOther_IonSourceBuilder(name).parameters(parameters);
			ret.add(builder.build());
		}
		return ret;
	}
}
