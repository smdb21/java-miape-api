package org.proteored.miapeapi.experiment.model.grouping;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.LocalOboTestControlVocabularyManager;
import org.proteored.miapeapi.exceptions.MiapeDatabaseException;
import org.proteored.miapeapi.exceptions.MiapeSecurityException;
import org.proteored.miapeapi.experiment.model.ExtendedIdentifiedPeptide;
import org.proteored.miapeapi.experiment.model.ExtendedIdentifiedProtein;
import org.proteored.miapeapi.experiment.model.ProteinGroup;
import org.proteored.miapeapi.experiment.model.datamanager.DataManager;
import org.proteored.miapeapi.interfaces.msi.MiapeMSIDocument;
import org.proteored.miapeapi.interfaces.xml.MiapeXmlFile;
import org.proteored.miapeapi.xml.msi.MIAPEMSIXmlFile;
import org.proteored.miapeapi.xml.msi.MiapeMSIXmlFactory;

import edu.scripps.yates.utilities.dates.DatesUtil;
import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;
import gnu.trove.set.hash.TIntHashSet;
import junit.framework.Assert;

/**
 *
 * @author gorka
 */
public class PAnalyzer {
	private static final Logger log = Logger.getLogger("log4j.logger.org.proteored");

	private final Map<String, InferenceProtein> mProts;
	private final Map<String, InferencePeptide> mPepts;
	private final List<ProteinGroupInference> mGroups;
	private PanalyzerStats mStats;
	private final boolean separateNonConclusiveProteins;

	private boolean ignoreProteinId;

	/**
	 * ignoreProteinId=true
	 * 
	 * @param separateNonConclusiveProteins
	 */
	public PAnalyzer(boolean separateNonConclusiveProteins) {
		this(separateNonConclusiveProteins, true);
	}

	/**
	 * 
	 * @param separateNonConclusiveProteins
	 * @param ignoreProteinId
	 *            if true, even protein with the same id are considered. It
	 *            happens that the id may be repeated and we want them to be
	 *            separated
	 */
	public PAnalyzer(boolean separateNonConclusiveProteins, boolean ignoreProteinId) {
		mProts = new THashMap<String, InferenceProtein>();
		mPepts = new THashMap<String, InferencePeptide>();
		mGroups = new ArrayList<ProteinGroupInference>();
		this.ignoreProteinId = ignoreProteinId;
		this.separateNonConclusiveProteins = separateNonConclusiveProteins;
	}

	public List<ProteinGroup> run(Collection<ExtendedIdentifiedProtein> proteins) {
		long t1 = System.currentTimeMillis();
		log.info("Grouping " + proteins.size() + " proteins");
		createInferenceMaps(proteins);
		log.debug("Running panalyzer for " + mProts.size() + " proteins and " + mPepts.size() + " peptides");

		// long t2 = System.currentTimeMillis();
		log.debug("Classifying peptides");
		classifyPeptides();
		// long t3 = System.currentTimeMillis();
		log.debug("Classifying proteins");
		classifyProteins();
		// long t4 = System.currentTimeMillis();
		log.debug("creating groups");
		createGroups();
		// long t5 = System.currentTimeMillis();
		log.debug("marking indistinguibles");
		markIndistinguishable();
		// long t6 = System.currentTimeMillis();

		if (!separateNonConclusiveProteins) {
			log.info("Returning " + mGroups.size() + " protein groups from " + proteins.size() + " proteins, in "
					+ DatesUtil.getDescriptiveTimeFromMillisecs(System.currentTimeMillis() - t1));
			log.info("Collapsing Non conclusive groups");
			collapseNonConclusiveGroups();

		}

		log.debug("Extracting groups");
		ArrayList<ProteinGroup> resultingGroups = extractProteinGroups();
		// long t7 = System.currentTimeMillis();

		// SorterUtil.sortProteinGroupsByAccession(resultingGroups);
		// this.fullGroupDump(System.out);
		// log.debug("Freeing memory");
		// Runtime.getRuntime().gc();
		// long t8 = System.currentTimeMillis();
		// log.info("Panalyzer times: " + (t2 - t1) + ", " + (t3 - t2) + ", "
		// + (t4 - t3) + ", " + (t5 - t4) + ", " + (t6 - t5) + ", "
		// + (t7 - t6) + ", " + (t8 - t7));

		// check test
		for (ProteinGroup proteinGroup : resultingGroups) {
			if (proteinGroup.getEvidence() == ProteinEvidence.INDISTINGUISHABLE) {
				for (ExtendedIdentifiedProtein extendedIdentifiedProtein : proteinGroup) {
					if (extendedIdentifiedProtein.getEvidence() == ProteinEvidence.CONCLUSIVE) {
						log.info(proteinGroup);
					}
				}
			}
		}
		log.info("Returning " + mGroups.size() + " protein groups from " + proteins.size() + " proteins, in "
				+ (System.currentTimeMillis() - t1) + " milliseconds");
		return resultingGroups;
	}

	private void collapseNonConclusiveGroups() {
		final Iterator<ProteinGroupInference> proteinGroupIterator = mGroups.iterator();
		while (proteinGroupIterator.hasNext()) {
			ProteinGroupInference proteinGroup = proteinGroupIterator.next();

			if (proteinGroup.getEvidence() == ProteinEvidence.NONCONCLUSIVE) {
				InferenceProtein nonConclusiveProtein = proteinGroup.get(0);
				// non conclusive groups only have one protein at index = 0
				final int nonConclusiveProteinHashCode = nonConclusiveProtein.hashCode();
				final List<InferencePeptide> peptides = nonConclusiveProtein.getInferencePeptides();
				// grab all other protein groups in which that protein is
				// sharing non discriminating peptides
				Set<ProteinGroupInference> otherProteinGroups = new THashSet<ProteinGroupInference>();
				for (InferencePeptide peptide : peptides) {
					if (peptide.getRelation() == PeptideRelation.NONDISCRIMINATING) {
						final List<InferenceProtein> proteinsSharingThisPeptide = peptide.getInferenceProteins();
						for (InferenceProtein proteinSharingNonDiscriminatingPeptide : proteinsSharingThisPeptide) {
							if (proteinSharingNonDiscriminatingPeptide.hashCode() != nonConclusiveProteinHashCode) {
								if (!proteinSharingNonDiscriminatingPeptide.getGroup().equals(proteinGroup)
										&& proteinSharingNonDiscriminatingPeptide.getGroup()
												.getEvidence() != ProteinEvidence.NONCONCLUSIVE) {
									// otherProteinGroups.add(proteinSharingNonDiscriminatingPeptide.getGroup());
									proteinSharingNonDiscriminatingPeptide.getGroup().add(nonConclusiveProtein);
									nonConclusiveProtein.setGroup(proteinSharingNonDiscriminatingPeptide.getGroup());
								}
								// protein.setEvidence(proteinSharingThisPeptide
								// .getGroup().getEvidence());
							}
						}
					}
				}
				// // if there is a conclusive protein group, add the non
				// ProteinGroup conclusiveProteinGroup =
				// proteinSharingThisPeptide.getGroup().add(nonConclusiveProtein);
				// nonConclusiveProtein.setGroup(proteinSharingThisPeptide.getGroup());
				// break;

				proteinGroupIterator.remove();
			}
		}
	}

	private ArrayList<ProteinGroup> extractProteinGroups() {
		ArrayList<ProteinGroup> ret = new ArrayList<ProteinGroup>();

		for (ProteinGroupInference iProteinGroup : mGroups) {
			ProteinGroup pg = new ProteinGroup(iProteinGroup);
			ret.add(pg);
		}

		return ret;
	}

	private void createInferenceMaps(Collection<ExtendedIdentifiedProtein> proteins) {
		InferenceProtein iProt = null;
		InferencePeptide iPept = null;
		TIntHashSet proteinIds = new TIntHashSet();

		for (ExtendedIdentifiedProtein prot : proteins) {
			if (ignoreProteinId || !proteinIds.contains(prot.getId())) {
				proteinIds.add(prot.getId());
				iProt = mProts.get(prot.getAccession());
				if (iProt == null) {
					iProt = new InferenceProtein(prot);
					mProts.put(iProt.getAccession(), iProt);
				} else {
					// merge in a common inference protein
					iProt.addExtendedProtein(prot);
				}

				List<ExtendedIdentifiedPeptide> peptides = prot.getPeptides();
				if (peptides != null && !peptides.isEmpty()) {
					for (ExtendedIdentifiedPeptide pept : peptides) {
						iPept = mPepts.get(pept.getSequence());
						if (iPept == null) {
							iPept = new InferencePeptide(pept);
							mPepts.put(pept.getSequence(), iPept);
						} else {
							// merge in a common inference peptide
							iPept.addExtendedPeptide(pept);
						}
						if (!iPept.getInferenceProteins().contains(iProt))
							iPept.getInferenceProteins().add(iProt);
						if (!iProt.getInferencePeptides().contains(iPept))
							iProt.getInferencePeptides().add(iPept);
					}
				} else {
					// this is a warning just in case minPeptideLength is not
					// MAXVALUE because in that case is because we dont want to
					// load the peptides (checking integrity of the project
					// step)
					if (DataManager.getMinPeptideLength() != Integer.MAX_VALUE)
						log.warn(prot.getId() + " no tiene peptidos! ");
				}
			} else {
				log.debug("This protein is already taken ");
			}

		}
	}

	private void classifyPeptides() {
		// Locate unique peptides
		for (InferencePeptide pept : mPepts.values()) {
			if (pept.getInferenceProteins().size() == 1) {
				pept.setRelation(PeptideRelation.UNIQUE);
				pept.getInferenceProteins().get(0).setEvidence(ProteinEvidence.CONCLUSIVE);
			} else {
				pept.setRelation(PeptideRelation.DISCRIMINATING);
			}
		}
		// Locate non-meaningful peptides (first round)
		for (InferenceProtein prot : mProts.values()) {
			if (prot.getEvidence() == ProteinEvidence.CONCLUSIVE) {
				// if conclusive is because they have a unique peptide
				for (InferencePeptide pept : prot.getInferencePeptides()) {
					if (pept.getRelation() != PeptideRelation.UNIQUE) {
						pept.setRelation(PeptideRelation.NONDISCRIMINATING);
					}
				}
			}
		}

		// Locate non-meaningful peptides (second round)
		boolean shared;
		for (InferencePeptide pept : mPepts.values()) {
			if (pept.getRelation() != PeptideRelation.DISCRIMINATING) {
				continue;
			}
			for (InferencePeptide pept2 : pept.getInferenceProteins().get(0).getInferencePeptides()) {
				if (pept2.getRelation() == PeptideRelation.NONDISCRIMINATING) {
					continue;
				}
				if (pept2.getInferenceProteins().size() <= pept.getInferenceProteins().size()) {
					continue;
				}
				shared = true;
				for (InferenceProtein p : pept.getInferenceProteins()) {
					if (!p.getInferencePeptides().contains(pept2)) {
						shared = false;
						break;
					}
				}
				if (shared) {
					pept2.setRelation(PeptideRelation.NONDISCRIMINATING);
				}
			}
		}
	}

	private void classifyProteins() {
		boolean group;

		for (InferenceProtein prot : mProts.values()) {
			if (prot.getEvidence() == ProteinEvidence.CONCLUSIVE) {
				continue;
			}
			List<InferencePeptide> peptides = prot.getInferencePeptides();
			if (peptides == null || peptides.isEmpty()) {
				prot.setEvidence(ProteinEvidence.FILTERED);
				continue;
			}

			group = false;
			for (InferencePeptide pept : peptides) {
				if (pept.getRelation() == PeptideRelation.DISCRIMINATING) {
					group = true;
					break;
				}
			}
			prot.setEvidence(group ? ProteinEvidence.AMBIGUOUSGROUP : ProteinEvidence.NONCONCLUSIVE);
		}

	}

	private void createGroups() {
		for (InferenceProtein prot : mProts.values()) {
			if (prot.getGroup() == null) {
				ProteinGroupInference group = new ProteinGroupInference(prot.getEvidence());
				group.add(prot);
				prot.setGroup(group);
				mGroups.add(group);
			}
			if (prot.getEvidence() != ProteinEvidence.AMBIGUOUSGROUP) {
				continue;
			}
			for (InferencePeptide pept : prot.getInferencePeptides()) {
				if (pept.getRelation() != PeptideRelation.DISCRIMINATING) {
					continue;
				}
				for (InferenceProtein subp : pept.getInferenceProteins()) {
					if (subp.getEvidence() != ProteinEvidence.AMBIGUOUSGROUP) {
						continue;
					}
					if (subp.getGroup() != null) { // merge groups
						if (subp.getGroup() == prot.getGroup()) {
							continue;
						}
						mGroups.remove(prot.getGroup());
						subp.getGroup().addAll(prot.getGroup());
						for (InferenceProtein pg : prot.getGroup()) {
							pg.setGroup(subp.getGroup());
						}
						continue;
					}
					prot.getGroup().add(subp);
					subp.setGroup(prot.getGroup());
				}
			}
		}
	}

	// private void markIndistinguishable() {
	// boolean indistinguishable;
	// for (ProteinGroupInference group : mGroups) {
	// if (group.getEvidence() != ProteinEvidence.AMBIGUOUSGROUP)
	// continue;
	// indistinguishable = true;
	// for (InferencePeptide pept : group.get(0).getInferencePeptides()) {
	// if (pept.getRelation() != PeptideRelation.DISCRIMINATING)
	// continue;
	// for (InferenceProtein prot : group) {
	// if (!prot.getInferencePeptides().contains(pept)) {
	// indistinguishable = false;
	// break;
	// }
	// }
	// if (!indistinguishable)
	// break;
	// }
	// if (indistinguishable)
	// group.setEvidence(ProteinEvidence.INDISTINGUISHABLE);
	// }
	//
	// }
	private void markIndistinguishable() {
		Set<InferencePeptide> discriminating = new THashSet<InferencePeptide>();
		boolean indistinguishable;
		for (ProteinGroupInference group : mGroups) {
			if (group.getEvidence() != ProteinEvidence.AMBIGUOUSGROUP || group.size() < 2)
				continue;
			indistinguishable = true;
			for (InferenceProtein prot : group)
				for (InferencePeptide pept : prot.getInferencePeptides())
					if (pept.getRelation() == PeptideRelation.DISCRIMINATING)
						discriminating.add(pept);
			for (InferenceProtein prot : group)
				if (!prot.getInferencePeptides().containsAll(discriminating)) {
					indistinguishable = false;
					break;
				}
			discriminating.clear();
			if (indistinguishable) {
				group.setEvidence(ProteinEvidence.INDISTINGUISHABLE);
				for (InferenceProtein prot : group)
					prot.setEvidence(ProteinEvidence.INDISTINGUISHABLE);
			}
		}
	}

	public PanalyzerStats getStats(List<ProteinGroupInference> groups) {
		return new PanalyzerStats(groups);
	}

	public PanalyzerStats getStats() {
		if (mStats == null)
			mStats = getStats(mGroups);
		return mStats;
	}

	// Change to your own path
	private static String miapePath = "/home/gorka/MyProjects/Salva/MIAPE_MSI_4619.xml";
	private static ControlVocabularyManager cvManager = new LocalOboTestControlVocabularyManager();

	// public static void main(String[] args) {
	// File miapeFile = new File(miapePath);
	//
	// Replicate replicate = new Replicate("rep", "exp", null,
	// getMiapeMSI(miapeFile), null, null, cvManager, true);
	// checkGroups(replicate.getIdentifiedProteinGroups());
	// }

	private static List<MiapeMSIDocument> getMiapeMSI(File miapeMSI) {
		List<MiapeMSIDocument> miapeMSIs = new ArrayList<MiapeMSIDocument>();
		MiapeXmlFile<MiapeMSIDocument> xmlFile = new MIAPEMSIXmlFile(miapeMSI);
		try {
			miapeMSIs.add(MiapeMSIXmlFactory.getFactory().toDocument(xmlFile, cvManager, null, null, null));
		} catch (MiapeDatabaseException e) {
			e.printStackTrace();
		} catch (MiapeSecurityException e) {
			e.printStackTrace();
		}
		return miapeMSIs;
	}

	public static void checkGroups(List<ProteinGroup> groups) {
		for (ProteinGroup proteinGroup : groups) {
			if (proteinGroup.getEvidence() == ProteinEvidence.AMBIGUOUSGROUP)
				Assert.assertTrue(proteinGroup.size() > 1);
			if (proteinGroup.getEvidence() == ProteinEvidence.CONCLUSIVE)
				Assert.assertTrue(proteinGroup.size() == 1);
			if (proteinGroup.getEvidence() == ProteinEvidence.INDISTINGUISHABLE)
				Assert.assertTrue(proteinGroup.size() > 1);
			if (proteinGroup.getEvidence() == ProteinEvidence.NONCONCLUSIVE)
				Assert.assertTrue(proteinGroup.size() == 1);
			if (proteinGroup.getEvidence() == ProteinEvidence.FILTERED)
				Assert.assertTrue(proteinGroup.size() == 1);
		}
	}
}
