package org.proteored.miapeapi.experiment.model.sort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;

import edu.scripps.yates.utilities.cores.SystemCoreManager;

public class ParallelSort {
	static int usedCores;
	private static Comparator comparator;
	private static final Logger log = Logger.getLogger("log4j.logger.org.proteored");

	private ParallelSort(){
	
	}

	public static void parallel_sort(List arr, Comparator comparator) {

		int usedCores = SystemCoreManager.getAvailableNumSystemCores();

		// log.info("Using " + usedCores + " out of " +
		// SystemCoreManager.getNumSystemCores()
		// + " cores");
		ParallelSort.comparator = comparator;
		/* split the array in chunks to be processed */
		final ArrayList<ArrayList<Object>> chunks = new ArrayList<ArrayList<Object>>();
		int chunkSize = arr.size() / usedCores;

		for (int tid = 0; tid < usedCores; tid++) {
			/* calc begining and end of piece */
			int begin = tid * chunkSize;
			int end = begin + chunkSize;
			if (tid == usedCores - 1) {
				/* if we are doing the last piece, go to the end of the array */
				end = arr.size();
			}
			ArrayList<Object> chunk = new ArrayList<Object>();
			/* copy the elements into the chunk */
			for (int j = 0, i = begin; i < end; i++) {
				chunk.add(j++, arr.get(i));
			}
			chunks.add(chunk);
		}

		class Sorter implements Runnable {
			private final int tid;

			public Sorter(int id) {
				tid = id;
			}

			@Override
			public void run() {
				/* sort our piece */
				ArrayList<Object> chunk = chunks.get(tid);
				Collections.sort(chunk, ParallelSort.comparator);
			}
		}

		Thread[] helpers = new Thread[usedCores];
		for (int id = 0; id < usedCores; id++) {
			Thread t = new Thread(new Sorter(id));
			t.start();
			helpers[id] = t;
		}

		/* Wait for all helpers to sort their chunks */
		for (Thread t : helpers) {
			try {
				t.join();
			} catch (InterruptedException e) {
				System.err.println(e);
			}
		}

		/* Merge */
		/*
		 * In the case when there is more than 4 chunks we can execute
		 * this step in parellel too but is it worth it since it is only
		 * a linear time operation anyway?
		 */
		ArrayList<Object> result = chunks.get(0);
		// for (Object object : result) {
		// IdentificationOccurrence<ExtendedIdentifiedPeptide> tmp =
		// (IdentificationOccurrence<ExtendedIdentifiedPeptide>) object;
		// Double bestScore = tmp.getBestScore(new
		// SortingParameters("Mascot:score",
		// Order.DESCENDANT));
		// System.out.println(bestScore);
		// }
		for (int i = 1; i < usedCores; i++) {
			result = merge(result, chunks.get(i));
		}

		for (int i = 0; i < arr.size(); i++) {
			arr.set(i, result.get(i));
		}
	}

	private static ArrayList merge(ArrayList arr1, ArrayList arr2) {
		ArrayList result = new ArrayList();

		int j1 = 0; /* index into arr1 */
		int j2 = 0; /* index into arr2 */
		for (int i = 0; i < arr1.size() + arr2.size(); i++) {
			if (j1 < arr1.size()) {
				if (j2 < arr2.size()) {
					if (comparator.compare(arr1.get(j1), arr2.get(j2)) == -1) {
						// arr1[j1].compareTo(arr2[j2]) == -1) {
						result.add(arr1.get(j1++));
					} else {
						result.add(arr2.get(j2++));
					}
				} else {
					/* reached end of second chunk, copy first */
					result.add(arr1.get(j1++));
				}
			} else {
				/* reached end of 1st chunk, copy 2nd */
				result.add(arr2.get(j2++));
			}
		}

		return result;
	}
}
