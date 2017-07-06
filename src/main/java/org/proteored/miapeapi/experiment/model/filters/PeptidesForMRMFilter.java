package org.proteored.miapeapi.experiment.model.filters;

import java.util.List;

import org.apache.log4j.Logger;
import org.proteored.miapeapi.experiment.model.ExtendedIdentifiedPeptide;
import org.proteored.miapeapi.experiment.model.IdentificationSet;
import org.proteored.miapeapi.experiment.model.ProteinGroup;
import org.proteored.miapeapi.experiment.model.datamanager.DataManager;
import org.proteored.miapeapi.interfaces.Software;

import gnu.trove.set.hash.TIntHashSet;

public class PeptidesForMRMFilter implements Filter {
	private static Logger log = Logger.getLogger("log4j.logger.org.proteored");

	private final boolean ignoreM;
	private final boolean ignoreW;
	private final boolean ignoreQAtBeginning;
	private final boolean ignoreMissedCleavages;
	private final Integer minLength;
	private final Integer maxLength;
	private final boolean requireUnique;
	private final Software software;

	public PeptidesForMRMFilter(boolean ignoreM, boolean ignoreW, boolean ignoreQAtBeginning,
			boolean ignoreMissedCleavages, Integer minLength, Integer maxLength, boolean requireUnique,
			Software software) {
		this.ignoreM = ignoreM;
		this.ignoreMissedCleavages = ignoreMissedCleavages;
		this.ignoreQAtBeginning = ignoreQAtBeginning;
		this.ignoreW = ignoreW;
		this.minLength = minLength;
		this.maxLength = maxLength;
		this.requireUnique = requireUnique;
		this.software = software;
	}

	@Override
	public Software getSoftware() {
		return software;
	}

	@Override
	public List<ProteinGroup> filter(List<ProteinGroup> identifiedProteins, IdentificationSet currentIdSet) {
		List<ExtendedIdentifiedPeptide> identifiedPeptides = DataManager
				.getPeptidesFromProteinGroupsInParallel(identifiedProteins);
		TIntHashSet filteredPeptides = filterPeptides(identifiedPeptides, currentIdSet);
		return DataManager.filterProteinGroupsByPeptides(identifiedProteins, filteredPeptides,
				currentIdSet.getCvManager());
	}

	private TIntHashSet filterPeptides(List<ExtendedIdentifiedPeptide> identifiedPeptides,
			IdentificationSet currentIdSet) {
		log.info("Filtering " + identifiedPeptides.size() + " peptides by MRM criteria.");

		TIntHashSet ret = new TIntHashSet();
		for (ExtendedIdentifiedPeptide extendedIdentifiedPeptide : identifiedPeptides) {

			String sequence = extendedIdentifiedPeptide.getSequence();
			if (this.ignoreM) {
				if (sequence.contains("M") || sequence.contains("m")) {
					continue;
				}
			}
			if (this.ignoreMissedCleavages) {
				if (extendedIdentifiedPeptide.getNumMissedcleavages() > 0)
					continue;
			}
			if (this.ignoreQAtBeginning) {
				if (sequence.startsWith("Q") || sequence.startsWith("q"))
					continue;
			}
			if (this.ignoreW) {
				if (sequence.contains("W") || sequence.contains("w")) {
					continue;
				}
			}
			if (this.minLength != null) {
				if (sequence.length() < this.minLength)
					continue;
			}
			if (this.maxLength != null) {
				if (sequence.length() > this.maxLength)
					continue;
			}
			if (this.requireUnique) {
				if (extendedIdentifiedPeptide.getIdentifiedProteins().size() > 1)
					continue;
			}
			// if the program is here it is because the peptide is valid
			if (!ret.contains(extendedIdentifiedPeptide.getId())) {
				ret.add(extendedIdentifiedPeptide.getId());
			}

		}
		log.info("Filtered " + ret.size() + " out of " + identifiedPeptides.size() + " peptides");

		return ret;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj instanceof PeptidesForMRMFilter) {
			PeptidesForMRMFilter filter = (PeptidesForMRMFilter) obj;
			if (areNotEqual(this.ignoreM, filter.ignoreM))
				return false;
			if (areNotEqual(this.ignoreMissedCleavages, filter.ignoreMissedCleavages))
				return false;
			if (areNotEqual(this.ignoreQAtBeginning, filter.ignoreQAtBeginning))
				return false;
			if (areNotEqual(this.ignoreW, filter.ignoreW))
				return false;
			if (areNotEqual(this.requireUnique, filter.requireUnique))
				return false;
			if (this.minLength != null && filter.minLength != null && this.minLength != filter.minLength)
				return false;
			if (this.maxLength != null && filter.maxLength != null && this.maxLength != filter.maxLength)
				return false;
			return true;
		} else
			return super.equals(obj);
	}

	private boolean areNotEqual(boolean b1, boolean b2) {
		if (b1 && b2)
			return false;
		if (!b1 && !b2)
			return false;
		return true;
	}

	@Override
	public String toString() {
		String ret = "Peptides";
		if (this.ignoreMissedCleavages)
			ret = ret + "\n no missed-cleavages";

		if (this.ignoreM)
			ret = ret + "\n ignore M";

		if (this.ignoreQAtBeginning)
			ret = ret + "\n ignore Q at first position";

		if (this.ignoreW)
			ret = ret + "\n ignore W";
		if (this.requireUnique)
			ret = ret + "\n require unique peptide";
		if (this.minLength != null)
			ret = ret + "\n min length = " + this.minLength;
		if (this.maxLength != null)
			ret = ret + "\n max length = " + this.maxLength;

		return ret;

	}

	@Override
	public boolean appliedToProteins() {
		return false;
	}

	@Override
	public boolean appliedToPeptides() {
		return true;
	}

}
