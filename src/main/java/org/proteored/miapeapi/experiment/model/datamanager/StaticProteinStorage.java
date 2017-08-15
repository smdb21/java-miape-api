package org.proteored.miapeapi.experiment.model.datamanager;

import java.util.Map;

import org.proteored.miapeapi.experiment.model.ExtendedIdentifiedProtein;
import org.proteored.miapeapi.interfaces.msi.MiapeMSIDocument;

import gnu.trove.map.hash.THashMap;
import gnu.trove.map.hash.TIntObjectHashMap;

public class StaticProteinStorage {
	private final static Map<String, TIntObjectHashMap<ExtendedIdentifiedProtein>> staticProteinsByMIAPEID = new THashMap<String, TIntObjectHashMap<ExtendedIdentifiedProtein>>();

	public static synchronized void addProtein(MiapeMSIDocument miapeMSI, ExtendedIdentifiedProtein Protein) {
		if (!staticProteinsByMIAPEID.containsKey(miapeMSI.getName())) {
			TIntObjectHashMap<ExtendedIdentifiedProtein> ProteinMap = new TIntObjectHashMap<ExtendedIdentifiedProtein>();
			staticProteinsByMIAPEID.put(miapeMSI.getName(), ProteinMap);
		}
		final TIntObjectHashMap<ExtendedIdentifiedProtein> ProteinMap = staticProteinsByMIAPEID.get(miapeMSI.getName());
		ProteinMap.put(Protein.getId(), Protein);
	}

	public static synchronized ExtendedIdentifiedProtein getProtein(MiapeMSIDocument miapeMSI, int ProteinID) {
		if (staticProteinsByMIAPEID.containsKey(miapeMSI.getName())) {
			return staticProteinsByMIAPEID.get(miapeMSI.getName()).get(ProteinID);
		}
		return null;
	}

	public static synchronized void clear() {
		for (String miapeMSIName : staticProteinsByMIAPEID.keySet()) {
			staticProteinsByMIAPEID.get(miapeMSIName).clear();
		}
		staticProteinsByMIAPEID.clear();

	}
}
