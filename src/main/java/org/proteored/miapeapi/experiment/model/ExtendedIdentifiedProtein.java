package org.proteored.miapeapi.experiment.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.proteored.miapeapi.experiment.model.datamanager.StaticPeptideStorage;
import org.proteored.miapeapi.experiment.model.filters.FDRFilter;
import org.proteored.miapeapi.experiment.model.grouping.ProteinEvidence;
import org.proteored.miapeapi.experiment.model.sort.SorterUtil;
import org.proteored.miapeapi.experiment.model.sort.SortingManager;
import org.proteored.miapeapi.experiment.model.sort.SortingParameters;
import org.proteored.miapeapi.interfaces.msi.IdentifiedPeptide;
import org.proteored.miapeapi.interfaces.msi.IdentifiedProtein;
import org.proteored.miapeapi.interfaces.msi.MiapeMSIDocument;
import org.proteored.miapeapi.interfaces.msi.ProteinScore;

import com.compomics.util.protein.AASequenceImpl;
import com.compomics.util.protein.Protein;

public class ExtendedIdentifiedProtein extends IdentificationItem implements IdentifiedProtein {
	private static Logger log = Logger.getLogger("log4j.logger.org.proteored");

	private String replicateName;
	private String experimentName;
	private final IdentifiedProtein protein;
	private List<ExtendedIdentifiedPeptide> peptides = new ArrayList<ExtendedIdentifiedPeptide>();
	private final Integer miapeMSReference;
	private final MiapeMSIDocument miapeMSI;
	protected ProteinGroup group;
	protected ProteinEvidence evidence;
	private String sequence;

	private boolean isDecoy;

	private SortingParameters sortingParameters;

	private Double proteinMass;

	public ExtendedIdentifiedProtein(Replicate replicate, IdentifiedProtein identifiedProtein,
			MiapeMSIDocument miapeMSI) {
		this(replicate, identifiedProtein, miapeMSI, ProteinEvidence.NONCONCLUSIVE);
	}

	public ExtendedIdentifiedProtein(ExtendedIdentifiedProtein p) {
		evidence = p.evidence;
		experimentName = p.experimentName;
		group = p.group;
		isDecoy = p.isDecoy;
		miapeMSI = p.miapeMSI;
		miapeMSReference = p.miapeMSReference;
		peptides = p.peptides;
		protein = p.protein;
		replicateName = p.replicateName;
		sequence = p.sequence;
		sortingParameters = p.sortingParameters;
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

	public ExtendedIdentifiedProtein(String replicateName, String experimentName, IdentifiedProtein identifiedProtein,
			MiapeMSIDocument miapeMSI, ProteinEvidence pe) {

		this.replicateName = replicateName;
		this.experimentName = experimentName;
		protein = identifiedProtein;
		if (miapeMSI != null)
			miapeMSReference = miapeMSI.getMSDocumentReference();
		else
			miapeMSReference = -1;
		this.miapeMSI = miapeMSI;
		evidence = pe;
		group = null;
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

	public IdentifiedProtein getProtein() {
		return protein;
	}

	public String getExperimentName() {
		return experimentName;
	}

	public String getReplicateName() {
		return replicateName;
	}

	@Override
	public String toString() {
		return getAccession() + "(" + getPeptides().size() + ")";
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
		return protein.getId();
	}

	@Override
	public String getAccession() {
		return getParsedAccession();
	}

	public String getParsedAccession() {
		String accession = protein.getAccession();
		IdentifierParser.setRemove_acc_version(false);
		accession = IdentifierParser.parseACC(accession);

		return accession;
	}

	@Override
	public String getDescription() {
		return protein.getDescription();
	}

	@Override
	public Set<ProteinScore> getScores() {
		return protein.getScores();
	}

	@Override
	public String getPeptideNumber() {
		return protein.getPeptideNumber();
	}

	@Override
	public String getCoverage() {
		return protein.getCoverage();
	}

	@Override
	public String getPeaksMatchedNumber() {
		return protein.getPeaksMatchedNumber();
	}

	@Override
	public String getUnmatchedSignals() {
		return protein.getUnmatchedSignals();
	}

	@Override
	public String getAdditionalInformation() {
		return protein.getAdditionalInformation();
	}

	@Override
	public Boolean getValidationStatus() {
		return protein.getValidationStatus();
	}

	@Override
	public String getValidationType() {
		return protein.getValidationType();
	}

	@Override
	public String getValidationValue() {
		return protein.getValidationValue();
	}

	@Override
	public List<IdentifiedPeptide> getIdentifiedPeptides() {
		return protein.getIdentifiedPeptides();

		// List<IdentifiedPeptide> ret = new ArrayList<IdentifiedPeptide>();
		// List<ExtendedIdentifiedPeptide> filteredPeptides = getPeptides();
		// List<Integer> filteredPeptidesIds = new ArrayList<Integer>();
		// for (ExtendedIdentifiedPeptide extendedIdentifiedPeptide :
		// filteredPeptides) {
		// filteredPeptidesIds.add(extendedIdentifiedPeptide.getId());
		// }
		// for (IdentifiedPeptide peptide : identifiedPeptides) {
		// if (filteredPeptidesIds.contains(peptide.getId()))
		// ret.add(peptide);
		// }
		// return ret;

	}

	public List<ExtendedIdentifiedPeptide> getPeptides() {
		return peptides;

	}

	public void addPeptide(ExtendedIdentifiedPeptide peptide) {
		if (peptides == null)
			peptides = new ArrayList<ExtendedIdentifiedPeptide>();

		for (ExtendedIdentifiedPeptide pep : peptides) {
			if (pep.getId() == peptide.getId())
				return;
		}

		peptides.add(peptide);
	}

	public void resetPeptides() {

		final List<IdentifiedPeptide> identifiedPeptides = getIdentifiedPeptides();
		if (peptides == null)
			peptides = new ArrayList<ExtendedIdentifiedPeptide>();
		if (identifiedPeptides != null) {
			peptides.clear();
			for (IdentifiedPeptide peptide : identifiedPeptides) {
				ExtendedIdentifiedPeptide peptide2 = StaticPeptideStorage.getPeptide(miapeMSI, peptide.getId());
				if (peptide2 != null) {
					peptides.add(peptide2);
					peptide2.setDecoy(false, false);
				}
			}
		}
	}

	@Override
	public Float getScore(String scoreName) {
		if (scoreName != null) {
			final Set<ProteinScore> scores = getScores();
			if (scores != null && !scores.isEmpty()) {
				for (ProteinScore proteinScore : scores) {
					if (proteinScore.getName().equalsIgnoreCase(scoreName)) {
						try {
							return Float.valueOf(proteinScore.getValue());
						} catch (NumberFormatException e) {
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
		for (ExtendedIdentifiedPeptide peptide : getPeptides()) {
			peptide.setDecoy(isDecoy);
		}
		if (getGroup() != null)
			getGroup().setDecoy(false);
	}

	public void setDecoy(boolean isDecoy, boolean setPeptides) {
		this.isDecoy = isDecoy;
		if (setPeptides)
			for (ExtendedIdentifiedPeptide peptide : getPeptides()) {
				peptide.setDecoy(isDecoy, setPeptides);
			}
		if (getGroup() != null)
			getGroup().setDecoy(isDecoy, false);
	}

	public void setDecoy(boolean isDecoy, boolean setPeptides, boolean setGroup) {
		this.isDecoy = isDecoy;
		if (setPeptides)
			for (ExtendedIdentifiedPeptide peptide : getPeptides()) {
				peptide.setDecoy(isDecoy, setPeptides);
			}
		if (getGroup() != null && setGroup)
			getGroup().setDecoy(isDecoy, false);
	}

	@Override
	public Float getScore() {
		SortingParameters sortingParameters = SortingManager.getInstance().getProteinSortingByProteinScore(this);
		if (sortingParameters == null)
			return null;
		return this.getScore(sortingParameters.getScoreName());
	}

	public ExtendedIdentifiedPeptide getBestPeptideByPeptideScore() {

		List<ExtendedIdentifiedPeptide> peptides = getPeptides();
		if (peptides != null && !peptides.isEmpty()) {
			SorterUtil.sortPeptidesByBestPeptideScore(peptides, false);
			return peptides.get(0);
		}
		return null;

	}

	public ExtendedIdentifiedPeptide getBestPeptideByPeptideScore(String scoreName) {

		List<ExtendedIdentifiedPeptide> peptides = getPeptides();
		if (peptides != null && !peptides.isEmpty()) {
			SorterUtil.sortPeptidesByPeptideScore(peptides, scoreName, false);
			return peptides.get(0);
		}
		return null;

	}

	public Float getBestPeptideScore() {

		ExtendedIdentifiedPeptide bestPeptideByScore = this.getBestPeptideByPeptideScore();
		if (bestPeptideByScore != null) {
			return bestPeptideByScore.getScore();
		}
		return null;
	}

	public Float getBestPeptideScore(String scoreName) {

		ExtendedIdentifiedPeptide bestPeptideByScore = this.getBestPeptideByPeptideScore(scoreName);
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
	 * Gets the MIAPE MSI document in which the protein was reported
	 *
	 * @return
	 */
	@Override
	public MiapeMSIDocument getMiapeMSI() {
		return miapeMSI;
	}

	@Override
	public List<String> getScoreNames() {
		List<String> ret = new ArrayList<String>();
		Set<ProteinScore> scores = getScores();
		if (scores != null)
			for (ProteinScore proteinScore : scores) {
				String name = proteinScore.getName();
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
				String proteinSequence = getProteinSequence();
				if (proteinSequence != null) {
					Protein prot = new Protein(new AASequenceImpl(proteinSequence));
					proteinMass = prot.getMass();
				}
			} catch (Exception e) {

			}
		}
		return proteinMass;
	}

	public void setProteinMass(Double proteinMass) {
		this.proteinMass = proteinMass;
	}

}
