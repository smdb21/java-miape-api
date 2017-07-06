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
import org.proteored.miapeapi.experiment.model.datamanager.ExperimentListDataManager;
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

public class ExperimentList implements IdentificationSet<Experiment> {
	private static final Logger log = Logger.getLogger("log4j.logger.org.proteored");
	// List of experiments
	private List<Experiment> experiments = new ArrayList<Experiment>();
	// Identification set
	private final ExperimentListDataManager dataManager;
	private String name;
	private final ControlVocabularyManager cvManager;

	public ExperimentList(String name, List<Experiment> experimentList, boolean groupingAtExperimentListLevel,
			List<Filter> filters, Integer minPeptideLength, ControlVocabularyManager cvManager,
			boolean processInParallel) {
		if (experimentList != null) {
			experiments = experimentList;
			for (Experiment experiment : experiments) {
				experiment.setPreviousLevelIdentificationSet(this);
			}
		}
		if (cvManager != null)
			this.cvManager = cvManager;
		else
			this.cvManager = SpringHandler.getInstance().getCVManager();
		this.name = name;
		dataManager = new ExperimentListDataManager(this, getDataManagers(experimentList),
				groupingAtExperimentListLevel, filters, minPeptideLength, processInParallel);
	}

	private List<DataManager> getDataManagers(List<Experiment> expList) {
		List<DataManager> ret = new ArrayList<DataManager>();
		if (expList != null)
			for (Experiment experiment : expList) {
				ret.add(experiment.getDataManager());
			}
		return ret;
	}

	public List<File> getPrideXmlFiles() {
		List<File> files = new ArrayList<File>();
		for (Experiment exp : getExperiments()) {
			File prideXMLFile = exp.getPrideXMLFile();
			if (prideXMLFile != null)
				files.add(prideXMLFile);
		}
		return files;
	}

	public List<Experiment> getExperiments() {
		return experiments;
	}

	public void addExperiment(Experiment exp) {
		experiments.add(exp);
		dataManager.setFiltered(false);
	}

	public int getNumExperiments() {
		return experiments.size();
	}

	/**
	 * Gets the total number of replicates over the set of experiments
	 *
	 * @return
	 */
	public int getNumReplicates() {
		int numReplicates = 0;
		for (Experiment experiment : experiments) {
			numReplicates += experiment.getNextLevelIdentificationSetList().size();
		}
		return numReplicates;
	}

	/**
	 * Gets the number of proteins identified over the experiments taking into
	 * account the filter
	 *
	 * @return
	 */
	@Override
	public int getTotalNumProteinGroups(boolean countNonConclusiveProteins) {
		return dataManager.getTotalNumProteinGroups(countNonConclusiveProteins);
	}

	@Override
	public List<ProteinGroup> getIdentifiedProteinGroups() {
		log.debug("Getting proteins from experiment list:" + getName());
		final List<ProteinGroup> proteinGroups = dataManager.getIdentifiedProteinGroups();
		log.debug("returning " + proteinGroups.size() + " protein groups in experiment list " + getName());
		return proteinGroups;
	}

	/**
	 * Gets a the number of times that a protein has been identified in a set of
	 * experiments
	 *
	 * @return
	 */
	@Override
	public int getProteinGroupOccurrenceNumber(ProteinGroup proteinGroup) {
		try {
			int num = 0;
			List<Experiment> nextLevelIdentificationSetList = getNextLevelIdentificationSetList();
			for (Experiment experiment : nextLevelIdentificationSetList) {
				num += experiment.getProteinGroupOccurrenceNumber(proteinGroup);
			}
			return num;
		} catch (UnsupportedOperationException e) {
			return dataManager.getProteinGroupOccurrenceNumber(proteinGroup);
		}
	}

	@Override
	public int getProteinGroupOccurrenceNumberByProteinGroupKey(String proteinGroupKey) {
		return dataManager.getProteinGroupOccurrenceNumberByProteinGroupKey(proteinGroupKey);
	}

	/**
	 * Gets a hashmap that indicates how many times has been identified each
	 * peptide in the total number of experiments.
	 *
	 * @return
	 */
	@Override
	public Map<String, PeptideOccurrence> getPeptideOccurrenceList(boolean distinguishModPep) {
		log.info("Getting peptide occurrence list from experiment list:" + getName());
		final Map<String, PeptideOccurrence> peptideOcurrenceList = dataManager
				.getPeptideOcurrenceList(distinguishModPep);
		log.info("returning " + peptideOcurrenceList.size() + " different peptides from experiment list: " + getName());
		return peptideOcurrenceList;
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
		log.info("Getting peptides from experiment list:" + getName());
		final List<ExtendedIdentifiedPeptide> identifiedPeptides = dataManager.getIdentifiedPeptides();
		log.info("returning " + identifiedPeptides.size() + " peptides in experiment list " + getName());
		return identifiedPeptides;
	}

	@Override
	public String toString() {
		;

		StringBuilder sb = new StringBuilder();
		sb.append("\tExperiment list " + name + ":\n");
		sb.append("\t\t\t\tNumber of protein groups:" + getTotalNumProteinGroups(false) + "\n");
		sb.append("\t\t\t\tNumber of different protein groups:" + getNumDifferentProteinGroups(false) + "\n");
		sb.append("\t\t\t\tNumber of proteins:" + getIdentifiedProteins().size() + "\n");
		sb.append("\t\t\t\tNumber of peptides:" + getTotalNumPeptides() + "\n");
		sb.append("\t\t\t\tNumber of different peptides:" + getNumDifferentPeptides(true) + "\n");
		sb.append("\t\t" + experiments.size() + " experiments:\n");
		for (Experiment experiment : experiments) {
			sb.append("\t\t" + experiment);
		}
		return sb.toString();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getFullName() {
		return getName();
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Map<String, ProteinGroupOccurrence> getProteinGroupOccurrenceList() {
		return dataManager.getProteinGroupOccurrenceList();
	}

	@Override
	public int getPeptideOccurrenceNumber(String sequence, boolean distModPep) {
		return dataManager.getPeptideOccurrenceNumber(sequence, distModPep);
	}

	@Override
	public double getAverageNumDifferentProteinGroups(boolean countNonConclusiveProteins) {
		List<Integer> nums = new ArrayList<Integer>();
		for (Experiment experiment : experiments) {
			nums.add(experiment.getNumDifferentProteinGroups(countNonConclusiveProteins));
		}
		return Statistics.calculateMean(nums);
	}

	@Override
	public double getAverageNumDifferentPeptides(boolean distPeptides) {
		List<Integer> nums = new ArrayList<Integer>();
		for (Experiment experiment : experiments) {
			nums.add(experiment.getNumDifferentPeptides(distPeptides));
		}
		return Statistics.calculateMean(nums);
	}

	@Override
	public double getStdNumDifferentProteinGroups(boolean countNonConclusiveProteins) {
		List<Integer> nums = new ArrayList<Integer>();
		if (experiments.size() < 2)
			return 0;
		for (Experiment experiment : experiments) {
			nums.add(experiment.getNumDifferentProteinGroups(countNonConclusiveProteins));
		}
		return Statistics.getStdDev(nums.toArray(new Integer[0]));
	}

	@Override
	public double getStdNumDifferentPeptides(boolean distPeptides) {
		List<Integer> nums = new ArrayList<Integer>();
		if (experiments.size() < 2)
			return 0;
		for (Experiment experiment : experiments) {
			nums.add(experiment.getNumDifferentPeptides(distPeptides));
		}
		return Statistics.getStdDev(nums.toArray(new Integer[0]));
	}

	@Override
	public List<Experiment> getNextLevelIdentificationSetList() {
		List<Experiment> ret = new ArrayList<Experiment>();
		for (Experiment experiment : experiments) {
			ret.add(experiment);
		}
		return ret;
	}

	@Override
	public IdentificationSet getPreviousLevelIdentificationSet() {
		throw new UnsupportedOperationException();
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
		List<String> ret = new ArrayList<String>();
		for (Experiment iterable_element : experiments) {
			List<String> scoreNames = iterable_element.getProteinScoreNames();
			for (String scoreName : scoreNames) {
				if (!ret.contains(scoreName))
					ret.add(scoreName);
			}
		}
		return ret;
	}

	@Override
	public List<String> getPeptideScoreNames() {
		List<String> ret = new ArrayList<String>();
		for (Experiment iterable_element : experiments) {
			List<String> scoreNames = iterable_element.getPeptideScoreNames();
			for (String scoreName : scoreNames) {
				if (!ret.contains(scoreName))
					ret.add(scoreName);
			}
		}
		return ret;
	}

	@Override
	public List<String> getDifferentPeptideModificationNames() {
		List<String> ret = new ArrayList<String>();
		for (Experiment experiment : experiments) {
			List<String> modifs = experiment.getDifferentPeptideModificationNames();
			for (String modif : modifs) {
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
	public TIntIntHashMap getModificationOccurrenceDistribution(String modifs) {
		return dataManager.getModificationOccurrenceDistribution(modifs);
	}

	@Override
	public DataManager getDataManager() {
		return dataManager;
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
	public void setFilters(List<Filter> filters) {
		dataManager.setFilters(filters);
		for (Experiment idSet : getNextLevelIdentificationSetList()) {
			idSet.setFilters(filters);
		}
	}

	@Override
	public void removeFilters() {
		dataManager.removeFilters();
	}

	@Override
	public List<DataManager> getNextLevelDataManagers() {
		List<Experiment> exps = getNextLevelIdentificationSetList();
		List<DataManager> list = new ArrayList<DataManager>();
		for (Experiment experiment : exps) {
			list.add(experiment.getDataManager());
		}
		return list;
	}

	@Override
	public float getProteinFDR(String scoreName) {
		if (validFDRCalculation())
			return dataManager.getProteinFDR(scoreName);
		return -1;
	}

	@Override
	public float getPeptideFDR(String scoreName) {
		if (validFDRCalculation())
			return dataManager.getPeptideFDR(scoreName);
		return -1;
	}

	@Override
	public float getPSMFDR(String scoreName) {
		if (validFDRCalculation())
			return dataManager.getPSMFDR(scoreName);
		return -1;
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
	public TIntIntHashMap getMissedCleavagesOccurrenceDistribution() {
		return dataManager.getMissedCleavagesOccurrenceDistribution();
	}

	@Override
	public int getPeptideModificatedOccurrence(String modification) {
		return dataManager.getPeptideModificatedOccurrence(modification);
	}

	@Override
	public double getAverageTotalNumProteinGroups(boolean countNonConclusiveProteins) {
		List<Integer> nums = new ArrayList<Integer>();
		for (Experiment replicate : experiments) {
			nums.add(replicate.getTotalNumProteinGroups(countNonConclusiveProteins));
		}
		return Statistics.calculateMean(nums);
	}

	@Override
	public double getAverageTotalNumPeptides() {
		List<Integer> nums = new ArrayList<Integer>();
		for (Experiment replicate : experiments) {
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
		List<Double> nums = new ArrayList<Double>();
		for (Experiment replicate : experiments) {
			nums.add(replicate.getTotalVsDifferentNumProteinGroups(countNonConclusiveProteins));
		}
		return Statistics.calculateMean(nums);

	}

	@Override
	public double getTotalVsDifferentNumProteinGroups(boolean countNonConclusiveProteins) {
		final int totalNumProteins = getTotalNumProteinGroups(countNonConclusiveProteins);
		if (totalNumProteins > 0)
			return Double.valueOf(getNumDifferentProteinGroups(countNonConclusiveProteins))
					/ Double.valueOf(totalNumProteins);
		return 0.0;
	}

	@Override
	public double getAverageTotalVsDifferentNumPeptides(boolean distModPep) {
		List<Double> nums = new ArrayList<Double>();
		for (Experiment replicate : experiments) {
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
		List<Double> nums = new ArrayList<Double>();
		for (Experiment replicate : experiments) {
			nums.add(replicate.getTotalVsDifferentNumProteinGroups(countNonConclusiveProteins));
		}
		return Statistics.getStdDev(nums.toArray(new Integer[0]));
	}

	@Override
	public double getStdTotalVsDifferentNumPeptides(Boolean distModPeptides) {
		List<Double> nums = new ArrayList<Double>();
		for (Experiment replicate : experiments) {
			nums.add(replicate.getTotalVsDifferentNumPeptides(distModPeptides));
		}
		return Statistics.getStdDev(nums.toArray(new Integer[0]));
	}

	@Override
	public double getStdTotalNumProteinGroups(boolean countNonConclusiveProteins) {
		List<Integer> nums = new ArrayList<Integer>();
		if (experiments.size() < 2)
			return 0;
		for (Experiment replicate : experiments) {
			nums.add(replicate.getTotalNumProteinGroups(countNonConclusiveProteins));
		}
		final double stdDev = Statistics.getStdDev(nums.toArray(new Integer[0]));
		return stdDev;
	}

	@Override
	public double getStdTotalNumPeptides() {
		List<Integer> nums = new ArrayList<Integer>();
		if (experiments.size() < 2)
			return 0;
		for (Experiment replicate : experiments) {
			nums.add(replicate.getTotalNumPeptides());
		}
		return Statistics.getStdDev(nums.toArray(new Integer[0]));
	}

	public boolean validFDRCalculation() {
		if (experiments != null) {
			Map<String, List<String>> scoreNames = new THashMap<String, List<String>>();
			for (Experiment idSet : experiments) {

				Experiment exp = idSet;

				final List<String> peptideScoreNames = exp.getPeptideScoreNames();
				if (!scoreNames.isEmpty()) {
					for (String repName : scoreNames.keySet()) {
						final List<String> scores = scoreNames.get(repName);
						if (!hasOneElementInCommon(scores, peptideScoreNames))
							return false;
					}
				}
				scoreNames.put(exp.getName(), peptideScoreNames);

			}
		}
		return true;
	}

	private boolean hasOneElementInCommon(List<String> list1, List<String> list2) {
		for (String string1 : list1) {
			for (String string2 : list2) {
				if (string1.equals(string2))
					return true;
			}
		}
		return false;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof IdentificationSet) {
			return obj.toString().equals(toString());
		}
		return false;
	}

	@Override
	public Set<String> getDifferentSearchedDatabases() {
		return dataManager.getDifferentSearchedDatabases();
	}

	@Override
	public List<ResultingData> getResultingDatas() {
		List<ResultingData> ret = new ArrayList<ResultingData>();
		for (Experiment experiment : experiments) {
			ret.addAll(experiment.getResultingDatas());
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
		List<MiapeMSDocument> ret = new ArrayList<MiapeMSDocument>();
		for (Experiment exp : getExperiments()) {
			ret.addAll(exp.getMiapeMSs());
		}
		return ret;
	}

	@Override
	public List<MiapeMSIDocument> getMiapeMSIs() {
		List<MiapeMSIDocument> ret = new ArrayList<MiapeMSIDocument>();
		for (Experiment exp : getExperiments()) {
			ret.addAll(exp.getMiapeMSIs());
		}
		return ret;
	}

	public Map<Replicate, List<ResultingData>> getPeakListResultingDataMapByReplicate() {
		Map<Replicate, List<ResultingData>> ret = new THashMap<Replicate, List<ResultingData>>();
		if (getExperiments() != null) {
			for (Experiment exp : getExperiments()) {
				Map<Replicate, List<ResultingData>> resultingDataMap = exp.getPeakListResultingDataMap();
				if (resultingDataMap != null && !resultingDataMap.isEmpty()) {
					for (Replicate replicateKey : resultingDataMap.keySet()) {
						if (ret.containsKey(replicateKey)) {
							ret.get(replicateKey).addAll(resultingDataMap.get(replicateKey));
						} else {
							ret.put(replicateKey, resultingDataMap.get(replicateKey));
						}
					}
				}
			}
		}
		if (!ret.isEmpty())
			return ret;
		return null;
	}

	public Map<Replicate, List<ResultingData>> getRawFileResultingDataMapByReplicate() {
		Map<Replicate, List<ResultingData>> ret = new THashMap<Replicate, List<ResultingData>>();
		if (getExperiments() != null) {
			for (Experiment exp : getExperiments()) {
				Map<Replicate, List<ResultingData>> resultingDataMap = exp.getRawFileResultingDataMap();
				if (resultingDataMap != null && !resultingDataMap.isEmpty()) {
					for (Replicate replicateKey : resultingDataMap.keySet()) {
						if (ret.containsKey(replicateKey)) {
							ret.get(replicateKey).addAll(resultingDataMap.get(replicateKey));
						} else {
							ret.put(replicateKey, resultingDataMap.get(replicateKey));
						}
					}
				}
			}
		}
		if (!ret.isEmpty())
			return ret;
		return null;
	}

	public Map<Experiment, List<ResultingData>> getPeakListResultingDataMapByExperiment() {
		Map<Experiment, List<ResultingData>> ret = new THashMap<Experiment, List<ResultingData>>();
		if (getExperiments() != null) {
			for (Experiment exp : getExperiments()) {
				Map<Replicate, List<ResultingData>> resultingDataMap = exp.getPeakListResultingDataMap();
				if (resultingDataMap != null && !resultingDataMap.isEmpty()) {
					for (List<ResultingData> resultingDatas : resultingDataMap.values()) {
						ret.put(exp, resultingDatas);
					}
				}
			}
		}
		if (!ret.isEmpty())
			return ret;
		return null;
	}

	public Map<Experiment, List<ResultingData>> getRawFileResultingDataMapByExperiment() {
		Map<Experiment, List<ResultingData>> ret = new THashMap<Experiment, List<ResultingData>>();
		if (getExperiments() != null) {
			for (Experiment exp : getExperiments()) {
				Map<Replicate, List<ResultingData>> resultingDataMap = exp.getRawFileResultingDataMap();
				if (resultingDataMap != null && !resultingDataMap.isEmpty()) {
					for (List<ResultingData> resultingDatas : resultingDataMap.values()) {
						ret.put(exp, resultingDatas);
					}
				}
			}
		}
		if (!ret.isEmpty())
			return ret;
		return null;
	}

	public Map<Replicate, List<String>> getMSIGeneratedFilesByReplicate() {
		Map<Replicate, List<String>> ret = new THashMap<Replicate, List<String>>();
		if (getExperiments() != null) {
			for (Experiment exp : getExperiments()) {
				for (Replicate rep : exp.getReplicates()) {
					List<String> generatedFiles = rep.getMSIGeneratedFiles();
					ret.put(rep, generatedFiles);
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
		final List<Experiment> experiments = getExperiments();
		Set<Spectrometer> spectrometers = new THashSet<Spectrometer>();
		for (Experiment exp : experiments) {
			spectrometers.addAll(exp.getSpectrometers());
		}
		return spectrometers;
	}

	@Override
	public Set<InputParameter> getInputParameters() {
		final List<Experiment> experiments = getExperiments();
		Set<InputParameter> inputParameters = new THashSet<InputParameter>();
		for (Experiment exp : experiments) {
			inputParameters.addAll(exp.getInputParameters());
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
	public int getNumPSMsForAPeptide(String sequenceKey) {
		return dataManager.getNumPSMsForAPeptide(sequenceKey);
	}

	@Override
	public ProteinGroupOccurrence getProteinGroupOccurrenceByProteinGroupKey(String proteinGroupKey) {
		return dataManager.getProteinGroupOccurrenceByProteinGroupKey(proteinGroupKey);
	}
}
