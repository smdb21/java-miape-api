package org.proteored.miapeapi.xml.pride.adapter.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ms.MSFileType;
import org.proteored.miapeapi.cv.ms.SoftwareName;
import org.proteored.miapeapi.experiment.model.Experiment;
import org.proteored.miapeapi.experiment.model.Replicate;
import org.proteored.miapeapi.interfaces.Software;
import org.proteored.miapeapi.interfaces.ms.MiapeMSDocument;
import org.proteored.miapeapi.interfaces.ms.ResultingData;
import org.proteored.miapeapi.interfaces.msi.MiapeMSIDocument;
import org.proteored.miapeapi.util.URLValidator;

public class MSResultingDataUtil {
	private static Logger log = Logger.getLogger("log4j.logger.org.proteored");

	public static List<MiapeMSDocument> getMiapeMSsFromExperiment(
			Experiment experiment) {
		List<MiapeMSDocument> ret = new ArrayList<MiapeMSDocument>();
		if (experiment != null) {
			List<Replicate> replicates = experiment.getReplicates();
			if (replicates != null) {
				for (Replicate replicate : replicates) {
					List<MiapeMSDocument> miapeMSs = replicate.getMiapeMSs();
					if (miapeMSs != null) {
						ret.addAll(miapeMSs);
					}
				}
			}
		}
		return ret;
	}

	public static List<MiapeMSIDocument> getMiapeMSIsFromExperiment(
			Experiment experiment) {
		List<MiapeMSIDocument> ret = new ArrayList<MiapeMSIDocument>();
		if (experiment != null) {
			List<Replicate> replicates = experiment.getReplicates();
			if (replicates != null) {
				for (Replicate replicate : replicates) {
					List<MiapeMSIDocument> miapeMSIs = replicate.getMiapeMSIs();
					if (miapeMSIs != null) {
						ret.addAll(miapeMSIs);
					}
				}
			}
		}
		return ret;
	}

	public static boolean hasMGFFile(MiapeMSDocument miapeMS,
			ControlVocabularyManager cvManager) {

		boolean comesFromMGFFile = false;
		if (miapeMS != null) {

			final List<ResultingData> resultingDatas = miapeMS
					.getResultingDatas();
			if (resultingDatas != null) {
				for (ResultingData resultingData2 : resultingDatas) {
					final String dataFileUri = resultingData2.getDataFileUri();
					if (dataFileUri != null && !"".equals(dataFileUri)) {
						final String dataFileType = resultingData2
								.getDataFileType();
						final String mascotMGFPreferredName = MSFileType
								.getInstance(cvManager)
								.getCVTermByAccession(MSFileType.MASCOT_MGF)
								.getPreferredName();
						if (dataFileType.equals(mascotMGFPreferredName)
								|| dataFileType.contains("MGF"))
							comesFromMGFFile = true;
					}

				}
			}
		}

		if (comesFromMGFFile)
			log.info("MIAPE MS containing a MGF file");
		else
			log.info("MIAPE MS not containing a MGF file");
		return comesFromMGFFile;
	}

	/**
	 * Search for a resulting data with a dataFileURI not empty and a
	 * dataFileType=MASCOT_MGF
	 * 
	 * @param resultingData
	 * @param cvManager
	 * @return
	 */
	public static boolean hasProteomeDiscoverer(MiapeMSIDocument miapeMSI,
			ControlVocabularyManager cvManager) {
		boolean hasProteomeDiscoverer = false;
		if (miapeMSI != null) {
			final Set<Software> softwares = miapeMSI.getSoftwares();
			if (softwares != null) {
				for (Software software : softwares) {
					if (software
							.getName()
							.equals(SoftwareName
									.getInstance(cvManager)
									.getCVTermByAccession(
											SoftwareName.PROTEOMEDISCOVERER_ACC)
									.getPreferredName())) {
						hasProteomeDiscoverer = true;
						continue;
					}
				}
			}
		}

		if (hasProteomeDiscoverer)
			log.info("MIAPE MSI containing ProteomeDiscoverer");
		else
			log.info("MIAPE MSI containing a software different to ProteomeDiscoverer");
		return hasProteomeDiscoverer;
	}

	/**
	 * Search for a resulting data with a dataFileURI not empty and a
	 * dataFileType=MzML
	 * 
	 * @param resultingData
	 * @return
	 */
	public static boolean hasMzMLFile(MiapeMSDocument miapeMS,
			ControlVocabularyManager cvManager) {
		boolean hasMzML = false;
		if (miapeMS != null) {
			final List<ResultingData> resultingDatas = miapeMS
					.getResultingDatas();
			if (resultingDatas != null) {
				for (ResultingData resultingData2 : resultingDatas) {
					final String dataFileUri = resultingData2.getDataFileUri();
					if (dataFileUri != null && !"".equals(dataFileUri)) {
						final String dataFileType = resultingData2
								.getDataFileType();
						if (dataFileType.equals(MSFileType
								.getInstance(cvManager)
								.getCVTermByAccession(MSFileType.MZML_ACC)
								.getPreferredName())
								|| dataFileType.contains("mzML"))
							hasMzML = true;
					}

				}
			}
		}
		if (hasMzML)
			log.info("MIAPE MS containing a mzML file");
		else
			log.info("MIAPE MS not containing a mzML file");
		return hasMzML;
	}

	public static ResultingData getMGFResultingDataFromMIAPEMS(
			MiapeMSDocument miapeMS, ControlVocabularyManager cvManager) {
		if (miapeMS != null) {

			final List<ResultingData> resultingDatas = miapeMS
					.getResultingDatas();
			if (resultingDatas != null) {
				for (ResultingData resultingData2 : resultingDatas) {
					final String dataFileUri = resultingData2.getDataFileUri();
					if (dataFileUri != null && !"".equals(dataFileUri)) {

						final String dataFileType = resultingData2
								.getDataFileType();
						if ((dataFileType.equals(MSFileType
								.getInstance(cvManager)
								.getCVTermByAccession(MSFileType.MASCOT_MGF)
								.getPreferredName()) || dataFileType
								.contains("MGF"))
								&& URLValidator.validateURL(dataFileUri))
							return resultingData2;
					}

				}
			}
		}
		return null;
	}

	public static ResultingData getMzMLResultingDataFromMIAPEMS(
			MiapeMSDocument miapeMS, ControlVocabularyManager cvManager) {
		if (miapeMS != null) {

			final List<ResultingData> resultingDatas = miapeMS
					.getResultingDatas();
			if (resultingDatas != null) {
				for (ResultingData resultingData2 : resultingDatas) {
					final String dataFileUri = resultingData2.getDataFileUri();
					if (dataFileUri != null && !"".equals(dataFileUri)) {

						final String dataFileType = resultingData2
								.getDataFileType();
						if ((dataFileType.equals(MSFileType
								.getInstance(cvManager)
								.getCVTermByAccession(MSFileType.MZML_ACC)
								.getPreferredName()) || dataFileType
								.contains("mzML"))
								&& URLValidator.validateURL(dataFileUri))
							return resultingData2;
					}

				}
			}
		}
		return null;
	}

	public static ResultingData getResultingDataFromMiapeMS(
			MiapeMSDocument miapeMS, ControlVocabularyManager cvManager) {
		if (MSResultingDataUtil.hasMGFFile(miapeMS, cvManager)) {
			return MSResultingDataUtil.getMGFResultingDataFromMIAPEMS(miapeMS,
					cvManager);
		}
		if (MSResultingDataUtil.hasMzMLFile(miapeMS, cvManager)) {
			return MSResultingDataUtil.getMzMLResultingDataFromMIAPEMS(miapeMS,
					cvManager);
		}
		return null;
	}
}
