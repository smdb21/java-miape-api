package org.proteored.miapeapi.cv;

public class CellTypeOntology {
	private static final String CELL_TYPES_ONTOLOGY_ADDRESS = "http://cell-ontology.googlecode.com/svn/trunk/src/ontology/cl.obo";
	private static final String CELL_TYPES_ONTOLOGY_FULLNAME = "Cell Type";
	private static final String CELL_TYPES_ONTOLOGY_CVLABEL = "CL";
	private static final String CELL_TYPES_ONTOLOGY_VERSION = "2014-07-08";

	public static String getAddress() {

		return CELL_TYPES_ONTOLOGY_ADDRESS;
	}

	public static String getFullName() {
		return CELL_TYPES_ONTOLOGY_FULLNAME;
	}

	public static String getCVLabel() {
		return CELL_TYPES_ONTOLOGY_CVLABEL;
	}

	public static String getVersion() {
		return CELL_TYPES_ONTOLOGY_VERSION;
	}
}
