package org.proteored.miapeapi.experiment.model.filters;

import java.util.ArrayList;
import java.util.List;

import org.proteored.miapeapi.experiment.model.ExtendedIdentifiedPeptide;
import org.proteored.miapeapi.experiment.model.IdentificationSet;
import org.proteored.miapeapi.experiment.model.ProteinGroup;
import org.proteored.miapeapi.interfaces.Software;

public class PeptideNumberFilter implements Filter {
	private final int minNumPeptides;
	private final Software software;

	public PeptideNumberFilter(int min, Software software) {
		this.minNumPeptides = min;
		this.software = software;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof PeptideNumberFilter) {
			PeptideNumberFilter filter = (PeptideNumberFilter) obj;
			if (filter != null) {
				if (filter.minNumPeptides == this.minNumPeptides)
					return true;
				else
					return false;
			}
		}

		return super.equals(obj);
	}

	@Override
	public List<ProteinGroup> filter(List<ProteinGroup> proteinGroups,
			IdentificationSet currentIdSet) {
		List<ProteinGroup> ret = new ArrayList<ProteinGroup>();
		for (ProteinGroup proteinGroup : proteinGroups) {
			final List<ExtendedIdentifiedPeptide> peptides = proteinGroup
					.getPeptides();
			if (peptides != null && peptides.size() >= minNumPeptides)
				ret.add(proteinGroup);
		}
		return ret;
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
		return "Proteins having at least " + this.minNumPeptides + " peptides";
	}

	@Override
	public Software getSoftware() {
		return software;
	}
}
