package org.proteored.miapeapi.xml.util;

import java.util.Map;

import org.proteored.miapeapi.interfaces.msi.IdentifiedPeptide;

import edu.scripps.yates.utilities.cache.AbstractCache;
import gnu.trove.map.hash.THashMap;

public class PeptideCacheBySpectrumTitle extends AbstractCache<IdentifiedPeptide, String> {

	@Override
	protected Map<String, IdentifiedPeptide> createMap() {
		return new THashMap<String, IdentifiedPeptide>();
	}

}
