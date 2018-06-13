package org.proteored.miapeapi.experiment.model;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ms.MSFileType;
import org.proteored.miapeapi.experiment.model.datamanager.DataManager;
import org.proteored.miapeapi.experiment.model.datamanager.ReplicateDataManager;
import org.proteored.miapeapi.experiment.model.filters.FDRFilter;
import org.proteored.miapeapi.experiment.model.filters.Filter;
import org.proteored.miapeapi.interfaces.ms.MiapeMSDocument;
import org.proteored.miapeapi.interfaces.ms.ResultingData;
import org.proteored.miapeapi.interfaces.ms.Spectrometer;
import org.proteored.miapeapi.interfaces.msi.InputParameter;
import org.proteored.miapeapi.interfaces.msi.MiapeMSIDocument;
import org.proteored.miapeapi.spring.SpringHandler;
import org.proteored.miapeapi.util.MiapeReportsLinkGenerator;

import gnu.trove.map.hash.TIntIntHashMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.set.hash.THashSet;

/**
 * Class that represents a replicate. A replicate contains a MIAPE MS and a
 * MIAPE MSI document associated.
 *
 * @author Salva
 */
public class Replicate implements IdentificationSet<Void> {
	// log
	private static final Logger log = Logger.getLogger("log4j.logger.org.proteored");
	// MIAPE MS
	private List<MiapeMSDocument> miapeMSs;
	// MIAPE MSI
	private final List<MiapeMSIDocument> miapeMSIs;

	protected ReplicateDataManager dataManager;

	private String replicateName;

	private final String experimentName;
	private Experiment previousLevelIdentificationSet;
	private final ControlVocabularyManager cvManager;
	private final Integer minPeptideLength;

	// Identification set

	// public Replicate(String name, String experimentName,
	// List<MiapeMSDocument> miapeMSs, List<MiapeMSIDocument> miapeMSIs,
	// List<Filter> filters, ControlVocabularyManager cvManager) {
	// this(name, experimentName, miapeMSs, miapeMSIs, filters, null,
	// cvManager);
	// }

	/**
	 *
	 * @param name
	 *            name of the replicate
	 * @param experimentName
	 * @param sortingProteins
	 *            The sorting parameters needed to sort the proteins
	 * @param sortingPeptides
	 *            The sorting parameters needed to sort the peptides
	 * @param miapeMSs
	 *            associated MIAPE MS
	 * @param miapeMSIs
	 *            associated MIAPE MSI
	 */
	public Replicate(String name, String experimentName, List<MiapeMSDocument> miapeMSs,
			List<MiapeMSIDocument> miapeMSIs, List<Filter> filters, boolean doNotGroupNonConclusiveProteins,
			boolean separateNonConclusiveProteins, Integer minPeptideLength, ControlVocabularyManager cvManager,
			boolean processInParallel) {
		this.experimentName = experimentName;
		replicateName = name;
		this.minPeptideLength = minPeptideLength;
		// if (experimentName != null && !"".equals(experimentName))
		// this.name = this.name + "/" + experimentName;
		if (cvManager != null)
			this.cvManager = cvManager;
		else
			this.cvManager = SpringHandler.getInstance().getCVManager();
		this.miapeMSs = miapeMSs;
		this.miapeMSIs = new LightMiapeMSIListAdapter(miapeMSIs).adapt();
		dataManager = new ReplicateDataManager(this, miapeMSIs, filters, doNotGroupNonConclusiveProteins,
				separateNonConclusiveProteins, minPeptideLength, processInParallel);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof IdentificationSet) {
			return obj.toString().equals(toString());
		}
		return false;
	}

	public String getExperimentName() {
		return experimentName;
	}

	@Override
	public String getFullName() {
		// if (previousLevelIdentificationSet != null
		// && previousLevelIdentificationSet.getPreviousLevelIdentificationSet()
		// != null
		// &&
		// previousLevelIdentificationSet.getPreviousLevelIdentificationSet().getNumExperiments()
		// == 1)
		// return getName();
		return getName() + " / " + getExperimentName();
	}

	@Override
	public int getNumDifferentPeptides(boolean distinguishModPep) {
		return dataManager.getNumDifferentPeptides(distinguishModPep);
	}

	@Override
	public int getNumDifferentProteinGroups(boolean countNonConclusiveProteins) {
		return dataManager.getNumDifferentProteinGroups(countNonConclusiveProteins);
	}

	@Override
	public List<ProteinGroup> getIdentifiedProteinGroups() {
		// log.info("Getting proteins from replicate:" + this.getName());
		final List<ProteinGroup> proteinGroups = dataManager.getIdentifiedProteinGroups();
		// log.info("returning " + proteinGroups.size() +
		// " protein groups in replicate " + getName());
		return proteinGroups;

	}

	/**
	 * Gets a list of peptides (ordered by score) that pass the FDR threshold
	 * defined by a {@link FDRFilter} that should be set before this call.
	 *
	 * @return
	 */
	@Override
	public List<ExtendedIdentifiedPeptide> getIdentifiedPeptides() {
		log.debug("Getting peptides from replicate:" + getName());
		final List<ExtendedIdentifiedPeptide> identifiedPeptides = dataManager.getIdentifiedPeptides();
		log.debug("returning " + identifiedPeptides.size() + " peptides in replicate " + getName());
		return identifiedPeptides;
	}

	@Override
	public boolean hasProteinGroup(ProteinGroup proteinGroup) {
		return dataManager.hasProteinGroup(proteinGroup);
	}

	@Override
	public Map<String, ProteinGroupOccurrence> getProteinGroupOccurrenceList() {
		return dataManager.getProteinGroupOccurrenceList();
	}

	@Override
	public Map<String, PeptideOccurrence> getPeptideOccurrenceList(boolean distinguishModPep) {
		return dataManager.getPeptideOcurrenceList(distinguishModPep);
	}

	@Override
	public void setName(String name) {
		replicateName = name;

	}

	/**
	 * Gets the number of proteins identified taking into account the filters
	 *
	 * @return
	 */
	@Override
	public int getTotalNumProteinGroups(boolean countNonConclusiveProteins) {
		return dataManager.getTotalNumProteinGroups(countNonConclusiveProteins);
	}

	@Override
	public List<MiapeMSDocument> getMiapeMSs() {
		return miapeMSs;
	}

	public void setMiapeMS(List<MiapeMSDocument> miapeMSs) {
		this.miapeMSs = miapeMSs;
	}

	@Override
	public List<MiapeMSIDocument> getMiapeMSIs() {
		return miapeMSIs;
	}

	@Override
	public int getPeptideOccurrenceNumber(String sequence, boolean distModPep) {
		return dataManager.getPeptideOccurrenceNumber(sequence, distModPep);
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("\t\tReplicate " + getName() + "\n");
		if (miapeMSs != null) {
			for (final MiapeMSDocument ms : miapeMSs) {
				sb.append("\t\t\t\tMIAPE MS: " + ms.getName() + "\n");
			}

		}
		if (miapeMSIs != null) {
			for (final MiapeMSIDocument msi : miapeMSIs) {
				sb.append("\t\t\t\tMIAPE MSI: " + msi.getName() + "\n");
			}

		}
		sb.append("\t\t\t\tNumber of protein groups:" + getTotalNumProteinGroups(false) + "\n");
		sb.append("\t\t\t\tNumber of different protein groups:" + getNumDifferentProteinGroups(false) + "\n");
		// sb.append("\t\t\t\tNumber of proteins:"
		// + getIdentifiedProteins().size() + "\n");
		sb.append("\t\t\t\tNumber of peptides:" + getTotalNumPeptides() + "\n");
		sb.append("\t\t\t\tNumber of different peptides:" + getNumDifferentPeptides(true) + "\n");

		return sb.toString();
	}

	@Override
	public int getProteinGroupOccurrenceNumber(ProteinGroup proteinGroup) {
		return dataManager.getProteinGroupOccurrenceNumber(proteinGroup);
		// final List<ProteinGroup> proteinGroups =
		// getIdentifiedProteinGroups();
		// if (proteinGroups.contains(proteinGroup))
		// return 1;
		//
		// return 0;
	}

	@Override
	public double getAverageNumDifferentProteinGroups(boolean countNonConclusiveProteins) {
		return getNumDifferentProteinGroups(countNonConclusiveProteins);
	}

	@Override
	public double getAverageNumDifferentPeptides(boolean distPeptides) {
		return getNumDifferentPeptides(distPeptides);
	}

	@Override
	public double getStdNumDifferentProteinGroups(boolean countNonConclusiveProteins) {
		return 0.0;
	}

	@Override
	public double getStdNumDifferentPeptides(boolean distPeptides) {
		return 0.0;
	}

	@Override
	public List getNextLevelIdentificationSetList() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean hasPeptide(String sequence, boolean distModPep) {
		return dataManager.hasPeptide(sequence, distModPep);
	}

	@Override
	public List<String> getProteinScoreNames() {
		return dataManager.getProteinScores();
	}

	@Override
	public List<String> getPeptideScoreNames() {
		return dataManager.getPeptideScores();
	}

	@Override
	public List<String> getDifferentPeptideModificationNames() {
		final List<String> differentPeptideModificationNames = dataManager.getDifferentPeptideModificationNames();
		Collections.sort(differentPeptideModificationNames);
		return differentPeptideModificationNames;
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
	public void removeFilters() {
		dataManager.removeFilters();
	}

	@Override
	public String getName() {
		return replicateName;
	}

	@Override
	public List<DataManager> getNextLevelDataManagers() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Experiment getPreviousLevelIdentificationSet() {
		return previousLevelIdentificationSet;
	}

	public void setPreviousLevelIdentificationSet(Experiment previousLevelIdentificationSet) {
		this.previousLevelIdentificationSet = previousLevelIdentificationSet;
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
	public void setFilters(List filters) {
		dataManager.setFilters(filters);
	}

	@Override
	public double getAverageTotalNumProteinGroups(boolean countNonConclusiveProteins) {
		return dataManager.getTotalNumProteinGroups(countNonConclusiveProteins);
	}

	@Override
	public double getAverageTotalNumPeptides() {
		return dataManager.getTotalNumPeptides();
	}

	@Override
	public int getTotalNumPeptides() {
		return dataManager.getTotalNumPeptides();
	}

	@Override
	public double getAverageTotalVsDifferentNumProteinGroups(boolean countNonConclusiveProteins) {
		return getTotalVsDifferentNumProteinGroups(countNonConclusiveProteins);
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
		return getTotalVsDifferentNumPeptides(distModPep);
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
		return 0.0;
	}

	@Override
	public double getStdTotalVsDifferentNumPeptides(Boolean distModPeptides) {
		return 0.0;
	}

	@Override
	public double getStdTotalNumProteinGroups(boolean countNonConclusiveProteins) {
		return 0.0;
	}

	@Override
	public double getStdTotalNumPeptides() {
		return 0.0;
	}

	@Override
	public Set<String> getDifferentSearchedDatabases() {
		return dataManager.getDifferentSearchedDatabases();
	}

	@Override
	public List<ResultingData> getResultingDatas() {
		final List<ResultingData> ret = new ArrayList<ResultingData>();

		if (miapeMSs != null) {
			for (final MiapeMSDocument miapeMSDocument : miapeMSs) {
				final List<ResultingData> resultingDatas = miapeMSDocument.getResultingDatas();
				if (resultingDatas != null) {
					ret.addAll(resultingDatas);
				}
			}
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
		dataManager.setFiltered(b);

	}

	public List<ResultingData> getPeakListResultingDatas() {

		if (miapeMSs != null) {
			final List<ResultingData> ret = new ArrayList<ResultingData>();
			for (final MiapeMSDocument miapeMS : miapeMSs) {
				final List<ResultingData> resultingDatas = miapeMS.getResultingDatas();
				if (resultingDatas != null) {
					for (final ResultingData resultingData : resultingDatas) {
						if (isPeakList(resultingData))
							ret.add(resultingData);
					}
				}
			}
			return ret;
		}
		return null;
	}

	private boolean isPeakList(ResultingData resultingData) {
		if (resultingData != null) {
			final String dataFileType = resultingData.getDataFileType();
			if (dataFileType != null) {
				if (MSFileType.getInstance(cvManager).isPeakListFileType(dataFileType, cvManager))
					return true;
			}
		}
		return false;
	}

	public List<ResultingData> getRawFileResultingDatas() {
		if (miapeMSs != null) {
			final List<ResultingData> ret = new ArrayList<ResultingData>();
			for (final MiapeMSDocument miapeMS : miapeMSs) {
				final List<ResultingData> resultingDatas = miapeMS.getResultingDatas();
				if (resultingDatas != null) {
					for (final ResultingData resultingData : resultingDatas) {
						if (!isPeakList(resultingData))
							ret.add(resultingData);
					}
				}
			}
			return ret;
		}
		return null;
	}

	public List<String> getMSIGeneratedFiles() {
		if (miapeMSIs != null) {
			final List<String> ret = new ArrayList<String>();
			for (final MiapeMSIDocument miapeMSI : miapeMSIs) {
				if (miapeMSI.getGeneratedFilesURI() != null)
					ret.add(miapeMSI.getGeneratedFilesURI());
			}
			return ret;
		}
		return null;
	}

	public TIntObjectHashMap<URL> getMSReportURLs(int userId) {
		final TIntObjectHashMap<URL> reports = new TIntObjectHashMap<URL>();
		for (final MiapeMSDocument miapeMS : miapeMSs) {
			final URL miapePublicLink = MiapeReportsLinkGenerator.getMiapePublicLink(userId, miapeMS.getId(), "MS");
			reports.put(miapeMS.getId(), miapePublicLink);
		}
		return reports;
	}

	public TIntObjectHashMap<URL> getMSIReportURLs(int userId) {
		final TIntObjectHashMap<URL> reports = new TIntObjectHashMap<URL>();

		for (final MiapeMSIDocument miapeMSI : miapeMSIs) {
			final URL miapePublicLink = MiapeReportsLinkGenerator.getMiapePublicLink(userId, miapeMSI.getId(), "MSI");
			reports.put(miapeMSI.getId(), miapePublicLink);
		}
		return reports;
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
		final List<MiapeMSDocument> miapeMSs = getMiapeMSs();
		final Set<Spectrometer> spectrometers = new THashSet<Spectrometer>();
		for (final MiapeMSDocument miapeMSDocument : miapeMSs) {
			if (miapeMSDocument.getSpectrometers() != null)
				spectrometers.addAll(miapeMSDocument.getSpectrometers());
		}
		return spectrometers;
	}

	@Override
	public Set<InputParameter> getInputParameters() {
		final List<MiapeMSIDocument> miapeMSIs = getMiapeMSIs();
		final Set<InputParameter> inputParameters = new THashSet<InputParameter>();
		for (final MiapeMSIDocument miapeMSIDocument : miapeMSIs) {
			if (miapeMSIDocument.getInputParameters() != null)
				inputParameters.addAll(miapeMSIDocument.getInputParameters());
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
