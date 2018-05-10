package org.proteored.miapeapi.experiment.model.filters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.exceptions.IllegalMiapeArgumentException;
import org.proteored.miapeapi.experiment.model.ExtendedIdentifiedPeptide;
import org.proteored.miapeapi.experiment.model.ExtendedIdentifiedProtein;
import org.proteored.miapeapi.experiment.model.IdentificationItemEnum;
import org.proteored.miapeapi.experiment.model.IdentificationSet;
import org.proteored.miapeapi.experiment.model.PeptideOccurrence;
import org.proteored.miapeapi.experiment.model.ProteinGroup;
import org.proteored.miapeapi.experiment.model.ProteinGroupOccurrence;
import org.proteored.miapeapi.experiment.model.datamanager.DataManager;
import org.proteored.miapeapi.experiment.model.grouping.ProteinEvidence;
import org.proteored.miapeapi.experiment.model.sort.Order;
import org.proteored.miapeapi.experiment.model.sort.SorterUtil;
import org.proteored.miapeapi.experiment.model.sort.SortingParameters;
import org.proteored.miapeapi.interfaces.Software;
import org.proteored.miapeapi.interfaces.msi.IdentifiedProtein;

import gnu.trove.set.hash.TIntHashSet;

public class FDRFilter implements Filter {
	private final float threshold;
	private final String decoyPrefix;
	private final Pattern decoyRegExp;
	private final boolean isConcatenatedDecoy;
	private boolean appliedToProteins = false;
	private boolean appliedToPeptides = false;
	private final IdentificationItemEnum identificationItem;
	private SortingParameters sortingParameters;
	private final String replicateName;
	private final String experimentName;
	private final Software software;
	private final boolean separateNonConclusiveProteins;
	private final boolean doNotGroupNonConclusiveProteins;
	private static final Logger log = Logger.getLogger("log4j.logger.org.proteored");

	/**
	 * Constructor
	 * 
	 * @param threshold
	 *            threshold value of FDR. The filter method will return a list
	 *            of proteins that pass the filter of FDR <= that value. This
	 *            value has to be between 0 and 100
	 * @param prefix
	 *            prefix of the protein accession that determines that is a
	 *            decoy hit
	 * @param concatenatedDecoy
	 *            indicates that the decoy search has been done over a decoy
	 *            concatenated database or not
	 */
	public FDRFilter(float threshold, String prefix, boolean concatenatedDecoy, SortingParameters sortingParameters,
			IdentificationItemEnum item, String experimentName, String replicateName,
			boolean doNotGroupNonConclusiveProteins, boolean separateNonConclusiveProteins, Software software) {
		if (threshold < 0 || threshold > 100)
			throw new IllegalMiapeArgumentException("Threshold has to be a number between 0 and 100");
		this.threshold = threshold;
		decoyPrefix = prefix;
		decoyRegExp = null;
		identificationItem = item;
		isConcatenatedDecoy = concatenatedDecoy;
		if (IdentificationItemEnum.PEPTIDE.equals(item) || IdentificationItemEnum.PSM.equals(item))
			appliedToPeptides = true;
		else if (IdentificationItemEnum.PROTEIN.equals(item))
			appliedToProteins = true;

		this.sortingParameters = sortingParameters;
		this.replicateName = replicateName;
		this.experimentName = experimentName;
		this.software = software;
		this.separateNonConclusiveProteins = separateNonConclusiveProteins;
		this.doNotGroupNonConclusiveProteins = doNotGroupNonConclusiveProteins;

	}

	/**
	 * Constructor
	 * 
	 * @param threshold
	 *            threshold value of FDR. The filter method will return a list
	 *            of proteins that pass the filter of FDR <= that value
	 * @param pattern
	 *            patter that matches to a decoy accession
	 * @param concatenatedDecoy
	 *            indicates that the decoy search has been done over a decoy
	 *            concatenated database or not
	 * @param identificationItem
	 *            PEPTIDE (peptide FDR) or PROTEIN (protein FDR)
	 */
	public FDRFilter(float threshold, Pattern pattern, boolean concatenatedDecoy, SortingParameters sortingParameters,
			IdentificationItemEnum item, String experimentName, String replicateName,
			boolean doNotGroupNonConclusiveProteins, boolean separateNonConclusiveProteins, Software software) {
		if (threshold < 0 || threshold > 100)
			throw new IllegalMiapeArgumentException("Threshold has to be a number between 0 and 100");
		this.threshold = threshold;
		decoyRegExp = pattern;
		decoyPrefix = null;
		isConcatenatedDecoy = concatenatedDecoy;
		identificationItem = item;
		if (IdentificationItemEnum.PEPTIDE.equals(item) || IdentificationItemEnum.PSM.equals(item))
			appliedToPeptides = true;
		else if (IdentificationItemEnum.PROTEIN.equals(item))
			appliedToProteins = true;
		this.sortingParameters = sortingParameters;
		this.experimentName = experimentName;
		this.replicateName = replicateName;
		this.software = software;
		this.separateNonConclusiveProteins = separateNonConclusiveProteins;
		this.doNotGroupNonConclusiveProteins = doNotGroupNonConclusiveProteins;
	}

	public float getThreshold() {
		return threshold;
	}

	public boolean isConcatenatedDecoy() {
		return isConcatenatedDecoy;
	}

	public boolean isAppliedToProteins() {
		return appliedToProteins;
	}

	public boolean isAppliedToPeptides() {
		return appliedToPeptides;
	}

	public boolean isDecoy(String acc) {
		if (acc != null) {
			if (decoyRegExp != null) {
				final Matcher m = decoyRegExp.matcher(acc);
				return m.find();
			} else if (decoyPrefix != null) {
				return acc.startsWith(decoyPrefix);
			}
		}
		return true;
	}

	// /**
	// * Get an ordered Map of the proteins after filter them by an FDR
	// threshold
	// *
	// * @param sortedProteinHash
	// * a sorted protein list
	// * @return
	// */
	// public LinkedHashMap<String, IdentifiedProtein> filterProteins(
	// LinkedHashMap<String, IdentifiedProtein> sortedProteinHash) {
	//
	// log.info("filtering by protein FDR <= " + this.threshold);
	// LinkedHashMap<String, IdentifiedProtein> ret = new LinkedHashMap<String,
	// IdentifiedProtein>();
	// long numFWHits = 0; // forward hits
	// long numDCHits = 0; // decoy hits
	// log.info("Calculating protein FDR values...");
	// for (String proteinACC : sortedProteinHash.keySet()) {
	// if (isDecoy(proteinACC))
	// numDCHits++;
	// else
	// numFWHits++;
	// double currentFDR = calculateFDR(numFWHits, numDCHits);
	// // log.info("Current FDR = " + currentFDR);
	// if (currentFDR > this.threshold) {
	// log.info("Threshold reached (protein FDR=" + currentFDR + " > " +
	// this.threshold
	// + ") " + ret.size() + " proteins pass threshold");
	// return ret;
	// } else {
	// ret.put(proteinACC, sortedProteinHash.get(proteinACC));
	// }
	// }
	// log.info("No threshold has been reached. " + ret.size() +
	// " proteins pass it");
	// return ret;
	// }
	public SortingParameters getSortingParameters() {
		return sortingParameters;
	}

	public void setSortingParameters(SortingParameters sortingParameters) {
		this.sortingParameters = sortingParameters;
	}

	// public List<ProteinGroup> filterNEW_FAILED(
	// List<ProteinGroup> proteinGroups, IdentificationSet currentIdSet) {
	//
	// if (appliedToPeptides) {
	// List<ExtendedIdentifiedPeptide> identifiedPeptides = DataManager
	// .getPeptidesFromProteinGroups(proteinGroups);
	// TIntHashSet filteredPeptideIDs = filterPeptides(identifiedPeptides);
	// return DataManager.filterProteinGroupsByPeptides(proteinGroups,
	// filteredPeptideIDs, currentIdSet.getCvManager());
	//
	// } else if (appliedToProteins) {
	//
	// List<ExtendedIdentifiedPeptide> identifiedPeptides = DataManager
	// .getPeptidesFromProteinGroups(proteinGroups);
	// TIntHashSet filteredPeptideIDs =
	// filterPeptidesByProteins(identifiedPeptides);
	// return DataManager.filterProteinGroupsByPeptides(proteinGroups,
	// filteredPeptideIDs, currentIdSet.getCvManager());
	// }
	// return null;
	// }

	/**
	 * @param proteinGroups
	 * @param currentIdSet
	 * @return
	 */
	@Override
	public List<ProteinGroup> filter(List<ProteinGroup> proteinGroups, IdentificationSet currentIdSet) {

		ControlVocabularyManager cvManager = null;
		if (currentIdSet != null)
			cvManager = currentIdSet.getCvManager();
		if (appliedToPeptides) {
			if (identificationItem.equals(IdentificationItemEnum.PEPTIDE)) {
				final List<ExtendedIdentifiedPeptide> identifiedPeptides = DataManager
						.getPeptidesFromProteinGroups(proteinGroups);
				final TIntHashSet filteredPeptideIDs = filterPeptides(identifiedPeptides);
				return DataManager.filterProteinGroupsByPeptides(proteinGroups, doNotGroupNonConclusiveProteins,
						separateNonConclusiveProteins, filteredPeptideIDs, cvManager);
			} else if (identificationItem.equals(IdentificationItemEnum.PSM)) {
				final List<ExtendedIdentifiedPeptide> identifiedPeptides = DataManager
						.getPeptidesFromProteinGroups(proteinGroups);
				final TIntHashSet filteredPeptideIDs = filterPSMs(identifiedPeptides);
				return DataManager.filterProteinGroupsByPeptides(proteinGroups, doNotGroupNonConclusiveProteins,
						separateNonConclusiveProteins, filteredPeptideIDs, cvManager);
			}

		} else if (appliedToProteins) {

			log.info("sorting " + proteinGroups.size() + " protein  groups by peptide score before to apply FDR");
			final Collection<ProteinGroupOccurrence> proteinGroupOccurrencesSet = DataManager
					.createProteinGroupOccurrenceList(proteinGroups).values();
			final List<ProteinGroupOccurrence> proteinGroupOccurrences = new ArrayList<ProteinGroupOccurrence>();
			for (final ProteinGroupOccurrence proteinGroupOccurrence : proteinGroupOccurrencesSet) {
				proteinGroupOccurrences.add(proteinGroupOccurrence);
			}
			SorterUtil.sortProteinGroupOcurrencesByPeptideScore(proteinGroupOccurrences,
					sortingParameters.getScoreName());

			long numFWHits = 0; // forward hits
			long numDCHits = 0; // decoy hits

			// List<ProteinGroup> proteinGroupsList = new
			// ArrayList<ProteinGroup>();
			final TIntHashSet peptidesToIncludeAfterFilter = new TIntHashSet();

			Float bestPeptideScore = null;
			Float currentFDR = null;
			// - 4: for each peptide, look to its protein and count if it is
			// decoy or not. Ignore it if already has been seen
			for (final ProteinGroupOccurrence proteinGroupOccurrence : proteinGroupOccurrences) {
				bestPeptideScore = proteinGroupOccurrence.getBestPeptideScore(sortingParameters.getScoreName());
				if (bestPeptideScore != null) {
					// if (!proteinGroupsList.contains(proteinGroup)) {
					// proteinGroupsList.add(proteinGroup);

					if (proteinGroupOccurrence.getEvidence() != ProteinEvidence.NONCONCLUSIVE) {

						if (proteinGroupOccurrence.isDecoy(this)) {
							numDCHits++;
							proteinGroupOccurrence.setDecoy(true);
						} else {
							numFWHits++;
							proteinGroupOccurrence.setDecoy(false);
						}
						// }
						// else {
						// //
						// log.info("Protein group nonconclusive... skipped for
						// count in FDR calculation");
						// }
						currentFDR = calculateFDR(numFWHits, numDCHits);
						proteinGroupOccurrence.setProteinLocalFDR(currentFDR);

						// log.info("Current FDR = "
						// + currentFDR
						// + " "
						// + proteinGroupOccurrence
						// + " -> "
						// + proteinGroupOccurrence.getBestPeptide(
						// this.sortingParameters.getScoreName())
						// .getSequence() + " - "
						// + bestPeptideScore);
						if (currentFDR > threshold) {
							log.info("Threshold reached (FW=" + numFWHits + ", DC=" + numDCHits + ") -> (protein FDR="
									+ currentFDR + " > " + threshold + ") " + proteinGroupOccurrence + " -> "
									+ bestPeptideScore);
							log.info("This is new!!! (25Oct12)");
							log.info("Now filtering peptides in these proteins by peptide score=" + bestPeptideScore);
							peptidesToIncludeAfterFilter.addAll(getPeptideIdsThatPassScoreThreshold(proteinGroups,
									bestPeptideScore, sortingParameters));
							break;

						}
					}
					// }
				}

			}
			// In case of no threshold has been reached
			if (currentFDR <= threshold) {
				log.info("No threshold has been reached. " + proteinGroups.size() + " proteins pass it");
				// Include all peptides
				peptidesToIncludeAfterFilter.clear();
				peptidesToIncludeAfterFilter
						.addAll(getPeptideIdsThatPassScoreThreshold(proteinGroups, null, sortingParameters));
			}

			return DataManager.filterProteinGroupsByPeptides(proteinGroups, doNotGroupNonConclusiveProteins,
					separateNonConclusiveProteins, peptidesToIncludeAfterFilter, cvManager);
		}
		return null;
	}

	/**
	 * Apply the FDR Filter to a list of peptides, but not counting all
	 * peptides, just counting the best occurrence of each one, and the best
	 * occurrence of each protein.
	 * 
	 * @param identifiedPeptides
	 * @return
	 */
	// private TIntHashSet filterPeptidesByProteins(
	// List<ExtendedIdentifiedPeptide> identifiedPeptides) {
	//
	// log.info("filtering " + identifiedPeptides.size()
	// + " peptides by FDR <= " + this.threshold);
	//
	// long numFWHits = 0; // forward hits
	// long numDCHits = 0; // decoy hits
	//
	// // create peptide occurrence in order to avoid PSM redundancy
	// Collection<PeptideOccurrence> peptideOccurrenceCollection = DataManager
	// .createPeptideOccurrenceList(identifiedPeptides, false)
	// .values();
	// List<PeptideOccurrence> peptideOccurrenceList = new
	// ArrayList<PeptideOccurrence>();
	// for (PeptideOccurrence identificationOccurrence :
	// peptideOccurrenceCollection) {
	// peptideOccurrenceList.add(identificationOccurrence);
	// }
	// // Sort peptide occurrences by score
	// log.info("sorting " + peptideOccurrenceList.size()
	// + " peptide occurrences before to apply FDR");
	//
	// SorterUtil
	// .sortPeptideOcurrencesByBestPeptideScore(peptideOccurrenceList);
	// int i = 1;
	// Set<String> proteinAccs = new THashSet<String>();
	// double thresholdScore = Double.MIN_VALUE;
	//
	// for (PeptideOccurrence peptideOccurrence : peptideOccurrenceList) {
	// peptideOccurrence.setFDRFilter(this);
	// final ExtendedIdentifiedPeptide bestPeptide = peptideOccurrence
	// .getBestPeptide();
	//
	// Set<ExtendedIdentifiedProtein> proteinList = peptideOccurrence
	// .getProteinList();
	// boolean thereIsANewProtein = false;
	// for (ExtendedIdentifiedProtein extendedIdentifiedProtein : proteinList) {
	// if (!proteinAccs.contains(extendedIdentifiedProtein
	// .getAccession()))
	// thereIsANewProtein = true;
	// proteinAccs.add(extendedIdentifiedProtein.getAccession());
	// }
	// // Just count if the protein is new
	// if (thereIsANewProtein) {
	// if (bestPeptide != null)
	// thresholdScore = bestPeptide.getBestPeptideScore();
	// if (bestPeptide != null && bestPeptide.isDecoy(this)) {
	// numDCHits++;
	// peptideOccurrence.setDecoy(true);
	// } else {
	// numFWHits++;
	// peptideOccurrence.setDecoy(false);
	// }
	// }
	// double currentFDR = calculateFDR(numFWHits, numDCHits);
	// // log.info(i++ + "\t" + peptideOccurrence.getBestPeptideScore()
	// // + "\t" + numDCHits + "\t" + numFWHits + "\t" + currentFDR
	// // + "\t" + peptideOccurrence.getKey() + " scoreThreshold="
	// // + thresholdScore);
	//
	// // log.info("Current FDR = " + currentFDR);
	// if (currentFDR > this.threshold) {
	// log.info("Threshold reached (peptide FDR=" + currentFDR + " > "
	// + this.threshold + ") NumFW:" + numFWHits + " numDC:"
	// + numDCHits + " Score="
	// + peptideOccurrence.getBestPeptideScore()
	// + " scoreThreshold=" + thresholdScore);
	//
	// TIntHashSet peptideIds = getPeptidesIdentifiersThatPassScoreThreshold(
	// thresholdScore, identifiedPeptides);
	// log.info("Threshold reached. " + peptideIds.size()
	// + " peptides pass it");
	// return peptideIds;
	// }
	// }
	//
	// TIntHashSet peptideIds = getPeptidesIdentifiersThatPassScoreThreshold(
	// thresholdScore, identifiedPeptides);
	// log.info("No threshold has been reached. " + peptideIds.size()
	// + " peptides pass it");
	// return peptideIds;
	// }

	/**
	 * Gets the peptide identifiers of peptides that pass a score threshold.
	 * 
	 * @param thresholdScore
	 *            if null, alll peptides pass the threshold
	 * @param identifiedPeptides
	 * @return
	 */
	private TIntHashSet getPeptidesIdentifiersThatPassScoreThreshold(Float thresholdScore,
			List<ExtendedIdentifiedPeptide> identifiedPeptides) {
		final TIntHashSet ret = new TIntHashSet();
		for (final ExtendedIdentifiedPeptide peptide : identifiedPeptides) {
			if (thresholdScore != null) {
				final Float score = peptide.getScore(sortingParameters.getScoreName());
				if (score != null) {
					if (getSortingParameters().getOrder().equals(Order.ASCENDANT)) {
						if (score <= thresholdScore)
							ret.add(peptide.getId());
					} else {
						if (score >= thresholdScore)
							ret.add(peptide.getId());
					}
				}
			} else {
				ret.add(peptide.getId());
			}
		}
		return ret;
	}

	/**
	 * This function returns a set of peptide identifiers that belongs to
	 * peptides that have passed the threshold
	 * 
	 * @param proteinGroups
	 * 
	 * @param thresholdScore
	 *            if it is null, all peptides will be included
	 * @param sortingParameters
	 * @return empty if there is not any peptide that pass the threshold
	 */
	private TIntHashSet getPeptideIdsThatPassScoreThreshold(List<ProteinGroup> proteinGroups, Float thresholdScore,
			SortingParameters sortingParameters) {
		final TIntHashSet ret = new TIntHashSet();

		for (final ProteinGroup proteinGroup : proteinGroups) {
			final List<ExtendedIdentifiedPeptide> peptides = proteinGroup.getPeptides();
			if (peptides != null) {
				for (final ExtendedIdentifiedPeptide extendedIdentifiedPeptide : peptides) {
					if (sortingParameters == null || thresholdScore == null) {
						ret.add(extendedIdentifiedPeptide.getId());
					} else {
						final Float score = extendedIdentifiedPeptide.getScore(sortingParameters.getScoreName());
						if (score != null) {
							if (sortingParameters.getOrder().equals(Order.DESCENDANT) && score >= thresholdScore)
								ret.add(extendedIdentifiedPeptide.getId());
							else if (sortingParameters.getOrder().equals(Order.ASCENDANT) && score <= thresholdScore)
								ret.add(extendedIdentifiedPeptide.getId());
						}
						// else
						// log.info("Peptide with score="
						// + extendedIdentifiedPeptide
						// .getScore(sortingParameters
						// .getScoreName())
						// + " removed");
					}
				}
			}
		}
		log.info(ret.size() + " peptides pass the threshold: " + sortingParameters.getScoreName() + " -> "
				+ thresholdScore);

		return ret;

	}

	// @Override
	// public List<ExtendedIdentifiedProtein> filter(
	// List<ExtendedIdentifiedProtein> identifiedProteins, IdentificationSet
	// currentIdSet) {
	//
	// if (appliedToPeptides) {
	// List<ExtendedIdentifiedPeptide> identifiedPeptides = DataManager
	// .getPeptidesFromProteins(identifiedProteins);
	// TIntObjectHashMap< ExtendedIdentifiedPeptide> filteredPeptides =
	// filterPeptides(
	// identifiedPeptides, currentIdSet);
	// return DataManager.filterProteinsByPeptides(identifiedProteins,
	// filteredPeptides);
	//
	// } else if (appliedToProteins) {
	// log.info("sorting " + identifiedProteins.size() +
	// " proteins before to apply FDR");
	//
	// SorterUtil.sortProteinsByScore(identifiedProteins, sortingParameters);
	//
	// log.info("filtering " + identifiedProteins.size() +
	// " proteins by FDR <= "
	// + this.threshold);
	// List<ExtendedIdentifiedProtein> ret = new
	// ArrayList<ExtendedIdentifiedProtein>();
	// long numFWHits = 0; // forward hits
	// long numDCHits = 0; // decoy hits
	// // log.info("Calculating protein FDR values...");
	// // for (IdentifiedProtein protein : identifiedProteins) {
	// // System.out.println(protein.getScores().iterator().next().getValue());
	// // }
	// for (ExtendedIdentifiedProtein protein : identifiedProteins) {
	// String proteinACC = protein.getAccession();
	// if (isDecoy(proteinACC))
	// numDCHits++;
	// else
	// numFWHits++;
	// double currentFDR = calculateFDR(numFWHits, numDCHits);
	// // log.info("Current FDR = " + currentFDR);
	// if (currentFDR > this.threshold) {
	// log.info("Threshold reached (FW=" + numFWHits + ", DC=" + numDCHits
	// + ") -> (protein FDR=" + currentFDR + " > " + this.threshold + ") "
	// + ret.size() + " proteins pass threshold");
	// return ret;
	// } else {
	// ret.add(protein);
	// }
	// }
	// log.info("No threshold has been reached. " + ret.size() +
	// " proteins pass it");
	// return ret;
	// }
	// return null;
	// }

	public float calculateFDR(long numFWHits, long numDCHits) {
		final float ret = (float) (numDCHits * 100.0 / (numFWHits + numDCHits));
		if (isConcatenatedDecoy)
			return 2 * ret;
		else
			return ret;
	}

	private TIntHashSet filterPeptides(List<ExtendedIdentifiedPeptide> identifiedPeptides) {

		log.info("filtering " + identifiedPeptides.size() + " peptides by FDR <= " + threshold);
		long numFWHits = 0; // forward hits
		long numDCHits = 0; // decoy hits
		long numPSMFWHits = 0; // forward hits for PSMs
		long numPSMDCHits = 0; // decoy hits for PSMs

		// create peptide occurrence in order to avoid PSM redundancy
		final Collection<PeptideOccurrence> peptideOccurrenceCollection = DataManager
				.createPeptideOccurrenceListInParallel(identifiedPeptides, false).values();
		final List<PeptideOccurrence> peptideOccurrenceList = new ArrayList<PeptideOccurrence>();
		for (final PeptideOccurrence identificationOccurrence : peptideOccurrenceCollection) {
			peptideOccurrenceList.add(identificationOccurrence);
		}
		// Sort peptide occurrences by score
		log.info("sorting " + peptideOccurrenceList.size() + " peptide occurrences before to apply FDR");

		SorterUtil.sortPeptideOcurrencesByPeptideScore(peptideOccurrenceList, sortingParameters.getScoreName());
		final int i = 1;
		for (final PeptideOccurrence peptideOccurrence : peptideOccurrenceList) {
			final List<ExtendedIdentifiedPeptide> peptides = peptideOccurrence.getPeptides();
			SorterUtil.sortPeptidesByPeptideScore(peptides, sortingParameters.getScoreName(), false);
			for (final ExtendedIdentifiedPeptide peptide : peptides) {
				if (peptide != null && peptide.isDecoy(this)) {
					numPSMDCHits++;
				} else {
					numPSMFWHits++;
				}
				final float currentPSMFDR = calculateFDR(numPSMFWHits, numPSMDCHits);
				peptide.setPSMLocalFDR(currentPSMFDR);
			}

			peptideOccurrence.setFDRFilter(this);
			final ExtendedIdentifiedPeptide bestPeptide = peptideOccurrence
					.getBestPeptide(sortingParameters.getScoreName());

			// String accession =
			// peptide.getIdentifiedProteins().get(0).getAccession();
			if (bestPeptide != null && bestPeptide.isDecoy(this)) {
				numDCHits++;
				peptideOccurrence.setDecoy(true);
			} else {
				numFWHits++;
				peptideOccurrence.setDecoy(false);
			}
			final float currentFDR = calculateFDR(numFWHits, numDCHits);
			peptideOccurrence.setPeptideLocalFDR(currentFDR);

			// System.out.println(i++
			// + "\t"
			// + peptideOccurrence.getBestPeptideScore(sortingParameters
			// .getScoreName()) + "\t" + numDCHits + "\t"
			// + numFWHits + "\t" + currentFDR + "\t"
			// + peptideOccurrence.getKey());

			// log.info("Current FDR = " + currentFDR);
			if (currentFDR > threshold) {
				final Float bestPeptideScore = peptideOccurrence.getBestPeptideScore(sortingParameters.getScoreName());

				final TIntHashSet peptidesIdentifiersThatPassScoreThreshold = getPeptidesIdentifiersThatPassScoreThreshold(
						bestPeptideScore, identifiedPeptides);
				log.info("Threshold reached (peptide FDR=" + currentFDR + " > " + threshold + ") "
						+ peptidesIdentifiersThatPassScoreThreshold.size() + " peptides pass threshold. NumFW:"
						+ numFWHits + " numDC:" + numDCHits + " Score=" + bestPeptideScore);
				return peptidesIdentifiersThatPassScoreThreshold;

			}

		}

		final TIntHashSet peptidesIdentifiersThatPassScoreThreshold = getPeptidesIdentifiersThatPassScoreThreshold(null,
				identifiedPeptides);
		log.info("No threshold has been reached. " + peptidesIdentifiersThatPassScoreThreshold.size()
				+ " peptides pass it");
		return peptidesIdentifiersThatPassScoreThreshold;

	}

	private TIntHashSet filterPSMs(List<ExtendedIdentifiedPeptide> identifiedPeptides) {

		log.info("filtering " + identifiedPeptides.size() + " PSMs by FDR <= " + threshold);
		long numFWHits = 0; // forward hits
		long numDCHits = 0; // decoy hits

		SorterUtil.sortPeptidesByPeptideScore(identifiedPeptides, sortingParameters.getScoreName(), true);
		int i = 0;
		for (final ExtendedIdentifiedPeptide peptide : identifiedPeptides) {
			peptide.setFDRFilter(this);
			i++;
			// String accession =
			// peptide.getIdentifiedProteins().get(0).getAccession();
			if (peptide.isDecoy(this)) {
				numDCHits++;
				peptide.setDecoy(true);
			} else {
				numFWHits++;
				peptide.setDecoy(false);
			}
			final float currentFDR = calculateFDR(numFWHits, numDCHits);
			peptide.setPSMLocalFDR(currentFDR);

			// System.out.println(i++
			// + "\t"
			// + peptideOccurrence.getBestPeptideScore(sortingParameters
			// .getScoreName()) + "\t" + numDCHits + "\t"
			// + numFWHits + "\t" + currentFDR + "\t"
			// + peptideOccurrence.getKey());

			// log.info("Current FDR = " + currentFDR);
			if (currentFDR > threshold) {
				final Float peptideScore = peptide.getScore(sortingParameters.getScoreName());

				final TIntHashSet peptidesIdentifiersThatPassScoreThreshold = getPeptidesIdentifiersThatPassScoreThreshold(
						peptideScore, identifiedPeptides);
				log.info("Threshold reached (peptide FDR=" + currentFDR + " > " + threshold + ") "
						+ peptidesIdentifiersThatPassScoreThreshold.size() + " peptides pass threshold. NumFW:"
						+ numFWHits + " numDC:" + numDCHits + " Score=" + peptideScore);
				return peptidesIdentifiersThatPassScoreThreshold;

			}

		}

		final TIntHashSet peptidesIdentifiersThatPassScoreThreshold = getPeptidesIdentifiersThatPassScoreThreshold(null,
				identifiedPeptides);
		log.info("No threshold has been reached. " + peptidesIdentifiersThatPassScoreThreshold.size()
				+ " peptides pass it");
		return peptidesIdentifiersThatPassScoreThreshold;

	}

	// backup:
	// private TIntObjectHashMap< ExtendedIdentifiedPeptide> filterPeptides(
	// List<ExtendedIdentifiedPeptide> identifiedPeptides, IdentificationSet
	// currentIdSet) {
	//
	// log.info("sorting " + identifiedPeptides.size() +
	// " peptides before to apply FDR");
	// SorterUtil.sortPeptidesByScore(identifiedPeptides, sortingParameters);
	//
	// log.info("filtering " + identifiedPeptides.size() +
	// " peptides by FDR <= " + this.threshold);
	// TIntObjectHashMap< ExtendedIdentifiedPeptide> ret = new
	// TIntObjectHashMap<
	// ExtendedIdentifiedPeptide>();
	// long numFWHits = 0; // forward hits
	// long numDCHits = 0; // decoy hits
	// // log.info("Calculating peptide FDR values...");
	// for (ExtendedIdentifiedPeptide peptide : identifiedPeptides) {
	// if (peptide != null && peptide.getScores() != null)
	// for (PeptideScore score : peptide.getScores()) {
	// if (score.getName().startsWith("Phenyx:Pepz")) {
	// System.out.println(peptide.getIdentifiedProteins().iterator().next()
	// .getAccession()
	// + " -> " + score.getName() + "=" + score.getValue());
	// }
	// }
	//
	// }
	// for (ExtendedIdentifiedPeptide peptide : identifiedPeptides) {
	//
	// final List<IdentifiedProtein> identifiedProteins =
	// peptide.getIdentifiedProteins();
	// if (identifiedProteins != null && !identifiedProteins.isEmpty()) {
	// int numProteins = identifiedProteins.size();
	// // if (numProteins > 1)
	// // log.info("this peptide has " + numProteins + " proteins");
	// String proteinACC = identifiedProteins.get(0).getAccession();
	// if (isDecoy(proteinACC)) {
	// numDCHits++;
	// peptide.setDecoy(true);
	// } else {
	// numFWHits++;
	// peptide.setDecoy(false);
	// }
	// // } else {
	// // numDCHits++;
	// // peptide.setDecoy(true);
	// // }
	// double currentFDR = calculateFDR(numFWHits, numDCHits);
	// // System.out.println(proteinACC + " - " + currentFDR);
	// peptide.setIndividualFdr(currentFDR);
	//
	// // log.info("Current FDR = " + currentFDR);
	// if (currentFDR > this.threshold) {
	// log.info("Threshold reached (peptide FDR=" + currentFDR + " > "
	// + this.threshold + ") " + ret.size() + " peptides pass threshold");
	// return ret;
	// } else {
	// ret.put(peptide.getId(), peptide);
	// }
	// }
	// }
	// log.info("No threshold has been reached. " + ret.size() +
	// " peptides pass it");
	// return ret;
	// }

	/**
	 * If some of the proteins in the list are decoy, it is decoy
	 * 
	 * @param identifiedProteins
	 * @return
	 */
	public boolean isDecoy(List<IdentifiedProtein> identifiedProteins) {
		if (identifiedProteins == null)
			return true;
		for (final IdentifiedProtein identifiedProtein : identifiedProteins) {
			if (isDecoy(identifiedProtein.getAccession()))
				return true;
		}
		return false;
	}

	public boolean isDecoyExtendedProteins(List<ExtendedIdentifiedProtein> identifiedProteins) {
		if (identifiedProteins == null)
			return true;
		for (final ExtendedIdentifiedProtein identifiedProtein : identifiedProteins) {
			if (isDecoy(identifiedProtein.getAccession()))
				return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object paramObject) {
		if (paramObject != null)
			if (paramObject instanceof FDRFilter) {
				final FDRFilter filter = (FDRFilter) paramObject;
				if (filter.isConcatenatedDecoy != isConcatenatedDecoy)
					return false;
				if (filter.decoyPrefix != null) {
					if (!filter.decoyPrefix.equals(decoyPrefix))
						return false;
				} else {
					if (decoyPrefix != null)
						return false;
				}
				if (filter.decoyRegExp != null) {
					if (!filter.decoyRegExp.pattern().equals(decoyRegExp.pattern()))
						return false;
				} else {
					if (decoyRegExp != null)
						return false;
				}
				if (filter.threshold != threshold)
					return false;
				if (!filter.sortingParameters.equals(sortingParameters))
					return false;
				if (filter.appliedToPeptides && !appliedToPeptides)
					return false;
				if (filter.appliedToProteins && !appliedToProteins)
					return false;
				if (filter.identificationItem != identificationItem)
					return false;
				return true;
			}
		return super.equals(paramObject);
	}

	@Override
	public boolean appliedToProteins() {
		return appliedToProteins;
	}

	@Override
	public boolean appliedToPeptides() {
		return appliedToPeptides;
	}

	public String getReplicateName() {
		return replicateName;
	}

	public String getExperimentName() {
		return experimentName;
	}

	@Override
	public String toString() {
		String level;
		if (appliedToPeptides) {
			if (IdentificationItemEnum.PSM.equals(identificationItem))
				level = "PSM level";
			else
				level = "peptide level";
		} else
			level = "protein level";
		String ret = "FDR at " + threshold + "% at " + level;
		// if (this.experimentName!=null)
		// ret += " in '"+experimentName + "'";
		if (replicateName != null)
			ret += " in '" + replicateName + "'";
		ret += " (" + sortingParameters.getScoreName() + ")";
		return ret;
	}

	@Override
	public Software getSoftware() {
		return software;
	}
}
