package org.proteored.miapeapi.experiment.model.grouping;

import java.util.ArrayList;
import java.util.List;

import org.proteored.miapeapi.experiment.model.ExtendedIdentifiedProtein;

public class InferenceProtein {

	private List<InferencePeptide> inferencePeptides = new ArrayList<InferencePeptide>();
	private List<ExtendedIdentifiedProtein> proteinsMerged = new ArrayList<ExtendedIdentifiedProtein>();
	private ProteinEvidence evidence;
	private ProteinGroupInference group;
	private final String accession;

	public InferenceProtein(ExtendedIdentifiedProtein p) {
		this(p, ProteinEvidence.NONCONCLUSIVE);
	}

	public InferenceProtein(ExtendedIdentifiedProtein p, ProteinEvidence e) {

		this.evidence = e;
		this.group = null;
		this.proteinsMerged.add(p);
		this.accession = p.getAccession();
	}

	public void addExtendedProtein(ExtendedIdentifiedProtein p) {
		this.proteinsMerged.add(p);
	}

	public List<ExtendedIdentifiedProtein> getProteinsMerged() {
		return this.proteinsMerged;
	}

	public List<InferencePeptide> getInferencePeptides() {
		return inferencePeptides;
	}

	public void setInferencePeptides(List<InferencePeptide> inferencePeptides) {
		this.inferencePeptides = inferencePeptides;
	}

	public ProteinEvidence getEvidence() {
		return evidence;
	}

	public void setEvidence(ProteinEvidence evidence) {
		for (ExtendedIdentifiedProtein p : getProteinsMerged())
			p.setEvidence(evidence);

		this.evidence = evidence;
	}

	public ProteinGroupInference getGroup() {
		return group;
	}

	public void setGroup(ProteinGroupInference group) {
		this.group = group;
	}

	public void setProteinsMerged(List<ExtendedIdentifiedProtein> proteinsMerged) {
		this.proteinsMerged = proteinsMerged;
	}

	public String getAccession() {
		return accession;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof InferenceProtein))
			return super.equals(obj);
		else {
			InferenceProtein protein = (InferenceProtein) obj;
			return protein.getAccession().equals(this.getAccession());
		}
	}

}
