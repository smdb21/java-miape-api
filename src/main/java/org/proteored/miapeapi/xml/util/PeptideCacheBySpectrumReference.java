package org.proteored.miapeapi.xml.util;

import java.math.BigInteger;
import java.util.Map;

import org.proteored.miapeapi.interfaces.msi.IdentifiedPeptide;

import edu.scripps.yates.utilities.cache.AbstractCache;
import gnu.trove.map.hash.THashMap;

public class PeptideCacheBySpectrumReference extends AbstractCache<IdentifiedPeptide, BigInteger> {

	@Override
	protected Map<BigInteger, IdentifiedPeptide> createMap() {
		return new THashMap<BigInteger, IdentifiedPeptide>();
	}

}
