package org.proteored.miapeapi.xml.ms.merge;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.proteored.miapeapi.factories.ms.AcquisitionBuilder;
import org.proteored.miapeapi.factories.ms.MiapeMSDocumentFactory;
import org.proteored.miapeapi.interfaces.ms.Acquisition;
import org.proteored.miapeapi.xml.merge.MergerUtil;
import org.proteored.miapeapi.xml.merge.MiapeMerger;

public class MiapeMSAcquisitionsMerger implements MiapeMerger<Set<Acquisition>> {
	private static MiapeMSAcquisitionsMerger instance;

	public static MiapeMSAcquisitionsMerger getInstance() {
		if (instance == null)
			instance = new MiapeMSAcquisitionsMerger();
		return instance;
	}

	@Override
	public Set<Acquisition> merge(Set<Acquisition> miapeDataOriginal,
			Set<Acquisition> miapeDataMetadata) {
		if (miapeDataMetadata == null)
			return miapeDataOriginal;
		if (miapeDataOriginal == null)
			return miapeDataMetadata;

		List<Acquisition> miapeDataOriginalList = new ArrayList<Acquisition>();
		List<Acquisition> miapeDataMetadataList = new ArrayList<Acquisition>();
		for (Acquisition activationDissociation : miapeDataMetadata) {
			miapeDataMetadataList.add(activationDissociation);
		}
		for (Acquisition activationDissociation : miapeDataOriginal) {
			miapeDataOriginalList.add(activationDissociation);
		}
		Set<Acquisition> ret = new HashSet<Acquisition>();
		int maxIndex = Math.max(miapeDataOriginalList.size(), miapeDataMetadataList.size());
		for (int i = 0; i < maxIndex; i++) {
			Acquisition acquisitionOriginal = null;
			Acquisition acquisitionMetadata = null;
			if (i < miapeDataOriginalList.size())
				acquisitionOriginal = miapeDataOriginalList.get(i);
			if (i < miapeDataMetadataList.size())
				acquisitionMetadata = miapeDataMetadataList.get(i);

			Object object;
			String name = null;
			object = MergerUtil
					.getNonNullValue(acquisitionOriginal, acquisitionMetadata, "getName");
			if (object != null)
				name = (String) object;

			String parameters = null;
			object = MergerUtil.getNonNullValue(acquisitionOriginal, acquisitionMetadata,
					"getParameters");
			if (object != null)
				parameters = (String) object;
			String catalogNumber = null;
			object = MergerUtil.getNonNullValue(acquisitionOriginal, acquisitionMetadata,
					"getCatalogNumber");
			if (object != null)
				catalogNumber = (String) object;
			String comments = null;
			object = MergerUtil.getNonNullValue(acquisitionOriginal, acquisitionMetadata,
					"getComments");
			if (object != null)
				comments = (String) object;
			String description = null;
			object = MergerUtil.getNonNullValue(acquisitionOriginal, acquisitionMetadata,
					"getDescription");
			if (object != null)
				description = (String) object;
			String manufacturer = null;
			object = MergerUtil.getNonNullValue(acquisitionOriginal, acquisitionMetadata,
					"getManufacturer");
			if (object != null)
				manufacturer = (String) object;
			String model = null;
			object = MergerUtil.getNonNullValue(acquisitionOriginal, acquisitionMetadata,
					"getModel");
			if (object != null)
				model = (String) object;
			String uri = null;
			object = MergerUtil.getNonNullValue(acquisitionOriginal, acquisitionMetadata, "getURI");
			if (object != null)
				uri = (String) object;
			String customizations = null;
			object = MergerUtil.getNonNullValue(acquisitionOriginal, acquisitionMetadata,
					"getCustomizations");
			if (object != null)
				customizations = (String) object;
			String version = null;
			object = MergerUtil.getNonNullValue(acquisitionOriginal, acquisitionMetadata,
					"getVersion");
			if (object != null)
				version = (String) object;

			int id = -1;
			object = MergerUtil.getNonNullValue(acquisitionOriginal, acquisitionMetadata, "getId");
			if (object != null)
				id = (Integer) object;
			String parametersFile = null;
			object = MergerUtil.getNonNullValue(acquisitionOriginal, acquisitionMetadata,
					"getParameterFile");
			if (object != null)
				parametersFile = (String) object;
			String targetList = null;
			object = MergerUtil.getNonNullValue(acquisitionOriginal, acquisitionMetadata,
					"getTargetList");
			if (object != null)
				targetList = (String) object;
			String transitionListFile = null;
			object = MergerUtil.getNonNullValue(acquisitionOriginal, acquisitionMetadata,
					"getTransitionListFile");
			if (object != null)
				transitionListFile = (String) object;
			final AcquisitionBuilder builder = (AcquisitionBuilder) MiapeMSDocumentFactory
					.createAcquisitionBuilder(name).parameterFile(parametersFile)
					.targetList(targetList).transitionListFile(transitionListFile).id(id)
					.customizations(customizations).parameters(parameters).version(version)
					.catalogNumber(catalogNumber).comments(comments).description(description)
					.manufacturer(manufacturer).model(model).uri(uri);
			ret.add(builder.build());
		}
		return ret;
	}

}
