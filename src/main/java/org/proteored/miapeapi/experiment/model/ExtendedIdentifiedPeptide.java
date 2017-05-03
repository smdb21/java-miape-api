package org.proteored.miapeapi.experiment.model;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.proteored.miapeapi.experiment.model.filters.FDRFilter;
import org.proteored.miapeapi.experiment.model.grouping.PeptideRelation;
import org.proteored.miapeapi.experiment.model.sort.SorterUtil;
import org.proteored.miapeapi.experiment.model.sort.SortingManager;
import org.proteored.miapeapi.experiment.model.sort.SortingParameters;
import org.proteored.miapeapi.interfaces.msi.IdentifiedPeptide;
import org.proteored.miapeapi.interfaces.msi.IdentifiedProtein;
import org.proteored.miapeapi.interfaces.msi.InputData;
import org.proteored.miapeapi.interfaces.msi.MiapeMSIDocument;
import org.proteored.miapeapi.interfaces.msi.PeptideModification;
import org.proteored.miapeapi.interfaces.msi.PeptideScore;
import org.proteored.miapeapi.util.ModificationMapping;
import org.proteored.miapeapi.xml.util.MiapeXmlUtil;

import com.compomics.util.experiment.biology.AminoAcid;
import com.compomics.util.experiment.biology.Atom;

import uk.ac.ebi.pridemod.slimmod.model.SlimModCollection;

public class ExtendedIdentifiedPeptide extends IdentificationItem implements IdentifiedPeptide {
	private static HashMap<String, List<String>> sequenceConversion = new HashMap<String, List<String>>();

	private final IdentifiedPeptide peptide;
	private final String modificationString;
	private boolean decoy;
	private String expMass;
	private String calcMass;
	private String errorMass;
	private String experimentName;
	private String replicateName;
	private FDRFilter fdrFilter;
	private Float psmLocalFDR;
	private Float peptideLocalFDR;
	private Float psmQValue;
	private Float peptideQValue;
	private int numMissedClieavages;

	private final Integer miapeMSReference;
	private final MiapeMSIDocument miapeMSI;
	private List<ExtendedIdentifiedProtein> proteins = new ArrayList<ExtendedIdentifiedProtein>();
	private static Logger log = Logger.getLogger("log4j.logger.org.proteored");
	protected PeptideRelation relation;

	private SortingParameters sortingParameters;

	private SlimModCollection preferredModifications;

	// private boolean filtered = false;

	public ExtendedIdentifiedPeptide(Replicate replicate, IdentifiedPeptide peptide, MiapeMSIDocument miapeMSI) {
		this(replicate, peptide, miapeMSI, PeptideRelation.NONDISCRIMINATING);

	}

	public ExtendedIdentifiedPeptide(Replicate replicate, IdentifiedPeptide peptide, MiapeMSIDocument miapeMSI,
			PeptideRelation relation) {
		this(null, null, peptide, miapeMSI, relation);
		if (replicate != null) {
			replicateName = replicate.getName().trim();
			experimentName = replicate.getExperimentName().trim();
		}
	}

	public ExtendedIdentifiedPeptide(String replicateName, String experimentName, IdentifiedPeptide peptide,
			MiapeMSIDocument miapeMSI) {
		this(replicateName, experimentName, peptide, miapeMSI, PeptideRelation.NONDISCRIMINATING);
	}

	public ExtendedIdentifiedPeptide(String replicateName, String experimentName, IdentifiedPeptide peptide,
			MiapeMSIDocument miapeMSI, PeptideRelation relation) {

		this.replicateName = replicateName;
		this.experimentName = experimentName;

		this.peptide = peptide;
		modificationString = createModificationString();
		if (miapeMSI != null)
			miapeMSReference = miapeMSI.getMSDocumentReference();
		else
			miapeMSReference = -1;
		this.miapeMSI = miapeMSI;
		this.relation = relation;
		processPeptide();
	}

	public ExtendedIdentifiedPeptide(ExtendedIdentifiedPeptide p) {
		calcMass = p.calcMass;
		decoy = p.decoy;
		errorMass = p.errorMass;
		experimentName = p.experimentName;
		expMass = p.expMass;
		miapeMSI = p.miapeMSI;
		miapeMSReference = p.miapeMSReference;
		modificationString = p.modificationString;
		numMissedClieavages = p.numMissedClieavages;
		peptide = p.peptide;
		proteins = p.proteins;
		relation = p.relation;
		replicateName = p.replicateName;

	}

	public FDRFilter getFdrFilter() {
		return fdrFilter;
	}

	public void setFDRFilter(FDRFilter fdrFilter) {
		this.fdrFilter = fdrFilter;
	}

	private void processPeptide() {
		String string = getMassDesviation();

		if (string != null && string.contains("\n")) {
			final String[] lines = string.split("\n");
			for (String line : lines) {
				if (line.contains("=")) {
					final String[] pairNameValue = line.split("=");

					if (pairNameValue[0].startsWith(MiapeXmlUtil.EXPERIMENTAL_MZ))
						expMass = pairNameValue[1];
					if (pairNameValue[0].startsWith(MiapeXmlUtil.CALCULATED_MZ))
						calcMass = pairNameValue[1];
					if (pairNameValue[0].startsWith(MiapeXmlUtil.ERROR_MZ))
						errorMass = pairNameValue[1];
				}
			}
		} else {
			try {
				double errorMZ = Double.valueOf(string);
				errorMass = String.valueOf(errorMZ);

				int charge = Integer.valueOf(getCharge());
				double calcMzDouble = ModificationMapping.getAASequenceImpl(getSequence(), getModifications())
						.getMz(charge);
				calcMass = String.valueOf(calcMzDouble);
				double expMZDouble = calcMzDouble - errorMZ;
				expMass = String.valueOf(expMZDouble);
			} catch (NumberFormatException e) {

			}
		}
		if (errorMass == null && calcMass != null && expMass != null) {
			try {
				double calcMzDouble = Double.valueOf(calcMass);
				double expMZDouble = Double.valueOf(expMass);
				double errorMZ = calcMzDouble - expMZDouble;
				errorMass = String.valueOf(errorMZ);
			} catch (NumberFormatException e) {

			}
		}
		String seq = getSequence();
		int numMiss = 0;
		if (seq.contains("K")) {
			final String[] split = seq.split("K");
			numMiss = numMiss + split.length - 1;
		}
		if (seq.contains("R")) {
			final String[] split = seq.split("R");
			numMiss = numMiss + split.length - 1;
		}
		// log.info("The sequence " + seq + " has " + numMiss +
		// " misscleavages");

		numMissedClieavages = numMiss;

		// if (this.peptide.getIdentifiedProteins() != null) {
		// Set<Integer> proteinIDs = new HashSet<Integer>();
		// proteins = new ArrayList<ExtendedIdentifiedProtein>();
		// for (IdentifiedProtein protein :
		// this.peptide.getIdentifiedProteins()) {
		// if (!proteinIDs.contains(protein.getId())) {
		// proteins.add(new ExtendedIdentifiedProtein(replicateName,
		// experimentName,
		// protein, miapeMSI, false));
		// proteinIDs.add(protein.getId());
		// }
		// }
		// }

	}

	public PeptideRelation getRelation() {
		return relation;
	}

	public void setRelation(PeptideRelation relation) {
		this.relation = relation;
	}

	public String getExperimentName() {
		return experimentName;
	}

	public String getReplicateName() {
		return replicateName;
	}

	@Override
	public String toString() {
		return this.getModificationString();
	}

	private String createModificationString() {
		Set<PeptideModification> modificationSet = getModifications();
		if (modificationSet != null) {
			List<PeptideModification> modificationList = new ArrayList<PeptideModification>();
			for (PeptideModification peptideModification : modificationSet) {
				modificationList.add(peptideModification);
			}
			String sequence = getSequence();
			if (modificationList != null && !modificationList.isEmpty()) {

				// sort modifications by position
				Collections.sort(modificationList, new Comparator<PeptideModification>() {
					@Override
					public int compare(PeptideModification pepMod1, PeptideModification pepMod2) {
						return Integer.valueOf(pepMod1.getPosition()).compareTo(Integer.valueOf(pepMod2.getPosition()));
					}
				});
				log.debug("PEptide: " + sequence + " numMods=" + modificationList.size());
				// parse modifications
				List<String> splits = new ArrayList<String>();
				String temp = sequence;
				int previousPosition = 0;
				String after = "";
				for (PeptideModification peptideModification : modificationList) {
					final int position = peptideModification.getPosition();

					log.debug("modification: " + sequence + " pos:" + position);

					if (position > 0 && position <= sequence.length()) {
						String before = sequence.substring(previousPosition, position);
						splits.add(before);
						previousPosition = position;
						after = "";
						if (position + 1 <= sequence.length())
							after = sequence.substring(position);
						// N-terminal
					} else if (position == 0) {
						String before = "";
						splits.add(before);
						previousPosition = position;
						after = sequence;
						// C-TERMINAL
					} else if (position == sequence.length()) {
						String before = sequence;
						splits.add(before);
						previousPosition = position;
						after = "";
					}

				}
				splits.add(after);
				DecimalFormat format = new DecimalFormat("#.##");
				StringBuilder sb = new StringBuilder();
				int i = 0;
				for (i = 0; i < splits.size(); i++) {
					PeptideModification peptideModification = null;
					String subtitution = null;
					if (i < modificationList.size())
						peptideModification = modificationList.get(i);
					Double deltaMass = null;
					if (peptideModification != null) {
						if (peptideModification.getMonoDelta() != null)
							deltaMass = peptideModification.getMonoDelta();
						if (deltaMass == null && peptideModification.getAvgDelta() != null)
							deltaMass = peptideModification.getAvgDelta();
						if (deltaMass == null) {

							if (peptideModification.getReplacementResidue() != null
									&& !"".equals(peptideModification.getReplacementResidue()))
								subtitution = peptideModification.getReplacementResidue();
						}
					}
					sb.append(splits.get(i));
					String prefix = "";
					if (deltaMass != null) {
						if (deltaMass > 0)
							prefix = "+";
						String delta = "(" + prefix + format.format(deltaMass) + ")";
						sb.append(delta);
					}
					if (subtitution != null) {
						sb.append("(->" + subtitution + ")");
					}
				}
				if (!ExtendedIdentifiedPeptide.sequenceConversion.containsKey(getSequence())) {
					List<String> list = new ArrayList<String>();
					list.add(sb.toString());
					ExtendedIdentifiedPeptide.sequenceConversion.put(getSequence(), list);
				} else {

					final List<String> list = ExtendedIdentifiedPeptide.sequenceConversion.get(getSequence());
					if (list != null) {
						try {
							if (!list.contains(sb.toString()))
								list.add(sb.toString());
						} catch (ArrayIndexOutOfBoundsException e) {
							e.printStackTrace();
						}
					}
					// list.add(sb.toString());
					// ExtendedIdentifiedPeptide.sequenceConversion.remove(getSequence());
					// ExtendedIdentifiedPeptide.sequenceConversion.put(getSequence(),
					// list);
				}
				return sb.toString();
			} else {
				if (!ExtendedIdentifiedPeptide.sequenceConversion.containsKey(sequence)) {
					List<String> list = new ArrayList<String>();
					ExtendedIdentifiedPeptide.sequenceConversion.put(sequence, list);
				}
			}
		}
		return getSequence();
	}

	public static List<String> getModifiedSequences(String sequence) {
		if (ExtendedIdentifiedPeptide.sequenceConversion.containsKey(sequence))
			return ExtendedIdentifiedPeptide.sequenceConversion.get(sequence);
		else {
			List<String> list = new ArrayList<String>();
			list.add(sequence);
			return list;
		}
	}

	// @Override
	// public boolean equals(Object obj) {
	// if (!(obj instanceof IdentifiedPeptide))
	// return super.equals(obj);
	// else {
	// IdentifiedPeptide peptide = (IdentifiedPeptide) obj;
	// return peptide.getId() == this.getId();
	// }
	// }

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof IdentifiedPeptide))
			return super.equals(obj);
		else {
			IdentifiedPeptide peptide = (IdentifiedPeptide) obj;
			return peptide.getSequence().equals(getSequence());
		}
	}

	public String getModificationString() {
		return modificationString;
	}

	private String getModificationString(PeptideModification peptideModification) {
		return peptideModification.getName() + peptideModification.getPosition()
				+ peptideModification.getReplacementResidue() + peptideModification.getResidues()
				+ peptideModification.getAvgDelta() + peptideModification.getMonoDelta()
				+ peptideModification.getNeutralLoss();
	}

	public IdentifiedPeptide getPeptide() {
		return peptide;
	}

	@Override
	public String getSequence() {
		return peptide.getSequence();
	}

	@Override
	public Set<PeptideScore> getScores() {
		return peptide.getScores();
	}

	@Override
	public Set<PeptideModification> getModifications() {
		return peptide.getModifications();
	}

	@Override
	public String getCharge() {
		return peptide.getCharge();
	}

	@Override
	public String getMassDesviation() {
		return peptide.getMassDesviation();
	}

	@Override
	public String getSpectrumRef() {
		return peptide.getSpectrumRef();
	}

	@Override
	public InputData getInputData() {
		return peptide.getInputData();
	}

	@Override
	public int getRank() {
		return peptide.getRank();
	}

	/**
	 * Gets a key that identified a peptide. If distiguishModificatedPeptides is
	 * false, the key will be the sequence of the peptide. If
	 * distiguishModificatedPeptides is true, the key will be the sequence
	 * appended to a string that comes from the modifications
	 *
	 * @param distiguishModificatedPeptides
	 * @return
	 */
	public String getKey(boolean distiguishModificatedPeptides) {
		if (!distiguishModificatedPeptides)
			return getSequence();
		else
			return getModificationString();
	}

	@Override
	public int getId() {
		return peptide.getId();
	}

	@Override
	public List<IdentifiedProtein> getIdentifiedProteins() {

		return peptide.getIdentifiedProteins();
		// List<IdentifiedProtein> ret = new ArrayList<IdentifiedProtein>();
		// List<ExtendedIdentifiedProtein> filteredProteins = getProteins();
		// List<Integer> filteredProteinIds = new ArrayList<Integer>();
		// for (ExtendedIdentifiedProtein extendedIdentifiedProtein :
		// filteredProteins) {
		// filteredProteinIds.add(extendedIdentifiedProtein.getId());
		// }
		// for (IdentifiedProtein protein :
		// this.peptide.getIdentifiedProteins()) {
		// if (filteredProteinIds.contains(protein.getId()))
		// ret.add(protein);
		// }
		// return ret;
	}

	// public void setAsFiltered(boolean b) {
	// this.filtered = b;
	// }

	public List<ExtendedIdentifiedProtein> getProteins() {
		return proteins;
	}

	public void addProtein(ExtendedIdentifiedProtein protein) {
		if (proteins == null)
			proteins = new ArrayList<ExtendedIdentifiedProtein>();

		for (ExtendedIdentifiedProtein prot : proteins) {
			if (prot.getId() == protein.getId())
				return;
		}
		proteins.add(protein);
	}

	public void clearProteins() {
		proteins.clear();
	}

	/**
	 * @return the decoy
	 */
	@Override
	public boolean isDecoy() {
		return decoy;
	}

	/**
	 * @param decoy
	 *            the decoy to set
	 */
	@Override
	public void setDecoy(boolean decoy) {
		this.decoy = decoy;
		List<ExtendedIdentifiedProtein> proteins2 = getProteins();
		if (proteins2 != null) {
			for (ExtendedIdentifiedProtein extendedIdentifiedProtein : proteins2) {
				extendedIdentifiedProtein.setDecoy(decoy, false);
				ProteinGroup group = extendedIdentifiedProtein.getGroup();
				if (group != null)
					group.setDecoy(decoy);
			}
		}
	}

	public void setDecoy(boolean decoy, boolean setProteins) {
		this.decoy = decoy;
		if (setProteins) {
			List<ExtendedIdentifiedProtein> proteins2 = getProteins();
			if (proteins2 != null) {
				for (ExtendedIdentifiedProtein extendedIdentifiedProtein : proteins2) {
					extendedIdentifiedProtein.setDecoy(decoy, false);
				}
			}
		}
	}

	public int getNumMissedcleavages() {
		return numMissedClieavages;
	}

	public Float getBestProteinScore() {
		final ExtendedIdentifiedProtein protein = getBestProteinByProteinScore();
		if (protein != null) {
			protein.getScore();
		}
		return null;

	}

	public ExtendedIdentifiedProtein getBestProteinByProteinScore() {
		final List<ExtendedIdentifiedProtein> proteins = getProteins();
		if (proteins != null && !proteins.isEmpty()) {
			SorterUtil.sortProteinsByBestProteinScore(proteins, false);
			return proteins.get(0);
		}
		return null;
	}

	public String getCalculatedMassToCharge() {
		return calcMass;
	}

	public String getExperimentalMassToCharge() {
		return expMass;

	}

	public String getMassError() {
		return errorMass;
	}

	@Override
	public boolean isDecoy(FDRFilter filter) {
		if (filter != null) {
			if (getProteins() != null) {
				return filter.isDecoyExtendedProteins(getProteins());
			}
		}
		return false;
	}

	@Override
	public Float getScore(String scoreName) {
		if (scoreName != null) {
			Set<PeptideScore> scores = getScores();
			if (scores != null && !scores.isEmpty()) {
				for (PeptideScore peptideScore : scores) {
					if (peptideScore.getName().equalsIgnoreCase(scoreName)) {
						try {
							String value = peptideScore.getValue();
							if (value.contains(","))
								value = value.replace(",", ".");
							return Float.valueOf(value);
						} catch (NumberFormatException e) {
							log.warn(e.getMessage());
						}
					}
				}
			}
		}
		return null;
	}

	@Override
	public Float getScore() {
		SortingParameters sorting = SortingManager.getInstance().getPeptideSortingByPeptideScore(this);
		if (sorting == null)
			return null;
		return this.getScore(sorting.getScoreName());
	}

	@Override
	public Integer getMiapeMSReference() {
		return miapeMSReference;
	}

	/**
	 * Gets the MIAPE MSI document in which the peptide was reported
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
		Set<PeptideScore> scores = getScores();
		if (scores != null)
			for (PeptideScore peptideScore : scores) {
				String name = peptideScore.getName();
				if (!ret.contains(name))
					ret.add(name);
			}
		return ret;
	}

	public double getTheoreticMass() throws IllegalArgumentException {

		double mass = Atom.H.mass;
		AminoAcid currentAA;

		for (int aa = 0; aa < getSequence().length(); aa++) {
			try {
				currentAA = AminoAcid.getAminoAcid(getSequence().charAt(aa));
				mass += currentAA.monoisotopicMass;
			} catch (NullPointerException e) {
				throw new IllegalArgumentException("Unknown amino acid: " + getSequence().charAt(aa) + "!");
			}
		}

		mass += Atom.H.mass + Atom.O.mass;

		for (PeptideModification ptm : getModifications()) {
			final Double monoDelta = ptm.getMonoDelta();
			if (monoDelta != null)
				mass += monoDelta;
		}
		return mass;
	}

	public SortingParameters getSortingParameters() {
		return sortingParameters;
	}

	public void setSortingParameters(SortingParameters sorting) {
		sortingParameters = sorting;
	}

	@Override
	public String getRetentionTimeInSeconds() {
		return peptide.getRetentionTimeInSeconds();
	}

	public Float getPSMLocalFDR() {
		return psmLocalFDR;
	}

	public void setPSMLocalFDR(Float psmLocalFDR) {
		this.psmLocalFDR = psmLocalFDR;
	}

	public Float getPeptideLocalFDR() {
		return peptideLocalFDR;
	}

	public void setPeptideLocalFDR(Float peptideLocalFDR) {
		this.peptideLocalFDR = peptideLocalFDR;
	}

	public Float getPsmQValue() {
		return psmQValue;
	}

	public void setPsmQValue(Float psmQValue) {
		this.psmQValue = psmQValue;
	}

	public Float getPeptideQValue() {
		return peptideQValue;
	}

	public void setPeptideQValue(Float peptideQValue) {
		this.peptideQValue = peptideQValue;
	}

}
