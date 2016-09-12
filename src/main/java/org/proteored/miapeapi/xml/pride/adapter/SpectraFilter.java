package org.proteored.miapeapi.xml.pride.adapter;

import java.util.Set;

import org.apache.log4j.Logger;
import org.proteored.miapeapi.xml.msi.adapter.InnerIteratorSync2;
import org.proteored.miapeapi.xml.util.parallel.InnerLock;

public class SpectraFilter<T> implements Runnable {
	private static Logger log = Logger.getLogger("log4j.logger.org.proteored");
	InnerIteratorSync2<T> iter = null;

	private InnerLock lock = null;
	private final int count = 0;
	private int iNumber = -1;
	private final Set<Integer> spectrumRefs;

	public SpectraFilter(InnerIteratorSync2<T> aIterator, InnerLock aLock, int aNumber,
			Set<Integer> spectrumRefs) {
		iter = aIterator;
		lock = aLock;
		iNumber = aNumber;
		this.spectrumRefs = spectrumRefs;
	}

	@Override
	public void run() {
		T s = null;
		while ((s = iter.next()) != null) {
			// if (s instanceof Spectrum) {
			// Spectrum spectrum = (Spectrum) s;
			// log.info("Processing spectrum: " + spectrum.getId() +
			// " from processor '"
			// + this.iNumber + "'");
			//
			// iter.remove(!spectrumRefs.contains(spectrum.getId()));
			//
			// }
		}
		lock.updateDoneCount();

	}

	public int getCount() {
		return count;
	}
}
