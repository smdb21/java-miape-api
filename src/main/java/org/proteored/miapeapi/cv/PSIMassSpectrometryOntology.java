package org.proteored.miapeapi.cv;

public class PSIMassSpectrometryOntology {
	private static final String PSI_ONTOLOGY_ADDRESS = "https://raw.githubusercontent.com/HUPO-PSI/psi-ms-CV/master/psi-ms.obo";
	private static final String PSI_ONTOLOGY_FULLNAME = "Proteomics Standards Initiative Mass Spectrometry Ontology";
	private static final String PSI_ONTOLOGY_CVLABEL = "MS";
	private static final String PSI_ONTOLOGY_VERSION = "4.0.4";

	public static String getAddress() {

		return PSI_ONTOLOGY_ADDRESS;
	}

	public static String getFullName() {
		return PSI_ONTOLOGY_FULLNAME;
	}

	public static String getCVLabel() {
		return PSI_ONTOLOGY_CVLABEL;
	}

	public static String getVersion() {
		return PSI_ONTOLOGY_VERSION;
	}

}
