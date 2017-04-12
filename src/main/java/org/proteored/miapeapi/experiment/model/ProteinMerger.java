package org.proteored.miapeapi.experiment.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.proteored.miapeapi.experiment.model.filters.FDRFilter;
import org.proteored.miapeapi.util.ProteinSequenceRetrieval;
import org.proteored.miapeapi.xml.util.MiapeXmlUtil;

import edu.scripps.yates.annotations.uniprot.UniprotProteinLocalRetriever;

public class ProteinMerger {
	private static final Logger log = Logger.getLogger("log4j.logger.org.proteored");

	/**
	 * Gets the mean of the protein coverages of the proteins in the
	 * {@link ProteinGroupOccurrence}
	 * 
	 * @param proteinGroupOccurrence
	 * @param filter
	 *            can be null, is just to see if the protein is decoy or not (in
	 *            case of yes, do not search the sequence)
	 * @param retrieveProteinSeqIfNotAvailable
	 *            if true, the protein sequences will be retrieved from the
	 *            internet if they are not available in the
	 *            {@link ExtendedIdentifiedProtein} objects of the
	 *            {@link ProteinGroupOccurrence}
	 * @param upr
	 * @return
	 */
	public static String getCoverage(ProteinGroupOccurrence proteinGroupOccurrence, FDRFilter filter,
			boolean retrieveProteinSeqIfNotAvailable, UniprotProteinLocalRetriever upr) {
		List<Double> coverages = new ArrayList<Double>();
		if (proteinGroupOccurrence != null) {
			List<String> accessions = proteinGroupOccurrence.getAccessions();
			ProteinSequenceRetrieval.getProteinSequence(accessions, retrieveProteinSeqIfNotAvailable, upr);
			for (String accession : accessions) {
				final List<ExtendedIdentifiedProtein> proteins = proteinGroupOccurrence.getProteins(accession);
				// take the peptides of all ocurrences of the same protein
				List<ExtendedIdentifiedPeptide> peptides = new ArrayList<ExtendedIdentifiedPeptide>();
				for (ExtendedIdentifiedProtein protein : proteins) {
					peptides.addAll(protein.getPeptides());
				}

				if (proteinGroupOccurrence.isDecoy() || (filter != null && filter.isDecoy(accession)))
					continue;

				// Get the protein sequence
				String sequence = null;
				for (ExtendedIdentifiedProtein protein : proteins) {
					sequence = protein.getProteinSequence();
					if (sequence != null && !"".equals(sequence))
						break;
				}

				// if the protein sequence is not stored yet, retrieve from
				// the internet
				if (sequence == null) {
					sequence = ProteinSequenceRetrieval.getProteinSequence(accession, retrieveProteinSeqIfNotAvailable,
							upr);
					if (sequence != null)
						// set to all proteins
						for (ExtendedIdentifiedProtein protein : proteins) {
						protein.setProteinSequence(sequence);
						}
				}

				// Calculate the protein coverage using all the peptides
				Double coverage = MiapeXmlUtil.calculateProteinCoverage(sequence, peptides);
				if (coverage != null) {
					coverages.add(coverage);
				}

			}
			if (!coverages.isEmpty()) {
				// make the mean of the coverages
				Double meanCoverage = getMean(coverages);

				return meanCoverage.toString();
			}
		}
		return null;
	}

	/**
	 * Gets the mean of the protein coverage of the proteins in the
	 * {@link ProteinGroup}s
	 * 
	 * @param proteinGroup
	 * @param filter
	 *            can be null, is just to see if the protein is decoy or not (in
	 *            case of yes, do not search the sequence)
	 * @param retrieveProteinSeqIfNotAvailable
	 *            if true, the protein sequences will be retrieved from the
	 *            internet if they are not available in the
	 *            {@link ExtendedIdentifiedProtein} objects of the
	 *            {@link ProteinGroup}
	 * @return
	 */
	public static String getCoverage(ProteinGroup proteinGroup, FDRFilter filter,
			boolean retrieveProteinSeqIfNotAvailable, UniprotProteinLocalRetriever upr) {
		List<Double> coverages = new ArrayList<Double>();
		if (proteinGroup != null) {
			List<String> accessions = proteinGroup.getAccessions();
			for (String accession : accessions) {
				// take the peptides of all ocurrences of the same protein
				List<ExtendedIdentifiedPeptide> peptides = new ArrayList<ExtendedIdentifiedPeptide>();
				for (ExtendedIdentifiedProtein protein : proteinGroup) {
					peptides.addAll(protein.getPeptides());
				}

				if (proteinGroup.isDecoy() || (filter != null && filter.isDecoy(accession)))
					continue;

				// Get the protein sequence
				String sequence = null;
				for (ExtendedIdentifiedProtein protein : proteinGroup) {
					sequence = protein.getProteinSequence();
					if (sequence != null && !"".equals(sequence))
						continue;
				}

				// if the protein sequence is not stored yet, retrieve from
				// the internet
				if (sequence == null) {

					sequence = ProteinSequenceRetrieval.getProteinSequence(accession, retrieveProteinSeqIfNotAvailable,
							upr);

					// set to all proteins
					for (ExtendedIdentifiedProtein protein : proteinGroup) {
						protein.setProteinSequence(sequence);
					}
				}

				// Calculate the protein coverage using all the peptides
				Double coverage = MiapeXmlUtil.calculateProteinCoverage(sequence, peptides);
				if (coverage != null) {
					coverages.add(coverage);

				}

			}
			if (!coverages.isEmpty()) {
				// make the mean of the coverages
				Double meanCoverage = getMean(coverages);

				return meanCoverage.toString();
			}
		}
		return null;
	}

	private static Double getMean(List<Double> doubles) {
		if (doubles.isEmpty())
			return 0.0;
		int total = doubles.size();
		Double sum = 0.0;
		for (Double double1 : doubles) {
			sum = sum + double1;
		}
		return sum / total;

	}

}
