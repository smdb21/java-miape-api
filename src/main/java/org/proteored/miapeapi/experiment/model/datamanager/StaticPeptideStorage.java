package org.proteored.miapeapi.experiment.model.datamanager;

import java.util.Map;

import org.proteored.miapeapi.experiment.model.ExtendedIdentifiedPeptide;
import org.proteored.miapeapi.interfaces.msi.MiapeMSIDocument;

import gnu.trove.map.hash.THashMap;
import gnu.trove.map.hash.TIntObjectHashMap;

public class StaticPeptideStorage {
	private final static Map<String, TIntObjectHashMap<ExtendedIdentifiedPeptide>> staticPeptidesByMIAPEID = new THashMap<String, TIntObjectHashMap<ExtendedIdentifiedPeptide>>();

	public static synchronized void addPeptide(MiapeMSIDocument miapeMSI, String experimentFullName,
			ExtendedIdentifiedPeptide peptide) {
		String key = getKey(miapeMSI, experimentFullName);
		if (!staticPeptidesByMIAPEID.containsKey(key)) {
			TIntObjectHashMap<ExtendedIdentifiedPeptide> peptideMap = new TIntObjectHashMap<ExtendedIdentifiedPeptide>();
			staticPeptidesByMIAPEID.put(key, peptideMap);
		}
		final TIntObjectHashMap<ExtendedIdentifiedPeptide> peptideMap = staticPeptidesByMIAPEID.get(key);
		peptideMap.put(peptide.getId(), peptide);
	}

	private static String getKey(MiapeMSIDocument miapeMSI, String experimentFullName) {
		return miapeMSI.getName() + "-" + experimentFullName;
	}

	public static synchronized ExtendedIdentifiedPeptide getPeptide(MiapeMSIDocument miapeMSI,
			String experimentFullName, int peptideID) {
		String key = getKey(miapeMSI, experimentFullName);
		if (staticPeptidesByMIAPEID.containsKey(key)) {
			return staticPeptidesByMIAPEID.get(key).get(peptideID);
		}
		return null;
	}

	public static synchronized void clear() {
		for (String key : staticPeptidesByMIAPEID.keySet()) {
			staticPeptidesByMIAPEID.get(key).clear();
		}
		staticPeptidesByMIAPEID.clear();

	}
}
