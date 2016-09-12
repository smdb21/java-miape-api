package org.proteored.miapeapi.xml.util.parallel;

import java.util.Set;

/**
 * Simple wrapper class to allow synchronization on the add() and contains()
 * methods from a {@link Set}
 */
public class SetSync<T> {
	private Set<T> list = null;

	public SetSync(Set<T> set) {
		this.list = set;
	}

	public synchronized boolean contains(T object) {
		return this.list.contains(object);
	}

	public synchronized boolean add(T object) {
		return this.list.add(object);
	}
}