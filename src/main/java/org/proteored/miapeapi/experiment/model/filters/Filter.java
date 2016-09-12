package org.proteored.miapeapi.experiment.model.filters;

import java.util.List;

import org.proteored.miapeapi.experiment.model.IdentificationSet;
import org.proteored.miapeapi.experiment.model.ProteinGroup;
import org.proteored.miapeapi.interfaces.Software;

public interface Filter {

	/**
	 * Gets the software information that has applied this filter
	 * 
	 * @return
	 */
	public Software getSoftware();

	/**
	 * 
	 * @param proteinGroups
	 * @param currentIdSet
	 * @param peptidesToIncludeAfterFilter
	 *            list of
	 * @return
	 */
	public List<ProteinGroup> filter(List<ProteinGroup> proteinGroups,
			IdentificationSet currentIdSet);

	// public List<IdentifiedProtein>
	// filterProteins(Set<List<IdentifiedProtein>> identifiedProteins);

	// public List<ExtendedIdentifiedPeptide> filterPeptides(
	// List<ExtendedIdentifiedPeptide> identifiedPeptides, IdentificationSet
	// currentIdSet);

	// public List<ExtendedIdentifiedPeptide> filterPeptides(
	// Set<List<ExtendedIdentifiedPeptide>> identifiedPeptides);

	public boolean appliedToProteins();

	public boolean appliedToPeptides();

}
