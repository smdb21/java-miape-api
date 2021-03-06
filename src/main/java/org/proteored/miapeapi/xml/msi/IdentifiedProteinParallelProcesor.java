package org.proteored.miapeapi.xml.msi;

import java.util.Map;

import org.proteored.miapeapi.interfaces.msi.IdentifiedProtein;
import org.proteored.miapeapi.xml.msi.autogenerated.MSIIdentifiedProtein;

import edu.scripps.yates.utilities.pi.ParIterator;
import edu.scripps.yates.utilities.pi.reductions.Reducible;
import gnu.trove.map.hash.THashMap;

public class IdentifiedProteinParallelProcesor extends Thread {

	private final ParIterator<MSIIdentifiedProtein> iterator;
	private final Reducible<Map<String, IdentifiedProtein>> reducibleProteinMap;

	public IdentifiedProteinParallelProcesor(ParIterator<MSIIdentifiedProtein> iterator,
			Reducible<Map<String, IdentifiedProtein>> reducibleProteinMap) {
		this.iterator = iterator;
		this.reducibleProteinMap = reducibleProteinMap;
	}

	@Override
	public void run() {
		Map<String, IdentifiedProtein> proteinMap = new THashMap<String, IdentifiedProtein>();
		reducibleProteinMap.set(proteinMap);
		while (iterator.hasNext()) {
			try {
				final MSIIdentifiedProtein msiIdentifiedProtein = iterator.next();
				proteinMap.put(msiIdentifiedProtein.getId(), new IdentifiedProteinImpl(msiIdentifiedProtein));
			} catch (Exception e) {
				iterator.register(e);
			}
		}

	}
}
