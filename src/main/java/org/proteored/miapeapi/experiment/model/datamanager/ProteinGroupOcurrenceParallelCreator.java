package org.proteored.miapeapi.experiment.model.datamanager;

import java.util.HashMap;

import org.proteored.miapeapi.experiment.model.ProteinGroup;
import org.proteored.miapeapi.experiment.model.ProteinGroupOccurrence;

import edu.scripps.yates.pi.ParIterator;
import edu.scripps.yates.pi.reductions.Reducible;

public class ProteinGroupOcurrenceParallelCreator extends Thread {
	private final ParIterator<ProteinGroup> iterator;
	private final Reducible<HashMap<String, ProteinGroupOccurrence>> reducibleProteinGroupMap;

	public ProteinGroupOcurrenceParallelCreator(
			ParIterator<ProteinGroup> iterator,

			Reducible<HashMap<String, ProteinGroupOccurrence>> reducibleProteinGroupMap) {
		this.iterator = iterator;
		this.reducibleProteinGroupMap = reducibleProteinGroupMap;
	}

	@Override
	public void run() {

		HashMap<String, ProteinGroupOccurrence> proteinGroupOccurrences = new HashMap<String, ProteinGroupOccurrence>();
		reducibleProteinGroupMap.set(proteinGroupOccurrences);
		while (iterator.hasNext()) {
			try {
				final ProteinGroup proteinGroup = iterator.next();
				if (proteinGroupOccurrences.containsKey(proteinGroup.getKey())) {
					ProteinGroupOccurrence proteinGroupOccurrence = proteinGroupOccurrences
							.get(proteinGroup.getKey());

					proteinGroupOccurrence.addOccurrence(proteinGroup);
				} else {
					final ProteinGroupOccurrence proteinGroupOccurrence = new ProteinGroupOccurrence();
					proteinGroupOccurrence.addOccurrence(proteinGroup);
					proteinGroupOccurrences.put(proteinGroup.getKey(),
							proteinGroupOccurrence);
				}
			} catch (Exception e) {
				iterator.register(e);
			}
		}

	}

}
