package org.proteored.miapeapi.xml.util.parallel;

import java.util.Iterator;

/**
 * Simple wrapper class to allow synchronisation on the hasNext() and next()
 * methods of the
 * iterator.
 */
public class InnerIteratorSync<T> {
	private Iterator<T> iter = null;

	public InnerIteratorSync(Iterator<T> aIterator) {
		iter = aIterator;
	}

	public synchronized T next() {
		T result = null;
		if (iter.hasNext()) {
			result = iter.next();
		}
		return result;
	}

	public synchronized void remove() {
		iter.remove();
	}
}
