package org.proteored.miapeapi.experiment.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.proteored.miapeapi.experiment.model.filters.FDRFilter;
import org.proteored.miapeapi.experiment.model.interfaces.Occurrence;
import org.proteored.miapeapi.experiment.model.interfaces.PeptideContainer;
import org.proteored.miapeapi.experiment.model.interfaces.ProteinContainer;
import org.proteored.miapeapi.experiment.model.sort.SorterUtil;
import org.proteored.miapeapi.experiment.model.sort.SortingParameters;
import org.proteored.miapeapi.interfaces.Software;
import org.proteored.miapeapi.interfaces.msi.Database;

import gnu.trove.set.hash.THashSet;
import gnu.trove.set.hash.TIntHashSet;

public class ProteinOccurrence implements Occurrence<ExtendedIdentifiedProtein>, ProteinContainer, PeptideContainer {
	private static Logger log = Logger.getLogger("log4j.logger.org.proteored");

	private final List<ExtendedIdentifiedProtein> proteinList = new ArrayList<ExtendedIdentifiedProtein>();
	private final String key;
	private Double proteinCoverage = null;
	private String proteinSequence = null;
	private ExtendedIdentifiedPeptide bestPeptide = null;
	private ExtendedIdentifiedProtein bestProtein = null;

	private List<ExtendedIdentifiedPeptide> peptides;
	private HashMap<String, List<ExtendedIdentifiedPeptide>> peptideListByScoreNames;

	public ProteinOccurrence(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}

	/**
	 * In case of haveing {@link ExtendedIdentifiedProtein}, it will return the
	 * parsedAccession of the protein Is not, it will return the key
	 * 
	 * @return
	 */
	public String getParsedKey() {
		if (proteinList != null) {
			for (ExtendedIdentifiedProtein object : proteinList) {
				return object.getParsedAccession();

			}
		}
		return getKey();
	}

	@Override
	public ExtendedIdentifiedProtein getFirstOccurrence() {
		return proteinList.get(0);
	}

	@Override
	public void addOccurrence(ExtendedIdentifiedProtein protein) {
		proteinCoverage = null;
		bestPeptide = null;
		bestProtein = null;

		// if (!this.proteinList.contains(protein))
		proteinList.add(protein);

	}

	@Override
	public List<ExtendedIdentifiedProtein> getItemList() {
		return proteinList;
	}

	@Override
	public ExtendedIdentifiedPeptide getBestPeptide() {
		if (bestPeptide != null)
			return bestPeptide;
		final List<ExtendedIdentifiedPeptide> peptides = this.getPeptides();
		if (peptides != null && !peptides.isEmpty()) {
			SorterUtil.sortPeptidesByBestPeptideScore(peptides, false);
			bestPeptide = peptides.get(0);
			return peptides.get(0);
		}
		return null;
	}

	@Override
	public ExtendedIdentifiedPeptide getBestPeptide(String scoreName) {

		final List<ExtendedIdentifiedPeptide> peptides = this.getPeptides(scoreName);
		if (peptides != null && !peptides.isEmpty()) {
			SorterUtil.sortPeptidesByPeptideScore(peptides, scoreName, false);
			return peptides.get(0);
		}
		return null;
	}

	@Override
	public Float getBestPeptideScore() {
		final ExtendedIdentifiedPeptide bestPeptideByPeptideScore = this.getBestPeptide();
		if (bestPeptideByPeptideScore != null)
			return bestPeptideByPeptideScore.getScore();
		return null;
	}

	@Override
	public Float getBestPeptideScore(String scoreName) {
		final ExtendedIdentifiedPeptide bestPeptideByPeptideScore = this.getBestPeptide(scoreName);
		if (bestPeptideByPeptideScore != null)
			return bestPeptideByPeptideScore.getScore(scoreName);
		return null;
	}

	/**
	 * Gets the best score of the {@link ExtendedIdentifiedProtein}s according
	 * to a {@link SortingParameters}
	 * 
	 * @param sorting
	 * @return
	 */
	@Override
	public Float getBestProteinScore() {
		final ExtendedIdentifiedProtein bestProteinByProteinScore = this.getBestProtein();
		if (bestProteinByProteinScore != null)
			return bestProteinByProteinScore.getScore();
		return null;
	}

	@Override
	public Float getBestProteinScore(String scoreName) {
		final ExtendedIdentifiedProtein bestProteinByProteinScore = this.getBestProtein(scoreName);
		if (bestProteinByProteinScore != null)
			return bestProteinByProteinScore.getScore(scoreName);
		return null;
	}

	/**
	 * Gets the best {@link ExtendedIdentifiedProtein} according to the score
	 * defined in a {@link SortingParameters}
	 * 
	 * @param sorting
	 * @return
	 */
	@Override
	public ExtendedIdentifiedProtein getBestProtein() {
		if (bestProtein != null)
			return bestProtein;
		List<ExtendedIdentifiedProtein> proteins = getItemList();
		SorterUtil.sortProteinsByBestProteinScore(proteins, false);
		bestProtein = proteins.get(0);
		return bestProtein;
	}

	@Override
	public ExtendedIdentifiedProtein getBestProtein(String scoreName) {

		List<ExtendedIdentifiedProtein> proteins = getItemList();
		if (proteins != null && !proteins.isEmpty()) {
			SorterUtil.sortProteinsByProteinScore(proteins, scoreName, false);
			if (proteins.get(0).getScoreNames().contains(scoreName))
				bestProtein = proteins.get(0);
		}
		return bestProtein;
	}

	/**
	 * @return the coverage
	 */
	public Double getProteinCoverage() {
		return proteinCoverage;
	}

	/**
	 * @param coverage
	 *            the coverage to set
	 */
	public void setProteinCoverage(Double coverage) {
		proteinCoverage = coverage;
	}

	/**
	 * @return the proteinSequence
	 */
	public String getProteinSequence() {
		if (proteinSequence == null) {
			if (proteinList != null) {
				for (ExtendedIdentifiedProtein protein : proteinList) {
					if (protein.getProteinSequence() != null)
						proteinSequence = protein.getProteinSequence();

				}
			}
		}
		return proteinSequence;
	}

	/**
	 * @param proteinSequence
	 *            the proteinSequence to set
	 */
	public void setProteinSequence(String proteinSequence) {
		this.proteinSequence = proteinSequence;
		if (proteinList != null) {
			for (ExtendedIdentifiedProtein protein : proteinList) {
				protein.setProteinSequence(proteinSequence);

			}
		}
	}

	@Override
	public List<ExtendedIdentifiedPeptide> getPeptides() {
		if (peptides == null || peptides.isEmpty()) {

			peptides = new ArrayList<ExtendedIdentifiedPeptide>();
			if (proteinList != null) {
				TIntHashSet peptideIds = new TIntHashSet();
				for (ExtendedIdentifiedProtein protein : proteinList) {

					final List<ExtendedIdentifiedPeptide> peptides = protein.getPeptides();
					if (peptides != null) {
						for (ExtendedIdentifiedPeptide extendedIdentifiedPeptide : peptides) {
							if (!peptideIds.contains(extendedIdentifiedPeptide.getId())) {
								this.peptides.add(extendedIdentifiedPeptide);
								peptideIds.add(extendedIdentifiedPeptide.getId());
							}
						}
					}

				}
			}
		}
		return peptides;
	}

	public List<ExtendedIdentifiedPeptide> getPeptides(String scoreName) {

		if (peptideListByScoreNames == null || peptideListByScoreNames.isEmpty()) {
			List<ExtendedIdentifiedPeptide> peptides = getPeptides();
			for (ExtendedIdentifiedPeptide peptide : peptides) {
				if (peptide.getScore(scoreName) != null) {
					if (peptideListByScoreNames.containsKey(scoreName)) {
						peptideListByScoreNames.get(scoreName).add(peptide);
					} else {
						List<ExtendedIdentifiedPeptide> list = new ArrayList<ExtendedIdentifiedPeptide>();
						list.add(peptide);
						peptideListByScoreNames.put(scoreName, list);
					}
				}
			}
		} else {
			if (peptideListByScoreNames.containsKey(scoreName))
				return peptideListByScoreNames.get(scoreName);
		}
		return null;
	}

	public boolean isDecoy() {

		if (proteinList != null && !proteinList.isEmpty()) {
			for (ExtendedIdentifiedProtein protein : proteinList) {
				if (protein.isDecoy())
					return true;
			}
		}

		return false;
	}

	public void setDecoy(boolean b) {

		for (ExtendedIdentifiedProtein protein : proteinList) {
			protein.setDecoy(b);
		}

	}

	/**
	 * Gets the different protein databases in which the
	 * {@link IdentificationItem} were searched
	 * 
	 * @return
	 */
	public List<Database> getProteinDatabases() {
		List<Database> ret = new ArrayList<Database>();
		for (ExtendedIdentifiedProtein protein : proteinList) {

			Set<Database> databases = protein.getDatabases();
			if (databases != null) {
				for (Database database : databases) {
					boolean found = false;
					for (Database selectedDatabase : ret) {
						String selectedDatabaseName = selectedDatabase.getName();
						if (selectedDatabase != null)
							if (selectedDatabaseName.equals(database.getName())) {
								String selectedDatabaseVersion = selectedDatabase.getNumVersion();
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
	public List<Software> getSoftwares() {
		List<Software> ret = new ArrayList<Software>();
		for (ExtendedIdentifiedProtein protein : this.proteinList) {
			Set<Software> softwares = protein.getSoftwares();
			if (softwares != null) {
				for (Software software : softwares) {
					boolean found = false;
					for (Software selectedSoftware : ret) {
						String selectedDatabaseName = selectedSoftware.getName();
						if (selectedSoftware != null)
							if (selectedDatabaseName.equals(software.getName())) {
								String selectedSoftwareVersion = selectedSoftware.getVersion();
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
		Set<String> ret = new THashSet<String>();
		if (proteinList != null) {
			for (ExtendedIdentifiedProtein protein : proteinList) {
				List<String> scoreNames = protein.getScoreNames();
				for (String scoreName : scoreNames) {
					if (!ret.contains(scoreName))
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
		if (proteinList != null)
			for (ExtendedIdentifiedProtein protein : proteinList) {
				Float score = protein.getScore(scoreName);
				if (score != null)
					values.add(score);
			}
		return values;
	}

	public void setFDRFilter(FDRFilter fdrFilter) {
		if (proteinList != null)
			for (ExtendedIdentifiedProtein protein : proteinList) {
				for (ExtendedIdentifiedPeptide peptide : protein.getPeptides()) {
					peptide.setFDRFilter(fdrFilter);
				}
			}
	}
}
