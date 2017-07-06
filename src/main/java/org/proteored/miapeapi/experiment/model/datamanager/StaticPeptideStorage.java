package org.proteored.miapeapi.experiment.model.datamanager;

import java.util.Map;

import org.proteored.miapeapi.experiment.model.ExtendedIdentifiedPeptide;
import org.proteored.miapeapi.interfaces.msi.MiapeMSIDocument;

import gnu.trove.map.hash.THashMap;
import gnu.trove.map.hash.TIntObjectHashMap;

public class StaticPeptideStorage {
	private final static Map<String, TIntObjectHashMap<ExtendedIdentifiedPeptide>> staticPeptidesByMIAPEID = new THashMap<String, TIntObjectHashMap<ExtendedIdentifiedPeptide>>();

	public static synchronized void addPeptide(MiapeMSIDocument miapeMSI, ExtendedIdentifiedPeptide peptide) {
		if (!staticPeptidesByMIAPEID.containsKey(miapeMSI.getName())) {
			TIntObjectHashMap<ExtendedIdentifiedPeptide> peptideMap = new TIntObjectHashMap<ExtendedIdentifiedPeptide>();
			staticPeptidesByMIAPEID.put(miapeMSI.getName(), peptideMap);
		}
		final TIntObjectHashMap<ExtendedIdentifiedPeptide> peptideMap = staticPeptidesByMIAPEID.get(miapeMSI.getName());
		peptideMap.put(peptide.getId(), peptide);
	}

	public static synchronized ExtendedIdentifiedPeptide getPeptide(MiapeMSIDocument miapeMSI, int peptideID) {
		if (staticPeptidesByMIAPEID.containsKey(miapeMSI.getName())) {
			return staticPeptidesByMIAPEID.get(miapeMSI.getName()).get(peptideID);
		}
		return null;
	}

	public static synchronized void clear() {
		for (String miapeMSIName : staticPeptidesByMIAPEID.keySet()) {
			staticPeptidesByMIAPEID.get(miapeMSIName).clear();
		}
		staticPeptidesByMIAPEID.clear();

	}
}
