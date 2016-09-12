package org.proteored.miapeapi.experiment.model.filters;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.proteored.miapeapi.experiment.model.ExtendedIdentifiedPeptide;
import org.proteored.miapeapi.experiment.model.IdentificationSet;
import org.proteored.miapeapi.experiment.model.ProteinGroup;
import org.proteored.miapeapi.experiment.model.datamanager.DataManager;
import org.proteored.miapeapi.interfaces.Software;

public class PeptideLengthFilter implements Filter {
	private int minLenth = 0;
	private int maxLenth = Integer.MAX_VALUE;
	private final Software software;

	private static final Logger log = Logger
			.getLogger("log4j.logger.org.proteored");

	public static final String NOT_MODIFIED = "NOT MODIFIED";

	public void ModificationFilterItem() {

	}

	public PeptideLengthFilter(int min, int max, Software sofware) {
		this.minLenth = min;
		this.maxLenth = max;
		this.software = sofware;
	}

	public PeptideLengthFilter(int min, Software software) {
		this.minLenth = min;
		this.maxLenth = Integer.MAX_VALUE;
		this.software = software;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof PeptideLengthFilter) {
			PeptideLengthFilter filter = (PeptideLengthFilter) obj;
			if (this.minLenth == filter.minLenth
					&& this.maxLenth == filter.maxLenth)
				return true;

			return false;
		} else
			return super.equals(obj);
	}

	private Set<Integer> filterPeptides(
			List<ExtendedIdentifiedPeptide> identifiedPeptides,
			IdentificationSet currentIdSet) {

		log.info("Filtering " + identifiedPeptides.size()
				+ " peptides by peptide length: " + this);
		Set<Integer> ret = new HashSet<Integer>();
		if (identifiedPeptides != null && !identifiedPeptides.isEmpty())
			for (ExtendedIdentifiedPeptide peptide : identifiedPeptides) {
				if (peptide != null) {
					String sequence = peptide.getSequence();
					if (sequence != null) {
						if (sequence.length() >= minLenth
								&& sequence.length() <= this.maxLenth) {
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
		if (maxLenth < Integer.MAX_VALUE)
			return "Peptide length between " + minLenth + " < length < "
					+ maxLenth;
		else
			return "Peptide length > " + minLenth;
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

	@Override
	public boolean appliedToProteins() {

		return false;
	}

	@Override
	public boolean appliedToPeptides() {
		return true;
	}

	@Override
	public Software getSoftware() {
		return software;
	}
}
