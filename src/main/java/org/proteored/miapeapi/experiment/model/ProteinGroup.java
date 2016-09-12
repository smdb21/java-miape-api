package org.proteored.miapeapi.experiment.model;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.proteored.miapeapi.exceptions.IllegalMiapeArgumentException;
import org.proteored.miapeapi.experiment.model.filters.FDRFilter;
import org.proteored.miapeapi.experiment.model.grouping.InferenceProtein;
import org.proteored.miapeapi.experiment.model.grouping.PAnalyzer;
import org.proteored.miapeapi.experiment.model.grouping.ProteinEvidence;
import org.proteored.miapeapi.experiment.model.grouping.ProteinGroupInference;
import org.proteored.miapeapi.experiment.model.sort.SorterUtil;
import org.proteored.miapeapi.interfaces.msi.MiapeMSIDocument;
import org.proteored.miapeapi.interfaces.msi.ProteinScore;
import org.proteored.miapeapi.util.ProteinSequenceRetrieval;

public class ProteinGroup extends ArrayList<ExtendedIdentifiedProtein> {
	private static final Logger log = Logger
			.getLogger("log4j.logger.org.proteored");
	private ProteinEvidence evidence;
	private ExtendedIdentifiedPeptide bestPeptide;
	private String selectedProteinSequence;
	private List<String> accessions;
	private Float proteinLocalFDR;
	private String key;

	public ProteinGroup(ProteinEvidence e) {
		super();
		this.evidence = e;
	}

	public ProteinGroup(ProteinGroupInference iProteinGroup) {
		if (iProteinGroup == null)
			throw new IllegalMiapeArgumentException("group is null");

		for (InferenceProtein inferenceProtein : iProteinGroup) {
			List<ExtendedIdentifiedProtein> proteinsMerged = inferenceProtein
					.getProteinsMerged();
			for (ExtendedIdentifiedProtein extendedIdentifiedProtein : proteinsMerged) {
				extendedIdentifiedProtein.setGroup(this);
				extendedIdentifiedProtein.setEvidence(inferenceProtein
						.getEvidence());
			}
			this.addAll(proteinsMerged);
		}
		this.evidence = iProteinGroup.getEvidence();

	}

	public String getKey() {
		if (this.key != null)
			return this.key;
		String ret = "";

		List<String> accessions2 = this.getAccessions();

		for (String accession : accessions2) {
			if (!"".equals(ret)) {
				ret = ret + ",";
			}
			ret = ret + accession;
		}

		this.key = ret + "[" + this.evidence.toString() + "]";
		return key;
	}

	// public ProteinGroup() {
	// this(ProteinEvidence.AMBIGUOUSGROUP);
	// }

	@Override
	public String toString() {
		return this.getKey();
	}

	public String getProteinSequence(boolean retrieveFromTheInternet) {
		if (this.selectedProteinSequence == null) {

			// First look at local data
			for (ExtendedIdentifiedProtein protein : this) {
				if (protein.getProteinSequence() != null) {
					this.selectedProteinSequence = protein.getProteinSequence();
					return this.selectedProteinSequence;
				}
			}
			// if there is not data look in the cache of the class
			// ProteinRetrieval or at the Internet
			for (ExtendedIdentifiedProtein protein : this) {
				String proteinAcc = protein.getAccession();

				this.selectedProteinSequence = ProteinSequenceRetrieval
						.getProteinSequence(proteinAcc, retrieveFromTheInternet);
				if (this.selectedProteinSequence != null)
					return this.selectedProteinSequence;
			}

		}
		return this.selectedProteinSequence;
	}

	public void dump(PrintStream stream) {
		stream.println(evidence.toString());
		for (ExtendedIdentifiedProtein prot : this) {
			stream.print("\t" + prot.getAccession() + ": ");
			for (ExtendedIdentifiedPeptide pept : prot.getPeptides())
				stream.print(pept.toString() + " ");
			stream.println();
		}
	}

	@Override
	public boolean equals(Object object) {
		// return shareOneProtein(object);
		return shareAllProteins(object);
		// return theyformJustOneGroup(object);
	}

	private boolean theyformJustOneGroup(Object object) {
		if (object instanceof ProteinGroup) {
			if (shareOneProtein(object)) {

				List<ExtendedIdentifiedProtein> proteins = new ArrayList<ExtendedIdentifiedProtein>();
				proteins.addAll(this);
				proteins.addAll((ProteinGroup) object);
				PAnalyzer pa = new PAnalyzer();
				List<ProteinGroup> groups = pa.run(proteins);
				if (groups.size() == 1)
					return true;
			}
			return false;
		}
		return super.equals(object);
	}

	public boolean shareAllProteins(Object object) {
		if (object instanceof ProteinGroup) {
			ProteinGroup pg2 = (ProteinGroup) object;

			if (this.getKey().equals(pg2.getKey())) {
				// if (this.evidence == pg2.evidence)
				return true;
			}
			return false;
		} else if (object instanceof ProteinGroupOccurrence) {
			ProteinGroupOccurrence pgo2 = (ProteinGroupOccurrence) object;
			if (this.equals(pgo2.getFirstOccurrence()))
				return true;
			return false;
		}
		return super.equals(object);
	}

	/**
	 * This method will determine how comparisons are made between
	 * proteinGroups! In this case, two groups are equals if share at least one
	 * protein.
	 */
	public boolean shareOneProtein(Object object) {
		if (object instanceof ProteinGroup) {
			ProteinGroup pg2 = (ProteinGroup) object;

			// At least share one protein
			for (String acc : this.getAccessions()) {
				if (pg2.getAccessions().contains(acc))
					return true;
			}
			return false;
		} else if (object instanceof ProteinGroupOccurrence) {
			ProteinGroupOccurrence pgo2 = (ProteinGroupOccurrence) object;
			if (this.equals(pgo2.getFirstOccurrence()))
				return true;
			return false;
		}
		return super.equals(object);
	}

	/*
	 * public int updateMinimum() { } List<ProteinGroup> getRecursive(
	 * ProteinGroup group, Iterator<ExtendedIdentifiedProtein> it ) {
	 * List<ProteinGroup> res = new ArrayList<ProteinGroup>(); if( group == null
	 * ) group = new ProteinGroup(); res.add(group); if( !it.hasNext() ) return
	 * res; ProteinGroup group2 = (ProteinGroup)group.clone();
	 * group2.add(it.next()); res.addAll(getRecursive(group, it));
	 * res.addAll(getRecursive(group2, it)); return res; }
	 */

	public List<String> getAccessions() {

		if (this.accessions != null)
			return this.accessions;
		this.accessions = new ArrayList<String>();
		for (ExtendedIdentifiedProtein protein : this) {
			if (!accessions.contains(protein.getAccession()))
				accessions.add(protein.getAccession());
		}
		Collections.sort(accessions);
		return accessions;

	}

	@Override
	public void add(int index, ExtendedIdentifiedProtein element) {
		this.accessions = null;
		super.add(index, element);
		this.key = null;
	}

	public ProteinEvidence getEvidence() {
		return this.evidence;
	}

	public void setEvidence(ProteinEvidence evidence) {
		this.evidence = evidence;

	}

	/**
	 * Gets all the scores of the proteins of the group
	 * 
	 * @return
	 */
	public List<ProteinScore> getScores() {
		List<ProteinScore> ret = new ArrayList<ProteinScore>();
		for (ExtendedIdentifiedProtein protein : this) {
			ret.addAll(protein.getScores());
		}
		return ret;
	}

	/**
	 * Gets all peptides from the proteins of the group
	 * 
	 * @return
	 */
	public List<ExtendedIdentifiedPeptide> getPeptides() {
		// if (this.peptides == null || this.peptides.isEmpty()) {
		List<ExtendedIdentifiedPeptide> ret = new ArrayList<ExtendedIdentifiedPeptide>();
		Set<Integer> peptideIds = new HashSet<Integer>();
		for (ExtendedIdentifiedProtein protein : this) {
			final List<ExtendedIdentifiedPeptide> pepts = protein.getPeptides();
			if (pepts != null)
				for (ExtendedIdentifiedPeptide extendedIdentifiedPeptide : pepts) {
					if (!peptideIds.contains(extendedIdentifiedPeptide.getId())) {
						peptideIds.add(extendedIdentifiedPeptide.getId());
						ret.add(extendedIdentifiedPeptide);
					}
				}

		}
		// }

		return ret;
	}

	/**
	 * A {@link ProteinGroup} is decoy if one of its protein is DECOY
	 * 
	 * @param filter
	 * @return
	 */
	public boolean isDecoyOLD(FDRFilter filter) {
		for (ExtendedIdentifiedProtein protein : this) {
			if (protein.isDecoy(filter))
				return true;
		}
		return false;
	}

	/**
	 * A {@link ProteinGroup} is decoy if ALL of its protein is DECOY
	 * 
	 * @param filter
	 * @return
	 */
	public boolean isDecoy(FDRFilter filter) {
		for (ExtendedIdentifiedProtein protein : this) {
			if (!protein.isDecoy(filter))
				return false;
		}
		return true;
	}

	public boolean isDecoy() {
		for (ExtendedIdentifiedProtein protein : this) {
			if (!protein.isDecoy())
				return false;
		}
		return true;
		// return this.isDecoy;
	}

	@Override
	public boolean add(ExtendedIdentifiedProtein e) {
		this.key = null;
		return super.add(e);
	}

	public void setDecoy(boolean b) {
		for (ExtendedIdentifiedProtein protein : this) {
			protein.setDecoy(b, false);
		}

	}

	public void setDecoy(boolean b, boolean setProteins) {

		if (setProteins)
			for (ExtendedIdentifiedProtein protein : this) {
				protein.setDecoy(b, false, false);
			}

	}

	public List<MiapeMSIDocument> getMiapeMSIs() {
		List<MiapeMSIDocument> ret = new ArrayList<MiapeMSIDocument>();
		Set<Integer> identifiers = new HashSet<Integer>();
		for (ExtendedIdentifiedProtein protein : this) {
			final MiapeMSIDocument miapeMSI = protein.getMiapeMSI();
			if (miapeMSI != null && !identifiers.contains(miapeMSI.getId())) {
				identifiers.add(miapeMSI.getId());
				ret.add(miapeMSI);
			}
		}
		return ret;
	}

	public List<String> getProteinScoreNames() {
		List<String> ret = new ArrayList<String>();
		for (ExtendedIdentifiedProtein protein : this) {
			if (protein.getScoreNames() != null) {
				for (String scoreName : protein.getScoreNames()) {
					if (!ret.contains(scoreName))
						ret.add(scoreName);
				}
			}
		}
		return ret;
	}

	public List<Float> getProteinScores(String scoreName) {
		List<Float> ret = new ArrayList<Float>();

		final List<ProteinScore> scores = this.getScores();
		for (ProteinScore proteinScore : scores) {
			try {
				if (proteinScore.getName().equals(scoreName))
					ret.add(Float.valueOf(proteinScore.getValue()));
			} catch (NumberFormatException e) {

			}
		}
		return ret;
	}

	public Float getBestProteinScore() {
		final ExtendedIdentifiedProtein bestProtein = getBestProtein();
		return bestProtein.getScore();
	}

	public Float getBestProteinScore(String scoreName) {
		final ExtendedIdentifiedProtein bestProtein = getBestProtein(scoreName);
		if (bestProtein != null)
			return bestProtein.getScore(scoreName);
		return null;
	}

	private ExtendedIdentifiedProtein getBestProtein(String scoreName) {
		SorterUtil.sortProteinsByProteinScore(this, scoreName, false);
		return this.get(0);
	}

	public ExtendedIdentifiedProtein getBestProtein() {
		SorterUtil.sortProteinsByBestProteinScore(this, false);
		return this.get(0);
	}

	public ExtendedIdentifiedPeptide getBestPeptideByScore() {
		if (this.bestPeptide != null)
			return this.bestPeptide;

		List<ExtendedIdentifiedPeptide> peptides = this.getPeptides();
		if (peptides != null && !peptides.isEmpty()) {
			SorterUtil.sortPeptidesByBestPeptideScore(peptides, false);
			this.bestPeptide = peptides.get(0);
			return peptides.get(0);
		}
		return null;

	}

	public ExtendedIdentifiedPeptide getBestPeptideByScore(String scoreName) {
		List<ExtendedIdentifiedPeptide> peptides = this.getPeptides();
		if (peptides != null && !peptides.isEmpty()) {
			SorterUtil.sortPeptidesByPeptideScore(peptides, scoreName, false);
			return peptides.get(0);
		}
		return null;

	}

	public Float getBestPeptideScore() {
		ExtendedIdentifiedPeptide bestPeptideByScore = this
				.getBestPeptideByScore();
		if (bestPeptideByScore != null) {
			return bestPeptideByScore.getScore();
		}
		return null;
	}

	public Float getBestPeptideScore(String scoreName) {
		ExtendedIdentifiedPeptide bestPeptideByScore = this
				.getBestPeptideByScore(scoreName);
		if (bestPeptideByScore != null) {
			return bestPeptideByScore.getScore(scoreName);
		}
		return null;
	}

	/**
	 * Created on 31 Jan 2013. This function will add the proteins of the group
	 * passed by parameters to the proteins of this group
	 * 
	 * @param group
	 */
	public void fusion(ProteinGroup group) {
		this.addAll(group);
	}

	public Float getProteinLocalFDR() {
		return proteinLocalFDR;
	}

	public void setProteinLocalFDR(Float proteinLocalFDR) {
		this.proteinLocalFDR = proteinLocalFDR;
	}
}
