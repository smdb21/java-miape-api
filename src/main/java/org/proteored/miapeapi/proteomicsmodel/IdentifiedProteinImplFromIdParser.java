package org.proteored.miapeapi.proteomicsmodel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.interfaces.msi.IdentifiedPeptide;
import org.proteored.miapeapi.interfaces.msi.IdentifiedProtein;
import org.proteored.miapeapi.interfaces.msi.ProteinScore;

import edu.scripps.yates.utilities.fasta.FastaParser;
import edu.scripps.yates.utilities.proteomicsmodel.PSM;
import edu.scripps.yates.utilities.proteomicsmodel.Protein;
import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;

public class IdentifiedProteinImplFromIdParser implements IdentifiedProtein {
	private final Protein idProtein;
	private final int id;
	private Set<ProteinScore> proteinScores;
	private List<IdentifiedPeptide> peptides;
	private final ControlVocabularyManager cvManager;
	public static final Map<String, IdentifiedPeptide> psmMapByPSMId = new THashMap<String, IdentifiedPeptide>();
	private final static String EMPAI_VALUE = "emPAI value";
	private static final String NSAF = "normalized spectral abundance factor";
	private static final String NSAF_NORM = "NSAF_norm";
	private static final String SPC_BY_LEN_RATIO = "SPC/Length ratio";
	private static final String PEPTIDE_PSM_COUNT = "peptide PSM count";

	public IdentifiedProteinImplFromIdParser(Protein dtaSelectProtein, ControlVocabularyManager cvManager) {
		this.idProtein = dtaSelectProtein;
		id = getRandomInt();
		this.cvManager = cvManager;
	}

	private int getRandomInt() {
		final Random generator = new Random();
		final int i = generator.nextInt(Integer.MAX_VALUE);
		return i;
	}

	@Override
	public int getId() {

		return id;
	}

	@Override
	public String getAccession() {
		return idProtein.getAccession();
	}

	@Override
	public String getDescription() {

		final String description = FastaParser.getDescription(idProtein.getDescription());
		if (description != null)
			return description;
		return idProtein.getDescription();
	}

	@Override
	public Set<ProteinScore> getScores() {
		if (proteinScores == null) {
			proteinScores = new THashSet<ProteinScore>();
			if (idProtein.getEmpai() != null) {

				final ProteinScore score = new org.proteored.miapeapi.xml.xtandem.msi.ProteinScoreImpl(EMPAI_VALUE,
						idProtein.getEmpai());
				proteinScores.add(score);
			}
			if (idProtein.getNsaf() != null) {
				final ProteinScore score = new org.proteored.miapeapi.xml.xtandem.msi.ProteinScoreImpl(NSAF,
						idProtein.getNsaf());
				proteinScores.add(score);
			}
			// if (dtaSelectProtein.getNsaf_norm() != null) {
			final ProteinScore NSAF_norm = new org.proteored.miapeapi.xml.xtandem.msi.ProteinScoreImpl(NSAF_NORM,
					idProtein.getNsaf_norm());
			proteinScores.add(NSAF_norm);
			// }

			final ProteinScore ratio = new org.proteored.miapeapi.xml.xtandem.msi.ProteinScoreImpl(SPC_BY_LEN_RATIO,
					getRatio(idProtein));
			proteinScores.add(ratio);

			if (idProtein.getSpectrumCount() != null) {

				final ProteinScore score = new org.proteored.miapeapi.xml.xtandem.msi.ProteinScoreImpl(
						PEPTIDE_PSM_COUNT, idProtein.getSpectrumCount());
				proteinScores.add(score);
			}
		}
		return proteinScores;
	}

	/**
	 * SPC / LENGTH
	 *
	 * @return
	 */
	public double getRatio(Protein protein) {
		if (protein.getSpectrumCount() != null && protein.getLength() != null && protein.getLength() > 0)
			return protein.getSpectrumCount() / protein.getLength();
		return Double.NaN;
	}

	@Override
	public String getPeptideNumber() {
		final List<IdentifiedPeptide> identifiedPeptides = getIdentifiedPeptides();
		final Set<String> seqs = new THashSet<String>();
		if (identifiedPeptides != null) {
			for (final IdentifiedPeptide pep : identifiedPeptides) {
				seqs.add(pep.getSequence());
			}
		}
		return String.valueOf(seqs.size());
	}

	@Override
	public String getCoverage() {
		return String.valueOf(idProtein.getCoverage());
	}

	@Override
	public String getPeaksMatchedNumber() {
		return null;
	}

	@Override
	public String getUnmatchedSignals() {
		return null;
	}

	@Override
	public String getAdditionalInformation() {
		return null;
	}

	@Override
	public Boolean getValidationStatus() {
		return null;
	}

	@Override
	public String getValidationType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getValidationValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<IdentifiedPeptide> getIdentifiedPeptides() {
		if (peptides == null) {
			peptides = new ArrayList<IdentifiedPeptide>();
			final List<PSM> psMs = idProtein.getPSMs();
			if (psMs != null) {
				for (final PSM dtaSelectPSM : psMs) {
					if (psmMapByPSMId.containsKey(dtaSelectPSM.getIdentifier())) {
						final IdentifiedPeptide pep = psmMapByPSMId.get(dtaSelectPSM.getIdentifier());
						pep.getIdentifiedProteins().add(this);
						peptides.add(pep);
					} else {
						final IdentifiedPeptideImplFromIdParser pep = new IdentifiedPeptideImplFromIdParser(
								dtaSelectPSM, cvManager);
						psmMapByPSMId.put(dtaSelectPSM.getIdentifier(), pep);
						pep.getIdentifiedProteins().add(this);
						peptides.add(pep);
					}

				}
			}
		}
		return peptides;
	}

}
