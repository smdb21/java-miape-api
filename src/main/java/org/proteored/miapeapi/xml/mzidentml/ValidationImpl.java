package org.proteored.miapeapi.xml.mzidentml;

import java.util.List;
import java.util.Set;

import org.proteored.miapeapi.cv.Accession;
import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.msi.ThresholdName;
import org.proteored.miapeapi.interfaces.Software;
import org.proteored.miapeapi.interfaces.msi.PostProcessingMethod;
import org.proteored.miapeapi.interfaces.msi.Validation;
import org.proteored.miapeapi.xml.mzidentml.autogenerated.FuGECommonAuditContactType;
import org.proteored.miapeapi.xml.mzidentml.autogenerated.FuGECommonOntologyCvParamType;
import org.proteored.miapeapi.xml.mzidentml.autogenerated.FuGECommonOntologyParamType;
import org.proteored.miapeapi.xml.mzidentml.autogenerated.PSIPIAnalysisProcessProteinDetectionProtocolType;
import org.proteored.miapeapi.xml.mzidentml.autogenerated.PSIPIAnalysisSearchAnalysisSoftwareType;
import org.proteored.miapeapi.xml.mzidentml.autogenerated.PSIPIAnalysisSearchSpectrumIdentificationProtocolType;
import org.proteored.miapeapi.xml.mzidentml.autogenerated.ParamListType;
import org.proteored.miapeapi.xml.mzidentml.util.MzidentmlControlVocabularyXmlFactory;
import org.proteored.miapeapi.xml.util.MiapeXmlUtil;

import gnu.trove.set.hash.THashSet;

public class ValidationImpl implements Validation {
	private final List<PSIPIAnalysisSearchAnalysisSoftwareType> analysisSoftwareList;
	private final List<FuGECommonAuditContactType> contactList;
	private final ControlVocabularyManager cvManager;
	private final List<Object> list;
	private final String spectrumIdentificationSoftwareID;
	private final PSIPIAnalysisSearchAnalysisSoftwareType analysisSoftware;

	public ValidationImpl(List<Object> list, List<PSIPIAnalysisSearchAnalysisSoftwareType> analysisSoftwareList,
			List<FuGECommonAuditContactType> contactList, String spectrumIdentificationSoftwareID,
			ControlVocabularyManager cvManager) {
		this.list = list;
		this.analysisSoftwareList = analysisSoftwareList;
		this.contactList = contactList;
		this.cvManager = cvManager;
		this.spectrumIdentificationSoftwareID = spectrumIdentificationSoftwareID;
		this.analysisSoftware = null;
	}

	public ValidationImpl(PSIPIAnalysisSearchAnalysisSoftwareType analysisSoftware,
			List<FuGECommonAuditContactType> contactList, ControlVocabularyManager cvManager2) {
		this.list = null;
		this.analysisSoftwareList = null;
		this.contactList = contactList;
		this.cvManager = cvManager2;
		this.spectrumIdentificationSoftwareID = null;
		this.analysisSoftware = analysisSoftware;
	}

	@Override
	public String getName() {
		return this.getNameFromList();
	}

	private String getNameFromList() {
		StringBuilder sb = new StringBuilder();
		if (list != null) {

			for (Object object : list) {
				if (object instanceof PSIPIAnalysisProcessProteinDetectionProtocolType) {
					if (!"".equals(sb.toString()))
						sb.append(" - ");
					PSIPIAnalysisProcessProteinDetectionProtocolType pdp = (PSIPIAnalysisProcessProteinDetectionProtocolType) object;
					if (pdp.getName() != null)
						sb.append(pdp.getName());
					else
						sb.append(pdp.getId());
				} else if (object instanceof PSIPIAnalysisSearchSpectrumIdentificationProtocolType) {
					if (!"".equals(sb.toString()))
						sb.append(" - ");
					PSIPIAnalysisSearchSpectrumIdentificationProtocolType pdp = (PSIPIAnalysisSearchSpectrumIdentificationProtocolType) object;
					if (pdp.getName() != null)
						sb.append(pdp.getName());
					else
						sb.append(pdp.getId());
				}
			}
		} else if (this.analysisSoftware != null) {
			if (this.analysisSoftware.getSoftwareName() != null) {
				String paramText = MzidentmlControlVocabularyXmlFactory
						.readEntireParam(this.analysisSoftware.getSoftwareName());
				return paramText;
			}
			if (this.analysisSoftware.getName() != null)
				return this.analysisSoftware.getName();
			if (this.analysisSoftware.getId() != null)
				return this.analysisSoftware.getId();
		}
		if (!"".equals(sb.toString()))
			return sb.toString();
		return "Validation";
	}

	@Override
	public String getStatisticalAnalysisResults() {
		// this is a description of how a FDR threshold has been applied
		// so it cannot be extracted from some CVs
		return null;
	}

	@Override
	public Set<PostProcessingMethod> getPostProcessingMethods() {
		// TODO Auto-generated method stub
		// no validation method is captured since there is a postprocessing
		// software here
		return null;
	}

	@Override
	public Set<Software> getPostProcessingSoftwares() {
		Set<Software> validationSoftwares = new THashSet<Software>();

		if (list != null) {
			ParamListType paramList = new ParamListType();
			ParamListType threshold = new ParamListType();
			String softwareRef = null;
			PSIPIAnalysisSearchAnalysisSoftwareType softwareXML = null;
			for (Object object : list) {
				if (object instanceof PSIPIAnalysisProcessProteinDetectionProtocolType) {
					PSIPIAnalysisProcessProteinDetectionProtocolType pdp = (PSIPIAnalysisProcessProteinDetectionProtocolType) object;
					softwareRef = pdp.getAnalysisSoftwareRef();
					softwareXML = getSoftware(softwareRef);
					final ParamListType analysisParams = pdp.getAnalysisParams();
					if (analysisParams != null)
						paramList.getParamGroup().addAll(analysisParams.getParamGroup());
					final ParamListType threshold2 = pdp.getThreshold();
					if (threshold2 != null)
						paramList.getParamGroup().addAll(threshold2.getParamGroup());
				} else if (object instanceof PSIPIAnalysisSearchSpectrumIdentificationProtocolType) {
					PSIPIAnalysisSearchSpectrumIdentificationProtocolType sip = (PSIPIAnalysisSearchSpectrumIdentificationProtocolType) object;
					softwareRef = sip.getAnalysisSoftwareRef();
					softwareXML = getSoftware(softwareRef);
					final ParamListType additionalSearchParams = sip.getAdditionalSearchParams();
					if (additionalSearchParams != null)
						paramList.getParamGroup().addAll(additionalSearchParams.getParamGroup());
					final ParamListType threshold2 = sip.getThreshold();
					if (threshold2 != null)
						paramList.getParamGroup().addAll(threshold2.getParamGroup());
				}
			}

			// Only capture it if different from the software referenced by
			// the SpectrumIdentificationProtocol passed in the argument of
			// the constructor
			if (!softwareRef.equals(spectrumIdentificationSoftwareID)) {
				if (softwareXML != null) {
					FuGECommonAuditContactType abstractContact = null;
					if (softwareXML.getContactRole() != null) {
						String contactRef = softwareXML.getContactRole().getContactRef();
						abstractContact = getContact(contactRef);
					}

					validationSoftwares.add(
							new ValidationSoftwareImpl(softwareXML, abstractContact, paramList, threshold, cvManager));

				}
			}

		} else if (this.analysisSoftware != null) {
			FuGECommonAuditContactType abstractContact = null;
			if (this.analysisSoftware.getContactRole() != null) {
				String contactRef = this.analysisSoftware.getContactRole().getContactRef();
				abstractContact = getContact(contactRef);
			}
			validationSoftwares
					.add(new ValidationSoftwareImpl(this.analysisSoftware, abstractContact, null, null, cvManager));
		}

		if (!validationSoftwares.isEmpty())
			return validationSoftwares;
		return null;
	}

	private FuGECommonAuditContactType getContact(String contactRef) {
		if (this.contactList != null) {
			for (FuGECommonAuditContactType contact : this.contactList) {
				if (contact.getId().equals(contactRef))
					return contact;
			}
		}
		return null;
	}

	private PSIPIAnalysisSearchAnalysisSoftwareType getSoftware(String softwareRef) {
		if (this.analysisSoftwareList != null) {
			for (PSIPIAnalysisSearchAnalysisSoftwareType analysisSoftware : analysisSoftwareList) {
				if (analysisSoftware.getId().equals(softwareRef))
					return analysisSoftware;
			}
		} else if (this.analysisSoftware != null && this.analysisSoftware.getId().equals(softwareRef)) {
			return this.analysisSoftware;
		}
		return null;
	}

	@Override
	public String getGlobalThresholds() {
		StringBuilder sb = new StringBuilder();
		MzidentmlControlVocabularyXmlFactory cvUtil = new MzidentmlControlVocabularyXmlFactory(null, cvManager);
		if (list != null) {
			for (Object object : list) {
				if (object instanceof PSIPIAnalysisProcessProteinDetectionProtocolType) {
					PSIPIAnalysisProcessProteinDetectionProtocolType proteinDetectionProtocol = (PSIPIAnalysisProcessProteinDetectionProtocolType) object;
					if (proteinDetectionProtocol != null && proteinDetectionProtocol.getThreshold() != null) {
						for (FuGECommonOntologyParamType param : proteinDetectionProtocol.getThreshold()
								.getParamGroup()) {
							if (param instanceof FuGECommonOntologyCvParamType) {
								if (cvUtil.isCV(new Accession(((FuGECommonOntologyCvParamType) param).getAccession()),
										ThresholdName.getInstance(cvManager))) {
									if (!"".equals(sb.toString()))
										sb.append(MiapeXmlUtil.TERM_SEPARATOR);
									sb.append(MzidentmlControlVocabularyXmlFactory.readEntireParam(param));
								}
							}
						}
					}
				} else if (object instanceof PSIPIAnalysisSearchSpectrumIdentificationProtocolType) {
					PSIPIAnalysisSearchSpectrumIdentificationProtocolType spectrumIdentificationProtocol = (PSIPIAnalysisSearchSpectrumIdentificationProtocolType) object;
					if (spectrumIdentificationProtocol.getThreshold() != null) {
						for (FuGECommonOntologyParamType param : spectrumIdentificationProtocol.getThreshold()
								.getParamGroup()) {
							if (param instanceof FuGECommonOntologyCvParamType) {
								if (cvUtil.isCV(new Accession(((FuGECommonOntologyCvParamType) param).getAccession()),
										ThresholdName.getInstance(cvManager))) {
									if (!"".equals(sb.toString()))
										sb.append(MiapeXmlUtil.TERM_SEPARATOR);
									sb.append(MzidentmlControlVocabularyXmlFactory.readEntireParam(param));
								}
							}
						}
					}

				}
			}
		}

		if (!"".equals(sb.toString()))
			return sb.toString();
		return null;
	}

}
