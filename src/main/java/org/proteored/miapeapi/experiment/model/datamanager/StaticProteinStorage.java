package org.proteored.miapeapi.experiment.model.datamanager;

import java.util.Map;

import org.proteored.miapeapi.experiment.model.ExtendedIdentifiedProtein;

import gnu.trove.map.hash.THashMap;
import gnu.trove.map.hash.TIntObjectHashMap;

public class StaticProteinStorage {
	private final static Map<String, TIntObjectHashMap<ExtendedIdentifiedProtein>> staticProteinsByMIAPEID = new THashMap<String, TIntObjectHashMap<ExtendedIdentifiedProtein>>();

	public static synchronized void addProtein(String miapeMSIName, String experimentFullName,
			ExtendedIdentifiedProtein protein) {
		String key = getKey(miapeMSIName, experimentFullName);
		if (!staticProteinsByMIAPEID.containsKey(key)) {
			TIntObjectHashMap<ExtendedIdentifiedProtein> proteinMap = new TIntObjectHashMap<ExtendedIdentifiedProtein>();
			staticProteinsByMIAPEID.put(key, proteinMap);
		}
		final TIntObjectHashMap<ExtendedIdentifiedProtein> proteinMap = staticProteinsByMIAPEID.get(key);
		proteinMap.put(protein.getId(), protein);
	}

	public static synchronized ExtendedIdentifiedProtein getProtein(String miapeMSIName, String experimentFullName,
			int proteinID) {
		String key = getKey(miapeMSIName, experimentFullName);
		if (staticProteinsByMIAPEID.containsKey(key)) {
			return staticProteinsByMIAPEID.get(key).get(proteinID);
		}
		return null;
	}

	private static String getKey(String miapeMSIName, String experimentFullName) {
		return miapeMSIName + "-" + experimentFullName;
	}

	public static synchronized void clear() {
		for (String miapeMSIName : staticProteinsByMIAPEID.keySet()) {
			staticProteinsByMIAPEID.get(miapeMSIName).clear();
		}
		staticProteinsByMIAPEID.clear();

	}
}
