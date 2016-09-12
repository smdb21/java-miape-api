package org.proteored.miapeapi.factories.ms;

import org.proteored.miapeapi.cv.AdditionalInformationName;
import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ms.EsiName;
import org.proteored.miapeapi.cv.ms.IonOpticsType;
import org.proteored.miapeapi.cv.ms.MassAnalyzerType;
import org.proteored.miapeapi.cv.ms.SpectrometerName;
import org.proteored.miapeapi.interfaces.Project;
import org.proteored.miapeapi.interfaces.User;
import org.proteored.miapeapi.interfaces.persistence.PersistenceManager;
import org.proteored.miapeapi.interfaces.xml.XmlManager;

public class MiapeMSDocumentFactory {

	private MiapeMSDocumentFactory() {
	};

	public static AcquisitionBuilder createAcquisitionBuilder(String acquisitionName) {
		return new AcquisitionBuilder(acquisitionName);
	}

	public static ActivationDissociationBuilder createActivationDissociationBuilder(String name) {
		return new ActivationDissociationBuilder(name);
	}

	public static AcquisitionBuilder createControlAnalysisSoftwareBuilder(String name) {
		return new AcquisitionBuilder(name);
	}

	/**
	 * Set the ESI name. It should be one of the possible values from
	 * {@link EsiName}
	 **/
	public static EsiBuilder createEsiBuilder(String name) {
		return new EsiBuilder(name);
	}

	/**
	 * Set the analyzer name. It should be one of the possible values from
	 * {@link MassAnalyzerType}
	 * 
	 */
	public static AnalyserBuilder createAnalyserBuilder(String name) {
		return new AnalyserBuilder(name);
	}

	/**
	 * Set the ion optic name. It should be one of the possible values from
	 * {@link IonOpticsType}
	 * 
	 */
	// public static IonOpticBuilder createIonOpticBuilder(String name) {
	// return new IonOpticBuilder(name);
	// }

	public static MaldiBuilder createMaldiBuilder(String name) {
		return new MaldiBuilder(name);
	}

	public static InstrumentConfigurationBuilder createInstrumentConfigurationBuilder(String name) {
		return new InstrumentConfigurationBuilder(name);
	}

	public static DataAnalysisBuilder createPeakListGenerationBuilder(String name) {
		return new DataAnalysisBuilder(name);
	}

	/*
	 * public static QuantitationBuilder createQuantitationBuilder(String name)
	 * { return new QuantitationBuilder(name); }
	 */

	public static MiapeMSDocumentBuilder createMiapeMSDocumentBuilder(Project project, String name,
			User owner) {
		return new MiapeMSDocumentBuilder(project, name, owner);
	}

	public static MiapeMSDocumentBuilder createMiapeMSDocumentBuilder(Project project, String name,
			User owner, PersistenceManager db) {
		return new MiapeMSDocumentBuilder(project, name, owner, db);
	}

	public static MiapeMSDocumentBuilder createMiapeMSDocumentBuilder(Project project, String name,
			User owner, PersistenceManager db, XmlManager xmlManager,
			ControlVocabularyManager cvUtil) {
		return new MiapeMSDocumentBuilder(project, name, owner, db, xmlManager, cvUtil);
	}

	/**
	 * Set the additional information. It should be one of the possible values
	 * from {@link AdditionalInformationName}
	 * 
	 */
	public static AdditionalInformationBuilder createAdditionalInformationBuilder(String addinfoname) {
		return new AdditionalInformationBuilder(addinfoname);
	}

	public static Other_IonSourceBuilder createOther_IonSourceBuilder(String otherIonSourceName) {
		return new Other_IonSourceBuilder(otherIonSourceName);
	}

	public static ResultingDataBuilder createResultingDataBuilder(String resultingDataName) {
		return new ResultingDataBuilder(resultingDataName);
	}

	/**
	 * Set the spectrometer name. It should be one of the possible values from
	 * {@link SpectrometerName}
	 **/
	public static SpectrometerBuilder createSpectrometerBuilder(String spectrometerName) {
		return new SpectrometerBuilder(spectrometerName);
	}

	public static DataAnalysisBuilder createDataAnalysisBuilder(String dataAnalysisName) {
		return new DataAnalysisBuilder(dataAnalysisName);
	}

}
