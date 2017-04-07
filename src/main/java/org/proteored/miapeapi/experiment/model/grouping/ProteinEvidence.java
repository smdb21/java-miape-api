package org.proteored.miapeapi.experiment.model.grouping;

/**
 *
 * @author gorka
 */
public enum ProteinEvidence {
	/**
	 * At least one unique peptides
	 */
	CONCLUSIVE,
	/**
	 * Same peptides and at least one Discriminating
	 */
	INDISTINGUISHABLE,
	/**
	 * Shared Discriminating peptides
	 */
	AMBIGUOUSGROUP,

	/**
	 * Only NonDiscrimitating peptides
	 */
	NONCONCLUSIVE,
	/**
	 * No peptides
	 */
	FILTERED
}
