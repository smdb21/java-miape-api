package org.proteored.miapeapi.xml.gi.adapter;

import org.proteored.miapeapi.interfaces.Adapter;
import org.proteored.miapeapi.interfaces.gi.DataReporting;
import org.proteored.miapeapi.xml.gi.autogenerated.GIDataReporting;
import org.proteored.miapeapi.xml.gi.autogenerated.ObjectFactory;
import org.proteored.miapeapi.xml.gi.util.GIControlVocabularyXmlFactory;

public class DataReportingAdapter implements Adapter<GIDataReporting> {
	private final DataReporting dataReporting;
	private final ObjectFactory factory;
	private final GIControlVocabularyXmlFactory cvFactory;
	public DataReportingAdapter(DataReporting dataReporting,
			ObjectFactory factory, GIControlVocabularyXmlFactory cvFactory) {
		this.dataReporting = dataReporting;
		this.factory = factory;
		this.cvFactory = cvFactory;
	}

	@Override
	public GIDataReporting adapt() {
		GIDataReporting xmlDataReporting = factory.createGIDataReporting();
		xmlDataReporting.setFeatureList(dataReporting.getFeatureList());
		xmlDataReporting.setFeatureURI(dataReporting.getFeatureURI());
		xmlDataReporting.setMatchesList(dataReporting.getMatchesList());
		xmlDataReporting.setMatchesURI(dataReporting.getMatchesURI());
		xmlDataReporting.setName(dataReporting.getName());
		xmlDataReporting.setResultsDescription(dataReporting.getResultsDescription());
		xmlDataReporting.setResultsURI(dataReporting.getResultsURI());
		
		return xmlDataReporting;
	}

}
