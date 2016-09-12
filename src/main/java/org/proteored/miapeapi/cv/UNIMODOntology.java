package org.proteored.miapeapi.cv;

public class UNIMODOntology {
	private static final String UNIMOD_ONTOLOGY_ADDRESS = "http://www.unimod.org/obo/unimod.obo";
	private static final String UNIMOD_ONTOLOGY_FULLNAME = "UNIMOD";
	private static final String UNIMOD_ONTOLOGY_CVLABEL = "UNIMOD";
	private static final String UNIMOD_ONTOLOGY_VERSION = "2016:02:01 14:24";

	public static String getAddress() {

		return UNIMOD_ONTOLOGY_ADDRESS;
	}

	public static String getFullName() {
		return UNIMOD_ONTOLOGY_FULLNAME;
	}

	public static String getCVLabel() {
		return UNIMOD_ONTOLOGY_CVLABEL;
	}

	public static String getVersion() {
		return UNIMOD_ONTOLOGY_VERSION;
	}
}
