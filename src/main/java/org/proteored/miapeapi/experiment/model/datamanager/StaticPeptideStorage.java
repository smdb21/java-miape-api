package org.proteored.miapeapi.experiment.model.datamanager;

import java.util.HashMap;
import java.util.Map;

import org.proteored.miapeapi.experiment.model.ExtendedIdentifiedPeptide;
import org.proteored.miapeapi.interfaces.msi.MiapeMSIDocument;

public class StaticPeptideStorage {
	private final static Map<String, Map<Integer, ExtendedIdentifiedPeptide>> staticPeptidesByMIAPEID = new HashMap<String, Map<Integer, ExtendedIdentifiedPeptide>>();

	public static synchronized void addPeptide(MiapeMSIDocument miapeMSI, ExtendedIdentifiedPeptide peptide) {
		if (!staticPeptidesByMIAPEID.containsKey(miapeMSI.getName())) {
			Map<Integer, ExtendedIdentifiedPeptide> peptideMap = new HashMap<Integer, ExtendedIdentifiedPeptide>();
			staticPeptidesByMIAPEID.put(miapeMSI.getName(), peptideMap);
		}
		final Map<Integer, ExtendedIdentifiedPeptide> peptideMap = staticPeptidesByMIAPEID.get(miapeMSI.getName());
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
