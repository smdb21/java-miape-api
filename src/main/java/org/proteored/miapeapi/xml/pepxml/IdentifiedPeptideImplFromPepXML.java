package org.proteored.miapeapi.xml.pepxml;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.msi.Score;
import org.proteored.miapeapi.factories.msi.PeptideScoreBuilder;
import org.proteored.miapeapi.interfaces.msi.IdentifiedPeptide;
import org.proteored.miapeapi.interfaces.msi.IdentifiedProtein;
import org.proteored.miapeapi.interfaces.msi.InputData;
import org.proteored.miapeapi.interfaces.msi.PeptideModification;
import org.proteored.miapeapi.interfaces.msi.PeptideScore;
import org.proteored.miapeapi.xml.mzidentml.util.Utils;
import org.proteored.miapeapi.xml.util.MiapeXmlUtil;

import edu.scripps.yates.utilities.masses.MassesUtil;
import gnu.trove.set.hash.THashSet;
import umich.ms.fileio.filetypes.pepxml.jaxb.standard.ModAminoacidMass;
import umich.ms.fileio.filetypes.pepxml.jaxb.standard.ModificationInfo;
import umich.ms.fileio.filetypes.pepxml.jaxb.standard.NameValueType;
import umich.ms.fileio.filetypes.pepxml.jaxb.standard.SearchHit;
import umich.ms.fileio.filetypes.pepxml.jaxb.standard.SpectrumQuery;
import umich.ms.fileio.filetypes.pepxml.jaxb.standard.SubInfoDataType;

public class IdentifiedPeptideImplFromPepXML implements IdentifiedPeptide {
	private final SearchHit searchHit;
	private final ControlVocabularyManager cvManager;
	private final List<IdentifiedProtein> proteins = new ArrayList<IdentifiedProtein>();
	private final int id;
	private String seq;
	private THashSet<PeptideScore> scores;
	private final SpectrumQuery spectrumQuery;
	private final InputData inputData;
	private final String searchEngine;
	private static Integer seed;

	public IdentifiedPeptideImplFromPepXML(SpectrumQuery spectrumquery2, SearchHit searchHit2, InputData inputData,
			String searchEngine, ControlVocabularyManager cvManager) {
		this.cvManager = cvManager;
		this.searchHit = searchHit2;
		this.spectrumQuery = spectrumquery2;
		this.inputData = inputData;
		id = getRandomInt();
		this.searchEngine = searchEngine;
	}

	public void addProtein(IdentifiedProtein protein) {
		if (!proteins.contains(protein)) {
			proteins.add(protein);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof IdentifiedPeptideImplFromPepXML) {
			if (((IdentifiedPeptideImplFromPepXML) obj).getSpectrumRef().equals(getSpectrumRef())) {
				return true;
			}
		}
		return super.equals(obj);
	}

	private int getRandomInt() {

		if (seed == null) {
			Random generator = new Random();
			seed = generator.nextInt(Integer.MAX_VALUE);
		}
		seed = seed + 1;
		return seed;
	}

	@Override
	public int getId() {

		return id;
	}

	@Override
	public String getSequence() {
		if (seq == null && searchHit.getPeptide() != null) {
			seq = parseSequence(searchHit.getPeptide());
		}
		return seq;
	}

	/**
	 * This function allow to get the peptide sequence as <br>
	 * <ul>
	 * <li>K.VDLSFSPSQSLPASHAHLR.V -> VDLSFSPSQSLPASHAHLR</li>
	 * <li>R.LLLQQVSLPELPGEYSMK.V + Oxidation (M) -> LLLQQVSLPELPGEYSMK</li>
	 * <li>(-)TVAAPSVFIFPPSDEQLK(S) -> TVAAPSVFIFPPSDEQLK</li>
	 * <li>K.EKS[167.00]KESAIASTEVK.L -> EKSKESAIASTEVK</li>
	 * </ul>
	 * getting just the sequence without modifications and between the pre and
	 * post AA if available
	 *
	 * @param seq
	 * @return
	 */
	public static String parseSequence(String seq) {
		if (seq == null)
			return null;
		try {
			while (somethingExtrangeInSequence(seq)) {
				if (seq.matches(".*\\[.*\\].*")) {
					int indexOf = seq.indexOf("[");
					String left = seq.substring(0, indexOf);
					indexOf = seq.indexOf("]");
					String rigth = seq.substring(indexOf + 1);
					seq = left + rigth;

				}
				if (seq.matches(".*\\.(.*)\\..*")) {
					int indexOf = seq.indexOf(".");
					seq = seq.substring(indexOf + 1);
					indexOf = seq.indexOf(".");
					seq = seq.substring(0, indexOf);
				}
				if (seq.matches(".*\\).*\\(.*")) {
					int indexOf = seq.indexOf(")");
					seq = seq.substring(indexOf + 1);
					indexOf = seq.indexOf("(");
					seq = seq.substring(0, indexOf);
				}
			}
			return seq.toUpperCase();
		} catch (Exception e) {
			return seq;
		}
	}

	private static boolean somethingExtrangeInSequence(String seq) {
		return seq.matches(".*\\[.*\\].*") || seq.matches(".*\\.(.*)\\..*") || seq.matches(".*\\).*\\(.*");

	}

	@Override
	public Set<PeptideScore> getScores() {
		if (scores == null) {
			scores = new THashSet<PeptideScore>();
			for (NameValueType nameValue : searchHit.getSearchScore()) {
				String scoreName = nameValue.getName();
				if ("xcorr".equalsIgnoreCase(scoreName)) {
					if ("comet".equalsIgnoreCase(searchEngine)) {
						scoreName = Score.getCometXCorrTerm(cvManager).getPreferredName();
					} else {
						scoreName = Score.getSequestXCorrTerm(cvManager).getPreferredName();
					}
				} else if ("deltacn".equalsIgnoreCase(scoreName)) {
					if ("comet".equalsIgnoreCase(searchEngine)) {
						scoreName = Score.getCometDeltaCNTerm(cvManager).getPreferredName();
					} else {
						scoreName = Score.getSequestDeltaCNTerm(cvManager).getPreferredName();
					}
				} else if ("deltacnstar".equals(scoreName)) {
					if ("comet".equalsIgnoreCase(searchEngine)) {
						scoreName = Score.getCometDeltaCNStarTerm(cvManager).getPreferredName();
					} else {
						scoreName = Score.getSequestDeltaCNStarTerm(cvManager).getPreferredName();
					}
				} else if ("spscore".equals(scoreName)) {
					if ("comet".equalsIgnoreCase(searchEngine)) {
						scoreName = Score.getCometSPScoreTerm(cvManager).getPreferredName();
					} else {
						scoreName = Score.getSequestSPScoreTerm(cvManager).getPreferredName();
					}
				} else if ("sprank".equals(scoreName)) {
					if ("comet".equalsIgnoreCase(searchEngine)) {
						scoreName = Score.getCometSPRankTerm(cvManager).getPreferredName();
					} else {
						scoreName = Score.getSequestSPRankTerm(cvManager).getPreferredName();
					}
				} else if ("expect".equals(scoreName)) {
					scoreName = Score.getExpectValueTerm(cvManager).getPreferredName();
				}
				PeptideScore score = new PeptideScoreBuilder(scoreName, nameValue.getValueStr()).build();
				scores.add(score);
			}
		}
		return scores;

	}

	@Override
	public Set<PeptideModification> getModifications() {
		Set<PeptideModification> ret = new THashSet<PeptideModification>();
		ModificationInfo modifications = searchHit.getModificationInfo();
		if (modifications != null) {
			if (modifications.getModAminoacidMass() != null) {
				for (ModAminoacidMass mod : modifications.getModAminoacidMass()) {
					ret.add(new PeptideModificationImplFromPepXML(getSequence(), mod));
				}
			}
			if (modifications.getAminoacidSubstitution() != null) {
				for (SubInfoDataType aaSubstitution : modifications.getAminoacidSubstitution()) {
					ret.add(new PeptideModificationImplFromPepXML(getSequence(), aaSubstitution));
				}
			}
			if (modifications.getModCtermMass() != null) {
				ret.add(new PeptideModificationImplFromPepXML(getSequence(), getSequence().length(),
						modifications.getModCtermMass(), null));
			}
			if (modifications.getModNtermMass() != null) {
				ret.add(new PeptideModificationImplFromPepXML(getSequence(), 0, modifications.getModNtermMass(), null));
			}
		}
		return ret;
	}

	@Override
	public String getCharge() {
		if (spectrumQuery.getAssumedCharge() != null) {
			return spectrumQuery.getAssumedCharge().toString();
		}
		return null;
	}

	@Override
	public String getMassDesviation() {
		StringBuilder sb = new StringBuilder();
		float calculatedMassH = searchHit.getCalcNeutralPepMass();
		Integer charge = null;
		charge = Integer.valueOf(getCharge());
		if (charge != null) {
			sb.append(MiapeXmlUtil.CALCULATED_MZ + "=");
			final double mz = (calculatedMassH - MassesUtil.H) / charge * 1.0;
			sb.append(mz);
			sb.append(MiapeXmlUtil.TERM_SEPARATOR);
		}
		float experimentalMassH = spectrumQuery.getPrecursorNeutralMass();
		if (charge != null) {
			sb.append(MiapeXmlUtil.EXPERIMENTAL_MZ + "=");
			final double mz = (experimentalMassH - MassesUtil.H) / charge * 1.0;
			sb.append(mz);
		}

		return Utils.checkReturnedString(sb);
		// final Double ppmError = dtaSelectPSM.getPpmError();
		// if (ppmError != null) {
		// return ppmError.toString();
		// }
		// return null;
	}

	@Override
	public String getSpectrumRef() {
		return spectrumQuery.getSpectrum();
	}

	@Override
	public InputData getInputData() {
		return inputData;
	}

	@Override
	public int getRank() {

		return Long.valueOf(searchHit.getHitRank()).intValue();
	}

	@Override
	public List<IdentifiedProtein> getIdentifiedProteins() {
		return proteins;
	}

	@Override
	public String getRetentionTimeInSeconds() {
		return null;
	}

}
