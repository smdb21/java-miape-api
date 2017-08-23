package org.proteored.miapeapi.experiment.model.filters;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.proteored.miapeapi.experiment.model.ExtendedIdentifiedPeptide;
import org.proteored.miapeapi.experiment.model.ExtendedIdentifiedProtein;
import org.proteored.miapeapi.experiment.model.IdentificationSet;
import org.proteored.miapeapi.experiment.model.ProteinGroup;
import org.proteored.miapeapi.experiment.model.grouping.PAnalyzer;
import org.proteored.miapeapi.interfaces.Software;
import org.proteored.miapeapi.util.UniprotId2AccMapping;

import com.compomics.dbtoolkit.io.implementations.FASTADBLoader;
import com.compomics.util.protein.Protein;

import gnu.trove.set.hash.THashSet;

public class ProteinACCFilter implements Filter, Filters<String> {
	private final Set<String> accessions = new THashSet<String>();
	private final Software software;
	private final List<String> sortedAccessions = new ArrayList<String>();
	private File fastaFile;
	private boolean filterReady;
	private final boolean separateNonConclusiveProteins;
	private final boolean doNotGroupNonConclusiveProteins;
	private static Logger log = Logger.getLogger("log4j.logger.org.proteored");

	public ProteinACCFilter(Collection<String> proteinComparatorKeys, boolean doNotGroupNonConclusiveProteins,
			boolean separateNonConclusiveProteins) {
		// check if some accession is an ID
		this.accessions.addAll(proteinComparatorKeys);
		for (String proteinComparatorKey : proteinComparatorKeys) {
			this.sortedAccessions.add(proteinComparatorKey);
		}

		this.software = null;
		filterReady = true;
		this.separateNonConclusiveProteins = separateNonConclusiveProteins;
		this.doNotGroupNonConclusiveProteins = doNotGroupNonConclusiveProteins;
	}

	public ProteinACCFilter(Collection<String> accessions, boolean doNotGroupNonConclusiveProteins,
			boolean separateNonConclusiveProteins, Software software) {
		this.separateNonConclusiveProteins = separateNonConclusiveProteins;
		this.doNotGroupNonConclusiveProteins = doNotGroupNonConclusiveProteins;
		// check if some accession is an ID
		for (String acc : accessions) {
			// ProteinComparatorKey pck = new ProteinComparatorKey(acc,
			// ProteinGroupComparisonType.BEST_PROTEIN);
			try {
				final UniprotId2AccMapping instance = UniprotId2AccMapping.getInstance();
				if (instance != null) {
					String accFromID = instance.getAccFromID(acc);
					if (accFromID != null) {
						// pck = new ProteinComparatorKey(accFromID,
						// ProteinGroupComparisonType.BEST_PROTEIN);
						// this.accessions.add(pck);
						this.accessions.add(accFromID);
						sortedAccessions.add(accFromID);
					} else {
						// this.accessions.add(pck);
						this.accessions.add(accFromID);
						sortedAccessions.add(acc);
					}
				}
			} catch (IOException e) {
				log.error(e);
			}
		}
		filterReady = true;
		this.software = software;
	}

	public ProteinACCFilter(File fastaFile, boolean doNotGroupNonConclusiveProteins,
			boolean separateNonConclusiveProteins, Software software) throws IOException {
		this.fastaFile = fastaFile;
		this.separateNonConclusiveProteins = separateNonConclusiveProteins;
		this.doNotGroupNonConclusiveProteins = doNotGroupNonConclusiveProteins;
		if (!fastaFile.exists()) {
			throw new IOException("File " + fastaFile.getAbsolutePath() + " not found");
		}
		this.software = software;
		// not ready yet
		filterReady = false;

	}

	private void getReady() {
		if (!filterReady) {
			if (fastaFile != null) {
				FASTADBLoader fastaLoader = new FASTADBLoader();
				try {
					fastaLoader.load(fastaFile.getAbsolutePath());

					final long countNumberOfEntries = fastaLoader.countNumberOfEntries();
					for (int i = 0; i < countNumberOfEntries; i++) {
						final Protein nextProtein = fastaLoader.nextProtein();
						if (nextProtein == null) {
							break;
						}
						String accession = nextProtein.getHeader().getAccession();
						if (accession == null) {
							accession = nextProtein.getHeader().getID();
						}
						if (accession == null) {
							accession = nextProtein.getHeader().getAbbreviatedFASTAHeader();
						}
						if (accession == null) {
							accession = nextProtein.getHeader().toString();
						}
						// accessions.add(new ProteinComparatorKey(accession,
						// ProteinGroupComparisonType.BEST_PROTEIN));
						accessions.add(accession);
						sortedAccessions.add(accession);
					}

					log.info("Loaded " + accessions.size() + " proteins from " + fastaFile.getAbsolutePath());
				} catch (IOException e) {
					e.printStackTrace();
					log.error(e);
				}
			}
			filterReady = true;
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj instanceof ProteinACCFilter) {
			if (obj == this) {
				return true;
			}
			getReady();
			ProteinACCFilter filter = (ProteinACCFilter) obj;
			if (filter.accessions.size() != accessions.size())
				return false;
			for (String accession : accessions) {
				if (!filter.accessions.contains(accession))
					return false;
			}
			return true;
		} else {
			return super.equals(obj);
		}
	}

	@Override
	public List<ProteinGroup> filter(List<ProteinGroup> proteinGroups, IdentificationSet currentIdSet) {
		getReady();
		List<ProteinGroup> ret = new ArrayList<ProteinGroup>();
		if (accessions != null && !accessions.isEmpty()) {
			log.info("Filtering " + proteinGroups.size() + " protein groups by a list of protein Accessions of "
					+ accessions.size() + " proteins");
			List<ExtendedIdentifiedProtein> proteins = new ArrayList<ExtendedIdentifiedProtein>();
			if (proteinGroups != null) {
				for (ProteinGroup proteinGroup : proteinGroups) {
					boolean proteinGroupIsValid = true;
					List<ExtendedIdentifiedProtein> proteinsTMP = new ArrayList<ExtendedIdentifiedProtein>();
					for (ExtendedIdentifiedProtein protein : proteinGroup) {
						if (isValid(protein.getAccession())) {
							proteinsTMP.add(protein);
						} else {
							proteinGroupIsValid = false;
							// unlink peptides to that protein
							List<ExtendedIdentifiedPeptide> peptides = protein.getPeptides();
							for (ExtendedIdentifiedPeptide peptide : peptides) {
								peptide.getProteins().remove(protein);
								if (peptide.getProteins().isEmpty()) {
									log.info(peptide + " has no peptides");
								}
							}
						}
					}
					if (!proteinsTMP.isEmpty()) {
						if (proteinGroupIsValid) {
							// all proteins are valid, so we dont need to redo
							// the group
							ret.add(proteinGroup);
						} else {
							// add them to make the groups
							proteins.addAll(proteinsTMP);
						}
					}
				}
				log.info(ret.size() + " proteins groups remain untouched");
				log.info("Running PAnalyzer to group " + proteins.size()
						+ " before to return the groups in the protein acc filter");
				PAnalyzer panalyzer = new PAnalyzer(doNotGroupNonConclusiveProteins, separateNonConclusiveProteins);
				ret.addAll(panalyzer.run(proteins));
				log.info("Filtered from " + proteinGroups.size() + " to " + ret.size() + " protein groups");
			}

		}
		return ret;
	}

	@Override
	public boolean appliedToProteins() {
		return true;
	}

	@Override
	public boolean appliedToPeptides() {
		return false;
	}

	@Override
	public String toString() {
		return "Proteins filtered by a list of " + accessions.size() + " protein accessions";
	}

	@Override
	public Software getSoftware() {
		return software;
	}

	/**
	 * Gets the lists of accessions as were introduced in the filter
	 * 
	 * @return
	 */
	public List<String> getSortedAccessions() {
		return sortedAccessions;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.proteored.miapeapi.experiment.model.filters.Filters#isValid(java.lang
	 * .Object)
	 */
	@Override
	public boolean isValid(String acc) {
		getReady();
		return accessions.contains(acc);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.proteored.miapeapi.experiment.model.filters.Filters#canCheck(java.
	 * lang.Object)
	 */
	@Override
	public boolean canCheck(Object obj) {
		return obj instanceof String;
	}

}
