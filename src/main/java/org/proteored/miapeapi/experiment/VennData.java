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

	private Map<Object, Set<Object>> hash1 = new THashMap<Object, Set<Object>>();
	private Map<Object, Set<Object>> hash2 = new THashMap<Object, Set<Object>>();
	private Map<Object, Set<Object>> hash3 = new THashMap<Object, Set<Object>>();

	private Set<Object> keys1 = new THashSet<Object>();
	private Set<Object> keys2 = new THashSet<Object>();
	private Set<Object> keys3 = new THashSet<Object>();
	private List<Object> uniqueTo1Keys;
	private List<Object> uniqueTo2Keys;
	private List<Object> uniqueTo3Keys;
	private Set<Object> union12Keys;
	private Set<Object> union13Keys;
	private Set<Object> union23Keys;
	private Set<Object> union123Keys;
	private final Collection<Object> col1;
	private final Collection<Object> col2;
	private final Collection<Object> col3;
	private boolean collectionProcessed;
	private Integer uniqueTo2Num;
	private Integer uniqueTo3Num;
	private Integer uniqueTo1Num;
	private Integer union23Num;
	private Integer union13Num;
	private Integer union12Num;
	private Integer union123Num;
	private Integer intersection23Size;
	private Integer intersection13Size;
	private Integer intersection12Size;
	private Integer intersection123Size;

	public VennData(Collection<Object> col1, Collection<Object> col2, Collection<Object> col3) {

		log.debug("Venn data processing:");
		if (col1 != null) {
			log.debug("Collection 1 contains " + col1.size() + " elements");
		} else {
			log.debug("Collection 1 is empty!");
			hash1 = null;
			keys1 = null;
		}
		if (col2 != null) {
			log.debug("Collection 2 contains " + col2.size() + " elements");
		} else {
			log.debug("Collection 2 is empty!");
			hash2 = null;
			keys2 = null;
		}
		if (col3 != null) {
			log.debug("Collection 3 contains " + col3.size() + " elements");
		} else {
			log.debug("Collection 3 is empty!");
			hash3 = null;
			keys3 = null;
		}
		this.col1 = col1;
		this.col2 = col2;
		this.col3 = col3;
	}

	private final void processCollections() {
		if (!collectionProcessed) {
			log.debug("Processing collection 1 ");
			processCollections(col1, keys1, hash1);
			log.debug("Processing collection 2");
			processCollections(col2, keys2, hash2);
			log.debug("Processing collection 3");
			processCollections(col3, keys3, hash3);
			final StringBuilder logmessage = new StringBuilder();
			if (keys1 != null) {
				logmessage.append("keys1=" + keys1.size());
			} else {
				logmessage.append("keys1=0");
			}
			if (keys2 != null) {
				logmessage.append(", keys2=" + keys2.size());
			} else {
				logmessage.append(", keys2=0");
			}
			if (keys3 != null) {
				logmessage.append(", keys3=" + keys3.size());
			} else {
				logmessage.append(", keys3=0");
			}
			log.debug(logmessage);
		}
		collectionProcessed = true;
	}

	private final void processCollections(Collection<Object> referenceCollection, Collection<Object> keys,
			Map<Object, Set<Object>> hash) {

		final long t1 = System.currentTimeMillis();
		// 1 VS (2 and 3)
		if (referenceCollection != null)
			for (final Object obj1 : referenceCollection) {

				if (isObjectValid(obj1)) {
					final Object key = getKeyFromObject(obj1);
					addTo123UnionKeys(key);
					if (hash.containsKey(key)) {
						hash.get(key).add(obj1);
					} else {
						final Set<Object> set = new THashSet<Object>();
						set.add(obj1);
						hash.put(key, set);
					}
					keys.add(key);
				}
			}
		if (keys != null && referenceCollection != null) {
			log.debug("data collection with " + referenceCollection.size() + " now has " + keys.size()
					+ " keys in a hash of " + hash.size() + " size. ");
			if (keys.size() != referenceCollection.size()) {
				log.debug(keys.size() + " " + referenceCollection.size());
			}
		}

		log.debug(DatesUtil.getDescriptiveTimeFromMillisecs(System.currentTimeMillis() - t1)
				+ " for processing collection");
	}

	private void addTo123UnionKeys(Object key) {
		if (union123Keys == null) {
			union123Keys = new THashSet<Object>();
		}
		union123Keys.add(key);
	}

	protected abstract Object getKeyFromObject(Object obj);

	protected String getUniqueKey() {
		return String.valueOf(count++);
	}

	public List<Object> getIntersection123() {
		final Set<Object> intersectionKeys = getIntersection123Keys();
		final List<Object> ret = getObjectsByKeys(intersectionKeys, true, true, true);
		return ret;
	}

	private Set<Object> getIntersection123Keys() {
		processCollections();
		return getIntersection(keys1, keys2, keys3);
	}

	public int getIntersection123Size() {
		if (intersection123Size == null) {
			intersection123Size = getIntersection123Keys().size();
		}
		return intersection123Size;
	}

	public List<Object> getIntersection12() {
		final Set<Object> intersectionKeys = getIntersection12Keys();
		final List<Object> ret = getObjectsByKeys(intersectionKeys, true, true, false);
		return ret;
	}

	private Set<Object> getIntersection12Keys() {
		processCollections();
		return getIntersection(keys1, keys2);
	}

	public int getIntersection12Size() {
		if (intersection12Size == null) {
			intersection12Size = getIntersection12Keys().size();
		}
		return intersection12Size;
	}

	public List<Object> getIntersection23() {
		final Set<Object> intersectionKeys = getIntersection23Keys();
		final List<Object> ret = getObjectsByKeys(intersectionKeys, false, true, true);
		return ret;
	}

	private Set<Object> getIntersection23Keys() {
		processCollections();
		return getIntersection(keys2, keys3);
	}

	public int getIntersection23Size() {
		if (intersection23Size == null) {
			intersection23Size = getIntersection23Keys().size();
		}
		return intersection23Size;
	}

	public List<Object> getIntersection13() {
		final Set<Object> intersectionKeys = getIntersection13Keys();
		final List<Object> ret = getObjectsByKeys(intersectionKeys, true, false, true);
		return ret;
	}

	private Set<Object> getIntersection13Keys() {
		processCollections();
		return getIntersection(keys1, keys3);
	}

	public int getIntersection13Size() {
		if (intersection13Size == null) {
			intersection13Size = getIntersection13Keys().size();
		}
		return intersection13Size;
	}

	public Collection<Object> getUniqueTo1() {
		final List<Object> uniqueTo1 = getUniqueTo1Keys();
		return getObjectsByKeys(uniqueTo1, true, false, false);
	}

	private List<Object> getUniqueTo1Keys() {
		processCollections();
		if (uniqueTo1Keys == null) {
			uniqueTo1Keys = getUniqueToFirstSet(keys1, keys2, keys3);
		}
		return uniqueTo1Keys;
	}

	public int getUniqueTo1Num() {
		if (uniqueTo1Num == null) {
			uniqueTo1Num = getUniqueTo1().size();
		}
		return uniqueTo1Num;
	}

	public Collection<Object> getUniqueTo2() {
		final List<Object> uniqueTo2 = getUniqueTo2Keys();
		return getObjectsByKeys(uniqueTo2, false, true, false);
	}

	private List<Object> getUniqueTo2Keys() {
		processCollections();
		if (uniqueTo2Keys == null) {
			uniqueTo2Keys = getUniqueToFirstSet(keys2, keys1, keys3);
		}
		return uniqueTo2Keys;
	}

	public int getUniqueTo2Num() {
		if (uniqueTo2Num == null) {
			uniqueTo2Num = getUniqueTo2().size();
		}
		return uniqueTo2Num;
	}

	public Collection<Object> getUniqueTo3() {
		final List<Object> uniqueTo3 = getUniqueTo3Keys();
		return getObjectsByKeys(uniqueTo3, false, false, true);
	}

	private List<Object> getUniqueTo3Keys() {
		processCollections();

		if (uniqueTo3Keys == null) {
			uniqueTo3Keys = getUniqueToFirstSet(keys3, keys1, keys2);
		}
		return uniqueTo3Keys;
	}

	public int getUniqueTo3Num() {
		if (uniqueTo3Num == null) {
			uniqueTo3Num = getUniqueTo3().size();
		}
		return uniqueTo3Num;

	}

	private static List<Object> getUniqueToFirstSet(Collection<Object> hashToIsolate, Collection<Object> hash2,
			Collection<Object> hash3) {
		final long t1 = System.currentTimeMillis();

		final List<Object> ret = new ArrayList<Object>();
		if (hashToIsolate == null) {
			return ret;
		}
		if (hash2 == null) {
			ret.addAll(hashToIsolate);
			return ret;
		}
		log.debug("Unique to isolate size = " + hashToIsolate.size());
		if (hashToIsolate != null) {
			final Iterator<Object> toIsolateIterator = hashToIsolate.iterator();

			while (toIsolateIterator.hasNext()) {
				final Object item = toIsolateIterator.next();
				if (hash2 != null && hash2.contains(item)) {
					continue;
				} else if (hash3 != null && hash3.contains(item)) {
					continue;
				}
				ret.add(item);

			}
		}
		log.debug(
				DatesUtil.getDescriptiveTimeFromMillisecs(System.currentTimeMillis() - t1) + " getting unique objects");
		return ret;
	}

	public List<Object> getUnion123() {
		final Set<Object> unionKeys = getUnion123Keys();
		final List<Object> ret = getObjectsByKeys(unionKeys, true, true, true);
		return ret;
	}

	private Set<Object> getUnion123Keys() {
		processCollections();

		if (union123Keys == null) {
			union123Keys = getUnion(keys1, keys2, keys3);
		}
		return union123Keys;
	}

	public int getUnion123Size() {
		if (union123Num == null) {
			union123Num = getUnion123Keys().size();
		}
		return union123Num;
	}

	public List<Object> getUnion12() {
		final Set<Object> unionKeys = getUnion12Keys();
		final List<Object> ret = getObjectsByKeys(unionKeys, true, true, false);
		return ret;
	}

	private Set<Object> getUnion12Keys() {
		processCollections();

		if (union12Keys == null) {
			union12Keys = getUnion(keys1, keys2, null);
		}
		return union12Keys;

	}

	public int getUnion12Size() {
		if (union12Num == null) {
			union12Num = getUnion12Keys().size();
		}
		return union12Num;
	}

	public List<Object> getUnion13() {
		final Set<Object> unionKeys = getUnion13Keys();
		final List<Object> ret = getObjectsByKeys(unionKeys, true, false, true);
		return ret;
	}

	private Set<Object> getUnion13Keys() {
		processCollections();

		if (union13Keys == null) {
			union13Keys = getUnion(keys1, null, keys3);
		}
		return union13Keys;

	}

	public int getUnion13Size() {
		if (union13Num == null) {
			union13Num = getUnion13Keys().size();
		}
		return union13Num;
	}

	public List<Object> getUnion23() {
		final Set<Object> unionKeys = getUnion23Keys();
		final List<Object> ret = getObjectsByKeys(unionKeys, false, true, true);
		return ret;
	}

	private Set<Object> getUnion23Keys() {
		processCollections();

		if (union23Keys == null) {
			union23Keys = getUnion(null, keys2, keys3);
		}
		return union23Keys;

	}

	public int getUnion23Size() {
		if (union23Num == null) {
			union23Num = getUnion23Keys().size();
		}
		return union23Num;
	}

	private List<Object> getObjectsByKeys(Collection<Object> keys, boolean from1, boolean from2, boolean from3) {
		processCollections();

		final List<Object> ret = new ArrayList<Object>();

		for (final Object key : keys) {
			if (from1 && hash1 != null && hash1.containsKey(key)) {
				ret.addAll(hash1.get(key));

			}
			if (from2 && hash2 != null && hash2.containsKey(key)) {
				ret.addAll(hash2.get(key));

			}
			if (from3 && hash3 != null && hash3.containsKey(key)) {
				ret.addAll(hash3.get(key));

			}

		}
		return ret;
	}

	/**
	 * Gets tbe intersection set of three string collections
	 * 
	 * @param col1
	 * @param col2
	 * @param col3
	 * @return
	 */
	private static Set<Object> getIntersection(Collection<Object> col1, Collection<Object> col2,
			Collection<Object> col3) {

		if (col1 == null || col1.isEmpty() || col2 == null || col2.isEmpty() || col3 == null || col3.isEmpty()) {

			return Collections.emptySet();
		}

		// loop with the sortest
		Collection<Object> shortest = col1;
		Collection<Object> longest;
		Collection<Object> longest2;
		if (shortest.size() > col2.size()) {
			shortest = col2;
			longest = col1;
		} else {
			longest = col2;
		}
		if (shortest.size() > col3.size()) {
			longest2 = shortest;
			shortest = col3;
		} else {
			longest2 = col3;
		}

		final Set<Object> ret = new THashSet<Object>();
		for (final Object key : shortest) {
			if (longest.contains(key) && longest2.contains(key)) {
				ret.add(key);
			}
		}

		return ret;
	}

	private static Set<Object> getIntersection(Collection<Object> col1, Collection<Object> col2) {

		if (col1 == null || col1.isEmpty() || col2 == null || col2.isEmpty()) {

			return Collections.emptySet();
		}
		final Set<Object> ret = new THashSet<Object>();
		// loop with the sortest
		Collection<Object> shortest;
		Collection<Object> longest;
		if (col1.size() > col2.size()) {
			shortest = col2;
			longest = col1;
		} else {
			shortest = col1;
			longest = col2;
		}
		for (final Object key : shortest) {
			if (longest.contains(key)) {
				ret.add(key);
			}
		}

		return ret;
	}

	/**
	 * Gets the union set of three string collections
	 * 
	 * @param list1
	 * @param list2
	 * @param list3
	 * @return
	 */
	private static Set<Object> getUnion(Set<Object> list1, Set<Object> list2, Set<Object> list3) {

		// Since the HashSet doesn't allow to add repeated elements, add all to
		// the set
		final Set<Object> ret = new THashSet<Object>();
		if (list1 != null && list2 == null && list3 == null) {
			return list1;
		}
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
		if (col1 != null) {
			return col1.size();
		}
		return null;
	}

	public Integer getSize2() {
		if (col2 != null) {
			return col2.size();
		}
		return null;
	}

	public Integer getSize3() {
		if (col3 != null) {
			return col3.size();
		}
		return null;
	}

	public Collection getCollection1() {

		return col1;
	}

	public Collection getCollection2() {

		return col2;
	}

	public Collection getCollection3() {

		return col3;
	}

	/**
	 * Gets the collection that is bigger than the others
	 * 
	 * @return
	 */
	public Integer getMaxCollectionSize() {
		processCollections();
		Integer ret = null;
		if (col1 != null) {
			if (ret == null) {
				ret = col1.size();
			} else {
				if (ret < col1.size()) {
					ret = col1.size();
				}
			}

		}
		if (col2 != null) {
			if (ret == null) {
				ret = col2.size();
			} else {
				if (ret < col2.size()) {
					ret = col2.size();
				}
			}

		}
		if (col3 != null) {
			if (ret == null) {
				ret = col3.size();
			} else {
				if (ret < col3.size()) {
					ret = col3.size();
				}
			}

		}
		return ret;
	}

	protected abstract boolean isObjectValid(Object obj);

}
