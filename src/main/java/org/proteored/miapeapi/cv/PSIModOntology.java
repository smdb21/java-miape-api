package org.proteored.miapeapi.cv;

public class PSIModOntology {
	private static final String PSI_MOD_ONTOLOGY_ADDRESS = "http://psidev.cvs.sourceforge.net/psidev/psi/mod/data/PSI-MOD.obo";
	private static final String PSI_MOD_ONTOLOGY_FULLNAME = "Protein modifications Ontology";
	private static final String PSI_MOD_ONTOLOGY_CVLABEL = "MOD";
	private static final String PSI_MOD_ONTOLOGY_VERSION = "1.010.7";

	public static String getAddress() {

		return PSI_MOD_ONTOLOGY_ADDRESS;
	}

	public static String getFullName() {
		return PSI_MOD_ONTOLOGY_FULLNAME;
	}

	public static String getCVLabel() {
		return PSI_MOD_ONTOLOGY_CVLABEL;
	}

	public static String getVersion() {
		return PSI_MOD_ONTOLOGY_VERSION;
	}

}
