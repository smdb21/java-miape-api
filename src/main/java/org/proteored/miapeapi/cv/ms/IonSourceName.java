package org.proteored.miapeapi.cv.ms;

import java.util.List;

import org.proteored.miapeapi.cv.Accession;
import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularySet;
import org.proteored.miapeapi.cv.ControlVocabularyTerm;

public class IonSourceName extends ControlVocabularySet {
	/*
	 * IONIZATION_TYPE("MS:1000008", "ionization type"),
	 * NEUTRALIZATION_REIONIZATION_MASS_SPECTROMETRY("MS:1000272",
	 * "neutralization reionization mass spectrometry"),
	 * FLOWING_AFTERGLOW("MS:1000255", "flowing afterglow"),
	 * PHOTOIONIZATION("MS:1000273", "photoionization"),
	 * NEGATIVE_ION_CHEMICAL_IONIZATION("MS:1000271",
	 * "Negative Ion chemical ionization"),
	 * RESONANCE_ENHANCED_MULTIPHOTON_IONIZATION("MS:1000276",
	 * "resonance enhanced multiphoton ionization"),
	 * PYROLYSIS_MASS_SPECTROMETRY("MS:1000274", "pyrolysis mass spectrometry"),
	 * MULTIPHOTON_IONIZATION("MS:1000227", "multiphoton ionization"),
	 * AUTODETACHMENT("MS:1000383", "autodetachment"),
	 * ATMOSPHERIC_PRESSURE_PHOTOIONIZATION("MS:1000382",
	 * "atmospheric pressure photoionization"),
	 * ASSOCIATIVE_IONIZATION("MS:1000381", "associative ionization"),
	 * ADIABATIC_IONIZATION("MS:1000380", "adiabatic ionization"),
	 * DESORPTION_IONIZATION_ON_SILICON("MS:1000387",
	 * "desorption/ionization on silicon"), CHEMI_IONIZATION("MS:1000386",
	 * "chemi-ionization"), DESORPTION_IONIZATION("MS:1000247",
	 * "desorption ionization"), MATRIX_ASSISTED_LASER_DESORPTION_IONIZATION(
	 * "MS:1000075", "matrix-assisted laser desorption ionization", true,
	 * false), SURFACE_ASSISTED_LASER_DESORPTION_IONIZATION( "MS:1000405",
	 * "surface-assisted laser desorption ionization", true, false),
	 * FIELD_DESORPTION("MS:1000257", "field desorption"),
	 * LASER_DESORPTION_IONIZATION("MS:1000393", "laser desorption ionization",
	 * true, false), CHARGE_EXCHANGE_IONIZATION("MS:1000385",
	 * "charge exchange ionization"), AUTOIONIZATION("MS:1000384",
	 * "autoionization"), DISSOCIATIVE_IONIZATION("MS:1000388",
	 * "dissociative ionization"), ELECTRON_IONIZATION("MS:1000389",
	 * "electron ionization"), FAST_ION_BOMBARDMENT("MS:1000446",
	 * "fast ion bombardment"), ATMOSPHERIC_PRESSURE_IONIZATION("MS:1000240",
	 * "atmospheric pressure ionization"),
	 * ATMOSPHERIC_PRESSURE_MATRIX_ASSISTED_LASER_DESORPTION_IONIZATION(
	 * "MS:1000239",
	 * "atmospheric pressure matrix-assisted laser desorption ionization"),
	 * ATMOSPHERIC_PRESSURE_CHEMICAL_IONIZATION("MS:1000070",
	 * "atmospheric pressure chemical ionization"),
	 * PENNING_IONIZATION("MS:1000399", "penning ionization"),
	 * ELECTROSPRAY_IONIZATION("MS:1000073", "electrospray ionization", false,
	 * true), NANOELECTROSPRAY("MS:1000398", "nanoelectrospray", false, true),
	 * MICROELECTROSPRAY("MS:1000397", "microelectrospray", false, true),
	 * THERMAL_IONIZATION("MS:1000407", "thermal ionization"),
	 * VERTICAL_IONIZATION("MS:1000408", "vertical ionization"),
	 * FAST_ATOM_BOMBARDMENT_IONIZATION("MS:1000074",
	 * "fast atom bombardment ionization"),
	 * SURFACE_ENHANCED_NEAT_DESORPTION("MS:1000279",
	 * "surface enhanced neat desorption"), SURFACE_IONIZATION("MS:1000406",
	 * "surface ionization", true, false),
	 * SURFACE_ENHANCED_LASER_DESORPTION_IONIZATION( "MS:1000278",
	 * "surface enhanced laser desorption ionization", true, false),
	 * SOFT_IONIZATION("MS:1000403", "soft ionization"),
	 * GLOW_DISCHARGE_IONIZATION("MS:1000259", "glow discharge ionization"),
	 * CHEMICAL_IONIZATION("MS:1000071", "chemical ionization"),
	 * SPARK_IONIZATION("MS:1000404", "spark ionization"),
	 * FIELD_IONIZATION("MS:1000258", "field ionization"),
	 * SECONDARY_IONIZATION("MS:1000402", "secondary ionization"),
	 * LIQUID_SECONDARY_IONIZATION("MS:1000395", "liquid secondary ionization"),
	 * PLASMA_DESORPTION_IONIZATION("MS:1000400",
	 * "plasma desorption ionization"), UNKNOWN("", "");
	 */

	// "MS:1000073", "electrospray ionization"
	// "MS:1000398", "nanoelectrospray"
	// "MS:1000397", "microelectrospray"
	private final static String[] esiCVs = { "MS:1000073", "MS:1000398", "MS:1000397" };

	public static final String ELECTROSPRAY_IONIZATION = "electrospray ionization";

	public static final String MATRIX_ASSISTED_LASER_DESORPTION_IONIZATION = "matrix-assisted laser desorption ionization";

	// "MS:1000075", "matrix-assisted laser desorption ionization"
	public static final Accession MALDI_ACC = new Accession("MS:1000075");

	public static final Accession IONIZATION_TYPE_ACC = new Accession("MS:1000008");
	private static IonSourceName instance;

	public static IonSourceName getInstance(ControlVocabularyManager cvManager) {
		if (instance == null)
			instance = new IonSourceName(cvManager);
		return instance;
	}

	private IonSourceName(ControlVocabularyManager cvManager) {
		super(cvManager);
		String[] parentAccessionsTMP = { "MS:1000008" }; // ionization type
		this.parentAccessions = parentAccessionsTMP;

		this.miapeSection = 218;

	}

	public static boolean isMaldiFromDescription(String description,
			ControlVocabularyManager cvManager) {
		if (description == null)
			return false;
		List<ControlVocabularyTerm> possibleValues = IonSourceName.getInstance(cvManager)
				.getPossibleValues();
		if (possibleValues != null)
			for (ControlVocabularyTerm ionSource : possibleValues) {
				if (ionSource.getPreferredName() != null)
					if (ionSource.getPreferredName().equals(description)) {
						return isMaldiFromAccession(ionSource.getTermAccession());
					}
			}
		if (description.contains("Laser") || description.contains("Matrix")) {
			return true;
		}
		return false;
	}

	public static boolean isMaldiFromAccession(Accession accession) {
		if (accession == null)
			return false;
		if (accession.equals(MALDI_ACC)) {
			return true;
		}
		return false;
	}

	public static boolean isEsiFromDescription(String description,
			ControlVocabularyManager cvManager) {
		if (description == null)
			return false;
		List<ControlVocabularyTerm> possibleValues = IonSourceName.getInstance(cvManager)
				.getPossibleValues();
		if (possibleValues != null)
			for (ControlVocabularyTerm ionSource : possibleValues) {
				if (ionSource.getPreferredName() != null)
					if (ionSource.getPreferredName().equals(description)) {
						return isEsiFromAccession(ionSource.getTermAccession());
					}
			}
		if (description.contains("Laser") || description.contains("Matrix")) {
			return true;
		}
		return false;

	}

	public static boolean isEsiFromAccession(Accession accession) {
		if (accession == null)
			return false;
		for (int i = 0; i < esiCVs.length; i++) {
			if (accession.equals(esiCVs[i])) {
				return true;
			}
		}
		return false;
	}

	public static ControlVocabularyTerm getMaldiCV(ControlVocabularyManager cvManager) {
		return getInstance(cvManager).getCVTermByAccession(IonSourceName.MALDI_ACC);
	}
}
