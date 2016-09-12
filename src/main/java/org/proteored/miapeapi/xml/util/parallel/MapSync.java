package org.proteored.miapeapi.xml.util.parallel;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Simple wrapper class to allow synchronization on the get(), containsKey() and
 * put() methods from a {@link Map}
 */
public class MapSync<T, Y> {
	private Map<T, Y> map = null;

	public MapSync(Map<T, Y> map) {
		this.map = map;
	}

	public synchronized Y get(T key) {
		return map.get(key);
	}

	public synchronized boolean containsKey(T key) {
		return this.map.containsKey(key);
	}

	public synchronized Y put(T key, Y value) {
		return this.map.put(key, value);
	}

	public Collection<Y> values() {
		return this.map.values();
	}

	public Set<T> keySet() {
		return this.map.keySet();
	}
}
