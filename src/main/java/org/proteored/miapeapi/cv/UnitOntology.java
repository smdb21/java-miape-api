package org.proteored.miapeapi.cv;

public class UnitOntology {
	private static final String UNIT_ONTOLOGY_ADDRESS = "http://ontologies.berkeleybop.org/uo.obo";
	private static final String UNIT_ONTOLOGY_FULLNAME = "Unit Ontology";
	private static final String UNIT_ONTOLOGY_CVLABEL = "UO";
	private static final String UNIT_ONTOLOGY_VERSION = "09:04:2014 13:37";

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
