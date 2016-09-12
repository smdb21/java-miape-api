package org.proteored.miapeapi.factories.msi;

import org.proteored.miapeapi.interfaces.msi.PeptideModification;

public class PeptideModificationImpl implements PeptideModification {

	private final String name;
	private final int position;
	private final String residues;
	private final Double monoDelta;
	private final Double avgDelta;
	private final String replacementResidue;
	private final Double neutralLoss;
	private final String evidence;

	public PeptideModificationImpl(PeptideModificationBuilder builder) {
		this.avgDelta = builder.avgDelta;
		this.monoDelta = builder.monoDelta;
		this.name = builder.name;
		this.position = builder.position;
		this.replacementResidue = builder.replacementResidue;
		this.residues = builder.residues;
		this.neutralLoss = builder.neutralLoss;
		this.evidence = builder.evidence;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getPosition() {
		return position;
	}

	@Override
	public String getResidues() {
		return residues;
	}

	@Override
	public Double getMonoDelta() {
		return monoDelta;
	}

	@Override
	public Double getAvgDelta() {
		return avgDelta;
	}

	@Override
	public String getReplacementResidue() {
		return replacementResidue;
	}

	@Override
	public Double getNeutralLoss() {
		return neutralLoss;
	}

	@Override
	public String getModificationEvidence() {
		return evidence;
	}

}
