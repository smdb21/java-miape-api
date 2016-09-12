package org.proteored.miapeapi.xml.msi.adapter;

import org.proteored.miapeapi.cv.ms.BackgroundName;
import org.proteored.miapeapi.cv.ms.SmoothingType;
import org.proteored.miapeapi.cv.msi.DataTransformation;
import org.proteored.miapeapi.cv.msi.ThresholdName;
import org.proteored.miapeapi.interfaces.Adapter;
import org.proteored.miapeapi.interfaces.Algorithm;
import org.proteored.miapeapi.xml.msi.autogenerated.MIAPEAlgorithmType;
import org.proteored.miapeapi.xml.msi.autogenerated.ObjectFactory;
import org.proteored.miapeapi.xml.msi.util.MSIControlVocabularyXmlFactory;

public class AlgorithmAdapter implements Adapter<MIAPEAlgorithmType> {
	private final Algorithm algorithm;
	private final ObjectFactory factory;
	private final MSIControlVocabularyXmlFactory cvFactory;

	public AlgorithmAdapter(Algorithm algorithm, ObjectFactory factory,
			MSIControlVocabularyXmlFactory cvFactory) {
		this.algorithm = algorithm;
		this.factory = factory;
		this.cvFactory = cvFactory;
	}

	@Override
	public MIAPEAlgorithmType adapt() {
		MIAPEAlgorithmType xmlAlgorithm = factory.createMIAPEAlgorithmType();
		xmlAlgorithm.setCatalogNumber(algorithm.getCatalogNumber());
		xmlAlgorithm.setComments(algorithm.getComments());
		xmlAlgorithm.setDescription(algorithm.getDescription());
		xmlAlgorithm.setManufacturer(algorithm.getManufacturer());
		xmlAlgorithm.setModel(algorithm.getModel());
		xmlAlgorithm.setName(cvFactory.createCV(algorithm.getName(), null,
				BackgroundName.getInstance(cvFactory.getCvManager()),
				SmoothingType.getInstance(cvFactory.getCvManager()),
				DataTransformation.getInstance(cvFactory.getCvManager()),
				ThresholdName.getInstance(cvFactory.getCvManager())));
		xmlAlgorithm.setVersion(algorithm.getVersion());
		xmlAlgorithm.setParameters(algorithm.getParameters());
		xmlAlgorithm.setURI(algorithm.getURI());
		return xmlAlgorithm;
	}

}
