package org.proteored.miapeapi.xml.mzidentml_1_1;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.proteored.miapeapi.cv.Accession;
import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularyTerm;
import org.proteored.miapeapi.cv.msi.MatchedPeaks;
import org.proteored.miapeapi.cv.msi.ProteinDescription;
import org.proteored.miapeapi.cv.msi.Score;
import org.proteored.miapeapi.cv.msi.ValidationType;
import org.proteored.miapeapi.interfaces.msi.IdentifiedPeptide;
import org.proteored.miapeapi.interfaces.msi.IdentifiedProtein;
import org.proteored.miapeapi.interfaces.msi.ProteinScore;
import org.proteored.miapeapi.xml.mzidentml_1_1.util.MzidentmlControlVocabularyXmlFactory;
import org.proteored.miapeapi.xml.mzidentml_1_1.util.Utils;
import org.proteored.miapeapi.xml.util.MiapeXmlUtil;

import gnu.trove.set.hash.THashSet;
import uk.ac.ebi.jmzidml.model.mzidml.AbstractParam;
import uk.ac.ebi.jmzidml.model.mzidml.CvParam;
import uk.ac.ebi.jmzidml.model.mzidml.DBSequence;
import uk.ac.ebi.jmzidml.model.mzidml.ProteinDetectionHypothesis;
import uk.ac.ebi.jmzidml.model.mzidml.UserParam;

public class IdentifiedProteinImpl implements IdentifiedProtein {
	private static final Logger log = Logger.getLogger("log4j.logger.org.proteored");
	private static final Accession CV_SEQUENCE_COVERAGE = new Accession("MS:1001093");
	private static final Accession CV_PEPTIDE_NUMBER = new Accession("MS:1001097");
	private final Map<String, AbstractParam> proteinHypotesisParams;
	private final Map<String, AbstractParam> dbSequenceParams;
	private final DBSequence dbSequenceXML;
	private final ProteinDetectionHypothesis proteinHypotesisXML;
	private String validationType;
	private String validationValue;
	private final Integer identifier;
	private final List<IdentifiedPeptide> identifiedPeptides = new ArrayList<IdentifiedPeptide>();
	private final MzidentmlControlVocabularyXmlFactory cvFactory;

	public IdentifiedProteinImpl(DBSequence dbSequenceXML, ProteinDetectionHypothesis proteinHypotesisXML,
			Integer proteinID, ControlVocabularyManager cvUtil) {
		this.dbSequenceXML = dbSequenceXML;
		this.proteinHypotesisXML = proteinHypotesisXML;
		if (proteinHypotesisXML != null) {
			this.proteinHypotesisParams = Utils.initParamMap(proteinHypotesisXML.getParamGroup());
		} else {
			this.proteinHypotesisParams = null;
		}
		this.dbSequenceParams = Utils.initParamMap(dbSequenceXML.getParamGroup());
		this.identifier = proteinID;
		this.cvFactory = new MzidentmlControlVocabularyXmlFactory(null, cvUtil);

		// get the validation type and validation value
		if (proteinHypotesisXML != null) {
			if (proteinHypotesisXML.getParamGroup() != null) {
				for (AbstractParam param : proteinHypotesisXML.getParamGroup()) {
					if (param instanceof CvParam) {
						if (cvFactory.isCV(param.getName(), ValidationType.getInstance(cvFactory.getCvManager()))) {
							validationType = param.getName();
							validationValue = param.getValue();
						}
						/*
						 * FuGECommonOntologyCvParamType cvParam =
						 * (FuGECommonOntologyCvParamType) param; if
						 * (OntologyManager
						 * .getInstance().isSonOf(cvParam.getAccession(),
						 * CV_QUALITY_ESTIMATION)) { validationType =
						 * MzidentmlControlVocabularyXmlFactory
						 * .readEntireParam(cvParam); }
						 */
					}
				}
				// if validation type has an "=" symbol is because it has a
				// value
				/*
				 * if (validationType != null) { if (validationType.indexOf("=")
				 * != -1) { validationValue =
				 * validationType.substring(validationType.indexOf("=")+1);
				 * validationType = validationType.substring(0,
				 * validationType.indexOf("=")); } }
				 */
			}
		}
	}

	@Override
	public String getAccession() {
		if (dbSequenceXML != null) {
			return dbSequenceXML.getAccession();
		}
		return null;
	}

	@Override
	public String getAdditionalInformation() {
		StringBuilder sb = new StringBuilder();
		// Add all the cvParams or UserParams that are not captured yet
		final ControlVocabularyTerm psiProteinDescriptionTerm = ProteinDescription.PSI_PROTEIN_DESCRIPTION;
		if (proteinHypotesisParams != null)
			for (String cvID : proteinHypotesisParams.keySet()) {
				if (proteinHypotesisParams.containsKey(cvID)) {
					if (!MatchedPeaks.NUMBER_OF_MATCHED_PEAKS.getTermAccession().equals(cvID)
							&& !CV_PEPTIDE_NUMBER.equals(cvID)
							&& !psiProteinDescriptionTerm.getTermAccession().equals(cvID)
							&& !CV_SEQUENCE_COVERAGE.equals(cvID)
							&& !MatchedPeaks.NUMBER_OF_UNMATCHED_PEAKS.getTermAccession().equals(cvID)
							&& !cvFactory.isCV(proteinHypotesisParams.get(cvID).getName(),
									Score.getInstance(cvFactory.getCvManager()))
							&&
							// !OntologyManager.getInstance().isSonOf(cvID,
							// CV_SEARCH_ENGINE_SCORE) &&
							!cvFactory.isCV(proteinHypotesisParams.get(cvID).getName(),
									ValidationType.getInstance(cvFactory.getCvManager())))
					// !OntologyManager.getInstance().isSonOf(cvID,
					// CV_QUALITY_ESTIMATION))
					{
						sb.append(
								MzidentmlControlVocabularyXmlFactory.readEntireParam(proteinHypotesisParams.get(cvID)));
						sb.append(MiapeXmlUtil.TERM_SEPARATOR);
					}
				}
			}
		if (dbSequenceParams != null)
			for (String cvID : dbSequenceParams.keySet()) {
				if (dbSequenceParams.containsKey(cvID)) {
					if (!MatchedPeaks.NUMBER_OF_MATCHED_PEAKS.getTermAccession().equals(cvID)
							&& !CV_PEPTIDE_NUMBER.equals(cvID)
							&& !psiProteinDescriptionTerm.getTermAccession().equals(cvID)
							&& !CV_SEQUENCE_COVERAGE.equals(cvID)
							&& !MatchedPeaks.NUMBER_OF_UNMATCHED_PEAKS.getTermAccession().equals(cvID)
							&& !cvFactory.isCV(dbSequenceParams.get(cvID).getName(),
									Score.getInstance(cvFactory.getCvManager()))
							&&
							// !OntologyManager.getInstance().isSonOf(cvID,
							// CV_SEARCH_ENGINE_SCORE) &&
							!cvFactory.isCV(dbSequenceParams.get(cvID).getName(),
									ValidationType.getInstance(cvFactory.getCvManager())))
					// !OntologyManager.getInstance().isSonOf(cvID,
					// CV_QUALITY_ESTIMATION))
					{
						sb.append(MzidentmlControlVocabularyXmlFactory.readEntireParam(dbSequenceParams.get(cvID)));
						sb.append(MiapeXmlUtil.TERM_SEPARATOR);
					}
				}
			}
		return Utils.checkReturnedString(sb);
	}

	@Override
	public String getCoverage() {
		if (proteinHypotesisParams != null)
			if (proteinHypotesisParams.containsKey(CV_SEQUENCE_COVERAGE.toString())) {
				String coverage = proteinHypotesisParams.get(CV_SEQUENCE_COVERAGE.toString()).getValue();
				return coverage;
			} else {
				// log.info("Calculating protein coverage");
				// / calculate coverage from the supporting peptides
				final String proteinSequence = dbSequenceXML.getSeq();
				final List<IdentifiedPeptide> supportingPeptides = this.getIdentifiedPeptides();
				Double coverage = MiapeXmlUtil.calculateProteinCoverage2(proteinSequence, supportingPeptides);
				if (coverage != null) {
					// log.info("Coverage = " + coverage);
					return coverage.toString();
				} else {
					// log.warn("Coverage could not be calculated");
				}
			}
		return null;
	}

	@Override
	public String getDescription() {
		if (dbSequenceXML == null)
			return null;
		if (dbSequenceParams.containsKey(ProteinDescription.PSI_PROTEIN_DESCRIPTION.getTermAccession().toString())) {
			String description = dbSequenceParams
					.get(ProteinDescription.PSI_PROTEIN_DESCRIPTION.getTermAccession().toString()).getValue();
			return description;
		}
		return dbSequenceXML.getName();
	}

	@Override
	public int getId() {
		return identifier;
	}

	@Override
	public List<IdentifiedPeptide> getIdentifiedPeptides() {
		return identifiedPeptides;
	}

	@Override
	public String getPeaksMatchedNumber() {
		if (proteinHypotesisParams != null)
			if (proteinHypotesisParams
					.containsKey(MatchedPeaks.NUMBER_OF_MATCHED_PEAKS.getTermAccession().toString())) {
				return proteinHypotesisParams.get(MatchedPeaks.NUMBER_OF_MATCHED_PEAKS.getTermAccession().toString())
						.getName();
			}
		return null;
	}

	@Override
	public String getPeptideNumber() {
		if (proteinHypotesisParams != null)
			if (proteinHypotesisParams.containsKey(CV_PEPTIDE_NUMBER.toString())) {
				String peptideNumber = proteinHypotesisParams.get(CV_PEPTIDE_NUMBER.toString()).getValue();
				return peptideNumber;
			}

		return null;
	}

	@Override
	public Set<ProteinScore> getScores() {
		if (proteinHypotesisXML == null)
			return null;

		Set<ProteinScore> proteinScores = new THashSet<ProteinScore>();
		// OntologyManager ontologyManager = OntologyManager.getInstance();
		List<AbstractParam> params = proteinHypotesisXML.getParamGroup();
		for (AbstractParam param : params) {
			if (param instanceof CvParam) {
				CvParam cvParam = (CvParam) param;
				// if the param is an score, store it in the set
				final ControlVocabularyTerm cvTerm = cvFactory.getCvManager().getCVTermByAccession(
						new Accession(cvParam.getAccession()), Score.getInstance(cvFactory.getCvManager()));
				if (cvTerm != null)
					proteinScores.add(new ProteinScoreImpl(cvTerm, cvParam.getValue()));
			} else if (param instanceof UserParam) {
				UserParam userParam = (UserParam) param;
				if (userParam.getName() != null) {
					if (userParam.getName().toLowerCase().contains("score")) {
						proteinScores.add(new ProteinScoreImpl(param));
					}
				}
			}

		}

		return proteinScores;

	}

	@Override
	public String getUnmatchedSignals() {
		Accession cvId = MatchedPeaks.NUMBER_OF_UNMATCHED_PEAKS.getTermAccession();
		if (proteinHypotesisParams != null)
			if (proteinHypotesisParams.containsKey(cvId.toString())) {
				return proteinHypotesisParams.get(cvId.toString()).getName();
			}
		return null;
	}

	@Override
	public Boolean getValidationStatus() {
		if (proteinHypotesisXML != null)
			return proteinHypotesisXML.isPassThreshold();
		return null;
	}

	@Override
	public String getValidationType() {
		return validationType;
	}

	@Override
	public String getValidationValue() {
		return validationValue;
	}

	public void addIdentifiedPeptide(IdentifiedPeptide peptide) {
		identifiedPeptides.add(peptide);
	}
}
