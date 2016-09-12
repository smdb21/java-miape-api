package org.proteored.miapeapi.xml.ms.merge;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.proteored.miapeapi.factories.ms.EsiBuilder;
import org.proteored.miapeapi.factories.ms.MiapeMSDocumentFactory;
import org.proteored.miapeapi.interfaces.Equipment;
import org.proteored.miapeapi.interfaces.ms.Esi;
import org.proteored.miapeapi.xml.merge.MergerUtil;
import org.proteored.miapeapi.xml.merge.MiapeMerger;

public class MiapeMSEsiMerger implements MiapeMerger<List<Esi>> {
	private static MiapeMSEsiMerger instance;

	public static MiapeMSEsiMerger getInstance() {
		if (instance == null)
			instance = new MiapeMSEsiMerger();
		return instance;
	}

	@Override
	public List<Esi> merge(List<Esi> miapeDataOriginal, List<Esi> miapeDataMetadata) {
		if (miapeDataMetadata == null)
			return miapeDataOriginal;
		if (miapeDataOriginal == null)
			return miapeDataMetadata;

		List<Esi> ret = new ArrayList<Esi>();
		int maxIndex = Math.max(miapeDataMetadata.size(), miapeDataOriginal.size());
		for (int i = 0; i < maxIndex; i++) {
			Esi esiOriginal = null;
			Esi esiMetadata = null;
			if (i < miapeDataOriginal.size())
				esiOriginal = miapeDataOriginal.get(i);
			if (i < miapeDataMetadata.size())
				esiMetadata = miapeDataMetadata.get(i);

			Object object;
			String name = null;
			object = MergerUtil.getNonNullValue(esiOriginal, esiMetadata, "getName");
			if (object != null)
				name = (String) object;

			String parameters = null;
			object = MergerUtil.getNonNullValue(esiOriginal, esiMetadata, "getParameters");
			if (object != null)
				parameters = (String) object;
			Set<Equipment> interfaces = null;
			object = MergerUtil.getNonNullValue(esiOriginal, esiMetadata, "getInterfaces");
			if (object != null)
				interfaces = (Set<Equipment>) object;
			Set<Equipment> sprayers = null;
			object = MergerUtil.getNonNullValue(esiOriginal, esiMetadata, "getSprayers");
			if (object != null)
				sprayers = (Set<Equipment>) object;
			final EsiBuilder builder = MiapeMSDocumentFactory.createEsiBuilder(name)
					.parameters(parameters).interfaces(interfaces).sprayers(sprayers);
			ret.add(builder.build());
		}
		return ret;
	}

}
