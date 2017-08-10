package org.proteored.miapeapi.experiment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.scripps.yates.utilities.dates.DatesUtil;
import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;

public abstract class VennData {
	private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger("log4j.logger.org.proteored");
	private static int count = 0;

	private final Map<Object, Object> hash1 = new THashMap<Object, Object>();
	private final Map<Object, Object> hash2 = new THashMap<Object, Object>();
	private final Map<Object, Object> hash3 = new THashMap<Object, Object>();

	private Set<Object> keys1 = new THashSet<Object>();
	private Set<Object> keys2 = new THashSet<Object>();
	private Set<Object> keys3 = new THashSet<Object>();
	private List<Object> uniqueTo1Keys;
	private List<Object> uniqueTo2Keys;
	private List<Object> uniqueTo3Keys;
	private Set<Object> union12;
	private Set<Object> union13;
	private Set<Object> union23;
	private Set<Object> union123;
	private Collection<Object> col1;
	private Collection<Object> col2;
	private Collection<Object> col3;
	private boolean collectionProcessed;
	private Integer uniqueTo2Num;
	private Integer uniqueTo3Num;
	private Integer uniqueTo1Num;

	public VennData(Collection<Object> col1, Collection<Object> col2, Collection<Object> col3) {

		log.debug("Venn data processing:");
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

	private final void processCollections(Collection<Object> referenceCollection, Set<Object> keys1,
			Map<Object, Object> hash1) {

		long t1 = System.currentTimeMillis();
		// 1 VS (2 and 3)
		if (referenceCollection != null)
			for (Object obj1 : referenceCollection) {
				if (isObjectValid(obj1)) {
					Object key = getKeyFromObject(obj1);
					addTo123UnionKeys(key);
					hash1.put(key, obj1);
					keys1.add(key);
				}
			}
		if (keys1 != null && referenceCollection != null) {
			log.info("data collection with " + referenceCollection.size() + " now has " + keys1.size() + "/"
					+ hash1.size() + " string keys. ");
		}
		log.info(DatesUtil.getDescriptiveTimeFromMillisecs(System.currentTimeMillis() - t1)
				+ " for processing collection");
	}

	private void addTo123UnionKeys(Object key) {
		if (union123 == null) {
			union123 = new THashSet<Object>();
		}
		union123.add(key);
	}

	protected abstract Object getKeyFromObject(Object obj);

	protected String getUniqueKey() {
		return String.valueOf(count++);
	}

	public Collection<Object> getIntersection123() {
		List<Object> intersectionKeys = getIntersection(keys1, keys2, keys3);
		Set<Object> ret = getObjectsByKeys(intersectionKeys);
		return ret;
	}

	public List<Object> getIntersection123Keys() {
		return getIntersection(keys1, keys2, keys3);
	}

	public Collection<Object> getIntersection12() {
		List<Object> intersectionKeys = getIntersection(keys1, keys2);
		Set<Object> ret = getObjectsByKeys(intersectionKeys);
		return ret;
	}

	public List<Object> getIntersection12Keys() {
		return getIntersection(keys1, keys2);
	}

	public Collection<Object> getIntersection23() {
		List<Object> intersectionKeys = getIntersection(keys2, keys3);
		Set<Object> ret = getObjectsByKeys(intersectionKeys);
		return ret;
	}

	public List<Object> getIntersection23Keys() {
		return getIntersection(keys2, keys3);
	}

	public Collection<Object> getIntersection13() {
		List<Object> intersectionKeys = getIntersection(keys1, keys3);
		Set<Object> ret = getObjectsByKeys(intersectionKeys);
		return ret;
	}

	public List<Object> getIntersection13Keys() {
		return getIntersection(keys1, keys3);
	}

	public Collection<Object> getUniqueTo1() {
		List<Object> uniqueTo1 = getUniqueTo1Keys();
		return this.getObjectsByKeys(uniqueTo1);
	}

	public List<Object> getUniqueTo1Keys() {
		processCollections();
		if (uniqueTo1Keys == null) {
			uniqueTo1Keys = getUniqueToFirstSet(keys1, keys2, keys3);
		}
		return uniqueTo1Keys;
	}

	public int getUniqueTo1KeysNum() {
		if (uniqueTo1Num == null) {
			uniqueTo1Num = getUniqueToFirstSetNum(keys1, keys2, keys3);
		}
		return uniqueTo1Num;

	}

	public Collection<Object> getUniqueTo2() {
		List<Object> uniqueTo2 = getUniqueTo2Keys();
		return this.getObjectsByKeys(uniqueTo2);
	}

	public List<Object> getUniqueTo2Keys() {
		processCollections();
		if (uniqueTo2Keys == null) {
			uniqueTo2Keys = getUniqueToFirstSet(keys2, keys1, keys3);
		}
		return uniqueTo2Keys;
	}

	public int getUniqueTo2KeysNum() {
		if (uniqueTo2Num == null) {
			uniqueTo2Num = getUniqueToFirstSetNum(keys2, keys1, keys3);
		}
		return uniqueTo2Num;

	}

	public Collection<Object> getUniqueTo3() {
		List<Object> uniqueTo3 = getUniqueTo3Keys();
		return this.getObjectsByKeys(uniqueTo3);
	}

	public List<Object> getUniqueTo3Keys() {
		if (uniqueTo3Keys == null) {
			uniqueTo3Keys = getUniqueToFirstSet(keys3, keys1, keys2);
		}
		return uniqueTo3Keys;
	}

	public int getUniqueTo3KeysNum() {
		if (uniqueTo3Num == null) {
			uniqueTo3Num = getUniqueToFirstSetNum(keys3, keys1, keys2);
		}
		return uniqueTo3Num;

	}

	private List<Object> getUniqueToFirstSet(Collection<Object> hashToIsolate, Collection<Object> hash2,
			Collection<Object> hash3) {
		processCollections();
		long t1 = System.currentTimeMillis();
		List<Object> toIsolateSet2 = new ArrayList<Object>();
		log.info("Unique to isolate size = " + hashToIsolate.size());
		if (hashToIsolate != null) {
			toIsolateSet2.addAll(hashToIsolate);
			Iterator<Object> toIsolateIterator = toIsolateSet2.iterator();

			while (toIsolateIterator.hasNext()) {
				Object item = toIsolateIterator.next();
				if (hash2 != null && hash2.contains(item)) {
					toIsolateIterator.remove();
				} else if (hash3 != null && hash3.contains(item)) {
					toIsolateIterator.remove();
				}
			}
		}
		log.info(
				DatesUtil.getDescriptiveTimeFromMillisecs(System.currentTimeMillis() - t1) + " getting unique objects");
		return toIsolateSet2;
	}

	private int getUniqueToFirstSetNum(Collection<Object> hashToIsolate, Collection<Object> hash2,
			Collection<Object> hash3) {

		processCollections();
		int ret = 0;
		long t1 = System.currentTimeMillis();
		log.info("Unique to isolate size=" + hashToIsolate.size());
		if (hashToIsolate != null) {
			for (Object item : hashToIsolate) {
				if (hash2 != null && hash2.contains(item)) {
					continue;
				} else if (hash3 != null && hash3.contains(item)) {
					continue;
				}
				ret++;
			}
		}
		log.info(DatesUtil.getDescriptiveTimeFromMillisecs(System.currentTimeMillis() - t1) + " getting unique number");

		return ret;
	}

	public Collection<Object> getUnion123() {
		Set<Object> unionKeys = getUnion123Keys();
		Set<Object> ret = getObjectsByKeys(unionKeys);
		return ret;
	}

	public Set<Object> getUnion123Keys() {
		if (union123 == null) {
			union123 = getUnion(keys1, keys2, keys3);
		}
		return union123;
	}

	public Collection<Object> getUnion12() {
		Set<Object> unionKeys = getUnion12Keys();
		Set<Object> ret = getObjectsByKeys(unionKeys);
		return ret;
	}

	public Set<Object> getUnion12Keys() {
		if (union12 == null) {
			union12 = getUnion(keys1, keys2, null);
		}
		return union12;

	}

	public Collection<Object> getUnion13() {
		Set<Object> unionKeys = getUnion13Keys();
		Set<Object> ret = getObjectsByKeys(unionKeys);
		return ret;
	}

	public Set<Object> getUnion13Keys() {
		if (union13 == null) {
			union13 = getUnion(keys1, null, keys3);
		}
		return union13;

	}

	public Collection<Object> getUnion23() {
		Set<Object> unionKeys = getUnion23Keys();
		Set<Object> ret = getObjectsByKeys(unionKeys);
		return ret;
	}

	public Set<Object> getUnion23Keys() {
		if (union23 == null) {
			union23 = getUnion(null, keys2, keys3);
		}
		return union23;

	}

	private Set<Object> getObjectsByKeys(Collection<Object> keys) {
		processCollections();

		Set<Object> ret = new THashSet<Object>();

		for (Object key : keys) {
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
	private List<Object> getIntersection(Collection<Object> list1, Collection<Object> list2, Collection<Object> list3) {
		processCollections();

		if (list1 == null || list1.isEmpty() || list2 == null || list2.isEmpty() || list3 == null || list3.isEmpty()) {

			return Collections.emptyList();
		}

		// loop with the sortest
		Collection<Object> shortest = list1;
		Collection<Object> longest;
		Collection<Object> longest2;
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

		List<Object> ret = new ArrayList<Object>();
		for (Object key : shortest) {
			if (longest.contains(key) && longest2.contains(key)) {
				ret.add(key);
			}
		}

		return ret;
	}

	private List<Object> getIntersection(Collection<Object> list1, Collection<Object> list2) {
		processCollections();

		if (list1 == null || list1.isEmpty() || list2 == null || list2.isEmpty()) {

			return Collections.emptyList();
		}
		List<Object> ret = new ArrayList<Object>();
		// loop with the sortest
		Collection<Object> shortest;
		Collection<Object> longest;
		if (list1.size() > list2.size()) {
			shortest = list2;
			longest = list1;
		} else {
			shortest = list1;
			longest = list2;
		}
		for (Object key : shortest) {
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
	private Set<Object> getUnion(Collection<Object> list1, Collection<Object> list2, Collection<Object> list3) {
		processCollections();

		// Since the HashSet doesn't allow to add repeated elements, add all to
		// the set
		Set<Object> ret = new THashSet<Object>();
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

	public Integer getSize1() {
		processCollections();
		if (this.col1 != null) {
			return this.keys1.size();
		}
		return null;
	}

	public Integer getSize2() {
		processCollections();
		if (this.col2 != null) {
			return this.keys2.size();
		}
		return null;
	}

	public Integer getSize3() {
		processCollections();
		if (this.col3 != null) {
			return this.keys3.size();
		}
		return null;
	}

	public Collection getCollection1() {
		processCollections();
		if (this.col1 != null) {
			return this.hash1.values();
		}
		return null;
	}

	public Collection getCollection2() {
		processCollections();
		if (this.col2 != null) {
			return this.hash2.values();
		}
		return null;
	}

	public Collection getCollection3() {
		processCollections();
		if (this.col3 != null) {
			return this.hash3.values();
		}
		return null;
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
