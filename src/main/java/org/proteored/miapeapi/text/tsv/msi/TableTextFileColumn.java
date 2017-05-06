package org.proteored.miapeapi.text.tsv.msi;

public enum TableTextFileColumn {
	ACC("ACC",
			"Column containing protein accessions. Any type of accession is valid, but some features will be only available in case of having UniprotKB accessions.<br>"
					+ "It may contain several accessions in the same cell of the table, as long as they are separated by other type of symbol among: "
					+ TableTextFileSeparator.valuesString() + " (and different than the separator of columns).",
			true), SEQ("SEQ",
					"Column containing peptide sequences. They may contain modifications strings inserted in the sequence such as <i>SEQUE(+80.02)NCE</i>.",
					true), PSMID("PSMID",
							"Column containing an identifier for the PSM, so that rows with the same PSMID will be referring to the same PSM assigned to a different protein.<br>"
									+ "If not provided, each row in the table will be considered as a new PSM.",
							false), CHARGE("Z",
									"Column containing the peptide charge state, which should be a positive integer.",
									false), MZ("MZ",
											"Column containing the precursor mass, which should be a real number.",
											false), RT("RT",
													"Column containing the retention time of the peptide, which should be a real number.",
													false);
	private final String headerName;
	private final String headerExplanation;
	private final boolean mandatory;

	private TableTextFileColumn(String headerName, String headerExplanation, boolean mandatory) {
		this.headerName = headerName;
		this.headerExplanation = headerExplanation;
		this.mandatory = mandatory;
	}

	public String getHeaderName() {
		return headerName;
	}

	public String getHeaderExplanation() {
		return headerExplanation;
	}

	public boolean isMandatory() {
		return mandatory;
	}
}
