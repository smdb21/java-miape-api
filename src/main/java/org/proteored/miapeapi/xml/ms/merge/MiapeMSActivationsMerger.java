package org.proteored.miapeapi.xml.ms.merge;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.proteored.miapeapi.factories.ms.ActivationDissociationBuilder;
import org.proteored.miapeapi.factories.ms.MiapeMSDocumentFactory;
import org.proteored.miapeapi.interfaces.ms.ActivationDissociation;
import org.proteored.miapeapi.xml.merge.MergerUtil;
import org.proteored.miapeapi.xml.merge.MiapeMerger;

import gnu.trove.set.hash.THashSet;

public class MiapeMSActivationsMerger implements MiapeMerger<Set<ActivationDissociation>> {
	private static MiapeMSActivationsMerger instance;

	public static MiapeMSActivationsMerger getInstance() {
		if (instance == null)
			instance = new MiapeMSActivationsMerger();
		return instance;
	}

	@Override
	public Set<ActivationDissociation> merge(Set<ActivationDissociation> miapeDataOriginal,
			Set<ActivationDissociation> miapeDataMetadata) {
		if (miapeDataMetadata == null)
			return miapeDataOriginal;
		if (miapeDataOriginal == null)
			return miapeDataMetadata;

		List<ActivationDissociation> miapeDataOriginalList = new ArrayList<ActivationDissociation>();
		List<ActivationDissociation> miapeDataMetadataList = new ArrayList<ActivationDissociation>();
		for (ActivationDissociation activationDissociation : miapeDataMetadata) {
			miapeDataMetadataList.add(activationDissociation);
		}
		for (ActivationDissociation activationDissociation : miapeDataOriginal) {
			miapeDataOriginalList.add(activationDissociation);
		}
		Set<ActivationDissociation> ret = new THashSet<ActivationDissociation>();
		int maxIndex = Math.max(miapeDataMetadataList.size(), miapeDataOriginalList.size());
		for (int i = 0; i < maxIndex; i++) {
			ActivationDissociation activationDissociationOriginal = null;
			ActivationDissociation activationDissociationMetadata = null;
			if (i < miapeDataOriginalList.size())
				activationDissociationOriginal = miapeDataOriginalList.get(i);
			if (i < miapeDataMetadataList.size())
				activationDissociationMetadata = miapeDataMetadataList.get(i);

			Object object;
			String name = null;
			object = MergerUtil.getNonNullValue(activationDissociationOriginal, activationDissociationMetadata,
					"getName");
			if (object != null)
				name = (String) object;

			String activationType = null;
			object = MergerUtil.getNonNullValue(activationDissociationOriginal, activationDissociationMetadata,
					"getActivationType");
			if (object != null)
				activationType = (String) object;
			String gas = null;
			object = MergerUtil.getNonNullValue(activationDissociationOriginal, activationDissociationMetadata,
					"getGasType");
			if (object != null)
				gas = (String) object;
			String pressure = null;
			object = MergerUtil.getNonNullValue(activationDissociationOriginal, activationDissociationMetadata,
					"getGasPressure");
			if (object != null)
				pressure = (String) object;
			String pressureUnit = null;
			object = MergerUtil.getNonNullValue(activationDissociationOriginal, activationDissociationMetadata,
					"getPressureUnit");
			if (object != null)
				pressureUnit = (String) object;
			final ActivationDissociationBuilder builder = MiapeMSDocumentFactory
					.createActivationDissociationBuilder(name).activationType(activationType).gas(gas)
					.pressure(pressure).pressureUnit(pressureUnit);
			ret.add(builder.build());
		}
		return ret;
	}
}
