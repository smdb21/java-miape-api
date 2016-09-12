package org.proteored.miapeapi.cv;

public class HumanDiseaseOntology {
	private static final String DISEASE_ONTOLOGY_ADDRESS = "http://obo.cvs.sourceforge.net/obo/obo/ontology/phenotype/human_disease.obo";
	private static final String DISEASE_ONTOLOGY_FULLNAME = "Human Disease";
	private static final String DISEASE_ONTOLOGY_CVLABEL = "DOID";
	private static final String DISEASE_ONTOLOGY_VERSION = "13:06:2012 15:46";

	public static String getAddress() {

		return DISEASE_ONTOLOGY_ADDRESS;
	}

	public static String getFullName() {
		return DISEASE_ONTOLOGY_FULLNAME;
	}

	public static String getCVLabel() {
		return DISEASE_ONTOLOGY_CVLABEL;
	}

	public static String getVersion() {
		return DISEASE_ONTOLOGY_VERSION;
	}
}
