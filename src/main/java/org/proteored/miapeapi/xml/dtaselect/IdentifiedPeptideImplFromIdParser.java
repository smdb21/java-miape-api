package org.proteored.miapeapi.xml.dtaselect;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularyTerm;
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
import edu.scripps.yates.utilities.proteomicsmodel.PSM;
import edu.scripps.yates.utilities.proteomicsmodel.PTM;
import edu.scripps.yates.utilities.proteomicsmodel.PTMSite;
import gnu.trove.set.hash.THashSet;

public class IdentifiedPeptideImplFromIdParser implements IdentifiedPeptide {
	private final PSM psm;
	private final ControlVocabularyManager cvManager;
	private final List<IdentifiedProtein> proteins = new ArrayList<IdentifiedProtein>();
	private final int id;
	private static Integer seed;

	public IdentifiedPeptideImplFromIdParser(PSM psm, ControlVocabularyManager cvManager) {
		this.cvManager = cvManager;
		this.psm = psm;
		id = getRandomInt();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof IdentifiedPeptideImplFromIdParser) {
			if (((IdentifiedPeptideImplFromIdParser) obj).getSpectrumRef().equals(getSpectrumRef())) {
				return true;
			}
		}
		return super.equals(obj);
	}

	private int getRandomInt() {

		if (seed == null) {
			final Random generator = new Random();
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
		if (psm.getSequence() != null && psm.getSequence() != null) {
			final String seq = parseSequence(psm.getSequence());
			if (seq != null)
				return seq;
		}
		return parseSequence(psm.getFullSequence());
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
					final String left = seq.substring(0, indexOf);
					indexOf = seq.indexOf("]");
					final String rigth = seq.substring(indexOf + 1);
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
		} catch (final Exception e) {
			return seq;
		}
	}

	private static boolean somethingExtrangeInSequence(String seq) {
		return seq.matches(".*\\[.*\\].*") || seq.matches(".*\\.(.*)\\..*") || seq.matches(".*\\).*\\(.*");

	}

	@Override
	public Set<PeptideScore> getScores() {
		final Set<PeptideScore> ret = new THashSet<PeptideScore>();
		// final Double conf = dtaSelectPSM.getConf();
		// if (conf != null) {
		// final PeptideScore score = new PeptideScoreBuilder("Conf%",
		// conf.toString()).build();
		// ret.add(score);
		// }
		final Double deltacn = psm.getDeltaCn();
		if (deltacn != null) {
			ControlVocabularyTerm scoreTerm = null;
			if ("sequest".equalsIgnoreCase(psm.getSearchEngine())) {
				Score.getInstance(cvManager);
				scoreTerm = Score.getSequestDeltaCNTerm(cvManager);
			} else if ("ProLuCID".equalsIgnoreCase(psm.getSearchEngine())) {
				Score.getInstance(cvManager);
				scoreTerm = Score.getProLuCIDDeltaCNTerm(cvManager);
			}
			String scoreName = "DeltaCN";
			if (scoreTerm != null) {
				scoreName = scoreTerm.getPreferredName();
			}
			final PeptideScore score = new PeptideScoreBuilder(scoreName, deltacn.toString()).build();
			ret.add(score);
		}
		final Double xcorr = psm.getXCorr();
		if (xcorr != null) {
			ControlVocabularyTerm scoreTerm = null;
			if ("sequest".equalsIgnoreCase(psm.getSearchEngine())) {
				Score.getInstance(cvManager);
				scoreTerm = Score.getSequestXCorrTerm(cvManager);
			} else if ("ProLuCID".equalsIgnoreCase(psm.getSearchEngine())) {
				Score.getInstance(cvManager);
				scoreTerm = Score.getProLuCIDXCorrCNTerm(cvManager);
			}
			String scoreName = "XCorr";
			if (scoreTerm != null) {
				scoreName = scoreTerm.getPreferredName();
			}
			final PeptideScore score = new PeptideScoreBuilder(scoreName, xcorr.toString()).build();
			ret.add(score);
		}
		// if (psm.getProb() != null) {
		// final PeptideScore score = new PeptideScoreBuilder("Prob",
		// psm.getProb().toString()).build();
		// ret.add(score);
		// }
		// if (psm.getProb_score() != null) {
		// final PeptideScore score = new PeptideScoreBuilder("Prob Score",
		// psm.getProb_score().toString()).build();
		// ret.add(score);
		// }
		if (psm.getIonProportion() != null) {
			final PeptideScore score = new PeptideScoreBuilder("Ion proportion", psm.getIonProportion().toString())
					.build();
			ret.add(score);
		}
		if (psm.getTotalIntensity() != null) {
			final PeptideScore score = new PeptideScoreBuilder("Total Intensity", psm.getTotalIntensity().toString())
					.build();
			ret.add(score);
		}
		return ret;
	}

	@Override
	public Set<PeptideModification> getModifications() {
		final List<PTM> modifications = psm.getPTMs();
		final Set<PeptideModification> ret = new THashSet<PeptideModification>();
		if (modifications != null) {
			for (final PTM dtaSelectPTM : modifications) {
				for (final PTMSite ptmSite : dtaSelectPTM.getPTMSites()) {
					ret.add(new PeptideModificationImplFromIdParser(dtaSelectPTM, ptmSite));
				}
			}
		}
		return ret;
	}

	@Override
	public String getCharge() {
		return String.valueOf(psm.getChargeState());
	}

	@Override
	public String getMassDesviation() {
		final StringBuilder sb = new StringBuilder();
		final Double calculatedMassH = psm.getCalcMH();
		Integer charge = null;
		charge = Integer.valueOf(getCharge());
		if (calculatedMassH != null && charge != null) {
			sb.append(MiapeXmlUtil.CALCULATED_MZ + "=");
			final double mz = (calculatedMassH - MassesUtil.H) / charge * 1.0;
			sb.append(mz);
			sb.append(MiapeXmlUtil.TERM_SEPARATOR);
		}
		final Double experimentalMassH = psm.getExperimentalMH();
		if (experimentalMassH != null && charge != null) {
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
		return psm.getIdentifier();
	}

	@Override
	public InputData getInputData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getRank() {

		return 1;
	}

	@Override
	public List<IdentifiedProtein> getIdentifiedProteins() {
		return proteins;
	}

	@Override
	public String getRetentionTimeInSeconds() {
		if (psm.getRtInMinutes() != null) {
			return String.valueOf(psm.getRtInMinutes() * 60.0);
		}
		return null;
	}

}
