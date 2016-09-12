package org.proteored.miapeapi.cv.ms;

import org.proteored.miapeapi.cv.Accession;
import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularySet;
import org.proteored.miapeapi.cv.ControlVocabularyTerm;

public class SoftwareName extends ControlVocabularySet {
	public static final Accession CV_SOFTWARE = new Accession("MS:1000531");
	private static final Accession XTANDEM_ACC = new Accession("MS:1001476");
	public static final Accession PROTEOMEDISCOVERER_ACC = new Accession(
			"MS:1000650");
	public static final Accession PROTEOWIZARD_ACCESSION = new Accession(
			"MS:1000615");
	public static final Accession COMPASSXPORT_ACCESSION = new Accession(
			"MS:1000717");

	/*
	 * SOFTWARE("MS:1000531", "software"),
	 * AB_SCIEX_TOF_TOF_SERIES_EXPLORER_SOFTWARE("MS:1001483",
	 * "AB SCIEX TOF/TOF Series Explorer Software"),
	 * PROTEINEXTRACTOR("MS:1001487", "ProteinExtractor"),
	 * SRM_SOFTWARE("MS:1000871", "SRM software"),
	 * SERIES_EXPLORER_SOFTWARE4000("MS:1000659",
	 * "4000 Series Explorer Software"), PROFILEANALYSIS("MS:1000728",
	 * "ProfileAnalysis"), PROTEINEER("MS:1000729", "PROTEINEER"),
	 * LIGHTSIGHT_SOFTWARE("MS:1000662", "LightSight Software"),
	 * MICROTOFCONTROL("MS:1000726", "micrOTOFcontrol"),
	 * GPS_EXPLORER("MS:1000661", "GPS Explorer"), POLYTOOLS("MS:1000727",
	 * "PolyTools"), GENOTOOLS("MS:1000724", "GenoTools"),
	 * HCTCONTROL("MS:1000725", "HCTcontrol"), MASCOT_PARSER("MS:1001478",
	 * "Mascot Parser"), FLEXIMAGING("MS:1000722", "flexImaging"),
	 * SPECTRAST("MS:1001477", "SpectraST"), GENOLINK("MS:1000723", "GENOLINK"),
	 * DPCONTROL("MS:1000720", "dpControl"), ESQUIRECONTROL("MS:1000721",
	 * "esquireControl"), OMSSA("MS:1001475", "OMSSA"), XTANDEM("MS:1001476",
	 * "xtandem"), MARKERVIEW_SOFTWARE("MS:1000665", "MarkerView Software"),
	 * MRMPILOT_SOFTWARE("MS:1000666", "MRMPilot Software"),
	 * CUSTOM_UNRELEASED_SOFTWARE_TOOL("MS:1000799",
	 * "custom unreleased software tool"), PROTEINPILOT_SOFTWARE("MS:1000663",
	 * "ProteinPilot Software"), TISSUEVIEW_SOFTWARE("MS:1000664",
	 * "TissueView Software"), PRO_ICAT("MS:1000669", "Pro ICAT"),
	 * BIOANALYST("MS:1000667", "BioAnalyst"), PRO_ID("MS:1000668", "Pro ID"),
	 * DATAANALYSIS("MS:1000719", "DataAnalysis"),
	 * MIDAS_WORKFLOW_DESIGNER("MS:1000673", "MIDAS Workflow Designer"),
	 * COMPASS_OPENACCESS("MS:1000715", "Compass OpenAccess"),
	 * CLIQUID("MS:1000672", "Cliquid?"), COMPASS_SECURITY_PACK("MS:1000716",
	 * "Compass Security Pack"), TIQAM("MS:1000923", "TIQAM"),
	 * PRO_BLAST("MS:1000671", "Pro BLAST"), COMPASSXPORT("MS:1000717",
	 * "CompassXport"), SKYLINE("MS:1000922", "Skyline"),
	 * PRO_QUANT("MS:1000670", "Pro Quant"), COMPASSXTRACT("MS:1000718",
	 * "CompassXtract"), ATAQS("MS:1000925", "ATAQS"),
	 * CLINPROTOOLS("MS:1000711", "ClinProTools"), COMPASS("MS:1000712",
	 * "Compass"), COMPASS_FOR_HCT_ESQUIRE("MS:1000713",
	 * "Compass for HCT/esquire"), COMPASS_FOR_MICROTOF("MS:1000714",
	 * "Compass for micrOTOF"), CLINPROT_ROBOT("MS:1000710", "CLINPROT robot"),
	 * XCALIBUR("MS:1000532", "Xcalibur"), BIOWORKS("MS:1000533", "Bioworks"),
	 * MASSLYNX("MS:1000534", "MassLynx"), FLEXANALYSIS("MS:1000535",
	 * "FlexAnalysis"), GREYLAG("MS:1001461", "greylag"),
	 * DATA_EXPLORER("MS:1000536", "Data Explorer"), EXPLORER4700("MS:1000537",
	 * "4700 Explorer"), MASSWOLF("MS:1000538", "massWolf"),
	 * VOYAGER_BIOSPECTROMETRY_WORKSTATION_SYSTEM("MS:1000539",
	 * "Voyager Biospectrometry Workstation System"),
	 * DATA_PROCESSING_SOFTWARE("MS:1001457", "data processing software"),
	 * ANALYSIS_SOFTWARE("MS:1001456", "analysis software"), READW("MS:1000541",
	 * "ReAdW"), ACQUISITION_SOFTWARE("MS:1001455", "acquisition software"),
	 * FLEXCONTROL("MS:1000540", "FlexControl"), MZSTAR("MS:1000542", "MzStar"),
	 * SPCONTROL("MS:1000737", "spControl"), PROTEOMICS_DISCOVERER("MS:1000650",
	 * "Proteomics Discoverer"), TARGETANALYSIS("MS:1000738", "TargetAnalysis"),
	 * WARP_LC("MS:1000739", "WARP-LC"), PROTEINEER_DP("MS:1000730",
	 * "PROTEINEER dp"), PROTEINEER_FC("MS:1000731", "PROTEINEER fc"),
	 * PROTEINEER_SPII("MS:1000732", "PROTEINEER spII"),
	 * PROTEINEER_LC("MS:1000733", "PROTEINEER-LC"), ANALYST("MS:1000551",
	 * "Analyst"), PROTEINSCAPE("MS:1000734", "ProteinScape"),
	 * PUREDISK("MS:1000735", "PureDisk"), QUANTANALYSIS("MS:1000736",
	 * "QuantAnalysis"), HYSTAR("MS:1000817", "HyStar"),
	 * PROTEOWIZARD("MS:1000615", "ProteoWizard"),
	 * MASCOT_DISTILLER("MS:1001488", "Mascot Distiller"),
	 * MASCOT_INTEGRA("MS:1001489", "Mascot Integra"),
	 * TOFCALIBRATION("MS:1000766", "TOFCalibration"),
	 * SPECTRAFILTER("MS:1000765", "SpectraFilter"), RESAMPLER("MS:1000764",
	 * "Resampler"), PEAKPICKER("MS:1000763", "PeakPicker"),
	 * NOISEFILTER("MS:1000762", "NoiseFilter"), PERCOLATOR("MS:1001490",
	 * "Percolator"), TRAPPER("MS:1000553", "Trapper"), MAPALIGNER("MS:1000760",
	 * "MapAligner"), MAPNORMALIZER("MS:1000761", "MapNormalizer"),
	 * FILECONVERTER("MS:1000756", "FileConverter"), DBIMPORTER("MS:1000755",
	 * "DBImporter"), FILEMERGER("MS:1000758", "FileMerger"),
	 * FILEFILTER("MS:1000757", "FileFilter"), TOPP_SOFTWARE("MS:1000752",
	 * "TOPP software"), DBEXPORTER("MS:1000754", "DBExporter"),
	 * BASELINEFILTER("MS:1000753", "BaselineFilter"),
	 * INTERNALCALIBRATION("MS:1000759", "InternalCalibration"),
	 * MASSHUNTER_BIOCONFIRM("MS:1000683", "MassHunter BioConfirm"),
	 * GENESPRING_MS("MS:1000684", "Genespring MS"),
	 * MASSHUNTER_QUANTITATIVE_ANALYSIS("MS:1000681",
	 * "MassHunter Quantitative Analysis"),
	 * MASSHUNTER_METABOLITE_ID("MS:1000682", "MassHunter Metabolite ID"),
	 * MASSHUNTER_QUALITATIVE_ANALYSIS("MS:1000680",
	 * "MassHunter Qualitative Analysis"), MASSHUNTER_EASY_ACCESS("MS:1000679",
	 * "MassHunter Easy Access"), MASSHUNTER_DATA_ACQUISITION("MS:1000678",
	 * "MassHunter Data Acquisition"), MULTIQUANT("MS:1000674", "MultiQuant"),
	 * MZWIFF("MS:1000591", "MzWiff"), PROTEINLYNX_GLOBAL_SERVER("MS:1000601",
	 * "ProteinLynx Global Server"), PROTEIOS("MS:1000600", "Proteios"),
	 * MASCOT("MS:1001207", "Mascot"), BRUKER_SOFTWARE("MS:1000692",
	 * "Bruker software"), CLINPROT_MICRO("MS:1000709", "CLINPROT micro"),
	 * THERMO_FINNIGAN_SOFTWARE("MS:1000693", "Thermo Finnigan software"),
	 * CLINPROT("MS:1000708", "CLINPROT"), PHENYX("MS:1001209", "Phenyx"),
	 * WATERS_SOFTWARE("MS:1000694", "Waters software"), SEQUEST("MS:1001208",
	 * "Sequest"), AB_SCIEX_SOFTWARE("MS:1000690", "AB SCIEX software"),
	 * BIOTOOLS("MS:1000707", "BioTools"),
	 * APPLIED_BIOSYSTEMS_SOFTWARE("MS:1000691", "Applied Biosystems software"),
	 * APEXCONTROL("MS:1000706", "apexControl"), AGILENT_SOFTWARE("MS:1000689",
	 * "Agilent software"), METLIN("MS:1000686", "METLIN"),
	 * MASSHUNTER_MASS_PROFILER("MS:1000685", "MassHunter Mass Profiler"),
	 * SERIES_ION_TRAP_DATA_ANALYSIS_SOFTWARE6300("MS:1000688",
	 * "6300 Series Ion Trap Data Analysis Software"),
	 * SPECTRUM_MILL_FOR_MASSHUNTER_WORKSTATION("MS:1000687",
	 * "Spectrum Mill for MassHunter Workstation"),
	 * PEPTIDE_ATTRIBUTE_CALCULATION_SOFTWARE("MS:1000873",
	 * "peptide attribute calculation software"), MARIMBA2("MS:1000872",
	 * "MaRiMba"), SSRCALC("MS:1000874", "SSRCalc"),
	 * SHIMADZU_BIOTECH_SOFTWARE("MS:1001557", "Shimadzu Biotech software"),
	 * MALDI_SOLUTIONS("MS:1001558", "MALDI Solutions"), SCAFFOLD("MS:1001561",
	 * "Scaffold"), XCMS("MS:1001582", "XCMS"), MAXQUANT("MS:1001583",
	 * "MaxQuant"), QUANTITATION_SOFTWARE_NAME("MS:1001139",
	 * "quantitation software name");
	 */
	private static SoftwareName instance;

	public static SoftwareName getInstance(ControlVocabularyManager cvManager) {
		if (instance == null)
			instance = new SoftwareName(cvManager);
		return instance;
	}

	private SoftwareName(ControlVocabularyManager cvManager) {
		super(cvManager);
		String[] parentAccessionsTMP = { "MS:1000690", "MS:1000689",
				"MS:1000691", "MS:1001949", "MS:1000692", "MS:1001798",
				"MS:1000871", "MS:1001557", "MS:1000693", "MS:1000694",
				"MS:1001455", "MS:1001456", "MS:1001457", "MS:1000873",
				"MS:1001139", "sep:00060" };
		this.parentAccessions = parentAccessionsTMP;
		String[] explicitAccessions = { "MS:1000799" }; // custom unreleased
														// software tool
		this.explicitAccessions = explicitAccessions;
		this.miapeSection = 500;
		this.setExcludeParents(true); // Exclude parents
	}

	public static ControlVocabularyTerm getXTandemTerm(
			ControlVocabularyManager cvManager) {
		return getInstance(cvManager).getCVTermByAccession(XTANDEM_ACC);
	}
}
