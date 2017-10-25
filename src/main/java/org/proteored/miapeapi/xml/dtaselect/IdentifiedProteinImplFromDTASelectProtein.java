package org.proteored.miapeapi.xml.dtaselect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.interfaces.msi.IdentifiedPeptide;
import org.proteored.miapeapi.interfaces.msi.IdentifiedProtein;
import org.proteored.miapeapi.interfaces.msi.ProteinScore;

import edu.scripps.yates.dtaselectparser.util.DTASelectPSM;
import edu.scripps.yates.dtaselectparser.util.DTASelectProtein;
import edu.scripps.yates.utilities.fasta.FastaParser;
import edu.scripps.yates.utilities.util.Pair;
import gnu.trove.set.hash.THashSet;

public class IdentifiedProteinImplFromDTASelectProtein implements IdentifiedProtein {
	private final DTASelectProtein dtaSelectProtein;
	private final int id;
	private Set<ProteinScore> proteinScores;
	private List<IdentifiedPeptide> peptides;
	private final ControlVocabularyManager cvManager;
	public static final Map<String, IdentifiedPeptide> psmMapByPSMId = new HashMap<String, IdentifiedPeptide>();

	public IdentifiedProteinImplFromDTASelectProtein(DTASelectProtein dtaSelectProtein,
			ControlVocabularyManager cvManager) {
		this.dtaSelectProtein = dtaSelectProtein;
		id = getRandomInt();
		this.cvManager = cvManager;
	}

	private int getRandomInt() {
		Random generator = new Random();
		int i = generator.nextInt(Integer.MAX_VALUE);
		return i;
	}

	@Override
	public int getId() {

		return id;
	}

	@Override
	public String getAccession() {
		final Pair<String, String> acc = FastaParser.getACC(dtaSelectProtein.getLocus());
		if (acc != null)
			return acc.getFirstelement();
		return dtaSelectProtein.getLocus();
	}

	@Override
	public String getDescription() {

		final String description = FastaParser.getDescription(dtaSelectProtein.getDescription());
		if (description != null)
			return description;
		return dtaSelectProtein.getDescription();
	}

	@Override
	public Set<ProteinScore> getScores() {
		if (proteinScores == null) {
			proteinScores = new THashSet<ProteinScore>();
			if (dtaSelectProtein.getEmpai() != null) {
				ProteinScore score = new org.proteored.miapeapi.xml.xtandem.msi.ProteinScoreImpl("emPAI value",
						dtaSelectProtein.getEmpai());
				proteinScores.add(score);
			}
			if (dtaSelectProtein.getNsaf() != null) {
				ProteinScore score = new org.proteored.miapeapi.xml.xtandem.msi.ProteinScoreImpl(
						"normalized spectral abundance factor", dtaSelectProtein.getNsaf());
				proteinScores.add(score);
			}
			// if (dtaSelectProtein.getNsaf_norm() != null) {
			ProteinScore NSAF_norm = new org.proteored.miapeapi.xml.xtandem.msi.ProteinScoreImpl("NSAF_norm",
					dtaSelectProtein.getNsaf_norm());
			proteinScores.add(NSAF_norm);
			// }

			ProteinScore ratio = new org.proteored.miapeapi.xml.xtandem.msi.ProteinScoreImpl("SPC/Length ratio",
					dtaSelectProtein.getRatio());
			proteinScores.add(ratio);

			if (dtaSelectProtein.getSpectrumCount() != null) {
				ProteinScore score = new org.proteored.miapeapi.xml.xtandem.msi.ProteinScoreImpl("peptide PSM count",
						dtaSelectProtein.getSpectrumCount());
				proteinScores.add(score);
			}
		}
		return proteinScores;
	}

	@Override
	public String getPeptideNumber() {
		final List<IdentifiedPeptide> identifiedPeptides = getIdentifiedPeptides();
		Set<String> seqs = new THashSet<String>();
		if (identifiedPeptides != null) {
			for (IdentifiedPeptide pep : identifiedPeptides) {
				seqs.add(pep.getSequence());
			}
		}
		return String.valueOf(seqs.size());
	}

	@Override
	public String getCoverage() {
		return String.valueOf(dtaSelectProtein.getCoverage());
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
			final List<DTASelectPSM> psMs = dtaSelectProtein.getPSMs();
			if (psMs != null) {
				for (DTASelectPSM dtaSelectPSM : psMs) {
					if (psmMapByPSMId.containsKey(dtaSelectPSM.getPsmIdentifier())) {
						final IdentifiedPeptide pep = psmMapByPSMId.get(dtaSelectPSM.getPsmIdentifier());
						pep.getIdentifiedProteins().add(this);
						peptides.add(pep);
					} else {
						final IdentifiedPeptideImplFromDTASelect pep = new IdentifiedPeptideImplFromDTASelect(
								dtaSelectPSM, cvManager);
						psmMapByPSMId.put(dtaSelectPSM.getPsmIdentifier(), pep);
						pep.getIdentifiedProteins().add(this);
						peptides.add(pep);
					}

				}
			}
		}
		return peptides;
	}

}
