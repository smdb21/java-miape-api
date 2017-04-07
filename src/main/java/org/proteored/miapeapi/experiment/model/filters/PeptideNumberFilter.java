package org.proteored.miapeapi.experiment.model.filters;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.proteored.miapeapi.experiment.model.ExtendedIdentifiedPeptide;
import org.proteored.miapeapi.experiment.model.IdentificationSet;
import org.proteored.miapeapi.experiment.model.ProteinGroup;
import org.proteored.miapeapi.interfaces.Software;

public class PeptideNumberFilter implements Filter {
	private final int minNumPeptides;
	private final Software software;
	private final boolean distinguishSequence;

	public PeptideNumberFilter(int min, boolean distinguishSequence, Software software) {
		minNumPeptides = min;
		this.software = software;
		this.distinguishSequence = distinguishSequence;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof PeptideNumberFilter) {
			PeptideNumberFilter filter = (PeptideNumberFilter) obj;
			if (filter != null) {
				if (filter.minNumPeptides == minNumPeptides) {
					return true;
				} else {
					return false;
				}
			}
		}

		return super.equals(obj);
	}

	@Override
	public List<ProteinGroup> filter(List<ProteinGroup> proteinGroups, IdentificationSet currentIdSet) {
		List<ProteinGroup> ret = new ArrayList<ProteinGroup>();
		for (ProteinGroup proteinGroup : proteinGroups) {
			final List<ExtendedIdentifiedPeptide> peptides = proteinGroup.getPeptides();
			int numPeptides = getNumPeptides(peptides, distinguishSequence);
			if (numPeptides >= minNumPeptides) {
				ret.add(proteinGroup);
			}
		}
		return ret;
	}

	private int getNumPeptides(List<ExtendedIdentifiedPeptide> peptides, boolean distinguishSequence2) {
		if (!distinguishSequence2) {
			return peptides.size();
		} else {
			Set<String> seqs = new HashSet<String>();
			for (ExtendedIdentifiedPeptide extendedIdentifiedPeptide : peptides) {
				seqs.add(extendedIdentifiedPeptide.getSequence());
			}
			return seqs.size();
		}
	}

	@Override
	public boolean appliedToProteins() {
		return true;
	}

	@Override
	public boolean appliedToPeptides() {
		return false;
	}

	@Override
	public String toString() {
		return "Proteins having at least " + minNumPeptides + " peptides";
	}

	@Override
	public Software getSoftware() {
		return software;
	}
}
