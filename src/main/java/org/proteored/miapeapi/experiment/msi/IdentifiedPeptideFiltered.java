package org.proteored.miapeapi.experiment.msi;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.proteored.miapeapi.experiment.model.ExtendedIdentifiedPeptide;
import org.proteored.miapeapi.experiment.model.ExtendedIdentifiedProtein;
import org.proteored.miapeapi.interfaces.msi.IdentifiedPeptide;
import org.proteored.miapeapi.interfaces.msi.IdentifiedProtein;
import org.proteored.miapeapi.interfaces.msi.InputData;
import org.proteored.miapeapi.interfaces.msi.PeptideModification;
import org.proteored.miapeapi.interfaces.msi.PeptideScore;

public class IdentifiedPeptideFiltered implements IdentifiedPeptide {

	private final List<IdentifiedProtein> identifiedProteins = new ArrayList<IdentifiedProtein>();
	private final int rank;
	private final InputData inputData;
	private final String spectrumRef;
	private final String massDesviation;
	private final String charge;
	private final Set<PeptideModification> modifications;
	private final Set<PeptideScore> scores;
	private final String sequence;
	private final int id;
	private final String retentionTimeInSeconds;
	private final Set<Integer> filteredProteinIds = new HashSet<Integer>();

	public IdentifiedPeptideFiltered(ExtendedIdentifiedPeptide peptide) {
		this.charge = peptide.getCharge();
		this.id = peptide.getId();
		this.inputData = peptide.getInputData();
		this.massDesviation = peptide.getMassDesviation();
		Set<PeptideModification> modifications2 = peptide.getModifications();
		if (modifications2 != null) {
			this.modifications = new HashSet<PeptideModification>();
			this.modifications.addAll(modifications2);
		} else {
			this.modifications = null;
		}
		this.rank = peptide.getRank();
		this.retentionTimeInSeconds = peptide.getRetentionTimeInSeconds();
		if (peptide.getScores() != null) {
			this.scores = new HashSet<PeptideScore>();
			this.scores.addAll(peptide.getScores());
		} else {
			this.scores = null;
		}
		this.sequence = peptide.getSequence();
		this.spectrumRef = peptide.getSpectrumRef();
		List<ExtendedIdentifiedProtein> proteins = peptide.getProteins();
		if (proteins != null) {
			for (ExtendedIdentifiedProtein extendedIdentifiedProtein : proteins) {
				filteredProteinIds.add(extendedIdentifiedProtein.getId());
			}
		}
	}

	public Set<Integer> getFilteredProteinIds() {
		return filteredProteinIds;
	}

	public void addProtein(IdentifiedProtein proteinToAdd) {
		for (IdentifiedProtein protein : identifiedProteins) {
			if (proteinToAdd.getId() == protein.getId())
				return;
		}
		this.identifiedProteins.add(proteinToAdd);
	}

	@Override
	public int getId() {
		return this.id;
	}

	@Override
	public String getSequence() {
		return this.sequence;
	}

	@Override
	public Set<PeptideScore> getScores() {
		return this.scores;
	}

	@Override
	public Set<PeptideModification> getModifications() {
		return this.modifications;
	}

	@Override
	public String getCharge() {
		return this.charge;
	}

	@Override
	public String getMassDesviation() {
		return this.massDesviation;
	}

	@Override
	public String getSpectrumRef() {
		return this.spectrumRef;
	}

	@Override
	public InputData getInputData() {
		return this.inputData;
	}

	@Override
	public int getRank() {
		return this.rank;
	}

	@Override
	public List<IdentifiedProtein> getIdentifiedProteins() {
		return this.identifiedProteins;
	}

	@Override
	public String getRetentionTimeInSeconds() {
		return this.retentionTimeInSeconds;
	}

}
