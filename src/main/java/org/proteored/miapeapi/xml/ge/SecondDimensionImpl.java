package org.proteored.miapeapi.xml.ge;

import java.util.Map;

import org.proteored.miapeapi.interfaces.ge.Dimension;
import org.proteored.miapeapi.xml.ge.autogenerated.GEDimensionType;
import org.proteored.miapeapi.xml.ge.autogenerated.GESample;

public class SecondDimensionImpl extends DimensionImpl implements Dimension {

	public SecondDimensionImpl(GEDimensionType geDimensionItem, Map<String, GESample> sampleMap) {
		super(geDimensionItem, sampleMap);
	}
	@Override
	public String getDimension() {
		return "2";
	}
}
