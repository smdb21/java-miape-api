package org.proteored.miapeapi.xml.ms.merge;

import java.util.ArrayList;
import java.util.List;

import org.proteored.miapeapi.interfaces.ms.ResultingData;
import org.proteored.miapeapi.xml.merge.MiapeMerger;

public class MiapeMSResultingDataMerger implements
		MiapeMerger<List<ResultingData>> {
	private static MiapeMSResultingDataMerger instance;

	public static MiapeMSResultingDataMerger getInstance() {
		if (instance == null)
			instance = new MiapeMSResultingDataMerger();
		return instance;
	}

	@Override
	public List<ResultingData> merge(List<ResultingData> miapeDataOriginal,
			List<ResultingData> miapeDataMetadata) {
		if (miapeDataMetadata == null)
			return miapeDataOriginal;
		if (miapeDataOriginal == null)
			return miapeDataMetadata;

		List<ResultingData> miapeDataOriginalList = new ArrayList<ResultingData>();
		List<ResultingData> miapeDataMetadataList = new ArrayList<ResultingData>();
		for (ResultingData resultingData : miapeDataMetadata) {
			miapeDataMetadataList.add(resultingData);
		}
		for (ResultingData resultingData : miapeDataOriginal) {
			miapeDataOriginalList.add(resultingData);
		}
		List<ResultingData> ret = new ArrayList<ResultingData>();
		ret.addAll(miapeDataMetadata);
		ret.addAll(miapeDataOriginal);
		return ret;

		// int maxIndex = Math.max(miapeDataOriginalList.size(),
		// miapeDataMetadataList.size());
		// for (int i = 0; i < maxIndex; i++) {
		// ResultingData ResultingDataOriginal = null;
		// ResultingData ResultingDataMetadata = null;
		// if (i < miapeDataOriginalList.size())
		// ResultingDataOriginal = miapeDataOriginalList.get(i);
		// if (i < miapeDataMetadataList.size())
		// ResultingDataMetadata = miapeDataMetadataList.get(i);
		//
		// Object object;
		// String name = null;
		// object = MergerUtil.getNonNullValue(ResultingDataOriginal,
		// ResultingDataMetadata,
		// "getName");
		// if (object != null)
		// name = (String) object;
		//
		// String dataFileURI = null;
		// object = MergerUtil.getNonNullValue(ResultingDataOriginal,
		// ResultingDataMetadata,
		// "getDataFileUri");
		// if (object != null)
		// dataFileURI = (String) object;
		// String dataFileType = null;
		// object = MergerUtil.getNonNullValue(ResultingDataOriginal,
		// ResultingDataMetadata,
		// "getDataFileType");
		// if (object != null)
		// dataFileType = (String) object;
		// String additionalUri = null;
		// object = MergerUtil.getNonNullValue(ResultingDataOriginal,
		// ResultingDataMetadata,
		// "getAdditionalUri");
		// if (object != null)
		// additionalUri = (String) object;
		// String srmURI = null;
		// object = MergerUtil.getNonNullValue(ResultingDataOriginal,
		// ResultingDataMetadata,
		// "getSRMUri");
		// if (object != null)
		// srmURI = (String) object;
		// String srmType = null;
		// object = MergerUtil.getNonNullValue(ResultingDataOriginal,
		// ResultingDataMetadata,
		// "getSRMType");
		// if (object != null)
		// srmType = (String) object;
		// String srmDescriptor = null;
		// object = MergerUtil.getNonNullValue(ResultingDataOriginal,
		// ResultingDataMetadata,
		// "getSRMDescriptor");
		// if (object != null)
		// srmDescriptor = (String) object;
		//
		// final ResultingDataBuilder builder = MiapeMSDocumentFactory
		// .createResultingDataBuilder(name).dataFileURI(dataFileURI)
		// .dataFileType(dataFileType).additionalURI(additionalUri).srmURI(srmURI)
		// .srmType(srmType).srmDescriptor(srmDescriptor);
		// ret.add(builder.build());
		// }
		// return ret;
	}

}
