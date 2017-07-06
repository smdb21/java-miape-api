package org.proteored.miapeapi.xml.ms.merge;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.proteored.miapeapi.factories.ms.MiapeMSDocumentFactory;
import org.proteored.miapeapi.factories.ms.SpectrometerBuilder;
import org.proteored.miapeapi.interfaces.ms.Spectrometer;
import org.proteored.miapeapi.xml.merge.MergerUtil;
import org.proteored.miapeapi.xml.merge.MiapeMerger;

import gnu.trove.set.hash.THashSet;

public class MiapeMSSpectrometersMerger implements MiapeMerger<Set<Spectrometer>> {
	private static MiapeMSSpectrometersMerger instance;

	public static MiapeMSSpectrometersMerger getInstance() {
		if (instance == null)
			instance = new MiapeMSSpectrometersMerger();
		return instance;
	}

	@Override
	public Set<Spectrometer> merge(Set<Spectrometer> miapeDataOriginal, Set<Spectrometer> miapeDataMetadata) {
		if (miapeDataMetadata == null)
			return miapeDataOriginal;
		if (miapeDataOriginal == null)
			return miapeDataMetadata;

		List<Spectrometer> miapeDataOriginalList = new ArrayList<Spectrometer>();
		List<Spectrometer> miapeDataMetadataList = new ArrayList<Spectrometer>();
		for (Spectrometer activationDissociation : miapeDataMetadata) {
			miapeDataMetadataList.add(activationDissociation);
		}
		for (Spectrometer activationDissociation : miapeDataOriginal) {
			miapeDataOriginalList.add(activationDissociation);
		}
		Set<Spectrometer> ret = new THashSet<Spectrometer>();
		int maxIndex = Math.max(miapeDataOriginalList.size(), miapeDataMetadataList.size());
		for (int i = 0; i < maxIndex; i++) {
			Spectrometer spectrometerOriginal = null;
			Spectrometer spectrometerMetadata = null;
			if (i < miapeDataOriginalList.size())
				spectrometerOriginal = miapeDataOriginalList.get(i);
			if (i < miapeDataMetadataList.size())
				spectrometerMetadata = miapeDataMetadataList.get(i);

			Object object;
			String name = null;
			object = MergerUtil.getNonNullValue(spectrometerOriginal, spectrometerMetadata, "getName");
			if (object != null)
				name = (String) object;

			String parameters = null;
			object = MergerUtil.getNonNullValue(spectrometerOriginal, spectrometerMetadata, "getParameters");
			if (object != null)
				parameters = (String) object;
			String catalogNumber = null;
			object = MergerUtil.getNonNullValue(spectrometerOriginal, spectrometerMetadata, "getCatalogNumber");
			if (object != null)
				catalogNumber = (String) object;
			String comments = null;
			object = MergerUtil.getNonNullValue(spectrometerOriginal, spectrometerMetadata, "getComments");
			if (object != null)
				comments = (String) object;
			String description = null;
			object = MergerUtil.getNonNullValue(spectrometerOriginal, spectrometerMetadata, "getDescription");
			if (object != null)
				description = (String) object;
			String manufacturer = null;
			object = MergerUtil.getNonNullValue(spectrometerOriginal, spectrometerMetadata, "getManufacturer");
			if (object != null)
				manufacturer = (String) object;
			String model = null;
			object = MergerUtil.getNonNullValue(spectrometerOriginal, spectrometerMetadata, "getModel");
			if (object != null)
				model = (String) object;
			String uri = null;
			object = MergerUtil.getNonNullValue(spectrometerOriginal, spectrometerMetadata, "getUri");
			if (object != null)
				uri = (String) object;
			String customizations = null;
			object = MergerUtil.getNonNullValue(spectrometerOriginal, spectrometerMetadata, "getCustomizations");
			if (object != null)
				customizations = (String) object;
			String version = null;
			object = MergerUtil.getNonNullValue(spectrometerOriginal, spectrometerMetadata, "getVersion");
			if (object != null)
				version = (String) object;
			int id = -1;
			object = MergerUtil.getNonNullValue(spectrometerOriginal, spectrometerMetadata, "getId");
			if (object != null)
				id = (Integer) object;
			final SpectrometerBuilder builder = (SpectrometerBuilder) MiapeMSDocumentFactory
					.createSpectrometerBuilder(name).customizations(customizations).parameters(parameters)
					.version(version).catalogNumber(catalogNumber).comments(comments).description(description)
					.manufacturer(manufacturer).model(model).uri(uri).id(id);
			ret.add(builder.build());
		}
		return ret;
	}

}
