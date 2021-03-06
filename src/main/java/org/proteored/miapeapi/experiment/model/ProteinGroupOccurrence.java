package org.proteored.miapeapi.experiment.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.proteored.miapeapi.experiment.model.filters.FDRFilter;
import org.proteored.miapeapi.experiment.model.grouping.ProteinEvidence;
import org.proteored.miapeapi.experiment.model.interfaces.Occurrence;
import org.proteored.miapeapi.experiment.model.interfaces.PeptideContainer;
import org.proteored.miapeapi.experiment.model.interfaces.ProteinContainer;
import org.proteored.miapeapi.experiment.model.interfaces.ProteinGroupContainer;
import org.proteored.miapeapi.experiment.model.sort.ComparatorManager;
import org.proteored.miapeapi.experiment.model.sort.Order;
import org.proteored.miapeapi.experiment.model.sort.ProteinComparatorKey;
import org.proteored.miapeapi.experiment.model.sort.ProteinGroupComparisonType;
import org.proteored.miapeapi.experiment.model.sort.SorterUtil;
import org.proteored.miapeapi.experiment.model.sort.SortingManager;
import org.proteored.miapeapi.experiment.model.sort.SortingParameters;
import org.proteored.miapeapi.interfaces.Software;
import org.proteored.miapeapi.interfaces.msi.Database;
import org.proteored.miapeapi.interfaces.msi.ProteinScore;

import edu.scripps.yates.annotations.uniprot.UniprotProteinLocalRetriever;
import gnu.trove.set.hash.THashSet;
import gnu.trove.set.hash.TIntHashSet;

public class ProteinGroupOccurrence
		implements Occurrence<ProteinGroup>, ProteinContainer, PeptideContainer, ProteinGroupContainer {
	private static Logger log = Logger.getLogger("log4j.logger.org.proteored");

	private final List<ProteinGroup> proteinGroups = new ArrayList<ProteinGroup>();

	private String name;
	private Float meanProteinCoverage = null;

	private ExtendedIdentifiedPeptide bestPeptide = null;
	private ProteinGroup bestProteinGroup = null;
	private ExtendedIdentifiedProtein bestProtein = null;

	private ProteinEvidence evidence;

	private List<String> accessions;

	private List<String> accessionsByEvidence;

	public ProteinGroupOccurrence() {

	}

	/**
	 * Gets the comparison key of the ProteinGroupOccurrence depending on the
	 * {@link ProteinGroupComparisonType} passed as parameter
	 * 
	 * @param proteinSelection
	 * @return
	 */
	public Object getKey(ProteinGroupComparisonType proteinSelection) {

		switch (proteinSelection) {
		case ALL_PROTEINS:
			return getAccessionsString();
		case BEST_PROTEIN:
			return this.getBestProtein().getAccession();
		case HIGHER_EVIDENCE_PROTEIN:
			final String acc = getAccessionsByEvidence().get(0);
			return acc;
		case SHARE_ONE_PROTEIN:

			return new ProteinComparatorKey(getAccessions(), proteinSelection);

		default:
			return null;
		}
	}

	/**
	 * Gets the first accession of the list of proteins
	 * 
	 * @return
	 */
	public String getAccessionsString() {
		final List<String> accList = getAccessions();

		String ret = "";
		for (final String accession : accList) {
			if (!"".equals(ret))
				ret = ret + ",";
			ret = ret + accession;
		}

		return ret;
	}

	public ProteinEvidence getEvidence() {
		return evidence;
	}

	public List<String> getAccessions() {
		if (accessions != null)
			return accessions;
		accessions = new ArrayList<String>();
		for (final ProteinGroup proteinGroup : proteinGroups) {
			final List<String> accessions = proteinGroup.getAccessions();
			for (final String accession : accessions) {
				if (!this.accessions.contains(accession))
					this.accessions.add(accession);
			}
		}
		Collections.sort(accessions);

		return accessions;
	}

	public List<String> getAccessionsByEvidence() {
		if (accessionsByEvidence != null && !accessionsByEvidence.isEmpty()) {
			return accessionsByEvidence;
		}
		accessionsByEvidence = new ArrayList<String>();
		final List<ExtendedIdentifiedProtein> proteinList = new ArrayList<ExtendedIdentifiedProtein>();
		for (final ProteinGroup pg : proteinGroups) {
			proteinList.addAll(pg);
		}
		Collections.sort(proteinList, ComparatorManager.getProteinComparatorByEvidence());
		for (final ExtendedIdentifiedProtein protein : proteinList) {
			if (protein.getAccession() == null) {
				log.warn("protein with no acc");
			} else {
				accessionsByEvidence.add(protein.getAccession());
			}
		}

		return accessionsByEvidence;
	}

	@Override
	public String toString() {
		final List<ProteinGroup> proteinGroups = getItemList();
		SorterUtil.sortProteinGroupsByAccession(proteinGroups);
		String ret = "";
		for (int i = 0; i < proteinGroups.size(); i++) {
			if (i > 0)
				ret = ret + " / ";
			ret = ret + proteinGroups.get(i);
		}
		return ret;
	}

	@Override
	public ProteinGroup getFirstOccurrence() {
		if (proteinGroups.isEmpty())
			return null;
		return proteinGroups.get(0);
	}

	@Override
	public void addOccurrence(ProteinGroup item) {
		accessions = null;

		evidence = item.getEvidence();
		meanProteinCoverage = null;
		bestPeptide = null;
		bestProteinGroup = null;
		bestProtein = null;
		proteinGroups.add(item);
	}

	@Override
	public List<ProteinGroup> getItemList() {
		return proteinGroups;
	}

	public String getName() {
		return name;
	}

	/**
	 * Gets the best score of the {@link T}s according to a
	 * {@link SortingParameters}
	 * 
	 * @param sorting
	 * @return
	 */
	@Override
	public Float getBestProteinScore() {
		if (bestProtein != null)
			return bestProtein.getScore();
		return getBestProtein().getScore();

	}

	@Override
	public ExtendedIdentifiedProtein getBestProtein() {
		if (bestProtein != null)
			return bestProtein;
		final List<ExtendedIdentifiedProtein> proteins = this.getProteins();
		if (proteins != null && !proteins.isEmpty()) {
			SorterUtil.sortProteinsByBestProteinScore(proteins, false);
		}
		return bestProtein = proteins.get(0);
	}

	public ExtendedIdentifiedProtein getBestProteinByPeptideScore() {

		final List<ExtendedIdentifiedProtein> proteins = this.getProteins();
		if (proteins != null && !proteins.isEmpty()) {
			SorterUtil.sortProteinsByBestPeptideScore(proteins);
		}
		return proteins.get(0);
	}

	@Override
	public Float getBestProteinScore(String scoreName) {
		final ExtendedIdentifiedProtein bestProtein2 = getBestProtein(scoreName);
		if (bestProtein2 != null)
			return bestProtein2.getScore();
		return null;
	}

	// @Override
	// public boolean equals(Object obj) {
	// // if the two group occurrences contains at least one equivalent
	// // protein group, they are equivalents.
	// if (obj instanceof ProteinGroupOccurrence) {
	// ProteinGroupOccurrence pgo = (ProteinGroupOccurrence) obj;
	// for (ProteinGroup proteinGroup : this.proteinGroups) {
	// if (proteinGroup.equals(pgo))
	// return true;
	// }
	// return false;
	// }
	// return super.equals(obj);
	// }

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ProteinGroupOccurrence) {
			final ProteinGroupOccurrence pgo = (ProteinGroupOccurrence) obj;
			return pgo.toString().equals(toString());

		}
		return super.equals(obj);
	}

	@Override
	public ExtendedIdentifiedProtein getBestProtein(String scoreName) {

		final List<ExtendedIdentifiedProtein> proteins = this.getProteins();
		if (proteins != null && !proteins.isEmpty()) {
			SorterUtil.sortProteinsByProteinScore(proteins, scoreName, false);
			if (proteins.get(0).getScoreNames().contains(scoreName))
				return proteins.get(0);
		}
		return null;
	}

	/**
	 * Gets the best {@link ProteinGroup} according to the score defined in a
	 * {@link SortingParameters}
	 * 
	 * @param sorting
	 * @return
	 */
	@Override
	public ProteinGroup getBestProteinGroup() {
		if (bestProteinGroup != null)
			return bestProteinGroup;

		final SortingParameters sorting = SortingManager.getInstance()
				.getProteinGroupOccurrenceSortingByProteinScore(this);
		if (sorting == null)
			return null;
		final Order order = sorting.getOrder();
		final String scoreName = sorting.getScoreName();
		float finalScore = 0;
		if (Order.ASCENDANT.equals(order)) { // we need to report the lowest //
												// score
			finalScore = Float.MAX_VALUE;
		} else if (Order.DESCENDANT.equals(order)) {// we need to report the //
													// highest value
			finalScore = Float.MIN_VALUE;
		} else {
			return null;
		}
		if (scoreName == null || "".equals(scoreName))
			return null;
		ProteinGroup ret = null;
		for (final ProteinGroup proteinGroup : proteinGroups) {
			for (final ExtendedIdentifiedProtein protein : proteinGroup) {
				if (protein.getScores() != null) {
					for (final ProteinScore score : protein.getScores()) {
						if (scoreName.equals(score.getName())) {
							if (Order.ASCENDANT.equals(order)) { // report the
																	// lowest
																	// score
								if (Float.valueOf(score.getValue()) < finalScore) {
									finalScore = Float.valueOf(score.getValue());
									ret = proteinGroup;
								}
							} else {
								if (Float.valueOf(score.getValue()) > finalScore) {
									finalScore = Float.valueOf(score.getValue());
									ret = proteinGroup;
								}
							}
						}
					}

				}
			}
		}
		bestProteinGroup = ret;
		return ret;

	}

	/**
	 * @param upr
	 * @return the coverage
	 */
	public Float getMeanProteinCoverage(boolean retrieveSequenceFromTheInternet, UniprotProteinLocalRetriever upr) {
		if (meanProteinCoverage == null)
			meanProteinCoverage = Float
					.valueOf(ProteinMerger.getCoverage(this, null, retrieveSequenceFromTheInternet, upr));
		return meanProteinCoverage;
	}

	@Override
	public List<ExtendedIdentifiedPeptide> getPeptides() {
		final List<ExtendedIdentifiedPeptide> ret = new ArrayList<ExtendedIdentifiedPeptide>();
		final TIntHashSet peptideIds = new TIntHashSet();
		if (proteinGroups != null) {
			for (final ProteinGroup proteinGroup : proteinGroups) {
				final List<ExtendedIdentifiedPeptide> peptides = proteinGroup.getPeptides();
				if (peptides != null) {
					for (final ExtendedIdentifiedPeptide extendedIdentifiedPeptide : peptides) {
						if (!peptideIds.contains(extendedIdentifiedPeptide.getId())) {
							peptideIds.add(extendedIdentifiedPeptide.getId());
							ret.add(extendedIdentifiedPeptide);
						}
					}
				}

			}
		}
		return ret;
	}

	public boolean isDecoy(FDRFilter filter) {
		if (filter != null) {
			if (proteinGroups != null && !proteinGroups.isEmpty()) {
				for (final ProteinGroup proteinGroup : proteinGroups) {
					if (proteinGroup.isDecoy(filter))
						return true;
				}
			}
		}
		return false;
	}

	public boolean isDecoy() {
		for (final ProteinGroup proteinGroup : proteinGroups) {
			if (proteinGroup.isDecoy())
				return true;
		}
		return false;
	}

	public void setDecoy(boolean isDecoy) {
		for (final ProteinGroup proteinGroup : proteinGroups) {
			proteinGroup.setDecoy(isDecoy);
		}
	}

	/**
	 * Gets the different protein databases in which the
	 * {@link IdentificationItem} were searched
	 * 
	 * @return
	 */
	public List<Database> getProteinDatabases() {
		final List<Database> ret = new ArrayList<Database>();

		for (final ProteinGroup proteinGroup : proteinGroups) {

			final Set<Database> databases = proteinGroup.getDatabases();
			if (databases != null) {
				for (final Database database : databases) {
					boolean found = false;
					for (final Database selectedDatabase : ret) {
						final String selectedDatabaseName = selectedDatabase.getName();
						if (selectedDatabase != null)
							if (selectedDatabaseName.equals(database.getName())) {
								final String selectedDatabaseVersion = selectedDatabase.getNumVersion();
								if (selectedDatabaseVersion != null) {
									if (selectedDatabaseVersion.equals(database.getNumVersion()))
										found = true;
								} else if (selectedDatabaseVersion == null && database.getNumVersion() == null) {
									found = true;
								}
							}
					}
					if (!found)
						ret.add(database);
				}
			}

		}
		return ret;
	}

	/**
	 * Gets the different protein databases in which the
	 * {@link IdentificationItem} were searched
	 * 
	 * @return
	 */
	public Set<Software> getSoftwares() {
		final Set<Software> ret = new THashSet<Software>();
		for (final ProteinGroup proteinGroup : proteinGroups) {
			final Set<Software> softwares = proteinGroup.getSoftwares();
			if (softwares != null) {
				for (final Software software : softwares) {
					boolean found = false;
					for (final Software selectedSoftware : ret) {
						final String selectedDatabaseName = selectedSoftware.getName();
						if (selectedSoftware != null)
							if (selectedDatabaseName.equals(software.getName())) {
								final String selectedSoftwareVersion = selectedSoftware.getVersion();
								if (selectedSoftwareVersion != null) {
									if (selectedSoftwareVersion.equals(software.getVersion()))
										found = true;
								} else if (selectedSoftwareVersion == null && software.getVersion() == null) {
									found = true;
								}
							}
					}
					if (!found)
						ret.add(software);
				}
			}
		}
		return ret;
	}

	/**
	 * Gets the different score names of the {@link IdentificationItem}s
	 * 
	 * @return
	 */
	@Override
	public Set<String> getScoreNames() {
		final Set<String> ret = new THashSet<String>();
		if (proteinGroups != null) {
			for (final ProteinGroup proteinGroup : proteinGroups) {
				final List<String> scoreNames = proteinGroup.getProteinScoreNames();
				for (final String scoreName : scoreNames) {
					ret.add(scoreName);
				}
			}
		}
		return ret;
	}

	/**
	 * Gets the values of a certain score over the {@link IdentificationItem}
	 * list
	 * 
	 * @param scoreName
	 * @return
	 */
	public List<Float> getScoreValues(String scoreName) {
		final List<Float> values = new ArrayList<Float>();
		if (proteinGroups != null)
			for (final ProteinGroup proteinGroup : proteinGroups) {
				final List<Float> scores = proteinGroup.getProteinScores(scoreName);
				if (scores != null)
					values.addAll(scores);
			}
		return values;
	}

	// public ProteinGroup getBestProteinGroups(SortingParameters sorting) {
	//
	// Order order = sorting.getOrder();
	// String scoreName = sorting.getScoreName();
	// double finalScore = 0;
	// if (Order.ASCENDANT.equals(order)) { // we need to report the lowest //
	// // score
	// finalScore = Double.MAX_VALUE;
	// } else if (Order.DESCENDANT.equals(order)) {// we need to report the //
	// // highest value
	// finalScore = Double.MIN_VALUE;
	// } else {
	// return null;
	// }
	// if (scoreName == null || "".equals(scoreName))
	// return null;
	// List<ExtendedIdentifiedProtein> ret = new
	// ArrayList<ExtendedIdentifiedProtein>();
	// final List<ProteinGroup> identificationItemList =
	// this.getIdentificationItemList();
	// List<ExtendedIdentifiedProtein> proteins = new
	// ArrayList<ExtendedIdentifiedProtein>();
	// for (ProteinGroup proteinGroup : identificationItemList) {
	// proteinGroup.get
	// }
	//
	// ExtendedIdentifiedProtein bestProtein = null;
	// for (ExtendedIdentifiedProtein protein : proteins) {
	// if (protein.getScores() != null) {
	// for (ProteinScore score : protein.getScores()) {
	// if (scoreName.equals(score.getName())) {
	// if (Order.ASCENDANT.equals(order)) {
	// if (Double.valueOf(score.getValue()) < finalScore) {
	// finalScore = Double.valueOf(score.getValue());
	// bestProtein = protein;
	// }
	// } else {
	// if (Double.valueOf(score.getValue()) > finalScore) {
	// finalScore = Double.valueOf(score.getValue());
	// bestProtein = protein;
	// }
	// }
	// }
	// }
	//
	// }
	// }
	//
	// return ret;
	// }

	public List<ExtendedIdentifiedProtein> getProteins() {
		final List<ExtendedIdentifiedProtein> ret = new ArrayList<ExtendedIdentifiedProtein>();
		final List<ProteinGroup> groupList = getItemList();
		for (final ProteinGroup proteinGroup : groupList) {
			ret.addAll(proteinGroup);
		}
		SorterUtil.sortProteinsByAccession(ret);
		return ret;
	}

	public List<ExtendedIdentifiedProtein> getProteins(String proteinACC) {
		final List<ExtendedIdentifiedProtein> ret = new ArrayList<ExtendedIdentifiedProtein>();
		final List<ProteinGroup> groupList = getItemList();
		for (final ProteinGroup proteinGroup : groupList) {
			for (final ExtendedIdentifiedProtein extendedIdentifiedProtein : proteinGroup) {
				if (extendedIdentifiedProtein.getAccession().equals(proteinACC))
					ret.add(extendedIdentifiedProtein);
			}

		}
		return ret;
	}

	@Override
	public ExtendedIdentifiedPeptide getBestPeptide() {
		if (bestPeptide != null)
			return bestPeptide;

		final List<ExtendedIdentifiedPeptide> peptides = getPeptides();
		if (peptides.isEmpty())
			return null;
		SorterUtil.sortPeptidesByBestPeptideScore(peptides, false);
		bestPeptide = peptides.get(0);
		return bestPeptide;

	}

	@Override
	public ExtendedIdentifiedPeptide getBestPeptide(String scoreName) {
		final List<ExtendedIdentifiedPeptide> peptides = getPeptides();
		SorterUtil.sortPeptidesByPeptideScore(peptides, scoreName, false);
		if (peptides != null && !peptides.isEmpty())
			return peptides.get(0);
		return null;

	}

	@Override
	public Float getBestPeptideScore() {

		final ExtendedIdentifiedPeptide bestPeptideByScore = this.getBestPeptide();
		if (bestPeptideByScore != null) {
			return bestPeptideByScore.getScore();
		}
		return null;
	}

	@Override
	public Float getBestPeptideScore(String scoreName) {

		final ExtendedIdentifiedPeptide bestPeptideByScore = this.getBestPeptide(scoreName);
		if (bestPeptideByScore != null) {
			return bestPeptideByScore.getScore(scoreName);
		}
		return null;
	}

	public Float getProteinLocalFDR() {
		final List<Float> fdrs = new ArrayList<Float>();
		for (final ProteinGroup proteinGroup : getItemList()) {
			final Float proteinLocalFDR = proteinGroup.getProteinLocalFDR();
			if (proteinLocalFDR != null)
				fdrs.add(proteinLocalFDR);
		}
		// get the minimum
		Float min = Float.MAX_VALUE;
		for (final Float fdr : fdrs) {
			if (fdr < min)
				min = fdr;
		}
		if (min.equals(Float.MAX_VALUE))
			return null;
		return min;
	}

	public void setProteinLocalFDR(Float proteinLocalFDR) {
		for (final ProteinGroup proteinGroup : proteinGroups) {
			proteinGroup.setProteinLocalFDR(proteinLocalFDR);
		}
	}
}
