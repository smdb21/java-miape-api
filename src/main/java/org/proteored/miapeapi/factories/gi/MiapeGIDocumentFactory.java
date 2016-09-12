package org.proteored.miapeapi.factories.gi;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.gi.ImageAnalysisSoftwareName;
import org.proteored.miapeapi.interfaces.Project;
import org.proteored.miapeapi.interfaces.User;
import org.proteored.miapeapi.interfaces.persistence.PersistenceManager;
import org.proteored.miapeapi.interfaces.xml.XmlManager;

public class MiapeGIDocumentFactory {
	private MiapeGIDocumentFactory() {
	};

	public static MiapeGIDocumentBuilder createMiapeGIDocumentBuilder(Project project, String name,
			User user) {
		return new MiapeGIDocumentBuilder(project, name, user);
	}

	public static MiapeGIDocumentBuilder createMiapeGIDocumentBuilder(Project project, String name,
			User user, PersistenceManager db) {
		return new MiapeGIDocumentBuilder(project, name, user, db);
	}

	public static MiapeGIDocumentBuilder createMiapeGIDocumentBuilder(Project project, String name,
			User user, PersistenceManager db, XmlManager xmlManager, ControlVocabularyManager cvUtil) {
		return new MiapeGIDocumentBuilder(project, name, user, db, xmlManager, cvUtil);
	}

	public static DataReportingBuilder createDataReportingBuilder(String name) {
		return new DataReportingBuilder(name);
	}

	public static ImageProcessingBuilder createAnalysisProcessingBuilder(String name) {
		return new ImageProcessingBuilder(name);
	}

	public static ImageProcessingStepBuilder createAnalysisProcessingStepBuilder(String name) {
		return new ImageProcessingStepBuilder(name);
	}

	public static AnalysisDesignBuilder createAnalysisDesignBuilder(String name) {
		return new AnalysisDesignBuilder(name);
	}

	public static DataAnalysisBuilder createDataAnalysisBuilder(String name) {
		return new DataAnalysisBuilder(name);
	}

	public static ImagePreparationStepBuilder createImagePreparationStepBuilder(String name) {
		return new ImagePreparationStepBuilder(name);
	}

	public static DataExtractionBuilder createDataExtractionBuilder(String name) {
		return new DataExtractionBuilder(name);
	}

	public static FeatureDetectionBuilder createFeatureDetectionBuilder(String name) {
		return new FeatureDetectionBuilder(name);
	}

	public static MatchingBuilder createMatchingBuilder(String name) {
		return new MatchingBuilder(name);
	}

	public static FeatureQuantitationBuilder createFeatureQuantitationBuilder(String name) {
		return new FeatureQuantitationBuilder(name);
	}

	/**
	 * Set the additional information. It should be one of the possible values
	 * from {@link ImageAnalysisSoftwareName}
	 */
	public static AdditionalInformationBuilder createAdditionalInformationBuilder(String addinfoname) {
		return new AdditionalInformationBuilder(addinfoname);
	}

	public static ImageGelInformaticsBuilder createImageGelInformaticsBuilder(String name) {
		return new ImageGelInformaticsBuilder(name);
	}

	/**
	 * Set the name of the image analysis software. It should be one of the
	 * possible values from {@link ImageAnalysisSoftwareName}
	 */
	public static ImageAnalysisSoftwareBuilder createImageAnalysisSoftwareBuilder(
			String imageAnalysisSoftwareName) {
		return new ImageAnalysisSoftwareBuilder(imageAnalysisSoftwareName);
	}

	public static ImageProcessingBuilder createImageProcessingBuilder(
			String imageProcessingBuilderName) {
		return new ImageProcessingBuilder(imageProcessingBuilderName);
	}
}
