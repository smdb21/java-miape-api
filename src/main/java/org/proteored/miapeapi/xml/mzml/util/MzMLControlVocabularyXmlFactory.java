package org.proteored.miapeapi.xml.mzml.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.proteored.miapeapi.cv.Accession;
import org.proteored.miapeapi.cv.ControlVocabularySet;
import org.proteored.miapeapi.cv.ControlVocabularyTerm;
import org.proteored.miapeapi.interfaces.MatchMode;

import uk.ac.ebi.jmzml.model.mzml.CVParam;
import uk.ac.ebi.jmzml.model.mzml.ParamGroup;
import uk.ac.ebi.jmzml.model.mzml.ReferenceableParamGroup;
import uk.ac.ebi.jmzml.model.mzml.ReferenceableParamGroupList;
import uk.ac.ebi.jmzml.model.mzml.ReferenceableParamGroupRef;
import uk.ac.ebi.jmzml.model.mzml.UserParam;

public class MzMLControlVocabularyXmlFactory {

	/**
	 * Search for cvParams in a {@link ParamGroup} looking by the CV allowed in
	 * one section
	 * 
	 * @param mzMLParamGroup
	 * @param referenceableParamGroupList
	 * @param cvSets
	 * @return the list of CVParam
	 */
	public static List<CVParam> getCvsFromParamGroup(ParamGroup mzMLParamGroup,
			ReferenceableParamGroupList referenceableParamGroupList, ControlVocabularySet... cvSets) {

		if (mzMLParamGroup != null && cvSets != null) {
			for (ControlVocabularySet cvSet : cvSets) {
				final List<ControlVocabularyTerm> cvTermSet = cvSet.getPossibleValues();
				if (cvTermSet != null) {
					List<CVParam> cvList = new ArrayList<CVParam>();
					// Search in CVParams
					for (CVParam cvParam : mzMLParamGroup.getCvParam()) {
						for (ControlVocabularyTerm controlVocabularyField : cvTermSet) {
							if (controlVocabularyField.getTermAccession().equals(cvParam.getAccession())) {
								cvList.add(cvParam);
							}
						}
					}
					// Search in CVParams of the referenceable ParamGroup
					if (mzMLParamGroup.getReferenceableParamGroupRef() != null
							&& mzMLParamGroup.getReferenceableParamGroupRef().size() > 0) {
						for (ReferenceableParamGroupRef ref : mzMLParamGroup.getReferenceableParamGroupRef()) {
							ReferenceableParamGroup refParamGroup = MzMLControlVocabularyXmlFactory
									.searchParamGroupInReferenceableParamGroupList(ref.getRef(),
											referenceableParamGroupList);
							for (CVParam cvParam : refParamGroup.getCvParam()) {
								for (ControlVocabularyTerm controlVocabularyField : cvTermSet) {
									if (controlVocabularyField.getTermAccession().equals(cvParam.getAccession())) {
										cvList.add(cvParam);
									}
								}
							}
						}
					}
					if (cvList.size() > 0)
						return cvList;
				}
			}

		}
		return null;
	}

	/**
	 * Search a cvParam in a {@link ParamGroup} looking by CV accession
	 * 
	 * @param mzMLParamGroup
	 * @param referenceableParamGroupList
	 * @param accession
	 * @return the CVParam
	 */
	public static CVParam getCvFromParamGroup(ParamGroup mzMLParamGroup,
			ReferenceableParamGroupList referenceableParamGroupList, Accession accession) {
		if (mzMLParamGroup != null && accession != null) {
			// Search in CVParams
			for (CVParam cvParam : mzMLParamGroup.getCvParam()) {
				if (accession.equals(cvParam.getAccession())) {
					return cvParam;
				}
			}

			// Search in CVParams of the referenceable ParamGroup
			if (mzMLParamGroup.getReferenceableParamGroupRef() != null
					&& mzMLParamGroup.getReferenceableParamGroupRef().size() > 0) {
				for (ReferenceableParamGroupRef ref : mzMLParamGroup.getReferenceableParamGroupRef()) {
					ReferenceableParamGroup refParamGroup = MzMLControlVocabularyXmlFactory
							.searchParamGroupInReferenceableParamGroupList(ref.getRef(), referenceableParamGroupList);
					for (CVParam cvParam : refParamGroup.getCvParam()) {
						if (cvParam != null && accession.equals(cvParam.getAccession())) {
							return cvParam;
						}

					}
				}
			}
		}
		return null;
	}

	/**
	 * Search a cvParam or userParam in a {@link ParamGroup} looking by CV name
	 * Note: all comparisons are not case sensitive
	 * 
	 * @param mzMLParamGroup
	 * @param name
	 * @param matchMode
	 *            type of string match. See {@link MatchMode}
	 * @return an object either a {@link CVParam} or a {@link UserParam}
	 */
	public static Object getCvFromParamGroupByName(ParamGroup mzMLParamGroup,
			ReferenceableParamGroupList referenceableParamGroupList, String name, String matchMode) {
		if (mzMLParamGroup != null && name != null) {
			// Search in CVParams
			for (CVParam cvParam : mzMLParamGroup.getCvParam()) {
				if (matchMode.equals(MatchMode.ANYWHERE)) {
					if (cvParam.getName().toLowerCase().contains(name.toLowerCase())) {
						return cvParam;
					}
				} else {
					if (cvParam.getName().toLowerCase().equals(name.toLowerCase())) {
						return cvParam;
					}
				}
			}

			// Search in CVParams
			for (UserParam userParam : mzMLParamGroup.getUserParam()) {
				if (matchMode.equals(MatchMode.ANYWHERE)) {
					if (userParam.getName().toLowerCase().contains(name.toLowerCase())) {
						return userParam;
					}
				} else {
					if (userParam.getName().toLowerCase().equals(name.toLowerCase())) {
						return userParam;
					}
				}
			}

			// Search in CVParams of the referenceable ParamGroup
			if (mzMLParamGroup.getReferenceableParamGroupRef() != null
					&& mzMLParamGroup.getReferenceableParamGroupRef().size() > 0) {
				for (ReferenceableParamGroupRef paramRef : mzMLParamGroup.getReferenceableParamGroupRef()) {
					ReferenceableParamGroup refParamGroup = searchParamGroupInReferenceableParamGroupList(
							paramRef.getRef(), referenceableParamGroupList);
					for (CVParam cvParam : refParamGroup.getCvParam()) {
						if (matchMode.equals(MatchMode.ANYWHERE)) {
							if (cvParam.getName().toLowerCase().contains(name.toLowerCase())) {
								return cvParam;
							}
						} else {
							if (cvParam.getName().toLowerCase().equals(name.toLowerCase())) {
								return cvParam;
							}
						}

					}
				}
			}
		}
		return null;
	}

	/**
	 * Gets the value of a CV if found in a paramGroup looking possible values
	 * of an array of strings. Note that also userParams are checked.
	 * 
	 * @param paramGroup
	 * @param referenceableParamGroupList
	 * @param textList
	 * @param matchMode
	 *            type of string matching. See {@link MatchMode}
	 * @return the value of the CV
	 */
	public static String getValueFromParamGroupByName(ParamGroup paramGroup,
			ReferenceableParamGroupList referenceableParamGroupList, String[] textList, String matchMode) {
		for (String text : textList) {

			Object cvOrUserParam = MzMLControlVocabularyXmlFactory.getCvFromParamGroupByName(paramGroup,
					referenceableParamGroupList, text, matchMode);
			if (cvOrUserParam != null) {
				if (cvOrUserParam instanceof CVParam)
					return ((CVParam) cvOrUserParam).getValue();
				if (cvOrUserParam instanceof UserParam)
					return ((UserParam) cvOrUserParam).getValue();
			}
		}

		return null;
	}

	/**
	 * Gets the full descirption of a CV if found in a paramGroup looking
	 * possible values of an array of strings. Note that also userParams are
	 * checked.
	 * 
	 * @param paramGroup
	 * @param referenceableParamGroupList
	 * @param textList
	 * @param matchMode
	 *            type of string matching. See {@link MatchMode}
	 * @return the value of the CV
	 */
	public static String getFullCVFromParamGroupByName(ParamGroup paramGroup,
			ReferenceableParamGroupList referenceableParamGroupList, String[] textList, String matchMode) {
		for (String text : textList) {
			Object cvOrUserParam = MzMLControlVocabularyXmlFactory.getCvFromParamGroupByName(paramGroup,
					referenceableParamGroupList, text, matchMode);
			if (cvOrUserParam != null) {
				if (cvOrUserParam instanceof CVParam)
					return getFullCVParam((CVParam) cvOrUserParam);
				if (cvOrUserParam instanceof UserParam)
					return getFullUserParam((UserParam) cvOrUserParam);
			}
		}

		return null;
	}

	/**
	 * Gets the value of a CV if found in a paramGroup looking possible values
	 * of a CV set
	 * 
	 * @param paramGroup
	 * @param cvSets
	 * @return the value of the CV
	 */
	public static String getValueFromParamGroup(ParamGroup paramGroup,
			ReferenceableParamGroupList referenceableParamGroupList, ControlVocabularySet... cvSets) {
		List<CVParam> cvs = MzMLControlVocabularyXmlFactory.getCvsFromParamGroup(paramGroup,
				referenceableParamGroupList, cvSets);
		if (cvs != null) {
			StringBuilder sb = new StringBuilder();
			for (CVParam cvParam : cvs) {
				if (!"".equals(sb.toString()))
					sb.append("\n");

				// if the value is "", return the name
				if (cvParam.getValue() == null || "".equals(cvParam.getValue()))
					sb.append(cvParam.getName());
				else
					sb.append(cvParam.getValue());
			}
			if (!"".equals(sb.toString()))
				return sb.toString();
		}
		return null;
	}

	/**
	 * Gets the value of a CV if found in a paramGroup looking by its accession
	 * 
	 * @param paramGroup
	 * @param accession
	 * @return the value of the CV
	 */
	public static String getValueFromParamGroupByAccession(ParamGroup paramGroup,
			ReferenceableParamGroupList referenceableParamGroupList, Accession accession) {
		CVParam cv = MzMLControlVocabularyXmlFactory.getCvFromParamGroup(paramGroup, referenceableParamGroupList,
				accession);
		if (cv != null)
			if (!"".equals(cv.getValue()))
				return cv.getValue();
		return null;
	}

	/**
	 * Gets the full description of a CV if found in a paramGroup looking by its
	 * accession
	 * 
	 * @param paramGroup
	 * @param accession
	 * @return the full description of the CV: "name=value unit"
	 */
	public static String getFullCVFromParamGroupByAccession(ParamGroup paramGroup,
			ReferenceableParamGroupList referenceableParamGroupList, Accession accession) {
		CVParam cv = MzMLControlVocabularyXmlFactory.getCvFromParamGroup(paramGroup, referenceableParamGroupList,
				accession);
		if (cv != null)
			return getFullCVParam(cv);
		return null;
	}

	/**
	 * Gets the full description of a CV if found in a paramGroup looking in the
	 * possible values of the CV in a certain MIAPE section
	 * 
	 * @param paramGroup
	 * @param referenceableParamGroupList
	 * @param cvSets
	 * @return the full description of the CV: "name=value unit"
	 */
	public static String getFullCVsFromParamGroup(ParamGroup paramGroup,
			ReferenceableParamGroupList referenceableParamGroupList, ControlVocabularySet... cvSets) {
		List<CVParam> cvs = MzMLControlVocabularyXmlFactory.getCvsFromParamGroup(paramGroup,
				referenceableParamGroupList, cvSets);
		if (cvs != null) {
			StringBuilder sb = new StringBuilder();
			for (CVParam cvParam : cvs) {
				if (!"".equals(sb.toString())) {
					sb.append("\n");
				}
				sb.append(getFullCVParam(cvParam));
			}
			return sb.toString();
		}
		return null;
	}

	/**
	 * Gets a string representing the information from a userParam. That is:
	 * "name=value unit"
	 * 
	 * @param param
	 * @return the string
	 */
	public static String getFullCVParam(CVParam param) {
		StringBuilder sb = new StringBuilder();
		if (param == null)
			return null;
		String value = param.getValue();
		String name = param.getName();
		String unitName = param.getUnitName();

		if (value == null || "".equals(value)) {
			sb.append(name);
		} else {
			sb.append(name);
			sb.append("=");
			sb.append(value);
		}
		if (unitName != null && !"".equals(unitName)) {
			sb.append(" " + unitName);
		}
		if (!"".equals(sb.toString()))
			return sb.toString();
		return null;
	}

	/**
	 * Gets a string representing the information from a userParam. That is:
	 * "name=value unit"
	 * 
	 * @param param
	 * @return the string
	 */
	public static String getFullUserParam(UserParam param) {
		StringBuilder sb = new StringBuilder();
		if (param == null)
			return null;
		String value = param.getValue();
		String name = param.getName();
		String unitName = param.getUnitName();

		if (value == null || value.equals("")) {
			sb.append(name);
		} else {
			sb.append(name);
			sb.append("=");
			sb.append(value);
		}
		if (unitName != null && !"".equals(unitName)) {
			sb.append(" " + unitName);
		}
		if (!"".equals(sb.toString()))
			return sb.toString();
		return null;
	}

	/**
	 * Get the name, value and unit from all CVParams and UserParams of a
	 * ParamGroup only if they are not found in a dictionary. They are separated
	 * by return carriages.
	 * 
	 * @param paramGroup
	 * @param referenceableParamGroupList
	 * @param dicc
	 *            the dictionary
	 * @return the string separated by return carriages
	 */
	public static String parseAllParams(ParamGroup paramGroup, ReferenceableParamGroupList referenceableParamGroupList,
			Map<String, String> dicc) {
		StringBuilder sb = new StringBuilder();

		String parseAllCvParams = parseAllCvParams(paramGroup.getCvParam(), dicc);
		if (parseAllCvParams != null)
			sb.append(parseAllCvParams);

		String parseAllUserParams = parseAllUserParams(paramGroup.getUserParam(), dicc);
		if (parseAllUserParams != null) {
			if (!"".equals(sb.toString()) && !"".equals(parseAllUserParams))
				sb.append("\n");
			sb.append(parseAllUserParams);
		}
		for (ReferenceableParamGroupRef paramRef : paramGroup.getReferenceableParamGroupRef()) {
			ReferenceableParamGroup referencedParamGroup = searchParamGroupInReferenceableParamGroupList(
					paramRef.getRef(), referenceableParamGroupList);
			if (referencedParamGroup.getCvParam() != null) {

				parseAllCvParams = parseAllCvParams(referencedParamGroup.getCvParam(), dicc);
				if (parseAllCvParams != null) {
					if (!"".equals(sb.toString()) && !"".equals(parseAllCvParams))
						sb.append("\n");
					sb.append(parseAllCvParams);
				}
			}
			if (referencedParamGroup.getUserParam() != null) {
				parseAllUserParams = parseAllUserParams(referencedParamGroup.getUserParam(), dicc);
				if (parseAllUserParams != null) {
					if (!"".equals(sb.toString()) && !"".equals(parseAllUserParams))
						sb.append("\n");
					sb.append(parseAllUserParams);
				}
			}
		}
		if (!"".equals(sb.toString()))
			return sb.toString();
		return null;
	}

	public static ReferenceableParamGroup searchParamGroupInReferenceableParamGroupList(String ref,
			ReferenceableParamGroupList referenceableParamGroupList) {
		if (ref == null || "".equals(ref) || referenceableParamGroupList == null
				|| referenceableParamGroupList.getReferenceableParamGroup().size() <= 0)
			return null;
		for (ReferenceableParamGroup refParamGroup : referenceableParamGroupList.getReferenceableParamGroup()) {
			if (refParamGroup.getId().equals(ref)) {
				return refParamGroup;
			}
		}
		return null;
	}

	public static String parseAllUserParams(List<UserParam> userParams, Map<String, String> dicc) {
		StringBuilder sb = new StringBuilder();
		String temp = null;
		boolean include;
		for (UserParam userParam : userParams) {
			include = true;
			temp = MzMLControlVocabularyXmlFactory.getFullUserParam(userParam);
			if (dicc != null) {
				if (dicc.containsKey(temp)) {
					include = false;
				}
				if (userParam.getName() != null && dicc.containsKey(userParam.getName())) {
					include = false;
				}
				if (userParam.getValue() != null && dicc.containsKey(userParam.getValue())) {
					include = false;
				}
			}
			if (include) {
				if (!sb.toString().equals(""))
					sb.append("\n");
				sb.append(MzMLControlVocabularyXmlFactory.getFullUserParam(userParam));
			}
		}
		if (!"".equals(sb.toString()))
			return sb.toString();
		return null;
	}

	public static String parseAllCvParams(List<CVParam> cvParams, Map<String, String> dicc) {
		StringBuilder sb = new StringBuilder();
		String temp = null;
		boolean include;
		if (cvParams != null) {
			for (CVParam cvParam : cvParams) {
				include = true;
				temp = MzMLControlVocabularyXmlFactory.getFullCVParam(cvParam);
				if (dicc != null) {
					if (dicc.containsKey(temp)) {
						include = false;
					}
					if (cvParam.getName() != null && dicc.containsKey(cvParam.getName())) {
						include = false;
					}
					if (cvParam.getValue() != null && dicc.containsKey(cvParam.getValue())) {
						include = false;
					}
				}
				if (include) {
					if (!sb.toString().equals(""))
						sb.append("\n");
					sb.append(temp);
				}
			}
		}
		if (!"".equals(sb.toString()))
			return sb.toString();
		return null;
	}

	public static ParamGroup createParamGroup(List<CVParam> cvParams, List<UserParam> userParams,
			List<ReferenceableParamGroupRef> paramGroupRefs) {
		ParamGroup paramGroup = new ParamGroup();
		if (cvParams != null)
			for (CVParam cvParam : cvParams) {
				paramGroup.getCvParam().add(cvParam);
			}
		if (userParams != null)
			for (UserParam userParam : userParams) {
				paramGroup.getUserParam().add(userParam);
			}
		if (paramGroupRefs != null)
			for (ReferenceableParamGroupRef paramRef : paramGroupRefs) {
				paramGroup.getReferenceableParamGroupRef().add(paramRef);
			}
		return paramGroup;

	}

	public static ParamGroup mergeParamGroups(ParamGroup paramGroup1, ParamGroup paramGroup2,
			ReferenceableParamGroupList referenceableParamGroupList) {
		ParamGroup commonParamGroup = new ParamGroup();
		if (paramGroup1 == null && paramGroup2 == null)
			return null;
		if (paramGroup1 != null) {
			for (CVParam cvParam : paramGroup1.getCvParam()) {
				if (isNotInTheList(cvParam.getAccession(), commonParamGroup.getCvParam()))
					commonParamGroup.getCvParam().add(cvParam);
			}
			for (UserParam userParam : paramGroup1.getUserParam()) {
				commonParamGroup.getUserParam().add(userParam);
			}
			for (ReferenceableParamGroupRef paramRef : paramGroup1.getReferenceableParamGroupRef()) {
				ReferenceableParamGroup refParamGroup = MzMLControlVocabularyXmlFactory
						.searchParamGroupInReferenceableParamGroupList(paramRef.getRef(), referenceableParamGroupList);
				if (refParamGroup != null && refParamGroup.getCvParam() != null) {
					for (CVParam cvParam : refParamGroup.getCvParam()) {
						if (isNotInTheList(cvParam.getAccession(), commonParamGroup.getCvParam()))
							commonParamGroup.getCvParam().add(cvParam);
					}
				}
			}
		}
		if (paramGroup2 != null) {
			for (CVParam cvParam : paramGroup2.getCvParam()) {
				if (isNotInTheList(cvParam.getAccession(), commonParamGroup.getCvParam()))
					commonParamGroup.getCvParam().add(cvParam);
			}
			for (UserParam userParam : paramGroup2.getUserParam()) {
				commonParamGroup.getUserParam().add(userParam);
			}
			for (ReferenceableParamGroupRef paramRef : paramGroup2.getReferenceableParamGroupRef()) {
				ReferenceableParamGroup refParamGroup = MzMLControlVocabularyXmlFactory
						.searchParamGroupInReferenceableParamGroupList(paramRef.getRef(), referenceableParamGroupList);
				if (refParamGroup != null && refParamGroup.getCvParam() != null) {
					for (CVParam cvParam : refParamGroup.getCvParam()) {
						if (isNotInTheList(cvParam.getAccession(), commonParamGroup.getCvParam()))
							commonParamGroup.getCvParam().add(cvParam);
					}
				}
			}
		}
		return commonParamGroup;
	}

	private static boolean isNotInTheList(String ref, List<CVParam> cvParams) {
		if (cvParams != null && !cvParams.isEmpty())
			for (CVParam cvParam : cvParams) {
				if (cvParam.getAccession().equals(ref))
					return false;
			}
		return true;
	}

}
