package org.proteored.miapeapi.experiment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.proteored.miapeapi.experiment.model.ExtendedIdentifiedPeptide;
import org.proteored.miapeapi.experiment.model.PeptideOccurrence;
import org.proteored.miapeapi.experiment.model.ProteinGroupOccurrence;
import org.proteored.miapeapi.experiment.model.sort.ProteinComparatorKey;
import org.proteored.miapeapi.experiment.model.sort.ProteinGroupComparisonType;

public class VennData {
	private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger("log4j.logger.org.proteored");
	private static int count = 0;
	private final HashMap<ProteinComparatorKey, Object> hash1 = new HashMap<ProteinComparatorKey, Object>();
	private final HashMap<ProteinComparatorKey, Object> hash2 = new HashMap<ProteinComparatorKey, Object>();
	private final HashMap<ProteinComparatorKey, Object> hash3 = new HashMap<ProteinComparatorKey, Object>();

	private final ProteinGroupComparisonType proteinComparisonType;
	private List<ProteinComparatorKey> keys1 = new ArrayList<ProteinComparatorKey>();
	private List<ProteinComparatorKey> keys2 = new ArrayList<ProteinComparatorKey>();
	private List<ProteinComparatorKey> keys3 = new ArrayList<ProteinComparatorKey>();
	private HashMap<Object, String> hash = new HashMap<Object, String>();
	private final Boolean countNonConclusiveProteins;
	private List<ProteinComparatorKey> uniqueTo1Keys;
	private List<ProteinComparatorKey> uniqueTo2Keys;
	private List<ProteinComparatorKey> uniqueTo3Keys;
	private Set<ProteinComparatorKey> union12;
	private Set<ProteinComparatorKey> union13;
	private Set<ProteinComparatorKey> union23;
	private Set<ProteinComparatorKey> union123;

	public VennData(Collection col1, Collection col2, Collection col3, ProteinGroupComparisonType proteinSelection,
			Boolean countNonConclusiveProteins) {

		this.proteinComparisonType = proteinSelection;

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
		this.countNonConclusiveProteins = countNonConclusiveProteins;
		log.info("Processing collection 1");
		processCollections(col1, keys1, hash1);
		log.info("Processing collection 2");
		processCollections(col2, keys2, hash2);
		log.info("Processing collection 3");
		processCollections(col3, keys3, hash3);
		log.info("keys1=" + keys1.size() + "keys2=" + keys2.size() + "keys3=" + keys3.size());

	}

	private void processCollections(Collection<Object> referenceCollection, List<ProteinComparatorKey> keys1,
			HashMap<ProteinComparatorKey, Object> hash1) {

		int uniques = 0; // number of unique objects in reference collection

		// Se trata de ir comparando dos a dos los grupos. Cuando tengan un
		// objecto en común, se les asigna a los dos una clave de tipo cadena de
		// texto. Si un elemento no se encuentra en el otro grupo, se le
		// asignará otra cadena.
		int numDiscarded = 0;
		// 1 VS (2 and 3)
		if (referenceCollection != null)
			for (Object obj1 : referenceCollection) {

				if (this.proteinComparisonType == null) {
					// if (!countNonConclusiveProteins) {
					// if (obj1 instanceof ExtendedIdentifiedPeptide) {
					// if (((ExtendedIdentifiedPeptide) obj1).getRelation() ==
					// PeptideRelation.NONDISCRIMINATING) {
					// numDiscarded++;
					// continue;
					// }
					// }
					// if (obj1 instanceof PeptideOccurrence) {
					// if (((PeptideOccurrence) obj1).getFirstOccurrence()
					// .getRelation() == PeptideRelation.NONDISCRIMINATING) {
					// numDiscarded++;
					// continue;
					// }
					// }
					//
					// }
					// Objects are peptides: compare by Peptide sequence
					ProteinComparatorKey seq = getPeptideSequence(obj1);
					// String objKey = getUniqueKey();
					// if (this.hash.containsKey(seq)) {
					// objKey = this.hash.get(seq);
					// } else {
					// this.hash.put(seq, objKey);
					// }
					hash1.put(seq, obj1);
					// if (keys1 == null)
					// keys1 = new HashSet<String>();
					keys1.add(seq);

				} else {
					// proteins
					ProteinGroupOccurrence pgo = (ProteinGroupOccurrence) obj1;
					// if (pgo.getEvidence() == ProteinEvidence.NONCONCLUSIVE &&
					// !countNonConclusiveProteins) {
					// numDiscarded++;
					// continue;
					// }
					if (pgo.getAccessions().contains("Q04917")) {
						log.info(pgo);
					}
					ProteinComparatorKey pgoKey = pgo.getKey(this.proteinComparisonType);
					// String objKey = getUniqueKey();
					// if (this.hash.containsKey(pgoKey)) {
					// objKey = this.hash.get(pgoKey);
					// } else {
					// this.hash.put(pgoKey, objKey);
					// }
					hash1.put(pgoKey, obj1);
					// if (keys1 == null) {
					// keys1 = new HashSet<String>();
					// }
					keys1.add(pgoKey);
				}
			}
		if (keys1 != null && referenceCollection != null) {
			log.info("data collection with " + referenceCollection.size() + " now has " + keys1.size() + "/"
					+ hash1.size() + " string keys. " + uniques + " are uniques, after discarding " + numDiscarded);
			log.info("");
		}

	}

	private ProteinComparatorKey getPeptideSequence(Object obj1) {
		if (obj1 instanceof ExtendedIdentifiedPeptide) {
			return new ProteinComparatorKey(((ExtendedIdentifiedPeptide) obj1).getSequence(),
					ProteinGroupComparisonType.ALL_PROTEINS);
		}
		if (obj1 instanceof PeptideOccurrence) {
			return new ProteinComparatorKey(((PeptideOccurrence) obj1).getKey(),
					ProteinGroupComparisonType.ALL_PROTEINS);
		}
		if (obj1 instanceof ProteinComparatorKey) {
			return new ProteinComparatorKey(((ProteinComparatorKey) obj1).getAccessionString(),
					ProteinGroupComparisonType.ALL_PROTEINS);
		}
		return new ProteinComparatorKey(obj1.toString(), ProteinGroupComparisonType.ALL_PROTEINS);
	}

	private String getUniqueKey() {
		return String.valueOf(VennData.count++);
	}

	public Collection<Object> getIntersection123() {
		List<ProteinComparatorKey> intersectionKeys = getIntersection(keys1, keys2, keys3);
		Set<Object> ret = getObjectsByKeys(intersectionKeys);
		return ret;
	}

	public List<ProteinComparatorKey> getIntersection123Keys() {
		return getIntersection(keys1, keys2, keys3);
	}

	public Collection<Object> getIntersection12() {
		List<ProteinComparatorKey> intersectionKeys = getIntersection(keys1, keys2);
		Set<Object> ret = getObjectsByKeys(intersectionKeys);
		return ret;
	}

	public List<ProteinComparatorKey> getIntersection12Keys() {
		return getIntersection(keys1, keys2);
	}

	public Collection<Object> getIntersection23() {
		List<ProteinComparatorKey> intersectionKeys = getIntersection(keys2, keys3);
		Set<Object> ret = getObjectsByKeys(intersectionKeys);
		return ret;
	}

	public List<ProteinComparatorKey> getIntersection23Keys() {
		return getIntersection(keys2, keys3);
	}

	public Collection<Object> getIntersection13() {
		List<ProteinComparatorKey> intersectionKeys = getIntersection(keys1, keys3);
		Set<Object> ret = getObjectsByKeys(intersectionKeys);
		return ret;
	}

	public List<ProteinComparatorKey> getIntersection13Keys() {
		return getIntersection(keys1, keys3);
	}

	public Collection<Object> getUniqueTo1() {
		List<ProteinComparatorKey> uniqueTo1 = getUniqueToFirstSet(keys1, keys2, keys3);
		return this.getObjectsByKeys(uniqueTo1);
	}

	public List<ProteinComparatorKey> getUniqueTo1Keys() {
		if (uniqueTo1Keys == null) {
			uniqueTo1Keys = getUniqueToFirstSet(keys1, keys2, keys3);
		}
		return uniqueTo1Keys;
	}

	public Collection<Object> getUniqueTo2() {
		List<ProteinComparatorKey> uniqueTo2 = getUniqueToFirstSet(keys2, keys1, keys3);
		return this.getObjectsByKeys(uniqueTo2);
	}

	public List<ProteinComparatorKey> getUniqueTo2Keys() {
		if (uniqueTo2Keys == null) {
			uniqueTo2Keys = getUniqueToFirstSet(keys2, keys1, keys3);
		}
		return uniqueTo2Keys;
	}

	public Collection<Object> getUniqueTo3() {
		List<ProteinComparatorKey> uniqueTo3 = getUniqueToFirstSet(keys3, keys1, keys2);
		return this.getObjectsByKeys(uniqueTo3);
	}

	public List<ProteinComparatorKey> getUniqueTo3Keys() {
		if (uniqueTo3Keys == null) {
			uniqueTo3Keys = getUniqueToFirstSet(keys3, keys1, keys2);
		}
		return uniqueTo3Keys;
	}

	private List<ProteinComparatorKey> getUniqueToFirstSet(Collection<ProteinComparatorKey> hashToIsolate,
			Collection<ProteinComparatorKey> hash2, Collection<ProteinComparatorKey> hash3) {
		List<ProteinComparatorKey> toIsolateSet2 = new ArrayList<ProteinComparatorKey>();
		log.info("Unique to isolate size=" + hashToIsolate.size());
		if (hashToIsolate != null) {
			toIsolateSet2.addAll(hashToIsolate);
			Iterator<ProteinComparatorKey> toIsolateIterator = toIsolateSet2.iterator();

			while (toIsolateIterator.hasNext()) {
				ProteinComparatorKey item = toIsolateIterator.next();
				if (item.getAccessionString().contains("Q04917")) {
					log.info(item.getAccessionString());
				}
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
		Set<ProteinComparatorKey> unionKeys = getUnion(keys1, keys2, keys3);
		Set<Object> ret = getObjectsByKeys(unionKeys);
		return ret;
	}

	public Collection<ProteinComparatorKey> getUnion123Keys() {
		if (union123 == null) {
			union123 = getUnion(keys1, keys2, keys3);
		}
		return union123;
	}

	public Collection<Object> getUnion12() {
		Set<ProteinComparatorKey> unionKeys = getUnion(keys1, keys2, null);
		Set<Object> ret = getObjectsByKeys(unionKeys);
		return ret;
	}

	public Set<ProteinComparatorKey> getUnion12Keys() {
		if (union12 == null) {
			union12 = getUnion(keys1, keys2, null);
		}
		return union12;

	}

	public Collection<Object> getUnion13() {
		Set<ProteinComparatorKey> unionKeys = getUnion(keys1, null, keys3);
		Set<Object> ret = getObjectsByKeys(unionKeys);
		return ret;
	}

	public Set<ProteinComparatorKey> getUnion13Keys() {
		if (union13 == null) {
			union13 = getUnion(keys1, null, keys3);
		}
		return union13;

	}

	public Collection<Object> getUnion23() {
		Set<ProteinComparatorKey> unionKeys = getUnion(null, keys2, keys3);
		Set<Object> ret = getObjectsByKeys(unionKeys);
		return ret;
	}

	public Set<ProteinComparatorKey> getUnion23Keys() {
		if (union23 == null) {
			union23 = getUnion(null, keys2, keys3);
		}
		return union23;

	}

	private Set<Object> getObjectsByKeys(Collection<ProteinComparatorKey> keys) {
		Set<Object> ret = new HashSet<Object>();

		for (ProteinComparatorKey key : keys) {
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
	private List<ProteinComparatorKey> getIntersection(Collection<ProteinComparatorKey> list1,
			Collection<ProteinComparatorKey> list2, Collection<ProteinComparatorKey> list3) {

		if (list1 == null || list1.isEmpty() || list2 == null || list2.isEmpty() || list3 == null || list3.isEmpty()) {

			return Collections.emptyList();
		}

		// loop with the sortest
		Collection<ProteinComparatorKey> shortest = list1;
		Collection<ProteinComparatorKey> longest;
		Collection<ProteinComparatorKey> longest2;
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

		List<ProteinComparatorKey> ret = new ArrayList<ProteinComparatorKey>();
		for (ProteinComparatorKey key : shortest) {
			if (longest.contains(key) && longest2.contains(key)) {
				ret.add(key);
			}
		}

		return ret;
	}

	private List<ProteinComparatorKey> getIntersection(Collection<ProteinComparatorKey> list1,
			Collection<ProteinComparatorKey> list2) {

		if (list1 == null || list1.isEmpty() || list2 == null || list2.isEmpty()) {

			return Collections.emptyList();
		}
		List<ProteinComparatorKey> ret = new ArrayList<ProteinComparatorKey>();
		// loop with the sortest
		Collection<ProteinComparatorKey> shortest;
		Collection<ProteinComparatorKey> longest;
		if (list1.size() > list2.size()) {
			shortest = list2;
			longest = list1;
		} else {
			shortest = list1;
			longest = list2;
		}
		for (ProteinComparatorKey key : shortest) {
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
	private Set<ProteinComparatorKey> getUnion(Collection<ProteinComparatorKey> list1,
			Collection<ProteinComparatorKey> list2, Collection<ProteinComparatorKey> list3) {
		// Since the HashSet doesn't allow to add repeated elements, add all to
		// the set
		Set<ProteinComparatorKey> ret = new HashSet<ProteinComparatorKey>();
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
		return this.keys1.size();
	}

	public int getSize2() {
		return this.keys2.size();
	}

	public int getSize3() {
		return this.keys3.size();
	}

	public Collection getCollection1() {
		return this.hash1.values();
	}

	public Collection getCollection2() {
		return this.hash2.values();
	}

	public Collection getCollection3() {
		return this.hash3.values();
	}

	/**
	 * Gets the collection that is bigger than the others
	 * 
	 * @return
	 */
	public Collection<Object> getMaxCollection() {
		HashSet<Object> ret = new HashSet<Object>();
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

}
