package org.proteored.miapeapi.xml.xtandem.msi;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularyTerm;
import org.proteored.miapeapi.cv.msi.Score;
import org.proteored.miapeapi.interfaces.msi.IdentifiedPeptide;
import org.proteored.miapeapi.interfaces.msi.IdentifiedProtein;
import org.proteored.miapeapi.interfaces.msi.ProteinScore;

import de.proteinms.xtandemparser.xtandem.ModificationMap;
import de.proteinms.xtandemparser.xtandem.Peptide;
import de.proteinms.xtandemparser.xtandem.Protein;
import gnu.trove.set.hash.THashSet;

public class IdentifiedProteinImpl implements IdentifiedProtein {
	private final Protein xmlProtein;
	private final Set<ProteinScore> proteinScores = new THashSet<ProteinScore>();
	private final Integer identifier;
	private List<Peptide> peptideList;
	private final ModificationMap modificationsMap;
	private final ControlVocabularyManager cvManager;
	private List<IdentifiedPeptide> peptides;

	public IdentifiedProteinImpl(Protein xTandemProtein, Integer identifier, ModificationMap modificationsMap,
			ControlVocabularyManager cvManager) {
		xmlProtein = xTandemProtein;
		this.identifier = identifier;
		// this.peptideList = list;
		this.modificationsMap = modificationsMap;
		this.cvManager = cvManager;
		processScores();
	}

	private void processScores() {
		if (xmlProtein != null) {
			final ControlVocabularyTerm xTandemExpectValueTerm = Score.getXTandemExpectValueTerm(cvManager);
			String name = "XTandem e-value";
			if (xTandemExpectValueTerm != null) {
				name = xTandemExpectValueTerm.getPreferredName();
			}
			final Double log10Evalue = xmlProtein.getExpectValue();
			if (log10Evalue != null) {
				double evalue = Math.pow(10, log10Evalue);

				proteinScores.add(new ProteinScoreImpl(name, evalue));
			}

			// this is not a score, is the sum of all of the fragment ions that
			// identify this protein
			// name = "XTandem summed score";
			// proteinScores.add(new ProteinScoreImpl(name, xmlProtein
			// .getSummedScore()));
		}

	}

	@Override
	public int getId() {
		if (identifier != null)
			return identifier;
		return -1;
	}

	@Override
	public String getAccession() {
		if (xmlProtein != null)
			return xmlProtein.getLabel();
		return null;
	}

	@Override
	public String getDescription() {
		if (xmlProtein != null) {

			return xmlProtein.getDescription();
		}
		return null;
	}

	@Override
	public Set<ProteinScore> getScores() {
		if (!proteinScores.isEmpty())
			return proteinScores;
		return null;
	}

	@Override
	public String getPeptideNumber() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCoverage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPeaksMatchedNumber() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUnmatchedSignals() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAdditionalInformation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean getValidationStatus() {
		// TODO Auto-generated method stub
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
		return peptides;
	}

	public void setPeptides(List<IdentifiedPeptide> peptides) {
		this.peptides = peptides;
	}

	public void addPeptide(IdentifiedPeptide identifiedPeptide) {
		if (peptides == null) {
			peptides = new ArrayList<IdentifiedPeptide>();
		}
		peptides.add(identifiedPeptide);

	}
}
