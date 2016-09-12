package org.proteored.miapeapi.experiment.model.grouping;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.proteored.miapeapi.experiment.model.ProteinGroup;

public class PanalyzerStats {
	private static final Logger log = Logger
			.getLogger("log4j.logger.org.proteored");
	// public int PeptideCount;
	public int proteinCount;
	public int proteinMaxCount;
	public int proteinMinCount;
	public int conclusiveCount;
	public int nonConclusiveCount;
	public int groupedCount;
	public int ambiguousGroupCount;
	public int indistinguishableGroupCount;
	public int filteredCount;
	public int differentNonConclusiveCount;

	public PanalyzerStats(Collection<ProteinGroup> groups) {
		List<ProteinGroup> nonConclusiveGroups = new ArrayList<ProteinGroup>();
		for (ProteinGroup group : groups) {
			switch (group.getEvidence()) {
			case AMBIGUOUSGROUP:
				this.ambiguousGroupCount++;
				this.groupedCount += group.size();
				this.proteinCount++;
				break;
			case CONCLUSIVE:
				this.conclusiveCount++;
				this.proteinCount++;
				break;
			case FILTERED:
				this.filteredCount++;
				break;
			case INDISTINGUISHABLE:
				this.indistinguishableGroupCount++;
				this.groupedCount += group.size();
				this.proteinCount++;
				break;
			case NONCONCLUSIVE:
				this.nonConclusiveCount++;
				if (!nonConclusiveGroups.contains(group))
					nonConclusiveGroups.add(group);
				break;
			}
			if (group.getEvidence() != ProteinEvidence.FILTERED)
				this.proteinMaxCount += group.size();
		}
		// this.PeptideCount = mPepts.size();
		this.differentNonConclusiveCount = nonConclusiveGroups.size();
		log.debug("Returning " + this.differentNonConclusiveCount
				+ " different non conclusive groups and "
				+ this.nonConclusiveCount + " non conclusive groups");
		this.proteinMinCount = -1;
	}

	public PanalyzerStats(List<ProteinGroupInference> groups) {
		List<ProteinGroupInference> nonConclusiveGroups = new ArrayList<ProteinGroupInference>();
		for (ProteinGroupInference group : groups) {
			switch (group.getEvidence()) {
			case AMBIGUOUSGROUP:
				this.ambiguousGroupCount++;
				this.groupedCount += group.size();
				this.proteinCount++;
				break;
			case CONCLUSIVE:
				this.conclusiveCount++;
				this.proteinCount++;
				break;
			case FILTERED:
				this.filteredCount++;
				break;
			case INDISTINGUISHABLE:
				this.indistinguishableGroupCount++;
				this.groupedCount += group.size();
				this.proteinCount++;
				break;
			case NONCONCLUSIVE:
				this.nonConclusiveCount++;
				if (!nonConclusiveGroups.contains(group))
					nonConclusiveGroups.add(group);
				break;
			}
			if (group.getEvidence() != ProteinEvidence.FILTERED)
				this.proteinMaxCount += group.size();
		}
		// this.PeptideCount = mPepts.size();
		this.differentNonConclusiveCount = nonConclusiveGroups.size();
		log.debug("Returning " + this.differentNonConclusiveCount
				+ " different non conclusive groups and "
				+ this.nonConclusiveCount + " non conclusive groups");
		this.proteinMinCount = -1;
	}

	public void dump(PrintStream stream) {
		// stream.println("Peptide count: " + PeptideCount);
		stream.println("Protein count: " + proteinCount);
		stream.println("\tMaximum: " + proteinMaxCount);
		stream.println("\tConclusive: " + conclusiveCount);
		stream.println("\tNon-conclusive: " + nonConclusiveCount);
		stream.println("\tFiltered: " + filteredCount);
		stream.println("\tGrouped: " + groupedCount);
		stream.println("\t\tIndistinguishable groups: "
				+ indistinguishableGroupCount);
		stream.println("\t\tAmbiguous groups: " + ambiguousGroupCount);
	}

}
