package org.proteored.miapeapi.factories.gi;

import org.proteored.miapeapi.interfaces.gi.DataReporting;

public class DataReportingBuilder {
	final String name;
	String featureList;
	String featureUri;
	String matchesList;
	String matchesUri;
	String resultsDescription;
	String resultsUri;

	DataReportingBuilder(String name) {
		this.name = name;
	}

	public DataReportingBuilder featureList(String value) {
		featureList = value;
		return this;
	}

	public DataReportingBuilder featureUri(String value) {
		featureUri = value;
		return this;
	}

	public DataReportingBuilder matchesList(String value) {
		matchesList = value;
		return this;
	}

	public DataReportingBuilder matchesUri(String value) {
		matchesUri = value;
		return this;
	}

	public DataReportingBuilder resultsDescription(String value) {
		resultsDescription = value;
		return this;
	}

	public DataReportingBuilder resultsUri(String value) {
		resultsUri = value;
		return this;
	}

	public DataReporting build() {
		return new DataReportingImpl(this);
	}
}