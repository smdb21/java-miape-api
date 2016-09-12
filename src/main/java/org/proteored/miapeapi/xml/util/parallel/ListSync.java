package org.proteored.miapeapi.xml.util.parallel;

import java.util.List;

/**
 * Simple wrapper class to allow synchronization on the get(), add() and
 * contains() methods from a {@link List}
 */
public class ListSync<T> {
	private List<T> list = null;

	public ListSync(List<T> hashMap) {
		this.list = hashMap;
	}

	public synchronized T get(int index) {
		return list.get(index);
	}

	public synchronized boolean contains(T object) {
		return this.list.contains(object);
	}

	public synchronized boolean add(T object) {
		return this.list.add(object);
	}
}
