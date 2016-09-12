package org.proteored.miapeapi.factories.msi;

import org.proteored.miapeapi.cv.msi.PeptideModificationName;
import org.proteored.miapeapi.interfaces.msi.PeptideModification;

public class PeptideModificationBuilder {

	public Double avgDelta;
	public Double monoDelta;
	public String name;
	public int position;
	public String replacementResidue;
	public String residues;
	public Double neutralLoss;
	public String evidence;

	/**
	 * Set the peptide modification name. It should be one of the possible values from
	 * {@link PeptideModificationName}
	 */
	PeptideModificationBuilder(String name) {
		this.name = name;
	}

	public PeptideModificationBuilder monoDelta(Double value) {
		this.monoDelta = value;
		return this;
	}

	public PeptideModificationBuilder avgDelta(Double value) {
		this.avgDelta = value;
		return this;
	}

	public PeptideModificationBuilder position(int value) {
		this.position = value;
		return this;
	}

	public PeptideModificationBuilder replacementResidue(String value) {
		this.replacementResidue = value;
		return this;
	}

	public PeptideModificationBuilder residues(String value) {
		this.residues = value;
		return this;
	}

	public PeptideModificationBuilder neutralLoss(Double value) {
		this.neutralLoss = value;
		return this;
	}

	public PeptideModificationBuilder evidence(String value) {
		this.evidence = value;
		return this;
	}

	public PeptideModification build() {
		return new PeptideModificationImpl(this);
	}

}