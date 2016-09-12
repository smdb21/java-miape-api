package org.proteored.miapeapi.xml.pride.adapter;

import java.util.List;

import org.apache.log4j.Logger;
import org.proteored.miapeapi.interfaces.Adapter;
import org.proteored.miapeapi.interfaces.ms.ResultingData;
import org.proteored.miapeapi.xml.pride.autogenerated.ObjectFactory;
import org.proteored.miapeapi.xml.pride.autogenerated.SourceFileType;

public class SourceFileTypeAdapter implements Adapter<SourceFileType> {
	private static Logger log = Logger.getLogger("log4j.logger.org.proteored");

	private final ObjectFactory factory;
	private final List<ResultingData> resultingDatas;

	public SourceFileTypeAdapter(ObjectFactory factory, List<ResultingData> resultingDatas) {
		this.factory = factory;
		this.resultingDatas = resultingDatas;
	}

	@Override
	public SourceFileType adapt() {
		log.info("createSourceFile");
		SourceFileType sourceFileType = factory.createSourceFileType();
		String fileType = null;
		String fileName = "unknown";
		String filePath = "unknown";
		if (!resultingDatas.isEmpty()) {
			// Take the fisrt one
			ResultingData miapeResultingData = resultingDatas.iterator().next();
			fileType = miapeResultingData.getDataFileType();
			fileName = miapeResultingData.getName();
			if (miapeResultingData.getDataFileUri() != null
					&& !"".equals(miapeResultingData.getDataFileUri())) {
				filePath = miapeResultingData.getDataFileUri();
			} else if (miapeResultingData.getAdditionalUri() != null
					&& !"".equals(miapeResultingData.getAdditionalUri())) {
				filePath = miapeResultingData.getAdditionalUri();
			}
		}

		// mandatories
		sourceFileType.setNameOfFile(fileName);
		sourceFileType.setPathToFile(filePath);

		// not mandatories
		if (fileType != null && !fileType.equals(""))
			sourceFileType.setFileType(fileType);

		return sourceFileType;
	}

}
