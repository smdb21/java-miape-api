package org.proteored.miapeapi.experiment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;

public abstract class VennData<T> {
	private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger("log4j.logger.org.proteored");
	private static int count = 0;
	private final Map<T, Object> hash1 = new THashMap<T, Object>();
	private final Map<T, Object> hash2 = new THashMap<T, Object>();
	private final Map<T, Object> hash3 = new THashMap<T, Object>();

	private List<T> keys1 = new ArrayList<T>();
	private List<T> keys2 = new ArrayList<T>();
	private List<T> keys3 = new ArrayList<T>();
	private List<T> uniqueTo1Keys;
	private List<T> uniqueTo2Keys;
	private List<T> uniqueTo3Keys;
	private Set<T> union12;
	private Set<T> union13;
	private Set<T> union23;
	private Set<T> union123;
	private Collection<Object> col1;
	private Collection<Object> col2;
	private Collection<Object> col3;
	private boolean collectionProcessed;

	public VennData(Collection<Object> col1, Collection<Object> col2, Collection<Object> col3) {

		log.debug("VeDnn data processing:");
		if (col1 != null) {
			log.debug("Collection 1 contains " + col1.size() + " elements");
		} else {
			log.debug("Collection 1 is empty!");
		}
		if (col2 != null) {
			log.debug("Collection 2 contains " + col2.size() + " elements");
		} else {
			log.debug("Collection 2 is empty!");
		}
		if (col3 != null) {
			log.debug("Collection 3 contains " + col3.size() + " elements");
		} else {
			log.debug("Collection 3 is empty!");
		}
		this.col1 = col1;
		this.col2 = col2;
		this.col3 = col3;
	}

	private final void processCollections() {
		if (!collectionProcessed) {
			log.info("Processing collection 1");
			processCollections(col1, keys1, hash1);
			log.info("Processing collection 2");
			processCollections(col2, keys2, hash2);
			log.info("Processing collection 3");
			processCollections(col3, keys3, hash3);
			log.info("keys1=" + keys1.size() + "keys2=" + keys2.size() + "keys3=" + keys3.size());
		}
		this.collectionProcessed = true;
	}

	private final void processCollections(Collection<Object> referenceCollection, List<T> keys1, Map<T, Object> hash1) {

		int uniques = 0; // number of unique objects in reference collection

		int numDiscarded = 0;
		// 1 VS (2 and 3)
		if (referenceCollection != null)
			for (Object obj1 : referenceCollection) {
				if (isObjectValid(obj1)) {
					T key = getKeyFromObject(obj1);
					hash1.put(key, obj1);
					keys1.add(key);
				}
			}
		if (keys1 != null && referenceCollection != null) {
			log.info("data collection with " + referenceCollection.size() + " now has " + keys1.size() + "/"
					+ hash1.size() + " string keys. " + uniques + " are uniques, after discarding " + numDiscarded);
			log.info("");
		}

	}

	protected abstract T getKeyFromObject(Object obj);

	protected String getUniqueKey() {
		return String.valueOf(count++);
	}

	public Collection<Object> getIntersection123() {
		List<T> intersectionKeys = getIntersection(keys1, keys2, keys3);
		Set<Object> ret = getObjectsByKeys(intersectionKeys);
		return ret;
	}

	public List<T> getIntersection123Keys() {
		return getIntersection(keys1, keys2, keys3);
	}

	public Collection<Object> getIntersection12() {
		List<T> intersectionKeys = getIntersection(keys1, keys2);
		Set<Object> ret = getObjectsByKeys(intersectionKeys);
		return ret;
	}

	public List<T> getIntersection12Keys() {
		return getIntersection(keys1, keys2);
	}

	public Collection<Object> getIntersection23() {
		List<T> intersectionKeys = getIntersection(keys2, keys3);
		Set<Object> ret = getObjectsByKeys(intersectionKeys);
		return ret;
	}

	public List<T> getIntersection23Keys() {
		return getIntersection(keys2, keys3);
	}

	public Collection<Object> getIntersection13() {
		List<T> intersectionKeys = getIntersection(keys1, keys3);
		Set<Object> ret = getObjectsByKeys(intersectionKeys);
		return ret;
	}

	public List<T> getIntersection13Keys() {
		return getIntersection(keys1, keys3);
	}

	public Collection<Object> getUniqueTo1() {
		processCollections();
		List<T> uniqueTo1 = getUniqueToFirstSet(keys1, keys2, keys3);
		return this.getObjectsByKeys(uniqueTo1);
	}

	public List<T> getUniqueTo1Keys() {
		processCollections();
		if (uniqueTo1Keys == null) {
			uniqueTo1Keys = getUniqueToFirstSet(keys1, keys2, keys3);
		}
		return uniqueTo1Keys;
	}

	public Collection<Object> getUniqueTo2() {
		processCollections();
		List<T> uniqueTo2 = getUniqueToFirstSet(keys2, keys1, keys3);
		return this.getObjectsByKeys(uniqueTo2);
	}

	public List<T> getUniqueTo2Keys() {
		processCollections();
		if (uniqueTo2Keys == null) {
			uniqueTo2Keys = getUniqueToFirstSet(keys2, keys1, keys3);
		}
		return uniqueTo2Keys;
	}

	public Collection<Object> getUniqueTo3() {
		processCollections();
		List<T> uniqueTo3 = getUniqueToFirstSet(keys3, keys1, keys2);
		return this.getObjectsByKeys(uniqueTo3);
	}

	public List<T> getUniqueTo3Keys() {
		processCollections();
		if (uniqueTo3Keys == null) {
			uniqueTo3Keys = getUniqueToFirstSet(keys3, keys1, keys2);
		}
		return uniqueTo3Keys;
	}

	private List<T> getUniqueToFirstSet(Collection<T> hashToIsolate, Collection<T> hash2, Collection<T> hash3) {
		processCollections();
		List<T> toIsolateSet2 = new ArrayList<T>();
		log.info("Unique to isolate size=" + hashToIsolate.size());
		if (hashToIsolate != null) {
			toIsolateSet2.addAll(hashToIsolate);
			Iterator<T> toIsolateIterator = toIsolateSet2.iterator();

			while (toIsolateIterator.hasNext()) {
				T item = toIsolateIterator.next();
				if (hash2 != null && hash2.contains(item)) {
					toIsolateIterator.remove();
				} else if (hash3 != null && hash3.contains(item)) {
					toIsolateIterator.remove();
				}
			}
		}
		return toIsolateSet2;
	}

	public Collection<Object> getUnion123() {
		Set<T> unionKeys = getUnion(keys1, keys2, keys3);
		Set<Object> ret = getObjectsByKeys(unionKeys);
		return ret;
	}

	public Collection<T> getUnion123Keys() {
		if (union123 == null) {
			union123 = getUnion(keys1, keys2, keys3);
		}
		return union123;
	}

	public Collection<Object> getUnion12() {
		Set<T> unionKeys = getUnion(keys1, keys2, null);
		Set<Object> ret = getObjectsByKeys(unionKeys);
		return ret;
	}

	public Set<T> getUnion12Keys() {
		if (union12 == null) {
			union12 = getUnion(keys1, keys2, null);
		}
		return union12;

	}

	public Collection<Object> getUnion13() {
		Set<T> unionKeys = getUnion(keys1, null, keys3);
		Set<Object> ret = getObjectsByKeys(unionKeys);
		return ret;
	}

	public Set<T> getUnion13Keys() {
		if (union13 == null) {
			union13 = getUnion(keys1, null, keys3);
		}
		return union13;

	}

	public Collection<Object> getUnion23() {
		Set<T> unionKeys = getUnion(null, keys2, keys3);
		Set<Object> ret = getObjectsByKeys(unionKeys);
		return ret;
	}

	public Set<T> getUnion23Keys() {
		if (union23 == null) {
			union23 = getUnion(null, keys2, keys3);
		}
		return union23;

	}

	private Set<Object> getObjectsByKeys(Collection<T> keys) {
		processCollections();

		Set<Object> ret = new THashSet<Object>();

		for (T key : keys) {
			if (this.hash1.containsKey(key)) {
				ret.add(this.hash1.get(key));
				continue;
			}
			if (this.hash2.containsKey(key)) {
				ret.add(this.hash2.get(key));
				continue;
			}
			if (this.hash3.containsKey(key)) {
				ret.add(this.hash3.get(key));
				continue;
			}

		}
		return ret;
	}

	/**
	 * Gets tbe intersection set of three string collections
	 * 
	 * @param list1
	 * @param list2
	 * @param list3
	 * @return
	 */
	private List<T> getIntersection(Collection<T> list1, Collection<T> list2, Collection<T> list3) {
		processCollections();

		if (list1 == null || list1.isEmpty() || list2 == null || list2.isEmpty() || list3 == null || list3.isEmpty()) {

			return Collections.emptyList();
		}

		// loop with the sortest
		Collection<T> shortest = list1;
		Collection<T> longest;
		Collection<T> longest2;
		if (shortest.size() > list2.size()) {
			shortest = list2;
			longest = list1;
		} else {
			longest = list2;
		}
		if (shortest.size() > list3.size()) {
			longest2 = shortest;
			shortest = list3;
		} else {
			longest2 = list3;
		}

		List<T> ret = new ArrayList<T>();
		for (T key : shortest) {
			if (longest.contains(key) && longest2.contains(key)) {
				ret.add(key);
			}
		}

		return ret;
	}

	private List<T> getIntersection(Collection<T> list1, Collection<T> list2) {
		processCollections();

		if (list1 == null || list1.isEmpty() || list2 == null || list2.isEmpty()) {

			return Collections.emptyList();
		}
		List<T> ret = new ArrayList<T>();
		// loop with the sortest
		Collection<T> shortest;
		Collection<T> longest;
		if (list1.size() > list2.size()) {
			shortest = list2;
			longest = list1;
		} else {
			shortest = list1;
			longest = list2;
		}
		for (T key : shortest) {
			if (longest.contains(key)) {
				ret.add(key);
			}
		}

		return ret;
	}

	/**
	 * Gets tbe union set of three string collections
	 * 
	 * @param list1
	 * @param list2
	 * @param list3
	 * @return
	 */
	private Set<T> getUnion(Collection<T> list1, Collection<T> list2, Collection<T> list3) {
		processCollections();

		// Since the HashSet doesn't allow to add repeated elements, add all to
		// the set
		Set<T> ret = new THashSet<T>();
		if (list1 != null) {
			ret.addAll(list1);
		}
		if (list2 != null) {
			ret.addAll(list2);
		}
		if (list3 != null) {
			ret.addAll(list3);
		}
		return ret;
	}

	public int getSize1() {
		processCollections();
		return this.keys1.size();
	}

	public int getSize2() {
		processCollections();
		return this.keys2.size();
	}

	public int getSize3() {
		processCollections();
		return this.keys3.size();
	}

	public Collection getCollection1() {
		processCollections();
		return this.hash1.values();
	}

	public Collection getCollection2() {
		processCollections();
		return this.hash2.values();
	}

	public Collection getCollection3() {
		processCollections();
		return this.hash3.values();
	}

	/**
	 * Gets the collection that is bigger than the others
	 * 
	 * @return
	 */
	public Collection<Object> getMaxCollection() {
		processCollections();
		Set<Object> ret = new THashSet<Object>();
		ret.addAll(hash1.values());
		if (hash2.size() > hash1.size()) {
			ret.clear();
			ret.addAll(hash2.values());
		}
		if (hash3.size() > hash2.size()) {
			ret.clear();
			ret.addAll(hash3.values());
		}
		return ret;
	}

	protected abstract boolean isObjectValid(Object obj);

}
