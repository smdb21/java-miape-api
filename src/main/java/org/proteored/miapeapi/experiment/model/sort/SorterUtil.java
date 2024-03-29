package org.proteored.miapeapi.experiment.model.sort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.proteored.miapeapi.exceptions.IllegalMiapeArgumentException;
import org.proteored.miapeapi.experiment.model.ExtendedIdentifiedPeptide;
import org.proteored.miapeapi.experiment.model.ExtendedIdentifiedProtein;
import org.proteored.miapeapi.experiment.model.PeptideOccurrence;
import org.proteored.miapeapi.experiment.model.ProteinGroup;
import org.proteored.miapeapi.experiment.model.ProteinGroupOccurrence;
import org.proteored.miapeapi.experiment.model.filters.Filter;
import org.proteored.miapeapi.interfaces.msi.IdentifiedPeptide;
import org.proteored.miapeapi.interfaces.msi.IdentifiedProtein;

import gnu.trove.map.hash.TObjectIntHashMap;

/**
 * Class that manages the sortings of identification lists
 * 
 * @author Salva
 * 
 */
public class SorterUtil {
	private static final Logger log = Logger.getLogger("log4j.logger.org.proteored");

	private static Float getPeptideOccurrenceBestScore(PeptideOccurrence peptideOccurrence) {
		if (peptideOccurrence != null)
			return peptideOccurrence.getBestPeptideScore();

		return null;
	}

	public static void sortProteinsByBestProteinScore(List<ExtendedIdentifiedProtein> identifiedProteins,
			boolean parallel) {

		log.debug("Sorting " + identifiedProteins.size() + " proteins by protein score");
		final long t1 = System.currentTimeMillis();

		if (parallel)
			ParallelSort.parallel_sort(identifiedProteins, ComparatorManager.getComparatorByProteinScore());
		else
			Collections.sort(identifiedProteins, ComparatorManager.getComparatorByProteinScore());
		final long t2 = System.currentTimeMillis();
		log.debug(identifiedProteins.size() + " proteins sorted by protein score in " + (double) (t2 - t1) / 1000
				+ " seg");

	}

	public static void sortProteinsByProteinScore(List<ExtendedIdentifiedProtein> identifiedProteins, String scoreName,
			boolean parallel) {

		log.debug("Sorting " + identifiedProteins.size() + " proteins by protein score");
		final long t1 = System.currentTimeMillis();

		if (parallel)
			ParallelSort.parallel_sort(identifiedProteins, ComparatorManager.getComparatorByProteinScore(scoreName));
		else
			Collections.sort(identifiedProteins, ComparatorManager.getComparatorByProteinScore(scoreName));
		final long t2 = System.currentTimeMillis();
		log.debug(identifiedProteins.size() + " proteins sorted by protein score in " + (double) (t2 - t1) / 1000
				+ " seg");

	}

	public static void sortProteinsByBestPeptideScore(List<ExtendedIdentifiedProtein> identifiedProteins) {

		log.info("Sorting " + identifiedProteins.size() + " proteins by best peptide score");
		final long t1 = System.currentTimeMillis();

		ParallelSort.parallel_sort(identifiedProteins, ComparatorManager.getComparatorByPeptideScore());

		final long t2 = System.currentTimeMillis();
		log.info(identifiedProteins.size() + " proteins sorted by best peptide score in " + (double) (t2 - t1) / 1000
				+ " seg");

	}

	public static void sortProteinGroupsByBestPeptideScore(List<ProteinGroup> proteinGroups) {

		log.info("Sorting " + proteinGroups.size() + " protein groups by best peptide score");
		final long t1 = System.currentTimeMillis();

		ParallelSort.parallel_sort(proteinGroups, ComparatorManager.getComparatorByPeptideScore());

		final long t2 = System.currentTimeMillis();
		log.info(proteinGroups.size() + " protein groups sorted by best peptide score in " + (double) (t2 - t1) / 1000
				+ " seg");

	}

	public static void sortProteinGroupsByPeptideScore(List<ProteinGroup> proteinGroups, String scoreName) {

		log.info("Sorting " + proteinGroups.size() + " protein groups by peptide score: " + scoreName);
		final long t1 = System.currentTimeMillis();

		ParallelSort.parallel_sort(proteinGroups, ComparatorManager.getComparatorByPeptideScore(scoreName));

		final long t2 = System.currentTimeMillis();
		log.info(proteinGroups.size() + " protein groups sorted by best peptide score in " + (double) (t2 - t1) / 1000
				+ " seg");

	}

	public static void sortProteinGroupsByProteinScore(List<ProteinGroup> proteinGroups) {

		log.info("Sorting " + proteinGroups.size() + " protein groups by best protein score");
		final long t1 = System.currentTimeMillis();

		ParallelSort.parallel_sort(proteinGroups, ComparatorManager.getComparatorByProteinScore());

		final long t2 = System.currentTimeMillis();
		log.info(proteinGroups.size() + " protein groups sorted by best protein score in " + (double) (t2 - t1) / 1000
				+ " seg");

	}

	public static void sortIdentifiedProteinsByProteinScore(List<IdentifiedProtein> identifiedProteins) {

		log.debug("Sorting " + identifiedProteins.size() + " proteins by protein score");
		final long t1 = System.currentTimeMillis();

		ParallelSort.parallel_sort(identifiedProteins, ComparatorManager.getComparatorByProteinScore());

		final long t2 = System.currentTimeMillis();
		log.debug(identifiedProteins.size() + " proteins sorted by protein score in " + (double) (t2 - t1) / 1000
				+ " seg");

	}

	public static void sortIdentifiedProteinsByAccession(ProteinGroup proteinGroup) {
		log.debug("Sorting " + proteinGroup.size() + " proteins by alfabethic order (no case sensitive)");
		final long t1 = System.currentTimeMillis();
		Collections.sort(proteinGroup, ComparatorManager.getProteinAccComparator());
		final long t2 = System.currentTimeMillis();
		log.debug("protein groups sorted in " + (double) (t2 - t1) / 1000 + " seg");

	}

	public static void sortProteinGroupsByAccession(List<ProteinGroup> proteinGroups) {
		log.debug("Sorting " + proteinGroups.size() + " protein groups by alfabethic order (no case sensitive)");
		final long t1 = System.currentTimeMillis();
		Collections.sort(proteinGroups, ComparatorManager.getProteinAccComparator());
		final long t2 = System.currentTimeMillis();
		log.debug("protein groups sorted in " + (double) (t2 - t1) / 1000 + " seg");

	}

	public static void sortIdentifiedPeptidesByBestPeptideScore(List<IdentifiedPeptide> identifiedPeptides) {

		log.debug("Sorting " + identifiedPeptides.size() + " peptides by best peptide score");
		final long t1 = System.currentTimeMillis();

		ParallelSort.parallel_sort(identifiedPeptides, ComparatorManager.getComparatorByPeptideScore());

		final long t2 = System.currentTimeMillis();
		log.debug(identifiedPeptides.size() + " peptides sorted by peptide score in " + (double) (t2 - t1) / 1000
				+ " seg");

	}

	public static void sortIdentifiedPeptidesByPeptideScore(List<IdentifiedPeptide> identifiedPeptides,
			String scoreName) {

		log.debug("Sorting " + identifiedPeptides.size() + " peptides by peptide score: " + scoreName);
		final long t1 = System.currentTimeMillis();

		ParallelSort.parallel_sort(identifiedPeptides, ComparatorManager.getComparatorByPeptideScore(scoreName));

		final long t2 = System.currentTimeMillis();
		log.debug(identifiedPeptides.size() + " peptides sorted by peptide score in " + (double) (t2 - t1) / 1000
				+ " seg");

	}

	public static void sortProteinGroupOcurrencesByProteinAccessionString(
			List<ProteinGroupOccurrence> proteinOccurrenceList) {
		log.debug("Sorting " + proteinOccurrenceList.size() + " protein group occurrences by protein accession string");
		final long t1 = System.currentTimeMillis();

		// sort
		ParallelSort.parallel_sort(proteinOccurrenceList, ComparatorManager.getOcurrenceStringKeyComparator());

		final long t2 = System.currentTimeMillis();
		log.debug(proteinOccurrenceList.size() + " protein group occurrences sorted by protein accession string in "
				+ (double) (t2 - t1) / 1000 + " seg");

	}

	public static void sortProteinGroupOcurrencesByOccurrence(List<ProteinGroupOccurrence> proteinGroupOccurrenceList) {
		log.debug("Sorting " + proteinGroupOccurrenceList.size() + " protein group occurrences by occurrence; ");
		final long t1 = System.currentTimeMillis();

		// sort
		ParallelSort.parallel_sort(proteinGroupOccurrenceList, ComparatorManager.getDescendantOcurrenceComparator());

		final long t2 = System.currentTimeMillis();
		log.debug(proteinGroupOccurrenceList.size() + " protein groups sorted by occurrence in "
				+ (double) (t2 - t1) / 1000 + " seg");

	}

	public static void sortProteinGroupOcurrencesByBestPeptideScore(
			List<ProteinGroupOccurrence> proteinGroupOccurrenceList) {

		final long t1 = System.currentTimeMillis();

		log.info("Sorting " + proteinGroupOccurrenceList.size() + " protein group occurrences by best peptide score:");

		ParallelSort.parallel_sort(proteinGroupOccurrenceList, ComparatorManager.getComparatorByPeptideScore());

		final long t2 = System.currentTimeMillis();
		log.info(proteinGroupOccurrenceList.size() + " protein group occurrences sorted by best peptide score in "
				+ (double) (t2 - t1) / 1000 + " seg");

	}

	public static void sortProteinGroupOcurrencesByPeptideScore(List<ProteinGroupOccurrence> proteinGroupOccurrenceList,
			String scoreName) {

		final long t1 = System.currentTimeMillis();

		log.info("Sorting " + proteinGroupOccurrenceList.size() + " protein group occurrences by peptide score: "
				+ scoreName);

		ParallelSort.parallel_sort(proteinGroupOccurrenceList,
				ComparatorManager.getComparatorByPeptideScore(scoreName));

		final long t2 = System.currentTimeMillis();
		log.info(proteinGroupOccurrenceList.size() + " protein group occurrences sorted by best peptide score in "
				+ (double) (t2 - t1) / 1000 + " seg");

	}

	public static void sortPeptidesByBestPeptideScore(List<ExtendedIdentifiedPeptide> identifiedPeptides,
			boolean parallel) {
		if (identifiedPeptides == null || identifiedPeptides.isEmpty())
			return;

		log.debug("Sorting " + identifiedPeptides.size() + " peptides by peptide score");
		final long t1 = System.currentTimeMillis();
		try {
			if (parallel)
				ParallelSort.parallel_sort(identifiedPeptides, ComparatorManager.getComparatorByPeptideScore());
			else
				Collections.sort(identifiedPeptides, ComparatorManager.getComparatorByPeptideScore());
		} catch (final IllegalArgumentException e) {
			throw new IllegalMiapeArgumentException(e);
		}
		final long t2 = System.currentTimeMillis();
		log.debug(identifiedPeptides.size() + " peptides sorted by peptide score in " + (double) (t2 - t1) / 1000
				+ " seg");

	}

	public static void sortPeptidesByPeptideScore(List<ExtendedIdentifiedPeptide> identifiedPeptides, String scoreName,
			boolean parallel) {
		if (identifiedPeptides == null || identifiedPeptides.isEmpty())
			return;

		log.debug("Sorting " + identifiedPeptides.size() + " peptides by peptide score: " + scoreName);
		final long t1 = System.currentTimeMillis();

		if (parallel)
			ParallelSort.parallel_sort(identifiedPeptides, ComparatorManager.getComparatorByPeptideScore(scoreName));
		else
			Collections.sort(identifiedPeptides, ComparatorManager.getComparatorByPeptideScore(scoreName));

		final long t2 = System.currentTimeMillis();
		log.debug(identifiedPeptides.size() + " peptides sorted by peptide score in " + (double) (t2 - t1) / 1000
				+ " seg");

	}

	public static void sortPeptideOcurrencesByOccurrence(List<PeptideOccurrence> peptideOccurrenceList) {
		log.info("Sorting " + peptideOccurrenceList.size() + " peptide occurrences by ocurrence");
		final long t1 = System.currentTimeMillis();

		// sort
		ParallelSort.parallel_sort(peptideOccurrenceList, ComparatorManager.getDescendantOcurrenceComparator());

		final long t2 = System.currentTimeMillis();
		log.info(peptideOccurrenceList.size() + " peptide occurrences sorted by occurrence in "
				+ (double) (t2 - t1) / 1000 + " seg");

	}

	public static void sortPeptideOcurrencesByBestPeptideScore(List<PeptideOccurrence> peptideOccurrenceList) {

		final long t1 = System.currentTimeMillis();

		log.debug("Sorting " + peptideOccurrenceList.size() + " peptide occurrences by best peptide score");

		ParallelSort.parallel_sort(peptideOccurrenceList, ComparatorManager.getComparatorByPeptideScore());

		final long t2 = System.currentTimeMillis();
		log.info(peptideOccurrenceList.size() + " peptide occurrences sorted by best peptide score in "
				+ (double) (t2 - t1) / 1000 + " seg");
	}

	public static void sortPeptideOcurrencesByPeptideScore(List<PeptideOccurrence> peptideOccurrenceList,
			String scoreName) {

		final long t1 = System.currentTimeMillis();

		log.debug("Sorting " + peptideOccurrenceList.size() + " peptide occurrences by peptide score: " + scoreName);

		ParallelSort.parallel_sort(peptideOccurrenceList, ComparatorManager.getComparatorByPeptideScore(scoreName));

		final long t2 = System.currentTimeMillis();
		log.info(peptideOccurrenceList.size() + " peptide occurrences sorted by best peptide score in "
				+ (double) (t2 - t1) / 1000 + " seg");
	}

	public static void sortPeptideOcurrencesBySequence(List<PeptideOccurrence> peptideOccurrenceList) {
		log.debug("Sorting " + peptideOccurrenceList.size() + " peptides by sequence");
		final long t1 = System.currentTimeMillis();

		// sort
		ParallelSort.parallel_sort(peptideOccurrenceList, ComparatorManager.getPeptideSequenceComparator());

		final long t2 = System.currentTimeMillis();
		log.debug("peptides sorted in " + (double) (t2 - t1) / 1000 + " seg");

	}

	/**
	 * <html>Sort a list of filters, according to:<br>
	 * <ul>
	 * <li>FDR filters first</li>
	 * <li>Score filters second</li>
	 * <li>Modification filters third</li>
	 * </ul>
	 * </html>
	 * 
	 * @param filters
	 */
	public static void sortFilters(List<Filter> filters) {
		log.debug("Sorting " + filters.size() + " filters");
		final long t1 = System.currentTimeMillis();

		// sort
		Collections.sort(filters, ComparatorManager.getFiltersComparator());

		final long t2 = System.currentTimeMillis();
		log.debug("filters sorted in " + (double) (t2 - t1) / 1000 + " seg");

	}

	public static List<String> getSortedKetList(TObjectIntHashMap<String> numKnownGenesHashMap) {
		final List<String> ret = new ArrayList<String>();
		final Set<String> keySet = numKnownGenesHashMap.keySet();
		for (final String key : keySet) {
			ret.add(key);
		}
		Collections.sort(ret);

		return ret;
	}

	public static void sortStringNoCaseSensitive(List<String> ret) {
		log.debug("Sorting " + ret.size() + " strings by alfabethic order (no case sensitive)");
		final long t1 = System.currentTimeMillis();
		Collections.sort(ret, ComparatorManager.getStringNoCaseSensitiveComparator());
		final long t2 = System.currentTimeMillis();
		log.debug("strings sorted in " + (double) (t2 - t1) / 1000 + " seg");
	}

	public static List<Object> getListFromArray(Object[] chunk) {
		final List<Object> ret = new ArrayList<Object>();
		for (final Object object : chunk) {
			ret.add(object);
		}
		return ret;
	}

	public static void sortProteinsByAccession(List<ExtendedIdentifiedProtein> proteins) {
		log.debug("Sorting " + proteins.size() + " proteins by alfabethic order (no case sensitive)");
		final long t1 = System.currentTimeMillis();
		Collections.sort(proteins, ComparatorManager.getProteinAccComparator());
		final long t2 = System.currentTimeMillis();
		log.debug("proteins sorted in " + (double) (t2 - t1) / 1000 + " seg");

	}

	public static void sortStringByLength(List<String> strings, boolean fromSortest) {
		log.debug("Sorting " + strings.size() + " strings by length. From sortest: " + fromSortest);
		final long t1 = System.currentTimeMillis();
		Collections.sort(strings, ComparatorManager.getStringLengthComparator(fromSortest));
		final long t2 = System.currentTimeMillis();
		log.debug("strings sorted in " + (double) (t2 - t1) / 1000 + " seg");
	}

}
