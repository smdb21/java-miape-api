package org.proteored.miapeapi.cv;

public class SeparationMethodsOntology {
	private static final String SEP_ONTOLOGY_ADDRESS = "http://gelml.googlecode.com/svn/trunk/CV/sep.obo";
	private static final String SEP_ONTOLOGY_CVLABEL = "SEP";
	private static final String SEP_ONTOLOGY_FULLNAME = "The CV of the PSI Working group for protein separations";
	private static final String SEP_ONTOLOGY_VERSION = "14:05:2009 11:45";

	public static String getAddress() {
		return SEP_ONTOLOGY_ADDRESS;
	}

	public static String getFullName() {
		return SEP_ONTOLOGY_FULLNAME;
	}

	public static String getCVLabel() {
		return SEP_ONTOLOGY_CVLABEL;
	}

	public static String getVersion() {
		return SEP_ONTOLOGY_VERSION;
	}

}
