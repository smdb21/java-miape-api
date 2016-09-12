package org.proteored.miapeapi.experiment.model.filters;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.proteored.miapeapi.experiment.model.ExtendedIdentifiedProtein;
import org.proteored.miapeapi.experiment.model.IdentificationSet;
import org.proteored.miapeapi.experiment.model.ProteinGroup;
import org.proteored.miapeapi.experiment.model.grouping.PAnalyzer;
import org.proteored.miapeapi.interfaces.Software;
import org.proteored.miapeapi.util.UniprotId2AccMapping;

import com.compomics.dbtoolkit.io.implementations.FASTADBLoader;
import com.compomics.util.protein.Protein;

public class ProteinACCFilter implements Filter {
	private final Set<String> accessions = new HashSet<String>();
	private final Software software;
	private final List<String> sortedAccessions = new ArrayList<String>();;
	private static Logger log = Logger.getLogger("log4j.logger.org.proteored");

	public ProteinACCFilter(Collection<String> accessions, Software software) {
		// check if some accession is an ID
		for (String acc : accessions) {
			try {
				final UniprotId2AccMapping instance = UniprotId2AccMapping
						.getInstance();
				if (instance != null) {
					String accFromID = instance.getAccFromID(acc);
					if (accFromID != null) {
						this.accessions.add(accFromID);
						sortedAccessions.add(accFromID);
					} else {
						this.accessions.add(acc);
						sortedAccessions.add(acc);
					}
				}
			} catch (IOException e) {
				this.accessions.addAll(accessions);
				sortedAccessions.addAll(accessions);
			}
		}

		this.software = software;
	}

	public ProteinACCFilter(File fastaFile, Software software)
			throws IOException {
		FASTADBLoader fastaLoader = new FASTADBLoader();
		fastaLoader.load(fastaFile.getAbsolutePath());
		final long countNumberOfEntries = fastaLoader.countNumberOfEntries();
		for (int i = 0; i < countNumberOfEntries; i++) {
			final Protein nextProtein = fastaLoader.nextProtein();
			if (nextProtein == null)
				break;
			accessions.add(nextProtein.getHeader().getAccession());
			sortedAccessions.add(nextProtein.getHeader().getAccession());
		}
		this.software = software;
		log.info("Loaded " + accessions.size() + " proteins from "
				+ fastaFile.getAbsolutePath());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj instanceof ProteinACCFilter) {
			ProteinACCFilter filter = (ProteinACCFilter) obj;
			if (filter.accessions.size() != accessions.size())
				return false;
			for (String accession : accessions) {
				if (!filter.accessions.contains(accession))
					return false;
			}
			return true;
		} else
			return super.equals(obj);
	}

	@Override
	public List<ProteinGroup> filter(List<ProteinGroup> proteinGroups,
			IdentificationSet currentIdSet) {
		List<ProteinGroup> ret = new ArrayList<ProteinGroup>();
		if (accessions != null && !accessions.isEmpty()) {
			log.info("Filtering " + proteinGroups.size()
					+ " protein groups by a list of protein Accessions of "
					+ accessions.size() + " proteins");
			List<ExtendedIdentifiedProtein> proteins = new ArrayList<ExtendedIdentifiedProtein>();
			if (proteinGroups != null) {
				for (ProteinGroup proteinGroup : proteinGroups) {
					for (ExtendedIdentifiedProtein protein : proteinGroup) {
						if (accessions.contains(protein.getAccession())) {
							proteins.add(protein);
						} else {
							log.debug("Protein " + protein.getAccession()
									+ " is not a human protein??");
						}
					}
				}
				log.info("Running PAnalyzer before to return the groups in the protein acc filter");
				PAnalyzer panalyzer = new PAnalyzer();
				ret = panalyzer.run(proteins);
				log.info("Filtered from " + proteinGroups.size() + " to "
						+ ret.size() + " protein groups");
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
		return "Proteins filtered by a list of " + accessions.size()
				+ " protein accessions";
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

}
