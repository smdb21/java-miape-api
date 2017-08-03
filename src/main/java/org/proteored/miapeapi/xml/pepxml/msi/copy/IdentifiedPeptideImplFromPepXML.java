package org.proteored.miapeapi.xml.pepxml.msi.copy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

import edu.scripps.yates.dtaselectparser.util.DTASelectModification;
import edu.scripps.yates.dtaselectparser.util.DTASelectPSM;
import edu.scripps.yates.utilities.masses.MassesUtil;
import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;

public class IdentifiedPeptideImplFromPepXML implements IdentifiedPeptide {
	private final DTASelectPSM dtaSelectPSM;
	private final ControlVocabularyManager cvManager;
	private final List<IdentifiedProtein> proteins = new ArrayList<IdentifiedProtein>();
	private final int id;
	private static Integer seed;
	public final static Map<String, IdentifiedPeptide> map = new THashMap<String, IdentifiedPeptide>();

	public IdentifiedPeptideImplFromPepXML(DTASelectPSM dtaSelectPSM, ControlVocabularyManager cvManager) {
		this.cvManager = cvManager;
		this.dtaSelectPSM = dtaSelectPSM;
		id = getRandomInt();
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
		if (dtaSelectPSM.getSequence() != null && dtaSelectPSM.getSequence().getSequence() != null) {
			String seq = parseSequence(dtaSelectPSM.getSequence().getSequence());
			if (seq != null)
				return seq;
		}
		return parseSequence(dtaSelectPSM.getFullSequence());
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
		Set<PeptideScore> ret = new THashSet<PeptideScore>();
		final Double conf = dtaSelectPSM.getConf();
		if (conf != null) {
			PeptideScore score = new PeptideScoreBuilder("Conf%", conf.toString()).build();
			ret.add(score);
		}
		final Double deltacn = dtaSelectPSM.getDeltacn();
		if (deltacn != null) {
			ControlVocabularyTerm scoreTerm = null;
			if ("sequest".equalsIgnoreCase(dtaSelectPSM.getSearchEngine())) {
				Score.getInstance(cvManager);
				scoreTerm = Score.getSequestDeltaCNTerm(cvManager);
			} else if ("ProLuCID".equalsIgnoreCase(dtaSelectPSM.getSearchEngine())) {
				Score.getInstance(cvManager);
				scoreTerm = Score.getProLuCIDDeltaCNTerm(cvManager);
			}
			String scoreName = "DeltaCN";
			if (scoreTerm != null) {
				scoreName = scoreTerm.getPreferredName();
			}
			PeptideScore score = new PeptideScoreBuilder(scoreName, deltacn.toString()).build();
			ret.add(score);
		}
		final Double xcorr = dtaSelectPSM.getXcorr();
		if (xcorr != null) {
			ControlVocabularyTerm scoreTerm = null;
			if ("sequest".equalsIgnoreCase(dtaSelectPSM.getSearchEngine())) {
				Score.getInstance(cvManager);
				scoreTerm = Score.getSequestXCorrTerm(cvManager);
			} else if ("ProLuCID".equalsIgnoreCase(dtaSelectPSM.getSearchEngine())) {
				Score.getInstance(cvManager);
				scoreTerm = Score.getProLuCIDXCorrCNTerm(cvManager);
			}
			String scoreName = "XCorr";
			if (scoreTerm != null) {
				scoreName = scoreTerm.getPreferredName();
			}
			PeptideScore score = new PeptideScoreBuilder(scoreName, xcorr.toString()).build();
			ret.add(score);
		}
		if (dtaSelectPSM.getProb() != null) {
			PeptideScore score = new PeptideScoreBuilder("Prob", dtaSelectPSM.getProb().toString()).build();
			ret.add(score);
		}
		if (dtaSelectPSM.getProb_score() != null) {
			PeptideScore score = new PeptideScoreBuilder("Prob Score", dtaSelectPSM.getProb_score().toString()).build();
			ret.add(score);
		}
		if (dtaSelectPSM.getIonProportion() != null) {
			PeptideScore score = new PeptideScoreBuilder("Ion proportion", dtaSelectPSM.getIonProportion().toString())
					.build();
			ret.add(score);
		}
		if (dtaSelectPSM.getTotalIntensity() != null) {
			PeptideScore score = new PeptideScoreBuilder("Total Intensity", dtaSelectPSM.getTotalIntensity().toString())
					.build();
			ret.add(score);
		}
		return ret;
	}

	@Override
	public Set<PeptideModification> getModifications() {
		final List<DTASelectModification> modifications = dtaSelectPSM.getModifications();
		Set<PeptideModification> ret = new THashSet<PeptideModification>();
		if (modifications != null) {
			for (DTASelectModification dtaSelectPTM : modifications) {
				ret.add(new PeptideModificationImplFromPepXML(dtaSelectPTM));
			}
		}
		return ret;
	}

	@Override
	public String getCharge() {
		return String.valueOf(dtaSelectPSM.getChargeState());
	}

	@Override
	public String getMassDesviation() {
		StringBuilder sb = new StringBuilder();
		Double calculatedMassH = dtaSelectPSM.getCalcMh();
		Integer charge = null;
		charge = Integer.valueOf(getCharge());
		if (calculatedMassH != null && charge != null) {
			sb.append(MiapeXmlUtil.CALCULATED_MZ + "=");
			final double mz = (calculatedMassH - MassesUtil.H) / charge * 1.0;
			sb.append(mz);
			sb.append(MiapeXmlUtil.TERM_SEPARATOR);
		}
		Double experimentalMassH = dtaSelectPSM.getMh();
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
		return dtaSelectPSM.getPsmIdentifier();
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
		if (dtaSelectPSM.getRTInMin() != null) {
			return String.valueOf(dtaSelectPSM.getRTInMin() * 60.0);
		}
		return null;
	}

}
