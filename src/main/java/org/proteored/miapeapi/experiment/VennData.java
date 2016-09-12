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
import org.proteored.miapeapi.experiment.model.grouping.ProteinEvidence;
import org.proteored.miapeapi.xml.util.ProteinGroupComparisonType;

public class VennData {
	private static org.apache.log4j.Logger log = org.apache.log4j.Logger
			.getLogger("log4j.logger.org.proteored");
	private static int count = 0;
	private final HashMap<String, Object> hash1 = new HashMap<String, Object>();
	private final HashMap<String, Object> hash2 = new HashMap<String, Object>();
	private final HashMap<String, Object> hash3 = new HashMap<String, Object>();

	private final ProteinGroupComparisonType proteinSelection;
	private Set<String> keys1 = new HashSet<String>();
	private Set<String> keys2 = new HashSet<String>();
	private Set<String> keys3 = new HashSet<String>();
	private HashMap<String, String> hash = new HashMap<String, String>();
	private final Boolean countNonConclusiveProteins;

	public VennData(Collection col1, Collection col2, Collection col3,
			ProteinGroupComparisonType proteinSelection,
			Boolean countNonConclusiveProteins) {

		this.proteinSelection = proteinSelection;

		log.debug("VeDnn data processing:");
		if (col1 != null)
			log.debug("Collection 1 contains " + col1.size() + " elements");
		else
			log.debug("Collection 1 is empty!");
		if (col2 != null)
			log.debug("Collection 2 contains " + col2.size() + " elements");
		else
			log.debug("Collection 2 is empty!");
		if (col3 != null)
			log.debug("Collection 3 contains " + col3.size() + " elements");
		else
			log.debug("Collection 3 is empty!");
		this.countNonConclusiveProteins = countNonConclusiveProteins;
		log.debug("Processing collection 1");
		processCollections(col1, keys1, hash1, countNonConclusiveProteins);
		log.debug("Processing collection 2");
		processCollections(col2, keys2, hash2, countNonConclusiveProteins);
		log.debug("Processing collection 3");
		processCollections(col3, keys3, hash3, countNonConclusiveProteins);

	}

	private void processCollections(Collection<Object> referenceCollection,
			Set<String> keys1, HashMap<String, Object> hash1,
			Boolean countNonConclusiveProteins) {

		int uniques = 0; // number of unique objects in reference collection

		// Se trata de ir comparando dos a dos los grupos. Cuando tengan un
		// objecto en común, se les asigna a los dos una clave de tipo cadena de
		// texto. Si un elemento no se encuentra en el otro grupo, se le
		// asignará otra cadena.

		// 1 VS (2 and 3)
		if (referenceCollection != null)
			for (Object obj1 : referenceCollection) {

				if (this.proteinSelection == null) {
					// Objects are peptides: compare by Peptide sequence
					String seq = getPeptideSequence(obj1);
					String objKey = getUniqueKey();
					if (this.hash.containsKey(seq)) {
						objKey = this.hash.get(seq);
					} else {
						this.hash.put(seq, objKey);
					}
					hash1.put(objKey, obj1);
					if (keys1 == null)
						keys1 = new HashSet<String>();
					keys1.add(objKey);

				} else {
					ProteinGroupOccurrence pgo = (ProteinGroupOccurrence) obj1;
					if (pgo.getEvidence() == ProteinEvidence.NONCONCLUSIVE
							&& !countNonConclusiveProteins)
						continue;
					if (pgo.getEvidence() != ProteinEvidence.NONCONCLUSIVE) {
						String pgoKey = pgo.getKey(this.proteinSelection);
						String objKey = getUniqueKey();
						if (this.hash.containsKey(pgoKey)) {
							objKey = this.hash.get(pgoKey);
						} else {
							this.hash.put(pgoKey, objKey);
						}
						hash1.put(objKey, obj1);
						if (keys1 == null)
							keys1 = new HashSet<String>();
						keys1.add(objKey);

						// if (ProteinGroupComparisonType.ALL_PROTEINS
						//
						// .equals(this.proteinSelection)) {
						// String allAccs = pgo.getAccessionsString();
						// String objKey = getUniqueKey();
						// if (this.hash.containsKey(allAccs)) {
						// objKey = this.hash.get(allAccs);
						// } else {
						// this.hash.put(allAccs, objKey);
						// }
						// hash1.put(objKey, obj1);
						// if (keys1 == null)
						// keys1 = new HashSet<String>();
						// keys1.add(objKey);
						// } else if (ProteinGroupComparisonType.BEST_PROTEIN
						// .equals(this.proteinSelection)) {
						// String acc = pgo.getBestProtein().getAccession();
						// String objKey = getUniqueKey();
						// if (this.hash.containsKey(acc)) {
						// objKey = this.hash.get(acc);
						// } else {
						// this.hash.put(acc, objKey);
						// }
						// hash1.put(objKey, obj1);
						// if (keys1 == null)
						// keys1 = new HashSet<String>();
						// keys1.add(objKey);
						// } else if (ProteinGroupComparisonType.FIRST_PROTEIN
						// .equals(this.proteinSelection)) {
						// String acc = pgo.getAccessions().get(0);
						// String objKey = getUniqueKey();
						// if (this.hash.containsKey(acc)) {
						// objKey = this.hash.get(acc);
						// } else {
						// this.hash.put(acc, objKey);
						// }
						// hash1.put(objKey, obj1);
						// if (keys1 == null)
						// keys1 = new HashSet<String>();
						// keys1.add(objKey);
						// } else if (ProteinSelection.SHARE_ONE_PROTEIN
						// .equals(this.proteinSelection)) {
						//
						// }
					}
				}

				// boolean found = false;
				// for (Object obj2 : this.hash.keySet()) {
				// if ((this.proteinSelection != null && this.proteinSelection
				// .compare(obj1, obj2) == 0) || obj1.equals(obj2)) {
				// String stringKey = getUniqueKey();
				//
				// stringKey = hash.get(obj2);
				//
				// hash1.put(stringKey, obj1);
				// if (keys1 == null)
				// keys1 = new HashSet<String>();
				// keys1.add(stringKey);
				// found = true;
				// break;
				// }
				// }
				// // si no se ha encontrado aún el objeto 1 en ningún otro
				// grupo:
				// if (!found) {
				// uniques++;
				// log.debug("Element " + obj1
				// + " is unique for reference collection");
				// String newStringKey = getUniqueKey();
				// hash.put(obj1, newStringKey);
				// if (keys1 == null)
				// keys1 = new HashSet<String>();
				// keys1.add(newStringKey);
				// hash1.put(newStringKey, obj1);
				//
				// }
			}
		if (keys1 != null)
			log.debug("Reference collection now has " + keys1.size()
					+ " string keys. " + uniques + " are uniques");

	}

	private String getPeptideSequence(Object obj1) {
		if (obj1 instanceof ExtendedIdentifiedPeptide)
			return ((ExtendedIdentifiedPeptide) obj1).getSequence();
		if (obj1 instanceof PeptideOccurrence)
			return ((PeptideOccurrence) obj1).getKey();
		return obj1.toString();
	}

	private String getUniqueKey() {
		return String.valueOf(VennData.count++);
	}

	public Collection<Object> getIntersection123() {
		Set<String> intersectionKeys = getIntersection(keys1, keys2, keys3);
		List<Object> ret = getObjectsByKeys(intersectionKeys);
		return ret;
	}

	public Collection<Object> getIntersection12() {
		Set<String> intersectionKeys = getIntersection(keys1, keys2);
		List<Object> ret = getObjectsByKeys(intersectionKeys);
		return ret;
	}

	public Collection<Object> getIntersection23() {
		Set<String> intersectionKeys = getIntersection(keys2, keys3);
		List<Object> ret = getObjectsByKeys(intersectionKeys);
		return ret;
	}

	public Collection<Object> getIntersection13() {
		Set<String> intersectionKeys = getIntersection(keys1, keys3);
		List<Object> ret = getObjectsByKeys(intersectionKeys);
		return ret;
	}

	public Collection<Object> get1In2() {
		return null;
	}

	public Collection<Object> get2In1() {
		return null;
	}

	public Collection<Object> getUniqueTo1() {
		Set<String> uniqueTo1 = getUniqueToFirstSet(keys1, keys2, keys3);
		return this.getObjectsByKeys(uniqueTo1);
	}

	public Collection<Object> getUniqueTo2() {
		Set<String> uniqueTo2 = getUniqueToFirstSet(keys2, keys1, keys3);
		return this.getObjectsByKeys(uniqueTo2);
	}

	public Collection<Object> getUniqueTo3() {
		Set<String> uniqueTo3 = getUniqueToFirstSet(keys3, keys1, keys2);
		return this.getObjectsByKeys(uniqueTo3);
	}

	private Set<String> getUniqueToFirstSet(Set<String> hashToIsolate,
			Set<String> hash2, Set<String> hash3) {
		Set<String> toIsolateSet2 = new HashSet<String>();
		if (hashToIsolate != null) {
			toIsolateSet2.addAll(hashToIsolate);
			Iterator<String> toIsolateIterator = toIsolateSet2.iterator();
			if (hash2 != null) {
				while (toIsolateIterator.hasNext()) {
					String item = toIsolateIterator.next();
					if (hash2.contains(item))
						toIsolateIterator.remove();
				}
			}
			toIsolateIterator = toIsolateSet2.iterator();
			if (hash3 != null) {
				while (toIsolateIterator.hasNext()) {
					String item = toIsolateIterator.next();
					if (hash3.contains(item))
						toIsolateIterator.remove();
				}
			}

		}
		return toIsolateSet2;
	}

	public Collection<Object> getUnion123() {
		Set<String> unionKeys = getUnion(keys1, keys2, keys3);
		List<Object> ret = getObjectsByKeys(unionKeys);
		return ret;
	}

	public Collection<Object> getUnion12() {
		Set<String> unionKeys = getUnion(keys1, keys2, null);
		List<Object> ret = getObjectsByKeys(unionKeys);
		return ret;
	}

	public Collection<Object> getUnion13() {
		Set<String> unionKeys = getUnion(keys1, null, keys3);
		List<Object> ret = getObjectsByKeys(unionKeys);
		return ret;
	}

	public Collection<Object> getUnion23() {
		Set<String> unionKeys = getUnion(null, keys2, keys3);
		List<Object> ret = getObjectsByKeys(unionKeys);
		return ret;
	}

	private List<Object> getObjectsByKeys(Set<String> keys) {
		List<Object> ret = new ArrayList<Object>();

		for (String stringKey : keys) {
			if (this.hash1.containsKey(stringKey)) {
				ret.add(this.hash1.get(stringKey));
				continue;
			}
			if (this.hash2.containsKey(stringKey)) {
				ret.add(this.hash2.get(stringKey));
				continue;
			}
			if (this.hash3.containsKey(stringKey)) {
				ret.add(this.hash3.get(stringKey));
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
	private Set<String> getIntersection(Set<String> list1, Set<String> list2,
			Set<String> list3) {

		if (list1 == null || list1.isEmpty() || list2 == null
				|| list2.isEmpty() || list3 == null || list3.isEmpty()) {

			return Collections.EMPTY_SET;
		}
		HashSet<String> ret = new HashSet<String>();
		for (String key : list1) {
			int numFound = 0;

			if (list2 != null)
				if (list2.contains(key))
					numFound++;

			if (list3 != null)
				if (list3.contains(key))
					numFound++;
			if (numFound == 2)
				ret.add(key);
		}

		return ret;
	}

	private Set<String> getIntersection(Set<String> list1, Set<String> list2) {

		if (list1 == null || list1.isEmpty() || list2 == null
				|| list2.isEmpty()) {

			return Collections.EMPTY_SET;
		}
		HashSet<String> ret = new HashSet<String>();
		for (String key : list1) {
			int numFound = 0;

			if (list2 != null)
				if (list2.contains(key))
					numFound++;

			if (numFound == 1)
				ret.add(key);
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
	private Set<String> getUnion(Set<String> list1, Set<String> list2,
			Set<String> list3) {
		// Since the HashSet doesn't allow to add repeated elements, add all to
		// the set
		HashSet<String> ret = new HashSet<String>();
		if (list1 != null)
			ret.addAll(list1);
		if (list2 != null)
			ret.addAll(list2);
		if (list3 != null)
			ret.addAll(list3);

		return ret;
	}

	public int getSize1() {
		return this.hash1.size();
	}

	public int getSize2() {
		return this.hash2.size();
	}

	public int getSize3() {
		return this.hash3.size();
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
