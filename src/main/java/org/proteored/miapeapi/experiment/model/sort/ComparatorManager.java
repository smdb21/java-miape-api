package org.proteored.miapeapi.experiment.model.sort;

import java.util.Comparator;

import org.apache.log4j.Logger;
import org.proteored.miapeapi.experiment.model.ExtendedIdentifiedPeptide;
import org.proteored.miapeapi.experiment.model.ExtendedIdentifiedProtein;
import org.proteored.miapeapi.experiment.model.PeptideOccurrence;
import org.proteored.miapeapi.experiment.model.ProteinGroup;
import org.proteored.miapeapi.experiment.model.ProteinGroupOccurrence;
import org.proteored.miapeapi.experiment.model.ProteinOccurrence;
import org.proteored.miapeapi.experiment.model.filters.FDRFilter;
import org.proteored.miapeapi.experiment.model.filters.ModificationFilter;
import org.proteored.miapeapi.experiment.model.filters.OccurrenceFilter;
import org.proteored.miapeapi.experiment.model.filters.PeptideLengthFilter;
import org.proteored.miapeapi.experiment.model.filters.PeptideNumberFilter;
import org.proteored.miapeapi.experiment.model.filters.PeptideSequenceFilter;
import org.proteored.miapeapi.experiment.model.filters.PeptidesForMRMFilter;
import org.proteored.miapeapi.experiment.model.filters.ProteinACCFilter;
import org.proteored.miapeapi.experiment.model.filters.ScoreFilter;

public class ComparatorManager {
	private static final Logger log = Logger.getLogger("log4j.logger.org.proteored");

	static Comparator getStringNoCaseSensitiveComparator() {
		return new Comparator() {

			@Override
			public int compare(Object o1, Object o2) {
				String p1 = null;
				String p2 = null;
				if (o1 instanceof String && o2 instanceof String) {
					p1 = (String) o1;
					p2 = (String) o2;
					p1 = p1.toLowerCase();
					p2 = p2.toLowerCase();
				}

				int compareTo = p1.compareTo(p2);
				return compareTo;
			}
		};
	}

	static Comparator getStringLengthComparator(final boolean fromSortest) {
		return new Comparator() {

			@Override
			public int compare(Object o1, Object o2) {
				int p1 = 0;
				int p2 = 0;
				if (o1 instanceof String && o2 instanceof String) {
					p1 = ((String) o1).length();
					p2 = ((String) o2).length();
					if (fromSortest) {
						if (p1 > p2)
							return 1;
						else if (p1 == p2)
							return -1;
						else
							return 0;

					} else {
						if (p1 < p2)
							return 1;
						else if (p1 == p2)
							return -1;
						else
							return 0;
					}

				}

				return 0;
			}
		};
	}

	static Comparator getComparatorByPeptideScore() {

		return new Comparator() {

			@Override
			public int compare(Object o1, Object o2) {
				try {
					Order order = Order.ASCENDANT;
					Float score1 = null;
					Float score2 = null;
					if (o1 instanceof ExtendedIdentifiedProtein && o2 instanceof ExtendedIdentifiedProtein) {
						ExtendedIdentifiedProtein p1 = (ExtendedIdentifiedProtein) o1;
						ExtendedIdentifiedProtein p2 = (ExtendedIdentifiedProtein) o2;
						score1 = p1.getBestPeptideScore();
						score2 = p2.getBestPeptideScore();
						SortingParameters proteinSortingByPeptideScore = SortingManager.getInstance()
								.getProteinSortingByPeptideScore(p1);
						if (proteinSortingByPeptideScore != null)
							order = proteinSortingByPeptideScore.getOrder();

					} else if (o1 instanceof ExtendedIdentifiedPeptide && o2 instanceof ExtendedIdentifiedPeptide) {
						ExtendedIdentifiedPeptide p1 = (ExtendedIdentifiedPeptide) o1;
						ExtendedIdentifiedPeptide p2 = (ExtendedIdentifiedPeptide) o2;
						score1 = p1.getScore();
						score2 = p2.getScore();
						SortingParameters peptideSortingByPeptideScore = SortingManager.getInstance()
								.getPeptideSortingByPeptideScore(p1);
						if (peptideSortingByPeptideScore != null)
							order = peptideSortingByPeptideScore.getOrder();

					} else if (o1 instanceof PeptideOccurrence && o2 instanceof PeptideOccurrence) {

						PeptideOccurrence peptideOccurrence1 = (PeptideOccurrence) o1;
						PeptideOccurrence peptideOccurrence2 = (PeptideOccurrence) o2;

						score1 = peptideOccurrence1.getBestPeptideScore();
						score2 = peptideOccurrence2.getBestPeptideScore();
						SortingParameters peptideOccurrenceSortingByPeptideScore = SortingManager.getInstance()
								.getPeptideOccurrenceSortingByPeptideScore(peptideOccurrence1);
						if (peptideOccurrenceSortingByPeptideScore != null)
							order = peptideOccurrenceSortingByPeptideScore.getOrder();

					} else if (o1 instanceof ProteinOccurrence && o2 instanceof ProteinOccurrence) {

						ProteinOccurrence proteinOccurrence1 = (ProteinOccurrence) o1;
						ProteinOccurrence proteinOccurrence2 = (ProteinOccurrence) o2;
						score1 = proteinOccurrence1.getBestPeptideScore();
						score2 = proteinOccurrence2.getBestPeptideScore();
						SortingParameters proteinOccurrenceSortingByPeptideScore = SortingManager.getInstance()
								.getProteinOccurrenceSortingByPeptideScore(proteinOccurrence1);
						if (proteinOccurrenceSortingByPeptideScore != null)
							order = proteinOccurrenceSortingByPeptideScore.getOrder();

					} else if (o1 instanceof ProteinGroup && o2 instanceof ProteinGroup) {

						ProteinGroup proteinGroup1 = (ProteinGroup) o1;
						ProteinGroup proteinGroup2 = (ProteinGroup) o2;
						score1 = proteinGroup1.getBestPeptideScore();
						score2 = proteinGroup2.getBestPeptideScore();
						SortingParameters proteinGroupSortingByPeptideScore = SortingManager.getInstance()
								.getProteinGroupSortingByPeptideScore(proteinGroup1);
						if (proteinGroupSortingByPeptideScore != null)
							order = proteinGroupSortingByPeptideScore.getOrder();

					} else if (o1 instanceof ProteinGroupOccurrence && o2 instanceof ProteinGroupOccurrence) {

						ProteinGroupOccurrence proteinGroupOccurrence1 = (ProteinGroupOccurrence) o1;
						ProteinGroupOccurrence proteinGroupOccurrence2 = (ProteinGroupOccurrence) o2;
						score1 = proteinGroupOccurrence1.getBestPeptideScore();
						score2 = proteinGroupOccurrence2.getBestPeptideScore();
						SortingParameters proteinGroupOccurrenceSortingByPeptideScore = SortingManager.getInstance()
								.getProteinGroupOccurrenceSortingByPeptideScore(proteinGroupOccurrence1);
						if (proteinGroupOccurrenceSortingByPeptideScore != null)
							order = proteinGroupOccurrenceSortingByPeptideScore.getOrder();

					}

					if (score1 == null && order == Order.DESCENDANT)
						score1 = Float.MIN_VALUE;
					if (score1 == null && order == Order.ASCENDANT)
						score1 = Float.MAX_VALUE;
					if (score2 == null && order == Order.DESCENDANT)
						score2 = Float.MIN_VALUE;
					if (score2 == null && order == Order.ASCENDANT)
						score2 = Float.MAX_VALUE;

					if (order == Order.ASCENDANT)
						return score1.compareTo(score2);
					else
						return score2.compareTo(score1);

				} catch (Exception e) {
					e.printStackTrace();
					log.warn(e.getMessage());
					return 0;
				}
			}
		};
	}

	static Comparator getComparatorByPeptideScore(final String scoreName) {

		return new Comparator() {
			SortingParameters sortingParameters = null;

			@Override
			public int compare(Object o1, Object o2) {
				Order order = Order.ASCENDANT;
				Float score1 = null;
				Float score2 = null;
				if (sortingParameters == null)
					sortingParameters = SortingManager.getInstance().getSortingParameters(scoreName);
				if (o1 instanceof ExtendedIdentifiedProtein && o2 instanceof ExtendedIdentifiedProtein) {
					ExtendedIdentifiedProtein p1 = (ExtendedIdentifiedProtein) o1;
					ExtendedIdentifiedProtein p2 = (ExtendedIdentifiedProtein) o2;
					score1 = p1.getBestPeptideScore(scoreName);
					score2 = p2.getBestPeptideScore(scoreName);
					if (sortingParameters != null)
						order = sortingParameters.getOrder();

				} else if (o1 instanceof ExtendedIdentifiedPeptide && o2 instanceof ExtendedIdentifiedPeptide) {
					ExtendedIdentifiedPeptide p1 = (ExtendedIdentifiedPeptide) o1;
					ExtendedIdentifiedPeptide p2 = (ExtendedIdentifiedPeptide) o2;
					score1 = p1.getScore(scoreName);
					score2 = p2.getScore(scoreName);
					if (sortingParameters != null)
						order = sortingParameters.getOrder();

				} else if (o1 instanceof PeptideOccurrence && o2 instanceof PeptideOccurrence) {

					PeptideOccurrence peptideOccurrence1 = (PeptideOccurrence) o1;
					PeptideOccurrence peptideOccurrence2 = (PeptideOccurrence) o2;

					score1 = peptideOccurrence1.getBestPeptideScore(scoreName);
					score2 = peptideOccurrence2.getBestPeptideScore(scoreName);
					if (sortingParameters != null)
						order = sortingParameters.getOrder();

				} else if (o1 instanceof ProteinOccurrence && o2 instanceof ProteinOccurrence) {

					ProteinOccurrence proteinOccurrence1 = (ProteinOccurrence) o1;
					ProteinOccurrence proteinOccurrence2 = (ProteinOccurrence) o2;
					score1 = proteinOccurrence1.getBestPeptideScore(scoreName);
					score2 = proteinOccurrence2.getBestPeptideScore(scoreName);
					if (sortingParameters != null)
						order = sortingParameters.getOrder();

				} else if (o1 instanceof ProteinGroup && o2 instanceof ProteinGroup) {

					ProteinGroup proteinGroup1 = (ProteinGroup) o1;
					ProteinGroup proteinGroup2 = (ProteinGroup) o2;
					score1 = proteinGroup1.getBestPeptideScore(scoreName);
					score2 = proteinGroup2.getBestPeptideScore(scoreName);
					if (sortingParameters != null)
						order = sortingParameters.getOrder();

				} else if (o1 instanceof ProteinGroupOccurrence && o2 instanceof ProteinGroupOccurrence) {

					ProteinGroupOccurrence proteinGroupOccurrence1 = (ProteinGroupOccurrence) o1;
					ProteinGroupOccurrence proteinGroupOccurrence2 = (ProteinGroupOccurrence) o2;
					score1 = proteinGroupOccurrence1.getBestPeptideScore(scoreName);
					score2 = proteinGroupOccurrence2.getBestPeptideScore(scoreName);
					if (sortingParameters != null)
						order = sortingParameters.getOrder();

				}

				if (score1 == null && order == Order.DESCENDANT)
					score1 = Float.MIN_VALUE;
				if (score1 == null && order == Order.ASCENDANT)
					score1 = Float.MAX_VALUE;
				if (score2 == null && order == Order.DESCENDANT)
					score2 = Float.MIN_VALUE;
				if (score2 == null && order == Order.ASCENDANT)
					score2 = Float.MAX_VALUE;
				if (score1 == score2)
					System.out.println("asdf");
				if (order == Order.ASCENDANT)
					return score1.compareTo(score2);
				else
					return score2.compareTo(score1);

			}
		};
	}

	static Comparator getComparatorByProteinScore() {

		return new Comparator() {

			@Override
			public int compare(Object o1, Object o2) {
				Order order = Order.ASCENDANT;
				Float score1 = null;
				Float score2 = null;
				if (o1 instanceof ExtendedIdentifiedProtein && o2 instanceof ExtendedIdentifiedProtein) {
					ExtendedIdentifiedProtein p1 = (ExtendedIdentifiedProtein) o1;
					ExtendedIdentifiedProtein p2 = (ExtendedIdentifiedProtein) o2;
					score1 = p1.getScore();
					score2 = p2.getScore();
					SortingParameters proteinSortingByProteinScore = SortingManager.getInstance()
							.getProteinSortingByProteinScore(p1);
					if (proteinSortingByProteinScore != null)
						order = proteinSortingByProteinScore.getOrder();
				} else if (o1 instanceof ProteinOccurrence && o2 instanceof ProteinOccurrence) {

					ProteinOccurrence proteinOccurrence1 = (ProteinOccurrence) o1;
					ProteinOccurrence proteinOccurrence2 = (ProteinOccurrence) o2;
					score1 = proteinOccurrence1.getBestProteinScore();
					score2 = proteinOccurrence2.getBestProteinScore();
					SortingParameters proteinOccurrenceSortingByProteinScore = SortingManager.getInstance()
							.getProteinOccurrenceSortingByProteinScore(proteinOccurrence1);
					if (proteinOccurrenceSortingByProteinScore != null)
						order = proteinOccurrenceSortingByProteinScore.getOrder();
				} else if (o1 instanceof ProteinGroup && o2 instanceof ProteinGroup) {

					ProteinGroup proteinGroup1 = (ProteinGroup) o1;
					ProteinGroup proteinGroup2 = (ProteinGroup) o2;
					score1 = proteinGroup1.getBestProteinScore();
					score2 = proteinGroup2.getBestProteinScore();
					SortingParameters proteinGroupSortingByProteinScore = SortingManager.getInstance()
							.getProteinGroupSortingByProteinScore(proteinGroup1);
					if (proteinGroupSortingByProteinScore != null)
						order = proteinGroupSortingByProteinScore.getOrder();
				} else if (o1 instanceof ProteinGroupOccurrence && o2 instanceof ProteinGroupOccurrence) {

					ProteinGroupOccurrence proteinGroupOccurrence1 = (ProteinGroupOccurrence) o1;
					ProteinGroupOccurrence proteinGroupOccurrence2 = (ProteinGroupOccurrence) o2;
					score1 = proteinGroupOccurrence1.getBestProteinScore();
					score2 = proteinGroupOccurrence2.getBestProteinScore();
					SortingParameters proteinGroupOccurrenceSortingByProteinScore = SortingManager.getInstance()
							.getProteinGroupOccurrenceSortingByProteinScore(proteinGroupOccurrence1);
					if (proteinGroupOccurrenceSortingByProteinScore != null)
						order = proteinGroupOccurrenceSortingByProteinScore.getOrder();
				}
				if (score1 == null && order == Order.DESCENDANT)
					score1 = Float.MIN_VALUE;
				if (score1 == null && order == Order.ASCENDANT)
					score1 = Float.MAX_VALUE;
				if (score2 == null && order == Order.DESCENDANT)
					score2 = Float.MIN_VALUE;
				if (score2 == null && order == Order.ASCENDANT)
					score2 = Float.MAX_VALUE;
				if (order == Order.ASCENDANT)
					return score1.compareTo(score2);
				else
					return score2.compareTo(score1);

			}
		};
	}

	static Comparator getComparatorByProteinScore(final String scoreName) {

		return new Comparator() {
			SortingParameters sortingParameters = null;

			@Override
			public int compare(Object o1, Object o2) {
				Order order = Order.ASCENDANT;
				Float score1 = null;
				Float score2 = null;
				if (sortingParameters == null)
					sortingParameters = SortingManager.getInstance().getSortingParameters(scoreName);
				if (o1 instanceof ExtendedIdentifiedProtein && o2 instanceof ExtendedIdentifiedProtein) {
					ExtendedIdentifiedProtein p1 = (ExtendedIdentifiedProtein) o1;
					ExtendedIdentifiedProtein p2 = (ExtendedIdentifiedProtein) o2;
					score1 = p1.getScore(scoreName);
					score2 = p2.getScore(scoreName);
					if (sortingParameters != null)
						order = sortingParameters.getOrder();
				} else if (o1 instanceof ProteinOccurrence && o2 instanceof ProteinOccurrence) {

					ProteinOccurrence proteinOccurrence1 = (ProteinOccurrence) o1;
					ProteinOccurrence proteinOccurrence2 = (ProteinOccurrence) o2;
					score1 = proteinOccurrence1.getBestProteinScore(scoreName);
					score2 = proteinOccurrence2.getBestProteinScore(scoreName);
					if (sortingParameters != null)
						order = sortingParameters.getOrder();
				} else if (o1 instanceof ProteinGroup && o2 instanceof ProteinGroup) {

					ProteinGroup proteinGroup1 = (ProteinGroup) o1;
					ProteinGroup proteinGroup2 = (ProteinGroup) o2;
					score1 = proteinGroup1.getBestProteinScore(scoreName);
					score2 = proteinGroup2.getBestProteinScore(scoreName);
					if (sortingParameters != null)
						order = sortingParameters.getOrder();
				} else if (o1 instanceof ProteinGroupOccurrence && o2 instanceof ProteinGroupOccurrence) {

					ProteinGroupOccurrence proteinGroupOccurrence1 = (ProteinGroupOccurrence) o1;
					ProteinGroupOccurrence proteinGroupOccurrence2 = (ProteinGroupOccurrence) o2;
					score1 = proteinGroupOccurrence1.getBestProteinScore(scoreName);
					score2 = proteinGroupOccurrence2.getBestProteinScore(scoreName);
					if (sortingParameters != null)
						order = sortingParameters.getOrder();
				}
				if (score1 == null && order == Order.DESCENDANT)
					score1 = Float.MIN_VALUE;
				if (score1 == null && order == Order.ASCENDANT)
					score1 = Float.MAX_VALUE;
				if (score2 == null && order == Order.DESCENDANT)
					score2 = Float.MIN_VALUE;
				if (score2 == null && order == Order.ASCENDANT)
					score2 = Float.MAX_VALUE;
				if (order == Order.ASCENDANT)
					return score1.compareTo(score2);
				else
					return score2.compareTo(score1);

			}
		};
	}

	static Comparator getPeptideSequenceComparator() {
		return new Comparator() {

			@Override
			public int compare(Object o1, Object o2) {
				if (o1 instanceof PeptideOccurrence && o2 instanceof PeptideOccurrence) {
					PeptideOccurrence p1 = (PeptideOccurrence) o1;
					PeptideOccurrence p2 = (PeptideOccurrence) o2;

					String seq1 = p1.getFirstOccurrence().getSequence();
					String seq2 = p2.getFirstOccurrence().getSequence();
					if (seq1.equals(seq2)) {
						return p1.getKey().compareTo(p2.getKey());
					}
					return seq1.compareTo(seq2);
				} else if (o1 instanceof ExtendedIdentifiedPeptide && o2 instanceof ExtendedIdentifiedPeptide) {
					ExtendedIdentifiedPeptide p1 = (ExtendedIdentifiedPeptide) o1;
					ExtendedIdentifiedPeptide p2 = (ExtendedIdentifiedPeptide) o2;
					String seq1 = p1.getSequence();
					String seq2 = p2.getSequence();
					if (seq1.equals(seq2)) {
						try {
							return Integer.compare(Integer.valueOf(p1.getCharge()), Integer.valueOf(p2.getCharge()));
						} catch (NumberFormatException e) {

						}
					}
					return seq1.compareTo(seq2);
				}
				return 0;
			}

		};
	}

	static Comparator getDescendantOcurrenceComparator() {
		return new Comparator() {

			@Override
			public int compare(Object o1, Object o2) {
				if (o1 instanceof PeptideOccurrence && o2 instanceof PeptideOccurrence) {
					PeptideOccurrence p1 = (PeptideOccurrence) o1;
					PeptideOccurrence p2 = (PeptideOccurrence) o2;
					Integer ocurrence1 = p1.getItemList().size();
					Integer ocurrence2 = p2.getItemList().size();

					return ocurrence2.compareTo(ocurrence1);
				} else if (o1 instanceof ProteinOccurrence && o2 instanceof ProteinOccurrence) {
					ProteinOccurrence p1 = (ProteinOccurrence) o1;
					ProteinOccurrence p2 = (ProteinOccurrence) o2;
					Integer ocurrence1 = p1.getItemList().size();
					Integer ocurrence2 = p2.getItemList().size();

					return ocurrence2.compareTo(ocurrence1);
				} else if (o1 instanceof ProteinGroupOccurrence && o2 instanceof ProteinGroupOccurrence) {
					ProteinGroupOccurrence p1 = (ProteinGroupOccurrence) o1;
					ProteinGroupOccurrence p2 = (ProteinGroupOccurrence) o2;
					Integer ocurrence1 = p1.getItemList().size();
					Integer ocurrence2 = p2.getItemList().size();

					return ocurrence2.compareTo(ocurrence1);
				}
				return 0;
			}
		};
	}

	static Comparator getOcurrenceStringKeyComparator() {
		return new Comparator() {

			@Override
			public int compare(Object o1, Object o2) {
				if (o1 instanceof PeptideOccurrence && o2 instanceof PeptideOccurrence) {
					PeptideOccurrence p1 = (PeptideOccurrence) o1;
					PeptideOccurrence p2 = (PeptideOccurrence) o2;
					String ocurrenceKey1 = p1.getKey();
					String ocurrenceKey2 = p2.getKey();

					return ocurrenceKey1.compareTo(ocurrenceKey2);
				} else if (o1 instanceof ProteinGroupOccurrence && o2 instanceof ProteinGroupOccurrence) {
					ProteinGroupOccurrence p1 = (ProteinGroupOccurrence) o1;
					ProteinGroupOccurrence p2 = (ProteinGroupOccurrence) o2;
					String ocurrenceKey1 = p1.getAccessionsString();
					String ocurrenceKey2 = p2.getAccessionsString();

					return ocurrenceKey1.compareTo(ocurrenceKey2);
				}
				return 0;
			}
		};
	}

	static Comparator getProteinAccComparator() {
		return new Comparator() {

			@Override
			public int compare(Object o1, Object o2) {

				if (o1 instanceof ExtendedIdentifiedProtein && o2 instanceof ExtendedIdentifiedProtein) {
					ExtendedIdentifiedProtein p1 = null;
					ExtendedIdentifiedProtein p2 = null;
					p1 = (ExtendedIdentifiedProtein) o1;
					p2 = (ExtendedIdentifiedProtein) o2;
					String acc1 = p1.getAccession();
					String acc2 = p2.getAccession();
					return acc1.compareTo(acc2);
				} else if (o1 instanceof ProteinGroup && o2 instanceof ProteinGroup) {
					ProteinGroup p1 = (ProteinGroup) o1;
					ProteinGroup p2 = (ProteinGroup) o2;
					String acc1 = p1.toString();
					String acc2 = p2.toString();
					return acc1.compareTo(acc2);
				}

				return 0;
			}
		};
	}

	/**
	 * Gets a comparator that sorts the Filters in the following order (from
	 * first to last): {@link PeptideLengthFilter} < {@link FDRFilter} <
	 * {@link ScoreFilter} < {@link OccurrenceFilter} <
	 * {@link ModificationFilter} < {@link ProteinACCFilter}
	 *
	 * @return
	 */
	static Comparator getFiltersComparator() {
		return new Comparator() {

			@Override
			public int compare(Object o1, Object o2) {

				int filter1 = getFilterNumber(o1);
				int filter2 = getFilterNumber(o2);

				if (filter1 < filter2)
					return -1;
				if (filter1 > filter2)
					return 1;
				return 0;
			}

			private int getFilterNumber(Object o1) {
				if (o1 instanceof PeptideLengthFilter)
					return -1;
				if (o1 instanceof FDRFilter)
					return 0;
				if (o1 instanceof ScoreFilter)
					return 1;
				if (o1 instanceof OccurrenceFilter)
					return 2;
				if (o1 instanceof ModificationFilter)
					return 3;
				if (o1 instanceof PeptideNumberFilter)
					return 4;
				if (o1 instanceof ProteinACCFilter)
					return 5;
				if (o1 instanceof PeptideSequenceFilter)
					return 6;
				if (o1 instanceof PeptidesForMRMFilter)
					return 7;
				return 10;
			}
		};
	}

	/**
	 * @return
	 */
	public static Comparator<? super ExtendedIdentifiedProtein> getProteinComparatorByEvidence() {
		return new Comparator<ExtendedIdentifiedProtein>() {

			@Override
			public int compare(ExtendedIdentifiedProtein o1, ExtendedIdentifiedProtein o2) {

				int ret = o1.getEvidence().compareTo(o2.getEvidence());
				if (ret != 0) {
					return ret;
				}
				ret = o2.getPeptideNumber().compareTo(o1.getPeptideNumber());
				if (ret != 0) {
					return ret;
				}
				ret = Integer.compare(o2.getPeptides().size(), o1.getPeptides().size());
				if (ret != 0) {
					return ret;
				}
				if (o1.getProteinSequence() != null && o2.getProteinSequence() != null) {
					try {
						int len1 = o1.getProteinSequence().length();
						int len2 = o2.getProteinSequence().length();
						return Integer.compare(len2, len1);
					} catch (Exception e) {

					}
				}
				// if we are here, the peptides and psms are the same, so we
				// return the one with less coverage, which will be the one with
				// longer sequence
				String coverage1 = o1.getCoverage();
				String coverage2 = o2.getCoverage();
				if (coverage1 != null && coverage2 != null) {
					ret = Double.valueOf(coverage1).compareTo(Double.valueOf(coverage2));
					if (ret != 0) {
						return ret;
					}
				}
				return 0;
			}
		};
	}
}
