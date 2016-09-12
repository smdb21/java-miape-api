package org.proteored.miapeapi.experiment.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.proteored.miapeapi.experiment.model.filters.FDRFilter;
import org.proteored.miapeapi.experiment.model.grouping.ProteinEvidence;
import org.proteored.miapeapi.experiment.model.interfaces.Occurrence;
import org.proteored.miapeapi.experiment.model.interfaces.PeptideContainer;
import org.proteored.miapeapi.experiment.model.interfaces.ProteinContainer;
import org.proteored.miapeapi.experiment.model.interfaces.ProteinGroupContainer;
import org.proteored.miapeapi.experiment.model.sort.Order;
import org.proteored.miapeapi.experiment.model.sort.SorterUtil;
import org.proteored.miapeapi.experiment.model.sort.SortingManager;
import org.proteored.miapeapi.experiment.model.sort.SortingParameters;
import org.proteored.miapeapi.interfaces.Software;
import org.proteored.miapeapi.interfaces.msi.Database;
import org.proteored.miapeapi.interfaces.msi.InputParameter;
import org.proteored.miapeapi.interfaces.msi.MiapeMSIDocument;
import org.proteored.miapeapi.interfaces.msi.ProteinScore;
import org.proteored.miapeapi.xml.util.ProteinGroupComparisonType;

public class ProteinGroupOccurrence implements Occurrence<ProteinGroup>,
		ProteinContainer, PeptideContainer, ProteinGroupContainer {
	private static Logger log = Logger.getLogger("log4j.logger.org.proteored");

	private final List<ProteinGroup> proteinGroups = new ArrayList<ProteinGroup>();

	private String name;
	private Float meanProteinCoverage = null;

	private ExtendedIdentifiedPeptide bestPeptide = null;
	private ProteinGroup bestProteinGroup = null;
	private ExtendedIdentifiedProtein bestProtein = null;

	private ProteinEvidence evidence;

	private List<String> accessions;

	public ProteinGroupOccurrence() {

	}

	/**
	 * Gets the comparison key of the ProteinGroupOccurrence depending on the
	 * {@link ProteinGroupComparisonType} passed as parameter
	 * 
	 * @param proteinSelection
	 * @return
	 */
	public String getKey(ProteinGroupComparisonType proteinSelection) {

		if (ProteinGroupComparisonType.ALL_PROTEINS.equals(proteinSelection)) {
			return getFirstOccurrence().getKey();

		} else if (ProteinGroupComparisonType.BEST_PROTEIN
				.equals(proteinSelection)) {
			return this.getBestProtein().getAccession();

		} else if (ProteinGroupComparisonType.FIRST_PROTEIN
				.equals(proteinSelection)) {
			return getAccessions().get(0);

		} else if (ProteinGroupComparisonType.SHARE_ONE_PROTEIN
				.equals(proteinSelection)) {
			return null;
		}
		return null;
	}

	/**
	 * Gets the first accession of the list of proteins
	 * 
	 * @return
	 */
	public String getAccessionsString() {
		List<String> accList = getAccessions();

		String ret = "";
		for (String accession : accList) {
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
		for (ProteinGroup proteinGroup : proteinGroups) {
			final List<String> accessions = proteinGroup.getAccessions();
			for (String accession : accessions) {
				if (!this.accessions.contains(accession))
					this.accessions.add(accession);
			}
		}
		Collections.sort(accessions);

		return accessions;
	}

	@Override
	public String toString() {
		List<ProteinGroup> proteinGroups = getItemList();
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
		ExtendedIdentifiedProtein bestProtein2 = getBestProtein(scoreName);
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
			ProteinGroupOccurrence pgo = (ProteinGroupOccurrence) obj;
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

		SortingParameters sorting = SortingManager.getInstance()
				.getProteinGroupOccurrenceSortingByProteinScore(this);
		if (sorting == null)
			return null;
		Order order = sorting.getOrder();
		String scoreName = sorting.getScoreName();
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
		for (ProteinGroup proteinGroup : proteinGroups) {
			for (ExtendedIdentifiedProtein protein : proteinGroup) {
				if (protein.getScores() != null) {
					for (ProteinScore score : protein.getScores()) {
						if (scoreName.equals(score.getName())) {
							if (Order.ASCENDANT.equals(order)) { // report the
																	// lowest
																	// score
								if (Float.valueOf(score.getValue()) < finalScore) {
									finalScore = Float
											.valueOf(score.getValue());
									ret = proteinGroup;
								}
							} else {
								if (Float.valueOf(score.getValue()) > finalScore) {
									finalScore = Float
											.valueOf(score.getValue());
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
	 * @return the coverage
	 */
	public Float getMeanProteinCoverage(boolean retrieveSequenceFromTheInternet) {
		if (meanProteinCoverage == null)
			meanProteinCoverage = Float.valueOf(ProteinMerger.getCoverage(this,
					null, retrieveSequenceFromTheInternet));
		return meanProteinCoverage;
	}

	@Override
	public List<ExtendedIdentifiedPeptide> getPeptides() {
		List<ExtendedIdentifiedPeptide> ret = new ArrayList<ExtendedIdentifiedPeptide>();
		Set<Integer> peptideIds = new HashSet<Integer>();
		if (proteinGroups != null) {
			for (ProteinGroup proteinGroup : proteinGroups) {
				final List<ExtendedIdentifiedPeptide> peptides = proteinGroup
						.getPeptides();
				if (peptides != null) {
					for (ExtendedIdentifiedPeptide extendedIdentifiedPeptide : peptides) {
						if (!peptideIds.contains(extendedIdentifiedPeptide
								.getId())) {
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
				for (ProteinGroup proteinGroup : proteinGroups) {
					if (proteinGroup.isDecoy(filter))
						return true;
				}
			}
		}
		return false;
	}

	public boolean isDecoy() {
		for (ProteinGroup proteinGroup : proteinGroups) {
			if (proteinGroup.isDecoy())
				return true;
		}
		return false;
	}

	public void setDecoy(boolean isDecoy) {
		for (ProteinGroup proteinGroup : proteinGroups) {
			proteinGroup.setDecoy(isDecoy);
		}
	}

	public List<MiapeMSIDocument> getMiapeMSIs() {
		List<MiapeMSIDocument> ret = new ArrayList<MiapeMSIDocument>();
		List<Integer> ids = new ArrayList<Integer>();
		for (ProteinGroup proteinGroup : proteinGroups) {
			List<MiapeMSIDocument> miapeMSIs = proteinGroup.getMiapeMSIs();
			for (MiapeMSIDocument miapeMSI : miapeMSIs) {
				if (!ids.contains(miapeMSI.getId())) {
					ret.add(miapeMSI);
					ids.add(miapeMSI.getId());
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
	public List<Database> getProteinDatabases() {
		List<Database> ret = new ArrayList<Database>();
		List<MiapeMSIDocument> miapeMSIs = getMiapeMSIs();
		for (MiapeMSIDocument miapeMSIDocument : miapeMSIs) {
			Set<InputParameter> inputParameters = miapeMSIDocument
					.getInputParameters();
			if (inputParameters != null) {
				for (InputParameter inputParameter : inputParameters) {
					Set<Database> databases = inputParameter.getDatabases();
					if (databases != null) {
						for (Database database : databases) {
							boolean found = false;
							for (Database selectedDatabase : ret) {
								String selectedDatabaseName = selectedDatabase
										.getName();
								if (selectedDatabase != null)
									if (selectedDatabaseName.equals(database
											.getName())) {
										String selectedDatabaseVersion = selectedDatabase
												.getNumVersion();
										if (selectedDatabaseVersion != null) {
											if (selectedDatabaseVersion
													.equals(database
															.getNumVersion()))
												found = true;
										} else if (selectedDatabaseVersion == null
												&& database.getNumVersion() == null) {
											found = true;
										}
									}
							}
							if (!found)
								ret.add(database);
						}
					}
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
	public List<Software> getSoftwares() {
		List<Software> ret = new ArrayList<Software>();
		List<MiapeMSIDocument> miapeMSIs = getMiapeMSIs();
		for (MiapeMSIDocument miapeMSIDocument : miapeMSIs) {
			Set<Software> softwares = miapeMSIDocument.getSoftwares();
			if (softwares != null) {
				for (Software software : softwares) {
					boolean found = false;
					for (Software selectedSoftware : ret) {
						String selectedDatabaseName = selectedSoftware
								.getName();
						if (selectedSoftware != null)
							if (selectedDatabaseName.equals(software.getName())) {
								String selectedSoftwareVersion = selectedSoftware
										.getVersion();
								if (selectedSoftwareVersion != null) {
									if (selectedSoftwareVersion.equals(software
											.getVersion()))
										found = true;
								} else if (selectedSoftwareVersion == null
										&& software.getVersion() == null) {
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
		Set<String> ret = new HashSet<String>();
		if (proteinGroups != null) {
			for (ProteinGroup proteinGroup : proteinGroups) {
				List<String> scoreNames = proteinGroup.getProteinScoreNames();
				for (String scoreName : scoreNames) {
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
		List<Float> values = new ArrayList<Float>();
		if (proteinGroups != null)
			for (ProteinGroup proteinGroup : proteinGroups) {
				List<Float> scores = proteinGroup.getProteinScores(scoreName);
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
		List<ExtendedIdentifiedProtein> ret = new ArrayList<ExtendedIdentifiedProtein>();
		final List<ProteinGroup> groupList = getItemList();
		for (ProteinGroup proteinGroup : groupList) {
			ret.addAll(proteinGroup);
		}
		SorterUtil.sortProteinsByAccession(ret);
		return ret;
	}

	public List<ExtendedIdentifiedProtein> getProteins(String proteinACC) {
		List<ExtendedIdentifiedProtein> ret = new ArrayList<ExtendedIdentifiedProtein>();
		final List<ProteinGroup> groupList = getItemList();
		for (ProteinGroup proteinGroup : groupList) {
			for (ExtendedIdentifiedProtein extendedIdentifiedProtein : proteinGroup) {
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

		List<ExtendedIdentifiedPeptide> peptides = getPeptides();
		if (peptides.isEmpty())
			return null;
		SorterUtil.sortPeptidesByBestPeptideScore(peptides, false);
		bestPeptide = peptides.get(0);
		return bestPeptide;

	}

	@Override
	public ExtendedIdentifiedPeptide getBestPeptide(String scoreName) {
		List<ExtendedIdentifiedPeptide> peptides = getPeptides();
		SorterUtil.sortPeptidesByPeptideScore(peptides, scoreName, false);
		if (peptides != null && !peptides.isEmpty())
			return peptides.get(0);
		return null;

	}

	@Override
	public Float getBestPeptideScore() {

		final ExtendedIdentifiedPeptide bestPeptideByScore = this
				.getBestPeptide();
		if (bestPeptideByScore != null) {
			return bestPeptideByScore.getScore();
		}
		return null;
	}

	@Override
	public Float getBestPeptideScore(String scoreName) {

		final ExtendedIdentifiedPeptide bestPeptideByScore = this
				.getBestPeptide(scoreName);
		if (bestPeptideByScore != null) {
			return bestPeptideByScore.getScore(scoreName);
		}
		return null;
	}

	public Float getProteinLocalFDR() {
		List<Float> fdrs = new ArrayList<Float>();
		for (ProteinGroup proteinGroup : getItemList()) {
			Float proteinLocalFDR = proteinGroup.getProteinLocalFDR();
			if (proteinLocalFDR != null)
				fdrs.add(proteinLocalFDR);
		}
		// get the minimum
		Float min = Float.MAX_VALUE;
		for (Float fdr : fdrs) {
			if (fdr < min)
				min = fdr;
		}
		if (min.equals(Float.MAX_VALUE))
			return null;
		return min;
	}

	public void setProteinLocalFDR(Float proteinLocalFDR) {
		for (ProteinGroup proteinGroup : proteinGroups) {
			proteinGroup.setProteinLocalFDR(proteinLocalFDR);
		}
	}
}
