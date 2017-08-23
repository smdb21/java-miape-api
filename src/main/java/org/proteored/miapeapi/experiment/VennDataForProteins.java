package org.proteored.miapeapi.experiment;

import java.util.Collection;

import org.proteored.miapeapi.experiment.model.ProteinGroupOccurrence;
import org.proteored.miapeapi.experiment.model.sort.ProteinComparatorKey;
import org.proteored.miapeapi.experiment.model.sort.ProteinGroupComparisonType;

public class VennDataForProteins extends VennData {

	private final ProteinGroupComparisonType proteinComparisonType;

	public VennDataForProteins(Collection col1, Collection col2, Collection col3,
			ProteinGroupComparisonType proteinSelection) {
		super(col1, col2, col3);
		this.proteinComparisonType = proteinSelection;

	}

	@Override
	protected Object getKeyFromObject(Object obj) {
		if (obj instanceof ProteinGroupOccurrence) {
			ProteinGroupOccurrence pgo = (ProteinGroupOccurrence) obj;

			Object pgoKey = pgo.getKey(this.proteinComparisonType);
			return pgoKey;
		}
		return new ProteinComparatorKey(obj.toString(), this.proteinComparisonType);
	}

	@Override
	protected boolean isObjectValid(Object obj) {
		// if (countNonConclusiveProteins != null &&
		// !countNonConclusiveProteins) {
		// if (obj instanceof ProteinGroupOccurrence) {
		// ProteinGroupOccurrence pgo = (ProteinGroupOccurrence) obj;
		// if (pgo.getEvidence() == ProteinEvidence.NONCONCLUSIVE) {
		// return false;
		// }
		// }
		// }
		return true;
	}

}
