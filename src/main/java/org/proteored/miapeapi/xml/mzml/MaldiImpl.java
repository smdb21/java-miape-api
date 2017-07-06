package org.proteored.miapeapi.xml.mzml;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.proteored.miapeapi.cv.Accession;
import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularyTerm;
import org.proteored.miapeapi.cv.ms.IonOpticsType;
import org.proteored.miapeapi.cv.ms.IonSourceName;
import org.proteored.miapeapi.cv.ms.LaserAttribute;
import org.proteored.miapeapi.cv.ms.LaserType;
import org.proteored.miapeapi.cv.ms.MaldiDissociationMethod;
import org.proteored.miapeapi.interfaces.MatchMode;
import org.proteored.miapeapi.interfaces.ms.Maldi;
import org.proteored.miapeapi.xml.mzml.util.MzMLControlVocabularyXmlFactory;

import gnu.trove.map.hash.THashMap;
import uk.ac.ebi.jmzml.model.mzml.CVParam;
import uk.ac.ebi.jmzml.model.mzml.ParamGroup;
import uk.ac.ebi.jmzml.model.mzml.ReferenceableParamGroup;
import uk.ac.ebi.jmzml.model.mzml.ReferenceableParamGroupList;
import uk.ac.ebi.jmzml.model.mzml.ReferenceableParamGroupRef;
import uk.ac.ebi.jmzml.model.mzml.SourceComponent;
import uk.ac.ebi.jmzml.model.mzml.UserParam;

public class MaldiImpl implements Maldi {
	private static Logger log = Logger.getLogger("log4j.logger.org.proteored");

	private final Accession WAVELENGTH_CV = new Accession("MS:1000843");
	private String name = null;
	private String laserType = null;
	private String wavelength = null;
	private String laser_parameters = null;
	private String plateType = null;
	private final String[] PLATE_TYPE_TEXT_LIST = { "plate" };
	private String matrixComposition = null;
	private final String[] MATRIX_TEXT_LIST = { "matrixComposition", "composition" };
	private final Accession MATRIX_SOLUTION_CV = new Accession("MS:1000834");
	private final Accession MATRIX_SOLUTION_CONCENTRATION_CV = new Accession("MS:1000835");
	private String dissociation = null;
	private final String[] DISSOCIATION_TEXT_LIST = { "laser-induced decomposition", "in-source dissociation" };
	private final String[] DISSOCIATION_SUMMARY_TEXT_LIST = { "reduction" };
	private String dissociationSummary = null;
	private static final Accession POST_SOURCE_DECAY_CV = new Accession("MS:1000135");
	private static final String[] EXTRACTION_DELAY_SUMMARY_TEXT_LIST = { "delay" };
	private final String[] NAME_TEXT_LIST = { "name" };
	private String extraction = null;
	private final ControlVocabularyManager cvManager;

	public MaldiImpl(SourceComponent sourceComponent, ReferenceableParamGroupList referenceableParamGroupList,
			ControlVocabularyManager cvManager) {
		log.info("Constructor of maldi");
		this.cvManager = cvManager;
		Map<String, String> dicc = new THashMap<String, String>();
		if (sourceComponent != null) {
			// Create a paramGroup
			ParamGroup paramGroup = MzMLControlVocabularyXmlFactory.createParamGroup(sourceComponent.getCvParam(),
					sourceComponent.getUserParam(), sourceComponent.getReferenceableParamGroupRef());

			if (this.isMaldiFromParamGroup(paramGroup, referenceableParamGroupList)) {
				// search the name
				this.name = MzMLControlVocabularyXmlFactory.getValueFromParamGroup(paramGroup,
						referenceableParamGroupList, IonSourceName.getInstance(cvManager));
				if (name == null)
					this.name = MzMLControlVocabularyXmlFactory.getValueFromParamGroupByName(paramGroup,
							referenceableParamGroupList, NAME_TEXT_LIST, MatchMode.ANYWHERE);
				if (name == null)
					this.name = "MALDI source";
				else
					dicc.put(name, name);

				// laser type
				this.laserType = MzMLControlVocabularyXmlFactory.getValueFromParamGroup(paramGroup,
						referenceableParamGroupList, LaserType.getInstance(cvManager));
				dicc.put(laserType, laserType);

				// wavelength
				this.wavelength = MzMLControlVocabularyXmlFactory.getValueFromParamGroupByAccession(paramGroup,
						referenceableParamGroupList, WAVELENGTH_CV);
				dicc.put(wavelength, wavelength);

				// plate type
				// Add parsing when new CV is available
				this.plateType = MzMLControlVocabularyXmlFactory.getValueFromParamGroupByName(paramGroup,
						referenceableParamGroupList, PLATE_TYPE_TEXT_LIST, MatchMode.ANYWHERE);
				dicc.put(plateType, plateType);

				// matrixComposition composition
				// firstly, search by CVs
				StringBuilder sb = new StringBuilder();
				String temp = MzMLControlVocabularyXmlFactory.getFullCVFromParamGroupByAccession(paramGroup,
						referenceableParamGroupList, MATRIX_SOLUTION_CV);
				if (temp != null) {
					sb.append(temp);
					dicc.put(temp, temp);
				}
				temp = MzMLControlVocabularyXmlFactory.getFullCVFromParamGroupByAccession(paramGroup,
						referenceableParamGroupList, MATRIX_SOLUTION_CONCENTRATION_CV);
				if (temp != null) {
					if (!"".equals(sb.toString()))
						sb.append("\n");
					sb.append(temp);
					dicc.put(temp, temp);
				}
				// then, by text
				temp = MzMLControlVocabularyXmlFactory.getValueFromParamGroupByName(paramGroup,
						referenceableParamGroupList, MATRIX_TEXT_LIST, MatchMode.ANYWHERE);
				if (temp != null) {
					if (!"".equals(sb.toString()))
						sb.append("\n");
					sb.append(temp);
					dicc.put(temp, temp);
				}
				if (!"".equals(sb.toString()))
					this.matrixComposition = sb.toString();
				// end matrix composition

				// Maldi Dissociation
				sb = new StringBuilder();
				List<CVParam> maldiDissociationMethod = MzMLControlVocabularyXmlFactory.getCvsFromParamGroup(paramGroup,
						referenceableParamGroupList, MaldiDissociationMethod.getInstance(cvManager));
				if (maldiDissociationMethod != null && !maldiDissociationMethod.isEmpty()) {
					CVParam cvParam = maldiDissociationMethod.get(0);
					this.dissociation = cvParam.getName();
					if (cvParam.getValue() != null && !"".equals(cvParam.getValue()))
						this.dissociationSummary = cvParam.getValue();
				}
				if (this.dissociation == null) {
					String dissociationMethod = MzMLControlVocabularyXmlFactory.getFullCVsFromParamGroup(paramGroup,
							referenceableParamGroupList, MaldiDissociationMethod.getInstance(cvManager));
					if (dissociationMethod != null) {
						sb.append(dissociationMethod);
						dicc.put(dissociationMethod, dissociationMethod);
					}

					if ("".equals(sb.toString())) {
						final String dissociationText = MzMLControlVocabularyXmlFactory.getFullCVFromParamGroupByName(
								paramGroup, referenceableParamGroupList, this.DISSOCIATION_TEXT_LIST,
								MatchMode.ANYWHERE);
						if (dissociationText != null) {
							sb.append(dissociationText);
							dicc.put(dissociationText, dissociationText);
						}
					}
					if (!"".equals(sb.toString()))
						this.dissociation = sb.toString();
				}
				// end dissociation

				// Dissociation summary
				this.dissociationSummary = MzMLControlVocabularyXmlFactory.getFullCVFromParamGroupByName(paramGroup,
						referenceableParamGroupList, DISSOCIATION_SUMMARY_TEXT_LIST, MatchMode.ANYWHERE);

				// delayed extraction
				final CVParam delayedExtractionCV = MzMLControlVocabularyXmlFactory.getCvFromParamGroup(paramGroup,
						referenceableParamGroupList, IonOpticsType.DELAYED_CV);
				if (delayedExtractionCV != null) {
					this.extraction = MzMLControlVocabularyXmlFactory.getFullCVParam(delayedExtractionCV);
				}
				if (this.extraction == null)
					this.extraction = MzMLControlVocabularyXmlFactory.getFullCVFromParamGroupByName(paramGroup,
							referenceableParamGroupList, EXTRACTION_DELAY_SUMMARY_TEXT_LIST, MatchMode.ANYWHERE);
				dicc.put(extraction, extraction);
				// end delayed extraction

				// laser parameters
				sb = new StringBuilder();
				final List<CVParam> laserAttributeCVs = MzMLControlVocabularyXmlFactory.getCvsFromParamGroup(
						sourceComponent, referenceableParamGroupList, LaserAttribute.getInstance(cvManager));
				if (laserAttributeCVs != null) {
					for (CVParam cvParam : laserAttributeCVs) {
						if (!"".equals(sb.toString()))
							sb.append("\n");
						sb.append(MzMLControlVocabularyXmlFactory.getFullCVParam(cvParam));
					}
				}
				if (!"".equals(sb.toString()))
					this.laser_parameters = sb.toString();
				else
					// Get all params that are not in the dicc and store in
					// "laser_parameters"
					this.laser_parameters = parseAllParams(paramGroup, dicc);
			} else {
				this.name = null;
			}
		}
	}

	private String parseAllParams(ParamGroup paramGroup, Map<String, String> dicc) {
		StringBuilder sb = new StringBuilder();

		sb.append(parseAllCvParams(paramGroup.getCvParam(), dicc));
		sb.append(parseAllUserParams(paramGroup.getUserParam(), dicc));

		for (ReferenceableParamGroupRef paramRef : paramGroup.getReferenceableParamGroupRef()) {
			if (paramRef.getReferenceableParamGroup().getCvParam() != null)
				sb.append(parseAllCvParams(paramRef.getReferenceableParamGroup().getCvParam(), dicc));
			if (paramRef.getReferenceableParamGroup().getUserParam() != null)
				sb.append(parseAllUserParams(paramRef.getReferenceableParamGroup().getUserParam(), dicc));
		}
		if (sb.toString() != null && !"".equals(sb.toString())) {
			return sb.toString();
		}
		return null;
	}

	private String parseAllUserParams(List<UserParam> userParams, Map<String, String> dicc) {
		StringBuilder sb = new StringBuilder();
		String temp = null;
		boolean include;
		for (UserParam userParam : userParams) {
			include = true;
			temp = MzMLControlVocabularyXmlFactory.getFullUserParam(userParam);
			if (dicc.containsKey(temp)) {
				include = false;
			}
			if (dicc.containsKey(userParam.getName())) {
				include = false;
			}
			if (dicc.containsKey(userParam.getValue())) {
				include = false;
			}
			if (include) {
				if (!sb.toString().equals(""))
					sb.append("\n");
				sb.append(MzMLControlVocabularyXmlFactory.getFullUserParam(userParam));
			}
		}
		return sb.toString();
	}

	private String parseAllCvParams(List<CVParam> cvParams, Map<String, String> dicc) {
		StringBuilder sb = new StringBuilder();
		String temp = null;
		boolean include;
		for (CVParam cvParam : cvParams) {
			include = true;
			temp = MzMLControlVocabularyXmlFactory.getFullCVParam(cvParam);
			if (dicc.containsKey(temp)) {
				include = false;
			}
			if (dicc.containsKey(cvParam.getName())) {
				include = false;
			}
			if (dicc.containsKey(cvParam.getValue())) {
				include = false;
			}
			if (include) {
				if (!sb.toString().equals(""))
					sb.append("\n");
				sb.append(MzMLControlVocabularyXmlFactory.getFullCVParam(cvParam));
			}
		}
		return sb.toString();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getLaserParameters() {
		final String ret = laser_parameters;
		if ("".equals(ret))
			return null;
		return ret;
	}

	@Override
	public String getPlateType() {
		return plateType;
	}

	@Override
	public String getMatrix() {
		return matrixComposition;
	}

	@Override
	public String getDissociation() {
		return dissociation;
	}

	@Override
	public String getDissociationSummary() {
		return dissociationSummary;
	}

	@Override
	public String getExtraction() {
		return extraction;
	}

	@Override
	public String getLaser() {
		return laserType;
	}

	@Override
	public String getLaserWaveLength() {
		return wavelength;
	}

	private boolean isMaldiFromParamGroup(ParamGroup paramGroup,
			ReferenceableParamGroupList referenceableParamGroupList) {
		if (paramGroup != null) {
			if (this.isMaldiFromCVParams(paramGroup.getCvParam()))
				return true;
			if (this.isMaldiFromUserParams(paramGroup.getUserParam()))
				return true;
			for (ReferenceableParamGroupRef refParam : paramGroup.getReferenceableParamGroupRef()) {
				ReferenceableParamGroup refParamGroup = MzMLControlVocabularyXmlFactory
						.searchParamGroupInReferenceableParamGroupList(refParam.getRef(), referenceableParamGroupList);
				if (this.isMaldiFromCVParams(refParamGroup.getCvParam()))
					return true;
				if (this.isMaldiFromUserParams(refParamGroup.getUserParam()))
					return true;

			}
		}
		return false;
	}

	private boolean isMaldiFromCVParams(List<CVParam> listParam) {
		for (CVParam cvParam : listParam) {
			if (IonSourceName.IONIZATION_TYPE_ACC.equals(cvParam.getAccession())) {

				if (IonSourceName.isMaldiFromDescription(cvParam.getValue(), cvManager))
					return true;
			} else {
				final ControlVocabularyTerm cvTerm = IonSourceName.getInstance(cvManager)
						.getCVTermByAccession(new Accession(cvParam.getAccession()));
				if (cvTerm != null && IonSourceName.isMaldiFromAccession(cvTerm.getTermAccession()))
					return true;
			}
		}
		return false;
	}

	private boolean isMaldiFromUserParams(List<UserParam> listParam) {
		for (UserParam userParam : listParam) {
			if (IonSourceName.isMaldiFromDescription(userParam.getValue(), cvManager))
				return true;
		}
		return false;
	}
}
