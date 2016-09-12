package org.proteored.miapeapi.xml.xtandem.msi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularyTerm;
import org.proteored.miapeapi.cv.msi.Score;
import org.proteored.miapeapi.interfaces.msi.IdentifiedPeptide;
import org.proteored.miapeapi.interfaces.msi.IdentifiedProtein;
import org.proteored.miapeapi.interfaces.msi.InputData;
import org.proteored.miapeapi.interfaces.msi.PeptideModification;
import org.proteored.miapeapi.interfaces.msi.PeptideScore;
import org.proteored.miapeapi.xml.util.MiapeXmlUtil;

import de.proteinms.xtandemparser.interfaces.Modification;
import de.proteinms.xtandemparser.xtandem.Domain;
import de.proteinms.xtandemparser.xtandem.InputParams;
import de.proteinms.xtandemparser.xtandem.ModificationMap;
import de.proteinms.xtandemparser.xtandem.Peptide;
import de.proteinms.xtandemparser.xtandem.Spectrum;

public class IdentifiedPeptideImpl implements IdentifiedPeptide {
	private final Domain domain;
	private final ModificationMap modificationsMap;
	private final ControlVocabularyManager cvManager;
	private final Integer identifier;
	private List<IdentifiedProtein> proteins = new ArrayList<IdentifiedProtein>();
	private final String spectrumRef;
	private final InputData inputData;
	private final Spectrum spectrum;
	private final HashMap<String, String> rawModMap;
	private final Peptide xTandemPeptide;
	private final InputParams inputParams;
	private final int rank;

	// private final ProteinMap proteinMap;

	public IdentifiedPeptideImpl(Domain domain, Peptide xTandemPeptide,
			ModificationMap modificationsMap,
			HashMap<String, String> rawModMap, InputParams inputParams,
			Integer identifier, InputData inputData,
			ControlVocabularyManager cvManager, Spectrum spectrum, int rank,
			String spectrumRef) {
		this.domain = domain;
		this.modificationsMap = modificationsMap;
		this.cvManager = cvManager;
		this.identifier = identifier;
		this.inputData = inputData;
		this.spectrum = spectrum;
		this.rawModMap = rawModMap;
		this.xTandemPeptide = xTandemPeptide;
		this.inputParams = inputParams;
		this.rank = rank;
		this.spectrumRef = spectrumRef;
	}

	@Override
	public String getSequence() {
		return domain.getDomainSequence();
	}

	@Override
	public Set<PeptideScore> getScores() {
		Set<PeptideScore> peptideScores = new HashSet<PeptideScore>();

		try {
			String name;

			name = "X!Tandem:hyperscore";
			final ControlVocabularyTerm xTandemHyperScoreTerm = Score
					.getXTandemHyperScoreTerm(cvManager);
			if (xTandemHyperScoreTerm != null)
				name = xTandemHyperScoreTerm.getPreferredName();
			peptideScores.add(new PeptideScoreImpl(name, domain
					.getDomainHyperScore()));
			name = "X!Tandem:expect";
			final ControlVocabularyTerm xTandemExpectedValueTerm = Score
					.getXTandemExpectValueTerm(cvManager);
			if (xTandemExpectedValueTerm != null)
				name = xTandemExpectedValueTerm.getPreferredName();
			peptideScores.add(new PeptideScoreImpl(name, domain
					.getDomainExpect()));

			// not appears in the xtandem output documentation
			// peptideScores.add(new PeptideScoreImpl("X!Tandem:nextscore",
			// domain
			// .getDomainNextScore()));

			return peptideScores;
		} catch (Exception ex) {
			return null;
		}
	}

	@Override
	public Set<PeptideModification> getModifications() {
		if (modificationsMap != null) {

			Set<PeptideModification> peptideModifications = new HashSet<PeptideModification>();
			ArrayList<Modification> allFixedModifications = modificationsMap
					.getAllFixedModifications();
			int start = domain.getDomainStart();
			int end = domain.getDomainEnd();

			int m_counter = 1;
			String modKey = getModKey(xTandemPeptide.getSpectrumNumber(),
					xTandemPeptide.getPeptideID(), domain.getDomainKey(),
					m_counter);

			while (rawModMap.get("name" + modKey) != null) {

				final PeptideModificationImpl2 peptideModification = new PeptideModificationImpl2(
						modKey, rawModMap, domain.getDomainStart(),
						inputParams, cvManager);

				peptideModifications.add(peptideModification);

				m_counter++;
				modKey = getModKey(xTandemPeptide.getSpectrumNumber(),
						xTandemPeptide.getPeptideID(), domain.getDomainKey(),
						m_counter);
			}
			// for (Modification modification : allFixedModifications) {
			// try {
			// modification.get
			// int location = Integer.valueOf(modification.getLocation());
			// if (location >= start && location <= end) {
			// peptideModifications.add(new PeptideModificationImpl(
			// modification, domain.getDomainStart(),
			// cvManager));
			// }
			// } catch (NumberFormatException e) {
			//
			// }
			// }
			//
			// ArrayList<Modification> allVariableModifications =
			// modificationsMap
			// .getAllVariableModifications();
			// for (Modification modification : allVariableModifications) {
			// try {
			// int location = Integer.valueOf(modification.getLocation());
			// if (location >= start && location <= end) {
			// peptideModifications.add(new PeptideModificationImpl(
			// modification, domain.getDomainStart(),
			// cvManager));
			// }
			// } catch (NumberFormatException e) {
			//
			// }
			//
			// }

			if (!peptideModifications.isEmpty())
				return peptideModifications;
		}
		return null;
	}

	private String getModKey(int spectrumNumber, String peptideID,
			String domainKey, int m_counter) {
		int peptideNumber = Integer.valueOf(peptideID.substring(peptideID
				.indexOf("_p") + 2));

		return "_" + domainKey + "_m" + m_counter;

	}

	@Override
	public String getCharge() {
		return String.valueOf(spectrum.getPrecursorCharge());
	}

	@Override
	public String getMassDesviation() {
		StringBuilder sb = new StringBuilder();

		int charge = spectrum.getPrecursorCharge();
		if (charge > 0) {
			// delta
			sb.append(MiapeXmlUtil.ERROR_MZ + "=" + domain.getDomainDeltaMh()
					/ charge);
			// experimental
			final double experimentalMZ = (spectrum.getPrecursorMh() - MiapeXmlUtil.PROTON_MASS)
					/ charge + MiapeXmlUtil.PROTON_MASS;
			sb.append(MiapeXmlUtil.TERM_SEPARATOR
					+ MiapeXmlUtil.EXPERIMENTAL_MZ + "=" + experimentalMZ);
			final double theoricMZ = (domain.getDomainMh() - MiapeXmlUtil.PROTON_MASS)
					/ charge + MiapeXmlUtil.PROTON_MASS;
			sb.append(MiapeXmlUtil.TERM_SEPARATOR + MiapeXmlUtil.CALCULATED_MZ
					+ "=" + theoricMZ);
		}
		return sb.toString();
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
		if (identifier != null)
			return identifier;
		return -1;
	}

	@Override
	public List<IdentifiedProtein> getIdentifiedProteins() {

		return proteins;
	}

	public void setProteins(List<IdentifiedProtein> proteins) {
		this.proteins = proteins;
	}

	public void addProtein(IdentifiedProtein identifiedProtein) {
		if (proteins == null) {
			proteins = new ArrayList<IdentifiedProtein>();
		}
		proteins.add(identifiedProtein);

	}

	@Override
	public String getRetentionTimeInSeconds() {
		return spectrum.getPrecursorRetentionTime();
	}
}
