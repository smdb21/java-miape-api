package org.proteored.miapeapi.factories.gi;

import org.proteored.miapeapi.interfaces.gi.DataReporting;

public class DataReportingImpl implements DataReporting {
	private final String name;
	private final String featureList;
	private final String featureUri;
	private final String matchesList;
	private final String matchesUri;
	private final String resultsDescription;
	private final String resultsUri;

	public DataReportingImpl(DataReportingBuilder dataReportingBuilder) {
		this.name = dataReportingBuilder.name;
		this.featureList = dataReportingBuilder.featureList;
		this.featureUri = dataReportingBuilder.featureUri;
		this.matchesList = dataReportingBuilder.matchesList;
		this.matchesUri = dataReportingBuilder.matchesUri;
		this.resultsDescription = dataReportingBuilder.resultsDescription;
		this.resultsUri = dataReportingBuilder.resultsUri;

	}



	@Override
	public String getFeatureList() {
		return featureList;
	}

	@Override
	public String getFeatureURI() {
		return featureUri;
	}

	@Override
	public String getMatchesList() {
		return matchesList;
	}

	@Override
	public String getMatchesURI() {
		return matchesUri;
	}

	@Override
	public String getName() {
		return name;
	}



	@Override
	public String getResultsDescription() {
		return resultsDescription;
	}

	@Override
	public String getResultsURI() {
		return resultsUri;
	}



}
