package org.proteored.miapeapi.experiment.model.grouping;

import java.util.ArrayList;
import java.util.List;

import org.proteored.miapeapi.experiment.model.ExtendedIdentifiedPeptide;

public class InferencePeptide {
	private List<InferenceProtein> inferenceProteins = new ArrayList<InferenceProtein>();
	private List<ExtendedIdentifiedPeptide> mergedPeptides = new ArrayList<ExtendedIdentifiedPeptide>();
	private PeptideRelation relation;
	private final int id;
	private final String sequence;

	public InferencePeptide(ExtendedIdentifiedPeptide p) {
		this(p, PeptideRelation.NONDISCRIMINATING);
	}

	public InferencePeptide(ExtendedIdentifiedPeptide p, PeptideRelation r) {

		this.relation = r;
		this.mergedPeptides.add(p);
		this.id = p.getId();
		this.sequence = p.getSequence();
	}

	public void addExtendedPeptide(ExtendedIdentifiedPeptide p) {
		this.mergedPeptides.add(p);
	}

	public List<ExtendedIdentifiedPeptide> getPeptidesMerged() {
		return this.mergedPeptides;
	}

	@Override
	public String toString() {
		switch (relation) {
		case DISCRIMINATING:
			return getId() + "*";
		case NONDISCRIMINATING:
			return getId() + "**";
		}
		return new Integer(getId()).toString();
	}

	private int getId() {
		return this.id;
	}

	public List<InferenceProtein> getInferenceProteins() {
		return inferenceProteins;
	}

	public List<ExtendedIdentifiedPeptide> getMergedPeptides() {
		return mergedPeptides;
	}

	public void setMergedPeptides(List<ExtendedIdentifiedPeptide> mergedPeptides) {
		this.mergedPeptides = mergedPeptides;
	}

	public PeptideRelation getRelation() {
		return relation;
	}

	public void setRelation(PeptideRelation relation) {
		for (ExtendedIdentifiedPeptide p : this.mergedPeptides) {
			p.setRelation(relation);
		}
		this.relation = relation;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof InferencePeptide))
			return super.equals(obj);
		else {
			InferencePeptide peptide = (InferencePeptide) obj;
			return peptide.getSequence().equals(this.getSequence());
		}
	}

	private String getSequence() {
		return this.sequence;
	}
}
