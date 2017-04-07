package org.proteored.miapeapi.experiment.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.experiment.model.datamanager.DataManager;
import org.proteored.miapeapi.experiment.model.filters.FDRFilter;
import org.proteored.miapeapi.experiment.model.filters.Filter;
import org.proteored.miapeapi.interfaces.ms.MiapeMSDocument;
import org.proteored.miapeapi.interfaces.ms.ResultingData;
import org.proteored.miapeapi.interfaces.ms.Spectrometer;
import org.proteored.miapeapi.interfaces.msi.InputParameter;
import org.proteored.miapeapi.interfaces.msi.MiapeMSIDocument;

public interface IdentificationSet<T> {

	public String getName();

	public String getFullName();

	public void setName(String name);

	/**
	 * Sets a new set of filters
	 *
	 * @param filters
	 * @return
	 */
	public void setFilters(List<Filter> filters);

	/**
	 * Removed all filters
	 */
	public void removeFilters();

	public float getProteinFDR(String scoreName);

	public float getPeptideFDR(String scoreName);

	public float getPSMFDR(String scoreName);

	// public FDRFilter getFDRFilter();

	/**
	 * Gets the list of {@link IdentificationSet} in the next level. That is,
	 * <br>
	 * for {@link ExperimentList}, return a list of {@link Experiment}<br>
	 * for a {@link Experiment}, returns a list of {@link Replicate}. For a
	 * {@link Replicate}, throws an {@link UnsupportedOperationException}.
	 */
	public List<T> getNextLevelIdentificationSetList() throws UnsupportedOperationException;

	/**
	 * Gets the {@link IdentificationSet} in the previous level, that is, <br>
	 * for {@link ExperimentList} list throws an
	 * {@link UnsupportedOperationException}, for <br>
	 * an {@link Experiment}, returns an {@link ExperimentList}, and for a
	 * {@link Replicate}, returns an {@link Experiment}.
	 *
	 * @return
	 */
	public IdentificationSet getPreviousLevelIdentificationSet();

	/**
	 * Gets a list of protein groups. If there is any filter defined, the
	 * returned protein groups will be filtered, sorted by Score.
	 *
	 * @return
	 */
	public List<ProteinGroup> getIdentifiedProteinGroups();

	/**
	 * Gets a list of peptides (ordered by score) that pass the FDR threshold
	 * defined by a {@link FDRFilter} that should be setted before this call.
	 *
	 *
	 * @return
	 */
	public List<ExtendedIdentifiedPeptide> getIdentifiedPeptides();

	/**
	 * Gets the number of times that a different protein group is identified.
	 *
	 * @param proteinGroup
	 * @return
	 */
	public int getProteinGroupOccurrenceNumber(ProteinGroup proteinGroup);

	/**
	 * Gets a hashmap that indicates how many times has been identified each
	 * protein
	 *
	 * @return
	 */
	public HashMap<String, ProteinGroupOccurrence> getProteinGroupOccurrenceList();

	/**
	 * Gets the {@link ProteinGroupOccurrence} of a protein group
	 *
	 * @return
	 */
	public ProteinGroupOccurrence getProteinGroupOccurrence(ProteinGroup proteinGroup);

	/**
	 * Gets the {@link ProteinGroupOccurrence} of a protein accession
	 *
	 * @return
	 */
	public ProteinGroupOccurrence getProteinGroupOccurrence(String proteinACC);

	/**
	 * Gets the {@link ProteinGroupOccurrence} of a protein accession
	 *
	 * @return
	 */
	public int getProteinGroupOccurrenceNumber(String proteinACC);

	/**
	 * Gets a list of {@link PeptideOccurrence} that indicates how many times
	 * has been identified each peptide in the total number of replicates.
	 *
	 * @return
	 */
	public HashMap<String, PeptideOccurrence> getPeptideOccurrenceList(boolean distinguishModPep);

	/**
	 * Gets the number of times that a certain sequence appears over the next
	 * level
	 *
	 * @param sequence
	 * @return
	 */
	public int getPeptideOccurrenceNumber(String sequence, boolean distinguishModPep);

	/**
	 * Gets the {@link PeptideOccurrence} of a peptide
	 *
	 * @param sequence
	 * @param distinguishModPep
	 * @return
	 */
	public PeptideOccurrence getPeptideOccurrence(String sequence, boolean distinguishModPep);

	/**
	 * Gets the total number of protein groups without redundancy removing
	 *
	 * @return
	 */
	public int getTotalNumProteinGroups(boolean countNonConclusiveProteins);

	/**
	 * Gets the total number of non conclusive protein groups without redundancy
	 * removing
	 *
	 * @return
	 */
	public int getTotalNumNonConclusiveProteinGroups();

	/**
	 * Gets the number of different protein groups identified
	 *
	 * @return
	 */
	public int getNumDifferentProteinGroups(boolean countNonConclusiveProteins);

	/**
	 * Gets the number of different non conclusive protein groups identified
	 *
	 * @return
	 */
	public int getNumDifferentNonConclusiveProteinGroups();

	/**
	 * Gets the number of different peptides identified
	 *
	 * @return
	 */
	public int getNumDifferentPeptides(boolean distiguishModificatedPeptides);

	/**
	 * Gets the number of different peptides identified differentiating the ones
	 * with different charge
	 *
	 * @return
	 */
	public int getNumDifferentPeptidesPlusCharge(boolean distiguishModificatedPeptides);

	public double getAverageTotalNumProteinGroups(boolean countNonConclusiveProteins);

	public double getStdTotalNumProteinGroups(boolean countNonConclusiveProteins);

	/**
	 * Gets the average number of different protein groups
	 *
	 * @return
	 */
	public double getAverageNumDifferentProteinGroups(boolean countNonConclusiveProteins);

	/**
	 * Gets the standard deviation of the number of different proteins
	 *
	 * @return
	 */
	public double getStdNumDifferentProteinGroups(boolean countNonConclusiveProteins);

	/**
	 * Gets the average number of different peptides
	 *
	 * @return
	 */
	public double getAverageNumDifferentPeptides(boolean distiguishModificatedPeptides);

	/**
	 * Gets the standard deviation of the number of different peptides
	 *
	 * @param distiguishModificatedPeptides
	 * @return
	 */
	public double getStdNumDifferentPeptides(boolean distiguishModificatedPeptides);

	/**
	 * Identicate if the identification set has that protein group or not
	 *
	 * @param proteinGroup
	 * @return
	 */
	public boolean hasProteinGroup(ProteinGroup proteinGroup);

	/**
	 * Identicate if the identification set has that peptide or not
	 *
	 * @param sequence
	 * @return
	 */
	public boolean hasPeptide(String sequence, boolean distinguishModPeptide);

	public List<String> getProteinScoreNames();

	public List<String> getPeptideScoreNames();

	public List<String> getDifferentPeptideModificationNames();

	/**
	 * Gets the number of Modified sites in the peptides of the identification
	 * set
	 *
	 * @param modification
	 * @return
	 */
	public int getModificatedSiteOccurrence(String modification);

	/**
	 *
	 * @param modif
	 * @return hashmap <number of modifications in the peptide, number of
	 *         peptides with that number of modifications>
	 */
	public HashMap<Integer, Integer> getModificationOccurrenceDistribution(String modif);

	/**
	 *
	 * @param modifs
	 * @return hashmap <number of missedclavage sites in the peptide, number of
	 *         peptides with that number of missedcleavages>
	 */
	public HashMap<Integer, Integer> getMissedCleavagesOccurrenceDistribution();

	/**
	 * Gets the {@link DataManager} of this {@link IdentificationSet}
	 *
	 * @return
	 */
	public DataManager getDataManager();

	/**
	 * Gets the {@link DataManagerOLD} of the next level
	 * {@link IdentificationSet}
	 *
	 * @return
	 */
	public List<DataManager> getNextLevelDataManagers();

	/**
	 * Gets the number of True Positives
	 *
	 * @param positiveProteinAccessions
	 * @return
	 */
	public int getProteinGroupTP(HashSet<String> positiveProteinAccessions, boolean countNonConclusiveProteins);

	/**
	 * Gets the number of False Negatives
	 *
	 * @param positiveProteinAccessions
	 * @return
	 */
	public int getProteinGroupFN(HashSet<String> positiveProteinAccessions, boolean countNonConclusiveProteins);

	/**
	 * Gets the number of True Negatives
	 *
	 * @param positiveProteinAccessions
	 * @return
	 */
	public int getProteinGroupTN(HashSet<String> positiveProteinAccessions, boolean countNonConclusiveProteins);

	/**
	 * Gets the number of False Positives
	 *
	 * @param positiveProteinAccessions
	 * @return
	 */
	public int getProteinGroupFP(HashSet<String> positiveProteinAccessions, boolean countNonConclusiveProteins);

	/**
	 * Gets the number of True Positives
	 *
	 * @param positiveProteinAccessions
	 * @return
	 */
	public int getPeptideTP(HashSet<String> positivePeptideSequences, boolean distinguisModificatedPeptides);

	/**
	 * Gets the number of False Negatives
	 *
	 * @param positivePeptideSequences
	 * @return
	 */
	public int getPeptideFN(HashSet<String> positivePeptideSequences, boolean distinguisModificatedPeptides);

	/**
	 * Gets the number of True Negatives
	 *
	 * @param positivePeptideSequences
	 * @return
	 */
	public int getPeptideTN(HashSet<String> positivePeptideSequences, boolean distinguisModificatedPeptides);

	/**
	 * Gets the number of False Positives
	 *
	 * @param positivePeptideSequences
	 * @return
	 */
	public int getPeptideFP(HashSet<String> positivePeptideSequences, boolean distinguisModificatedPeptides);

	/**
	 * Gets the number of different peptides that contains at least one AA
	 * modified with a certain modification
	 *
	 * @param modification
	 * @return
	 */
	public int getPeptideModificatedOccurrence(String modification);

	/**
	 * gets the average number of total peptides of the idset over the next
	 * level idsets
	 *
	 * @return
	 */
	public double getAverageTotalNumPeptides();

	/**
	 * Gets the total number of peptides of the idSet
	 *
	 * @return
	 */
	public int getTotalNumPeptides();

	public double getStdTotalNumPeptides();

	/**
	 * Gets the average of the ratios of the next level idSets between the
	 * different protein number and the total protein number
	 *
	 * @return
	 */
	public double getAverageTotalVsDifferentNumProteinGroups(boolean countNonConclusiveProteins);

	/**
	 * Gets the ratio between the different protein number and the total protein
	 * in the idSet number
	 *
	 * @return
	 */
	public double getTotalVsDifferentNumProteinGroups(boolean countNonConclusiveProteins);

	/**
	 * Gets the average of the ratios of the next level idSets between the
	 * different peptide number and the total peptide number
	 *
	 * @return
	 */
	public double getAverageTotalVsDifferentNumPeptides(boolean distModPep);

	/**
	 * Gets the ratio between the different peptide number and the total
	 * peptides in the idSet number
	 *
	 * @return
	 */
	public double getTotalVsDifferentNumPeptides(boolean distModPep);

	/**
	 * Gets the standard deviation of the ratios of the next level idSets
	 * between the different protein number and the total protein number
	 *
	 * @return
	 */
	public double getStdTotalVsDifferentNumProteinGroups(boolean countNonConclusiveProteins);

	/**
	 * Gets the standard deviation of the ratios of the next level idSets
	 * between the different peptide number and the total peptide number
	 *
	 * @return
	 */
	public double getStdTotalVsDifferentNumPeptides(Boolean distModPeptides);

	/**
	 * Gets the list of different databases that where used to search.
	 *
	 * @return
	 */
	public Set<String> getDifferentSearchedDatabases();

	/**
	 * Gets the {@link ResultingData} of the {@link IdentificationSet}, that is,
	 * gets all {@link ResultingData} from all {@link MiapeMSDocument}s in the
	 * {@link IdentificationSet}
	 *
	 * @return
	 */
	public List<ResultingData> getResultingDatas();

	/**
	 * Gets the filters applied to the data
	 *
	 * @return
	 */
	public List<Filter> getFilters();

	/**
	 * Gets the {@link FDRFilter} of the {@link IdentificationSet} if exists
	 *
	 * @return
	 */
	public FDRFilter getFDRFilter();

	/**
	 * Gets the number of proteins that are decoys according to an applied
	 * {@link FDRFilter}
	 *
	 * @return
	 */
	public int getNumProteinGroupDecoys();

	/**
	 * Gets the number of different proteins that are decoys according to an
	 * applied {@link FDRFilter}
	 *
	 * @return
	 */
	public int getNumDifferentProteinGroupsDecoys();

	/**
	 * Gets the number of peptides that are decoys according to an applied
	 * {@link FDRFilter}
	 *
	 * @return
	 */
	public int getNumPeptideDecoys();

	/**
	 * Gets the number of different peptides that are decoys according to an
	 * applied {@link FDRFilter}
	 *
	 * @return
	 */
	public int getNumDifferentPeptideDecoys(boolean distringuishModificatedPeptides);

	/**
	 * Gets the {@link ControlVocabularyManager}
	 *
	 * @return
	 */
	public ControlVocabularyManager getCvManager();

	public void setFiltered(boolean b);

	public List<MiapeMSDocument> getMiapeMSs();

	public List<MiapeMSIDocument> getMiapeMSIs();

	/**
	 * Get a list of identified proteins without any protein grouping
	 *
	 * @return
	 */
	public List<ExtendedIdentifiedProtein> getIdentifiedProteins();

	public List<ProteinGroup> getNonFilteredIdentifiedProteinGroups();

	public List<ExtendedIdentifiedPeptide> getNonFilteredIdentifiedPeptides();

	public Set<Spectrometer> getSpectrometers();

	public Set<InputParameter> getInputParameters();

	/**
	 * Gets a list of {@link PeptideOccurrence} that indicates how many times
	 * has been identified each peptide in the total number of replicates
	 * distinguising them by charge.
	 *
	 * @return
	 */
	public HashMap<String, PeptideOccurrence> getPeptideChargeOccurrenceList(boolean distinguishModPep);

	public PeptideOccurrence getPeptideChargeOccurrence(String sequencePlusChargeKey, boolean b);

	public int getPeptideChargeOccurrenceNumber(String sequencePlusChargeKey, Boolean distinguishModPep);

	/**
	 * @param proteinGroupKey
	 * @return
	 */
	public int getProteinGroupOccurrenceNumberByProteinGroupKey(String proteinGroupKey);

}
