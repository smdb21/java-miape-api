package org.proteored.miapeapi.experiment.msi;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.proteored.miapeapi.experiment.model.ExtendedIdentifiedPeptide;
import org.proteored.miapeapi.experiment.model.ExtendedIdentifiedProtein;
import org.proteored.miapeapi.interfaces.msi.IdentifiedPeptide;
import org.proteored.miapeapi.interfaces.msi.IdentifiedProtein;
import org.proteored.miapeapi.interfaces.msi.ProteinScore;

import gnu.trove.set.hash.THashSet;
import gnu.trove.set.hash.TIntHashSet;

public class IdentifiedProteinFiltered implements IdentifiedProtein {

	private final List<IdentifiedPeptide> identifiedPeptides = new ArrayList<IdentifiedPeptide>();
	private final String validationValue;
	private final String validationType;
	private final Boolean validationStatus;
	private final String additionalInformation;
	private final String unmatchedSignals;
	private final String peaksMatchedNumber;
	private final String coverage;
	private final String peptideNumber;
	private final Set<ProteinScore> scores;
	private final String description;
	private final String accession;
	private final int id;
	private final TIntHashSet validPeptideIds = new TIntHashSet();

	public IdentifiedProteinFiltered(ExtendedIdentifiedProtein protein) {
		this.id = protein.getId();
		this.validationValue = protein.getValidationValue();
		this.accession = protein.getAccession();
		this.additionalInformation = protein.getAdditionalInformation();
		this.coverage = this.getCoverage();
		this.description = protein.getDescription();
		this.peaksMatchedNumber = protein.getPeaksMatchedNumber();
		this.peptideNumber = protein.getPeptideNumber();
		this.unmatchedSignals = protein.getUnmatchedSignals();
		this.validationStatus = protein.getValidationStatus();
		this.validationType = protein.getValidationType();
		if (protein.getScores() != null) {
			this.scores = new THashSet<ProteinScore>();
			this.scores.addAll(protein.getScores());
		} else {
			this.scores = null;
		}
		List<ExtendedIdentifiedPeptide> peptides = protein.getPeptides();
		if (peptides != null) {
			for (ExtendedIdentifiedPeptide extendedIdentifiedPeptide : peptides) {
				int id2 = extendedIdentifiedPeptide.getId();
				validPeptideIds.add(id2);
			}
		}
	}

	public TIntHashSet getValidPeptideIds() {
		return validPeptideIds;
	}

	public void addPeptide(IdentifiedPeptide peptideToAdd) {
		for (IdentifiedPeptide peptide : this.identifiedPeptides) {
			if (peptide.getId() == peptideToAdd.getId())
				return;
		}
		this.identifiedPeptides.add(peptideToAdd);
	}

	@Override
	public int getId() {
		return this.id;
	}

	@Override
	public String getAccession() {
		return this.accession;
	}

	@Override
	public String getDescription() {
		return this.description;
	}

	@Override
	public Set<ProteinScore> getScores() {
		return this.scores;
	}

	@Override
	public String getPeptideNumber() {
		return this.peptideNumber;
	}

	@Override
	public String getCoverage() {
		return this.coverage;
	}

	@Override
	public String getPeaksMatchedNumber() {
		return this.peaksMatchedNumber;
	}

	@Override
	public String getUnmatchedSignals() {
		return this.unmatchedSignals;
	}

	@Override
	public String getAdditionalInformation() {
		return this.additionalInformation;
	}

	@Override
	public Boolean getValidationStatus() {
		return this.validationStatus;
	}

	@Override
	public String getValidationType() {
		return this.validationType;
	}

	@Override
	public String getValidationValue() {
		return this.validationValue;
	}

	@Override
	public List<IdentifiedPeptide> getIdentifiedPeptides() {
		return this.identifiedPeptides;
	}

}
