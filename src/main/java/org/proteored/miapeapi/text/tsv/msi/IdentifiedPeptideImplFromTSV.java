package org.proteored.miapeapi.text.tsv.msi;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.proteored.miapeapi.interfaces.msi.IdentifiedPeptide;
import org.proteored.miapeapi.interfaces.msi.IdentifiedProtein;
import org.proteored.miapeapi.interfaces.msi.InputData;
import org.proteored.miapeapi.interfaces.msi.PeptideModification;
import org.proteored.miapeapi.interfaces.msi.PeptideScore;
import org.proteored.miapeapi.util.ModificationMapping;

import com.compomics.util.protein.AASequenceImpl;

import gnu.trove.set.hash.THashSet;

public class IdentifiedPeptideImplFromTSV implements IdentifiedPeptide {
	private final String sequence;
	private final int id;
	private final List<IdentifiedProtein> proteins = new ArrayList<IdentifiedProtein>();
	private final Set<PeptideScore> scores = new THashSet<PeptideScore>();
	private final Set<PeptideModification> modifications = new THashSet<PeptideModification>();
	private String charge;
	private Double precursorMZ;
	private String retentionTime;
	private static Integer seed;

	public IdentifiedPeptideImplFromTSV(String seq) {
		sequence = seq.toUpperCase();
		id = getRandomInt();
	}

	public void addProtein(IdentifiedProtein protein) {
		proteins.add(protein);
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

		final Set<PeptideModification> modifications2 = getModifications();

		final AASequenceImpl seq = ModificationMapping.getAASequenceImpl(getSequence(), modifications2);
		try {
			final int z = Integer.valueOf(getCharge());
			final double mz = seq.getMz(z);
			return mz;
		} catch (final NumberFormatException e) {

		} catch (final Exception e) {

		}
		return null;
	}

	public void setPrecursorMZ(Double mz) {
		precursorMZ = mz;
	}

	@Override
	public String getSpectrumRef() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputData getInputData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getRank() {
		// TODO Auto-generated method stub
		return 0;
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
		scores.add(peptideScore);

	}

	public void addModification(PeptideModification modification) {
		modifications.add(modification);
	}

	public void setRetentionTime(Double rt) {
		if (rt != null) {
			retentionTime = String.valueOf(rt);
		}
	}

}
