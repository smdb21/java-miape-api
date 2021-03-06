package org.proteored.miapeapi.xml.ge.adapter;

import org.proteored.miapeapi.interfaces.Adapter;
import org.proteored.miapeapi.interfaces.ge.Lane;
import org.proteored.miapeapi.xml.ge.autogenerated.GELane;
import org.proteored.miapeapi.xml.ge.autogenerated.ObjectFactory;
import org.proteored.miapeapi.xml.ge.util.GEControlVocabularyXmlFactory;
import org.proteored.miapeapi.xml.util.MiapeXmlUtil;

public class LaneAdapter implements Adapter<GELane>{
	private final ObjectFactory factory;
	private final Lane lane;

	public LaneAdapter(Lane lane2, ObjectFactory factory2,
			GEControlVocabularyXmlFactory cvFactory2) {
		this.factory = factory2;
		this.lane = lane2;
	}

	@Override
	public GELane adapt() {
		GELane laneXML = factory.createGELane();
		laneXML.setId(MiapeXmlUtil.IdentifierPrefixes.LANE.getPrefix() + lane.getId());

		laneXML.setName(lane.getName());
		laneXML.setLaneNumber(lane.getLaneNumber());
		laneXML.setDescription(lane.getDescription());
		if (lane.getReferencedSample() != null)
			laneXML.setSampleRef(MiapeXmlUtil.IdentifierPrefixes.SAMPLE.getPrefix() + lane.getReferencedSample().getId());

		return laneXML;
	}

}
