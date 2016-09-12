package org.proteored.miapeapi.cv;

public class BrendaTissueOntology {
	private static final String TISSUE_ONTOLOGY_ADDRESS = "http://obo.cvs.sourceforge.net/obo/obo/ontology/phenotype/human_TISSUE.obo";
	private static final String TISSUE_ONTOLOGY_FULLNAME = "BRENDA tissue / enzyme source";
	private static final String TISSUE_ONTOLOGY_CVLABEL = "BTO";
	private static final String TISSUE_ONTOLOGY_VERSION = "03:12:2012 09:57";

	public static String getAddress() {

		return TISSUE_ONTOLOGY_ADDRESS;
	}

	public static String getFullName() {
		return TISSUE_ONTOLOGY_FULLNAME;
	}

	public static String getCVLabel() {
		return TISSUE_ONTOLOGY_CVLABEL;
	}

	public static String getVersion() {
		return TISSUE_ONTOLOGY_VERSION;
	}
}
