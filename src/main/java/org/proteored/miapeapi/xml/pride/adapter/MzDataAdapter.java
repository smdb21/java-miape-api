package org.proteored.miapeapi.xml.pride.adapter;

import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.proteored.miapeapi.cv.BrendaTissueOntology;
import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.HumanDiseaseOntology;
import org.proteored.miapeapi.cv.NEWTOntology;
import org.proteored.miapeapi.cv.PRIDEOntology;
import org.proteored.miapeapi.cv.PSIMassSpectrometryOntology;
import org.proteored.miapeapi.cv.PSIModOntology;
import org.proteored.miapeapi.cv.SeparationMethodsOntology;
import org.proteored.miapeapi.cv.UNIMODOntology;
import org.proteored.miapeapi.cv.UnitOntology;
import org.proteored.miapeapi.interfaces.Adapter;
import org.proteored.miapeapi.interfaces.ms.MiapeMSDocument;
import org.proteored.miapeapi.xml.ms.merge.MiapeMSMerger;
import org.proteored.miapeapi.xml.pride.autogenerated.CvLookupType;
import org.proteored.miapeapi.xml.pride.autogenerated.ExperimentType.MzData;
import org.proteored.miapeapi.xml.pride.autogenerated.ExperimentType.MzData.Description;
import org.proteored.miapeapi.xml.pride.autogenerated.ObjectFactory;
import org.proteored.miapeapi.xml.pride.util.PrideControlVocabularyXmlFactory;

public class MzDataAdapter implements Adapter<MzData> {
	private static Logger log = Logger.getLogger("log4j.logger.org.proteored");
	private final ObjectFactory factory;
	private final ControlVocabularyManager cvManager;
	private final List<MiapeMSDocument> miapeMSs;
	private final MiapeMSDocument mergedMiapeMS;
	private final PrideControlVocabularyXmlFactory prideCvUtil;
	private final boolean addPeakList;

	/**
	 * 
	 * @param factory
	 * @param cvManager
	 * @param miapeMSs
	 * @param addPeakList
	 * @param mgfOrder
	 *            order in which the mgf (if the resulting data contains a MGF
	 *            file) should be sorted before to be readed. Possible values
	 *            comes from TMsData constants class
	 */
	public MzDataAdapter(ObjectFactory factory,
			ControlVocabularyManager cvManager, List<MiapeMSDocument> miapeMSs,
			boolean addPeakList) {
		this.factory = factory;
		this.cvManager = cvManager;
		this.miapeMSs = miapeMSs;
		this.mergedMiapeMS = MzDataAdapter.unifyMiapeMSs(miapeMSs, cvManager);

		this.prideCvUtil = new PrideControlVocabularyXmlFactory(factory,
				cvManager);
		this.addPeakList = addPeakList;

	}

	@Override
	public MzData adapt() {
		log.info("createMzData");
		MzData mzData = factory.createExperimentTypeMzData();
		// mandatory
		mzData.setAccessionNumber(String.valueOf(Calendar.getInstance()
				.getTimeInMillis()));
		mzData.setVersion(PrideControlVocabularyXmlFactory.MZDATA_VERSION);

		mzData.setDescription(createDescription());
		mzData.setSpectrumList(new SpectrumListAdapter(factory, cvManager,
				miapeMSs, addPeakList).adapt());
		addCVLookup(mzData.getCvLookup());
		return mzData;
	}

	/**
	 * Unify all miape MSs in a merged one and store the Resulting datas in a
	 * hasMap in order to get the spectra from there
	 * 
	 * @param miapeMSs
	 * @return
	 */
	public static MiapeMSDocument unifyMiapeMSs(List<MiapeMSDocument> miapeMSs,
			ControlVocabularyManager cvManager) {
		MiapeMSDocument ret = null;
		// parse MIAPE MS files
		if (miapeMSs != null && !miapeMSs.isEmpty()) {
			log.info("Parsing MIAPE MSs");
			if (miapeMSs.size() == 1) {
				return miapeMSs.get(0);
			} else {
				for (MiapeMSDocument document : miapeMSs) {
					int miapeMSId = document.getId();

					log.info("MIAPE MS " + miapeMSId + " parsed");
					log.info("Merging MIAPE MS " + miapeMSId);
					ret = MiapeMSMerger.getInstance(cvManager).merge(ret,
							document);

					log.debug("MIAPE MS merged:\n" + ret.toXml());
				}
			}
		}

		return ret;

	}

	private void addCVLookup(List<CvLookupType> cvLookup) {
		// PRIDE ONTOLOGY
		CvLookupType prideOntology = factory.createCvLookupType();
		prideOntology.setAddress(PRIDEOntology.getAddress());
		prideOntology.setCvLabel(PRIDEOntology.getCVLabel());
		prideOntology.setFullName(PRIDEOntology.getFullName());
		prideOntology.setVersion(PRIDEOntology.getVersion());
		cvLookup.add(prideOntology);

		// PSI ONTOLOGY
		CvLookupType psiOntology = factory.createCvLookupType();
		psiOntology.setAddress(PSIMassSpectrometryOntology.getAddress());
		psiOntology.setCvLabel(PSIMassSpectrometryOntology.getCVLabel());
		psiOntology.setFullName(PSIMassSpectrometryOntology.getFullName());
		psiOntology.setVersion(PSIMassSpectrometryOntology.getVersion());
		cvLookup.add(psiOntology);

		// SEP ONTOLOGY
		CvLookupType sepOntology = factory.createCvLookupType();
		sepOntology.setAddress(SeparationMethodsOntology.getAddress());
		sepOntology.setCvLabel(SeparationMethodsOntology.getCVLabel());
		sepOntology.setFullName(SeparationMethodsOntology.getFullName());
		sepOntology.setVersion(SeparationMethodsOntology.getVersion());
		cvLookup.add(sepOntology);

		// UNIMOD ONTOLOGY
		CvLookupType unimodOntology = factory.createCvLookupType();
		unimodOntology.setAddress(UNIMODOntology.getAddress());
		unimodOntology.setCvLabel(UNIMODOntology.getCVLabel());
		unimodOntology.setFullName(UNIMODOntology.getFullName());
		unimodOntology.setVersion(UNIMODOntology.getVersion());
		cvLookup.add(unimodOntology);

		// PSIMOD ONTOLOGY
		CvLookupType psiModOntology = factory.createCvLookupType();
		psiModOntology.setAddress(PSIModOntology.getAddress());
		psiModOntology.setCvLabel(PSIModOntology.getCVLabel());
		psiModOntology.setFullName(PSIModOntology.getFullName());
		psiModOntology.setVersion(PSIModOntology.getVersion());
		cvLookup.add(psiModOntology);

		// UNIT ONTOLOGY
		CvLookupType unitOntology = factory.createCvLookupType();
		unitOntology.setAddress(UnitOntology.getAddress());
		unitOntology.setCvLabel(UnitOntology.getCVLabel());
		unitOntology.setFullName(UnitOntology.getFullName());
		unitOntology.setVersion(UnitOntology.getVersion());
		cvLookup.add(unitOntology);

		// Human disease ONTOLOGY
		CvLookupType humanDiseaseOntology = factory.createCvLookupType();
		humanDiseaseOntology.setAddress(HumanDiseaseOntology.getAddress());
		humanDiseaseOntology.setCvLabel(HumanDiseaseOntology.getCVLabel());
		humanDiseaseOntology.setFullName(HumanDiseaseOntology.getFullName());
		humanDiseaseOntology.setVersion(HumanDiseaseOntology.getVersion());
		cvLookup.add(humanDiseaseOntology);

		// Brenda Tissue ONTOLOGY
		CvLookupType brendaTissueOntology = factory.createCvLookupType();
		brendaTissueOntology.setAddress(BrendaTissueOntology.getAddress());
		brendaTissueOntology.setCvLabel(BrendaTissueOntology.getCVLabel());
		brendaTissueOntology.setFullName(BrendaTissueOntology.getFullName());
		brendaTissueOntology.setVersion(BrendaTissueOntology.getVersion());
		cvLookup.add(brendaTissueOntology);

		// NEWT ONTOLOGY
		CvLookupType newtOntology = factory.createCvLookupType();
		newtOntology.setAddress(NEWTOntology.getAddress());
		newtOntology.setCvLabel(NEWTOntology.getCVLabel());
		newtOntology.setFullName(NEWTOntology.getFullName());
		newtOntology.setVersion(NEWTOntology.getVersion());
		cvLookup.add(newtOntology);

	}

	private Description createDescription() {
		log.info("createDescription");
		Description description = factory
				.createExperimentTypeMzDataDescription();
		description
				.setAdmin(new AdminAdapter(factory, cvManager, mergedMiapeMS)
						.adapt());
		description.setDataProcessing(new DataProcessingAdapter(factory,
				cvManager, mergedMiapeMS).adapt());
		description.setInstrument(new InstrumentDescriptionAdapter(factory,
				cvManager, mergedMiapeMS).adapt());
		return description;
	}

}
