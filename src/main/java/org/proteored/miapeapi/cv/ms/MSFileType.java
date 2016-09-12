package org.proteored.miapeapi.cv.ms;

import org.proteored.miapeapi.cv.Accession;
import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularySet;
import org.proteored.miapeapi.cv.ControlVocabularyTerm;

public class MSFileType extends ControlVocabularySet {

	/*
	 * MASS_SPECTROMETER("MS:1000560", "mass spectrometer file format"),
	 * TEXT("MS:1001369", "text file"), WATERS_RAW("MS:1000526",
	 * "Waters raw file"), ABI_WIFF ("MS:1000562", "ABI WIFF file"),
	 * THERMO_RAW("MS:1000563", "Thermo RAW file"),
	 * AB_SCIEX_TOFTOF("MS:1001481", "AB SCIEX TOF/TOF database"),
	 * PHENYX_XML("MS:1001463", "Phenyx XML format"), MZ_ML("MS:1000584",
	 * "mzML file"), DTA("MS:1000613", "DTA file"), BRUKER_FID("MS:1000825",
	 * "Bruker FID file"), BRUKER_U2("MS:1000816", "Bruker U2 file"),
	 * BRUKER_BAF("MS:1000815", "Bruker BAF file"),
	 * PROTEINLYNX_XML("MS:1000614",
	 * "ProteinLynx Global Server mass spectrum XML file"),
	 * AGILENT_MASSHUNTER("MS:1001509", "Agilent MassHunter file"),
	 * PERSEPTIVE_PKS("MS:1001245", "PerSeptive PKS file"),
	 * BIOWORKS_SRF("MS:1000742", "Bioworks SRF file"), MASCOT_MGF("MS:1001062",
	 * "Mascot MGF file"), PROTEINSCAPE_SPECTRA("MS:1001527",
	 * "Proteinscape spectra"), SCIEX_API_III("MS:1001246",
	 * "Sciex API III file"), PARAMETER_FILE_CV("MS:1000740", "parameter file"),
	 * BRUKER_XML("MS:1001247", "Bruker XML file"), MICROMASS_PKL("MS:1000565",
	 * "Micromass PKL file"), PSI_MZDATA("MS:1000564", "PSI mzData file"),
	 * BRUKER_AGILENT_YEP("MS:1000567", "Bruker/Agilent YEP file"),
	 * ISB_MZ_XML("MS:1000566", "ISB mzXML file"),
	 * AB_SCIEX_TOFTOF_T2D("MS:1001560", "AB SCIEX TOF/TOF T2D file"),
	 * SHIMADZU_BIOTECH_DATABASE_ENTITY
	 * ("MS:1000930","Shimadzu Biotech database entity"); ;
	 */

	public static final Accession PARAMETER_FILE_CV = new Accession(
			"MS:1000740");
	public static final Accession MASCOT_MGF = new Accession("MS:1001062");
	public static final Accession DTA = new Accession("MS:1000613");
	public static final Accession PKL = new Accession("MS:1000565");

	public static final Accession MZML_ACC = new Accession("MS:1000584");
	public static final Accession THERMO_RAW_ACCESSION = new Accession(
			"MS:1000563");
	public static final Accession WATERS_RAW_ACCESSION = new Accession(
			"MS:1000526");
	public static final Accession BRUKER_AGILENT_YEP_ACCESSION = new Accession(
			"MS:1000567");

	private static MSFileType instance;

	public static MSFileType getInstance(ControlVocabularyManager cvManager) {
		if (instance == null)
			instance = new MSFileType(cvManager);
		return instance;
	}

	private MSFileType(ControlVocabularyManager cvManager) {
		super(cvManager);
		// "mass spectrometer file format"
		String[] parentAccessionsTMP = { "MS:1000560" };
		parentAccessions = parentAccessionsTMP;

		miapeSection = 207;

	}

	public boolean isPeakListFileType(String fileType,
			ControlVocabularyManager cvManager) {
		if (fileType.contains("MGF"))
			return true;
		ControlVocabularyTerm cvTermByPreferredName = MSFileType.getInstance(
				cvManager).getCVTermByPreferredName(fileType);
		if (cvTermByPreferredName != null) {
			Accession cvTermAcc = cvTermByPreferredName.getTermAccession();
			if (cvTermAcc != null) {
				if (cvTermAcc.equals(DTA) || cvTermAcc.equals(MASCOT_MGF)
						|| cvTermAcc.equals(PKL))
					return true;
			}
		}
		return false;
	}
}
