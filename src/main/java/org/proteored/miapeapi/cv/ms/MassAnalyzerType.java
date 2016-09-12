package org.proteored.miapeapi.cv.ms;

import org.proteored.miapeapi.cv.Accession;
import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularySet;

public class MassAnalyzerType extends ControlVocabularySet {
	/*
	 * MASS_ANALYZER_TYPE("MS:1000443", "mass analyzer type"),
	 * ELECTROSTATIC_ENERGY_ANALYZER("MS:1000254",
	 * "electrostatic energy analyzer"),
	 * STORED_WAVEFORM_INVERSE_FOURIER_TRANSFORM("MS:1000284",
	 * "stored waveform inverse fourier transform"), ION_TRAP("MS:1000264",
	 * "ion trap"), AXIAL_EJECTION_LINEAR_ION_TRAP("MS:1000078",
	 * "axial ejection linear ion trap"), QUADRUPOLE_ION_TRAP("MS:1000082",
	 * "quadrupole ion trap"), RADIAL_EJECTION_LINEAR_ION_TRAP("MS:1000083",
	 * "radial ejection linear ion trap"), LINEAR_ION_TRAP("MS:1000291",
	 * "linear ion trap"), CYCLOTRON("MS:1000288", "cyclotron"),
	 * FOURIER_TRANSFORM_ION_CYCLOTRON_RESONANCE_MASS_SPECTROMETER("MS:1000079",
	 * "fourier transform ion cyclotron resonance mass spectrometer"),
	 * ORBITRAP("MS:1000484", "orbitrap"), QUADRUPOLE("MS:1000081",
	 * "quadrupole"), MAGNETIC_SECTOR("MS:1000080", "magnetic sector"),
	 * TIME_OF_FLIGHT("MS:1000084", "time-of-flight"),
	 * ANALYZER_TYPE("MS:1000010", "analyzer type");
	 */

	// mass analyzer type
	public static final Accession MASS_ANALYZER_TYPE_ACCESSION = new Accession("MS:1000443");
	private static MassAnalyzerType instance;

	public static MassAnalyzerType getInstance(ControlVocabularyManager cvManager) {
		if (instance == null)
			instance = new MassAnalyzerType(cvManager);
		return instance;
	}

	private MassAnalyzerType(ControlVocabularyManager cvManager) {
		super(cvManager);
		String[] parentAccessionsTMP = { MASS_ANALYZER_TYPE_ACCESSION.toString(), "MS:1000010" };
		this.parentAccessions = parentAccessionsTMP;

		this.miapeSection = 221;

	}

}
