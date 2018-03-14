package org.proteored.miapeapi.experiment.model.filters;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.proteored.miapeapi.experiment.model.Experiment;
import org.proteored.miapeapi.experiment.model.ExperimentList;
import org.proteored.miapeapi.experiment.model.ExtendedIdentifiedPeptide;
import org.proteored.miapeapi.experiment.model.IdentificationItemEnum;
import org.proteored.miapeapi.experiment.model.IdentificationSet;
import org.proteored.miapeapi.experiment.model.ProteinGroup;
import org.proteored.miapeapi.experiment.model.datamanager.DataManager;
import org.proteored.miapeapi.interfaces.Software;

import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.TIntHashSet;

public class OccurrenceFilter implements Filter {
	private final int minOccurrence;
	private boolean appliedToProteins = false;
	private boolean appliedToPeptides = false;
	private final boolean distinguishModificatedPeptides;
	private final boolean byReplicates;
	private final Software software;
	private final boolean separateNonConclusiveProteins;
	private final boolean doNotGroupNonConclusiveProteins;
	private final int level; // level 0, 1 or 2

	public boolean isDistinguishModificatedPeptides() {
		return distinguishModificatedPeptides;
	}

	@Override
	public String toString() {
		String level;
		if (appliedToPeptides)
			level = "peptides";
		else
			level = "proteins";

		String over;
		String at;
		if (byReplicates) {
			over = "replicates";
			at = "in at least";
		} else {
			over = "times";
			at = "at";
		}
		return "Just include " + level + " present " + at + " " + minOccurrence + " " + over;
	}

	private static final Logger log = Logger.getLogger("log4j.logger.org.proteored");

	/**
	 * 
	 * @param minOccurrence
	 * @param item
	 * @param distinguisModificatedPeptides
	 * @param minNumReplicates
	 *            if true, the minOccurrence is the number of replicates in
	 *            which the item appears, and if false the minOccurrence is the
	 *            number of times that the item appears over the replicates
	 */
	public OccurrenceFilter(int minOccurrence, int level, IdentificationItemEnum item,
			boolean distinguisModificatedPeptides, boolean minNumReplicates, boolean doNotGroupNonConclusiveProteins,
			boolean separateNonConclusiveProteins, Software software) {
		this.minOccurrence = minOccurrence;
		this.level = level;
		if (IdentificationItemEnum.PEPTIDE.equals(item))
			this.appliedToPeptides = true;
		else if (IdentificationItemEnum.PROTEIN.equals(item))
			this.appliedToProteins = true;
		this.distinguishModificatedPeptides = distinguisModificatedPeptides;
		this.byReplicates = minNumReplicates;
		this.software = software;
		this.separateNonConclusiveProteins = separateNonConclusiveProteins;
		this.doNotGroupNonConclusiveProteins = doNotGroupNonConclusiveProteins;
	}

	@Override
	public boolean appliedToProteins() {
		return this.appliedToProteins;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof OccurrenceFilter) {
			OccurrenceFilter filter = (OccurrenceFilter) obj;
			if (filter.appliedToPeptides != this.appliedToPeptides)
				return false;
			if (filter.appliedToProteins != this.appliedToProteins)
				return false;
			if (filter.appliedToPeptides)
				if (filter.distinguishModificatedPeptides != this.distinguishModificatedPeptides)
					return false;
			if (filter.minOccurrence != this.minOccurrence)
				return false;
			if (filter.byReplicates && !this.byReplicates)
				return false;
			if (!filter.byReplicates && this.byReplicates)
				return false;
			if (filter.level != this.level) {
				return false;
			}
			return true;

		} else
			return super.equals(obj);
	}

	@Override
	public boolean appliedToPeptides() {
		return this.appliedToPeptides;
	}

	private TIntHashSet filterPeptides(List<ExtendedIdentifiedPeptide> identifiedPeptides,
			IdentificationSet currentIdSet) {
		List<IdentificationSet> nextLevelIdentificationSetList = null;
		try {
			nextLevelIdentificationSetList = currentIdSet.getNextLevelIdentificationSetList();
		} catch (UnsupportedOperationException e) {

		}
		if (byReplicates) {
			log.info("filtering " + identifiedPeptides.size() + " peptides by Occurrence " + minOccurrence
					+ " replicates");
		} else {
			log.info("filtering " + identifiedPeptides.size() + " peptides by Occurrence " + minOccurrence + " times");
		}
		TIntHashSet ret = new TIntHashSet();
		if (currentIdSet instanceof ExperimentList && level == 0 || //
				currentIdSet instanceof Experiment && level == 1) {
			if (byReplicates) {
				for (ExtendedIdentifiedPeptide peptide : identifiedPeptides) {
					int replicates = 0;
					if (nextLevelIdentificationSetList == null) {
						// this is because it is a Replicate
						// everything passes
						ret.add(peptide.getId());
					} else {

						for (IdentificationSet idSet : nextLevelIdentificationSetList) {
							final int peptideOccurrence = idSet.getPeptideOccurrenceNumber(peptide.getSequence(),
									this.distinguishModificatedPeptides);
							if (peptideOccurrence > 0) {
								replicates = replicates + 1;
							}
						}
						if (replicates >= minOccurrence) {
							ret.add(peptide.getId());
						}
					}
				}
			} else {
				// by number of times
				THashMap<String, Integer> peptideKeyMap = new THashMap<String, Integer>();
				for (ExtendedIdentifiedPeptide peptide : identifiedPeptides) {
					String key = peptide.getKey(distinguishModificatedPeptides);
					if (peptideKeyMap.contains(key)) {
						peptideKeyMap.put(key, peptideKeyMap.get(key) + 1);
					} else {
						peptideKeyMap.put(key, 1);
					}
				}
				for (ExtendedIdentifiedPeptide peptide : identifiedPeptides) {
					String key = peptide.getKey(distinguishModificatedPeptides);
					// this is because it is a Replicate
					if (peptideKeyMap.get(key) >= minOccurrence) {
						ret.add(peptide.getId());
					}
				}
			}
		} else {
			if (nextLevelIdentificationSetList != null) {
				// get what the next level gets
				for (IdentificationSet idSet : nextLevelIdentificationSetList) {
					List<ExtendedIdentifiedPeptide> peptides = idSet.getIdentifiedPeptides();
					for (ExtendedIdentifiedPeptide peptide : peptides) {
						ret.add(peptide.getId());
					}
				}
			} else {
				for (ExtendedIdentifiedPeptide peptide : identifiedPeptides) {
					ret.add(peptide.getId());
				}
			}
		}

		log.info("Resulting " + ret.size() + " peptides after filtering " + identifiedPeptides.size() + " peptides");
		return ret;

	}

	@Override
	public List<ProteinGroup> filter(List<ProteinGroup> proteinGroups, IdentificationSet currentIdSet) {
		List<IdentificationSet> nextLevelIdentificationSetList = currentIdSet.getNextLevelIdentificationSetList();
		if (appliedToPeptides) {
			List<ExtendedIdentifiedPeptide> identifiedPeptides = DataManager
					.getPeptidesFromProteinGroupsInParallel(proteinGroups);
			TIntHashSet filteredPeptides = filterPeptides(identifiedPeptides, currentIdSet);
			return DataManager.filterProteinGroupsByPeptides(proteinGroups, doNotGroupNonConclusiveProteins,
					separateNonConclusiveProteins, filteredPeptides, currentIdSet.getCvManager());
		} else {
			if (nextLevelIdentificationSetList == null)
				return proteinGroups;
			if (byReplicates)
				log.info("filtering " + proteinGroups.size() + " protein groups by Occurrence " + minOccurrence
						+ " replicates");
			else
				log.info("filtering " + proteinGroups.size() + " protein groups by Occurrence " + minOccurrence
						+ " times");
			List<ProteinGroup> ret = new ArrayList<ProteinGroup>();
			for (ProteinGroup proteinGroup : proteinGroups) {
				int replicates = 0;
				for (IdentificationSet idSet : nextLevelIdentificationSetList) {
					final int proteinGroupOccurrence = idSet.getProteinGroupOccurrenceNumber(proteinGroup);
					if (proteinGroupOccurrence > 0) {
						if (byReplicates)
							replicates = replicates + 1;
						else
							replicates = replicates + proteinGroupOccurrence;
					}
				}
				if (replicates >= minOccurrence)
					ret.add(proteinGroup);
				// else
				// log.info("This protein has not the enough occurrence");
			}
			log.info("Running PAnalyzer before to return the groups in the occurrence filter");
			ret = DataManager.filterProteinGroupsByPeptides(ret, doNotGroupNonConclusiveProteins,
					separateNonConclusiveProteins, null, currentIdSet.getCvManager());
			log.info("Resulting " + ret.size() + " protein groups after filtering " + proteinGroups.size()
					+ " protein groups");
			return ret;
		}
	}

	public boolean isByReplicates() {
		return byReplicates;
	}

	/**
	 * Gets the proteins from identifiedProteins that are present in
	 * inclusionProteinList
	 * 
	 * @param identifiedProteinGroups
	 * @param inclusionProteinList
	 * @return
	 */
	public List<ProteinGroup> filterProteins(List<ProteinGroup> identifiedProteinGroups,
			List<ProteinGroup> inclusionProteinGroupList, IdentificationSet currentIdSet) {

		// if (inclusionProteinGroupList == null)
		// return identifiedProteinGroups;

		if (!appliedToProteins) {
			// throw new
			// UnsupportedOperationException("This filter cannot filter
			// proteins");
			List<ExtendedIdentifiedPeptide> identifiedPeptides = DataManager
					.getPeptidesFromProteinGroupsInParallel(identifiedProteinGroups);
			TIntHashSet filteredPeptides = filterPeptides(identifiedPeptides, currentIdSet);
			return DataManager.filterProteinGroupsByPeptides(identifiedProteinGroups, doNotGroupNonConclusiveProteins,
					separateNonConclusiveProteins, filteredPeptides, currentIdSet.getCvManager());

			// return identifiedProteinGroups;

		}

		log.info("filtering " + identifiedProteinGroups.size() + " protein groups by Occurrence of the experiment.");

		List<ProteinGroup> ret = new ArrayList<ProteinGroup>();
		for (ProteinGroup proteinGroup : identifiedProteinGroups) {
			if (inclusionProteinGroupList == null || inclusionProteinGroupList.contains(proteinGroup))
				ret.add(proteinGroup);
		}
		log.info("Resulting " + ret.size() + " protein groups after filtering " + identifiedProteinGroups.size()
				+ " protein groups");
		return ret;
	}

	public int getMinOccurrence() {
		return minOccurrence;
	}

	@Override
	public Software getSoftware() {
		return software;
	}
}
