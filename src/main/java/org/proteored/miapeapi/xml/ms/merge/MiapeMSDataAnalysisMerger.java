package org.proteored.miapeapi.xml.ms.merge;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.proteored.miapeapi.factories.ms.DataAnalysisBuilder;
import org.proteored.miapeapi.factories.ms.MiapeMSDocumentFactory;
import org.proteored.miapeapi.interfaces.ms.DataAnalysis;
import org.proteored.miapeapi.xml.merge.MergerUtil;
import org.proteored.miapeapi.xml.merge.MiapeMerger;

import gnu.trove.set.hash.THashSet;

public class MiapeMSDataAnalysisMerger implements MiapeMerger<Set<DataAnalysis>> {
	private static MiapeMSDataAnalysisMerger instance;

	public static MiapeMSDataAnalysisMerger getInstance() {
		if (instance == null)
			instance = new MiapeMSDataAnalysisMerger();
		return instance;
	}

	@Override
	public Set<DataAnalysis> merge(Set<DataAnalysis> miapeDataOriginal, Set<DataAnalysis> miapeDataMetadata) {
		if (miapeDataMetadata == null)
			return miapeDataOriginal;
		if (miapeDataOriginal == null)
			return miapeDataMetadata;

		List<DataAnalysis> miapeDataOriginalList = new ArrayList<DataAnalysis>();
		List<DataAnalysis> miapeDataMetadataList = new ArrayList<DataAnalysis>();
		for (DataAnalysis activationDissociation : miapeDataMetadata) {
			miapeDataMetadataList.add(activationDissociation);
		}
		for (DataAnalysis activationDissociation : miapeDataOriginal) {
			miapeDataOriginalList.add(activationDissociation);
		}
		Set<DataAnalysis> ret = new THashSet<DataAnalysis>();
		int maxIndex = Math.max(miapeDataOriginalList.size(), miapeDataMetadataList.size());
		for (int i = 0; i < maxIndex; i++) {
			DataAnalysis dataAnalysisOriginal = null;
			DataAnalysis dataAnalysisMetadata = null;
			if (i < miapeDataOriginalList.size())
				dataAnalysisOriginal = miapeDataOriginalList.get(i);
			if (i < miapeDataMetadataList.size())
				dataAnalysisMetadata = miapeDataMetadataList.get(i);

			Object object;
			String name = null;
			object = MergerUtil.getNonNullValue(dataAnalysisOriginal, dataAnalysisMetadata, "getName");
			if (object != null)
				name = (String) object;

			String parameters = null;
			object = MergerUtil.getNonNullValue(dataAnalysisOriginal, dataAnalysisMetadata, "getParameters");
			if (object != null)
				parameters = (String) object;
			String catalogNumber = null;
			object = MergerUtil.getNonNullValue(dataAnalysisOriginal, dataAnalysisMetadata, "getCatalogNumber");
			if (object != null)
				catalogNumber = (String) object;
			String comments = null;
			object = MergerUtil.getNonNullValue(dataAnalysisOriginal, dataAnalysisMetadata, "getComments");
			if (object != null)
				comments = (String) object;
			String description = null;
			object = MergerUtil.getNonNullValue(dataAnalysisOriginal, dataAnalysisMetadata, "getDescription");
			if (object != null)
				description = (String) object;
			String manufacturer = null;
			object = MergerUtil.getNonNullValue(dataAnalysisOriginal, dataAnalysisMetadata, "getManufacturer");
			if (object != null)
				manufacturer = (String) object;
			String model = null;
			object = MergerUtil.getNonNullValue(dataAnalysisOriginal, dataAnalysisMetadata, "getModel");
			if (object != null)
				model = (String) object;
			String uri = null;
			object = MergerUtil.getNonNullValue(dataAnalysisOriginal, dataAnalysisMetadata, "getURI");
			if (object != null)
				uri = (String) object;
			String customizations = null;
			object = MergerUtil.getNonNullValue(dataAnalysisOriginal, dataAnalysisMetadata, "getCustomizations");
			if (object != null)
				customizations = (String) object;
			String version = null;
			object = MergerUtil.getNonNullValue(dataAnalysisOriginal, dataAnalysisMetadata, "getVersion");
			if (object != null)
				version = (String) object;
			String parametersLocation = null;
			object = MergerUtil.getNonNullValue(dataAnalysisOriginal, dataAnalysisMetadata, "getParametersLocation");
			if (object != null)
				parametersLocation = (String) object;
			int id = -1;
			object = MergerUtil.getNonNullValue(dataAnalysisOriginal, dataAnalysisMetadata, "getId");
			if (object != null)
				id = (Integer) object;
			final DataAnalysisBuilder builder = (DataAnalysisBuilder) MiapeMSDocumentFactory
					.createDataAnalysisBuilder(name).parametersLocation(parametersLocation).id(id)
					.customizations(customizations).parameters(parameters).version(version).catalogNumber(catalogNumber)
					.comments(comments).description(description).manufacturer(manufacturer).model(model).uri(uri);
			ret.add(builder.build());
		}
		return ret;
	}

}
