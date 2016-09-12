package org.proteored.miapeapi.xml.ms.merge;

import java.util.ArrayList;
import java.util.List;

import org.proteored.miapeapi.factories.ms.AdditionalInformationBuilder;
import org.proteored.miapeapi.factories.ms.MiapeMSDocumentFactory;
import org.proteored.miapeapi.interfaces.ms.MSAdditionalInformation;
import org.proteored.miapeapi.xml.merge.MergerUtil;
import org.proteored.miapeapi.xml.merge.MiapeMerger;

public class MiapeMSAdditionalInformationsMerger implements
		MiapeMerger<List<MSAdditionalInformation>> {
	private static MiapeMSAdditionalInformationsMerger instance;

	public static MiapeMSAdditionalInformationsMerger getInstance() {
		if (instance == null)
			instance = new MiapeMSAdditionalInformationsMerger();
		return instance;
	}

	@Override
	public List<MSAdditionalInformation> merge(List<MSAdditionalInformation> miapeDataOriginal,
			List<MSAdditionalInformation> miapeDataMetadata) {
		if (miapeDataMetadata == null)
			return miapeDataOriginal;
		if (miapeDataOriginal == null)
			return miapeDataMetadata;

		List<MSAdditionalInformation> ret = new ArrayList<MSAdditionalInformation>();
		int maxIndex = Math.max(miapeDataMetadata.size(), miapeDataOriginal.size());
		for (int i = 0; i < maxIndex; i++) {
			MSAdditionalInformation MSAdditionalInformationOriginal = null;
			MSAdditionalInformation MSAdditionalInformationMetadata = null;
			if (i < miapeDataOriginal.size())
				MSAdditionalInformationOriginal = miapeDataOriginal.get(i);
			if (i < miapeDataMetadata.size())
				MSAdditionalInformationMetadata = miapeDataMetadata.get(i);

			Object object;
			String name = null;
			object = MergerUtil.getNonNullValue(MSAdditionalInformationOriginal,
					MSAdditionalInformationMetadata, "getName");
			if (object != null)
				name = (String) object;

			String value = null;
			object = MergerUtil.getNonNullValue(MSAdditionalInformationOriginal,
					MSAdditionalInformationMetadata, "getValue");
			if (object != null)
				value = (String) object;

			final AdditionalInformationBuilder builder = MiapeMSDocumentFactory
					.createAdditionalInformationBuilder(name).value(value);

			ret.add(builder.build());
		}
		return ret;
	}
}
