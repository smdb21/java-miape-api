package org.proteored.miapeapi.experiment.model.datamanager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.exceptions.IllegalMiapeArgumentException;
import org.proteored.miapeapi.experiment.model.Experiment;
import org.proteored.miapeapi.experiment.model.ExperimentList;
import org.proteored.miapeapi.experiment.model.ExtendedIdentifiedPeptide;
import org.proteored.miapeapi.experiment.model.ExtendedIdentifiedProtein;
import org.proteored.miapeapi.experiment.model.IdentificationSet;
import org.proteored.miapeapi.experiment.model.PeptideOccurrence;
import org.proteored.miapeapi.experiment.model.ProteinGroup;
import org.proteored.miapeapi.experiment.model.ProteinGroupOccurrence;
import org.proteored.miapeapi.experiment.model.ProteinOccurrence;
import org.proteored.miapeapi.experiment.model.Replicate;
import org.proteored.miapeapi.experiment.model.filters.FDRFilter;
import org.proteored.miapeapi.experiment.model.filters.Filter;
import org.proteored.miapeapi.experiment.model.filters.ModificationFilter;
import org.proteored.miapeapi.experiment.model.filters.OccurrenceFilter;
import org.proteored.miapeapi.experiment.model.filters.ProteinACCFilter;
import org.proteored.miapeapi.experiment.model.grouping.PAnalyzer;
import org.proteored.miapeapi.experiment.model.grouping.PanalyzerStats;
import org.proteored.miapeapi.experiment.model.grouping.ProteinEvidence;
import org.proteored.miapeapi.experiment.model.sort.SorterUtil;
import org.proteored.miapeapi.experiment.model.sort.SystemCoreManager;
import org.proteored.miapeapi.interfaces.msi.Database;
import org.proteored.miapeapi.interfaces.msi.IdentifiedPeptide;
import org.proteored.miapeapi.interfaces.msi.IdentifiedProtein;
import org.proteored.miapeapi.interfaces.msi.IdentifiedProteinSet;
import org.proteored.miapeapi.interfaces.msi.InputParameter;
import org.proteored.miapeapi.interfaces.msi.MiapeMSIDocument;
import org.proteored.miapeapi.interfaces.msi.PeptideModification;
import org.proteored.miapeapi.interfaces.msi.PeptideScore;
import org.proteored.miapeapi.interfaces.msi.ProteinScore;
import org.proteored.miapeapi.spring.SpringHandler;

import pi.ParIterator;
import pi.ParIterator.Schedule;
import pi.ParIteratorFactory;
import pi.reductions.Reducible;
import pi.reductions.Reduction;

public abstract class DataManager {
	// Peptides less than this length, will be discarded
	public final static int DEFAULT_MIN_PEPTIDE_LENGTH = 7;

	// private final static HashMap<Integer, ExtendedIdentifiedProtein>
	// staticProteins = new HashMap<Integer, ExtendedIdentifiedProtein>();
	private final ControlVocabularyManager cvManager;
	protected final IdentificationSet idSet;

	// log
	private static final Logger log = Logger.getLogger("log4j.logger.org.proteored");

	private static final int MAX_NUMBER_PARALLEL_PROCESSES = 12;
	// //////////
	// PROTEINS
	// Total list of proteinsgroups
	private List<ProteinGroup> identifiedProteinGroups = new ArrayList<ProteinGroup>();
	private List<ExtendedIdentifiedProtein> identifiedProteins = new ArrayList<ExtendedIdentifiedProtein>();

	// List of non filtered proteins

	// Protein occurrence, indicates how many times a protein group has been
	// identified over the replicates
	protected HashMap<String, ProteinGroupOccurrence> proteinGroupOccurrenceList = new HashMap<String, ProteinGroupOccurrence>();

	// Indicate if the proteins has already been filtered or not
	protected boolean isDataReady = false;
	// Sorting parameters for proteins
	// private SortingParameters sortingProteins;

	private final List<String> proteinScores = new ArrayList<String>();

	// /////////
	// PEPTIDES
	// List of peptides sorted by peptide score
	private List<ExtendedIdentifiedPeptide> identifiedPeptides = new ArrayList<ExtendedIdentifiedPeptide>();
	// Non filtered peptides, List of peptides sorted by peptides score
	// Peptide occurrence, indicates how many times a peptide has been
	// identified over replicates
	protected HashMap<String, PeptideOccurrence> peptideOccurrenceList = new HashMap<String, PeptideOccurrence>();
	// Indicates if two peptides with the same modification are considered equal
	// even if they have
	// different modifications
	private boolean distinguishModificatedPeptides;

	// Indicate if the peptides has already been filtered or not
	// protected boolean isPeptideFiltered = false;
	// Sorting parameters for peptides
	// private SortingParameters sortingPeptides;

	private final List<String> peptideScores = new ArrayList<String>();
	private final List<String> peptideModifications = new ArrayList<String>();
	// Filters
	private final List<Filter> filters = new ArrayList<Filter>();

	// Next level datamanagers
	private List<DataManager> dataManagers;

	private List<ProteinGroup> inclusionList = null;

	private final Set<String> differentSearchedDatabases = new HashSet<String>();

	private Float proteinFDR;
	private Float peptideFDR;
	private Float psmFDR;

	private Integer numProteinGroupDecoys = null;

	private Integer numDifferentProteinDecoys = null;

	private Integer numPeptideDecoys = null;

	private Integer numDifferentPeptideDecoys = null;
	private Integer numNonConclusiveGroups = null;
	private Integer numDifferentNonConclusiveGroups = null;

	// Needed for calculating the True Positives and all that stuff
	private final List<ExtendedIdentifiedProtein> nonFilteredIdentifiedProteins = new ArrayList<ExtendedIdentifiedProtein>();
	private final List<ExtendedIdentifiedPeptide> nonFilteredIdentifiedPeptides = new ArrayList<ExtendedIdentifiedPeptide>();

	private final boolean useStaticCollections = true;

	private static Integer minPeptideLength;

	private boolean groupingAtExperimentListLevel = false;
	private final boolean processInParallel;
	/**
	 * Create an identification set from the data extracted from a replicate
	 *
	 * @param identificationSets
	 *            that comes from the replicates of the experiment
	 */
	// public DataManager(List<DataManager> identificationSets,
	// SortingParameters sortingProteins,
	// SortingParameters sortingPeptides, FDRFilter proteinFDRFilter,
	// FDRFilter peptideFDRFilter) {
	// this.proteinFDRFilter = proteinFDRFilter;
	// this.peptideFDRFilter = peptideFDRFilter;
	// this.sortingProteins = sortingProteins;
	// this.sortingPeptides = sortingPeptides;
	//
	// // process replicates
	// processIdentificationSets(identificationSets);
	//
	// }

	/**
	 * Create an identification set from the data extracted from a replicate
	 *
	 * @param dataManagers
	 *            that comes from the replicates of the experiment
	 * @param groupingAtExperimentListLevel
	 *            perform PAnalyzer grouping at experiment list level
	 */
	public DataManager(IdentificationSet idSet, List<DataManager> dataManagers, boolean groupingAtExperimentListLevel,
			List<Filter> filters, Integer minPeptideLength, boolean processInParallel) {
		this.processInParallel = processInParallel;
		this.idSet = idSet;
		if (idSet != null && idSet.getCvManager() != null)
			cvManager = idSet.getCvManager();
		else
			cvManager = SpringHandler.getInstance().getCVManager();
		this.dataManagers = dataManagers;
		if (filters != null) {
			setFilters(filters);
		}
		DataManager.minPeptideLength = minPeptideLength;
		// this.sortingProteins = sortingProteins;
		// this.sortingPeptides = sortingPeptides;

		// process replicates
		boolean isASetOfRelicates = hasReplicates(dataManagers);
		if (isASetOfRelicates) {
			collectDataFromReplicates();
		} else {
			collectDataFromExperiments(groupingAtExperimentListLevel);
		}
	}

	/**
	 * Create an identification set from the data extracted from a replicate
	 *
	 * @param dataManagers
	 *            that comes from the replicates of the experiment
	 */
	public DataManager(IdentificationSet idSet, List<DataManager> dataManagers, Integer minPeptideLength,
			List<Filter> filters, boolean processInParallel) {
		this(idSet, dataManagers, false, filters, minPeptideLength, processInParallel);
	}

	public DataManager(IdentificationSet idSet, List<MiapeMSIDocument> miapeMSIs, List<Filter> filters,
			Integer minPeptideLength, boolean processInParallel) {
		this.idSet = idSet;
		this.processInParallel = processInParallel;
		if (idSet != null && idSet.getCvManager() != null)
			cvManager = idSet.getCvManager();
		else
			cvManager = SpringHandler.getInstance().getCVManager();
		if (filters != null)
			setFilters(filters);

		DataManager.minPeptideLength = minPeptideLength;
		collectDataFromMSIs(miapeMSIs);
	}

	private boolean hasReplicates(List<DataManager> dmgs) {
		if (dmgs == null || dmgs.isEmpty()) {
			throw new IllegalMiapeArgumentException("Data for " + idSet.getName() + " is empty!");
		}
		boolean replicate = false;
		for (DataManager dataManager : dmgs) {
			if (dataManager instanceof ReplicateDataManager) {
				replicate = true;
			} else if (replicate) {
				throw new IllegalMiapeArgumentException(
						"Datamanagers for " + idSet.getName() + " are from different sources");
			}
			if (dataManager instanceof ExperimentListDataManager) {
				throw new IllegalMiapeArgumentException("ExperimentListDatamanagers cannot be collected!");
			}
		}
		return replicate;
	}

	public String getExperimentName() {
		if (idSet instanceof Experiment || idSet instanceof ExperimentList)
			return idSet.getName();
		if (idSet instanceof Replicate)
			return ((Replicate) idSet).getExperimentName();
		return null;
	}

	public String getReplicateName() {
		if (idSet instanceof Replicate)
			return idSet.getName();
		return null;
	}

	private void linkProteinsAndPeptides() {
		int minPeptideLength = getMinPeptideLength();
		if (nonFilteredIdentifiedPeptides.isEmpty() || nonFilteredIdentifiedProteins.isEmpty()) {
			log.info("No peptides  or proteins!!");
			return;
		}
		log.info("Linking " + nonFilteredIdentifiedProteins.size() + " proteins with "
				+ nonFilteredIdentifiedPeptides.size() + " peptides");
		HashMap<Integer, ExtendedIdentifiedPeptide> peptideMap = new HashMap<Integer, ExtendedIdentifiedPeptide>();
		for (ExtendedIdentifiedPeptide peptide : nonFilteredIdentifiedPeptides) {
			peptideMap.put(peptide.getId(), peptide);
		}
		HashMap<Integer, ExtendedIdentifiedProtein> proteinMap = new HashMap<Integer, ExtendedIdentifiedProtein>();
		for (ExtendedIdentifiedProtein prot : nonFilteredIdentifiedProteins) {
			proteinMap.put(prot.getId(), prot);
		}

		for (ExtendedIdentifiedProtein protein : nonFilteredIdentifiedProteins) {
			List<IdentifiedPeptide> peptidesFromProtein = protein.getIdentifiedPeptides();
			if (peptidesFromProtein != null && !peptidesFromProtein.isEmpty()) {
				boolean peptideFound = false;
				for (IdentifiedPeptide peptideFromProtein : peptidesFromProtein) {
					if (peptideFromProtein != null && peptideFromProtein.getSequence() != null
							&& peptideFromProtein.getSequence().length() >= minPeptideLength
							&& peptideMap.containsKey(peptideFromProtein.getId())) {

						protein.addPeptide(peptideMap.get(peptideFromProtein.getId()));
						peptideMap.get(peptideFromProtein.getId()).addProtein(protein);
						peptideFound = true;
					}
				}
				if (!peptideFound) {
					// can be possible because of the required minimum length of
					// the peptide

				}
			} else {
				if (getMinPeptideLength() != Integer.MAX_VALUE)
					log.warn("The protein " + protein.getAccession() + " has not peptides");
			}
		}
		for (ExtendedIdentifiedPeptide peptide : nonFilteredIdentifiedPeptides) {
			if (peptide.getSequence() != null && peptide.getSequence().length() >= minPeptideLength) {
				List<IdentifiedProtein> proteinsFromPeptide = peptide.getIdentifiedProteins();
				if (proteinsFromPeptide != null && !proteinsFromPeptide.isEmpty()) {
					boolean proteinFound = false;
					for (IdentifiedProtein proteinFromPeptide : proteinsFromPeptide) {
						if (proteinFromPeptide != null && proteinMap.containsKey(proteinFromPeptide.getId())) {
							proteinFound = true;
							peptide.addProtein(proteinMap.get(proteinFromPeptide.getId()));
							proteinMap.get(proteinFromPeptide.getId()).addPeptide(peptide);
						}
					}
					if (!proteinFound)
						log.warn("The peptide " + peptide.getId() + " has not proteins");
				} else {
					log.warn("The peptide " + peptide.getId() + " has not proteins");
				}
			}
		}
		log.info("End linking proteins and peptides");

		log.info("Discarding Proteins without peptides and peptide without proteins");
		final Iterator<ExtendedIdentifiedPeptide> peptideIterator = nonFilteredIdentifiedPeptides.iterator();
		final Iterator<ExtendedIdentifiedProtein> proteinIterator = nonFilteredIdentifiedProteins.iterator();
		int numPeptidesDiscarded = 0;
		while (peptideIterator.hasNext()) {
			final List<ExtendedIdentifiedProtein> proteins = peptideIterator.next().getProteins();
			if (proteins == null || proteins.isEmpty()) {
				numPeptidesDiscarded++;
				peptideIterator.remove();
			}
		}
		log.info("Discarded peptides = " + numPeptidesDiscarded + " because they have not a protein linked");
		int numProteinsDiscarded = 0;
		while (proteinIterator.hasNext()) {
			final List<ExtendedIdentifiedPeptide> peptides = proteinIterator.next().getPeptides();
			if (peptides == null || peptides.isEmpty()) {
				numProteinsDiscarded++;
				proteinIterator.remove();
			}
		}
		log.info("Discarded proteins = " + numProteinsDiscarded + " because of the minimum peptide length limit");
	}

	public static int getMinPeptideLength() {
		int minPepLength = DEFAULT_MIN_PEPTIDE_LENGTH;
		if (DataManager.minPeptideLength != null)
			minPepLength = DataManager.minPeptideLength;
		log.debug("min peptide length=" + minPepLength);
		return minPepLength;
	}

	/**
	 * Extracts proteins and peptides from MIAPE MSI
	 *
	 * @param experimentName
	 *
	 */
	private void collectDataFromMSIs(List<MiapeMSIDocument> miapeMSIs) {
		int minPepLength = getMinPeptideLength();
		int peptideWithoutProteins = 0;
		// reset collections
		resetIdentificationSet();
		Set<Integer> peptidesIds = new HashSet<Integer>();
		// process the MIAPE
		if (miapeMSIs != null) {
			for (MiapeMSIDocument miapeMSIDocument : miapeMSIs) {
				if (miapeMSIDocument != null) {
					// Databases
					Set<InputParameter> inputParameters = miapeMSIDocument.getInputParameters();
					if (inputParameters != null) {
						for (InputParameter inputParameter : inputParameters) {
							if (inputParameter != null && inputParameter.getDatabases() != null) {
								for (Database database : inputParameter.getDatabases()) {
									String name = database.getName();
									if (name != null && !"".equals(name) && !differentSearchedDatabases.contains(name))
										differentSearchedDatabases.add(name);
								}
							}
						}
					}

					// PROTEINS
					Set<IdentifiedProteinSet> identifiedProteinSets = miapeMSIDocument.getIdentifiedProteinSets();
					if (identifiedProteinSets != null) {
						for (IdentifiedProteinSet identifiedProteinSet : identifiedProteinSets) {
							HashMap<String, IdentifiedProtein> identifiedProteins2 = identifiedProteinSet
									.getIdentifiedProteins();
							if (identifiedProteins2 != null) {
								log.info(identifiedProteins2.size() + " proteins in MIAPE MSI "
										+ miapeMSIDocument.getId());
								for (IdentifiedProtein protein : identifiedProteins2.values()) {
									// Allow proteins without peptides
									// if (!hasOnePeptideGreaterOrEqualThan(
									// protein, minPepLength))
									// continue;
									ExtendedIdentifiedProtein extendedIdentifiedProtein = null;
									// if (!staticProteins.containsKey(protein
									// .getId())) {
									extendedIdentifiedProtein = new ExtendedIdentifiedProtein((Replicate) idSet,
											protein, miapeMSIDocument);
									// if (useStaticCollections)
									// staticProteins.put(
									// extendedIdentifiedProtein
									// .getId(),
									// extendedIdentifiedProtein);

									// } else {
									// extendedIdentifiedProtein =
									// staticProteins
									// .get(protein.getId());
									// extendedIdentifiedProtein
									// .resetPeptides();
									// }

									extendedIdentifiedProtein.clearPeptides();
									nonFilteredIdentifiedProteins.add(extendedIdentifiedProtein);

									final Set<ProteinScore> scores = extendedIdentifiedProtein.getScores();
									if (scores != null) {
										for (ProteinScore proteinScore : scores) {
											String scoreName = proteinScore.getName();
											if (!proteinScores.contains(scoreName))
												proteinScores.add(scoreName);
										}
									}

								}
							}
						}
					}
					// PEPTIDES
					List<IdentifiedPeptide> identifiedPeptides2 = miapeMSIDocument.getIdentifiedPeptides();
					log.info("There are " + identifiedPeptides2.size() + " peptides in the MSI document");
					if (identifiedPeptides2 != null) {
						for (IdentifiedPeptide peptide : identifiedPeptides2) {
							if (peptide.getIdentifiedProteins().isEmpty()) {
								peptideWithoutProteins++;
								continue;
							}
							if (peptide.getSequence() != null && peptide.getSequence().length() >= minPepLength) {
								// NOT ADD PEPTIDES THAT
								// WERE
								// ADDED
								// BEFORE!
								if (peptidesIds.contains(peptide.getId())) {
									log.info("Peptide is already processed");
									continue;
								}
								peptidesIds.add(peptide.getId());

								ExtendedIdentifiedPeptide extendedIdentifiedPeptide = null;
								// if (!staticPeptides
								// .containsKey(peptide
								// .getId())) {
								extendedIdentifiedPeptide = new ExtendedIdentifiedPeptide((Replicate) idSet, peptide,
										miapeMSIDocument);
								if (useStaticCollections) {
									StaticPeptideStorage.addPeptide(miapeMSIDocument, extendedIdentifiedPeptide);
									// staticPeptides.put(extendedIdentifiedPeptide.getId(),
									// extendedIdentifiedPeptide);
								}
								// } else {
								// extendedIdentifiedPeptide =
								// staticPeptides
								// .get(peptide
								// .getId());
								// }
								// extendedIdentifiedPeptide
								// .setAsFiltered(false);
								extendedIdentifiedPeptide.clearProteins();
								nonFilteredIdentifiedPeptides.add(extendedIdentifiedPeptide);

								final Set<PeptideScore> peptideScores = extendedIdentifiedPeptide.getScores();
								if (peptideScores != null) {
									for (PeptideScore peptideScore : peptideScores) {
										String scoreName = peptideScore.getName();
										if (scoreName != null && !"".equals(scoreName.trim()))
											if (!this.peptideScores.contains(scoreName))
												this.peptideScores.add(scoreName);
									}
								}
								final Set<PeptideModification> modifs = peptide.getModifications();
								if (modifs != null) {
									for (PeptideModification peptideModification : modifs) {
										String modificationName = peptideModification.getName();
										if (!peptideModifications.contains(modificationName))
											peptideModifications.add(modificationName);
									}
								}
							}
						}
					}

				}
			}

		}
		if (peptideWithoutProteins > 0)
			log.warn("There are " + peptideWithoutProteins + " peptides without proteins");

		linkProteinsAndPeptides();
		// add the non filtered identified proteins and peptides after link
		// proteins and peptides!!
		identifiedProteins.addAll(nonFilteredIdentifiedProteins);
		identifiedPeptides.addAll(nonFilteredIdentifiedPeptides);
		log.info(identifiedPeptides.size() + " peptides and " + identifiedProteins.size()
				+ " proteins readed from MSIs");

		// In order to get ready all the data
		getIdentifiedProteinGroups();

		log.info("Miape MSIs processed. Num peptides:" + identifiedPeptides.size() + "   Num proteins:"
				+ identifiedProteins.size());

	}

	/**
	 * Construct the identification lists from Replicates.<br>
	 * In this case, the proteins are collected individually and a final protein
	 * grouping will be performed. {@link DataManager}
	 *
	 * @param dataManagers
	 */
	private void collectDataFromReplicates() {
		log.info("Collecting data from replicates at experiment level");
		// Set<Integer> proteinIDList = new HashSet<Integer>();
		// Set<Integer> peptideIDList = new HashSet<Integer>();

		for (DataManager dataManager : dataManagers) {

			// DATABASES
			Set<String> differentSearchedDatabases2 = dataManager.getDifferentSearchedDatabases();
			differentSearchedDatabases.addAll(differentSearchedDatabases2);

			// PROTEINS
			final List<ExtendedIdentifiedProtein> proteins = dataManager.getIdentifiedProteins();
			log.info(proteins.size() + " proteins in replicate: " + dataManager.idSet.getName());

			nonFilteredIdentifiedProteins.addAll(proteins);

			// PEPTIDES
			final List<ExtendedIdentifiedPeptide> peptides = dataManager.getIdentifiedPeptides();
			log.info(peptides.size() + " peptides in replicate: " + dataManager.idSet.getName());

			nonFilteredIdentifiedPeptides.addAll(peptides);

		}

		identifiedPeptides.clear();
		identifiedProteins.clear();
		identifiedProteins.addAll(nonFilteredIdentifiedProteins);
		identifiedPeptides.addAll(nonFilteredIdentifiedPeptides);

		// TODO
		// Change in 29Jan2013: Disable grouping, it will be made when
		// getIdentifiedProteinGroups is called.
		// PAnalyzer panalyzer = new PAnalyzer();
		// this.identifiedProteinGroups = panalyzer.run(identifiedProteins);

		log.info(identifiedProteins.size() + " proteins (not groups), " + identifiedPeptides.size()
				+ " peptides collected from " + dataManagers.size() + " replicates");

		// Get protein groups in order to getReady the data
		getIdentifiedProteinGroups();

	}

	/**
	 * Construct the identification lists from Experiments.<br>
	 * if groupingAtExperimentListLevel is false, the proteins are collected
	 * grouped and no Panlyzer is performed. {@link DataManager}
	 *
	 * @param groupingAtExperimentListLevel
	 *
	 * @param dataManagers
	 */
	private void collectDataFromExperiments(boolean groupingAtExperimentListLevel) {
		log.info("Processing next level id sets");
		this.groupingAtExperimentListLevel = groupingAtExperimentListLevel;
		for (DataManager dataManager : dataManagers) {

			// DATABASES
			Set<String> differentSearchedDatabases2 = dataManager.getDifferentSearchedDatabases();
			differentSearchedDatabases.addAll(differentSearchedDatabases2);

			if (!groupingAtExperimentListLevel) {
				// PROTEIN GROUPS
				final List<ProteinGroup> proteinGroups = dataManager.getIdentifiedProteinGroups();
				log.info(proteinGroups.size() + " proteinGroups in experiment " + dataManager.idSet.getName());

				for (ProteinGroup proteinGroup : proteinGroups) {
					// add to the non filtered identified protein list
					identifiedProteinGroups.add(proteinGroup);
					nonFilteredIdentifiedProteins.addAll(proteinGroup);
				}
			} else {
				// PROTEINS
				final List<ExtendedIdentifiedProtein> proteins = dataManager.getIdentifiedProteins();
				log.info(proteins.size() + " proteins in replicate: " + dataManager.idSet.getName());

				nonFilteredIdentifiedProteins.addAll(proteins);
			}
			log.info(nonFilteredIdentifiedProteins.size() + " proteins in experiment " + dataManager.idSet.getName());
			// PEPTIDES
			final List<ExtendedIdentifiedPeptide> peptides = dataManager.getIdentifiedPeptides();
			log.info(peptides.size() + " peptides in experiment " + dataManager.idSet.getName());

			nonFilteredIdentifiedPeptides.addAll(peptides);

		}

		identifiedProteins.addAll(nonFilteredIdentifiedProteins);
		identifiedPeptides.addAll(nonFilteredIdentifiedPeptides);
		log.info(identifiedProteinGroups.size() + " proteins groups collected from " + dataManagers.size()
				+ " experiments");

		// Get protein groups in order to getReady the data
		getIdentifiedProteinGroups();

	}

	/**
	 * Removes all data stored.
	 */
	private void resetIdentificationSet() {

		isDataReady = false;
		// peptides
		identifiedPeptides.clear();
		peptideOccurrenceList.clear();
		nonFilteredIdentifiedPeptides.clear();
		numDifferentPeptideDecoys = null;
		numPeptideDecoys = null;
		// proteins
		identifiedProteinGroups.clear();
		proteinGroupOccurrenceList.clear();
		nonFilteredIdentifiedProteins.clear();
		numDifferentProteinDecoys = null;
		numProteinGroupDecoys = null;
		numNonConclusiveGroups = null;
		numDifferentNonConclusiveGroups = null;
	}

	/**
	 * @return the proteinScores
	 */
	public List<String> getProteinScores() {
		return proteinScores;
	}

	/**
	 * @return the peptideScores
	 */
	public List<String> getPeptideScores() {
		return peptideScores;
	}

	public void setFilters(List<Filter> filters) {
		setFilters(filters, true);
	}

	public void setFilters(List<Filter> filters, boolean removeFiltersBefore) {
		if (removeFiltersBefore) {
			removeFilters();
		}

		if (filters != null && !filters.isEmpty()) {
			// log.info("Setting " + filters.size() + " filters to " +
			// idSet.getName());
		} else {
			log.info("Removing filters to " + idSet.getName());

			return;
		}
		List<Filter> filtersToSet = new ArrayList<Filter>();
		for (Filter filter : filters) {

			if (!(filter instanceof FDRFilter)) {
				filtersToSet.add(filter);
			} else {
				// If this is a FDRFilter
				FDRFilter fdrFilter = (FDRFilter) filter;
				// if the name of the replicate is not specified, it is applied
				// to all replicates
				if (fdrFilter.getReplicateName() == null)
					filtersToSet.add(fdrFilter);
				// If this is a FDRFilter
				else if (fdrFilter.getReplicateName().equals(getReplicateName())
						&& fdrFilter.getExperimentName().equals(getExperimentName()))
					filtersToSet.add(fdrFilter);

				// TODO check this from 14 Jan 2013
				// else if (this instanceof ExperimentListDataManager)
				// filtersToSet.add(fdrFilter);
			}
		}
		if (!filtersToSet.isEmpty()) {
			if (removeFiltersBefore) {
				this.filters.clear();
			}
			this.filters.addAll(filtersToSet);

			peptideOccurrenceList.clear();
			isDataReady = false;
			proteinGroupOccurrenceList.clear();
		}

	}

	public void removeFilters() {
		boolean callToGetProteins = false;
		if (!filters.isEmpty())
			callToGetProteins = true;

		filters.clear();
		peptideOccurrenceList.clear();
		proteinGroupOccurrenceList.clear();
		proteinFDR = null;
		peptideFDR = null;
		psmFDR = null;
		clearCachedData();

		// NEW IN 17 Jan 2013
		// call to get proteigroups in order to reset identifications to non
		// filtered
		if (callToGetProteins) {
			isDataReady = false;
			getIdentifiedProteinGroups();
		}
		// Set to dataReady=false
		isDataReady = false;
	}

	public List<Filter> getFilters() {
		return filters;
	}

	protected ModificationFilter getModificationFilter() {
		for (Filter filter : filters) {
			if (filter instanceof ModificationFilter)
				return (ModificationFilter) filter;
		}
		return null;
	}

	/**
	 * Gets the protein groups that are present in the next level (after apply
	 * if defined, some filters).<br>
	 * If this is a Replicate (has no next level idSets), it returns the
	 * nonFilteredIdentifiedProteinGroups
	 *
	 * @return
	 */
	private List<ProteinGroup> getNextLevelIdentifiedProteinGroups() {

		List<ProteinGroup> nextLevelProteinGroups = new ArrayList<ProteinGroup>();
		try {
			final List<IdentificationSet> nextLevelIdentificationSetList = idSet.getNextLevelIdentificationSetList();
			for (IdentificationSet identificationSet : nextLevelIdentificationSetList) {
				List<ProteinGroup> pgroups = identificationSet.getIdentifiedProteinGroups();
				nextLevelProteinGroups.addAll(pgroups);
			}
			return nextLevelProteinGroups;
		} catch (UnsupportedOperationException e) {
			// in case of not having next level, reset protein groups and return
			if (getFilters().isEmpty()) {
				resetPeptidesFromProteins(nonFilteredIdentifiedProteins);
				PAnalyzer panalyzer = new PAnalyzer(false);
				identifiedProteinGroups = panalyzer.run(nonFilteredIdentifiedProteins);
				return identifiedProteinGroups;
			}
			return getIdentifiedProteinGroups();

		}
	}

	private List<ExtendedIdentifiedProtein> getNextLevelIdentifiedProteins() {

		List<ExtendedIdentifiedProtein> nextLevelProteins = new ArrayList<ExtendedIdentifiedProtein>();
		try {
			final List<IdentificationSet> nextLevelIdentificationSetList = idSet.getNextLevelIdentificationSetList();
			for (IdentificationSet identificationSet : nextLevelIdentificationSetList) {
				List<ExtendedIdentifiedProtein> proteins = identificationSet.getIdentifiedProteins();
				nextLevelProteins.addAll(proteins);
			}
			return nextLevelProteins;

		} catch (UnsupportedOperationException e) {
			if (getFilters().isEmpty()) {
				resetPeptidesFromProteins(nonFilteredIdentifiedProteins);
				return nonFilteredIdentifiedProteins;
			}
			// if (!this.identifiedProteinGroups.isEmpty())
			return getProteinsFromProteinGroups(getIdentifiedProteinGroups());

		}
	}

	/**
	 * Gets the identified proteins in all replicates. If there is any filter
	 * defined, the returned proteins will be filtered.
	 *
	 * @return
	 */
	public List<ProteinGroup> getIdentifiedProteinGroups() {

		if (!isDataReady) {
			clearCachedData();
			proteinGroupOccurrenceList.clear();

			List<ProteinGroup> toFilter = null;
			if (this instanceof ExperimentListDataManager) {
				if (!groupingAtExperimentListLevel) {
					toFilter = getNextLevelIdentifiedProteinGroups();
				} else {
					if (idSet.getNextLevelDataManagers().size() > 1) {
						log.info("Making grouping at experiment list level");
						// gets the proteins from the
						// next lower level, and make the grouping again.
						// Get proteins from next level
						List<ExtendedIdentifiedProtein> nextLevelProteins = getNextLevelIdentifiedProteins();

						// Run panalyzer
						PAnalyzer panalyzer = new PAnalyzer(false);
						toFilter = panalyzer.run(nextLevelProteins);
					} else {
						// if there is only one replicate, get all directly
						toFilter = getNextLevelIdentifiedProteinGroups();
					}
				}
			} else if (this instanceof ExperimentDataManager) {
				if (idSet.getNextLevelDataManagers().size() > 1) {
					// For experiments, gets the proteins from the
					// next lower level, and make the grouping again.
					// Get proteins from next level
					List<ExtendedIdentifiedProtein> nextLevelProteins = getNextLevelIdentifiedProteins();

					// Run panalyzer
					PAnalyzer panalyzer = new PAnalyzer(false);
					toFilter = panalyzer.run(nextLevelProteins);
				} else {
					// if there is only one replicate, get all directly
					toFilter = getNextLevelIdentifiedProteinGroups();
				}
			} else if (this instanceof ReplicateDataManager) {
				toFilter = new ArrayList<ProteinGroup>();
				toFilter.addAll(getNonFilteredIdentifiedProteinGroups());
			}
			// Get filters
			List<Filter> filters = getFilters();
			if (filters != null && !filters.isEmpty()) {
				log.info("Filtering " + toFilter.size() + " protein groups");
			}
			identifiedProteinGroups = applyFilters(toFilter, filters);
			if (processInParallel) {
				identifiedPeptides = getPeptidesFromProteinGroupsInParallel(identifiedProteinGroups);
			} else {
				identifiedPeptides = getPeptidesFromProteinGroups(identifiedProteinGroups);
			}
			PanalyzerStats panalyzerStats = new PanalyzerStats(identifiedProteinGroups);
			numNonConclusiveGroups = panalyzerStats.nonConclusiveCount;
			numDifferentNonConclusiveGroups = panalyzerStats.differentNonConclusiveCount;

			// Set to the previousLevelIdentificationSet as non filtered
			try {
				IdentificationSet previousLevelIdentificationSet = idSet.getPreviousLevelIdentificationSet();
				if (previousLevelIdentificationSet != null)
					previousLevelIdentificationSet.setFiltered(false);
			} catch (UnsupportedOperationException e) {
				// do nothing
			}

			log.info(identifiedProteinGroups.size() + " protein groups in " + idSet.getName());
			proteinGroupOccurrenceList = DataManager.createProteinGroupOccurrenceList(identifiedProteinGroups);
			// Set this idSet as ready
			isDataReady = true;
			if (identifiedProteinGroups.size() > 5000)
				Runtime.getRuntime().gc();
		}

		return identifiedProteinGroups;
	}

	/**
	 * Gets the identified peptides. If there is any filter defined, the
	 * returned peptides will be filtered.
	 *
	 * @return Sorted list of peptides
	 */
	public List<ExtendedIdentifiedPeptide> getIdentifiedPeptides() {

		if (!isDataReady) {
			clearCachedData();
			peptideOccurrenceList.clear();

			final List<ProteinGroup> proteinGroups = getIdentifiedProteinGroups();

			if (processInParallel) {
				identifiedPeptides = getPeptidesFromProteinGroupsInParallel(proteinGroups);
			} else {
				identifiedPeptides = getPeptidesFromProteinGroups(proteinGroups);
			}
			// Set to the previousLevelIdentificationSet as non filtered
			try {
				IdentificationSet previousLevelIdentificationSet = idSet.getPreviousLevelIdentificationSet();
				if (previousLevelIdentificationSet != null)
					previousLevelIdentificationSet.setFiltered(false);
				else
					log.info("asdf");
			} catch (UnsupportedOperationException e) {
				// do nothing
			}
			// Set this idSet as filtered
			isDataReady = true;

			log.info(identifiedPeptides.size() + " peptides in " + idSet.getName());
		}

		return identifiedPeptides;

	}

	public List<ExtendedIdentifiedProtein> getIdentifiedProteins() {

		// if (!this.isDataReady) {
		// this.identifiedProteins.clear();
		// this.identifiedProteins.addAll(this
		// .getNextLevelIdentifiedProteins());
		//
		// log.info(identifiedProteins.size() + " proteins in "
		// + this.idSet.getName());
		// }
		// if (identifiedProteins == null || identifiedProteins.isEmpty())
		identifiedProteins = getProteinsFromProteinGroups(getIdentifiedProteinGroups());
		return identifiedProteins;
	}

	public List<ProteinGroup> getNonFilteredIdentifiedProteinGroups() {
		resetPeptidesFromProteins(nonFilteredIdentifiedProteins);

		PAnalyzer pAnalyzer = new PAnalyzer(false);
		List<ProteinGroup> groups = pAnalyzer.run(nonFilteredIdentifiedProteins);
		return groups;
	}

	public List<ExtendedIdentifiedPeptide> getNonFilteredIdentifiedPeptides() {
		// for (ExtendedIdentifiedPeptide peptide :
		// this.nonFilteredIdentifiedPeptides) {
		// peptide.setDecoy(false);
		// }
		return nonFilteredIdentifiedPeptides;
	}

	private void resetPeptidesFromProteins(List<ExtendedIdentifiedProtein> proteins) {
		// if (!this.isReseted) {
		log.info("RESETING PROTEINS");
		for (ExtendedIdentifiedProtein protein : proteins) {
			// if (protein.getPeptides().size() !=
			// protein.getIdentifiedPeptides()
			// .size())
			protein.setDecoy(false, false);
			protein.resetPeptides();
		}
		log.info("END RESETING PROTEINS");

	}

	private List<ProteinGroup> applyFilters(List<ProteinGroup> toFilter, List<Filter> filters) {
		List<ProteinGroup> ret = null;

		// if no filters, return the same set of proteins
		if (filters == null || filters.isEmpty()) {
			ret = new ArrayList<ProteinGroup>();
			ret.addAll(toFilter);
			return ret;
		}
		boolean aFilterIsApplied = false;
		// sort filters
		SorterUtil.sortFilters(filters);
		for (Filter filter : filters) {
			List<ProteinGroup> filteredProteinGroups = new ArrayList<ProteinGroup>();

			// use the occurrence filter just in case of being an experiment to
			// filter proteins that are not present in at least X replicates
			if (filter instanceof OccurrenceFilter && this instanceof ExperimentDataManager) {
				// filter
				if (ret == null) {
					filteredProteinGroups = filter.filter(toFilter, idSet);
				} else {
					filteredProteinGroups = filter.filter(ret, idSet);
				}
				aFilterIsApplied = true;
				if (ret == null)
					ret = new ArrayList<ProteinGroup>();
				ret.clear();
				ret.addAll(filteredProteinGroups);
				log.info(" (" + idSet.getName() + ") Passing from " + toFilter.size() + " to " + ret.size()
						+ " proteins");
				for (DataManager dataManager : dataManagers) {
					dataManager.setInclusionList(filteredProteinGroups);
					dataManager.setFiltered(false);
				}
			} else if (filter instanceof OccurrenceFilter && this instanceof ReplicateDataManager
					&& !((OccurrenceFilter) filter).isByReplicates()) {
				OccurrenceFilter occurrenceFilter = (OccurrenceFilter) filter;
				if (ret == null) {
					filteredProteinGroups = occurrenceFilter.filterProteins(toFilter, inclusionList);
				} else {
					filteredProteinGroups = occurrenceFilter.filterProteins(ret, inclusionList);
				}
				aFilterIsApplied = true;
				if (ret == null)
					ret = new ArrayList<ProteinGroup>();

				ret.clear();
				ret.addAll(filteredProteinGroups);
				log.info(" (" + idSet.getName() + ") Passing from " + toFilter.size() + " to " + ret.size()
						+ " proteins");
				// if it is not an occurrenceFilter
			} else if (!(filter instanceof OccurrenceFilter)) {
				boolean applyFilter = false;
				// if (this instanceof ExperimentListDataManager)
				// System.out.println("asdf");
				if (filter instanceof FDRFilter && this instanceof ReplicateDataManager) {
					FDRFilter fdrFilter = (FDRFilter) filter;
					if (fdrFilter.getReplicateName() == null)
						applyFilter = true;
					else if (fdrFilter.getReplicateName().equals(getReplicateName())
							&& fdrFilter.getExperimentName().equals(getExperimentName()))
						applyFilter = true;
				}
				// if it is a protein ACC filter, filter on Replicates
				if (filter instanceof ProteinACCFilter && this instanceof ReplicateDataManager)
					applyFilter = true;

				// if it is not a FDR filter
				if (!(filter instanceof FDRFilter) && !(filter instanceof ProteinACCFilter))
					applyFilter = true;

				log.info("Apply filter= " + applyFilter);
				if (applyFilter) {

					if (ret == null) {
						filteredProteinGroups = filter.filter(toFilter, idSet);
					} else {
						filteredProteinGroups = filter.filter(ret, idSet);
					}
					aFilterIsApplied = true;
					ret = filteredProteinGroups;

					log.info(" (" + idSet.getName() + ") Passing from " + toFilter.size() + " to " + ret.size()
							+ " proteins");
				}
			}
		}

		if (!aFilterIsApplied && (ret == null || ret.isEmpty()))
			return toFilter;

		return ret;

	}

	public void setFiltered(boolean b) {
		isDataReady = b;
	}

	private void setInclusionList(List<ProteinGroup> inclusionList) {
		if (this.inclusionList == null)
			this.inclusionList = new ArrayList<ProteinGroup>();
		this.inclusionList.clear();

		this.inclusionList.addAll(inclusionList);

	}

	/**
	 * Gets the total list of proteins identified, not considering any filter,
	 * sorted by score
	 *
	 * @return
	 */
	// public List<IdentifiedProtein> getNonFilteredIdentifiedProteins() {
	// List<IdentifiedProtein> ret = new ArrayList<IdentifiedProtein>();
	// try {
	// // if it is an experiment or an experiment list, the list of
	// // proteins returned is the concatenation of protein list returned
	// // by the call of getNonFilteredIdentifiedPRoteins in all children
	// // if it is a replicate, a nonFilteredIdentifiedProteins is
	// // returned.
	// final List<IdentificationSet> nextLevelIdentificationSetList = this.idSet
	// .getNextLevelIdentificationSetList();
	// for (IdentificationSet identificationSet :
	// nextLevelIdentificationSetList) {
	// ret.addAll(identificationSet.getNonFilteredIdentifiedProteins());
	// }
	// } catch (UnsupportedOperationException e) {
	// ret.addAll(this.nonFilteredIdentifiedProteins);
	// }
	// return ret;
	// }

	/**
	 * Gets the number of protein groups identified, taking into account the
	 * filter.
	 *
	 * @return
	 */
	public int getTotalNumProteinGroups(boolean countNonConclusiveProteins) {
		int size = getIdentifiedProteinGroups().size();
		if (!countNonConclusiveProteins)
			size -= getNumNonConclusiveProteinGroups();
		return size;
	}

	/**
	 * Gets the number of different proteins in the data set
	 *
	 * @return
	 */
	public int getNumDifferentProteinGroups(boolean countNonConclusiveProteins) {

		int proteinGroups = getProteinGroupOccurrenceList().size();

		int ret = proteinGroups;
		if (!countNonConclusiveProteins) {
			ret = ret - getNumDifferentNonConclusiveProteinGroups();
			log.debug(idSet.getName() + "---->proteinGroups  " + proteinGroups + "-"
					+ getNumDifferentNonConclusiveProteinGroups() + "=" + ret);
		} else {
			log.debug(idSet.getName() + "---->proteinGroups  " + proteinGroups + "=" + ret);
		}
		return ret;
	}

	public int getNumNonConclusiveProteinGroups() {
		if (numNonConclusiveGroups != null)
			return numNonConclusiveGroups;
		return 0;
	}

	public int getNumDifferentNonConclusiveProteinGroups() {
		if (numDifferentNonConclusiveGroups != null)
			return numDifferentNonConclusiveGroups;
		return 0;
	}

	/**
	 * Gets a List of {@link PeptideOccurrence} that indicates how many times
	 * has been identified each protein in the total number of replicates.
	 *
	 * @return
	 */
	public HashMap<String, ProteinGroupOccurrence> getProteinGroupOccurrenceList() {
		// log.info("Getting protein occurrence list from " +
		// this.idSet.getName());

		if (!proteinGroupOccurrenceList.isEmpty()) {
			return proteinGroupOccurrenceList;
		}
		// log.info("Creating protein occurrence list from " +
		// this.idSet.getName());
		getIdentifiedProteinGroups();

		return proteinGroupOccurrenceList;
	}

	/**
	 * Gets a the number of times that a protein group has been identified in a
	 * experiment
	 *
	 * @return
	 */
	public int getProteinGroupOccurrenceNumber(ProteinGroup proteinGroup) {
		final ProteinGroupOccurrence proteinGroupOccurrence = this.getProteinGroupOccurrence(proteinGroup);
		if (proteinGroupOccurrence != null)
			return proteinGroupOccurrence.getItemList().size();
		return 0;
	}

	public int getProteinGroupOccurrenceNumberByProteinGroupKey(String proteinGroupKey) {
		final ProteinGroupOccurrence proteinGroupOccurrence = this
				.getProteinGroupOccurrenceByProteinGroupKey(proteinGroupKey);
		if (proteinGroupOccurrence != null)
			return proteinGroupOccurrence.getItemList().size();
		return 0;
	}

	public ProteinGroupOccurrence getProteinGroupOccurrence(ProteinGroup proteinGroup) {
		final HashMap<String, ProteinGroupOccurrence> proteinOcurrenceList = getProteinGroupOccurrenceList();
		if (proteinOcurrenceList.containsKey(proteinGroup.getKey()))
			return proteinOcurrenceList.get(proteinGroup.getKey());
		return null;

	}

	public ProteinGroupOccurrence getProteinGroupOccurrenceByProteinGroupKey(String proteinGroupKey) {
		final HashMap<String, ProteinGroupOccurrence> proteinOcurrenceList = getProteinGroupOccurrenceList();
		if (proteinOcurrenceList.containsKey(proteinGroupKey))
			return proteinOcurrenceList.get(proteinGroupKey);
		return null;

	}

	public ProteinGroupOccurrence getProteinGroupOccurrence(String proteinACC) {
		final HashMap<String, ProteinGroupOccurrence> proteinOcurrenceList = getProteinGroupOccurrenceList();
		for (ProteinGroupOccurrence proteinGroupOccurrence : proteinOcurrenceList.values()) {
			if (proteinGroupOccurrence.getAccessions().contains(proteinACC)) {
				return proteinGroupOccurrence;
			}
		}
		return null;
	}

	public int getProteinGroupOccurrenceNumber(String proteinACC) {
		final HashMap<String, ProteinGroupOccurrence> proteinOcurrenceList = getProteinGroupOccurrenceList();
		for (ProteinGroupOccurrence proteinGroupOccurrence : proteinOcurrenceList.values()) {
			if (proteinGroupOccurrence.getAccessions().contains(proteinACC)) {
				return proteinGroupOccurrence.getItemList().size();
			}
		}
		return 0;
	}

	/**
	 * Gets the peptides that are present in the next level (after apply if
	 * defined, some filters)<br>
	 * If this is a Replicate (has no next level idSets), it returns the
	 * nonFilteredIdentifiedPeptides
	 *
	 *
	 * @return
	 */
	private List<ExtendedIdentifiedPeptide> getNextLevelIdentifiedPeptides() {
		List<ExtendedIdentifiedPeptide> ret = new ArrayList<ExtendedIdentifiedPeptide>();
		try {
			Set<Integer> peptideIDs = new HashSet<Integer>();
			final List<IdentificationSet> nextLevelIdentificationSetList = idSet.getNextLevelIdentificationSetList();
			for (IdentificationSet identificationSet : nextLevelIdentificationSetList) {
				List<ExtendedIdentifiedPeptide> peptides = null;

				peptides = identificationSet.getIdentifiedPeptides();

				if (peptides != null)
					for (ExtendedIdentifiedPeptide extendedIdentifiedPeptide : peptides) {
						if (!peptideIDs.contains(extendedIdentifiedPeptide.getId())) {
							ret.add(extendedIdentifiedPeptide);
							peptideIDs.add(extendedIdentifiedPeptide.getId());
						}
					}
			}
		} catch (UnsupportedOperationException e) {
			// in case of
			return nonFilteredIdentifiedPeptides;
		}

		return ret;
	}

	/**
	 * Gets a List of {@link PeptideOccurrence} that indicates how many times
	 * has been identified each protein in the total number of replicates.
	 *
	 * @return
	 */
	public HashMap<String, PeptideOccurrence> getPeptideOcurrenceList(boolean distinguishModPep) {

		// recalculate de occurrence list if the boolean distinguishModPep
		// changes
		final boolean b = (distinguishModificatedPeptides && distinguishModPep)
				|| (!distinguishModificatedPeptides && !distinguishModPep);
		// if it has changed, the peptide occurrent list has to be calculated
		// again
		if (!b)
			peptideOccurrenceList.clear();
		distinguishModificatedPeptides = distinguishModPep;
		if (!peptideOccurrenceList.isEmpty() && b) {
			return peptideOccurrenceList;
		}

		final List<ExtendedIdentifiedPeptide> peptides = getIdentifiedPeptides();

		log.debug("Creating peptide occurrence list(" + distinguishModPep + ") from " + peptides.size() + " peptides");
		peptideOccurrenceList = DataManager.createPeptideOccurrenceListInParallel(peptides, distinguishModPep);
		log.debug(peptideOccurrenceList.size() + " peptide occurrences");
		// sort occurrences
		// SorterUtil.sortPeptideOcurrences(ret);
		return peptideOccurrenceList;
	}

	public HashMap<String, PeptideOccurrence> getPeptideChargeOcurrenceList(boolean distinguishModPep) {
		final HashMap<String, PeptideOccurrence> peptideChargeOcurrenceList = getPeptideOcurrenceList(
				distinguishModPep);
		HashMap<String, PeptideOccurrence> ret = new HashMap<String, PeptideOccurrence>();
		for (PeptideOccurrence peptideOccurrence : peptideChargeOcurrenceList.values()) {
			final List<ExtendedIdentifiedPeptide> itemList = peptideOccurrence.getItemList();
			for (ExtendedIdentifiedPeptide peptide : itemList) {
				String sequenceChargeKey = peptide.getSequence();
				if (peptide.getCharge() != null) {
					sequenceChargeKey += "_" + peptide.getCharge();
				}
				if (!ret.containsKey(sequenceChargeKey)) {
					PeptideOccurrence newPeptideOccurrence = new PeptideOccurrence(sequenceChargeKey);
					newPeptideOccurrence.addOccurrence(peptide);
					ret.put(sequenceChargeKey, newPeptideOccurrence);
				} else {
					ret.get(sequenceChargeKey).addOccurrence(peptide);
				}
			}
		}
		return ret;
	}

	public static HashMap<String, PeptideOccurrence> createPeptideOccurrenceList(
			List<ExtendedIdentifiedPeptide> peptides, boolean distinguishModPep) {
		HashMap<String, PeptideOccurrence> ret = new HashMap<String, PeptideOccurrence>();

		for (ExtendedIdentifiedPeptide extPeptide : peptides) {
			if (extPeptide != null) {
				String key = extPeptide.getKey(distinguishModPep);
				// peptide occurrence
				if (ret.containsKey(key)) {
					ret.get(key).addOccurrence(extPeptide);
				} else {
					final PeptideOccurrence newIdentOccurrence = new PeptideOccurrence(key);
					newIdentOccurrence.addOccurrence(extPeptide);
					ret.put(key, newIdentOccurrence);
				}
			}
		}

		return ret;
	}

	public static HashMap<String, PeptideOccurrence> createPeptideOccurrenceListInParallel(
			List<ExtendedIdentifiedPeptide> peptides, final boolean distinguishModPep) {

		int threadCount = SystemCoreManager.getAvailableNumSystemCores(MAX_NUMBER_PARALLEL_PROCESSES);
		log.debug("Creating peptide occurrence list " + peptides.size() + " distinguish = " + distinguishModPep
				+ " using " + threadCount + " threads");
		ParIterator<ExtendedIdentifiedPeptide> iterator = ParIteratorFactory.createParIterator(peptides, threadCount,
				Schedule.GUIDED);
		Reducible<HashMap<String, PeptideOccurrence>> reduciblePeptideMap = new Reducible<HashMap<String, PeptideOccurrence>>();

		List<PeptideOcurrenceParallelCreator> runners = new ArrayList<PeptideOcurrenceParallelCreator>();
		for (int numCore = 0; numCore < threadCount; numCore++) {
			// take current DB session
			PeptideOcurrenceParallelCreator runner = new PeptideOcurrenceParallelCreator(iterator, numCore,
					distinguishModPep, reduciblePeptideMap);
			runners.add(runner);
			runner.start();
		}
		if (iterator.getAllExceptions().length > 0) {
			throw new IllegalArgumentException(iterator.getAllExceptions()[0].getException());
		}
		// Main thread waits for worker threads to complete
		for (int k = 0; k < threadCount; k++) {
			try {
				runners.get(k).join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		// Reductors
		Reduction<HashMap<String, PeptideOccurrence>> peptideListReduction = new Reduction<HashMap<String, PeptideOccurrence>>() {
			@Override
			public HashMap<String, PeptideOccurrence> reduce(HashMap<String, PeptideOccurrence> first,
					HashMap<String, PeptideOccurrence> second) {
				HashMap<String, PeptideOccurrence> peptideOccurrences = new HashMap<String, PeptideOccurrence>();
				for (String key : first.keySet()) {
					if (peptideOccurrences.containsKey(key)) {
						final PeptideOccurrence peptideOccurrenceFirst = first.get(key);
						for (ExtendedIdentifiedPeptide expPeptide : peptideOccurrenceFirst.getItemList()) {
							peptideOccurrences.get(key).addOccurrence(expPeptide);
						}

					} else {
						peptideOccurrences.put(key, first.get(key));
					}

				}
				for (String key : second.keySet()) {
					if (peptideOccurrences.containsKey(key)) {
						final PeptideOccurrence peptideOccurrenceSecond = second.get(key);
						for (ExtendedIdentifiedPeptide expPeptide : peptideOccurrenceSecond.getItemList()) {
							peptideOccurrences.get(key).addOccurrence(expPeptide);
						}

					} else {
						peptideOccurrences.put(key, second.get(key));
					}

				}
				return peptideOccurrences;
			}
		};

		final HashMap<String, PeptideOccurrence> mergedPeptideOccurrences = reduciblePeptideMap
				.reduce(peptideListReduction);
		log.debug(mergedPeptideOccurrences.size() + " peptides occurrences from " + peptides.size() + " peptides");

		return mergedPeptideOccurrences;

	}

	public static HashMap<String, ProteinOccurrence> createProteinOccurrenceList(
			List<ExtendedIdentifiedProtein> proteins) {
		HashMap<String, ProteinOccurrence> ret = new HashMap<String, ProteinOccurrence>();

		for (ExtendedIdentifiedProtein protein : proteins) {
			if (protein != null) {
				String key = protein.getAccession();
				// peptide occurrence
				if (ret.containsKey(key)) {
					ret.get(key).addOccurrence(protein);
				} else {
					final ProteinOccurrence newIdentOccurrence = new ProteinOccurrence(key);
					newIdentOccurrence.addOccurrence(protein);
					ret.put(key, newIdentOccurrence);
				}
			}
		}

		return ret;
	}

	public static HashMap<String, ProteinGroupOccurrence> createProteinGroupOccurrenceList(
			List<ProteinGroup> proteinGroups) {
		// return
		// createProteinGroupOccurrenceListWithRelaxedComparison(proteinGroups);
		// return
		// createProteinGroupOccurrenceListWithStrictComparisonInParallel(proteinGroups);
		return createProteinGroupOccurrenceListWithStrictComparison(proteinGroups);

	}

	/**
	 * This is used when two groups are equals if share all proteins.<br>
	 * <b>Note that method "equals" in {@link ProteinGroup} class should return:
	 * <br>
	 * 'return shareAllProteins(object);'</b>.<br>
	 * Otherwise it will not work properly.
	 *
	 * @param proteinGroups
	 * @return
	 */
	public static HashMap<String, ProteinGroupOccurrence> createProteinGroupOccurrenceListWithStrictComparison(
			List<ProteinGroup> proteinGroups) {
		log.debug("Creating protein group occurrences from " + proteinGroups.size() + " protein groups");
		HashMap<String, ProteinGroupOccurrence> differentProteinGroups = new HashMap<String, ProteinGroupOccurrence>();

		for (ProteinGroup proteinGroup : proteinGroups) {
			if (differentProteinGroups.containsKey(proteinGroup.getKey())) {
				ProteinGroupOccurrence proteinGroupOccurrence = differentProteinGroups.get(proteinGroup.getKey());

				proteinGroupOccurrence.addOccurrence(proteinGroup);
			} else {

				final ProteinGroupOccurrence proteinGroupOccurrence = new ProteinGroupOccurrence();
				proteinGroupOccurrence.addOccurrence(proteinGroup);
				differentProteinGroups.put(proteinGroup.getKey(), proteinGroupOccurrence);
			}

		}
		if (differentProteinGroups.size() == 0)
			log.info("stop here");
		log.debug(differentProteinGroups.size() + " protein group occurrences from " + proteinGroups.size()
				+ " protein groups");

		return differentProteinGroups;
	}

	public static HashMap<String, ProteinGroupOccurrence> createProteinGroupOccurrenceListWithStrictComparisonInParallel(
			List<ProteinGroup> proteinGroups) {
		int threadCount = SystemCoreManager.getAvailableNumSystemCores(MAX_NUMBER_PARALLEL_PROCESSES);
		log.debug("Creating protein group occurrences from " + proteinGroups.size() + " protein groups using "
				+ threadCount + " threads");
		ParIterator<ProteinGroup> iterator = ParIteratorFactory.createParIterator(proteinGroups, threadCount,
				Schedule.GUIDED);
		Reducible<HashMap<String, ProteinGroupOccurrence>> reducibleProteinGroupMap = new Reducible<HashMap<String, ProteinGroupOccurrence>>();

		List<ProteinGroupOcurrenceParallelCreator> runners = new ArrayList<ProteinGroupOcurrenceParallelCreator>();
		for (int numCore = 0; numCore < threadCount; numCore++) {
			// take current DB session
			ProteinGroupOcurrenceParallelCreator runner = new ProteinGroupOcurrenceParallelCreator(iterator,
					reducibleProteinGroupMap);
			runners.add(runner);
			runner.start();
		}

		// Main thread waits for worker threads to complete
		for (int k = 0; k < threadCount; k++) {
			try {
				runners.get(k).join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if (iterator.getAllExceptions().length > 0) {
			throw new IllegalArgumentException(iterator.getAllExceptions()[0].getException());
		}
		// Reductors
		Reduction<HashMap<String, ProteinGroupOccurrence>> proteinGroupListReduction = new Reduction<HashMap<String, ProteinGroupOccurrence>>() {
			@Override
			public HashMap<String, ProteinGroupOccurrence> reduce(HashMap<String, ProteinGroupOccurrence> first,
					HashMap<String, ProteinGroupOccurrence> second) {
				HashMap<String, ProteinGroupOccurrence> proteinGroupOccurrences = new HashMap<String, ProteinGroupOccurrence>();
				for (String key : first.keySet()) {
					final ProteinGroupOccurrence proteinGroupOccurrenceFirst = first.get(key);
					if (proteinGroupOccurrences.containsKey(key)) {
						final ProteinGroupOccurrence proteinGroupOccurrence = proteinGroupOccurrences.get(key);
						for (ProteinGroup proteinGroup : proteinGroupOccurrenceFirst.getItemList()) {
							proteinGroupOccurrence.addOccurrence(proteinGroup);
						}
					} else {
						proteinGroupOccurrences.put(key, proteinGroupOccurrenceFirst);
					}
				}
				for (String key : second.keySet()) {
					final ProteinGroupOccurrence proteinGroupOccurrencesSecond = second.get(key);
					if (proteinGroupOccurrences.containsKey(key)) {
						final ProteinGroupOccurrence proteinGroupOccurrence = proteinGroupOccurrences.get(key);
						for (ProteinGroup proteinGroup : proteinGroupOccurrencesSecond.getItemList()) {
							proteinGroupOccurrence.addOccurrence(proteinGroup);
						}

					} else {
						proteinGroupOccurrences.put(key, proteinGroupOccurrencesSecond);
					}
				}
				return proteinGroupOccurrences;
			}
		};

		final HashMap<String, ProteinGroupOccurrence> mergedPeptideOccurrences = reducibleProteinGroupMap
				.reduce(proteinGroupListReduction);
		log.debug(mergedPeptideOccurrences.size() + " protein group occurrences from " + proteinGroups.size()
				+ " protein groups");
		return mergedPeptideOccurrences;
	}

	/**
	 * This is used when two groups are equals if at least share one protein *
	 * <b>Note that method "equals" in {@link ProteinGroup} class should return:
	 * <br>
	 * 'return shareOneProtein(object);'</b>.<br>
	 * Otherwise it will not work properly.
	 *
	 * @param proteinGroups
	 * @return
	 */
	// public static List<ProteinGroupOccurrence>
	// createProteinGroupOccurrenceListWithRelaxedComparison(
	// List<ProteinGroup> proteinGroups) {
	// log.info("Creating protein group occurrences from "
	// + proteinGroups.size() + " protein groups");
	//
	// // protein acc: proteingocc
	// HashMap<String, ProteinGroupOccurrence>
	// proteinToProteinGroupOccurrenceMapping = new HashMap<String,
	// ProteinGroupOccurrence>();
	//
	// for (ProteinGroup proteinGroup : proteinGroups) {
	// ProteinGroupOccurrence pgo = null;
	// for (ExtendedIdentifiedProtein protein : proteinGroup) {
	// if (proteinToProteinGroupOccurrenceMapping.containsKey(protein
	// .getAccession())) {
	// pgo = proteinToProteinGroupOccurrenceMapping.get(protein
	// .getAccession());
	// break;
	// }
	// }
	// if (pgo != null) {
	// try {
	// pgo.addOccurrence(proteinGroup);
	// // log.info(proteinGroup);
	// // log.info(pgo);
	// // log.info("ASDf");
	// } catch (IllegalMiapeArgumentException e) {
	// // This exception is thrown just if the proteinGroup cannot
	// // be added to the protein group occurrence (see equals
	// // implementation in ProteinGroup class).
	// log.debug(e.getMessage());
	// pgo = null;
	// }
	//
	// }
	// if (pgo == null) {
	// final ProteinGroupOccurrence newPgo = new ProteinGroupOccurrence();
	// try {
	// newPgo.addOccurrence(proteinGroup);
	// // map to all protein accs
	// for (ExtendedIdentifiedProtein protein : proteinGroup) {
	// proteinToProteinGroupOccurrenceMapping.put(
	// protein.getAccession(), newPgo);
	// }
	// } catch (IllegalMiapeArgumentException e) {
	// log.info(e.getMessage());
	//
	// }
	// }
	// }
	// // store all protein groups occurrences in a set
	// Set<ProteinGroupOccurrence> pgoSet = new
	// HashSet<ProteinGroupOccurrence>();
	// for (ProteinGroupOccurrence proteinGroupOccurrence :
	// proteinToProteinGroupOccurrenceMapping
	// .values()) {
	// // if (!pgoList.contains(proteinGroupOccurrence))
	// pgoSet.add(proteinGroupOccurrence);
	// }
	//
	// if (pgoSet.size() != proteinToProteinGroupOccurrenceMapping.size())
	// log.info("No son iguales...bien");
	// else
	// log.warn("Malll....");
	//
	// log.info(pgoSet.size() + " protein group occurrences from "
	// + proteinGroups.size() + " protein groups");
	//
	// List<ProteinGroupOccurrence> list = new
	// ArrayList<ProteinGroupOccurrence>();
	// for (ProteinGroupOccurrence proteinGroupOccurrence : pgoSet) {
	// list.add(proteinGroupOccurrence);
	// }
	// return list;
	// }

	// public static List<ProteinGroupOccurrence>
	// createProteinGroupOccurrenceListWithRelaxedComparison(
	// List<ProteinGroup> proteinGroups) {
	// log.info("Creating protein group occurrences from "
	// + proteinGroups.size() + " protein groups");
	// List<ProteinGroupOccurrence> ret = new
	// ArrayList<ProteinGroupOccurrence>();
	//
	// for (ProteinGroup proteinGroup : proteinGroups) {
	// ProteinGroupOccurrence proteinGroupOccurrenceFound = null;
	// for (ProteinGroupOccurrence proteinGroupOccurrence : ret) {
	// for (ProteinGroup proteinGroup2 : proteinGroupOccurrence
	// .getItemList()) {
	// if (proteinGroup.equals(proteinGroup2))
	// proteinGroupOccurrenceFound = proteinGroupOccurrence;
	// break;
	// }
	// if (proteinGroupOccurrenceFound != null)
	// break;
	// }
	// if (proteinGroupOccurrenceFound != null) {
	// proteinGroupOccurrenceFound.addOccurrence(proteinGroup);
	// } else {
	// ProteinGroupOccurrence pgo = new ProteinGroupOccurrence();
	// pgo.addOccurrence(proteinGroup);
	// ret.add(pgo);
	// }
	// }
	//
	// log.info(ret.size() + " protein group occurrences from "
	// + proteinGroups.size() + " protein groups");
	//
	// return ret;
	// }

	/**
	 * Gets a the number of times that a peptide has been identified in a
	 * experiment
	 *
	 * @param distModPep
	 *
	 * @return
	 */
	public int getPeptideOccurrenceNumber(String sequence, boolean distModPep) {
		final PeptideOccurrence peptideOccurrence = getPeptideOccurrence(sequence, distModPep);
		if (peptideOccurrence != null)
			return peptideOccurrence.getItemList().size();
		return 0;
	}

	public PeptideOccurrence getPeptideOccurrence(String sequence, boolean distModPep) {
		final HashMap<String, PeptideOccurrence> peptideOcurrenceList = getPeptideOcurrenceList(distModPep);
		return peptideOcurrenceList.get(sequence);
	}

	public PeptideOccurrence getPeptideChargeOccurrence(String sequencePlusChargeKey, boolean distModPep) {
		final HashMap<String, PeptideOccurrence> peptideChargeOcurrenceList = getPeptideChargeOcurrenceList(distModPep);
		return peptideChargeOcurrenceList.get(sequencePlusChargeKey);
	}

	/**
	 * Gets a the number of times that a peptide has been identified in a
	 * experiment distinguising by charge
	 *
	 * @param distModPep
	 *
	 * @return
	 */
	public int getPeptideChargeOccurrenceNumber(String sequencePlusChargeKey, boolean distModPep) {
		final PeptideOccurrence peptideOccurrence = getPeptideChargeOccurrence(sequencePlusChargeKey, distModPep);
		if (peptideOccurrence != null)
			return peptideOccurrence.getItemList().size();
		return 0;
	}

	// END GETTERS AND SETTERS
	/**
	 * State if the experiment has a certain protein or not
	 *
	 * @return
	 */
	public boolean hasProteinGroup(ProteinGroup proteinGroup) {
		final int occurrence = this.getProteinGroupOccurrenceNumber(proteinGroup);
		if (occurrence > 0)
			return true;
		return false;
	}

	/**
	 * State if the experiment has a certain peptide or not
	 *
	 * @return
	 */
	public boolean hasPeptide(String sequence, boolean distModPep) {
		final int occurrence = getPeptideOccurrenceNumber(sequence, distModPep);
		if (occurrence > 0)
			return true;
		return false;
	}

	// BEGIN GETTERS AND SETTERS
	/**
	 * @return the distiguishModificatedPeptides
	 */
	public boolean isDistiguishModificatedPeptides() {
		return distinguishModificatedPeptides;
	}

	public int getNumDifferentPeptides(boolean distiguishModificatedPeptides) {
		int numDifPeptides = getPeptideOcurrenceList(distiguishModificatedPeptides).size();

		int total = numDifPeptides;

		log.debug(idSet.getName() + "---->Peptides  " + numDifPeptides + " = " + total);
		return total;
	}

	/**
	 * @return the peptideModifications
	 */
	public List<String> getDifferentPeptideModificationNames() {
		if (peptideModifications != null && !peptideModifications.isEmpty())
			return peptideModifications;
		peptideModifications.clear();
		final List<ExtendedIdentifiedPeptide> peptides = getIdentifiedPeptides();
		for (ExtendedIdentifiedPeptide extendedIdentifiedPeptide : peptides) {
			if (extendedIdentifiedPeptide.getModifications() != null) {
				for (PeptideModification modification : extendedIdentifiedPeptide.getModifications()) {
					if (!peptideModifications.contains(modification.getName()))
						peptideModifications.add(modification.getName());
				}
			}
		}
		return peptideModifications;
	}

	public int getModificatedSiteOccurrence(String modif) {
		int count = 0;
		if (modif == null || "".equals(modif))
			return 0;
		final List<ExtendedIdentifiedPeptide> peptides = getIdentifiedPeptides();
		for (ExtendedIdentifiedPeptide extendedIdentifiedPeptide : peptides) {
			final Set<PeptideModification> modifications = extendedIdentifiedPeptide.getModifications();
			if (modifications != null) {
				for (PeptideModification peptideModification : modifications) {
					if (modif.equals(peptideModification.getName()))
						count++;
				}
			}
		}
		return count;
	}

	public HashMap<Integer, Integer> getModificationOccurrenceDistribution(String modif) {

		HashMap<Integer, Integer> ret = new HashMap<Integer, Integer>();
		final HashMap<String, PeptideOccurrence> peptideOccurrences = getPeptideOcurrenceList(true);
		for (PeptideOccurrence peptideOccurrence : peptideOccurrences.values()) {
			final Set<PeptideModification> peptideModifications = peptideOccurrence.getFirstOccurrence()
					.getModifications();
			int count = 0;
			if (peptideModifications != null) {
				if (modif != null) {
					for (PeptideModification peptideModification : peptideModifications) {
						if (modif.equals(peptideModification.getName())) {
							count++;
						}
					}
				}
			}
			if (count > 0) {
				if (ret.containsKey(count)) {
					Integer integer = ret.get(count);
					integer++;
					ret.remove(count);
					ret.put(count, integer);
				} else {
					ret.put(count, new Integer(1));
				}
			}
		}
		return ret;
	}

	public int getPeptideModificatedOccurrence(String modification) {
		int count = 0;
		if (modification == null || "".equals(modification))
			return 0;
		final Collection<PeptideOccurrence> peptideOccurrences = getPeptideOcurrenceList(true).values();
		for (PeptideOccurrence peptideOccurrence : peptideOccurrences) {
			final ExtendedIdentifiedPeptide firstIdentificationOccurrence = peptideOccurrence.getFirstOccurrence();
			final Set<PeptideModification> modifications = firstIdentificationOccurrence.getModifications();
			if (modifications != null) {
				for (PeptideModification peptideModification : modifications) {
					if (modification.equals(peptideModification.getName())) {
						count++;
						break;
					}
				}
			}
		}
		return count;
	}

	public HashMap<Integer, Integer> getMissedCleavagesOccurrenceDistribution() {

		HashMap<Integer, Integer> ret = new HashMap<Integer, Integer>();
		final List<ExtendedIdentifiedPeptide> peptides = getIdentifiedPeptides();
		for (ExtendedIdentifiedPeptide peptide : peptides) {
			int missedCleavages = peptide.getNumMissedcleavages();

			if (ret.containsKey(missedCleavages)) {
				Integer integer = ret.get(missedCleavages);
				integer++;
				ret.remove(missedCleavages);
				ret.put(missedCleavages, integer);
			} else {
				ret.put(missedCleavages, new Integer(1));
			}
		}

		return ret;
	}

	public FDRFilter getFDRFilter() {
		if (this instanceof ReplicateDataManager) {
			if (filters != null) {
				for (Filter filter : filters) {
					if (filter instanceof FDRFilter)
						return (FDRFilter) filter;
				}
			}
		} else if (this instanceof ExperimentDataManager || this instanceof ExperimentListDataManager) {
			if (dataManagers != null) {
				FDRFilter commonFdrFilter = null;
				for (DataManager datamanager : dataManagers) {
					FDRFilter fdrFilter = datamanager.getFDRFilter();
					if (fdrFilter != null) {
						if (commonFdrFilter == null) {
							commonFdrFilter = fdrFilter;
						} else {
							if (commonFdrFilter.getThreshold() != fdrFilter.getThreshold())
								return null;
							if (!commonFdrFilter.getSortingParameters().getScoreName()
									.equals(fdrFilter.getSortingParameters().getScoreName())) {
								return null;
							}
							if (commonFdrFilter.getSortingParameters().getOrder() != fdrFilter.getSortingParameters()
									.getOrder()) {
								return null;
							}
						}
					}

				}
				return commonFdrFilter;
			}

		}

		return null;
	}

	/**
	 * Gets the number of True Positives
	 *
	 * @param positiveProteinAccessions
	 * @return
	 */
	public int getProteinGroupTP(HashSet<String> truePositiveProteinACCs, boolean countNonConclusiveProteins) {
		List<ProteinGroup> positiveProteinGroups = getIdentifiedProteinGroups();
		int TP = 0;
		for (ProteinGroup proteinGroup : positiveProteinGroups) {
			if (proteinGroup.getEvidence() == ProteinEvidence.NONCONCLUSIVE && !countNonConclusiveProteins)
				continue;
			for (ExtendedIdentifiedProtein extendedIdentifiedProtein : proteinGroup) {
				if (truePositiveProteinACCs.contains(extendedIdentifiedProtein.getAccession())) {
					TP++;
					break;
				}
			}
		}
		return TP;
	}

	public int getProteinGroupFN(HashSet<String> truePositiveProteinACCs, boolean countNonConclusiveProteins) {
		List<ProteinGroup> allProteinGroups = getNonFilteredIdentifiedProteinGroups();
		List<ProteinGroup> positiveProteinGroups = getIdentifiedProteinGroups();

		// if (getPeptideFDRFilter() == null)
		// throw new IllegalMiapeArgumentException(
		// "False Negatives cannot be calculated. A threshold is needed to
		// determine which proteins have been considered as positives after the
		// filter");
		int FN = 0;
		for (ProteinGroup proteinGroup : allProteinGroups) {
			if (proteinGroup.getEvidence() == ProteinEvidence.NONCONCLUSIVE && !countNonConclusiveProteins)
				continue;
			boolean isConsideredAsPositiveProteinGroup = false;
			for (ProteinGroup positiveProtein : positiveProteinGroups) {
				if (positiveProtein.shareOneProtein(proteinGroup)) {
					isConsideredAsPositiveProteinGroup = true;
					break;
				}
			}
			if (!isConsideredAsPositiveProteinGroup) {
				for (ExtendedIdentifiedProtein extendedIdentifiedProtein : proteinGroup) {
					if (truePositiveProteinACCs.contains(extendedIdentifiedProtein.getAccession())) {
						FN++;
						break;
					}
				}
			}
		}
		return FN;
	}

	public int getProteinGroupTN(HashSet<String> truePositiveProteinACCs, boolean countNonConclusiveProteins) {
		List<ProteinGroup> allProteinGroups = getNonFilteredIdentifiedProteinGroups();
		List<ProteinGroup> positiveProteinGroups = getIdentifiedProteinGroups();

		// if (getPeptideFDRFilter() == null)
		// throw new IllegalMiapeArgumentException(
		// "True Negatives cannot be calculated. A threshold is needed to
		// determine which proteins have been considered as positives after the
		// filter");
		int TN = 0;
		for (ProteinGroup proteinGroup : allProteinGroups) {
			if (proteinGroup.getEvidence() == ProteinEvidence.NONCONCLUSIVE && !countNonConclusiveProteins)
				continue;
			boolean isConsideredAsPositiveProteinGroup = false;
			for (ProteinGroup positiveProteinGroup : positiveProteinGroups) {
				if (positiveProteinGroup.shareOneProtein(proteinGroup)) {
					isConsideredAsPositiveProteinGroup = true;
					break;
				}
			}
			if (!isConsideredAsPositiveProteinGroup) {
				boolean found = false;
				for (ExtendedIdentifiedProtein extendedIdentifiedProtein : proteinGroup) {
					if (truePositiveProteinACCs.contains(extendedIdentifiedProtein.getAccession())) {
						found = true;
						break;
					}
				}
				if (!found)
					TN++;
			}
		}
		return TN;
	}

	public int getProteinGroupFP(HashSet<String> truePositiveProteinACCs, boolean countNonConclusiveProteins) {
		List<ProteinGroup> positiveProteinGroups = getIdentifiedProteinGroups();
		int FP = 0;
		for (ProteinGroup proteinGroup : positiveProteinGroups) {
			if (proteinGroup.getEvidence() == ProteinEvidence.NONCONCLUSIVE && !countNonConclusiveProteins)
				continue;
			boolean found = false;
			for (ExtendedIdentifiedProtein extendedIdentifiedProtein : proteinGroup) {
				if (truePositiveProteinACCs.contains(extendedIdentifiedProtein.getAccession())) {
					found = true;
					break;
				}
			}
			if (!found)
				FP++;
		}
		return FP;
	}

	public int getPeptideTP(HashSet<String> truePositivePeptideSequences, boolean distinguishModificatedPeptides) {
		List<ExtendedIdentifiedPeptide> positivePeptides = getIdentifiedPeptides();
		int TP = 0;
		for (ExtendedIdentifiedPeptide identifiedPeptide : positivePeptides) {
			if (!distinguishModificatedPeptides
					&& truePositivePeptideSequences.contains(identifiedPeptide.getSequence()))
				TP++;
			else if (distinguishModificatedPeptides
					&& truePositivePeptideSequences.contains(identifiedPeptide.getModificationString()))
				TP++;
		}
		return TP;
	}

	public int getPeptideFN(HashSet<String> truePositivePeptideSequences, boolean distinguishModificatedPeptides) {
		List<ExtendedIdentifiedPeptide> allPeptides = getNonFilteredIdentifiedPeptides();
		List<ExtendedIdentifiedPeptide> positivePeptides = getIdentifiedPeptides();

		// if (getPeptideFDRFilter() == null)
		// throw new IllegalMiapeArgumentException(
		// "False Negatives cannot be calculated. A threshold is needed to
		// determine which proteins have been considered as positives after the
		// filter");
		int FN = 0;
		for (ExtendedIdentifiedPeptide identifiedPeptide : allPeptides) {
			boolean isConsideredAsPositivePeptide = false;
			for (ExtendedIdentifiedPeptide positivePeptide : positivePeptides) {
				if (!distinguishModificatedPeptides
						&& positivePeptide.getSequence().equals(identifiedPeptide.getSequence())) {
					isConsideredAsPositivePeptide = true;
					continue;
				} else if (distinguishModificatedPeptides
						&& positivePeptide.getModificationString().equals(identifiedPeptide.getModificationString())) {
					isConsideredAsPositivePeptide = true;
					continue;
				}
			}
			if (!isConsideredAsPositivePeptide) {
				if (!distinguishModificatedPeptides
						&& truePositivePeptideSequences.contains(identifiedPeptide.getSequence()))
					FN++;
				else if (distinguishModificatedPeptides
						&& truePositivePeptideSequences.contains(identifiedPeptide.getModificationString()))
					FN++;
			}

		}
		return FN;
	}

	public int getPeptideTN(HashSet<String> truePositivePeptideSequences, boolean distinguishModificatedPeptides) {
		List<ExtendedIdentifiedPeptide> allPeptides = getNonFilteredIdentifiedPeptides();
		List<ExtendedIdentifiedPeptide> positivePeptides = getIdentifiedPeptides();

		// if (getPeptideFDRFilter() == null)
		// throw new IllegalMiapeArgumentException(
		// "True Negatives cannot be calculated. A threshold is needed to
		// determine which proteins have been considered as positives after the
		// filter");
		int TN = 0;
		for (ExtendedIdentifiedPeptide identifiedPeptide : allPeptides) {
			boolean isConsideredAsPositivePeptide = false;
			for (ExtendedIdentifiedPeptide positivePeptide : positivePeptides) {
				if (distinguishModificatedPeptides
						&& positivePeptide.getSequence().equals(identifiedPeptide.getSequence()))
					isConsideredAsPositivePeptide = true;
				else if (!distinguishModificatedPeptides
						&& positivePeptide.getModificationString().equals(identifiedPeptide.getModificationString()))
					isConsideredAsPositivePeptide = true;
			}
			if (!isConsideredAsPositivePeptide) {
				if (!distinguishModificatedPeptides
						&& !truePositivePeptideSequences.contains(identifiedPeptide.getSequence()))
					TN++;
				else if (distinguishModificatedPeptides
						&& !truePositivePeptideSequences.contains(identifiedPeptide.getModificationString()))
					TN++;
			}
		}
		return TN;
	}

	public int getPeptideFP(HashSet<String> truePositivePeptideSequences, boolean distinguishModificatedPeptides) {
		List<ExtendedIdentifiedPeptide> positivePeptides = getIdentifiedPeptides();
		int FP = 0;
		for (ExtendedIdentifiedPeptide identifiedPeptide : positivePeptides) {
			if (!distinguishModificatedPeptides
					&& !truePositivePeptideSequences.contains(identifiedPeptide.getSequence()))
				FP++;
			else if (distinguishModificatedPeptides
					&& !truePositivePeptideSequences.contains(identifiedPeptide.getModificationString()))
				FP++;
		}
		return FP;
	}

	public float getProteinFDR(String scoreName) {
		if (proteinFDR != null)
			return proteinFDR;
		FDRFilter fdrFilter = getFDRFilter();
		if (fdrFilter == null)
			throw new IllegalMiapeArgumentException("FDR filter is not defined");

		Collection<ProteinGroupOccurrence> proteinGroupOccurrencesSet = getProteinGroupOccurrenceList().values();
		List<ProteinGroupOccurrence> proteinGroupOccurrences = new ArrayList<ProteinGroupOccurrence>();
		for (ProteinGroupOccurrence proteinGroupOccurrence : proteinGroupOccurrencesSet) {
			proteinGroupOccurrences.add(proteinGroupOccurrence);
		}
		// Sort proteins by best peptide score
		SorterUtil.sortProteinGroupOcurrencesByPeptideScore(proteinGroupOccurrences, scoreName);

		long numFWHits = 0; // forward hits
		long numDCHits = 0; // decoy hits
		new ArrayList<ProteinGroup>();
		// - 4: for each peptide, look to its protein and count if it is
		// decoy or not. Ignore it if already has been seen
		for (ProteinGroupOccurrence proteinGroupOccurrence : proteinGroupOccurrences) {
			Float bestPeptideScore = proteinGroupOccurrence.getBestPeptideScore(scoreName);
			if (bestPeptideScore != null) {
				// for (ProteinGroup proteinGroup :
				// proteinGroupOccurrence.getItemList()) {

				// if (!proteinGroupList.contains(proteinGroupOccurrence)) {
				// log.info("protein: " + proteinACC);
				// proteinGroupList.add(proteinGroupOccurrence);

				if (proteinGroupOccurrence.getEvidence() != ProteinEvidence.NONCONCLUSIVE) {

					if (proteinGroupOccurrence.isDecoy(fdrFilter)) {
						numDCHits++;
					} else {
						numFWHits++;
					}
				}
			}
			// }
			// }
		}
		float totalFDR = fdrFilter.calculateFDR(numFWHits, numDCHits);
		proteinFDR = totalFDR;
		log.info("Protein FDR calculated: " + totalFDR);
		return totalFDR;
	}

	public float getPeptideFDR(String scoreName) {
		if (peptideFDR != null)
			return peptideFDR;

		FDRFilter fdrFilter = getFDRFilter();
		if (fdrFilter == null)
			throw new IllegalMiapeArgumentException("FDR filter is not defined");
		// - 2: remove peptide redundancy, taking the best PSM for each
		// sequence

		Collection<PeptideOccurrence> peptideOccurrenceCollection = getPeptideOcurrenceList(false).values();
		List<PeptideOccurrence> peptideOccurrenceList = new ArrayList<PeptideOccurrence>();
		for (PeptideOccurrence identificationOccurrence : peptideOccurrenceCollection) {
			peptideOccurrenceList.add(identificationOccurrence);
		}

		// - 3: sort peptides by score

		log.info("sorting " + peptideOccurrenceList.size()
				+ " peptide occurrences before to calculate the total peptide FDR");
		SorterUtil.sortPeptideOcurrencesByPeptideScore(peptideOccurrenceList, scoreName);
		int numFW = 0;
		int numDC = 0;
		for (PeptideOccurrence peptideOccurrence : peptideOccurrenceList) {
			ExtendedIdentifiedPeptide bestPeptide = peptideOccurrence.getBestPeptide(scoreName);
			if (bestPeptide != null && bestPeptide.isDecoy(fdrFilter))
				numDC++;
			else
				numFW++;
		}
		Float totalFDR = fdrFilter.calculateFDR(numFW, numDC);
		log.info("Num forward=" + numFW + "   num decoy=" + numDC);
		log.info("Calculated peptide FDR=" + totalFDR + " %");
		peptideFDR = totalFDR;
		return totalFDR;
	}

	public float getPSMFDR(String scoreName) {
		if (psmFDR != null)
			return psmFDR;

		FDRFilter fdrFilter = getFDRFilter();
		if (fdrFilter == null)
			throw new IllegalMiapeArgumentException("FDR filter is not defined");
		// - 2: remove peptide redundancy, taking the best PSM for each
		// sequence

		List<ExtendedIdentifiedPeptide> peptides = getIdentifiedPeptides();

		// - 3: sort peptides by score

		log.info("sorting " + peptides.size() + " PSMs before to calculate the total psm FDR");
		SorterUtil.sortPeptidesByPeptideScore(peptides, scoreName, true);
		int numFW = 0;
		int numDC = 0;
		for (ExtendedIdentifiedPeptide peptide : peptides) {

			if (peptide.isDecoy(fdrFilter))
				numDC++;
			else
				numFW++;
		}
		Float totalFDR = fdrFilter.calculateFDR(numFW, numDC);
		log.info("Num forward=" + numFW + "   num decoy=" + numDC);
		log.info("Calculated psm FDR=" + totalFDR + " %");
		psmFDR = totalFDR;
		return totalFDR;
	}

	public int getTotalNumPeptides() {
		return getIdentifiedPeptides().size();
	}

	public Set<String> getDifferentSearchedDatabases() {
		return differentSearchedDatabases;
	}

	/**
	 * Clears the internal cache of number of identifications
	 */
	public void clearCachedData() {
		numDifferentPeptideDecoys = null;
		numDifferentProteinDecoys = null;
		numPeptideDecoys = null;
		numProteinGroupDecoys = null;
		numNonConclusiveGroups = null;
		numDifferentNonConclusiveGroups = null;

	}

	public int getNumProteinGroupDecoys() {
		final FDRFilter fdrFilter = getFDRFilter();
		if (fdrFilter == null) {
			numProteinGroupDecoys = null;
			return 0;
		}
		if (numProteinGroupDecoys != null)
			return numProteinGroupDecoys;

		final List<ProteinGroup> proteinGroups = getIdentifiedProteinGroups();
		int numDecoys = 0;
		for (ProteinGroup proteinGroup : proteinGroups) {
			if (proteinGroup.isDecoy(fdrFilter))
				numDecoys++;
		}
		numProteinGroupDecoys = numDecoys;
		return numDecoys;
	}

	public int getNumDifferentProteinGroupsDecoys() {

		final FDRFilter fdrFilter = getFDRFilter();
		if (fdrFilter == null) {
			numDifferentProteinDecoys = null;
			return 0;
		}
		if (numDifferentProteinDecoys != null)
			return numDifferentProteinDecoys;
		final Collection<ProteinGroupOccurrence> proteinOccurrences = getProteinGroupOccurrenceList().values();
		int numDecoys = 0;
		for (ProteinGroupOccurrence proteinGroupOccurrence : proteinOccurrences) {
			if (proteinGroupOccurrence.isDecoy(fdrFilter))
				numDecoys++;
		}
		numDifferentProteinDecoys = numDecoys;
		return numDecoys;
	}

	public int getNumPeptideDecoys() {

		final FDRFilter fdrFilter = getFDRFilter();
		if (fdrFilter == null) {
			numPeptideDecoys = null;
			return 0;
		}
		if (numPeptideDecoys != null)
			return numPeptideDecoys;
		final List<ExtendedIdentifiedPeptide> identifiedPeptides = getIdentifiedPeptides();
		int numDecoys = 0;
		for (ExtendedIdentifiedPeptide peptide : identifiedPeptides) {
			if (peptide.isDecoy(fdrFilter))
				numDecoys++;
		}
		numPeptideDecoys = numDecoys;
		return numDecoys;
	}

	public int getNumDifferentPeptideDecoys(boolean distinguishModificatedPeptides) {
		final FDRFilter fdrFilter = getFDRFilter();
		if (fdrFilter == null) {
			numDifferentPeptideDecoys = null;
			return 0;
		}
		if (numDifferentPeptideDecoys != null)
			return numDifferentPeptideDecoys;
		final HashMap<String, PeptideOccurrence> peptideOccurrences = getPeptideOcurrenceList(
				distinguishModificatedPeptides);
		int numDecoys = 0;
		for (PeptideOccurrence peptideOccurrence : peptideOccurrences.values()) {
			if (peptideOccurrence.isDecoy())
				numDecoys++;
		}
		numDifferentPeptideDecoys = numDecoys;
		return numDecoys;
	}

	public static List<ExtendedIdentifiedPeptide> getPeptidesFromProteinGroups(List<ProteinGroup> proteinGroups) {
		List<ExtendedIdentifiedPeptide> ret = new ArrayList<ExtendedIdentifiedPeptide>();
		Set<Integer> peptideIds = new HashSet<Integer>();
		int minPeptideLength2 = getMinPeptideLength();

		if (proteinGroups != null) {
			log.info("Getting peptides from " + proteinGroups.size() + " protein groups");
			for (ProteinGroup proteinGroup : proteinGroups) {
				final List<ExtendedIdentifiedPeptide> peptidesFromProteins = proteinGroup.getPeptides();
				if (peptidesFromProteins != null && !peptidesFromProteins.isEmpty()) {
					for (ExtendedIdentifiedPeptide extendedIdentifiedPeptide : peptidesFromProteins) {
						// Do not take the same peptide several times ->
						// This
						// happens when a peptide is shared by several
						// proteins
						if (!peptideIds.contains(extendedIdentifiedPeptide.getId())) {
							if (minPeptideLength2 > extendedIdentifiedPeptide.getSequence().length())
								continue;
							ret.add(extendedIdentifiedPeptide);
							peptideIds.add(extendedIdentifiedPeptide.getId());
						} else {
							// log.info("Peptide is already in the list");
						}

					}

				} else {
					// log.warn("this protein has no peptides!: " +
					// protein.getAccession());
				}
			}
		}
		log.info(ret.size() + " peptides extracted from " + proteinGroups.size() + " protein groups");
		return ret;
	}

	public static List<ExtendedIdentifiedPeptide> getPeptidesFromProteinGroupsInParallel(
			List<ProteinGroup> proteinGroups) {

		int minPeptideLength2 = getMinPeptideLength();

		if (proteinGroups != null && !proteinGroups.isEmpty()) {
			int proteinGroupCount = proteinGroups.size();

			log.info("Getting peptides from " + proteinGroups.size() + " protein groups");
			int threadCount = SystemCoreManager.getAvailableNumSystemCores(MAX_NUMBER_PARALLEL_PROCESSES);

			log.info("Using " + threadCount + " processors from processing " + proteinGroupCount + " proteinGroups");
			Iterator<ProteinGroup> proteinGroupsIterator = proteinGroups.iterator();
			ParIterator<ProteinGroup> iterator = ParIteratorFactory.createParIterator(proteinGroupsIterator,
					proteinGroupCount, threadCount, Schedule.GUIDED);

			Reducible<List<ExtendedIdentifiedPeptide>> reduciblePeptides = new Reducible<List<ExtendedIdentifiedPeptide>>();

			List<PeptidesFromProteinGroupsParallelExtractor> runners = new ArrayList<PeptidesFromProteinGroupsParallelExtractor>();
			for (int numCore = 0; numCore < threadCount; numCore++) {
				PeptidesFromProteinGroupsParallelExtractor runner = new PeptidesFromProteinGroupsParallelExtractor(
						iterator, reduciblePeptides, minPeptideLength2);
				runners.add(runner);
				runner.start();
			}
			if (iterator.getAllExceptions().length > 0) {
				throw new IllegalArgumentException(iterator.getAllExceptions()[0].getException());
			}
			// Main thread waits for worker threads to complete
			for (int k = 0; k < threadCount; k++) {
				try {
					runners.get(k).join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			// Reductors
			Reduction<List<ExtendedIdentifiedPeptide>> peptideListReduction = new Reduction<List<ExtendedIdentifiedPeptide>>() {
				@Override
				public List<ExtendedIdentifiedPeptide> reduce(List<ExtendedIdentifiedPeptide> first,
						List<ExtendedIdentifiedPeptide> second) {
					List<ExtendedIdentifiedPeptide> peptides = new ArrayList<ExtendedIdentifiedPeptide>();
					Set<Integer> peptideIds = new HashSet<Integer>();
					for (ExtendedIdentifiedPeptide peptide : first) {
						if (!peptideIds.contains(peptide.getId())) {
							peptideIds.add(peptide.getId());
							peptides.add(peptide);
						}
					}
					for (ExtendedIdentifiedPeptide peptide : second) {
						if (!peptideIds.contains(peptide.getId())) {
							peptideIds.add(peptide.getId());
							peptides.add(peptide);
						}
					}

					return peptides;
				}
			};

			List<ExtendedIdentifiedPeptide> mergedPeptides = reduciblePeptides.reduce(peptideListReduction);
			log.info(mergedPeptides.size() + " peptides extracted from " + proteinGroups.size() + " protein groups");
			return mergedPeptides;
		}
		return new ArrayList<ExtendedIdentifiedPeptide>();
	}

	private List<ExtendedIdentifiedProtein> getProteinsFromProteinGroups(List<ProteinGroup> proteinGroups) {
		List<ExtendedIdentifiedProtein> ret = new ArrayList<ExtendedIdentifiedProtein>();
		Set<Integer> proteinIds = new HashSet<Integer>();
		if (proteinGroups != null) {
			// log.info("Getting proteins from " + proteinGroups.size()
			// + " protein groups");
			for (ProteinGroup proteinGroup : proteinGroups) {
				for (ExtendedIdentifiedProtein extendedIdentifiedProtein : proteinGroup) {
					if (!proteinIds.contains(extendedIdentifiedProtein.getId())) {
						ret.add(extendedIdentifiedProtein);
						proteinIds.add(extendedIdentifiedProtein.getId());
					}
				}

			}
		}
		log.info(ret.size() + " proteins extracted from " + proteinGroups.size() + " protein groups");
		return ret;
	}

	/**
	 * Check the peptides from all proteins and see if they are in the filtered
	 * peptides.<br>
	 * If yes, they are kept in the protein, otherwise they are removed from the
	 * protein.<br>
	 * If the protein has no peptides after this process, it is removed from the
	 * list.<br>
	 * PAnalyzer is run just before to return the proteinGroups
	 *
	 * @param proteinGroups
	 * @param filteredPeptideIDs
	 * @return
	 */
	public static List<ProteinGroup> filterProteinGroupsByPeptides(List<ProteinGroup> proteinGroups,
			Set<Integer> filteredPeptideIDs, ControlVocabularyManager cvManager) {
		List<ProteinGroup> ret = new ArrayList<ProteinGroup>();
		if (filteredPeptideIDs == null) {

			return proteinGroups;
		}

		int numPeptidesRejected = 0;
		int numPeptideAccepted = 0;
		int numProteinsRejected = 0;
		int numProteinsAccepted = 0;
		List<ExtendedIdentifiedProtein> totalProteins = new ArrayList<ExtendedIdentifiedProtein>();
		// take the proteins that has already peptides, and make the panalyzer
		// grouping again
		for (ProteinGroup proteinGroup : proteinGroups) {
			for (ExtendedIdentifiedProtein protein : proteinGroup) {
				final Iterator<ExtendedIdentifiedPeptide> peptideIterator = protein.getPeptides().iterator();
				if (peptideIterator != null) {
					while (peptideIterator.hasNext()) {
						ExtendedIdentifiedPeptide peptide = peptideIterator.next();

						if (peptide == null || !filteredPeptideIDs.contains(peptide.getId())) {

							peptideIterator.remove();
							numPeptidesRejected++;
						} else {
							numPeptideAccepted++;
						}
					}

					if (!protein.getPeptides().isEmpty()) {
						totalProteins.add(protein);
						numProteinsAccepted++;
					} else {

						numProteinsRejected++;
					}

				}
			}
		}
		log.info("Peptides accepted=" + numPeptideAccepted + "  Peptides rejected=" + numPeptidesRejected
				+ "   Proteins accepted=" + numProteinsAccepted + "  Proteins rejected=" + numProteinsRejected);
		// run panalyzer
		PAnalyzer panalyzer = new PAnalyzer(false);
		ret = panalyzer.run(totalProteins);
		log.info("Filtering protein groups by peptides: from " + proteinGroups.size() + " to " + ret.size());
		return ret;
	}

	public static void clearStaticInfo() {
		StaticPeptideStorage.clear();
	}

	public int getNumDifferentPeptidesPlusCharge(boolean distiguishModificatedPeptides) {
		HashMap<String, PeptideOccurrence> numDifPeptides = getPeptideOcurrenceList(distiguishModificatedPeptides);
		Set<String> peptidePlusCharge = new HashSet<String>();
		for (PeptideOccurrence peptideOccurrence : numDifPeptides.values()) {
			final List<ExtendedIdentifiedPeptide> itemList = peptideOccurrence.getItemList();
			for (ExtendedIdentifiedPeptide extendedIdentifiedPeptide : itemList) {
				String key = extendedIdentifiedPeptide.getCharge()
						+ extendedIdentifiedPeptide.getKey(distiguishModificatedPeptides);
				peptidePlusCharge.add(key);
			}
		}
		int total = peptidePlusCharge.size();
		log.debug(idSet.getName() + "---->Peptides  " + numDifPeptides + " = " + total);
		return total;
	}

	public int getNumPSMsForAPeptide(String sequenceKey) {
		if (peptideOccurrenceList.containsKey(sequenceKey)) {
			PeptideOccurrence peptideOccurrence = peptideOccurrenceList.get(sequenceKey);
			return peptideOccurrence.getPeptides().size();
		}
		return 0;
	}
}
