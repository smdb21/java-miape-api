package org.proteored.miapeapi.experiment;

import java.util.Collection;

import org.proteored.miapeapi.experiment.model.ExtendedIdentifiedPeptide;
import org.proteored.miapeapi.experiment.model.PeptideOccurrence;

public class VennDataForPeptides extends VennData {
	private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger("log4j.logger.org.proteored");

	public VennDataForPeptides(Collection col1, Collection col2, Collection col3) {

		super(col1, col2, col3);

	}

	private String getPeptideSequence(Object obj1) {
		if (obj1 instanceof ExtendedIdentifiedPeptide) {
			return ((ExtendedIdentifiedPeptide) obj1).getSequence();
		}
		if (obj1 instanceof PeptideOccurrence) {
			return ((PeptideOccurrence) obj1).getKey();
		}

		return obj1.toString();
	}

	@Override
	protected String getKeyFromObject(Object obj) {
		return getPeptideSequence(obj);
	}

	@Override
	protected boolean isObjectValid(Object obj) {
		return true;
	}

}
