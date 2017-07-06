package org.proteored.miapeapi.experiment.model.datamanager;

import java.util.Map;

import org.proteored.miapeapi.experiment.model.ProteinGroup;
import org.proteored.miapeapi.experiment.model.ProteinGroupOccurrence;

import edu.scripps.yates.utilities.pi.ParIterator;
import edu.scripps.yates.utilities.pi.reductions.Reducible;
import gnu.trove.map.hash.THashMap;

public class ProteinGroupOcurrenceParallelCreator extends Thread {
	private final ParIterator<ProteinGroup> iterator;
	private final Reducible<Map<String, ProteinGroupOccurrence>> reducibleProteinGroupMap;

	public ProteinGroupOcurrenceParallelCreator(ParIterator<ProteinGroup> iterator,

			Reducible<Map<String, ProteinGroupOccurrence>> reducibleProteinGroupMap) {
		this.iterator = iterator;
		this.reducibleProteinGroupMap = reducibleProteinGroupMap;
	}

	@Override
	public void run() {

		Map<String, ProteinGroupOccurrence> proteinGroupOccurrences = new THashMap<String, ProteinGroupOccurrence>();
		reducibleProteinGroupMap.set(proteinGroupOccurrences);
		while (iterator.hasNext()) {
			try {
				final ProteinGroup proteinGroup = iterator.next();
				if (proteinGroupOccurrences.containsKey(proteinGroup.getKey())) {
					ProteinGroupOccurrence proteinGroupOccurrence = proteinGroupOccurrences.get(proteinGroup.getKey());

					proteinGroupOccurrence.addOccurrence(proteinGroup);
				} else {
					final ProteinGroupOccurrence proteinGroupOccurrence = new ProteinGroupOccurrence();
					proteinGroupOccurrence.addOccurrence(proteinGroup);
					proteinGroupOccurrences.put(proteinGroup.getKey(), proteinGroupOccurrence);
				}
			} catch (Exception e) {
				iterator.register(e);
			}
		}

	}

}
