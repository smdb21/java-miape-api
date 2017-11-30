package org.proteored.miapeapi.xml.mzidentml_1_1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.proteored.miapeapi.cv.Accession;
import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularyTerm;
import org.proteored.miapeapi.cv.msi.Score;
import org.proteored.miapeapi.exceptions.IllegalMiapeArgumentException;
import org.proteored.miapeapi.interfaces.msi.IdentifiedPeptide;
import org.proteored.miapeapi.interfaces.msi.IdentifiedProtein;
import org.proteored.miapeapi.interfaces.msi.InputData;
import org.proteored.miapeapi.interfaces.msi.PeptideModification;
import org.proteored.miapeapi.interfaces.msi.PeptideScore;
import org.proteored.miapeapi.xml.mzidentml.util.Utils;
import org.proteored.miapeapi.xml.mzidentml_1_1.util.MzidentmlControlVocabularyXmlFactory;
import org.proteored.miapeapi.xml.util.MiapeXmlUtil;
import org.proteored.miapeapi.xml.util.parallel.MapSync;

import gnu.trove.set.hash.THashSet;
import uk.ac.ebi.jmzidml.model.mzidml.AbstractParam;
import uk.ac.ebi.jmzidml.model.mzidml.CvParam;
import uk.ac.ebi.jmzidml.model.mzidml.DBSequence;
import uk.ac.ebi.jmzidml.model.mzidml.Modification;
import uk.ac.ebi.jmzidml.model.mzidml.Peptide;
import uk.ac.ebi.jmzidml.model.mzidml.PeptideEvidence;
import uk.ac.ebi.jmzidml.model.mzidml.PeptideEvidenceRef;
import uk.ac.ebi.jmzidml.model.mzidml.ProteinDetectionHypothesis;
import uk.ac.ebi.jmzidml.model.mzidml.SpectrumIdentificationItem;
import uk.ac.ebi.jmzidml.model.mzidml.SubstitutionModification;

public class IdentifiedPeptideImpl implements IdentifiedPeptide {
	private static final String CV_SEARCH_ENGINE_SCORE = "MS:1001153";
	private final InputData inputData;
	private final MzidentmlControlVocabularyXmlFactory cvUtil;
	private final String spectrumRef;
	private final int identifier;
	private List<IdentifiedProtein> identifiedProteins;
	private static Logger log = Logger.getLogger("log4j.logger.org.proteored");
	private final Peptide peptide;
	private final Set<PeptideModification> modifications;
	private final Set<PeptideScore> scores;
	private final String massDeviation;
	private final String charge;
	private final int rank;
	private final String RT;
	private final MapSync<String, ProteinDetectionHypothesis> proteinDetectionHypotesisWithPeptideEvidence;
	private final Map<String, IdentifiedProtein> proteinHash;

	public IdentifiedPeptideImpl(SpectrumIdentificationItem spectIdentItemXML, Peptide peptide, InputData inputData,
			String spectrumRef, Integer peptideID, ControlVocabularyManager cvManager,
			MapSync<String, ProteinDetectionHypothesis> proteinDetectionHypotesisWithPeptideEvidence2,
			Map<String, IdentifiedProtein> proteinHash2, String RT) {
		if (peptide == null || peptide.getPeptideSequence() == null || "".equals(peptide.getPeptideSequence()))
			throw new IllegalMiapeArgumentException("The peptide should have a peptide sequence!");
		this.peptide = peptide;
		this.inputData = inputData;
		cvUtil = new MzidentmlControlVocabularyXmlFactory(null, cvManager);

		identifier = peptideID;
		proteinDetectionHypotesisWithPeptideEvidence = proteinDetectionHypotesisWithPeptideEvidence2;
		proteinHash = proteinHash2;
		identifiedProteins = getProteinsFromThisPeptide(spectIdentItemXML);
		// this.identifiedProteins =
		// getProteinsFromThisPeptide2(spectIdentItemXML);
		modifications = getModificationsFromThisPeptide(spectIdentItemXML);
		scores = getScoresFromThisPeptide(spectIdentItemXML, peptide, cvManager);
		massDeviation = getMassDesviationFromThisPeptide(spectIdentItemXML);
		charge = getChargeFromThisPeptide(spectIdentItemXML);
		rank = getRankFromThisPeptide(spectIdentItemXML);
		this.RT = RT;

		// In case of spectrumRef is null, get it from the ID of the peptide. If
		// it is not null, no changes are made.
		this.spectrumRef = getSpectrumRefFromThisPeptide(spectrumRef);
	}

	public void addProtein(IdentifiedProtein protein) {
		if (identifiedProteins == null)
			identifiedProteins = new ArrayList<IdentifiedProtein>();
		identifiedProteins.add(protein);
	}

	private int getRankFromThisPeptide(SpectrumIdentificationItem spectIdentItemXML) {
		return spectIdentItemXML.getRank();
	}

	private String getChargeFromThisPeptide(SpectrumIdentificationItem spectIdentItemXML) {
		return String.valueOf(spectIdentItemXML.getChargeState());
	}

	private String getMassDesviationFromThisPeptide(SpectrumIdentificationItem spectrumItemXML) {
		StringBuilder sb = new StringBuilder();
		Double calculatedMassToCharge = spectrumItemXML.getCalculatedMassToCharge();
		if (calculatedMassToCharge != null) {
			sb.append(MiapeXmlUtil.CALCULATED_MZ + "=");
			sb.append(calculatedMassToCharge);
			sb.append(MiapeXmlUtil.TERM_SEPARATOR);
		}
		double experimentalMassToCharge = spectrumItemXML.getExperimentalMassToCharge();
		if (experimentalMassToCharge > 0) {
			sb.append(MiapeXmlUtil.EXPERIMENTAL_MZ + "=");
			sb.append(experimentalMassToCharge);
		}

		return Utils.checkReturnedString(sb);
	}

	/**
	 * In case of spectrumRef is null, get it from the ID of the peptide
	 *
	 * @param spectrumRef
	 * @return
	 */
	private String getSpectrumRefFromThisPeptide(String spectrumRef) {
		if (spectrumRef != null)
			return spectrumRef;
		String specRef = null;
		if (peptide != null) {
			String id = peptide.getId();
			if (!id.equals("")) {
				// TODO, support more formats?

				String entireSpectrumRef = id;
				// if the spectrumRef have the following format: peptide_x_y
				// where x is the query number and y is the number of candidate
				// of this query:
				String regexp = "^peptide_(\\d+)_\\d+$";

				if (Pattern.matches(regexp, entireSpectrumRef)) {
					Pattern p = Pattern.compile(regexp);
					Matcher m = p.matcher(entireSpectrumRef);
					if (m.find()) {
						specRef = m.group(1);
					}
				} else {
					specRef = id;
				}

				// if the spectrumRef have the following format:
				// IGGGEKLIVR%ACET_nterm:::::::::::%sample_0%cmpd_11555
				// where the desired ref is the number after "cmpd_"
				regexp = "^.*cmpd_(\\d+).*$";

				if (Pattern.matches(regexp, entireSpectrumRef)) {
					Pattern p = Pattern.compile(regexp);
					Matcher m = p.matcher(entireSpectrumRef);
					if (m.find()) {
						specRef = m.group(1);
					}
				} else {
					regexp = "^.*PEP_(\\d+).*$";
					if (Pattern.matches(regexp, entireSpectrumRef)) {
						Pattern p = Pattern.compile(regexp);
						Matcher m = p.matcher(entireSpectrumRef);
						if (m.find()) {
							specRef = m.group(1);
						}
					} else {

						// throw new IllegalMiapeArgumentException(
						// "The spectrum ref is not recognized: "
						// + entireSpectrumRef);
					}

				}
			}
		}
		return specRef;
	}

	public static Set<PeptideScore> getScoresFromThisPeptide(SpectrumIdentificationItem spectrumItemXML,
			Peptide peptideXML, ControlVocabularyManager cvManager) {
		Set<PeptideScore> peptideScores = new THashSet<PeptideScore>();

		// Scores in the SpectrumIdentificationItem
		List<AbstractParam> params = spectrumItemXML.getParamGroup();
		// Scores in the Peptide element
		params.addAll(peptideXML.getParamGroup());

		for (AbstractParam param : params) {
			if (param instanceof CvParam) {
				CvParam cvParam = (CvParam) param;
				ControlVocabularyTerm cvTerm = cvManager.getCVTermByAccession(new Accession(cvParam.getAccession()),
						Score.getInstance(cvManager));
				if (cvTerm != null) {
					peptideScores.add(new PeptideScoreImpl(cvTerm, param.getValue()));
				}
			} else {
				final Accession controlVocabularyId = cvManager.getControlVocabularyId(param.getName(),
						Score.getInstance(cvManager));
				if (controlVocabularyId != null) {
					final ControlVocabularyTerm cvTerm = cvManager.getCVTermByAccession(controlVocabularyId,
							Score.getInstance(cvManager));
					if (cvTerm != null) {
						peptideScores.add(new PeptideScoreImpl(cvTerm, param.getValue()));
					}
				}
			}

		}

		return peptideScores;
	}

	private Set<PeptideModification> getModificationsFromThisPeptide(SpectrumIdentificationItem spectrumItemXML) {
		Peptide peptideXML = spectrumItemXML.getPeptide();
		if (peptideXML == null) {
			peptideXML = peptide;

		}
		if (peptideXML != null) {
			Set<PeptideModification> modifications = new THashSet<PeptideModification>();
			// Modifications
			List<Modification> xmlModifications = peptideXML.getModification();
			if (xmlModifications != null) {
				for (Modification modification : xmlModifications) {
					modifications.add(new PeptideModificationImpl(modification, cvUtil));
				}
			}
			// Substitution modifications
			List<SubstitutionModification> xmlSubstitutionModification = peptideXML.getSubstitutionModification();
			if (xmlSubstitutionModification != null) {
				for (SubstitutionModification substitutionModification : xmlSubstitutionModification) {
					modifications.add(new PeptideModificationImpl(substitutionModification, cvUtil));
				}
			}
			if (modifications.size() > 0)
				return modifications;
		}
		return null;
	}

	private List<IdentifiedProtein> getProteinsFromThisPeptide(SpectrumIdentificationItem spectrumItemXML) {
		if (spectrumItemXML.getId().equals(
				"Hsilam-fmrKO1P17-ctx-p2-03_Spec_Hsilam-fmrKO1P17-ctx-p2-03-9493-TELEDTLDSTAAQQELR-2_TELEDTLDSTAAQQELR")) {
			log.info(spectrumItemXML);
		}
		List<IdentifiedProtein> ret = new ArrayList<IdentifiedProtein>();
		for (PeptideEvidenceRef peptideEvidenceRef : spectrumItemXML.getPeptideEvidenceRef()) {
			final PeptideEvidence peptideEvidence = peptideEvidenceRef.getPeptideEvidence();
			final DBSequence dbSequenceXML = peptideEvidence.getDBSequence();

			ProteinDetectionHypothesis proteinHypotesisXML = proteinDetectionHypotesisWithPeptideEvidence
					.get(peptideEvidence.getId());

			// Only create the protein if has passed the threshold
			if ((proteinHypotesisXML != null && proteinHypotesisXML.isPassThreshold()) || dbSequenceXML != null) {
				Integer proteinID = MiapeXmlUtil.ProteinCounter.increaseCounter();
				IdentifiedProtein protein = null;
				// If the protein has been added previously, not add to the
				// general proteinhash
				if (!proteinHash.containsKey(dbSequenceXML.getAccession())) {
					protein = new IdentifiedProteinImpl(dbSequenceXML, proteinHypotesisXML, proteinID,
							cvUtil.getCvManager());
					// log.info("adding protein " + protein.getAccession() + "/"
					// +
					// protein.getId()
					// + " to the hash from peptide " + getSequence());
					proteinHash.put(dbSequenceXML.getAccession(), protein);
				} else {
					protein = proteinHash.get(dbSequenceXML.getAccession());
				}

				// new 23-may-2013: add peptide to the protein
				((IdentifiedProteinImpl) protein).addIdentifiedPeptide(this);
				ret.add(protein);
			}

		}
		return ret;

	}

	// private List<IdentifiedProtein> getProteinsFromThisPeptide2(
	// SpectrumIdentificationItem spectrumItemXML) {
	// List<IdentifiedProtein> ret = new ArrayList<IdentifiedProtein>();
	// for (ProteinDetectionHypothesis proteinHypotesisXML :
	// this.pdhFromPeptide) {
	//
	// final DBSequence dbSequenceXML = proteinHypotesisXML
	// .getDBSequence();
	//
	// // Only create the protein if has passed the threshold
	// if ((proteinHypotesisXML != null && proteinHypotesisXML
	// .isPassThreshold()) || dbSequenceXML != null) {
	// Integer proteinID = MiapeXmlUtil.ProteinCounter
	// .increaseCounter();
	// IdentifiedProtein protein = null;
	// // If the protein has been added previously, not add to the
	// // general proteinhash
	// if (!this.proteinHash.containsKey(dbSequenceXML.getAccession())) {
	// protein = new IdentifiedProteinImpl(dbSequenceXML,
	// proteinHypotesisXML, proteinID,
	// cvUtil.getCvManager());
	// // log.info("adding protein " + protein.getAccession() + "/"
	// // +
	// // protein.getId()
	// // + " to the hash from peptide " + getSequence());
	// this.proteinHash.put(protein.getAccession(), protein);
	// } else {
	// protein = this.proteinHash
	// .get(dbSequenceXML.getAccession());
	// }
	//
	// // new 23-may-2013: add peptide to the protein
	// ((IdentifiedProteinImpl) protein).addIdentifiedPeptide(this);
	// ret.add(protein);
	// }
	//
	// }
	// return ret;
	//
	// }

	@Override
	public String toString() {
		List<String> scoreNames = new ArrayList<String>();

		for (PeptideScore peptScore : getScores()) {
			scoreNames.add(peptScore.getName());
		}
		Collections.sort(scoreNames);
		String value = null;
		for (PeptideScore peptScore : getScores()) {
			if (peptScore.getName().equals(scoreNames.get(0)))
				value = peptScore.getValue();
		}
		return getId() + "-" + getSequence() + "(" + value + ")";
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
	public Set<PeptideModification> getModifications() {
		return modifications;

	}

	@Override
	public Set<PeptideScore> getScores() {
		return scores;

	}

	@Override
	public String getSequence() {
		if (peptide != null)
			return peptide.getPeptideSequence();
		return null;
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

	@Override
	public int getId() {
		return identifier;
	}

	@Override
	public List<IdentifiedProtein> getIdentifiedProteins() {
		return identifiedProteins;

	}

	@Override
	public String getRetentionTimeInSeconds() {
		return RT;
	}

}
