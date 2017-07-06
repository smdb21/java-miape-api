package org.proteored.miapeapi.experiment.model.grouping;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.experiment.model.ExtendedIdentifiedPeptide;
import org.proteored.miapeapi.experiment.model.ExtendedIdentifiedProtein;
import org.proteored.miapeapi.experiment.model.ProteinGroup;
import org.proteored.miapeapi.spring.SpringHandler;

import gnu.trove.map.hash.TIntObjectHashMap;

/**
 * 
 * @author gorka
 */
public class PAnalyzerOLD {
	private static final Logger log = Logger.getLogger("log4j.logger.org.proteored");
	private ControlVocabularyManager cv;

	private final TIntObjectHashMap<ExtendedIdentifiedProtein> mProts;
	private TIntObjectHashMap<ExtendedIdentifiedPeptide> mPepts;
	private final List<ProteinGroup> mGroups;
	private PanalyzerStats mStats;

	public PAnalyzerOLD(ControlVocabularyManager cvManager) {
		if (cvManager != null)
			this.cv = cvManager;
		else
			this.cv = SpringHandler.getInstance().getCVManager();
		this.mProts = new TIntObjectHashMap<ExtendedIdentifiedProtein>();
		this.mPepts = new TIntObjectHashMap<ExtendedIdentifiedPeptide>();
		this.mGroups = new ArrayList<ProteinGroup>();
	}

	public List<ProteinGroup> run(List<ExtendedIdentifiedProtein> proteins) {
		long t1 = System.currentTimeMillis();
		log.info("Running panalyzer for " + proteins.size() + " proteins");
		// clear protein groups in proteins
		for (ExtendedIdentifiedProtein protein : proteins) {
			protein.setGroup(null);
			protein.setEvidence(null);
			for (ExtendedIdentifiedPeptide peptide : protein.getPeptides()) {
				peptide.setRelation(null);
			}
		}

		createInferenceMaps(proteins);
		long t2 = System.currentTimeMillis();
		classifyPeptides();
		long t3 = System.currentTimeMillis();
		classifyProteins();
		long t4 = System.currentTimeMillis();
		createGroups();
		long t5 = System.currentTimeMillis();
		markIndistinguishable();
		long t6 = System.currentTimeMillis();
		this.mStats = getStats(this.mGroups);
		long t7 = System.currentTimeMillis();

		log.info("Returning " + mGroups.size() + " protein groups from " + proteins.size() + " proteins, in "
				+ (System.currentTimeMillis() - t1) + " milliseconds");
		log.info("Panalyzer times: " + (t2 - t1) + ", " + (t3 - t2) + ", " + (t4 - t3) + ", " + (t5 - t4) + ", "
				+ (t6 - t5) + ", " + (t7 - t6));

		// SorterUtil.sortProteinGroupsByBestPeptideScore(mGroups);
		// this.fullGroupDump(System.out);
		return mGroups;
	}

	private void createInferenceMaps(List<ExtendedIdentifiedProtein> proteins) {

		for (ExtendedIdentifiedProtein prot : proteins) {
			prot.setGroup(null);
			List<ExtendedIdentifiedPeptide> peptides = prot.getPeptides();
			if (peptides != null && !peptides.isEmpty()) {
				for (ExtendedIdentifiedPeptide pept : peptides) {
					if (!mPepts.containsKey(pept.getId())) {
						mPepts.put(pept.getId(), pept);
					}
				}
				mProts.put(prot.getId(), prot);
			}
			// else {
			// log.info("This protein " + prot.getAccession()
			// + " has no peptides");
			// }

		}
	}

	private void classifyPeptides() {
		// Locate unique peptides
		for (ExtendedIdentifiedPeptide pept : mPepts.valueCollection())
			if (pept.getProteins().size() == 1) {
				pept.setRelation(PeptideRelation.UNIQUE);
				pept.getProteins().get(0).setEvidence(ProteinEvidence.CONCLUSIVE);
			} else
				pept.setRelation(PeptideRelation.DISCRIMINATING);

		// Locate non-meaningful peptides (first round)
		for (ExtendedIdentifiedProtein prot : mProts.valueCollection())
			if (prot.getEvidence() == ProteinEvidence.CONCLUSIVE)
				for (ExtendedIdentifiedPeptide pept : prot.getPeptides())
					if (pept.getRelation() != PeptideRelation.UNIQUE)
						pept.setRelation(PeptideRelation.NONDISCRIMINATING);

		// Locate non-meaningful peptides (second round)
		boolean shared;
		for (ExtendedIdentifiedPeptide pept : mPepts.valueCollection()) {
			if (pept.getRelation() != PeptideRelation.DISCRIMINATING)
				continue;
			for (ExtendedIdentifiedPeptide pept2 : pept.getProteins().get(0).getPeptides()) {
				if (pept2.getRelation() == PeptideRelation.NONDISCRIMINATING)
					continue;
				if (pept2.getProteins().size() <= pept.getProteins().size())
					continue;
				shared = true;
				for (ExtendedIdentifiedProtein p : pept.getProteins())
					if (!p.getPeptides().contains(pept2)) {
						shared = false;
						break;
					}
				if (shared)
					pept2.setRelation(PeptideRelation.NONDISCRIMINATING);
			}
		}
	}

	private void classifyProteins() {
		boolean group;

		for (ExtendedIdentifiedProtein prot : mProts.valueCollection()) {
			if (prot.getEvidence() == ProteinEvidence.CONCLUSIVE)
				continue;
			List<ExtendedIdentifiedPeptide> peptides = prot.getPeptides();
			if (peptides == null || peptides.isEmpty()) {
				prot.setEvidence(ProteinEvidence.FILTERED);
				continue;
			}
			group = false;
			for (ExtendedIdentifiedPeptide pept : peptides)
				if (pept.getRelation() == PeptideRelation.DISCRIMINATING) {
					group = true;
					break;
				}
			prot.setEvidence(group ? ProteinEvidence.AMBIGUOUSGROUP : ProteinEvidence.NONCONCLUSIVE);
		}
	}

	private void createGroups() {
		ProteinGroup group;

		for (ExtendedIdentifiedProtein prot : mProts.valueCollection()) {
			if (prot.getGroup() == null) {
				group = new ProteinGroup(prot.getEvidence());
				if (!group.contains(prot)) {// added by Salva (6Dec2012)
					group.add(prot);
					prot.setGroup(group);
				}
				mGroups.add(group);
			}
			if (prot.getEvidence() != ProteinEvidence.AMBIGUOUSGROUP)
				continue;
			for (ExtendedIdentifiedPeptide pept : prot.getPeptides()) {
				if (pept.getRelation() != PeptideRelation.DISCRIMINATING)
					continue;
				for (ExtendedIdentifiedProtein subp : pept.getProteins()) {
					if (subp.getEvidence() != ProteinEvidence.AMBIGUOUSGROUP || subp.getGroup() != null)
						continue;
					prot.getGroup().add(subp);
					subp.setGroup(prot.getGroup());
				}
			}
		}
	}

	private void markIndistinguishable() {
		boolean indistinguishable;
		for (ProteinGroup group : mGroups) {
			if (group.getEvidence() != ProteinEvidence.AMBIGUOUSGROUP)
				continue;
			indistinguishable = true;
			for (ExtendedIdentifiedPeptide pept : group.get(0).getPeptides()) {
				if (pept.getRelation() != PeptideRelation.DISCRIMINATING)
					continue;
				for (ExtendedIdentifiedProtein prot : group) {
					if (!prot.getPeptides().contains(pept)) {
						indistinguishable = false;
						break;
					}
				}
				if (!indistinguishable)
					break;
			}
			if (indistinguishable)
				group.setEvidence(ProteinEvidence.INDISTINGUISHABLE);
		}
	}

	public PanalyzerStats getStats(List<ProteinGroup> groups) {
		return new PanalyzerStats(groups);
	}

	public PanalyzerStats getStats() {
		return this.mStats;
	}

	public void fullGroupDump(PrintStream stream) {
		for (ProteinGroup proteinGroup : this.mGroups) {
			stream.println(proteinGroup + " " + proteinGroup.getBestPeptideByScore().getSequence() + " "
					+ proteinGroup.getBestPeptideScore());
		}
	}

}
