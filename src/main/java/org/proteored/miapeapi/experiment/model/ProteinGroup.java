package org.proteored.miapeapi.experiment.model;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.proteored.miapeapi.exceptions.IllegalMiapeArgumentException;
import org.proteored.miapeapi.experiment.model.filters.FDRFilter;
import org.proteored.miapeapi.experiment.model.grouping.InferenceProtein;
import org.proteored.miapeapi.experiment.model.grouping.ProteinEvidence;
import org.proteored.miapeapi.experiment.model.grouping.ProteinGroupInference;
import org.proteored.miapeapi.experiment.model.sort.SorterUtil;
import org.proteored.miapeapi.interfaces.Software;
import org.proteored.miapeapi.interfaces.msi.Database;
import org.proteored.miapeapi.interfaces.msi.ProteinScore;
import org.proteored.miapeapi.util.ProteinSequenceRetrieval;

import edu.scripps.yates.annotations.uniprot.UniprotProteinLocalRetriever;
import gnu.trove.set.hash.THashSet;
import gnu.trove.set.hash.TIntHashSet;

public class ProteinGroup extends ArrayList<ExtendedIdentifiedProtein> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2979064683814192733L;
	private static final Logger log = Logger.getLogger("log4j.logger.org.proteored");
	private ProteinEvidence evidence;
	private ExtendedIdentifiedPeptide bestPeptide;
	private String selectedProteinSequence;
	private List<String> accessions;
	private Float proteinLocalFDR;
	private String key;

	public ProteinGroup(ProteinEvidence e) {
		super();
		evidence = e;
	}

	public ProteinGroup(ProteinGroupInference iProteinGroup) {
		if (iProteinGroup == null)
			throw new IllegalMiapeArgumentException("group is null");

		for (final InferenceProtein inferenceProtein : iProteinGroup) {
			final List<ExtendedIdentifiedProtein> proteinsMerged = inferenceProtein.getProteinsMerged();
			for (final ExtendedIdentifiedProtein extendedIdentifiedProtein : proteinsMerged) {
				extendedIdentifiedProtein.setGroup(this);
				extendedIdentifiedProtein.setEvidence(inferenceProtein.getEvidence());
			}
			this.addAll(proteinsMerged);
		}
		evidence = iProteinGroup.getEvidence();

	}

	public String getKey() {
		if (key != null)
			return key;
		String ret = "";

		final List<String> accessions2 = getAccessions();

		for (final String accession : accessions2) {
			if (!"".equals(ret)) {
				ret = ret + ",";
			}
			ret = ret + accession;
		}

		key = ret + "[" + evidence.toString() + "]";
		return key;
	}

	// public ProteinGroup() {
	// this(ProteinEvidence.AMBIGUOUSGROUP);
	// }

	@Override
	public String toString() {
		return getKey();
	}

	public String getProteinSequence(boolean retrieveFromTheInternet, UniprotProteinLocalRetriever upr) {
		if (selectedProteinSequence == null) {

			// First look at local data
			for (final ExtendedIdentifiedProtein protein : this) {
				if (protein.getProteinSequence() != null) {
					selectedProteinSequence = protein.getProteinSequence();
					return selectedProteinSequence;
				}
			}
			// if there is not data look in the cache of the class
			// ProteinRetrieval or at the Internet
			for (final ExtendedIdentifiedProtein protein : this) {
				final String proteinAcc = protein.getAccession();

				selectedProteinSequence = ProteinSequenceRetrieval.getProteinSequence(proteinAcc,
						retrieveFromTheInternet, upr);
				if (selectedProteinSequence != null)
					return selectedProteinSequence;
			}

		}
		return selectedProteinSequence;
	}

	public void dump(PrintStream stream) {
		stream.println(evidence.toString());
		for (final ExtendedIdentifiedProtein prot : this) {
			stream.print("\t" + prot.getAccession() + ": ");
			for (final ExtendedIdentifiedPeptide pept : prot.getPeptides())
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

	public boolean shareAllProteins(Object object) {
		if (object instanceof ProteinGroup) {
			final ProteinGroup pg2 = (ProteinGroup) object;

			if (getKey().equals(pg2.getKey())) {
				// if (this.evidence == pg2.evidence)
				return true;
			}
			return false;
		} else if (object instanceof ProteinGroupOccurrence) {
			final ProteinGroupOccurrence pgo2 = (ProteinGroupOccurrence) object;
			if (equals(pgo2.getFirstOccurrence()))
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
			final ProteinGroup pg2 = (ProteinGroup) object;

			// At least share one protein
			for (final String acc : getAccessions()) {
				if (pg2.getAccessions().contains(acc))
					return true;
			}
			return false;
		} else if (object instanceof ProteinGroupOccurrence) {
			final ProteinGroupOccurrence pgo2 = (ProteinGroupOccurrence) object;
			if (equals(pgo2.getFirstOccurrence()))
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

		if (accessions != null)
			return accessions;
		accessions = new ArrayList<String>();
		for (final ExtendedIdentifiedProtein protein : this) {
			if (!accessions.contains(protein.getAccession()))
				accessions.add(protein.getAccession());
		}
		Collections.sort(accessions);
		return accessions;

	}

	/**
	 * Gets the first accession of the list of proteins
	 * 
	 * @return
	 */
	public String getAccessionsString() {
		final List<String> accList = getAccessions();

		String ret = "";
		for (final String accession : accList) {
			if (!"".equals(ret))
				ret = ret + ",";
			ret = ret + accession;
		}

		return ret;
	}

	@Override
	public void add(int index, ExtendedIdentifiedProtein element) {
		accessions = null;
		super.add(index, element);
		key = null;
	}

	public ProteinEvidence getEvidence() {
		return evidence;
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
		final List<ProteinScore> ret = new ArrayList<ProteinScore>();
		for (final ExtendedIdentifiedProtein protein : this) {
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
		final List<ExtendedIdentifiedPeptide> ret = new ArrayList<ExtendedIdentifiedPeptide>();
		final TIntHashSet peptideIds = new TIntHashSet();
		for (final ExtendedIdentifiedProtein protein : this) {
			final List<ExtendedIdentifiedPeptide> pepts = protein.getPeptides();
			if (pepts != null)
				for (final ExtendedIdentifiedPeptide extendedIdentifiedPeptide : pepts) {
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
		for (final ExtendedIdentifiedProtein protein : this) {
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
		for (final ExtendedIdentifiedProtein protein : this) {
			if (!protein.isDecoy(filter))
				return false;
		}
		return true;
	}

	public boolean isDecoy() {
		for (final ExtendedIdentifiedProtein protein : this) {
			if (!protein.isDecoy())
				return false;
		}
		return true;
		// return this.isDecoy;
	}

	@Override
	public boolean add(ExtendedIdentifiedProtein e) {
		key = null;
		return super.add(e);
	}

	public void setDecoy(boolean b) {
		for (final ExtendedIdentifiedProtein protein : this) {
			protein.setDecoy(b, false);
		}

	}

	public void setDecoy(boolean b, boolean setProteins) {

		if (setProteins)
			for (final ExtendedIdentifiedProtein protein : this) {
				protein.setDecoy(b, false, false);
			}

	}

	public List<String> getProteinScoreNames() {
		final List<String> ret = new ArrayList<String>();
		for (final ExtendedIdentifiedProtein protein : this) {
			if (protein.getScoreNames() != null) {
				for (final String scoreName : protein.getScoreNames()) {
					if (!ret.contains(scoreName))
						ret.add(scoreName);
				}
			}
		}
		return ret;
	}

	public List<Float> getProteinScores(String scoreName) {
		final List<Float> ret = new ArrayList<Float>();

		final List<ProteinScore> scores = getScores();
		for (final ProteinScore proteinScore : scores) {
			try {
				if (proteinScore.getName().equals(scoreName))
					ret.add(Float.valueOf(proteinScore.getValue()));
			} catch (final NumberFormatException e) {

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
		return get(0);
	}

	public ExtendedIdentifiedProtein getBestProtein() {
		SorterUtil.sortProteinsByBestProteinScore(this, false);
		return get(0);
	}

	public ExtendedIdentifiedPeptide getBestPeptideByScore() {
		if (bestPeptide != null)
			return bestPeptide;

		final List<ExtendedIdentifiedPeptide> peptides = getPeptides();
		if (peptides != null && !peptides.isEmpty()) {
			SorterUtil.sortPeptidesByBestPeptideScore(peptides, false);
			bestPeptide = peptides.get(0);
			return peptides.get(0);
		}
		return null;

	}

	public ExtendedIdentifiedPeptide getBestPeptideByScore(String scoreName) {
		final List<ExtendedIdentifiedPeptide> peptides = getPeptides();
		if (peptides != null && !peptides.isEmpty()) {
			SorterUtil.sortPeptidesByPeptideScore(peptides, scoreName, false);
			return peptides.get(0);
		}
		return null;

	}

	public Float getBestPeptideScore() {
		final ExtendedIdentifiedPeptide bestPeptideByScore = this.getBestPeptideByScore();
		if (bestPeptideByScore != null) {
			return bestPeptideByScore.getScore();
		}
		return null;
	}

	public Float getBestPeptideScore(String scoreName) {
		final ExtendedIdentifiedPeptide bestPeptideByScore = this.getBestPeptideByScore(scoreName);
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

	public Set<Database> getDatabases() {
		final Set<Database> ret = new THashSet<Database>();
		for (final ExtendedIdentifiedProtein protein : this) {
			final Set<Database> databases = protein.getDatabases();
			ret.addAll(databases);
		}
		return ret;
	}

	public Set<Software> getSoftwares() {
		final Set<Software> ret = new THashSet<Software>();
		for (final ExtendedIdentifiedProtein protein : this) {
			final Set<Software> softwares = protein.getSoftwares();
			ret.addAll(softwares);
		}
		return ret;
	}
}
