package org.proteored.miapeapi.util;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.proteored.miapeapi.exceptions.MiapeDataInconsistencyException;
import org.proteored.miapeapi.interfaces.msi.IdentifiedPeptide;
import org.proteored.miapeapi.interfaces.msi.IdentifiedProtein;
import org.proteored.miapeapi.interfaces.msi.IdentifiedProteinSet;
import org.proteored.miapeapi.interfaces.msi.MiapeMSIDocument;

import edu.scripps.yates.utilities.progresscounter.ProgressCounter;
import edu.scripps.yates.utilities.progresscounter.ProgressPrintingType;
import gnu.trove.set.hash.THashSet;

public class ProteinPeptideConsistencyChecker {
	private static final Logger log = Logger.getLogger(ProteinPeptideConsistencyChecker.class);

	public static void checkProteinPeptideConsistency(MiapeMSIDocument miapeMSI) {
		log.info("Checking protein-peptide consistency");
		final List<IdentifiedPeptide> peptides = miapeMSI.getIdentifiedPeptides();

		if (peptides != null && !peptides.isEmpty()) {
			// store them in a set, which is going to be much faster to check
			// whether a peptide is in there or not
			final Set<IdentifiedPeptide> peptideSet = new THashSet<IdentifiedPeptide>();
			peptideSet.addAll(peptides);
			final Set<IdentifiedProteinSet> identifiedProteinSets = miapeMSI.getIdentifiedProteinSets();
			if (identifiedProteinSets != null) {
				for (final IdentifiedProteinSet identifiedProteinSet : identifiedProteinSets) {
					final Map<String, IdentifiedProtein> proteinMap = identifiedProteinSet.getIdentifiedProteins();
					if (proteinMap == null || proteinMap.isEmpty()) {
						throw new MiapeDataInconsistencyException("Error, proteinSet is empty or null");
					}
					final ProgressCounter counter = new ProgressCounter(proteinMap.size(),
							ProgressPrintingType.PERCENTAGE_STEPS, 0);
					for (final String acc : proteinMap.keySet()) {
						counter.increment();
						final String printIfNecessary = counter.printIfNecessary();
						if (!"".equals(printIfNecessary)) {
							log.info(printIfNecessary);
						}
						final IdentifiedProtein protein = proteinMap.get(acc);
						try {
							checkProtein(protein, peptideSet);
						} catch (final MiapeDataInconsistencyException e) {
							e.printStackTrace();
							log.error(e.getMessage());
							throw e;
						}
					}
				}
			}
			final ProgressCounter counter = new ProgressCounter(peptides.size(), ProgressPrintingType.PERCENTAGE_STEPS,
					0);
			for (final IdentifiedPeptide peptide : peptides) {
				counter.increment();
				final String printIfNecessary = counter.printIfNecessary();
				if (!"".equals(printIfNecessary)) {
					log.info(printIfNecessary);
				}
				try {
					checkPeptide(peptide);
				} catch (final MiapeDataInconsistencyException e) {
					e.printStackTrace();
					log.error(e.getMessage());
					throw e;
				}
			}
		}
		log.info("protein-peptide consistency is just fine");
	}

	private static void checkPeptide(IdentifiedPeptide peptide) {
		// check that its proteins have the peptide on them
		final List<IdentifiedProtein> identifiedProteins = peptide.getIdentifiedProteins();
		if (identifiedProteins != null && !identifiedProteins.isEmpty()) {
			for (final IdentifiedProtein protein : identifiedProteins) {
				if (!protein.getIdentifiedPeptides().contains(peptide)) {
					throw new MiapeDataInconsistencyException(
							getProteinInfo(protein) + " has not " + getPeptideInfo(peptide) + " assigned");
				}
			}
		} else {
			throw new MiapeDataInconsistencyException(getPeptideInfo(peptide) + " has no proteins");
		}
	}

	private static void checkProtein(IdentifiedProtein protein, Set<IdentifiedPeptide> totalPeptides) {
		final List<IdentifiedPeptide> peptides = protein.getIdentifiedPeptides();
		if (peptides != null && !peptides.isEmpty()) {
			for (final IdentifiedPeptide peptide : peptides) {
				// check that the peptide has this protein in his list
				if (!peptide.getIdentifiedProteins().contains(protein)) {
					throw new MiapeDataInconsistencyException(
							getPeptideInfo(peptide) + " has not " + getProteinInfo(protein) + " assigned");
				}
				// check that the peptide is in the total list
				if (!totalPeptides.contains(peptide)) {
					throw new MiapeDataInconsistencyException(
							getPeptideInfo(peptide) + " is not in the total peptide list of the document");
				}
			}
		} else {
			throw new MiapeDataInconsistencyException(
					"Protein " + protein.getId() + "-" + protein.getAccession() + " with no peptides");
		}

	}

	private static String getProteinInfo(IdentifiedProtein protein) {
		return "protein " + protein.getId() + "-" + protein.getAccession();
	}

	private static String getPeptideInfo(IdentifiedPeptide peptide) {
		return "peptide " + peptide.getId() + " - spectrumRef:" + peptide.getSpectrumRef() + " - sequence:"
				+ peptide.getSequence();
	}
}
