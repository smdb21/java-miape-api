package org.proteored.miapeapi.cv;

public class UnitOntology {
	private static final String UNIT_ONTOLOGY_ADDRESS = "https://raw.githubusercontent.com/bio-ontology-research-group/unit-ontology/master/unit.obo";
	private static final String UNIT_ONTOLOGY_FULLNAME = "Unit Ontology";
	private static final String UNIT_ONTOLOGY_CVLABEL = "UO";
	private static final String UNIT_ONTOLOGY_VERSION = "27:06:2013 17:08";

	public static String getAddress() {

		return UNIT_ONTOLOGY_ADDRESS;
	}

	public static String getFullName() {
		return UNIT_ONTOLOGY_FULLNAME;
	}

	public static String getCVLabel() {
		return UNIT_ONTOLOGY_CVLABEL;
	}

	public static String getVersion() {
		return UNIT_ONTOLOGY_VERSION;
	}
}
