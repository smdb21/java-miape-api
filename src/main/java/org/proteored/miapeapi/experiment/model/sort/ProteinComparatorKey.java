/**
 * 
 */
package org.proteored.miapeapi.experiment.model.sort;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import gnu.trove.set.hash.THashSet;

/**
 * @author Salva
 *
 */
public class ProteinComparatorKey {
	private Set<String> accSet;
	private String uniqueAcc;
	private final ProteinGroupComparisonType comparationType;

	public ProteinComparatorKey(String acc, ProteinGroupComparisonType comparationType) {
		if (acc == null) {
			throw new IllegalArgumentException("acc cannot be null");
		}
		uniqueAcc = acc;
		accSet = null;
		this.comparationType = comparationType;

	}

	public ProteinComparatorKey(Collection<String> accs, ProteinGroupComparisonType comparationType) {
		if (accs.isEmpty()) {
			throw new IllegalArgumentException("asdf");
		}

		if (accs.size() == 1) {
			uniqueAcc = accs.iterator().next();
			accSet = null;
		} else {
			accSet = new THashSet<String>();
			accSet.addAll(accs);
			uniqueAcc = null;
		}
		this.comparationType = comparationType;

	}

	@Override
	public int hashCode() {
		return 1;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ProteinComparatorKey) {
			ProteinComparatorKey key = (ProteinComparatorKey) obj;
			switch (this.comparationType) {
			case ALL_PROTEINS:
				return this.getAccessionString().equals(key.getAccessionString());
			case BEST_PROTEIN:
			case HIGHER_EVIDENCE_PROTEIN:
				return this.getFirstElement().equals(key.getFirstElement());
			case SHARE_ONE_PROTEIN:
				if (this.uniqueAcc != null) {
					if (key.uniqueAcc != null) {
						return this.uniqueAcc.equals(key.uniqueAcc);
					} else {
						return key.accSet.contains(this.uniqueAcc);
					}
				} else {
					if (key.uniqueAcc != null) {
						return this.accSet.contains(key.uniqueAcc);
					} else {
						for (String acc : this.accSet) {
							if (key.accSet.contains(acc)) {
								return true;
							}
						}
						return false;
					}
				}

			default:
				break;
			}
		}
		return super.equals(obj);
	}

	private String getFirstElement() {
		if (uniqueAcc != null) {
			return uniqueAcc;
		} else {
			return accSet.iterator().next();
		}
	}

	/**
	 * @return
	 */
	public String getAccessionString() {
		if (uniqueAcc != null) {
			return uniqueAcc;
		}
		if (accSet != null && !accSet.isEmpty() && accSet.size() == 1) {
			return accSet.iterator().next();
		}
		List<String> list = new ArrayList<String>();
		list.addAll(accSet);
		Collections.sort(list);
		StringBuilder sb = new StringBuilder();
		for (String acc : list) {
			sb.append(acc).append(",");
		}
		return sb.toString();
	}

	@Override
	public String toString() {
		return getAccessionString();
	}

}
