/**
 * 
 */
package org.proteored.miapeapi.experiment.model.sort;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Salva
 *
 */
public class ProteinComparatorKey {
	private final List<String> accList = new ArrayList<String>();
	private final ProteinGroupComparisonType comparationType;

	public ProteinComparatorKey(String acc, ProteinGroupComparisonType comparationType) {
		if (acc == null) {
			throw new IllegalArgumentException("acc cannot be null");
		}
		accList.add(acc);
		this.comparationType = comparationType;

	}

	public ProteinComparatorKey(Collection<String> accs, ProteinGroupComparisonType comparationType) {
		if (accs.isEmpty()) {
			throw new IllegalArgumentException("asdf");
		}
		accList.addAll(accs);
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
				return this.accList.get(0).equals(key.accList.get(0));
			case SHARE_ONE_PROTEIN:
				for (String acc : accList) {
					if (key.accList.contains(acc)) {
						return true;
					}
				}
				return false;
			default:
				break;
			}
		}
		return super.equals(obj);
	}

	/**
	 * @return
	 */
	public String getAccessionString() {
		if (accList != null && !accList.isEmpty() && accList.size() == 1) {
			return accList.get(0);
		}
		Collections.sort(accList);
		StringBuilder sb = new StringBuilder();
		for (String acc : accList) {
			sb.append(acc).append(",");
		}
		return sb.toString();
	}

	@Override
	public String toString() {
		return getAccessionString();
	}

}
