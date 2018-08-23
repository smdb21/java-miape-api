package org.proteored.miapeapi.text.tsv.msi;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.proteored.miapeapi.interfaces.msi.IdentifiedPeptide;
import org.proteored.miapeapi.interfaces.msi.IdentifiedProtein;
import org.proteored.miapeapi.interfaces.msi.ProteinScore;

public class IdentifiedProteinImplFromTSV implements IdentifiedProtein {
	private final String acc;
	private final int id;
	private final List<IdentifiedPeptide> peptides = new ArrayList<IdentifiedPeptide>();
	private final String description;
	private static Integer seed;

	public IdentifiedProteinImplFromTSV(String acc) {
		this.acc = acc;
		description = null;
		id = getRandomInt();
	}

	public IdentifiedProteinImplFromTSV(String acc, String description) {
		this.acc = acc;
		this.description = description;
		id = getRandomInt();
	}

	public void addPeptide(IdentifiedPeptide peptide) {
		peptides.add(peptide);
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
	public String getAccession() {
		return acc;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public Set<ProteinScore> getScores() {
		// TODO Auto-generated method stub
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

}
