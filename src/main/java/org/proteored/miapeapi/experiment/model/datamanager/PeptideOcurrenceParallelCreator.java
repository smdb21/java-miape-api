package org.proteored.miapeapi.experiment.model.datamanager;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.proteored.miapeapi.experiment.model.ExtendedIdentifiedPeptide;
import org.proteored.miapeapi.experiment.model.PeptideOccurrence;

import edu.scripps.yates.pi.ParIterator;
import edu.scripps.yates.pi.reductions.Reducible;

public class PeptideOcurrenceParallelCreator extends Thread {
	private static Logger log = Logger.getLogger("log4j.logger.org.proteored");

	private final ParIterator<ExtendedIdentifiedPeptide> iterator;
	private final int numCore;
	private final boolean distinguishModPep;
	private final Reducible<HashMap<String, PeptideOccurrence>> reduciblePeptideMap;

	public PeptideOcurrenceParallelCreator(
			ParIterator<ExtendedIdentifiedPeptide> iterator, int numCore,
			boolean distinguishModPep,
			Reducible<HashMap<String, PeptideOccurrence>> reduciblePeptideMap) {
		this.iterator = iterator;
		this.numCore = numCore;
		this.distinguishModPep = distinguishModPep;
		this.reduciblePeptideMap = reduciblePeptideMap;
	}

	@Override
	public void run() {
		HashMap<String, PeptideOccurrence> peptideMap = new HashMap<String, PeptideOccurrence>();
		reduciblePeptideMap.set(peptideMap);
		log.debug("Creating peptide occurrence from thread " + numCore);
		while (iterator.hasNext()) {
			try {
				final ExtendedIdentifiedPeptide extPeptide = iterator.next();
				if (extPeptide != null) {
					String key = extPeptide.getKey(distinguishModPep);
					// peptide occurrence
					if (peptideMap.containsKey(key)) {
						peptideMap.get(key).addOccurrence(extPeptide);
					} else {
						final PeptideOccurrence newIdentOccurrence = new PeptideOccurrence(
								key);
						newIdentOccurrence.addOccurrence(extPeptide);
						peptideMap.put(key, newIdentOccurrence);
					}
				}
			} catch (Exception e) {
				iterator.register(e);
			}
		}
		log.debug(peptideMap.size()
				+ " peptide occurrences created from thread " + numCore);
	}

}
