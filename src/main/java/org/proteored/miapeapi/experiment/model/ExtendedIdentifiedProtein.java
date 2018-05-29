package org.proteored.miapeapi.experiment.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.proteored.miapeapi.experiment.model.datamanager.StaticPeptideStorage;
import org.proteored.miapeapi.experiment.model.datamanager.StaticProteinStorage;
import org.proteored.miapeapi.experiment.model.filters.FDRFilter;
import org.proteored.miapeapi.experiment.model.grouping.ProteinEvidence;
import org.proteored.miapeapi.experiment.model.sort.SorterUtil;
import org.proteored.miapeapi.experiment.model.sort.SortingManager;
import org.proteored.miapeapi.experiment.model.sort.SortingParameters;
import org.proteored.miapeapi.interfaces.Software;
import org.proteored.miapeapi.interfaces.msi.Database;
import org.proteored.miapeapi.interfaces.msi.IdentifiedPeptide;
import org.proteored.miapeapi.interfaces.msi.IdentifiedProtein;
import org.proteored.miapeapi.interfaces.msi.InputParameter;
import org.proteored.miapeapi.interfaces.msi.MiapeMSIDocument;
import org.proteored.miapeapi.interfaces.msi.ProteinScore;

import com.compomics.util.protein.AASequenceImpl;
import com.compomics.util.protein.Protein;

import gnu.trove.list.array.TIntArrayList;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.set.hash.THashSet;

public class ExtendedIdentifiedProtein extends IdentificationItem implements IdentifiedProtein {
	private static Logger log = Logger.getLogger("log4j.logger.org.proteored");

	private String replicateName;
	private String experimentName;
	private List<ExtendedIdentifiedPeptide> peptides = new ArrayList<ExtendedIdentifiedPeptide>();
	private final Integer miapeMSReference;
	private final String miapeMSIName;
	protected ProteinGroup group;
	protected ProteinEvidence evidence;
	private String sequence;

	private boolean isDecoy;

	private SortingParameters sortingParameters;

	private Double proteinMass;
	// private static int staticIdentifier = 0;
	// private final int id = ++staticIdentifier;

	private final Set<Database> databases;

	private final Set<Software> softwares;

	private final TIntArrayList identifiedPeptideIDs = new TIntArrayList();

	private final TIntObjectHashMap<String> peptideSequencesByID = new TIntObjectHashMap<String>();

	private final int id;

	private final String parsedAccession;

	private final String description;

	private final Set<ProteinScore> scores;

	private final String peptideNumber;

	private final String coverage;

	private final String peaksMatchedNumber;

	private final String unmatchedSignals;

	private final String additionalInformation;

	private final Boolean validationStatus;

	private final String validationType;

	private final String validationValue;

	private Set<ExtendedIdentifiedPeptide> peptideSet;

	public ExtendedIdentifiedProtein(Replicate replicate, IdentifiedProtein identifiedProtein,
			MiapeMSIDocument miapeMSI) {
		this(replicate, identifiedProtein, miapeMSI, ProteinEvidence.NONCONCLUSIVE);
	}

	public ExtendedIdentifiedProtein(Replicate replicate, IdentifiedProtein identifiedProtein,
			MiapeMSIDocument miapeMSI, ProteinEvidence pe) {
		this(null, null, identifiedProtein, miapeMSI, pe);

		if (replicate != null) {
			replicateName = replicate.getName().trim();
			experimentName = replicate.getExperimentName().trim();
		}
	}

	public ExtendedIdentifiedProtein(String replicateName, String experimentName, IdentifiedProtein identifiedProtein,
			MiapeMSIDocument miapeMSI) {

		this(replicateName, experimentName, identifiedProtein, miapeMSI, ProteinEvidence.NONCONCLUSIVE);
	}

	public ExtendedIdentifiedProtein(String replicateName, String experimentName, IdentifiedProtein protein,
			MiapeMSIDocument miapeMSI, ProteinEvidence pe) {

		this.replicateName = replicateName;
		this.experimentName = experimentName;

		if (miapeMSI != null)
			miapeMSReference = miapeMSI.getMSDocumentReference();
		else
			miapeMSReference = -1;
		miapeMSIName = miapeMSI.getName();
		evidence = pe;
		group = null;
		// databases
		databases = new THashSet<Database>();
		final Set<InputParameter> inputParameters = miapeMSI.getInputParameters();
		if (inputParameters != null) {
			for (final InputParameter inputParameter : inputParameters) {
				final Set<Database> databases2 = inputParameter.getDatabases();
				if (databases2 != null) {
					for (final Database database : databases2) {
						boolean found = false;
						for (final Database selectedDatabase : databases) {
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
							databases.add(database);
					}
				}
			}
		}
		// softwares
		softwares = new THashSet<Software>();
		final Set<Software> softwares2 = miapeMSI.getSoftwares();
		if (softwares2 != null) {
			for (final Software software : softwares2) {
				boolean found = false;
				for (final Software selectedSoftware : softwares) {
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
					softwares.add(software);
			}
		}
		final List<Integer> ids = protein.getIdentifiedPeptides().stream().map(p -> p.getId())
				.collect(Collectors.toList());
		identifiedPeptideIDs.addAll(ids);
		protein.getIdentifiedPeptides().stream()
				.forEach(pep -> peptideSequencesByID.put(pep.getId(), pep.getSequence()));
		id = protein.getId();

		final String accession = protein.getAccession();
		IdentifierParser.setRemove_acc_version(false);
		parsedAccession = IdentifierParser.parseACC(accession);
		description = protein.getDescription();
		scores = new THashSet<ProteinScore>();
		scores.addAll(protein.getScores());
		peptideNumber = protein.getPeptideNumber();
		coverage = protein.getCoverage();
		peaksMatchedNumber = protein.getPeaksMatchedNumber();
		unmatchedSignals = protein.getUnmatchedSignals();
		additionalInformation = protein.getAdditionalInformation();
		validationStatus = protein.getValidationStatus();
		validationType = protein.getValidationType();
		validationValue = protein.getValidationValue();
	}

	// private void processProtein() {
	// final List<IdentifiedPeptide> identifiedPeptides =
	// protein.getIdentifiedPeptides();
	// TIntHashSet peptideIDs = new TIntHashSet();
	// if (identifiedPeptides != null) {
	// peptides = new ArrayList<ExtendedIdentifiedPeptide>();
	// for (IdentifiedPeptide peptide : identifiedPeptides) {
	// if (!peptideIDs.contains(peptide.getId())) {
	// peptides.add(new ExtendedIdentifiedPeptide(replicateName, experimentName,
	// peptide, miapeMSI, false));
	// peptideIDs.add(peptide.getId());
	// } else {
	// // log.warn("It can not be possible!");
	// }
	// }
	// }
	// }

	public String getExperimentName() {
		return experimentName;
	}

	public String getReplicateName() {
		return replicateName;
	}

	@Override
	public String toString() {
		return getAccession() + "-" + getEvidence() + ",(" + getPeptides().size() + ")";
		// String accession = this.getAccession();
		// List<ExtendedIdentifiedPeptide> peptides2 = this.getPeptides();
		// String peptideString = "";
		// for (ExtendedIdentifiedPeptide extendedIdentifiedPeptide : peptides2)
		// {
		// if (!"".equals(peptideString))
		// peptideString += "\n";
		// peptideString += "\t" + extendedIdentifiedPeptide.getSequence();
		// }
		// return accession + "\n" + peptideString;
	}

	@Override
	public boolean equals(Object obj) {
		// if (!(obj instanceof IdentifiedProtein))
		return super.equals(obj);
		// else {
		// IdentifiedProtein protein = (IdentifiedProtein) obj;
		// return protein.getAccession().equals(getAccession());
		// }
	}

	@Override
	public int getId() {

		return id;
	}

	@Override
	public String getAccession() {
		return getParsedAccession();
	}

	public String getParsedAccession() {
		return parsedAccession;

	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public Set<ProteinScore> getScores() {
		return scores;
	}

	@Override
	public String getPeptideNumber() {
		return peptideNumber;
	}

	@Override
	public String getCoverage() {
		return coverage;
	}

	@Override
	public String getPeaksMatchedNumber() {
		return peaksMatchedNumber;
	}

	@Override
	public String getUnmatchedSignals() {

		return unmatchedSignals;
	}

	@Override
	public String getAdditionalInformation() {
		return additionalInformation;
	}

	@Override
	public Boolean getValidationStatus() {
		return validationStatus;
	}

	@Override
	public String getValidationType() {

		return validationType;
	}

	@Override
	public String getValidationValue() {

		return validationValue;
	}

	@Override
	public List<IdentifiedPeptide> getIdentifiedPeptides() {
		// return protein.getIdentifiedPeptides();

		throw new UnsupportedOperationException();

	}

	public TIntArrayList getIdentifiedPeptideIDs() {
		return identifiedPeptideIDs;
	}

	public List<ExtendedIdentifiedPeptide> getPeptides() {
		return peptides;

	}

	public void addPeptide(ExtendedIdentifiedPeptide peptide) {
		if (peptide == null) {
			return;
		}
		if (peptides == null) {
			peptides = new ArrayList<ExtendedIdentifiedPeptide>();
			peptideSet = new THashSet<ExtendedIdentifiedPeptide>();
		}

		for (final ExtendedIdentifiedPeptide pep : peptides) {
			if (pep.getId() == peptide.getId())
				return;
		}

		peptides.add(peptide);
	}

	public void resetPeptides(String idSetFullName) {

		final TIntArrayList identifiedPeptideIDs = getIdentifiedPeptideIDs();
		if (peptides == null)
			peptides = new ArrayList<ExtendedIdentifiedPeptide>();
		if (identifiedPeptideIDs != null) {
			peptides.clear();
			for (final int peptideID : identifiedPeptideIDs.toArray()) {
				final ExtendedIdentifiedPeptide peptide2 = StaticPeptideStorage.getPeptide(miapeMSIName, idSetFullName,
						peptideID);
				if (peptide2 != null) {
					// add it to the protein
					peptides.add(peptide2);
					peptide2.setDecoy(false, false);

					// check that it has all the proteins that should have
					final List<Integer> identifiedProteinIDs = peptide2.getIdentifiedProteinIDs();
					for (final Integer identifiedProteinID : identifiedProteinIDs) {
						final ExtendedIdentifiedProtein protein2 = StaticProteinStorage.getProtein(miapeMSIName,
								idSetFullName, identifiedProteinID);
						if (protein2 != null) {
							peptide2.addProtein(protein2);
						}

					}

				}
			}
		}
	}

	@Override
	public Float getScore(String scoreName) {
		if (scoreName != null) {
			final Set<ProteinScore> scores = getScores();
			if (scores != null && !scores.isEmpty()) {
				for (final ProteinScore proteinScore : scores) {
					if (proteinScore.getName().equalsIgnoreCase(scoreName)) {
						try {
							return Float.valueOf(proteinScore.getValue());
						} catch (final NumberFormatException e) {
						}
					}
				}
			}
		}
		return null;
	}

	@Override
	public boolean isDecoy(FDRFilter filter) {
		if (filter != null) {
			return filter.isDecoy(getAccession());
		}
		return false;
	}

	@Override
	public boolean isDecoy() {
		return isDecoy;
	}

	@Override
	public void setDecoy(boolean isDecoy) {
		this.isDecoy = isDecoy;
		for (final ExtendedIdentifiedPeptide peptide : getPeptides()) {
			peptide.setDecoy(isDecoy);
		}
		if (getGroup() != null)
			getGroup().setDecoy(false);
	}

	public void setDecoy(boolean isDecoy, boolean setPeptides) {
		this.isDecoy = isDecoy;
		if (setPeptides)
			for (final ExtendedIdentifiedPeptide peptide : getPeptides()) {
				peptide.setDecoy(isDecoy, setPeptides);
			}
		if (getGroup() != null)
			getGroup().setDecoy(isDecoy, false);
	}

	public void setDecoy(boolean isDecoy, boolean setPeptides, boolean setGroup) {
		this.isDecoy = isDecoy;
		if (setPeptides)
			for (final ExtendedIdentifiedPeptide peptide : getPeptides()) {
				peptide.setDecoy(isDecoy, setPeptides);
			}
		if (getGroup() != null && setGroup)
			getGroup().setDecoy(isDecoy, false);
	}

	@Override
	public Float getScore() {
		final SortingParameters sortingParameters = SortingManager.getInstance().getProteinSortingByProteinScore(this);
		if (sortingParameters == null)
			return null;
		return this.getScore(sortingParameters.getScoreName());
	}

	public ExtendedIdentifiedPeptide getBestPeptideByPeptideScore() {

		final List<ExtendedIdentifiedPeptide> peptides = getPeptides();
		if (peptides != null && !peptides.isEmpty()) {
			SorterUtil.sortPeptidesByBestPeptideScore(peptides, false);
			return peptides.get(0);
		}
		return null;

	}

	public ExtendedIdentifiedPeptide getBestPeptideByPeptideScore(String scoreName) {

		final List<ExtendedIdentifiedPeptide> peptides = getPeptides();
		if (peptides != null && !peptides.isEmpty()) {
			SorterUtil.sortPeptidesByPeptideScore(peptides, scoreName, false);
			return peptides.get(0);
		}
		return null;

	}

	public Float getBestPeptideScore() {

		final ExtendedIdentifiedPeptide bestPeptideByScore = this.getBestPeptideByPeptideScore();
		if (bestPeptideByScore != null) {
			return bestPeptideByScore.getScore();
		}
		return null;
	}

	public Float getBestPeptideScore(String scoreName) {

		final ExtendedIdentifiedPeptide bestPeptideByScore = this.getBestPeptideByPeptideScore(scoreName);
		if (bestPeptideByScore != null) {
			return bestPeptideByScore.getScore(scoreName);
		}
		return null;
	}

	@Override
	public Integer getMiapeMSReference() {
		return miapeMSReference;
	}

	/**
	 * Gets the MIAPE MSI document name in which the protein was reported
	 *
	 * @return
	 */
	@Override
	public String getMiapeMSIName() {
		return miapeMSIName;
	}

	@Override
	public List<String> getScoreNames() {
		final List<String> ret = new ArrayList<String>();
		final Set<ProteinScore> scores = getScores();
		if (scores != null)
			for (final ProteinScore proteinScore : scores) {
				final String name = proteinScore.getName();
				if (!ret.contains(name))
					ret.add(name);
			}
		return ret;
	}

	public void setProteinSequence(String proteinSequence) {
		sequence = proteinSequence;

	}

	public String getProteinSequence() {
		return sequence;
	}

	public void setEvidence(ProteinEvidence evidence) {
		this.evidence = evidence;

	}

	public ProteinEvidence getEvidence() {
		return evidence;
	}

	public ProteinGroup getGroup() {
		return group;
	}

	public void setGroup(ProteinGroup group) {
		this.group = group;

	}

	public SortingParameters getSortingParameters() {
		return sortingParameters;
	}

	public void setSortingParameters(SortingParameters sorting) {
		sortingParameters = sorting;
	}

	public void clearPeptides() {
		peptides.clear();
	}

	public Double getProteinMass() {
		if (proteinMass == null) {
			try {
				final String proteinSequence = getProteinSequence();
				if (proteinSequence != null) {
					final Protein prot = new Protein(new AASequenceImpl(proteinSequence));
					proteinMass = prot.getMass();
				}
			} catch (final Exception e) {

			}
		}
		return proteinMass;
	}

	public void setProteinMass(Double proteinMass) {
		this.proteinMass = proteinMass;
	}

	public Set<Database> getDatabases() {
		return databases;
	}

	public Set<Software> getSoftwares() {
		return softwares;
	}

	public String getPeptideSequenceByID(int peptideID) {
		return peptideSequencesByID.get(peptideID);
	}

}
