package org.proteored.miapeapi.xml.pride.adapter;

import java.util.List;
import java.util.Set;

import org.proteored.miapeapi.cv.Accession;
import org.proteored.miapeapi.cv.AdditionalInformationName;
import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularyTerm;
import org.proteored.miapeapi.cv.PRIDEOntology;
import org.proteored.miapeapi.cv.ms.MSFileType;
import org.proteored.miapeapi.interfaces.Adapter;
import org.proteored.miapeapi.interfaces.Project;
import org.proteored.miapeapi.interfaces.ms.MiapeMSDocument;
import org.proteored.miapeapi.interfaces.ms.ResultingData;
import org.proteored.miapeapi.interfaces.msi.MiapeMSIDocument;
import org.proteored.miapeapi.xml.pride.autogenerated.CvParamType;
import org.proteored.miapeapi.xml.pride.autogenerated.ExperimentType;
import org.proteored.miapeapi.xml.pride.autogenerated.ObjectFactory;
import org.proteored.miapeapi.xml.pride.autogenerated.ParamType;
import org.proteored.miapeapi.xml.pride.util.PrideControlVocabularyXmlFactory;

import gnu.trove.set.hash.THashSet;

public class AdditionalAdapter implements Adapter<ParamType> {
	private final ObjectFactory factory;
	private final ControlVocabularyManager cvManager;
	private final MiapeMSDocument miapeMS;
	private final PrideControlVocabularyXmlFactory prideCvUtil;
	private final MiapeMSIDocument miapeMSI;
	private final ExperimentType xmlExperiment;
	private final boolean gelBasedExperiment;

	public AdditionalAdapter(ObjectFactory factory, ControlVocabularyManager cvManager, MiapeMSDocument miapeMS,
			MiapeMSIDocument miapeMSI, ExperimentType xmlExperiment, boolean gelBasedExperiment) {
		this.factory = factory;
		this.cvManager = cvManager;
		this.miapeMS = miapeMS;
		this.miapeMSI = miapeMSI;
		this.prideCvUtil = new PrideControlVocabularyXmlFactory(factory, cvManager);
		this.xmlExperiment = xmlExperiment;
		this.gelBasedExperiment = gelBasedExperiment;
	}

	@Override
	public ParamType adapt() {
		String title = "PRIDE created automatically from MIAPE information";
		// String title = "Chr16-HPP.Pilot experiment.JPR HPP special issue";
		String shortLabel = "PRIDE created automatically from MIAPE information";
		ParamType additional = factory.createParamType();

		if (miapeMSI != null) {
			final Project project = miapeMSI.getProject();
			if (project != null)
				title = project.getName();
			shortLabel = miapeMSI.getName();
		} else if (miapeMS != null) {
			final Project project = miapeMS.getProject();
			if (project != null)
				title = project.getName();
			shortLabel = miapeMS.getName();
		}
		xmlExperiment.setShortLabel(shortLabel);
		xmlExperiment.setTitle(title);

		// Project name
		String projectName = "Project (replace this string by a description of your project)";
		// project name
		if (this.miapeMS != null && this.miapeMS.getProject() != null) {
			projectName = this.miapeMS.getProject().getName();
		} else if (this.miapeMSI != null && this.miapeMSI.getProject() != null) {
			projectName = this.miapeMSI.getProject().getName();
		}
		final ControlVocabularyTerm projectTerm = AdditionalInformationName.getInstance(cvManager)
				.getCVTermByAccession(AdditionalInformationName.PROJECT);
		CvParamType cvParamProject = prideCvUtil.createCvParam(projectTerm.getPreferredName(), projectName,
				AdditionalInformationName.getInstance(cvManager));
		if (cvParamProject != null) {
			additional.getCvParamOrUserParam().add(cvParamProject);
		}

		// Gel based experiment
		if (this.gelBasedExperiment) {
			final ControlVocabularyTerm gelBasedExperimentTerm = AdditionalInformationName.getInstance(cvManager)
					.getCVTermByAccession(AdditionalInformationName.GEL_BASED_EXPERIMENT);
			CvParamType cvParamGelBasedExperiment = prideCvUtil.createCvParam(gelBasedExperimentTerm.getPreferredName(),
					null, AdditionalInformationName.getInstance(cvManager));
			if (cvParamGelBasedExperiment != null) {
				additional.getCvParamOrUserParam().add(cvParamGelBasedExperiment);
			}
		}

		// Original MS data file format
		if (miapeMS != null) {
			final List<ResultingData> resultingDatas = miapeMS.getResultingDatas();
			if (miapeMS.getResultingDatas() != null) {
				Set<String> fileTypes = new THashSet<String>();
				for (ResultingData resultingData : resultingDatas) {
					final String dataFileType = resultingData.getDataFileType();
					if (dataFileType != null && !fileTypes.contains(dataFileType)) {
						final ControlVocabularyTerm mzMLCV = MSFileType.getInstance(cvManager)
								.getCVTermByAccession(MSFileType.MZML_ACC);
						String mzMLName = dataFileType;
						if (mzMLCV != null) {
							mzMLName = mzMLCV.getPreferredName();
						}
						// not add if it is a mzML
						if (!dataFileType.equals(mzMLName))
							fileTypes.add(dataFileType);
					}
				}
				if (!fileTypes.isEmpty()) {
					String fileFormat = "";
					for (String string : fileTypes) {
						if (!"".equals(fileFormat))
							fileFormat = fileFormat + ", ";
						fileFormat = fileFormat + string;
					}
					final CvParamType cvParam = prideCvUtil.createCvParam(new Accession("PRIDE:0000218"),
							"Original MS data file format", fileFormat, PRIDEOntology.getCVLabel());
					additional.getCvParamOrUserParam().add(cvParam);
				}
			}

		}
		if (this.miapeMS != null) {
			// miape ms
			prideCvUtil.addUserParamToParamType(additional, "MIAPE Mass Spectrometry (ID:" + this.miapeMS.getId() + ")",
					this.miapeMS.getName());
		}
		if (this.miapeMSI != null) {
			// miape msi
			prideCvUtil.addUserParamToParamType(additional,
					"MIAPE Mass Spectrometry Informatics (ID:" + this.miapeMSI.getId() + ")", this.miapeMSI.getName());
		}
		if (additional != null && additional.getCvParamOrUserParam() != null
				&& !additional.getCvParamOrUserParam().isEmpty())
			return additional;

		return null;
	}

}
