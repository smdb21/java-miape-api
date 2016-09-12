package org.proteored.miapeapi.xml.pride.msi;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.proteored.miapeapi.cv.Accession;
import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ms.ChargeState;
import org.proteored.miapeapi.cv.msi.Score;
import org.proteored.miapeapi.interfaces.MatchMode;
import org.proteored.miapeapi.interfaces.msi.IdentifiedPeptide;
import org.proteored.miapeapi.interfaces.msi.IdentifiedProtein;
import org.proteored.miapeapi.interfaces.msi.InputData;
import org.proteored.miapeapi.interfaces.msi.InputDataSet;
import org.proteored.miapeapi.interfaces.msi.PeptideModification;
import org.proteored.miapeapi.interfaces.msi.PeptideScore;
import org.proteored.miapeapi.xml.pride.autogenerated.CvParamType;
import org.proteored.miapeapi.xml.pride.autogenerated.Modification;
import org.proteored.miapeapi.xml.pride.autogenerated.ParamType;
import org.proteored.miapeapi.xml.pride.autogenerated.Peptide;
import org.proteored.miapeapi.xml.pride.autogenerated.UserParamType;
import org.proteored.miapeapi.xml.pride.util.PrideControlVocabularyXmlFactory;

public class IdentifiedPeptideImpl implements IdentifiedPeptide {
	private static final Accession PRIDE_RANK_CV = new Accession(
			"PRIDE:0000091");
	private final Peptide peptidePRIDE;
	private final Set<InputDataSet> inputDataSets;
	private final ControlVocabularyManager cvManager;
	private final Integer identifier;
	private List<IdentifiedProtein> proteins = new ArrayList<IdentifiedProtein>();

	public IdentifiedPeptideImpl(Peptide peptide,
			Set<InputDataSet> inputDataSets, Integer identifier,
			ControlVocabularyManager cvManager, IdentifiedProtein protein) {
		this.peptidePRIDE = peptide;
		this.inputDataSets = inputDataSets;
		this.cvManager = cvManager;
		this.identifier = identifier;
		this.proteins.add(protein);
	}

	@Override
	public String getCharge() {
		if (peptidePRIDE.getAdditional() != null) {
			final CvParamType cvParam = PrideControlVocabularyXmlFactory
					.getCvFromParamType(peptidePRIDE.getAdditional(),
							ChargeState.CHARGE_STATE_ACCESSION);
			if (cvParam != null)
				return cvParam.getValue();
		}
		return null;
	}

	@Override
	public String getMassDesviation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<PeptideModification> getModifications() {
		Set<PeptideModification> pepMods = new HashSet<PeptideModification>();
		List<Modification> modificationItemList = peptidePRIDE
				.getModificationItem();
		if (modificationItemList != null) {
			for (Modification modificationItem : modificationItemList) {
				pepMods.add(new PeptideModificationImpl(modificationItem,
						peptidePRIDE.getSequence(), cvManager));
			}
		}
		if (pepMods.size() > 0)
			return pepMods;
		return null;
	}

	@Override
	public Set<PeptideScore> getScores() {
		StringBuilder sb = new StringBuilder();
		final ParamType additional = peptidePRIDE.getAdditional();
		if (additional == null)
			return null;
		Set<PeptideScore> scores = new HashSet<PeptideScore>();
		for (Object param : additional.getCvParamOrUserParam()) {
			if (param instanceof CvParamType) {
				CvParamType cvParam = (CvParamType) param;
				if (Score.getInstance(cvManager).getCVTermByPreferredName(
						cvParam.getName()) != null)
					scores.add(new PeptideScoreImpl(cvParam));
			} else if (param instanceof UserParamType) {
				UserParamType userParam = (UserParamType) param;
				String[] SCORE_TEXT_LIST = { "Score", "Threshold" };
				if (PrideControlVocabularyXmlFactory.findTextInUserParamType(
						userParam, SCORE_TEXT_LIST, MatchMode.ANYWHERE)) {
					scores.add(new PeptideScoreImpl(userParam));
				}
			}
		}
		return scores;
	}

	@Override
	public String getSequence() {
		return peptidePRIDE.getSequence();
	}

	@Override
	public String getSpectrumRef() {
		if (peptidePRIDE.getSpectrumReference() != null) {
			return peptidePRIDE.getSpectrumReference().toString();
		}
		return null;
	}

	@Override
	public String toString() {
		return "IdentifiedPeptideImpl [peptide=" + peptidePRIDE.getSequence()
				+ "]";
	}

	@Override
	public InputData getInputData() {
		if (inputDataSets != null) {
			final InputDataSet inputDataSet = inputDataSets.iterator().next();
			if (inputDataSet != null && inputDataSet.getInputDatas() != null
					&& inputDataSet.getInputDatas().size() > 0) {
				return inputDataSet.getInputDatas().iterator().next();
			}
		}
		return null;
	}

	@Override
	public int getRank() {
		if (peptidePRIDE.getAdditional() != null) {
			final CvParamType cvParam = PrideControlVocabularyXmlFactory
					.getCvFromParamType(peptidePRIDE.getAdditional(),
							PRIDE_RANK_CV);
			if (cvParam != null) {
				try {
					return Integer.valueOf(cvParam.getValue());
				} catch (NumberFormatException ex) {
					// do nothing
				}
			}
		}
		return 0;
	}

	@Override
	public int getId() {
		return identifier;
	}

	@Override
	public List<IdentifiedProtein> getIdentifiedProteins() {
		return this.proteins;
	}

	@Override
	public String getRetentionTimeInSeconds() {
		if (peptidePRIDE != null) {
			ParamType additional = peptidePRIDE.getAdditional();
			if (additional != null
					&& additional.getCvParamOrUserParam() != null) {
				for (Object object : additional.getCvParamOrUserParam()) {
					if (object instanceof CvParamType) {
						CvParamType cvParam = (CvParamType) object;
						if (cvParam.getValue() != null) {
							try {
								int num = Integer.valueOf(cvParam.getValue());
								if (cvParam.getAccession().equals(
										"PRIDE:0000203"))
									num = num * 60; // convert to seconds if it
													// is thePRIDE:0000203
								return String.valueOf(num);
							} catch (NumberFormatException e) {

							}

						}
					}
				}
			}
		}
		return null;
	}
}
