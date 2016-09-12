package org.proteored.miapeapi.xml.ms.merge;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.factories.ms.MiapeMSDocumentBuilder;
import org.proteored.miapeapi.factories.ms.MiapeMSDocumentFactory;
import org.proteored.miapeapi.interfaces.MiapeDate;
import org.proteored.miapeapi.interfaces.Project;
import org.proteored.miapeapi.interfaces.User;
import org.proteored.miapeapi.interfaces.ms.Acquisition;
import org.proteored.miapeapi.interfaces.ms.DataAnalysis;
import org.proteored.miapeapi.interfaces.ms.InstrumentConfiguration;
import org.proteored.miapeapi.interfaces.ms.MSAdditionalInformation;
import org.proteored.miapeapi.interfaces.ms.MSContact;
import org.proteored.miapeapi.interfaces.ms.MiapeMSDocument;
import org.proteored.miapeapi.interfaces.ms.ResultingData;
import org.proteored.miapeapi.interfaces.ms.Spectrometer;
import org.proteored.miapeapi.interfaces.persistence.PersistenceManager;
import org.proteored.miapeapi.xml.merge.MergerUtil;
import org.proteored.miapeapi.xml.merge.MiapeMerger;
import org.proteored.miapeapi.xml.miapeproject.merge.MiapeProjectMerger;

public class MiapeMSMerger implements MiapeMerger<MiapeMSDocument> {
	private static MiapeMSMerger instance;
	private static Logger log = Logger.getLogger("log4j.logger.org.proteored");
	private static ControlVocabularyManager cvManager = null;
	private static PersistenceManager dbManager = null;
	private static String userName;
	private static String password;

	public static MiapeMSMerger getInstance() {
		if (instance == null)
			instance = new MiapeMSMerger();
		return instance;
	}

	public static MiapeMSMerger getInstance(ControlVocabularyManager cvManager) {
		if (instance == null)
			instance = new MiapeMSMerger();
		MiapeMSMerger.cvManager = cvManager;
		return instance;
	}

	public static MiapeMSMerger getInstance(ControlVocabularyManager cvManager,
			PersistenceManager db) {
		if (instance == null)
			instance = new MiapeMSMerger();
		MiapeMSMerger.cvManager = cvManager;
		MiapeMSMerger.dbManager = db;
		return instance;
	}

	public static MiapeMSMerger getInstance(ControlVocabularyManager cvManager2,
			PersistenceManager db, String userName, String password) {
		if (instance == null)
			instance = new MiapeMSMerger();
		MiapeMSMerger.cvManager = cvManager;
		MiapeMSMerger.dbManager = db;
		MiapeMSMerger.userName = userName;
		MiapeMSMerger.password = password;
		return instance;
	}

	@Override
	public MiapeMSDocument merge(MiapeMSDocument miapeOriginal, MiapeMSDocument miapeMetadata) {

		Project projectOriginal = null;
		if (miapeOriginal != null)
			projectOriginal = miapeOriginal.getProject();
		Project projectMetadata = null;
		if (miapeMetadata != null)
			projectMetadata = miapeMetadata.getProject();
		Project project = MiapeProjectMerger.getInstance().merge(projectOriginal, projectMetadata);

		String miapeName = null;

		Object ret;
		ret = MergerUtil.getNonNullValue(miapeOriginal, miapeMetadata, "getName");
		if (ret != null)
			miapeName = (String) ret;

		User owner = null;
		ret = MergerUtil.getNonNullValue(miapeOriginal, miapeMetadata, "getOwner");
		if (ret != null)
			owner = (User) ret;
		MiapeDate date = null;
		ret = MergerUtil.getNonNullValue(miapeOriginal, miapeMetadata, "getDate");
		if (ret != null)
			date = (MiapeDate) ret;
		Date modificationDate = null;
		ret = MergerUtil.getNonNullValue(miapeOriginal, miapeMetadata, "getModificationDate");
		if (ret != null)
			modificationDate = (Date) ret;
		MSContact contact = null;

		ret = MergerUtil.getNonNullValue(miapeOriginal, miapeMetadata, "getContact");
		if (ret != null)
			contact = (MSContact) ret;
		String prideURL = null;
		ret = MergerUtil.getNonNullValue(miapeOriginal, miapeMetadata, "getAttachedFileLocation");
		if (ret != null)
			prideURL = (String) ret;

		List<InstrumentConfiguration> instrumentConfigurationsOriginal = null;
		if (miapeOriginal != null)
			instrumentConfigurationsOriginal = miapeOriginal.getInstrumentConfigurations();
		List<InstrumentConfiguration> instrumentConfigurationsMetadata = null;
		if (miapeMetadata != null)
			instrumentConfigurationsMetadata = miapeMetadata.getInstrumentConfigurations();
		List<InstrumentConfiguration> instrumentConfigurations = MiapeMSInstrumentConfigurationMerger
				.getInstance().merge(instrumentConfigurationsOriginal,
						instrumentConfigurationsMetadata);

		Set<Spectrometer> spectrometersOriginal = null;
		if (miapeOriginal != null)
			spectrometersOriginal = miapeOriginal.getSpectrometers();
		Set<Spectrometer> spectrometersMetadata = null;
		if (miapeMetadata != null)
			spectrometersMetadata = miapeMetadata.getSpectrometers();
		Set<Spectrometer> spectrometers = MiapeMSSpectrometersMerger.getInstance().merge(
				spectrometersOriginal, spectrometersMetadata);

		Set<DataAnalysis> dataAnalysisOriginal = null;
		if (miapeOriginal != null)
			dataAnalysisOriginal = miapeOriginal.getDataAnalysis();
		Set<DataAnalysis> dataAnalysisMetadata = null;
		if (miapeMetadata != null)
			dataAnalysisMetadata = miapeMetadata.getDataAnalysis();
		Set<DataAnalysis> dataAnalysis = MiapeMSDataAnalysisMerger.getInstance().merge(
				dataAnalysisOriginal, dataAnalysisMetadata);

		Set<Acquisition> acquisitionsOriginal = null;
		if (miapeOriginal != null)
			acquisitionsOriginal = miapeOriginal.getAcquisitions();
		Set<Acquisition> acquisitionsMetadata = null;
		if (miapeMetadata != null)
			acquisitionsMetadata = miapeMetadata.getAcquisitions();
		Set<Acquisition> acquisitions = MiapeMSAcquisitionsMerger.getInstance().merge(
				acquisitionsOriginal, acquisitionsMetadata);

		String version = null;
		ret = MergerUtil.getNonNullValue(miapeOriginal, miapeMetadata, "getVersion");
		if (ret != null)
			version = (String) ret;

		List<MSAdditionalInformation> additionalInformationsOriginal = null;
		if (miapeOriginal != null)
			additionalInformationsOriginal = miapeOriginal.getAdditionalInformations();
		List<MSAdditionalInformation> additionalInformationsMetadata = null;
		if (miapeMetadata != null)
			additionalInformationsMetadata = miapeMetadata.getAdditionalInformations();
		List<MSAdditionalInformation> additionalInformations = MiapeMSAdditionalInformationsMerger
				.getInstance()
				.merge(additionalInformationsOriginal, additionalInformationsMetadata);

		List<ResultingData> resultingDatasOriginal = null;
		if (miapeOriginal != null)
			resultingDatasOriginal = miapeOriginal.getResultingDatas();
		List<ResultingData> resultingDatasMetadata = null;
		if (miapeMetadata != null)
			resultingDatasMetadata = miapeMetadata.getResultingDatas();
		List<ResultingData> resultingDatas = MiapeMSResultingDataMerger.getInstance().merge(
				resultingDatasOriginal, resultingDatasMetadata);

		MiapeMSDocumentBuilder miapeMSBuilder = (MiapeMSDocumentBuilder) MiapeMSDocumentFactory
				.createMiapeMSDocumentBuilder(project, miapeName, owner).contact(contact)
				.instrumentConfigurations(instrumentConfigurations).spectrometers(spectrometers)
				.dataAnalyses(dataAnalysis).acquisitions(acquisitions)
				.resultingDatas(resultingDatas).additionalInformations(additionalInformations)
				.version(version).template(false).prideUrl(prideURL)
				.modificationDate(modificationDate).date(date);
		if (cvManager != null)
			miapeMSBuilder = (MiapeMSDocumentBuilder) miapeMSBuilder.cvManager(cvManager);
		if (dbManager != null)
			miapeMSBuilder = (MiapeMSDocumentBuilder) miapeMSBuilder.dbManager(dbManager);

		return miapeMSBuilder.build();
	}
}
