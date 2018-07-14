package org.proteored.miapeapi.text.proteinpilot.msi;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.proteored.miapeapi.interfaces.msi.IdentifiedPeptide;
import org.proteored.miapeapi.interfaces.msi.IdentifiedProtein;
import org.proteored.miapeapi.interfaces.msi.InputData;
import org.proteored.miapeapi.interfaces.msi.PeptideModification;
import org.proteored.miapeapi.interfaces.msi.PeptideScore;

import gnu.trove.set.hash.THashSet;

public class IdentifiedPeptideImplFromProteinPilot implements IdentifiedPeptide {
	private final String sequence;
	private final int id;
	private final List<IdentifiedProtein> proteins = new ArrayList<IdentifiedProtein>();
	private final Set<PeptideScore> scores = new THashSet<PeptideScore>();
	private final Set<PeptideModification> modifications = new THashSet<PeptideModification>();
	private String charge;
	private Double precursorMZ;
	private String retentionTime;
	private Double theorMZ;
	private String spectrumRef;

	public IdentifiedPeptideImplFromProteinPilot(String seq) {
		sequence = seq.toUpperCase();
		id = getRandomInt();
	}

	public void addProtein(IdentifiedProtein protein) {
		if (!proteins.contains(protein)) {
			proteins.add(protein);
		}
	}

	private int getRandomInt() {
		// Random generator = new Random();
		// int i = generator.nextInt(Integer.MAX_VALUE);
		// return i;
		return hashCode();
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public String getSequence() {
		return sequence;
	}

	@Override
	public Set<PeptideScore> getScores() {
		return scores;
	}

	@Override
	public Set<PeptideModification> getModifications() {
		return modifications;
	}

	@Override
	public String getCharge() {
		return charge;
	}

	public void setCharge(String charge) {
		this.charge = charge;
	}

	@Override
	public String getMassDesviation() {
		final Double theoreticalMZ = getTheoreticalMZ();
		if (theoreticalMZ != null && precursorMZ != null) {
			return String.valueOf(theoreticalMZ - precursorMZ);
		}
		return null;
	}

	private Double getTheoreticalMZ() {

		return theorMZ;
	}

	public void setTheorMZ(Double mz) {
		theorMZ = mz;
	}

	public void setPrecursorMZ(Double mz) {
		precursorMZ = mz;
	}

	@Override
	public String getSpectrumRef() {
		return spectrumRef;
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
		return retentionTime;
	}

	public void addScore(PeptideScore peptideScore) {
		// just if not repeated
		final String scoreName = peptideScore.getName();
		for (final PeptideScore score : scores) {
			if (score.getName().equals(scoreName)) {
				return;
			}
		}
		scores.add(peptideScore);

	}

	public void addModification(PeptideModification modification) {
		// just if it is not repeated
		final int position = modification.getPosition();
		for (final PeptideModification peptideModification : modifications) {
			if (peptideModification.getPosition() == position) {
				return;
			}
		}
		modifications.add(modification);
	}

	public void setRetentionTime(Double rt) {
		if (rt != null) {
			retentionTime = String.valueOf(rt);
		}
	}

	public void setSpectrumRef(String ref) {
		spectrumRef = ref;
	}
}
