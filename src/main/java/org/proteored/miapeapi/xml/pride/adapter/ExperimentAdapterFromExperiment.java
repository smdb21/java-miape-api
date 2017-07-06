package org.proteored.miapeapi.xml.pride.adapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.proteored.miapeapi.cv.Accession;
import org.proteored.miapeapi.cv.AdditionalInformationName;
import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularyTerm;
import org.proteored.miapeapi.cv.PRIDEOntology;
import org.proteored.miapeapi.cv.ms.MSFileType;
import org.proteored.miapeapi.experiment.model.Experiment;
import org.proteored.miapeapi.experiment.model.ExtendedIdentifiedProtein;
import org.proteored.miapeapi.experiment.model.ProteinGroupOccurrence;
import org.proteored.miapeapi.experiment.model.ProteinOccurrence;
import org.proteored.miapeapi.experiment.model.datamanager.DataManager;
import org.proteored.miapeapi.experiment.model.grouping.ProteinEvidence;
import org.proteored.miapeapi.experiment.model.sort.SorterUtil;
import org.proteored.miapeapi.interfaces.Adapter;
import org.proteored.miapeapi.interfaces.ms.ResultingData;
import org.proteored.miapeapi.xml.msi.adapter.InnerIteratorSync2;
import org.proteored.miapeapi.xml.pride.adapter.util.MSResultingDataUtil;
import org.proteored.miapeapi.xml.pride.autogenerated.CvParamType;
import org.proteored.miapeapi.xml.pride.autogenerated.ExperimentType;
import org.proteored.miapeapi.xml.pride.autogenerated.ExperimentType.MzData;
import org.proteored.miapeapi.xml.pride.autogenerated.ExperimentType.MzData.SpectrumList;
import org.proteored.miapeapi.xml.pride.autogenerated.ExperimentType.MzData.SpectrumList.Spectrum;
import org.proteored.miapeapi.xml.pride.autogenerated.GelFreeIdentificationType;
import org.proteored.miapeapi.xml.pride.autogenerated.ObjectFactory;
import org.proteored.miapeapi.xml.pride.autogenerated.ParamType;
import org.proteored.miapeapi.xml.pride.autogenerated.Peptide;
import org.proteored.miapeapi.xml.pride.util.PrideControlVocabularyXmlFactory;
import org.proteored.miapeapi.xml.util.parallel.InnerLock;

import edu.scripps.yates.utilities.cores.SystemCoreManager;
import gnu.trove.set.hash.THashSet;
import gnu.trove.set.hash.TIntHashSet;

public class ExperimentAdapterFromExperiment implements Adapter<ExperimentType> {
	private static Logger log = Logger.getLogger("log4j.logger.org.proteored");
	private final ObjectFactory factory;
	private final ControlVocabularyManager cvManager;
	private static final int MAX_NUMBER_THREADS = 12;

	private final PrideControlVocabularyXmlFactory prideCvUtil;
	private final Experiment experiment;
	private final boolean addPeakList;
	private final boolean removeNotMatchedSpectra;
	private boolean gelBasedExperiment;
	private final boolean excludeNonConclusiveProteinGroups;
	private final String prideGenerationSoftware;

	/**
	 * Creates an experiment PRIDE element. <br>
	 * By default, all spectra will be included (matched and not matched
	 * spectra).
	 * 
	 * @param factory
	 * @param cvManager
	 * @param experiment
	 * @param addPeakList
	 */
	public ExperimentAdapterFromExperiment(ObjectFactory factory, ControlVocabularyManager cvManager,
			Experiment experiment, boolean addPeakList, boolean excludeNonConclusiveProteinGroups) {
		this(factory, cvManager, false, experiment, addPeakList, false, excludeNonConclusiveProteinGroups);
	}

	public ExperimentAdapterFromExperiment(ObjectFactory factory, ControlVocabularyManager cvManager,
			Experiment experiment, boolean addPeakList, boolean excludeNonConclusiveProteinGroups,
			String prideGenerationSoftware) {
		this(factory, cvManager, false, experiment, addPeakList, false, excludeNonConclusiveProteinGroups,
				prideGenerationSoftware);
	}

	public ExperimentAdapterFromExperiment(ObjectFactory factory, ControlVocabularyManager cvManager,
			boolean gelBasedExperiment, Experiment experiment, boolean addPeakList, boolean removeNoMatchedSpectra,
			boolean excludeNonConclusiveProteinGroups, String prideGenerationSoftware) {

		this.factory = factory;
		this.cvManager = cvManager;
		this.experiment = experiment;
		this.prideCvUtil = new PrideControlVocabularyXmlFactory(factory, cvManager);
		this.addPeakList = addPeakList;
		this.removeNotMatchedSpectra = removeNoMatchedSpectra;
		this.gelBasedExperiment = gelBasedExperiment;
		this.excludeNonConclusiveProteinGroups = excludeNonConclusiveProteinGroups;
		this.prideGenerationSoftware = prideGenerationSoftware;
	}

	public ExperimentAdapterFromExperiment(ObjectFactory factory, ControlVocabularyManager cvManager,
			boolean gelBasedExperiment, Experiment experiment, boolean addPeakList, boolean removeNoMatchedSpectra,
			boolean excludeNonConclusiveProteinGroups) {

		this.factory = factory;
		this.cvManager = cvManager;
		this.experiment = experiment;
		this.prideCvUtil = new PrideControlVocabularyXmlFactory(factory, cvManager);
		this.addPeakList = addPeakList;
		this.removeNotMatchedSpectra = removeNoMatchedSpectra;
		this.gelBasedExperiment = gelBasedExperiment;
		this.excludeNonConclusiveProteinGroups = excludeNonConclusiveProteinGroups;
		this.prideGenerationSoftware = "ProteoRed MIAPE Extractor";
	}

	/**
	 * 
	 * @param factory
	 * @param cvManager
	 * @param experiment
	 * @param addPeakList
	 * @param removeNotMatchedSpectra
	 *            if true, resulting PRIDE Experiment element will not contain
	 *            spectra that have not been matched
	 */
	public ExperimentAdapterFromExperiment(ObjectFactory factory, ControlVocabularyManager cvManager,
			Experiment experiment, boolean addPeakList, boolean removeNotMatchedSpectra,
			boolean excludeNonConclusiveProteinGroups) {
		this(factory, cvManager, false, experiment, addPeakList, removeNotMatchedSpectra,
				excludeNonConclusiveProteinGroups);

	}

	public ExperimentAdapterFromExperiment(ObjectFactory factory, ControlVocabularyManager cvManager,
			Experiment experiment, boolean addPeakList, boolean removeNotMatchedSpectra,
			boolean excludeNonConclusiveProteinGroups, String prideGenerationSoftware) {
		this(factory, cvManager, false, experiment, addPeakList, removeNotMatchedSpectra,
				excludeNonConclusiveProteinGroups, prideGenerationSoftware);

	}

	public void setGelBasedExperiment(boolean gelBasedExperiment) {
		this.gelBasedExperiment = gelBasedExperiment;
	}

	@Override
	public ExperimentType adapt() {
		ExperimentType xmlExperiment = factory.createExperimentType();

		log.info("adding  MIAPE data: titles and contacts");

		addAdditionals(xmlExperiment);

		xmlExperiment.setProtocol(new ProtocolAdapterFromExperiment(factory, cvManager, experiment).adapt());

		xmlExperiment.setMzData(new MzDataAdapter(factory, cvManager,
				MSResultingDataUtil.getMiapeMSsFromExperiment(experiment), this.addPeakList).adapt());

		addGelFreeIdentifications(xmlExperiment);

		if (removeNotMatchedSpectra)
			// removeNotMatchedSpectra(xmlExperiment);
			removeNotMatchedSpectraInParallel(xmlExperiment);
		return xmlExperiment;
	}

	private void removeNotMatchedSpectra(ExperimentType xmlExperiment) {
		final MzData mzData = xmlExperiment.getMzData();
		if (mzData != null) {
			final SpectrumList spectrumList = mzData.getSpectrumList();
			if (spectrumList != null) {
				final List<Spectrum> spectra = spectrumList.getSpectrum();
				if (spectra != null) {
					int numSpectra = spectra.size();
					final List<GelFreeIdentificationType> gelFreeIdentifications = xmlExperiment
							.getGelFreeIdentification();
					if (gelFreeIdentifications != null) {
						TIntHashSet spectrumRefs = new TIntHashSet();
						log.info("Removing not matched spectra from " + numSpectra + " spectra");
						for (GelFreeIdentificationType gelFreeIdentification : gelFreeIdentifications) {
							final List<Peptide> peptideItems = gelFreeIdentification.getPeptideItem();
							for (Peptide peptide : peptideItems) {
								if (!spectrumRefs.contains(peptide.getSpectrumReference().intValue())) {
									spectrumRefs.add(Integer.valueOf(String.valueOf(peptide.getSpectrumReference())));
								}
							}
						}
						log.info(spectrumRefs.size() + " spectra in the inclusion list");
						int total = spectra.size();

						long t1 = System.currentTimeMillis();
						final Iterator<Spectrum> iterator = spectra.iterator();
						while (iterator.hasNext()) {
							final Spectrum spect = iterator.next();
							if (!spectrumRefs.contains(spect.getId())) {
								iterator.remove();
							}
						}
						log.info(total + " proteins processed successfully in "
								+ (System.currentTimeMillis() - t1) / 1000 + " sg.");
						log.info("Now there are " + spectra.size() + " spectra");
					}
				}
			}
		}
	}

	private void removeNotMatchedSpectraInParallel(ExperimentType xmlExperiment) {
		final MzData mzData = xmlExperiment.getMzData();
		if (mzData != null) {
			final SpectrumList spectrumList = mzData.getSpectrumList();
			if (spectrumList != null) {
				final List<Spectrum> spectra = spectrumList.getSpectrum();
				if (spectra != null) {
					int numSpectra = spectra.size();
					final List<GelFreeIdentificationType> gelFreeIdentifications = xmlExperiment
							.getGelFreeIdentification();
					if (gelFreeIdentifications != null) {
						TIntHashSet spectrumRefs = new TIntHashSet();
						log.info("Removing not matched spectra from " + numSpectra + " spectra");
						for (GelFreeIdentificationType gelFreeIdentification : gelFreeIdentifications) {
							final List<Peptide> peptideItems = gelFreeIdentification.getPeptideItem();
							for (Peptide peptide : peptideItems) {
								if (!spectrumRefs.contains(peptide.getSpectrumReference().intValue())) {
									spectrumRefs.add(Integer.valueOf(String.valueOf(peptide.getSpectrumReference())));
								}
							}
						}
						log.info(spectrumRefs.size() + " spectra in the inclusion list");

						// PARALLELIZED
						int total = spectra.size();
						long t1 = System.currentTimeMillis();
						final Iterator<Spectrum> iterator = spectra.iterator();

						InnerLock lock = new InnerLock();
						InnerIteratorSync2<Spectrum> iteratorSync = new InnerIteratorSync2(iterator, spectrumRefs);
						Collection<SpectraFilter> runners = new ArrayList<SpectraFilter>();
						int processorCount = SystemCoreManager.getAvailableNumSystemCores();
						if (processorCount > MAX_NUMBER_THREADS)
							processorCount = MAX_NUMBER_THREADS;

						log.info("Using " + processorCount + " processors");
						for (int i = 0; i < processorCount; i++) {
							// take current DB session
							SpectraFilter runner = new SpectraFilter(iteratorSync, lock, i, spectrumRefs);
							runners.add(runner);
							new Thread(runner).start();
						}

						// Wait until all the processors ends
						lock.isDone(runners.size());
						log.info(total + " proteins processed successfully in parallel in "
								+ (System.currentTimeMillis() - t1) / 1000 + " sg.");

						log.info("Now there are " + spectra.size() + " spectra");
						log.info("Finished parallelism");
					}
				}
			}
		}
	}

	private void addGelFreeIdentifications(ExperimentType xmlExperiment) {
		if (this.experiment != null) {
			log.info("Adding gel free identifications");
			Collection<ProteinGroupOccurrence> proteinGroupOccurrenceSet = this.experiment
					.getProteinGroupOccurrenceList().values();
			List<ProteinGroupOccurrence> proteinGroupOccurrencelist = new ArrayList<ProteinGroupOccurrence>();
			for (ProteinGroupOccurrence identificationOccurrence : proteinGroupOccurrenceSet) {
				proteinGroupOccurrencelist.add(identificationOccurrence);
			}
			// Sort by protein accession name
			SorterUtil.sortProteinGroupOcurrencesByProteinAccessionString(proteinGroupOccurrencelist);

			// if exclude nonconclusive proteins or it is a decoy hit, not
			// include
			for (ProteinGroupOccurrence proteinGroupOccurrence : proteinGroupOccurrencelist) {
				if ((this.excludeNonConclusiveProteinGroups
						&& proteinGroupOccurrence.getEvidence() == ProteinEvidence.NONCONCLUSIVE)
						|| proteinGroupOccurrence.isDecoy())
					continue;
				final List<ExtendedIdentifiedProtein> proteins = proteinGroupOccurrence.getProteins();
				Map<String, ProteinOccurrence> proteinOccurrences = DataManager.createProteinOccurrenceList(proteins);

				for (ProteinOccurrence proteinOccurrence : proteinOccurrences.values()) {
					xmlExperiment.getGelFreeIdentification()
							.add(new GelFreeIdentificationAdapterFromProteinOccurrence(factory, cvManager,
									proteinOccurrence, proteinGroupOccurrence.getProteinLocalFDR(), this.addPeakList)
											.adapt());
				}

			}
		}
	}

	private void addAdditionals(ExperimentType xmlExperiment) {
		String title = "Title of the experiment (replace this string by a description of the experiment)";
		String shortLabel = "Experiment short label (replace this string by a short description of the experiment)";

		if (experiment != null) {
			title = experiment.getFullName();
			shortLabel = experiment.getName();
		}
		xmlExperiment.setShortLabel(shortLabel);
		xmlExperiment.setTitle(title);

		ParamType additional = factory.createParamType();

		// Project name
		String projectName = "Project (replace this string by a description of your project)";
		if (this.experiment != null && this.experiment.getPreviousLevelIdentificationSet() != null) {
			projectName = this.experiment.getPreviousLevelIdentificationSet().getFullName();
		}
		final ControlVocabularyTerm projectTerm = AdditionalInformationName.getInstance(cvManager)
				.getCVTermByAccession(AdditionalInformationName.PROJECT);
		CvParamType cvParamProject = prideCvUtil.createCvParam(projectTerm.getPreferredName(), projectName,
				AdditionalInformationName.getInstance(cvManager));
		if (cvParamProject != null) {
			additional.getCvParamOrUserParam().add(cvParamProject);
		}

		// pride generation software
		final ControlVocabularyTerm prideXmlSoftwareGenerator = AdditionalInformationName.getInstance(cvManager)
				.getCVTermByAccession(AdditionalInformationName.PRIDE_GENERATION_SOFTWARE_ACCESSION);
		CvParamType cvParamSoftware = prideCvUtil.createCvParam(prideXmlSoftwareGenerator.getPreferredName(),
				this.prideGenerationSoftware, AdditionalInformationName.getInstance(cvManager));
		if (cvParamSoftware != null) {
			additional.getCvParamOrUserParam().add(cvParamSoftware);
		}

		if (experiment != null) {
			// No PTMs
			if (experiment.getDifferentPeptideModificationNames() == null
					|| experiment.getDifferentPeptideModificationNames().isEmpty()) {
				final ControlVocabularyTerm noPTMsTerm = AdditionalInformationName.getInstance(cvManager)
						.getCVTermByAccession(AdditionalInformationName.NO_PTMS);
				if (noPTMsTerm != null) {
					CvParamType cvParamNoPTms = prideCvUtil.createCvParam(noPTMsTerm.getPreferredName(), null,
							AdditionalInformationName.getInstance(cvManager));
					if (cvParamNoPTms != null) {
						additional.getCvParamOrUserParam().add(cvParamNoPTms);
					}
				}
			}
			// Gel based experiment
			if (this.gelBasedExperiment) {
				final ControlVocabularyTerm gelBasedExperimentTerm = AdditionalInformationName.getInstance(cvManager)
						.getCVTermByAccession(AdditionalInformationName.GEL_BASED_EXPERIMENT);
				CvParamType cvParamGelBasedExperiment = prideCvUtil.createCvParam(
						gelBasedExperimentTerm.getPreferredName(), null,
						AdditionalInformationName.getInstance(cvManager));
				if (cvParamGelBasedExperiment != null) {
					additional.getCvParamOrUserParam().add(cvParamGelBasedExperiment);
				}
			}
			// Original MS data file format
			final List<ResultingData> resultingDatas = experiment.getResultingDatas();
			if (resultingDatas != null) {
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
		if (additional != null && additional.getCvParamOrUserParam() != null
				&& !additional.getCvParamOrUserParam().isEmpty())
			xmlExperiment.setAdditional(additional);
		/*
		 * changed since customizations is part of the spectrometer if (document
		 * instanceof MiapeMSDocument) { Protocol protocol =
		 * factory.createExperimentTypeProtocol(); MiapeMSDocument ms =
		 * (MiapeMSDocument)document; String customizations =
		 * ms.getCustomizations(); if (customizations.startsWith("Protocol:")) {
		 * String stepName = customizations.substring(8);
		 * protocol.setProtocolName(stepName);
		 * xmlExperiment.setProtocol(protocol); } }
		 */
	}

}
