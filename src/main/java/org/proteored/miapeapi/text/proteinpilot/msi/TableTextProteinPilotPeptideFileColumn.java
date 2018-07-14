package org.proteored.miapeapi.text.proteinpilot.msi;

public enum TableTextProteinPilotPeptideFileColumn {
	ACC("Accessions", true), //
	INDIVIDUAL_PROTEIN_SCORE("Total", true), //
	// GROUP_PROTEIN_SCORE("Unused", true), //
	DESCRIPTION("Names", false), //
	GENE_NAME("Gene Names", false), //
	GROUP_NUMBER("N", false), //
	SEQUENCE("Sequence", true), //
	MODIFICATIONS("Modifications", false), //
	DELTA_MASS("dMass", false), //
	OBS_MW("Obs MW", false), //
	OBS_MZ("Obs m/z", false), //
	THEOR_MW("Theor MW", false), //
	THEOR_MZ("Theor m/z", false), //
	THEOR_Z("Theor z", true), //
	CONF("Conf", false, true), //
	CONTRIB("Contrib", false, true), //
	SC("Sc", false, true), //
	SPECTRUM("Spectrum", true), //
	ACQ_TIME("Acq Time", false, true), //
	INTENSITY_PEPTIDE("Intensity (Peptide)", false, true), //
	INTENSITY_PRECURSOR("PrecursorIntensityAcquisition", false, true), //
	APEX_PEPTIDE("Apex Time (Peptide)", false, true), //
	ELUTION_PEAK_WIDTH("Elution Peak Width (Peptide)", false, true), //
	MS2COUNTS("MS2Counts", false, true), //
	PREC_MW("Prec MW", false), //
	PREC_MZ("Prec m/z", false);

	private final String headerName;
	private final boolean mandatory;
	private boolean isScore;

	private TableTextProteinPilotPeptideFileColumn(String headerName, boolean mandatory) {
		this.headerName = headerName;
		this.mandatory = mandatory;
		isScore = false;
	}

	private TableTextProteinPilotPeptideFileColumn(String headerName, boolean mandatory, boolean isScore) {
		this.headerName = headerName;
		this.mandatory = mandatory;
		this.isScore = isScore;
	}

	public boolean isScore() {
		return isScore;
	}

	public String getHeaderName() {
		return headerName;
	}

	public boolean isMandatory() {
		return mandatory;
	}
}
