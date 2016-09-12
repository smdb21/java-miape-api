package org.proteored.miapeapi.cv;

public class PRIDEOntology {
	private static final String PRIDE_ONTOLOGY_ADDRESS = "http://www.ebi.ac.uk/pride";
	private static final String PRIDE_ONTOLOGY_CVLABEL = "PRIDE";
	private static final String PRIDE_ONTOLOGY_FULLNAME = "The PRIDE Ontology";
	private static final String PRIDE_ONTOLOGY_VERSION = "1.0";

	public static String getAddress() {
		return PRIDE_ONTOLOGY_ADDRESS;
	}


	public static String getFullName() {
		return PRIDE_ONTOLOGY_FULLNAME;
	}


	public static String getCVLabel() {
		return PRIDE_ONTOLOGY_CVLABEL;
	}


	public static String getVersion() {
		return PRIDE_ONTOLOGY_VERSION;
	}

}
