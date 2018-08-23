package org.proteored.miapeapi.experiment.model.datamanager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.exceptions.IllegalMiapeArgumentException;
import org.proteored.miapeapi.exceptions.InterruptedMIAPEThreadException;
import org.proteored.miapeapi.exceptions.MiapeDataInconsistencyException;
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
import org.proteored.miapeapi.experiment.model.filters.ProteinACCFilterByProteinComparatorKey;
import org.proteored.miapeapi.experiment.model.grouping.PAnalyzer;
import org.proteored.miapeapi.experiment.model.grouping.PanalyzerStats;
import org.proteored.miapeapi.experiment.model.grouping.ProteinEvidence;
import org.proteored.miapeapi.experiment.model.sort.SorterUtil;
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

import edu.scripps.yates.utilities.cores.SystemCoreManager;
import edu.scripps.yates.utilities.pi.ParIterator;
import edu.scripps.yates.utilities.pi.ParIterator.Schedule;
import edu.scripps.yates.utilities.pi.ParIteratorFactory;
import edu.scripps.yates.utilities.pi.reductions.Reducible;
import edu.scripps.yates.utilities.pi.reductions.Reduction;
import gnu.trove.list.array.TIntArrayList;
import gnu.trove.map.hash.THashMap;
import gnu.trove.map.hash.TIntIntHashMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.set.hash.THashSet;
import gnu.trove.set.hash.TIntHashSet;

public abstract class DataManager {
	// Peptides less than this length, will be discarded
	public final static int DEFAULT_MIN_PEPTIDE_LENGTH = 6;

	// private final static TIntObjectHashMap< ExtendedIdentifiedProtein>
	// staticProteins = new TIntObjectHashMap< ExtendedIdentifiedProtein>();
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
	protected Map<String, ProteinGroupOccurrence> proteinGroupOccurrenceList = new THashMap<String, ProteinGroupOccurrence>();

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
	protected Map<String, PeptideOccurrence> peptideOccurrenceList = new THashMap<String, PeptideOccurrence>();
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

	// private List<ProteinGroup> inclusionList = null;

	private final Set<String> differentSearchedDatabases = new THashSet<String>();

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
	private final boolean doNotGroupNonConclusiveProteins;
	private final boolean separateNonConclusiveProteins;
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
	public DataManager(IdentificationSet idSet, List<DataManager> dataManagers, boolean doNotGroupNonConclusiveProteins,
			boolean separateNonConclusiveProteins, boolean groupingAtExperimentListLevel, List<Filter> filters,
			Integer minPeptideLength, boolean processInParallel) {
		this.processInParallel = processInParallel;
		this.idSet = idSet;
		this.doNotGroupNonConclusiveProteins = doNotGroupNonConclusiveProteins;
		this.separateNonConclusiveProteins = separateNonConclusiveProteins;
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
		final boolean isASetOfRelicates = hasReplicates(dataManagers);
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
	public DataManager(IdentificationSet idSet, List<DataManager> dataManagers, boolean doNotGroupNonConclusiveProteins,
			boolean separateNonConclusiveProteins, Integer minPeptideLength, List<Filter> filters,
			boolean processInParallel) {
		this(idSet, dataManagers, doNotGroupNonConclusiveProteins, separateNonConclusiveProteins, false, filters,
				minPeptideLength, processInParallel);
	}

	public DataManager(IdentificationSet idSet, List<MiapeMSIDocument> miapeMSIs, List<Filter> filters,
			boolean doNotGroupNonConclusiveProteins, boolean separateNonConclusiveProteins, Integer minPeptideLength,
			boolean processInParallel) {
		this.doNotGroupNonConclusiveProteins = doNotGroupNonConclusiveProteins;
		this.separateNonConclusiveProteins = separateNonConclusiveProteins;
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
		for (final DataManager dataManager : dmgs) {
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
		final int minPeptideLength = getMinPeptideLength();
		if (nonFilteredIdentifiedPeptides.isEmpty() || nonFilteredIdentifiedProteins.isEmpty()) {
			log.warn("No peptides  or proteins!!");
			return;
		}
		log.info("Linking " + nonFilteredIdentifiedProteins.size() + " proteins with "
				+ nonFilteredIdentifiedPeptides.size() + " peptides");
		final TIntObjectHashMap<ExtendedIdentifiedPeptide> peptideMap = new TIntObjectHashMap<ExtendedIdentifiedPeptide>();
		for (final ExtendedIdentifiedPeptide peptide : nonFilteredIdentifiedPeptides) {
			peptideMap.put(peptide.getId(), peptide);
		}
		if (peptideMap.size() != nonFilteredIdentifiedPeptides.size()) {
			throw new MiapeDataInconsistencyException("Some peptides contain the same id");
		}
		final TIntObjectHashMap<ExtendedIdentifiedProtein> proteinMap = new TIntObjectHashMap<ExtendedIdentifiedProtein>();
		for (final ExtendedIdentifiedProtein prot : nonFilteredIdentifiedProteins) {
			proteinMap.put(prot.getId(), prot);
		}
		if (proteinMap.size() != nonFilteredIdentifiedProteins.size()) {
			throw new MiapeDataInconsistencyException("Some proteins contain the same id");
		}
		for (final ExtendedIdentifiedProtein protein : nonFilteredIdentifiedProteins) {
			if (Thread.currentThread().isInterrupted()) {
				throw new InterruptedMIAPEThreadException("Task cancelled");
			}
			final TIntArrayList peptidesFromProteinIDs = protein.getIdentifiedPeptideIDs();
			if (peptidesFromProteinIDs != null && !peptidesFromProteinIDs.isEmpty()) {
				boolean peptideFound = false;
				for (final int peptideFromProteinID : peptidesFromProteinIDs.toArray()) {
					if (Thread.currentThread().isInterrupted()) {
						throw new InterruptedMIAPEThreadException("Task cancelled");
					}
					if (protein.getPeptideSequenceByID(peptideFromProteinID) != null
							&& protein.getPeptideSequenceByID(peptideFromProteinID).length() >= minPeptideLength
							&& peptideMap.containsKey(peptideFromProteinID)) {

						protein.addPeptide(peptideMap.get(peptideFromProteinID));
						peptideMap.get(peptideFromProteinID).addProtein(protein);
						peptideFound = true;
					}
				}
				if (!peptideFound) {
					// can be possible because of the required minimum length of
					// the peptide

				}
			} else {
				if (getMinPeptideLength() != Integer.MAX_VALUE)
					log.debug("The protein " + protein.getAccession() + " has not peptides");
			}
		}
		for (final ExtendedIdentifiedPeptide peptide : nonFilteredIdentifiedPeptides) {
			if (peptide.getSequence() != null && peptide.getSequence().length() >= minPeptideLength) {
				final List<Integer> proteinsFromPeptide = peptide.getIdentifiedProteinIDs();
				if (proteinsFromPeptide != null && !proteinsFromPeptide.isEmpty()) {
					boolean proteinFound = false;
					for (final Integer proteinFromPeptideID : proteinsFromPeptide) {
						if (proteinFromPeptideID != null && proteinMap.containsKey(proteinFromPeptideID)) {
							proteinFound = true;
							peptide.addProtein(proteinMap.get(proteinFromPeptideID));
							proteinMap.get(proteinFromPeptideID).addPeptide(peptide);
						}
					}
					if (!proteinFound)
						log.debug("The peptide " + peptide.getId() + " has not proteins");
				} else {
					log.debug("The peptide " + peptide.getId() + " has not proteins");
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
		log.info("Discarded " + numPeptidesDiscarded + " peptides because they have not a protein linked");
		int numProteinsDiscarded = 0;
		while (proteinIterator.hasNext()) {
			final List<ExtendedIdentifiedPeptide> peptides = proteinIterator.next().getPeptides();
			if (peptides == null || peptides.isEmpty()) {
				numProteinsDiscarded++;
				proteinIterator.remove();
			}
		}
		log.info("Discarded " + numProteinsDiscarded + " proteins because of the minimum peptide length limit");
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
	 * @throws InterruptedException
	 *
	 */
	private void collectDataFromMSIs(List<MiapeMSIDocument> miapeMSIs) {
		final int minPepLength = getMinPeptideLength();
		int peptideWithoutProteins = 0;
		int psmsSkippedByRank = 0;

		// reset collections
		resetIdentificationSet();
		final TIntHashSet peptidesIds = new TIntHashSet();
		// process the MIAPE
		if (miapeMSIs != null) {
			for (final MiapeMSIDocument miapeMSIDocument : miapeMSIs) {
				if (miapeMSIDocument != null) {
					// Databases
					final Set<InputParameter> inputParameters = miapeMSIDocument.getInputParameters();
					if (inputParameters != null) {
						for (final InputParameter inputParameter : inputParameters) {
							if (inputParameter != null && inputParameter.getDatabases() != null) {
								for (final Database database : inputParameter.getDatabases()) {
									final String name = database.getName();
									if (name != null && !"".equals(name) && !differentSearchedDatabases.contains(name))
										differentSearchedDatabases.add(name);
								}
							}
						}
					}

					// PROTEINS
					final Set<IdentifiedProteinSet> identifiedProteinSets = miapeMSIDocument.getIdentifiedProteinSets();
					if (identifiedProteinSets != null) {
						for (final IdentifiedProteinSet identifiedProteinSet : identifiedProteinSets) {
							final Map<String, IdentifiedProtein> identifiedProteins2 = identifiedProteinSet
									.getIdentifiedProteins();
							if (identifiedProteins2 != null) {
								log.info(identifiedProteins2.size() + " proteins in dataset " + miapeMSIDocument.getId()
										+ " " + miapeMSIDocument.getName());
								for (final IdentifiedProtein protein : identifiedProteins2.values()) {
									if (Thread.currentThread().isInterrupted()) {
										throw new InterruptedMIAPEThreadException("Task cancelled");
									}
									// Allow proteins without peptides
									// if (!hasOnePeptideGreaterOrEqualThan(
									// protein, minPepLength))
									// continue;
									ExtendedIdentifiedProtein extendedIdentifiedProtein = null;
									// if (!staticProteins.containsKey(protein
									// .getId())) {
									extendedIdentifiedProtein = new ExtendedIdentifiedProtein((Replicate) idSet,
											protein, miapeMSIDocument);
									if (useStaticCollections) {
										StaticProteinStorage.addProtein(miapeMSIDocument.getName(), idSet.getFullName(),
												extendedIdentifiedProtein);
									}
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
										for (final ProteinScore proteinScore : scores) {
											final String scoreName = proteinScore.getName();
											if (!proteinScores.contains(scoreName))
												proteinScores.add(scoreName);
										}
									}

								}
							}
						}
					}
					// PEPTIDES
					final List<IdentifiedPeptide> identifiedPeptides2 = miapeMSIDocument.getIdentifiedPeptides();
					log.info("There are " + identifiedPeptides2.size() + " peptides in the MSI document");
					if (identifiedPeptides2 != null) {
						for (final IdentifiedPeptide peptide : identifiedPeptides2) {
							// only take the first in the rank
							if (peptide.getRank() > 1) {
								psmsSkippedByRank++;
								continue;
							}
							if (Thread.currentThread().isInterrupted()) {
								throw new InterruptedMIAPEThreadException("Task cancelled");
							}
							if (peptide.getIdentifiedProteins().isEmpty()) {
								peptideWithoutProteins++;
								// continue;
							}
							if (peptide.getSequence() != null && peptide.getSequence().length() >= minPepLength) {
								// NOT ADD PEPTIDES THAT WERE ADDED BEFORE!
								if (peptidesIds.contains(peptide.getId())) {
									// log.info("Peptide is already processed");
									// continue;
								}
								peptidesIds.add(peptide.getId());

								ExtendedIdentifiedPeptide extendedIdentifiedPeptide = null;
								// if (!staticPeptides
								// .containsKey(peptide
								// .getId())) {
								extendedIdentifiedPeptide = new ExtendedIdentifiedPeptide((Replicate) idSet, peptide,
										miapeMSIDocument);
								if (useStaticCollections) {
									StaticPeptideStorage.addPeptide(miapeMSIDocument.getName(), idSet.getFullName(),
											extendedIdentifiedPeptide);

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
									for (final PeptideScore peptideScore : peptideScores) {
										final String scoreName = peptideScore.getName();
										if (scoreName != null && !"".equals(scoreName.trim()))
											if (!this.peptideScores.contains(scoreName))
												this.peptideScores.add(scoreName);
									}
								}
								final Set<PeptideModification> modifs = peptide.getModifications();
								if (modifs != null) {
									for (final PeptideModification peptideModification : modifs) {
										final String modificationName = peptideModification.getName();
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
		if (psmsSkippedByRank > 0) {
			log.info(psmsSkippedByRank + "PSMs skipped for being a rank > than 1");
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
		// TIntHashSet proteinIDList = new TIntHashSet();
		// TIntHashSet peptideIDList = new TIntHashSet();

		for (final DataManager dataManager : dataManagers) {

			// DATABASES
			final Set<String> differentSearchedDatabases2 = dataManager.getDifferentSearchedDatabases();
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
		for (final DataManager dataManager : dataManagers) {

			// DATABASES
			final Set<String> differentSearchedDatabases2 = dataManager.getDifferentSearchedDatabases();
			differentSearchedDatabases.addAll(differentSearchedDatabases2);

			if (!groupingAtExperimentListLevel) {
				// PROTEIN GROUPS
				final List<ProteinGroup> proteinGroups = dataManager.getIdentifiedProteinGroups();
				log.info(proteinGroups.size() + " proteinGroups in experiment " + dataManager.idSet.getName());

				for (final ProteinGroup proteinGroup : proteinGroups) {
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
		final List<Filter> filtersToSet = new ArrayList<Filter>();
		for (final Filter filter : filters) {

			if (!(filter instanceof FDRFilter)) {
				filtersToSet.add(filter);
			} else {
				// If this is a FDRFilter
				final FDRFilter fdrFilter = (FDRFilter) filter;
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
		for (final Filter filter : filters) {
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

		final List<ProteinGroup> nextLevelProteinGroups = new ArrayList<ProteinGroup>();
		try {
			final List<IdentificationSet> nextLevelIdentificationSetList = idSet.getNextLevelIdentificationSetList();
			for (final IdentificationSet identificationSet : nextLevelIdentificationSetList) {
				final List<ProteinGroup> pgroups = identificationSet.getIdentifiedProteinGroups();
				nextLevelProteinGroups.addAll(pgroups);
			}
			return nextLevelProteinGroups;
		} catch (final UnsupportedOperationException e) {
			// in case of not having next level, reset protein groups and return
			if (getFilters().isEmpty()) {
				resetPeptidesFromProteins(nonFilteredIdentifiedProteins);
				final PAnalyzer panalyzer = new PAnalyzer(doNotGroupNonConclusiveProteins,
						separateNonConclusiveProteins);
				identifiedProteinGroups = panalyzer.run(nonFilteredIdentifiedProteins);
				return identifiedProteinGroups;
			}
			return getIdentifiedProteinGroups();

		}
	}

	private List<ExtendedIdentifiedProtein> getNextLevelIdentifiedProteins() {

		final List<ExtendedIdentifiedProtein> nextLevelProteins = new ArrayList<ExtendedIdentifiedProtein>();
		try {
			final List<IdentificationSet> nextLevelIdentificationSetList = idSet.getNextLevelIdentificationSetList();
			for (final IdentificationSet identificationSet : nextLevelIdentificationSetList) {
				final List<ExtendedIdentifiedProtein> proteins = identificationSet.getIdentifiedProteins();
				nextLevelProteins.addAll(proteins);
			}
			return nextLevelProteins;

		} catch (final UnsupportedOperationException e) {
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
						final List<ExtendedIdentifiedProtein> nextLevelProteins = getNextLevelIdentifiedProteins();

						// Run panalyzer
						final PAnalyzer panalyzer = new PAnalyzer(doNotGroupNonConclusiveProteins,
								separateNonConclusiveProteins);
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
					final List<ExtendedIdentifiedProtein> nextLevelProteins = getNextLevelIdentifiedProteins();

					// Run panalyzer
					final PAnalyzer panalyzer = new PAnalyzer(doNotGroupNonConclusiveProteins,
							separateNonConclusiveProteins);
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
			final List<Filter> filters = getFilters();
			if (filters != null && !filters.isEmpty()) {
				log.info("Filtering " + toFilter.size() + " protein groups");
			}
			identifiedProteinGroups = applyFilters(toFilter, filters);
			if (processInParallel) {
				identifiedPeptides = getPeptidesFromProteinGroupsInParallel(identifiedProteinGroups);
			} else {
				identifiedPeptides = getPeptidesFromProteinGroups(identifiedProteinGroups);

			}
			final PanalyzerStats panalyzerStats = new PanalyzerStats(identifiedProteinGroups);
			numNonConclusiveGroups = panalyzerStats.nonConclusiveCount;
			numDifferentNonConclusiveGroups = panalyzerStats.differentNonConclusiveCount;

			// Set to the previousLevelIdentificationSet as non filtered
			try {
				final IdentificationSet previousLevelIdentificationSet = idSet.getPreviousLevelIdentificationSet();
				if (previousLevelIdentificationSet != null)
					previousLevelIdentificationSet.setFiltered(false);
			} catch (final UnsupportedOperationException e) {
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
				final IdentificationSet previousLevelIdentificationSet = idSet.getPreviousLevelIdentificationSet();
				if (previousLevelIdentificationSet != null)
					previousLevelIdentificationSet.setFiltered(false);

			} catch (final UnsupportedOperationException e) {
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

		final PAnalyzer pAnalyzer = new PAnalyzer(doNotGroupNonConclusiveProteins, separateNonConclusiveProteins);
		final List<ProteinGroup> groups = pAnalyzer.run(nonFilteredIdentifiedProteins);
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

		log.info("RESETING PROTEINS");

		for (final ExtendedIdentifiedProtein protein : proteins) {
			protein.setDecoy(false, false);
			protein.resetPeptides(idSet.getFullName());
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
		for (final Filter filter : filters) {
			List<ProteinGroup> filteredProteinGroups = new ArrayList<ProteinGroup>();

			// use the occurrence filter just in case of being an experiment to
			// filter proteins that are not present in at least X replicates
			if (filter instanceof OccurrenceFilter
			// && this instanceof ExperimentDataManager
			) {
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
				if (dataManagers != null) {
					for (final DataManager dataManager : dataManagers) {
						// dataManager.setInclusionList(filteredProteinGroups);
						// dataManager.setFiltered(false);
					}
				}
				// } else if (filter instanceof OccurrenceFilter && this
				// instanceof ReplicateDataManager
				// && !((OccurrenceFilter) filter).isByReplicates()) {
				// OccurrenceFilter occurrenceFilter = (OccurrenceFilter)
				// filter;
				// if (ret == null) {
				// filteredProteinGroups =
				// occurrenceFilter.filterProteins(toFilter, inclusionList,
				// idSet);
				// } else {
				// filteredProteinGroups = occurrenceFilter.filterProteins(ret,
				// inclusionList, idSet);
				// }
				// aFilterIsApplied = true;
				// if (ret == null)
				// ret = new ArrayList<ProteinGroup>();
				//
				// ret.clear();
				// ret.addAll(filteredProteinGroups);
				// log.info(" (" + idSet.getName() + ") Passing from " +
				// toFilter.size() + " to " + ret.size()
				// + " proteins");
				// if it is not an occurrenceFilter
			} else if (!(filter instanceof OccurrenceFilter)) {
				boolean applyFilter = false;
				// if (this instanceof ExperimentListDataManager)
				// System.out.println("asdf");
				if (filter instanceof FDRFilter && this instanceof ReplicateDataManager) {
					final FDRFilter fdrFilter = (FDRFilter) filter;
					if (fdrFilter.getReplicateName() == null)
						applyFilter = true;
					else if (fdrFilter.getReplicateName().equals(getReplicateName())
							&& fdrFilter.getExperimentName().equals(getExperimentName()))
						applyFilter = true;
				}
				// if it is a protein ACC filter, filter on Replicates
				if (filter instanceof ProteinACCFilterByProteinComparatorKey && this instanceof ReplicateDataManager)
					applyFilter = true;

				// if it is not a FDR filter
				if (!(filter instanceof FDRFilter) && !(filter instanceof ProteinACCFilterByProteinComparatorKey))
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

	// private void setInclusionList(List<ProteinGroup> inclusionList) {
	// if (this.inclusionList == null)
	// this.inclusionList = new ArrayList<ProteinGroup>();
	// this.inclusionList.clear();
	//
	// this.inclusionList.addAll(inclusionList);
	//
	// }

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

		final int proteinGroups = getProteinGroupOccurrenceList().size();

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
	public Map<String, ProteinGroupOccurrence> getProteinGroupOccurrenceList() {
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
		final ProteinGroupOccurrence proteinGroupOccurrence = getProteinGroupOccurrenceByProteinGroupKey(
				proteinGroupKey);
		if (proteinGroupOccurrence != null)
			return proteinGroupOccurrence.getItemList().size();
		return 0;
	}

	public ProteinGroupOccurrence getProteinGroupOccurrence(ProteinGroup proteinGroup) {
		final Map<String, ProteinGroupOccurrence> proteinOcurrenceList = getProteinGroupOccurrenceList();
		if (proteinOcurrenceList.containsKey(proteinGroup.getKey()))
			return proteinOcurrenceList.get(proteinGroup.getKey());
		return null;

	}

	public ProteinGroupOccurrence getProteinGroupOccurrenceByProteinGroupKey(String proteinGroupKey) {
		final Map<String, ProteinGroupOccurrence> proteinOcurrenceList = getProteinGroupOccurrenceList();
		if (proteinOcurrenceList.containsKey(proteinGroupKey))
			return proteinOcurrenceList.get(proteinGroupKey);
		return null;

	}

	public ProteinGroupOccurrence getProteinGroupOccurrence(String proteinACC) {
		final Map<String, ProteinGroupOccurrence> proteinOcurrenceList = getProteinGroupOccurrenceList();
		for (final ProteinGroupOccurrence proteinGroupOccurrence : proteinOcurrenceList.values()) {
			if (proteinGroupOccurrence.getAccessions().contains(proteinACC)) {
				return proteinGroupOccurrence;
			}
		}
		return null;
	}

	public int getProteinGroupOccurrenceNumber(String proteinACC) {
		final Map<String, ProteinGroupOccurrence> proteinOcurrenceList = getProteinGroupOccurrenceList();
		for (final ProteinGroupOccurrence proteinGroupOccurrence : proteinOcurrenceList.values()) {
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
		final List<ExtendedIdentifiedPeptide> ret = new ArrayList<ExtendedIdentifiedPeptide>();
		try {
			final TIntHashSet peptideIDs = new TIntHashSet();
			final List<IdentificationSet> nextLevelIdentificationSetList = idSet.getNextLevelIdentificationSetList();
			for (final IdentificationSet identificationSet : nextLevelIdentificationSetList) {
				List<ExtendedIdentifiedPeptide> peptides = null;

				peptides = identificationSet.getIdentifiedPeptides();

				if (peptides != null)
					for (final ExtendedIdentifiedPeptide extendedIdentifiedPeptide : peptides) {
						if (!peptideIDs.contains(extendedIdentifiedPeptide.getId())) {
							ret.add(extendedIdentifiedPeptide);
							peptideIDs.add(extendedIdentifiedPeptide.getId());
						}
					}
			}
		} catch (final UnsupportedOperationException e) {
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
	public Map<String, PeptideOccurrence> getPeptideOcurrenceList(boolean distinguishModPep) {

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

	public Map<String, PeptideOccurrence> getPeptideChargeOcurrenceList(boolean distinguishModPep) {
		final Map<String, PeptideOccurrence> peptideChargeOcurrenceList = getPeptideOcurrenceList(distinguishModPep);
		final Map<String, PeptideOccurrence> ret = new THashMap<String, PeptideOccurrence>();
		for (final PeptideOccurrence peptideOccurrence : peptideChargeOcurrenceList.values()) {
			final List<ExtendedIdentifiedPeptide> itemList = peptideOccurrence.getItemList();
			for (final ExtendedIdentifiedPeptide peptide : itemList) {
				String sequenceChargeKey = peptide.getSequence();
				if (distinguishModPep) {
					sequenceChargeKey = peptide.getModificationString();
				}
				if (peptide.getCharge() != null) {
					sequenceChargeKey += "_" + peptide.getCharge();
				}
				if (!ret.containsKey(sequenceChargeKey)) {
					final PeptideOccurrence newPeptideOccurrence = new PeptideOccurrence(sequenceChargeKey);
					newPeptideOccurrence.addOccurrence(peptide);
					ret.put(sequenceChargeKey, newPeptideOccurrence);
				} else {
					ret.get(sequenceChargeKey).addOccurrence(peptide);
				}
			}
		}
		return ret;
	}

	public static Map<String, PeptideOccurrence> createPeptideOccurrenceList(List<ExtendedIdentifiedPeptide> peptides,
			boolean distinguishModPep) {
		final Map<String, PeptideOccurrence> ret = new THashMap<String, PeptideOccurrence>();

		for (final ExtendedIdentifiedPeptide extPeptide : peptides) {
			if (extPeptide != null) {
				final String key = extPeptide.getKey(distinguishModPep);
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

	public static Map<String, PeptideOccurrence> createPeptideOccurrenceListInParallel(
			List<ExtendedIdentifiedPeptide> peptides, final boolean distinguishModPep) {

		final int threadCount = SystemCoreManager.getAvailableNumSystemCores(MAX_NUMBER_PARALLEL_PROCESSES);
		log.debug("Creating peptide occurrence list " + peptides.size() + " distinguish = " + distinguishModPep
				+ " using " + threadCount + " threads");
		final ParIterator<ExtendedIdentifiedPeptide> iterator = ParIteratorFactory.createParIterator(peptides,
				threadCount, Schedule.GUIDED);
		final Reducible<Map<String, PeptideOccurrence>> reduciblePeptideMap = new Reducible<Map<String, PeptideOccurrence>>();

		final List<PeptideOcurrenceParallelCreator> runners = new ArrayList<PeptideOcurrenceParallelCreator>();
		for (int numCore = 0; numCore < threadCount; numCore++) {
			// take current DB session
			final PeptideOcurrenceParallelCreator runner = new PeptideOcurrenceParallelCreator(iterator, numCore,
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
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}
		}

		// Reductors
		final Reduction<Map<String, PeptideOccurrence>> peptideListReduction = new Reduction<Map<String, PeptideOccurrence>>() {
			@Override
			public Map<String, PeptideOccurrence> reduce(Map<String, PeptideOccurrence> first,
					Map<String, PeptideOccurrence> second) {
				final Map<String, PeptideOccurrence> peptideOccurrences = new THashMap<String, PeptideOccurrence>();
				for (final String key : first.keySet()) {
					if (peptideOccurrences.containsKey(key)) {
						final PeptideOccurrence peptideOccurrenceFirst = first.get(key);
						for (final ExtendedIdentifiedPeptide expPeptide : peptideOccurrenceFirst.getItemList()) {
							peptideOccurrences.get(key).addOccurrence(expPeptide);
						}

					} else {
						peptideOccurrences.put(key, first.get(key));
					}

				}
				for (final String key : second.keySet()) {
					if (peptideOccurrences.containsKey(key)) {
						final PeptideOccurrence peptideOccurrenceSecond = second.get(key);
						for (final ExtendedIdentifiedPeptide expPeptide : peptideOccurrenceSecond.getItemList()) {
							peptideOccurrences.get(key).addOccurrence(expPeptide);
						}

					} else {
						peptideOccurrences.put(key, second.get(key));
					}

				}
				return peptideOccurrences;
			}
		};

		final Map<String, PeptideOccurrence> mergedPeptideOccurrences = reduciblePeptideMap
				.reduce(peptideListReduction);
		log.debug(mergedPeptideOccurrences.size() + " peptides occurrences from " + peptides.size() + " peptides");

		return mergedPeptideOccurrences;

	}

	public static Map<String, ProteinOccurrence> createProteinOccurrenceList(List<ExtendedIdentifiedProtein> proteins) {
		final Map<String, ProteinOccurrence> ret = new THashMap<String, ProteinOccurrence>();

		for (final ExtendedIdentifiedProtein protein : proteins) {
			if (protein != null) {
				final String key = protein.getAccession();
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

	public static Map<String, ProteinGroupOccurrence> createProteinGroupOccurrenceList(
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
	public static Map<String, ProteinGroupOccurrence> createProteinGroupOccurrenceListWithStrictComparison(
			List<ProteinGroup> proteinGroups) {
		log.debug("Creating protein group occurrences from " + proteinGroups.size() + " protein groups");
		final Map<String, ProteinGroupOccurrence> differentProteinGroups = new THashMap<String, ProteinGroupOccurrence>();

		for (final ProteinGroup proteinGroup : proteinGroups) {
			if (differentProteinGroups.containsKey(proteinGroup.getKey())) {
				final ProteinGroupOccurrence proteinGroupOccurrence = differentProteinGroups.get(proteinGroup.getKey());

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

	public static Map<String, ProteinGroupOccurrence> createProteinGroupOccurrenceListWithStrictComparisonInParallel(
			List<ProteinGroup> proteinGroups) {
		final int threadCount = SystemCoreManager.getAvailableNumSystemCores(MAX_NUMBER_PARALLEL_PROCESSES);
		log.debug("Creating protein group occurrences from " + proteinGroups.size() + " protein groups using "
				+ threadCount + " threads");
		final ParIterator<ProteinGroup> iterator = ParIteratorFactory.createParIterator(proteinGroups, threadCount,
				Schedule.GUIDED);
		final Reducible<Map<String, ProteinGroupOccurrence>> reducibleProteinGroupMap = new Reducible<Map<String, ProteinGroupOccurrence>>();

		final List<ProteinGroupOcurrenceParallelCreator> runners = new ArrayList<ProteinGroupOcurrenceParallelCreator>();
		for (int numCore = 0; numCore < threadCount; numCore++) {
			// take current DB session
			final ProteinGroupOcurrenceParallelCreator runner = new ProteinGroupOcurrenceParallelCreator(iterator,
					reducibleProteinGroupMap);
			runners.add(runner);
			runner.start();
		}

		// Main thread waits for worker threads to complete
		for (int k = 0; k < threadCount; k++) {
			try {
				runners.get(k).join();
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}
		}
		if (iterator.getAllExceptions().length > 0) {
			throw new IllegalArgumentException(iterator.getAllExceptions()[0].getException());
		}
		// Reductors
		final Reduction<Map<String, ProteinGroupOccurrence>> proteinGroupListReduction = new Reduction<Map<String, ProteinGroupOccurrence>>() {
			@Override
			public Map<String, ProteinGroupOccurrence> reduce(Map<String, ProteinGroupOccurrence> first,
					Map<String, ProteinGroupOccurrence> second) {
				final Map<String, ProteinGroupOccurrence> proteinGroupOccurrences = new THashMap<String, ProteinGroupOccurrence>();
				for (final String key : first.keySet()) {
					final ProteinGroupOccurrence proteinGroupOccurrenceFirst = first.get(key);
					if (proteinGroupOccurrences.containsKey(key)) {
						final ProteinGroupOccurrence proteinGroupOccurrence = proteinGroupOccurrences.get(key);
						for (final ProteinGroup proteinGroup : proteinGroupOccurrenceFirst.getItemList()) {
							proteinGroupOccurrence.addOccurrence(proteinGroup);
						}
					} else {
						proteinGroupOccurrences.put(key, proteinGroupOccurrenceFirst);
					}
				}
				for (final String key : second.keySet()) {
					final ProteinGroupOccurrence proteinGroupOccurrencesSecond = second.get(key);
					if (proteinGroupOccurrences.containsKey(key)) {
						final ProteinGroupOccurrence proteinGroupOccurrence = proteinGroupOccurrences.get(key);
						for (final ProteinGroup proteinGroup : proteinGroupOccurrencesSecond.getItemList()) {
							proteinGroupOccurrence.addOccurrence(proteinGroup);
						}

					} else {
						proteinGroupOccurrences.put(key, proteinGroupOccurrencesSecond);
					}
				}
				return proteinGroupOccurrences;
			}
		};

		final Map<String, ProteinGroupOccurrence> mergedPeptideOccurrences = reducibleProteinGroupMap
				.reduce(proteinGroupListReduction);
		log.debug(mergedPeptideOccurrences.size() + " protein group occurrences from " + proteinGroups.size()
				+ " protein groups");
		return mergedPeptideOccurrences;
	}

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
		final Map<String, PeptideOccurrence> peptideOcurrenceList = getPeptideOcurrenceList(distModPep);
		return peptideOcurrenceList.get(sequence);
	}

	public PeptideOccurrence getPeptideChargeOccurrence(String sequencePlusChargeKey, boolean distModPep) {
		final Map<String, PeptideOccurrence> peptideChargeOcurrenceList = getPeptideChargeOcurrenceList(distModPep);
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
		final int numDifPeptides = getPeptideOcurrenceList(distiguishModificatedPeptides).size();

		final int total = numDifPeptides;

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
		for (final ExtendedIdentifiedPeptide extendedIdentifiedPeptide : peptides) {
			if (extendedIdentifiedPeptide.getModifications() != null) {
				for (final PeptideModification modification : extendedIdentifiedPeptide.getModifications()) {
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
		for (final ExtendedIdentifiedPeptide extendedIdentifiedPeptide : peptides) {
			final Set<PeptideModification> modifications = extendedIdentifiedPeptide.getModifications();
			if (modifications != null) {
				for (final PeptideModification peptideModification : modifications) {
					if (modif.equals(peptideModification.getName()))
						count++;
				}
			}
		}
		return count;
	}

	public TIntIntHashMap getModificationOccurrenceDistribution(String modif) {

		final TIntIntHashMap ret = new TIntIntHashMap();
		final Map<String, PeptideOccurrence> peptideOccurrences = getPeptideOcurrenceList(true);
		for (final PeptideOccurrence peptideOccurrence : peptideOccurrences.values()) {
			final Set<PeptideModification> peptideModifications = peptideOccurrence.getFirstOccurrence()
					.getModifications();
			int count = 0;
			if (peptideModifications != null) {
				if (modif != null) {
					for (final PeptideModification peptideModification : peptideModifications) {
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
		for (final PeptideOccurrence peptideOccurrence : peptideOccurrences) {
			final ExtendedIdentifiedPeptide firstIdentificationOccurrence = peptideOccurrence.getFirstOccurrence();
			final Set<PeptideModification> modifications = firstIdentificationOccurrence.getModifications();
			if (modifications != null) {
				for (final PeptideModification peptideModification : modifications) {
					if (modification.equals(peptideModification.getName())) {
						count++;
						break;
					}
				}
			}
		}
		return count;
	}

	public TIntIntHashMap getMissedCleavagesOccurrenceDistribution(String cleavageAminoacids) {

		final TIntIntHashMap ret = new TIntIntHashMap();
		final List<ExtendedIdentifiedPeptide> peptides = getIdentifiedPeptides();
		for (final ExtendedIdentifiedPeptide peptide : peptides) {
			final int missedCleavages = peptide.getNumMissedcleavages(cleavageAminoacids);

			if (ret.containsKey(missedCleavages)) {
				Integer integer = ret.get(missedCleavages);
				integer++;
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
				for (final Filter filter : filters) {
					if (filter instanceof FDRFilter)
						return (FDRFilter) filter;
				}
			}
		} else if (this instanceof ExperimentDataManager || this instanceof ExperimentListDataManager) {
			if (dataManagers != null) {
				FDRFilter commonFdrFilter = null;
				for (final DataManager datamanager : dataManagers) {
					final FDRFilter fdrFilter = datamanager.getFDRFilter();
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
	public int getProteinGroupTP(Set<String> truePositiveProteinACCs, boolean countNonConclusiveProteins) {
		final List<ProteinGroup> positiveProteinGroups = getIdentifiedProteinGroups();
		int TP = 0;
		for (final ProteinGroup proteinGroup : positiveProteinGroups) {
			if (proteinGroup.getEvidence() == ProteinEvidence.NONCONCLUSIVE && !countNonConclusiveProteins)
				continue;
			for (final ExtendedIdentifiedProtein extendedIdentifiedProtein : proteinGroup) {
				if (truePositiveProteinACCs.contains(extendedIdentifiedProtein.getAccession())) {
					TP++;
					break;
				}
			}
		}
		return TP;
	}

	public int getProteinGroupFN(Set<String> truePositiveProteinACCs, boolean countNonConclusiveProteins) {
		final List<ProteinGroup> allProteinGroups = getNonFilteredIdentifiedProteinGroups();
		final List<ProteinGroup> positiveProteinGroups = getIdentifiedProteinGroups();

		// if (getPeptideFDRFilter() == null)
		// throw new IllegalMiapeArgumentException(
		// "False Negatives cannot be calculated. A threshold is needed to
		// determine which proteins have been considered as positives after the
		// filter");
		int FN = 0;
		for (final ProteinGroup proteinGroup : allProteinGroups) {
			if (proteinGroup.getEvidence() == ProteinEvidence.NONCONCLUSIVE && !countNonConclusiveProteins)
				continue;
			boolean isConsideredAsPositiveProteinGroup = false;
			for (final ProteinGroup positiveProtein : positiveProteinGroups) {
				if (positiveProtein.shareOneProtein(proteinGroup)) {
					isConsideredAsPositiveProteinGroup = true;
					break;
				}
			}
			if (!isConsideredAsPositiveProteinGroup) {
				for (final ExtendedIdentifiedProtein extendedIdentifiedProtein : proteinGroup) {
					if (truePositiveProteinACCs.contains(extendedIdentifiedProtein.getAccession())) {
						FN++;
						break;
					}
				}
			}
		}
		return FN;
	}

	public int getProteinGroupTN(Set<String> truePositiveProteinACCs, boolean countNonConclusiveProteins) {
		final List<ProteinGroup> allProteinGroups = getNonFilteredIdentifiedProteinGroups();
		final List<ProteinGroup> positiveProteinGroups = getIdentifiedProteinGroups();

		// if (getPeptideFDRFilter() == null)
		// throw new IllegalMiapeArgumentException(
		// "True Negatives cannot be calculated. A threshold is needed to
		// determine which proteins have been considered as positives after the
		// filter");
		int TN = 0;
		for (final ProteinGroup proteinGroup : allProteinGroups) {
			if (proteinGroup.getEvidence() == ProteinEvidence.NONCONCLUSIVE && !countNonConclusiveProteins)
				continue;
			boolean isConsideredAsPositiveProteinGroup = false;
			for (final ProteinGroup positiveProteinGroup : positiveProteinGroups) {
				if (positiveProteinGroup.shareOneProtein(proteinGroup)) {
					isConsideredAsPositiveProteinGroup = true;
					break;
				}
			}
			if (!isConsideredAsPositiveProteinGroup) {
				boolean found = false;
				for (final ExtendedIdentifiedProtein extendedIdentifiedProtein : proteinGroup) {
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

	public int getProteinGroupFP(Set<String> truePositiveProteinACCs, boolean countNonConclusiveProteins) {
		final List<ProteinGroup> positiveProteinGroups = getIdentifiedProteinGroups();
		int FP = 0;
		for (final ProteinGroup proteinGroup : positiveProteinGroups) {
			if (proteinGroup.getEvidence() == ProteinEvidence.NONCONCLUSIVE && !countNonConclusiveProteins)
				continue;
			boolean found = false;
			for (final ExtendedIdentifiedProtein extendedIdentifiedProtein : proteinGroup) {
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

	public int getPeptideTP(Set<String> truePositivePeptideSequences, boolean distinguishModificatedPeptides) {
		final List<ExtendedIdentifiedPeptide> positivePeptides = getIdentifiedPeptides();
		int TP = 0;
		for (final ExtendedIdentifiedPeptide identifiedPeptide : positivePeptides) {
			if (!distinguishModificatedPeptides
					&& truePositivePeptideSequences.contains(identifiedPeptide.getSequence()))
				TP++;
			else if (distinguishModificatedPeptides
					&& truePositivePeptideSequences.contains(identifiedPeptide.getModificationString()))
				TP++;
		}
		return TP;
	}

	public int getPeptideFN(Set<String> truePositivePeptideSequences, boolean distinguishModificatedPeptides) {
		final List<ExtendedIdentifiedPeptide> allPeptides = getNonFilteredIdentifiedPeptides();
		final List<ExtendedIdentifiedPeptide> positivePeptides = getIdentifiedPeptides();

		// if (getPeptideFDRFilter() == null)
		// throw new IllegalMiapeArgumentException(
		// "False Negatives cannot be calculated. A threshold is needed to
		// determine which proteins have been considered as positives after the
		// filter");
		int FN = 0;
		for (final ExtendedIdentifiedPeptide identifiedPeptide : allPeptides) {
			boolean isConsideredAsPositivePeptide = false;
			for (final ExtendedIdentifiedPeptide positivePeptide : positivePeptides) {
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

	public int getPeptideTN(Set<String> truePositivePeptideSequences, boolean distinguishModificatedPeptides) {
		final List<ExtendedIdentifiedPeptide> allPeptides = getNonFilteredIdentifiedPeptides();
		final List<ExtendedIdentifiedPeptide> positivePeptides = getIdentifiedPeptides();

		// if (getPeptideFDRFilter() == null)
		// throw new IllegalMiapeArgumentException(
		// "True Negatives cannot be calculated. A threshold is needed to
		// determine which proteins have been considered as positives after the
		// filter");
		int TN = 0;
		for (final ExtendedIdentifiedPeptide identifiedPeptide : allPeptides) {
			boolean isConsideredAsPositivePeptide = false;
			for (final ExtendedIdentifiedPeptide positivePeptide : positivePeptides) {
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

	public int getPeptideFP(Set<String> truePositivePeptideSequences, boolean distinguishModificatedPeptides) {
		final List<ExtendedIdentifiedPeptide> positivePeptides = getIdentifiedPeptides();
		int FP = 0;
		for (final ExtendedIdentifiedPeptide identifiedPeptide : positivePeptides) {
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
		final FDRFilter fdrFilter = getFDRFilter();
		if (fdrFilter == null)
			throw new IllegalMiapeArgumentException("FDR filter is not defined");

		final Collection<ProteinGroupOccurrence> proteinGroupOccurrencesSet = getProteinGroupOccurrenceList().values();
		final List<ProteinGroupOccurrence> proteinGroupOccurrences = new ArrayList<ProteinGroupOccurrence>();
		for (final ProteinGroupOccurrence proteinGroupOccurrence : proteinGroupOccurrencesSet) {
			proteinGroupOccurrences.add(proteinGroupOccurrence);
		}
		// Sort proteins by best peptide score
		SorterUtil.sortProteinGroupOcurrencesByPeptideScore(proteinGroupOccurrences, scoreName);

		long numFWHits = 0; // forward hits
		long numDCHits = 0; // decoy hits
		new ArrayList<ProteinGroup>();
		// - 4: for each peptide, look to its protein and count if it is
		// decoy or not. Ignore it if already has been seen
		for (final ProteinGroupOccurrence proteinGroupOccurrence : proteinGroupOccurrences) {
			final Float bestPeptideScore = proteinGroupOccurrence.getBestPeptideScore(scoreName);
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
		final float totalFDR = fdrFilter.calculateFDR(numFWHits, numDCHits);
		proteinFDR = totalFDR;
		log.info("Protein FDR calculated: " + totalFDR);
		return totalFDR;
	}

	public float getPeptideFDR(String scoreName) {
		if (peptideFDR != null)
			return peptideFDR;

		final FDRFilter fdrFilter = getFDRFilter();
		if (fdrFilter == null)
			throw new IllegalMiapeArgumentException("FDR filter is not defined");
		// - 2: remove peptide redundancy, taking the best PSM for each
		// sequence

		final Collection<PeptideOccurrence> peptideOccurrenceCollection = getPeptideOcurrenceList(false).values();
		final List<PeptideOccurrence> peptideOccurrenceList = new ArrayList<PeptideOccurrence>();
		for (final PeptideOccurrence identificationOccurrence : peptideOccurrenceCollection) {
			peptideOccurrenceList.add(identificationOccurrence);
		}

		// - 3: sort peptides by score

		log.info("sorting " + peptideOccurrenceList.size()
				+ " peptide occurrences before to calculate the total peptide FDR");
		SorterUtil.sortPeptideOcurrencesByPeptideScore(peptideOccurrenceList, scoreName);
		int numFW = 0;
		int numDC = 0;
		for (final PeptideOccurrence peptideOccurrence : peptideOccurrenceList) {
			final ExtendedIdentifiedPeptide bestPeptide = peptideOccurrence.getBestPeptide(scoreName);
			if (bestPeptide != null && bestPeptide.isDecoy(fdrFilter))
				numDC++;
			else
				numFW++;
		}
		final Float totalFDR = fdrFilter.calculateFDR(numFW, numDC);
		log.info("Num forward=" + numFW + "   num decoy=" + numDC);
		log.info("Calculated peptide FDR=" + totalFDR + " %");
		peptideFDR = totalFDR;
		return totalFDR;
	}

	public float getPSMFDR(String scoreName) {
		if (psmFDR != null)
			return psmFDR;

		final FDRFilter fdrFilter = getFDRFilter();
		if (fdrFilter == null)
			throw new IllegalMiapeArgumentException("FDR filter is not defined");
		// - 2: remove peptide redundancy, taking the best PSM for each
		// sequence

		final List<ExtendedIdentifiedPeptide> peptides = getIdentifiedPeptides();

		// - 3: sort peptides by score

		log.info("sorting " + peptides.size() + " PSMs before to calculate the total psm FDR");
		SorterUtil.sortPeptidesByPeptideScore(peptides, scoreName, true);
		int numFW = 0;
		int numDC = 0;
		for (final ExtendedIdentifiedPeptide peptide : peptides) {

			if (peptide.isDecoy(fdrFilter))
				numDC++;
			else
				numFW++;
		}
		final Float totalFDR = fdrFilter.calculateFDR(numFW, numDC);
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
		for (final ProteinGroup proteinGroup : proteinGroups) {
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
		for (final ProteinGroupOccurrence proteinGroupOccurrence : proteinOccurrences) {
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
		for (final ExtendedIdentifiedPeptide peptide : identifiedPeptides) {
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
		final Map<String, PeptideOccurrence> peptideOccurrences = getPeptideOcurrenceList(
				distinguishModificatedPeptides);
		int numDecoys = 0;
		for (final PeptideOccurrence peptideOccurrence : peptideOccurrences.values()) {
			if (peptideOccurrence.isDecoy())
				numDecoys++;
		}
		numDifferentPeptideDecoys = numDecoys;
		return numDecoys;
	}

	public static List<ExtendedIdentifiedPeptide> getPeptidesFromProteinGroups(List<ProteinGroup> proteinGroups) {
		final List<ExtendedIdentifiedPeptide> ret = new ArrayList<ExtendedIdentifiedPeptide>();
		final TIntHashSet peptideIds = new TIntHashSet();
		final int minPeptideLength2 = getMinPeptideLength();

		if (proteinGroups != null) {
			log.info("Getting peptides from " + proteinGroups.size() + " protein groups");
			for (final ProteinGroup proteinGroup : proteinGroups) {
				final List<ExtendedIdentifiedPeptide> peptidesFromProteins = proteinGroup.getPeptides();
				if (peptidesFromProteins != null && !peptidesFromProteins.isEmpty()) {
					for (final ExtendedIdentifiedPeptide extendedIdentifiedPeptide : peptidesFromProteins) {
						if (extendedIdentifiedPeptide.getProteins().isEmpty()) {
							log.info(extendedIdentifiedPeptide);
						}
						// Do not take the same peptide several times ->
						// This happens when a peptide is shared by several
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

		final int minPeptideLength2 = getMinPeptideLength();

		if (proteinGroups != null && !proteinGroups.isEmpty()) {
			final int proteinGroupCount = proteinGroups.size();

			log.info("Getting peptides from " + proteinGroups.size() + " protein groups");
			final int threadCount = SystemCoreManager.getAvailableNumSystemCores(MAX_NUMBER_PARALLEL_PROCESSES);

			log.info("Using " + threadCount + " processors from processing " + proteinGroupCount + " proteinGroups");
			final Iterator<ProteinGroup> proteinGroupsIterator = proteinGroups.iterator();
			final ParIterator<ProteinGroup> iterator = ParIteratorFactory.createParIterator(proteinGroupsIterator,
					proteinGroupCount, threadCount, Schedule.GUIDED);

			final Reducible<List<ExtendedIdentifiedPeptide>> reduciblePeptides = new Reducible<List<ExtendedIdentifiedPeptide>>();

			final List<PeptidesFromProteinGroupsParallelExtractor> runners = new ArrayList<PeptidesFromProteinGroupsParallelExtractor>();
			for (int numCore = 0; numCore < threadCount; numCore++) {
				final PeptidesFromProteinGroupsParallelExtractor runner = new PeptidesFromProteinGroupsParallelExtractor(
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
				} catch (final InterruptedException e) {
					e.printStackTrace();
				}
			}

			// Reductors
			final Reduction<List<ExtendedIdentifiedPeptide>> peptideListReduction = new Reduction<List<ExtendedIdentifiedPeptide>>() {
				@Override
				public List<ExtendedIdentifiedPeptide> reduce(List<ExtendedIdentifiedPeptide> first,
						List<ExtendedIdentifiedPeptide> second) {
					final List<ExtendedIdentifiedPeptide> peptides = new ArrayList<ExtendedIdentifiedPeptide>();
					final TIntHashSet peptideIds = new TIntHashSet();
					for (final ExtendedIdentifiedPeptide peptide : first) {
						if (!peptideIds.contains(peptide.getId())) {
							peptideIds.add(peptide.getId());
							peptides.add(peptide);
						}
					}
					for (final ExtendedIdentifiedPeptide peptide : second) {
						if (!peptideIds.contains(peptide.getId())) {
							peptideIds.add(peptide.getId());
							peptides.add(peptide);
						}
					}

					return peptides;
				}
			};

			final List<ExtendedIdentifiedPeptide> mergedPeptides = reduciblePeptides.reduce(peptideListReduction);
			log.info(mergedPeptides.size() + " peptides extracted from " + proteinGroups.size() + " protein groups");
			return mergedPeptides;
		}
		return new ArrayList<ExtendedIdentifiedPeptide>();
	}

	private List<ExtendedIdentifiedProtein> getProteinsFromProteinGroups(List<ProteinGroup> proteinGroups) {
		final List<ExtendedIdentifiedProtein> ret = new ArrayList<ExtendedIdentifiedProtein>();
		final TIntHashSet proteinIds = new TIntHashSet();
		if (proteinGroups != null) {
			// log.info("Getting proteins from " + proteinGroups.size()
			// + " protein groups");
			for (final ProteinGroup proteinGroup : proteinGroups) {
				for (final ExtendedIdentifiedProtein extendedIdentifiedProtein : proteinGroup) {
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
	 * @param validPeptideIDs
	 * @return
	 */
	public static List<ProteinGroup> filterProteinGroupsByPeptides(List<ProteinGroup> proteinGroups,
			boolean doNotGroupNonConclusiveProteins, boolean separateNonConclusiveProteins, TIntHashSet validPeptideIDs,
			ControlVocabularyManager cvManager) {
		List<ProteinGroup> ret = new ArrayList<ProteinGroup>();
		if (validPeptideIDs == null) {

			return proteinGroups;
		}

		int numPeptidesRejected = 0;
		int numPeptideAccepted = 0;
		int numProteinsRejected = 0;
		int numProteinsAccepted = 0;
		final List<ExtendedIdentifiedProtein> totalProteins = new ArrayList<ExtendedIdentifiedProtein>();
		// take the proteins that has already peptides, and make the panalyzer
		// grouping again
		for (final ProteinGroup proteinGroup : proteinGroups) {
			for (final ExtendedIdentifiedProtein protein : proteinGroup) {
				final Iterator<ExtendedIdentifiedPeptide> peptideIterator = protein.getPeptides().iterator();
				if (peptideIterator != null) {
					while (peptideIterator.hasNext()) {
						final ExtendedIdentifiedPeptide peptide = peptideIterator.next();

						if (peptide == null || !validPeptideIDs.contains(peptide.getId())) {
							// remove peptide from protein
							peptideIterator.remove();
							// also, remove protein from peptide
							peptide.getProteins().remove(protein);
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
		final PAnalyzer panalyzer = new PAnalyzer(doNotGroupNonConclusiveProteins, separateNonConclusiveProteins);
		ret = panalyzer.run(totalProteins);
		log.info("Filtering protein groups by peptides: from " + proteinGroups.size() + " to " + ret.size());
		return ret;
	}

	public static void clearStaticInfo() {
		StaticPeptideStorage.clear();
		StaticProteinStorage.clear();
	}

	public int getNumDifferentPeptidesPlusCharge(boolean distiguishModificatedPeptides) {
		final Map<String, PeptideOccurrence> numDifPeptides = getPeptideOcurrenceList(distiguishModificatedPeptides);
		final Set<String> peptidePlusCharge = new THashSet<String>();
		for (final PeptideOccurrence peptideOccurrence : numDifPeptides.values()) {
			final List<ExtendedIdentifiedPeptide> itemList = peptideOccurrence.getItemList();
			for (final ExtendedIdentifiedPeptide extendedIdentifiedPeptide : itemList) {
				final String key = extendedIdentifiedPeptide.getCharge()
						+ extendedIdentifiedPeptide.getKey(distiguishModificatedPeptides);
				peptidePlusCharge.add(key);
			}
		}
		final int total = peptidePlusCharge.size();
		log.debug(idSet.getName() + "---->Peptides  " + numDifPeptides + " = " + total);
		return total;
	}

	public int getNumPSMsForAPeptide(String sequenceKey) {
		if (peptideOccurrenceList.containsKey(sequenceKey)) {
			final PeptideOccurrence peptideOccurrence = peptideOccurrenceList.get(sequenceKey);
			return peptideOccurrence.getPeptides().size();
		}
		return 0;
	}

	@Override
	public String toString() {
		return "DatasetManager (" + this.getClass().getCanonicalName() + ") " + idSet.getName();
	}
}
