package org.proteored.miapeapi.cv;

public class NEWTOntology {
	private static final String NEWT_ONTOLOGY_ADDRESS = "http://www.ebi.ac.uk/ontology-lookup/browse.do?ontName=NEWT";
	private static final String NEWT_ONTOLOGY_CVLABEL = "NEWT";
	private static final String NEWT_ONTOLOGY_FULLNAME = "NEWT UniProt Taxonomy Database";
	private static final String NEWT_ONTOLOGY_VERSION = "1.0";

	public static String getAddress() {
		return NEWT_ONTOLOGY_ADDRESS;
	}

	public static String getFullName() {
		return NEWT_ONTOLOGY_FULLNAME;
	}

	public static String getCVLabel() {
		return NEWT_ONTOLOGY_CVLABEL;
	}

	public static String getVersion() {
		return NEWT_ONTOLOGY_VERSION;
	}

}
