package org.proteored.miapeapi.experiment.model;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.proteored.miapeapi.experiment.model.filters.FDRFilter;
import org.proteored.miapeapi.experiment.model.grouping.PeptideRelation;
import org.proteored.miapeapi.experiment.model.sort.SorterUtil;
import org.proteored.miapeapi.experiment.model.sort.SortingManager;
import org.proteored.miapeapi.experiment.model.sort.SortingParameters;
import org.proteored.miapeapi.factories.msi.PeptideScoreBuilder;
import org.proteored.miapeapi.interfaces.Software;
import org.proteored.miapeapi.interfaces.msi.Database;
import org.proteored.miapeapi.interfaces.msi.IdentifiedPeptide;
import org.proteored.miapeapi.interfaces.msi.IdentifiedProtein;
import org.proteored.miapeapi.interfaces.msi.InputData;
import org.proteored.miapeapi.interfaces.msi.InputParameter;
import org.proteored.miapeapi.interfaces.msi.MiapeMSIDocument;
import org.proteored.miapeapi.interfaces.msi.PeptideModification;
import org.proteored.miapeapi.interfaces.msi.PeptideScore;
import org.proteored.miapeapi.util.ModificationMapping;
import org.proteored.miapeapi.xml.util.MiapeXmlUtil;

import edu.scripps.yates.dbindex.util.IndexUtil;
import edu.scripps.yates.utilities.staticstorage.StaticStrings;
import edu.scripps.yates.utilities.strings.StringUtils;
import gnu.trove.list.array.TIntArrayList;
import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;
import uk.ac.ebi.pridemod.slimmod.model.SlimModCollection;

public class ExtendedIdentifiedPeptide extends IdentificationItem implements IdentifiedPeptide {
	private static Map<String, List<String>> sequenceConversion = new THashMap<String, List<String>>();

	private final String modificationString;
	private boolean decoy;
	private Float expMass;
	private Float calcMass;
	private Float errorMass;
	private String experimentName;
	private String replicateName;
	private FDRFilter fdrFilter;
	private Float psmLocalFDR;
	private Float peptideLocalFDR;
	private Float psmQValue;
	private Float peptideQValue;
	private int numMissedCleavages;

	private final Integer miapeMSReference;
	private final String miapeMSIName;
	private List<ExtendedIdentifiedProtein> proteins = new ArrayList<ExtendedIdentifiedProtein>();
	private static Logger log = Logger.getLogger("log4j.logger.org.proteored");
	protected PeptideRelation relation;

	private SortingParameters sortingParameters;

	private SlimModCollection preferredModifications;
	// private static int staticIdentifier = 0;
	// private final int id = ++staticIdentifier;

	private String charge;

	private int id;

	private String sequence;

	private Set<PeptideScore> scores;

	private Set<PeptideModification> modifications;

	private String massDeviation;

	private String spectrumRef;

	private InputData inputData;

	private int rank;

	private String retentionTimeInSeconds;

	private Set<Database> databases;

	private Set<Software> softwares;

	private List<Integer> identifiedProteinIDs;

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

		modificationString = StaticStrings.getUniqueInstance(createModificationString(peptide));
		if (miapeMSI != null)
			miapeMSReference = miapeMSI.getMSDocumentReference();
		else
			miapeMSReference = -1;
		miapeMSIName = miapeMSI.getName();
		this.relation = relation;
		processPeptide(peptide, miapeMSI);
	}

	public FDRFilter getFdrFilter() {
		return fdrFilter;
	}

	public void setFDRFilter(FDRFilter fdrFilter) {
		this.fdrFilter = fdrFilter;
	}

	private void processPeptide(IdentifiedPeptide peptide, MiapeMSIDocument miapeMSI) {
		final String string = peptide.getMassDesviation();
		charge = peptide.getCharge();
		modifications = new THashSet<PeptideModification>();
		modifications.addAll(peptide.getModifications());
		sequence = peptide.getSequence();
		if (string != null) {
			if (string.contains("\n")) {
				final String[] lines = string.split("\n");
				for (final String line : lines) {
					if (line.contains("=")) {
						final String[] pairNameValue = line.split("=");

						if (pairNameValue[0].startsWith(MiapeXmlUtil.EXPERIMENTAL_MZ)) {
							try {
								expMass = Float.valueOf(pairNameValue[1]);
							} catch (final NumberFormatException e) {

							}
						}
						if (pairNameValue[0].startsWith(MiapeXmlUtil.CALCULATED_MZ)) {
							try {
								calcMass = Float.valueOf(pairNameValue[1]);
							} catch (final NumberFormatException e) {

							}
						}
						if (pairNameValue[0].startsWith(MiapeXmlUtil.ERROR_MZ)) {
							try {
								errorMass = Float.valueOf(pairNameValue[1]);
							} catch (final NumberFormatException e) {

							}
						}
					}
				}
			} else {
				try {
					errorMass = Float.valueOf(string);
					final int charge = Integer.valueOf(getCharge());
					final float calcMzDouble = Double.valueOf(
							ModificationMapping.getAASequenceImpl(getSequence(), getModifications()).getMz(charge))
							.floatValue();
					calcMass = calcMzDouble;
					expMass = calcMzDouble - errorMass;
				} catch (final NumberFormatException e) {

				}
			}
		}
		if (errorMass == null && calcMass != null && expMass != null) {
			errorMass = calcMass - expMass;
		}
		final String seq = peptide.getSequence();
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

		numMissedCleavages = numMiss;

		// if (this.peptide.getIdentifiedProteins() != null) {
		// TIntHashSet proteinIDs = new TIntHashSet();
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
		// TODO add all parameters

		id = peptide.getId();

		scores = new THashSet<PeptideScore>();
		// scores.addAll(peptide.getScores());
		for (final PeptideScore score : peptide.getScores()) {
			final PeptideScore adaptedScore = new PeptideScoreBuilder(StaticStrings.getUniqueInstance(score.getName()),
					StaticStrings.getUniqueInstance(score.getValue())).build();
			scores.add(adaptedScore);
		}

		massDeviation = StaticStrings.getUniqueInstance(peptide.getMassDesviation());
		spectrumRef = peptide.getSpectrumRef();
		inputData = peptide.getInputData();
		rank = peptide.getRank();
		retentionTimeInSeconds = peptide.getRetentionTimeInSeconds();
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
		// protein ids
		identifiedProteinIDs = peptide.getIdentifiedProteins().stream().map(p -> p.getId())
				.collect(Collectors.toList());

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

	private String createModificationString(IdentifiedPeptide peptide) {
		final Set<PeptideModification> modificationSet = peptide.getModifications();
		if (modificationSet != null) {
			final List<PeptideModification> modificationList = new ArrayList<PeptideModification>();
			for (final PeptideModification peptideModification : modificationSet) {
				modificationList.add(peptideModification);
			}
			final String sequence = peptide.getSequence();
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
				final List<String> splits = new ArrayList<String>();
				final String temp = sequence;
				int previousPosition = 0;
				String after = "";
				for (final PeptideModification peptideModification : modificationList) {
					final int position = peptideModification.getPosition();

					log.debug("modification: " + sequence + " pos:" + position);

					if (position > 0 && position <= sequence.length()) {
						final String before = sequence.substring(previousPosition, position);
						splits.add(before);
						previousPosition = position;
						after = "";
						if (position + 1 <= sequence.length())
							after = sequence.substring(position);
						// N-terminal
					} else if (position == 0) {
						final String before = "";
						splits.add(before);
						previousPosition = position;
						after = sequence;
						// C-TERMINAL
					} else if (position == sequence.length()) {
						final String before = sequence;
						splits.add(before);
						previousPosition = position;
						after = "";
					}

				}
				splits.add(after);
				final DecimalFormat format = new DecimalFormat("#.##");
				final StringBuilder sb = new StringBuilder();
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
						final String delta = "(" + prefix + format.format(deltaMass) + ")";
						sb.append(delta);
					}
					if (subtitution != null) {
						sb.append("(->" + subtitution + ")");
					}
				}
				if (!ExtendedIdentifiedPeptide.sequenceConversion.containsKey(peptide.getSequence())) {
					final List<String> list = new ArrayList<String>();
					list.add(sb.toString());
					ExtendedIdentifiedPeptide.sequenceConversion.put(peptide.getSequence(), list);
				} else {

					final List<String> list = ExtendedIdentifiedPeptide.sequenceConversion.get(peptide.getSequence());
					if (list != null) {
						try {
							if (!list.contains(sb.toString()))
								list.add(sb.toString());
						} catch (final ArrayIndexOutOfBoundsException e) {
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
					final List<String> list = new ArrayList<String>();
					ExtendedIdentifiedPeptide.sequenceConversion.put(sequence, list);
				}
			}
		}
		return peptide.getSequence();
	}

	public static List<String> getModifiedSequences(String sequence) {
		if (ExtendedIdentifiedPeptide.sequenceConversion.containsKey(sequence))
			return ExtendedIdentifiedPeptide.sequenceConversion.get(sequence);
		else {
			final List<String> list = new ArrayList<String>();
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
			final IdentifiedPeptide peptide = (IdentifiedPeptide) obj;
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

	@Override
	public String getSequence() {
		return sequence;
	}

	@Override
	public Set<PeptideScore> getScores() {
		return scores;
	}

	@Override
	public Set<PeptideModification> getModifications() {
		return modifications;
	}

	@Override
	public String getCharge() {
		return charge;
	}

	@Override
	public String getMassDesviation() {
		return massDeviation;
	}

	@Override
	public String getSpectrumRef() {
		return spectrumRef;
	}

	@Override
	public InputData getInputData() {
		return inputData;
	}

	@Override
	public int getRank() {
		return rank;
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

		return id;
	}

	@Override
	public List<IdentifiedProtein> getIdentifiedProteins() {

		return getProteins().stream().map(p -> (IdentifiedProtein) p).collect(Collectors.toList());
	}

	public List<Integer> getIdentifiedProteinIDs() {

		return identifiedProteinIDs;

	}

	public List<ExtendedIdentifiedProtein> getProteins() {
		return proteins;
	}

	public void addProtein(ExtendedIdentifiedProtein protein) {
		if (proteins == null)
			proteins = new ArrayList<ExtendedIdentifiedProtein>();

		for (final ExtendedIdentifiedProtein prot : proteins) {
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
		final List<ExtendedIdentifiedProtein> proteins2 = getProteins();
		if (proteins2 != null) {
			for (final ExtendedIdentifiedProtein extendedIdentifiedProtein : proteins2) {
				extendedIdentifiedProtein.setDecoy(decoy, false);
				final ProteinGroup group = extendedIdentifiedProtein.getGroup();
				if (group != null)
					group.setDecoy(decoy);
			}
		}
	}

	public void setDecoy(boolean decoy, boolean setProteins) {
		this.decoy = decoy;
		if (setProteins) {
			final List<ExtendedIdentifiedProtein> proteins2 = getProteins();
			if (proteins2 != null) {
				for (final ExtendedIdentifiedProtein extendedIdentifiedProtein : proteins2) {
					extendedIdentifiedProtein.setDecoy(decoy, false);
				}
			}
		}
	}

	public int getNumMissedcleavages() {
		return numMissedCleavages;
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

	public Float getCalculatedMassToCharge() {
		return calcMass;
	}

	public Float getExperimentalMassToCharge() {
		return expMass;

	}

	public Float getMassError() {
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
			final Set<PeptideScore> scores = getScores();
			if (scores != null && !scores.isEmpty()) {
				for (final PeptideScore peptideScore : scores) {
					if (peptideScore.getName().equalsIgnoreCase(scoreName)) {
						try {
							String value = peptideScore.getValue();
							if (value.contains(","))
								value = value.replace(",", ".");
							return Float.valueOf(value);
						} catch (final NumberFormatException e) {
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
		final SortingParameters sorting = SortingManager.getInstance().getPeptideSortingByPeptideScore(this);
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
	public String getMiapeMSIName() {
		return miapeMSIName;
	}

	@Override
	public List<String> getScoreNames() {
		final List<String> ret = new ArrayList<String>();
		final Set<PeptideScore> scores = getScores();
		if (scores != null)
			for (final PeptideScore peptideScore : scores) {
				final String name = peptideScore.getName();
				if (!ret.contains(name))
					ret.add(name);
			}
		return ret;
	}

	public double getTheoreticMass() throws IllegalArgumentException {

		double mass = IndexUtil.calculateMass(getSequence(), true);

		for (final PeptideModification ptm : getModifications()) {
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
		return retentionTimeInSeconds;
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

	public int getNumMissedcleavages(String cleavageAminoacids) {
		int ret = 0;
		final String sequence = getSequence().substring(0, getSequence().length() - 1);
		for (int i = 0; i < cleavageAminoacids.length(); i++) {
			final TIntArrayList allPositionsOf = StringUtils.allPositionsOf(sequence, cleavageAminoacids.charAt(i));
			ret += allPositionsOf.size();
		}
		return ret;
	}

	public Set<Database> getDatabases() {
		return databases;
	}

	public Set<Software> getSoftwares() {
		return softwares;
	}

}
