package org.proteored.miapeapi.experiment.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.jfree.data.statistics.Statistics;
import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.experiment.model.datamanager.DataManager;
import org.proteored.miapeapi.experiment.model.datamanager.ExperimentDataManager;
import org.proteored.miapeapi.experiment.model.filters.FDRFilter;
import org.proteored.miapeapi.experiment.model.filters.Filter;
import org.proteored.miapeapi.interfaces.ms.MiapeMSDocument;
import org.proteored.miapeapi.interfaces.ms.ResultingData;
import org.proteored.miapeapi.interfaces.ms.Spectrometer;
import org.proteored.miapeapi.interfaces.msi.InputParameter;
import org.proteored.miapeapi.interfaces.msi.MiapeMSIDocument;
import org.proteored.miapeapi.spring.SpringHandler;

import gnu.trove.map.hash.THashMap;
import gnu.trove.map.hash.TIntIntHashMap;
import gnu.trove.set.hash.THashSet;

/**
 * This class represents an experiment with several replicates
 *
 * @author Salva
 *
 */
public class Experiment implements IdentificationSet<Replicate> {
	private static final Logger log = Logger.getLogger("log4j.logger.org.proteored");
	// List of replicates
	private List<Replicate> replicates = new ArrayList<Replicate>();
	// Name of the experiment
	private String experimentName;
	private final ControlVocabularyManager cvManager;
	private File prideXMLFile;

	// {KEY=A string without meaning that identified a set of ; VALUE=Number of
	// Occurrences}

	private ExperimentDataManager dataManager;
	private ExperimentList previousLevelIdentificationSet;
	private final Integer minPeptideLength;
	private final boolean processInParallel;
	private final boolean doNotGroupNonConclusiveProteins;
	private final boolean separateNonConclusiveProteins;

	public Experiment(String experimentName, List<Replicate> replicates, List<Filter> filters,
			boolean doNotGroupNonConclusiveProteins, boolean separateNonConclusiveProteins, Integer minPeptideLength,
			ControlVocabularyManager cvManager, boolean processInParallel) {
		this.replicates = replicates;
		if (replicates != null)
			for (final Replicate replicate : replicates) {
				replicate.setPreviousLevelIdentificationSet(this);
			}
		this.experimentName = experimentName;
		if (cvManager != null)
			this.cvManager = cvManager;
		else
			this.cvManager = SpringHandler.getInstance().getCVManager();
		dataManager = new ExperimentDataManager(this, getNextLevelDataManagers(), filters,
				doNotGroupNonConclusiveProteins, separateNonConclusiveProteins, minPeptideLength, processInParallel);
		this.minPeptideLength = minPeptideLength;
		this.processInParallel = processInParallel;
		this.doNotGroupNonConclusiveProteins = doNotGroupNonConclusiveProteins;
		this.separateNonConclusiveProteins = separateNonConclusiveProteins;
	}

	/**
	 * Add a replicate to the list of replicates. All the internal status will
	 * be recalculated
	 *
	 * @param name
	 * @param sortingProteins
	 * @param sortingPeptides
	 * @param proteinFDRFilter
	 * @param peptideFDRFilter
	 * @param replicate
	 */
	public void addReplicate(String name, Replicate replicate, List<Filter> filters) {
		if (replicate != null) {
			replicates.add(replicate);
			replicate.setPreviousLevelIdentificationSet(this);
			dataManager = new ExperimentDataManager(this, getNextLevelDataManagers(), filters,
					doNotGroupNonConclusiveProteins, separateNonConclusiveProteins, minPeptideLength,
					processInParallel);
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof IdentificationSet) {
			return obj.toString().equals(toString());
		}
		return false;
	}

	public File getPrideXMLFile() {
		return prideXMLFile;
	}

	public void setPrideXMLFile(File prideXMLFile) {
		this.prideXMLFile = prideXMLFile;
	}

	/**
	 * @return the identificationSet
	 */
	@Override
	public DataManager getDataManager() {
		return dataManager;
	}

	/**
	 * Gets a list of the identification set from each replicate
	 *
	 * @return
	 */
	@Override
	public List<DataManager> getNextLevelDataManagers() {
		final List<Replicate> reps = getNextLevelIdentificationSetList();
		final List<DataManager> list = new ArrayList<DataManager>();
		for (final Replicate replicate : reps) {
			list.add(replicate.getDataManager());
		}
		return list;
	}

	public Replicate getReplicate(String replicateName) {
		for (final Replicate replicate : replicates) {
			if (replicate.getName().equals(replicateName))
				return replicate;
		}
		return null;
	}

	public List<Replicate> getReplicates() {
		return replicates;

	}

	/**
	 * Gets the identified proteins in all replicates. If there is any filter
	 * defined, the returned proteins will be filtered, sorted by Score.
	 *
	 * @return
	 */
	@Override
	public List<ProteinGroup> getIdentifiedProteinGroups() {
		// log.debug("Getting protein groups from experiment:" +
		// this.getName());
		final List<ProteinGroup> proteinGroups = dataManager.getIdentifiedProteinGroups();
		log.debug("returning " + proteinGroups.size() + " protein groups in experiment " + getName());
		return proteinGroups;
	}

	/**
	 * Gets a the number of times that a protein has been identified in a
	 * experiment
	 *
	 * @return
	 */
	@Override
	public int getProteinGroupOccurrenceNumber(ProteinGroup proteinGroup) {
		try {
			int num = 0;
			final List<Replicate> nextLevelIdentificationSetList = getNextLevelIdentificationSetList();
			for (final Replicate replicate : nextLevelIdentificationSetList) {
				num += replicate.getProteinGroupOccurrenceNumber(proteinGroup);
			}
			return num;
		} catch (final UnsupportedOperationException e) {
			return dataManager.getProteinGroupOccurrenceNumber(proteinGroup);
		}
	}

	/**
	 * Gets a Map that indicates how many times has been identified each protein
	 * in the total number of replicates.
	 *
	 * @return
	 */
	@Override
	public Map<String, ProteinGroupOccurrence> getProteinGroupOccurrenceList() {

		final Map<String, ProteinGroupOccurrence> proteinOccurrenceList = dataManager.getProteinGroupOccurrenceList();

		// int num = 0;
		// int numTripicates = 0;
		// for (IdentificationOccurrence<ExtendedIdentifiedProtein> occ :
		// proteinOccurrenceList
		// .values()) {
		// num = num + occ.getIdentificationItemList().size();
		// // System.out.println(occ.getIdentificationItemList().size());
		// if (occ.getIdentificationItemList().size() == 3)
		// numTripicates = numTripicates + 1;
		// }
		// System.out.println("total is = " + num);
		// System.out.println("num triplicates is = " + numTripicates);
		return proteinOccurrenceList;
	}

	/**
	 * Gets a Map that indicates how many times has been identified each peptide
	 * in the total number of replicates.
	 *
	 * @return
	 */
	@Override
	public Map<String, PeptideOccurrence> getPeptideOccurrenceList(boolean distinguishModPep) {
		log.debug("Getting peptide occurrences from:" + getName());
		final Map<String, PeptideOccurrence> peptideOcurrenceList = dataManager
				.getPeptideOcurrenceList(distinguishModPep);
		log.debug("Returning " + peptideOcurrenceList.size() + " peptides from experiment " + getName());
		return peptideOcurrenceList;
	}

	/**
	 * Gets the number of replicates in this experiment
	 *
	 * @return
	 */
	public int getNumReplicates() {
		return replicates.size();
	}

	@Override
	public double getAverageNumDifferentProteinGroups(boolean countNonConclusiveProteins) {
		final List<Integer> nums = new ArrayList<Integer>();
		for (final Replicate replicate : replicates) {
			nums.add(replicate.getNumDifferentProteinGroups(countNonConclusiveProteins));
		}
		return Statistics.calculateMean(nums);
	}

	@Override
	public double getAverageNumDifferentPeptides(boolean distPeptides) {
		final List<Integer> nums = new ArrayList<Integer>();
		for (final Replicate replicate : replicates) {
			nums.add(replicate.getNumDifferentPeptides(distPeptides));
		}
		return Statistics.calculateMean(nums);
	}

	@Override
	public double getStdNumDifferentProteinGroups(boolean countNonConclusiveProteins) {
		final List<Integer> nums = new ArrayList<Integer>();
		if (replicates.size() < 2)
			return 0;
		for (final Replicate replicate : replicates) {
			nums.add(replicate.getNumDifferentProteinGroups(countNonConclusiveProteins));
		}
		final double stdDev = Statistics.getStdDev(nums.toArray(new Integer[0]));
		return stdDev;
	}

	@Override
	public double getStdNumDifferentPeptides(boolean distPeptides) {
		final List<Integer> nums = new ArrayList<Integer>();
		if (replicates.size() < 2)
			return 0;
		for (final Replicate replicate : replicates) {
			nums.add(replicate.getNumDifferentPeptides(distPeptides));
		}
		return Statistics.getStdDev(nums.toArray(new Integer[0]));
	}

	@Override
	public int getTotalNumProteinGroups(boolean countNonConclusiveProteins) {
		return dataManager.getTotalNumProteinGroups(countNonConclusiveProteins);
	}

	/**
	 * Gets a list of peptides (ordered by score) that pass the FDR threshold
	 * defined by a {@link FDRFilter} that should be setted before this call.
	 *
	 *
	 * @return
	 */
	@Override
	public List<ExtendedIdentifiedPeptide> getIdentifiedPeptides() {
		log.debug("Getting peptides from experiment:" + getName());
		final List<ExtendedIdentifiedPeptide> identifiedPeptides = dataManager.getIdentifiedPeptides();
		log.debug("returning " + identifiedPeptides.size() + " peptides from experiment " + getName());
		return identifiedPeptides;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("\tExperiment " + experimentName + ":\n");
		sb.append("\t\t\t\tNumber of protein groups:" + getTotalNumProteinGroups(false) + "\n");
		sb.append("\t\t\t\tNumber of different protein groups:" + getNumDifferentProteinGroups(false) + "\n");
		sb.append("\t\t\t\tNumber of proteins:" + getIdentifiedProteins().size() + "\n");
		sb.append("\t\t\t\tNumber of peptides:" + getTotalNumPeptides() + "\n");
		sb.append("\t\t\t\tNumber of different peptides:" + getNumDifferentPeptides(true) + "\n");
		sb.append("\t\t" + replicates.size() + " replicates:\n");
		for (final Replicate replicate : replicates) {
			sb.append("\t\t" + replicate);
		}
		return sb.toString();
	}

	public static void printProteinOcurrenceList(List<ProteinGroupOccurrence> proteinGroupOccurrences) {
		System.out.println();
		int previousSize = 10;
		for (final ProteinGroupOccurrence po : proteinGroupOccurrences) {
			final int size = po.getItemList().size();
			if (size > previousSize)
				System.out.print("ERROR");
			previousSize = size;
			System.out.print(size + ",");
		}
		System.out.println();
	}

	@Override
	public String getName() {
		return experimentName;
	}

	@Override
	public String getFullName() {
		return getName();
	}

	@Override
	public void setName(String name) {
		experimentName = name;
	}

	@Override
	public int getPeptideOccurrenceNumber(String sequence, boolean distModPep) {
		return dataManager.getPeptideOccurrenceNumber(sequence, distModPep);
	}

	@Override
	public List<Replicate> getNextLevelIdentificationSetList() {
		final List<Replicate> ret = new ArrayList<Replicate>();
		for (final Replicate replicate : replicates) {
			ret.add(replicate);
		}
		return ret;
	}

	@Override
	public ExperimentList getPreviousLevelIdentificationSet() {
		return previousLevelIdentificationSet;
	}

	public void setPreviousLevelIdentificationSet(ExperimentList previousLevelIdentificationSet) {
		this.previousLevelIdentificationSet = previousLevelIdentificationSet;
	}

	@Override
	public int getNumDifferentPeptides(boolean distiguishModificatedPeptides) {
		return dataManager.getNumDifferentPeptides(distiguishModificatedPeptides);
	}

	@Override
	public boolean hasProteinGroup(ProteinGroup proteinGroup) {
		return dataManager.hasProteinGroup(proteinGroup);
	}

	@Override
	public boolean hasPeptide(String sequence, boolean distModPep) {
		return dataManager.hasPeptide(sequence, distModPep);
	}

	@Override
	public int getNumDifferentProteinGroups(boolean countNonConclusiveProteins) {
		return dataManager.getNumDifferentProteinGroups(countNonConclusiveProteins);
	}

	@Override
	public List<String> getProteinScoreNames() {
		final List<String> ret = new ArrayList<String>();
		for (final Replicate iterable_element : replicates) {
			final List<String> scoreNames = iterable_element.getProteinScoreNames();
			for (final String scoreName : scoreNames) {
				if (!ret.contains(scoreName))
					ret.add(scoreName);
			}
		}
		return ret;
	}

	@Override
	public List<String> getPeptideScoreNames() {
		final List<String> ret = new ArrayList<String>();
		for (final Replicate iterable_element : replicates) {
			final List<String> scoreNames = iterable_element.getPeptideScoreNames();
			for (final String scoreName : scoreNames) {
				if (!ret.contains(scoreName))
					ret.add(scoreName);
			}
		}
		return ret;
	}

	@Override
	public List<String> getDifferentPeptideModificationNames() {
		final List<String> ret = new ArrayList<String>();
		for (final Replicate replicate : replicates) {
			final List<String> modifs = replicate.getDifferentPeptideModificationNames();
			for (final String modif : modifs) {
				if (!ret.contains(modif))
					ret.add(modif);
			}
		}
		Collections.sort(ret);
		return ret;
	}

	@Override
	public int getModificatedSiteOccurrence(String modif) {
		return dataManager.getModificatedSiteOccurrence(modif);
	}

	@Override
	public TIntIntHashMap getModificationOccurrenceDistribution(String modif) {
		return dataManager.getModificationOccurrenceDistribution(modif);
	}

	@Override
	public void setFilters(List<Filter> filters) {
		dataManager.setFilters(filters);
		for (final Replicate idSet : getNextLevelIdentificationSetList())
			idSet.setFilters(filters);
	}

	@Override
	public void removeFilters() {
		dataManager.removeFilters();

	}

	@Override
	public int getProteinGroupTP(Set<String> truePositiveProteinACCs, boolean countNonConclusiveProteins) {
		return dataManager.getProteinGroupTP(truePositiveProteinACCs, countNonConclusiveProteins);
	}

	@Override
	public int getProteinGroupFN(Set<String> truePositiveProteinACCs, boolean countNonConclusiveProteins) {
		return dataManager.getProteinGroupFN(truePositiveProteinACCs, countNonConclusiveProteins);
	}

	@Override
	public int getProteinGroupTN(Set<String> truePositiveProteinACCs, boolean countNonConclusiveProteins) {
		return dataManager.getProteinGroupTN(truePositiveProteinACCs, countNonConclusiveProteins);
	}

	@Override
	public int getProteinGroupFP(Set<String> truePositiveProteinACCs, boolean countNonConclusiveProteins) {
		return dataManager.getProteinGroupFP(truePositiveProteinACCs, countNonConclusiveProteins);
	}

	@Override
	public int getPeptideTP(Set<String> positivePeptideSequences, boolean distinguisModificatedPeptides) {
		return dataManager.getPeptideTP(positivePeptideSequences, distinguisModificatedPeptides);
	}

	@Override
	public int getPeptideFN(Set<String> positivePeptideSequences, boolean distinguisModificatedPeptides) {
		return dataManager.getPeptideFN(positivePeptideSequences, distinguisModificatedPeptides);
	}

	@Override
	public int getPeptideTN(Set<String> positivePeptideSequences, boolean distinguisModificatedPeptides) {
		return dataManager.getPeptideTN(positivePeptideSequences, distinguisModificatedPeptides);
	}

	@Override
	public int getPeptideFP(Set<String> positivePeptideSequences, boolean distinguisModificatedPeptides) {
		return dataManager.getPeptideFP(positivePeptideSequences, distinguisModificatedPeptides);
	}

	// @Override
	// public FDRFilter getFDRFilter() {
	// return this.dataManager.getFDRFilter();
	// }

	@Override
	public float getProteinFDR(String scoreName) {
		return dataManager.getProteinFDR(scoreName);
	}

	@Override
	public float getPeptideFDR(String scoreName) {
		return dataManager.getPeptideFDR(scoreName);
	}

	@Override
	public float getPSMFDR(String scoreName) {
		return dataManager.getPSMFDR(scoreName);
	}

	@Override
	public ProteinGroupOccurrence getProteinGroupOccurrence(ProteinGroup proteinGroup) {
		return dataManager.getProteinGroupOccurrence(proteinGroup);
	}

	@Override
	public PeptideOccurrence getPeptideOccurrence(String sequence, boolean distinguishModPep) {
		return dataManager.getPeptideOccurrence(sequence, distinguishModPep);
	}

	@Override
	public TIntIntHashMap getMissedCleavagesOccurrenceDistribution(String cleavageAminoacids) {
		return dataManager.getMissedCleavagesOccurrenceDistribution(cleavageAminoacids);
	}

	@Override
	public int getPeptideModificatedOccurrence(String modification) {
		return dataManager.getPeptideModificatedOccurrence(modification);
	}

	@Override
	public double getAverageTotalNumProteinGroups(boolean countNonConclusiveProteins) {
		final List<Integer> nums = new ArrayList<Integer>();
		for (final Replicate replicate : replicates) {
			nums.add(replicate.getTotalNumProteinGroups(countNonConclusiveProteins));
		}
		return Statistics.calculateMean(nums);
	}

	@Override
	public double getAverageTotalNumPeptides() {
		final List<Integer> nums = new ArrayList<Integer>();
		for (final Replicate replicate : replicates) {
			nums.add(replicate.getTotalNumPeptides());
		}
		return Statistics.calculateMean(nums);
	}

	@Override
	public int getTotalNumPeptides() {
		return dataManager.getTotalNumPeptides();
	}

	@Override
	public double getAverageTotalVsDifferentNumProteinGroups(boolean countNonConclusiveProteins) {
		final List<Double> nums = new ArrayList<Double>();
		for (final Replicate replicate : replicates) {
			nums.add(replicate.getTotalVsDifferentNumProteinGroups(countNonConclusiveProteins));
		}
		return Statistics.calculateMean(nums);

	}

	@Override
	public double getTotalVsDifferentNumProteinGroups(boolean countNonConclusiveProteins) {
		final int totalNumProteins = getTotalNumProteinGroups(countNonConclusiveProteins);
		if (totalNumProteins > 0) {

			return Double.valueOf(getNumDifferentProteinGroups(countNonConclusiveProteins))
					/ Double.valueOf(totalNumProteins);
		}
		return 0.0;
	}

	@Override
	public double getAverageTotalVsDifferentNumPeptides(boolean distModPep) {
		final List<Double> nums = new ArrayList<Double>();
		for (final Replicate replicate : replicates) {
			nums.add(replicate.getTotalVsDifferentNumPeptides(distModPep));
		}
		return Statistics.calculateMean(nums);

	}

	@Override
	public double getTotalVsDifferentNumPeptides(boolean distModPep) {
		final int totalNumPeptides = getTotalNumPeptides();
		if (totalNumPeptides > 0)
			return Double.valueOf(getNumDifferentPeptides(distModPep)) / Double.valueOf(totalNumPeptides);
		return 0.0;
	}

	@Override
	public double getStdTotalVsDifferentNumProteinGroups(boolean countNonConclusiveProteins) {
		final List<Double> nums = new ArrayList<Double>();
		for (final Replicate replicate : replicates) {
			nums.add(replicate.getTotalVsDifferentNumProteinGroups(countNonConclusiveProteins));
		}
		return Statistics.getStdDev(nums.toArray(new Integer[0]));
	}

	@Override
	public double getStdTotalVsDifferentNumPeptides(Boolean distModPeptides) {
		final List<Double> nums = new ArrayList<Double>();
		for (final Replicate replicate : replicates) {
			nums.add(replicate.getTotalVsDifferentNumPeptides(distModPeptides));
		}
		return Statistics.getStdDev(nums.toArray(new Integer[0]));
	}

	@Override
	public double getStdTotalNumProteinGroups(boolean countNonConclusiveProteins) {
		final List<Integer> nums = new ArrayList<Integer>();
		if (replicates.size() < 2)
			return 0;
		for (final Replicate replicate : replicates) {
			nums.add(replicate.getTotalNumProteinGroups(countNonConclusiveProteins));
		}
		final double stdDev = Statistics.getStdDev(nums.toArray(new Integer[0]));
		return stdDev;
	}

	@Override
	public double getStdTotalNumPeptides() {
		final List<Integer> nums = new ArrayList<Integer>();
		if (replicates.size() < 2)
			return 0;
		for (final Replicate replicate : replicates) {
			nums.add(replicate.getTotalNumPeptides());
		}
		return Statistics.getStdDev(nums.toArray(new Integer[0]));
	}

	@Override
	public Set<String> getDifferentSearchedDatabases() {
		return dataManager.getDifferentSearchedDatabases();
	}

	@Override
	public List<ResultingData> getResultingDatas() {
		final List<ResultingData> ret = new ArrayList<ResultingData>();
		for (final Replicate replicate : replicates) {
			ret.addAll(replicate.getResultingDatas());
		}
		return ret;
	}

	@Override
	public List<Filter> getFilters() {
		return dataManager.getFilters();
	}

	@Override
	public int getNumProteinGroupDecoys() {
		return dataManager.getNumProteinGroupDecoys();
	}

	@Override
	public int getNumDifferentProteinGroupsDecoys() {
		return dataManager.getNumDifferentProteinGroupsDecoys();
	}

	@Override
	public int getNumPeptideDecoys() {
		return dataManager.getNumPeptideDecoys();
	}

	@Override
	public int getNumDifferentPeptideDecoys(boolean distringuishModificatedPeptides) {
		return dataManager.getNumDifferentPeptideDecoys(distringuishModificatedPeptides);
	}

	@Override
	public ProteinGroupOccurrence getProteinGroupOccurrence(String proteinACC) {
		return dataManager.getProteinGroupOccurrence(proteinACC);
	}

	@Override
	public int getProteinGroupOccurrenceNumber(String proteinACC) {
		return dataManager.getProteinGroupOccurrenceNumber(proteinACC);
	}

	@Override
	public FDRFilter getFDRFilter() {
		return dataManager.getFDRFilter();
	}

	@Override
	public ControlVocabularyManager getCvManager() {
		return cvManager;
	}

	@Override
	public void setFiltered(boolean b) {
		if (dataManager != null)
			dataManager.setFiltered(b);

	}

	@Override
	public List<MiapeMSDocument> getMiapeMSs() {
		final List<MiapeMSDocument> ret = new ArrayList<MiapeMSDocument>();
		for (final Replicate replicate : getReplicates()) {
			ret.addAll(replicate.getMiapeMSs());
		}
		return ret;
	}

	@Override
	public List<MiapeMSIDocument> getMiapeMSIs() {
		final List<MiapeMSIDocument> ret = new ArrayList<MiapeMSIDocument>();
		for (final Replicate replicate : getReplicates()) {
			ret.addAll(replicate.getMiapeMSIs());
		}
		return ret;
	}

	public Map<Replicate, List<ResultingData>> getPeakListResultingDataMap() {
		final Map<Replicate, List<ResultingData>> ret = new THashMap<Replicate, List<ResultingData>>();
		if (getReplicates() != null) {
			for (final Replicate replicate : getReplicates()) {
				final List<ResultingData> resultingDatas = replicate.getPeakListResultingDatas();
				if (resultingDatas != null && !resultingDatas.isEmpty()) {
					ret.put(replicate, resultingDatas);
				}
			}
		}
		if (!ret.isEmpty())
			return ret;
		return null;
	}

	public Map<Replicate, List<ResultingData>> getRawFileResultingDataMap() {
		final Map<Replicate, List<ResultingData>> ret = new THashMap<Replicate, List<ResultingData>>();
		if (getReplicates() != null) {
			for (final Replicate replicate : getReplicates()) {
				final List<ResultingData> resultingDatas = replicate.getRawFileResultingDatas();
				if (resultingDatas != null && !resultingDatas.isEmpty()) {
					ret.put(replicate, resultingDatas);
				}
			}
		}
		if (!ret.isEmpty())
			return ret;
		return null;
	}

	@Override
	public List<ExtendedIdentifiedProtein> getIdentifiedProteins() {
		return dataManager.getIdentifiedProteins();
	}

	@Override
	public List<ProteinGroup> getNonFilteredIdentifiedProteinGroups() {
		return dataManager.getNonFilteredIdentifiedProteinGroups();
	}

	@Override
	public List<ExtendedIdentifiedPeptide> getNonFilteredIdentifiedPeptides() {
		return dataManager.getNonFilteredIdentifiedPeptides();
	}

	@Override
	public int getTotalNumNonConclusiveProteinGroups() {
		return dataManager.getNumNonConclusiveProteinGroups();
	}

	@Override
	public int getNumDifferentNonConclusiveProteinGroups() {
		return dataManager.getNumDifferentNonConclusiveProteinGroups();
	}

	@Override
	public Set<Spectrometer> getSpectrometers() {
		final List<Replicate> replicates = getReplicates();
		final Set<Spectrometer> spectrometers = new THashSet<Spectrometer>();
		for (final Replicate replicate : replicates) {
			spectrometers.addAll(replicate.getSpectrometers());
		}
		return spectrometers;
	}

	@Override
	public Set<InputParameter> getInputParameters() {
		final List<Replicate> replicates = getReplicates();
		final Set<InputParameter> inputParameters = new THashSet<InputParameter>();
		for (final Replicate replicate : replicates) {
			inputParameters.addAll(replicate.getInputParameters());
		}
		return inputParameters;
	}

	@Override
	public int getNumDifferentPeptidesPlusCharge(boolean distiguishModificatedPeptides) {
		return dataManager.getNumDifferentPeptidesPlusCharge(distiguishModificatedPeptides);
	}

	@Override
	public Map<String, PeptideOccurrence> getPeptideChargeOccurrenceList(boolean distinguishModPep) {
		return dataManager.getPeptideChargeOcurrenceList(distinguishModPep);
	}

	@Override
	public PeptideOccurrence getPeptideChargeOccurrence(String sequencePlusChargeKey, boolean b) {
		return dataManager.getPeptideChargeOccurrence(sequencePlusChargeKey, b);

	}

	@Override
	public int getPeptideChargeOccurrenceNumber(String sequencePlusChargeKey, Boolean distinguishModPep) {
		return dataManager.getPeptideChargeOccurrenceNumber(sequencePlusChargeKey, distinguishModPep);
	}

	@Override
	public int getProteinGroupOccurrenceNumberByProteinGroupKey(String proteinGroupKey) {
		return dataManager.getProteinGroupOccurrenceNumberByProteinGroupKey(proteinGroupKey);
	}

	@Override
	public int getNumPSMsForAPeptide(String sequenceKey) {
		return dataManager.getNumPSMsForAPeptide(sequenceKey);
	}

	@Override
	public ProteinGroupOccurrence getProteinGroupOccurrenceByProteinGroupKey(String proteinGroupKey) {
		return dataManager.getProteinGroupOccurrenceByProteinGroupKey(proteinGroupKey);
	}
}
