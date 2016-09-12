package org.proteored.miapeapi.experiment.model.filters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.proteored.miapeapi.experiment.model.ExtendedIdentifiedPeptide;
import org.proteored.miapeapi.experiment.model.IdentificationSet;
import org.proteored.miapeapi.experiment.model.ProteinGroup;
import org.proteored.miapeapi.experiment.model.datamanager.DataManager;
import org.proteored.miapeapi.interfaces.Software;

public class PeptideSequenceFilter implements Filter {
	private static final Logger log = Logger
			.getLogger("log4j.logger.org.proteored");

	private final HashSet<String> sequences = new HashSet<String>();
	private final List<String> sortedSequences = new ArrayList<String>();
	private final boolean distinguisModificatedPeptides;
	private final Software software;

	public PeptideSequenceFilter(Collection<String> sequences,
			boolean distinguisModificatedPeptides, Software software) {
		this.distinguisModificatedPeptides = distinguisModificatedPeptides;
		this.software = software;

		for (String sequence : sequences) {
			this.sequences.add(sequence);
			this.sortedSequences.add(sequence);
		}
	}

	@Override
	public Software getSoftware() {
		return software;
	}

	@Override
	public List<ProteinGroup> filter(List<ProteinGroup> proteinGroups,
			IdentificationSet currentIdSet) {
		List<ExtendedIdentifiedPeptide> identifiedPeptides = DataManager
				.getPeptidesFromProteinGroupsInParallel(proteinGroups);
		Set<Integer> filteredPeptides = filterPeptides(identifiedPeptides,
				currentIdSet);
		return DataManager.filterProteinGroupsByPeptides(proteinGroups,
				filteredPeptides, currentIdSet.getCvManager());
	}

	private Set<Integer> filterPeptides(
			List<ExtendedIdentifiedPeptide> identifiedPeptides,
			IdentificationSet currentIdSet) {

		log.info("Filtering by peptide " + this.sequences.size()
				+ " sequences: " + this);
		Set<Integer> ret = new HashSet<Integer>();
		if (identifiedPeptides != null && !identifiedPeptides.isEmpty())
			for (ExtendedIdentifiedPeptide peptide : identifiedPeptides) {
				if (peptide != null) {
					String sequence = null;
					if (!distinguisModificatedPeptides) {
						sequence = peptide.getSequence();
					} else {
						sequence = peptide.getModificationString();
					}
					if (sequence != null) {
						if (this.sequences.contains(sequence)) {
							if (!ret.contains(peptide.getId()))
								ret.add(peptide.getId());
							else
								log.info("This peptide has passed already the threshold");
						}
					}

				}

			}
		log.info("Filtered " + ret.size() + " out of "
				+ identifiedPeptides.size() + " peptides");
		return ret;

	}

	@Override
	public String toString() {

		return "Peptides filtered by a list of " + this.sequences.size()
				+ " peptide sequences";

	}

	@Override
	public boolean appliedToProteins() {
		return false;
	}

	@Override
	public boolean appliedToPeptides() {
		return true;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof PeptideSequenceFilter) {
			PeptideSequenceFilter filter = (PeptideSequenceFilter) obj;
			if (this.sequences.size() == filter.sequences.size())
				if ((this.distinguisModificatedPeptides && filter.distinguisModificatedPeptides)
						|| (!this.distinguisModificatedPeptides && !filter.distinguisModificatedPeptides))
					return true;

			return false;
		} else
			return super.equals(obj);
	}

	public List<String> getSortedSequences() {
		return this.sortedSequences;
	}
}
