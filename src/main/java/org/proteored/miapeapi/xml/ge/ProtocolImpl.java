package org.proteored.miapeapi.xml.ge;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.proteored.miapeapi.interfaces.ge.Dimension;
import org.proteored.miapeapi.interfaces.ge.InterdimensionProcess;
import org.proteored.miapeapi.interfaces.ge.Protocol;
import org.proteored.miapeapi.xml.ge.autogenerated.GEDimensionType;
import org.proteored.miapeapi.xml.ge.autogenerated.GEInterDimensionProcess;
import org.proteored.miapeapi.xml.ge.autogenerated.GEProtocol;
import org.proteored.miapeapi.xml.ge.autogenerated.GESample;

public class ProtocolImpl implements Protocol {
	private final GEProtocol protocolXML;
	private final Map<String, GESample> sampleMap;

	public ProtocolImpl(GEProtocol geProtocol, Map<String, GESample> sampleMap) {
		this.protocolXML = geProtocol;
		this.sampleMap = sampleMap;
	}

	@Override
	public String getDescription() {
		return protocolXML.getDescription();
	}

	@Override
	public Set<Dimension> getDimensions() {
		Set<Dimension> setOfDimension = new HashSet<Dimension>();
		List<GEDimensionType> firstDimensionList = protocolXML.getGEFirstDimension();
		for (GEDimensionType geDimensionItem : firstDimensionList) {
			setOfDimension.add(new FirstDimensionImpl(geDimensionItem, sampleMap));
		}
		List<GEDimensionType> secondDimensionList = protocolXML.getGESecondDimension();
		for (GEDimensionType geDimensionItem : secondDimensionList) {
			setOfDimension.add(new SecondDimensionImpl(geDimensionItem, sampleMap));
		}
		return setOfDimension;
	}

	@Override
	public Set<InterdimensionProcess> getInterdimensionProcesses() {
		Set<InterdimensionProcess> setOfInterdimension = new HashSet<InterdimensionProcess>();
		List<GEInterDimensionProcess> interdimensionList = protocolXML.getGEInterDimensionProcess();
		for (GEInterDimensionProcess geInterDimensionProcess : interdimensionList) {
			setOfInterdimension.add(new InterdimensionProcessImpl(geInterDimensionProcess));
		}
		return setOfInterdimension;
	}

	@Override
	public String getName() {
		return protocolXML.getName();
	}

}
