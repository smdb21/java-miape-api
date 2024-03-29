package org.proteored.miapeapi.xml.pride.adapter;

import org.apache.log4j.Logger;
import org.proteored.miapeapi.cv.Accession;
import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularyTerm;
import org.proteored.miapeapi.cv.ms.DataProcessingParameters;
import org.proteored.miapeapi.cv.ms.SoftwareManufacturer;
import org.proteored.miapeapi.cv.msi.DataTransformation;
import org.proteored.miapeapi.interfaces.Adapter;
import org.proteored.miapeapi.interfaces.ms.DataAnalysis;
import org.proteored.miapeapi.interfaces.ms.MiapeMSDocument;
import org.proteored.miapeapi.xml.pride.autogenerated.DataProcessingType;
import org.proteored.miapeapi.xml.pride.autogenerated.ObjectFactory;
import org.proteored.miapeapi.xml.pride.autogenerated.ParamType;
import org.proteored.miapeapi.xml.pride.util.PrideControlVocabularyXmlFactory;

public class DataProcessingAdapter implements Adapter<DataProcessingType> {
	private static Logger log = Logger.getLogger("log4j.logger.org.proteored");
	private final ObjectFactory factory;
	private final MiapeMSDocument miapeMS;
	private final PrideControlVocabularyXmlFactory prideCvUtil;
	private final ControlVocabularyManager cvManager;

	public DataProcessingAdapter(ObjectFactory factory, ControlVocabularyManager cvManager,
			MiapeMSDocument miapeMS) {
		this.factory = factory;
		this.miapeMS = miapeMS;
		this.prideCvUtil = new PrideControlVocabularyXmlFactory(factory, cvManager);
		this.cvManager = cvManager;
	}

	@Override
	public DataProcessingType adapt() {
		log.info("createDataProcessing");
		DataProcessingType prideDataProcessing = factory.createDataProcessingType();
		if (miapeMS != null && miapeMS.getDataAnalysis() != null
				&& !miapeMS.getDataAnalysis().isEmpty()) {
			DataAnalysis peakListGenerator = miapeMS.getDataAnalysis().iterator().next();
			// Mandatory
			prideDataProcessing.setSoftware(createDataProcessingSoftware(peakListGenerator));

			ParamType paramList = factory.createParamType();
			if (peakListGenerator.getCustomizations() != null
					&& !peakListGenerator.getCustomizations().equals(""))
				prideCvUtil.addUserParamToParamType(paramList,
						PrideControlVocabularyXmlFactory.CUSTOMIZATIONS,
						peakListGenerator.getCustomizations());
			if (peakListGenerator.getDescription() != null
					&& !peakListGenerator.getDescription().equals("")) {
				// data transformation
				Accession accession = cvManager.getControlVocabularyId(
						peakListGenerator.getDescription(),
						DataTransformation.getInstance(cvManager));
				if (accession != null) {
					prideCvUtil.addCvParamOrUserParamToParamType(paramList,
							peakListGenerator.getDescription(), null,
							DataTransformation.getInstance(cvManager));
				} else {
					prideCvUtil.addUserParamToParamType(paramList, "Software description",
							peakListGenerator.getDescription());
				}
			}
			final String manufacturer = peakListGenerator.getManufacturer();
			if (manufacturer != null && !manufacturer.equals("")) {
				Accession accession = cvManager.getControlVocabularyId(manufacturer,
						SoftwareManufacturer.getInstance(cvManager));
				if (accession != null) {
					prideCvUtil.addCvParamOrUserParamToParamType(paramList, manufacturer, null,
							SoftwareManufacturer.getInstance(cvManager));
				} else {
					final ControlVocabularyTerm softManufacturer = SoftwareManufacturer
							.getInstance(cvManager).getCVTermByAccession(
									new Accession(
											SoftwareManufacturer.SOFTWARE_MANUFACTURER_ACCESSION));
					if (softManufacturer != null)
						prideCvUtil.addCvParamOrUserParamToParamType(paramList,
								softManufacturer.getPreferredName(), manufacturer,
								SoftwareManufacturer.getInstance(cvManager));
					else
						prideCvUtil.addUserParamToParamType(paramList, "software vendor",
								manufacturer);
				}
			}
			if (peakListGenerator.getCatalogNumber() != null
					&& !peakListGenerator.getCatalogNumber().equals(""))
				prideCvUtil.addUserParamToParamType(paramList,
						PrideControlVocabularyXmlFactory.CATALOG_NUMBER,
						peakListGenerator.getCatalogNumber());
			if (peakListGenerator.getModel() != null && !peakListGenerator.getModel().equals(""))
				prideCvUtil.addUserParamToParamType(paramList,
						PrideControlVocabularyXmlFactory.MODEL, peakListGenerator.getModel());
			if (peakListGenerator.getParameters() != null
					&& !peakListGenerator.getParameters().equals("")) {
				Accession accession = cvManager.getControlVocabularyId(
						peakListGenerator.getParameters(),
						DataProcessingParameters.getInstance(cvManager));
				if (accession != null) {
					prideCvUtil.addCvParamOrUserParamToParamType(paramList,
							peakListGenerator.getParameters(), null,
							DataProcessingParameters.getInstance(cvManager));
				} else {
					ControlVocabularyTerm cvTerm = DataProcessingParameters
							.getInstance(cvManager)
							.getCVTermByAccession(
									new Accession(
											DataProcessingParameters.DATA_PROCESSING_PARAMETERS_ACCESSION));
					prideCvUtil.addCvParamOrUserParamToParamType(paramList,
							cvTerm.getPreferredName(), peakListGenerator.getParameters());
				}

			}
			if (peakListGenerator.getParametersLocation() != null
					&& !peakListGenerator.getParametersLocation().equals("")) {
				prideCvUtil.addUserParamToParamType(paramList,
						PrideControlVocabularyXmlFactory.PARAMETER_FILE,
						peakListGenerator.getParametersLocation());

			}
			if (peakListGenerator.getURI() != null && !peakListGenerator.getURI().equals(""))
				prideCvUtil.addUserParamToParamType(paramList,
						PrideControlVocabularyXmlFactory.URI, peakListGenerator.getURI());

			prideDataProcessing.setProcessingMethod(paramList);
		} else {
			prideDataProcessing.setSoftware(createDataProcessingSoftware(null));
		}

		return prideDataProcessing;

	}

	private org.proteored.miapeapi.xml.pride.autogenerated.DataProcessingType.Software createDataProcessingSoftware(
			DataAnalysis peakListGenerator) {
		org.proteored.miapeapi.xml.pride.autogenerated.DataProcessingType.Software prideSoftware = factory
				.createDataProcessingTypeSoftware();
		if (peakListGenerator == null) {
			prideSoftware.setName("");
			prideSoftware.setVersion("");
			return prideSoftware;
		}
		// mandatory
		String name = peakListGenerator.getName();
		if (name != null) {
			prideSoftware.setName(name);
		} else {
			prideSoftware.setName("");
		}
		String version = peakListGenerator.getVersion();
		if (version != null) {
			prideSoftware.setVersion(version);
		} else {
			prideSoftware.setVersion("");
		}

		// not mandatory
		String comments = peakListGenerator.getComments();
		if (comments != null && !comments.equals(""))
			prideSoftware.setComments(comments);

		return prideSoftware;
	}

}
