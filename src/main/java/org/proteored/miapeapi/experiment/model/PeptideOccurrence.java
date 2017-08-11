package org.proteored.miapeapi.experiment.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.proteored.miapeapi.experiment.model.filters.FDRFilter;
import org.proteored.miapeapi.experiment.model.interfaces.Occurrence;
import org.proteored.miapeapi.experiment.model.interfaces.PeptideContainer;
import org.proteored.miapeapi.experiment.model.sort.SorterUtil;
import org.proteored.miapeapi.experiment.model.sort.SortingParameters;
import org.proteored.miapeapi.interfaces.Software;
import org.proteored.miapeapi.interfaces.msi.Database;
import org.proteored.miapeapi.interfaces.msi.InputParameter;
import org.proteored.miapeapi.interfaces.msi.MiapeMSIDocument;

import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;

public class PeptideOccurrence implements Occurrence<ExtendedIdentifiedPeptide>, PeptideContainer {

	private static Logger log = Logger.getLogger("log4j.logger.org.proteored");

	private final List<ExtendedIdentifiedPeptide> peptideList = new ArrayList<ExtendedIdentifiedPeptide>();
	private final String key;
	private String name;

	private ExtendedIdentifiedPeptide bestPeptide;

	private final Map<String, List<ExtendedIdentifiedPeptide>> peptideListByScoreNames = new THashMap<String, List<ExtendedIdentifiedPeptide>>();

	public PeptideOccurrence(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}

	@Override
	public ExtendedIdentifiedPeptide getFirstOccurrence() {
		return peptideList.get(0);
	}

	@Override
	public String toString() {
		return getKey() + "(" + peptideList.size() + ")";
	}

	@Override
	public void addOccurrence(ExtendedIdentifiedPeptide peptide) {
		bestPeptide = null;
		name = peptide.getSequence();

		// if (!peptideList.contains(peptide))
		peptideList.add(peptide);
		List<String> scoreNames = peptide.getScoreNames();
		for (String scoreName : scoreNames) {
			if (peptideListByScoreNames.containsKey(scoreName)) {
				peptideListByScoreNames.get(scoreName).add(peptide);
			} else {
				List<ExtendedIdentifiedPeptide> list = new ArrayList<ExtendedIdentifiedPeptide>();
				list.add(peptide);
				peptideListByScoreNames.put(scoreName, list);
			}
		}

	}

	@Override
	public List<ExtendedIdentifiedPeptide> getItemList() {
		return peptideList;
	}

	@Override
	public List<ExtendedIdentifiedPeptide> getPeptides() {
		return getItemList();
	}

	public Set<ExtendedIdentifiedProtein> getProteinList() {
		Set<ExtendedIdentifiedProtein> ret = new THashSet<ExtendedIdentifiedProtein>();
		for (ExtendedIdentifiedPeptide peptide : peptideList) {
			ret.addAll(peptide.getProteins());
		}
		return ret;
	}

	public List<ExtendedIdentifiedPeptide> getPeptides(String scoreName) {
		return peptideListByScoreNames.get(scoreName);
	}

	public String getName() {
		if (name != null && !"".equals(name))
			return name;
		return key;
	}

	/**
	 * Gets the best score of the {@link ExtendedIdentifiedPeptide}s accordnig
	 * to a {@link SortingParameters}
	 *
	 * @param sorting
	 * @return
	 */
	@Override
	public Float getBestPeptideScore() {

		ExtendedIdentifiedPeptide peptide = this.getBestPeptide();
		if (peptide != null) {
			return peptide.getScore();
		}
		return null;
	}

	@Override
	public Float getBestPeptideScore(String scoreName) {

		ExtendedIdentifiedPeptide peptide = this.getBestPeptide(scoreName);
		if (peptide != null) {
			return peptide.getScore(scoreName);
		}
		return null;
	}

	/**
	 * Gets the best {@link ExtendedIdentifiedPeptide} according to the score
	 * defined in a {@link SortingParameters}
	 *
	 * @param sorting
	 * @return
	 */
	@Override
	public ExtendedIdentifiedPeptide getBestPeptide() {
		if (bestPeptide != null)
			return bestPeptide;

		List<ExtendedIdentifiedPeptide> peptides = getItemList();
		SorterUtil.sortPeptidesByBestPeptideScore(peptides, false);
		bestPeptide = peptides.get(0);
		return bestPeptide;
	}

	@Override
	public ExtendedIdentifiedPeptide getBestPeptide(String scoreName) {

		List<ExtendedIdentifiedPeptide> peptides = getPeptides(scoreName);
		if (peptides == null || peptides.isEmpty())
			return null;
		SorterUtil.sortPeptidesByPeptideScore(peptides, scoreName, false);
		return peptides.get(0);
	}

	public void setDecoy(boolean b) {
		for (ExtendedIdentifiedPeptide peptide : getItemList()) {
			peptide.setDecoy(b);
		}
	}

	public boolean isDecoy() {
		if (peptideList != null && !peptideList.isEmpty()) {
			for (ExtendedIdentifiedPeptide peptide : peptideList) {
				if (peptide.isDecoy())
					return true;
			}
		}
		return false;
	}

	public boolean isDecoy(FDRFilter fdrFilter) {
		if (peptideList != null && !peptideList.isEmpty()) {
			for (ExtendedIdentifiedPeptide peptide : peptideList) {
				if (peptide.isDecoy(fdrFilter))
					return true;
			}
		}
		return false;
	}

	public List<MiapeMSIDocument> getMiapeMSIs() {
		List<MiapeMSIDocument> ret = new ArrayList<MiapeMSIDocument>();
		List<Integer> ids = new ArrayList<Integer>();
		for (ExtendedIdentifiedPeptide identificationItem : peptideList) {
			MiapeMSIDocument miapeMSI = identificationItem.getMiapeMSI();
			if (!ids.contains(miapeMSI.getId())) {
				ret.add(miapeMSI);
				ids.add(miapeMSI.getId());
			}
		}
		return ret;
	}

	/**
	 * Gets the different protein databases in which the
	 * {@link ExtendedIdentifiedPeptide} were searched
	 *
	 * @return
	 */
	public List<Database> getProteinDatabases() {
		List<Database> ret = new ArrayList<Database>();
		List<MiapeMSIDocument> miapeMSIs = getMiapeMSIs();
		for (MiapeMSIDocument miapeMSIDocument : miapeMSIs) {
			Set<InputParameter> inputParameters = miapeMSIDocument.getInputParameters();
			if (inputParameters != null) {
				for (InputParameter inputParameter : inputParameters) {
					Set<Database> databases = inputParameter.getDatabases();
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
		if (peptideList != null) {
			for (ExtendedIdentifiedPeptide peptide : peptideList) {
				List<String> scoreNames = peptide.getScoreNames();
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
		if (peptideList != null)
			for (ExtendedIdentifiedPeptide peptide : peptideList) {
				Float score = peptide.getScore(scoreName);
				if (score != null)
					values.add(score);
			}
		return values;
	}

	public void setFDRFilter(FDRFilter fdrFilter) {
		if (peptideList != null)
			for (ExtendedIdentifiedPeptide item : peptideList) {
				item.setFDRFilter(fdrFilter);
			}
	}

	public Float getPeptideLocalFDR() {
		ExtendedIdentifiedPeptide bestPeptide2 = this.getBestPeptide();
		if (bestPeptide2 != null)
			return bestPeptide2.getPeptideLocalFDR();
		return null;
	}

	public void setPeptideLocalFDR(Float peptideLocalFDR) {
		for (ExtendedIdentifiedPeptide peptide : this.getPeptides()) {
			peptide.setPeptideLocalFDR(peptideLocalFDR);
		}
	}

	public Float getPeptideQValue() {
		ExtendedIdentifiedPeptide bestPeptide2 = this.getBestPeptide();
		if (bestPeptide2 != null)
			return bestPeptide2.getPeptideQValue();
		return null;
	}

	public void setPeptideQValue(Float peptideLocalFDR) {
		for (ExtendedIdentifiedPeptide peptide : this.getPeptides()) {
			peptide.setPeptideQValue(peptideLocalFDR);
		}
	}
}
