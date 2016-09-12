package org.proteored.miapeapi.xml.mzidentml_1_1;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.proteored.miapeapi.cv.Accession;
import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.msi.ThresholdName;
import org.proteored.miapeapi.interfaces.Software;
import org.proteored.miapeapi.interfaces.msi.PostProcessingMethod;
import org.proteored.miapeapi.interfaces.msi.Validation;
import org.proteored.miapeapi.xml.mzidentml_1_1.util.MzidentmlControlVocabularyXmlFactory;
import org.proteored.miapeapi.xml.util.MiapeXmlUtil;

import uk.ac.ebi.jmzidml.model.mzidml.AbstractContact;
import uk.ac.ebi.jmzidml.model.mzidml.AbstractParam;
import uk.ac.ebi.jmzidml.model.mzidml.AnalysisSoftware;
import uk.ac.ebi.jmzidml.model.mzidml.CvParam;
import uk.ac.ebi.jmzidml.model.mzidml.ParamList;
import uk.ac.ebi.jmzidml.model.mzidml.ProteinDetectionProtocol;
import uk.ac.ebi.jmzidml.model.mzidml.SpectrumIdentificationProtocol;

public class ValidationImpl implements Validation {

	private final List<Object> list;
	private final List<AnalysisSoftware> analysisSoftwareList;

	private final ControlVocabularyManager cvManager;
	private final String spectrumIdentificationSoftwareID;
	private final AnalysisSoftware analysisSoftware;

	/**
	 * 
	 * @param list
	 * @param analysisSoftware
	 * @param spectrumIdentificationSoftwareID2
	 *            Only capture it if different from the software referenced by
	 *            this SpectrumIdentificationProtocol
	 * 
	 * @param cvManager2
	 */
	public ValidationImpl(List<Object> list,
			List<AnalysisSoftware> analysisSoftware,
			String spectrumIdentificationSoftwareID2,
			ControlVocabularyManager cvManager2) {
		this.list = list;
		this.analysisSoftwareList = analysisSoftware;
		this.analysisSoftware = null;
		this.cvManager = cvManager2;
		this.spectrumIdentificationSoftwareID = spectrumIdentificationSoftwareID2;
	}

	public ValidationImpl(AnalysisSoftware analysisSoftware,
			ControlVocabularyManager cvManager2) {
		this.list = null;
		this.analysisSoftwareList = null;
		this.analysisSoftware = analysisSoftware;
		this.cvManager = cvManager2;
		this.spectrumIdentificationSoftwareID = null;
	}

	@Override
	public String getName() {
		return getNameFromList();
	}

	private String getNameFromList() {
		StringBuilder sb = new StringBuilder();
		if (list != null) {
			for (Object object : list) {
				if (object instanceof ProteinDetectionProtocol) {
					if (!"".equals(sb.toString()))
						sb.append(" - ");
					ProteinDetectionProtocol pdp = (ProteinDetectionProtocol) object;
					if (pdp.getName() != null)
						sb.append(pdp.getName());
					else
						// sb.append(pdp.getId());
						sb.append("Protein detection protocol");

				} else if (object instanceof SpectrumIdentificationProtocol) {
					if (!"".equals(sb.toString()))
						sb.append(" - ");
					SpectrumIdentificationProtocol sip = (SpectrumIdentificationProtocol) object;
					if (sip.getName() != null)
						sb.append(sip.getName());
					else {
						// sb.append(sip.getId());
						sb.append("Spectrum identification protocol");
					}

				}
			}
			if (!"".equals(sb.toString()))
				return sb.toString();
		} else if (this.analysisSoftware != null) {
			if (this.analysisSoftware.getSoftwareName() != null) {
				String paramText = MzidentmlControlVocabularyXmlFactory
						.readEntireParam(this.analysisSoftware
								.getSoftwareName());
				return paramText;
			}
			if (this.analysisSoftware.getName() != null)
				return this.analysisSoftware.getName();
			if (this.analysisSoftware.getId() != null)
				return this.analysisSoftware.getId();
		}

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
		Set<Software> validationSoftwares = new HashSet<Software>();

		if (list != null) {
			ParamList paramList = new ParamList();
			ParamList threshold = new ParamList();
			String softwareRef = null;
			AnalysisSoftware softwareXML = null;
			for (Object object : list) {
				if (object instanceof ProteinDetectionProtocol) {
					ProteinDetectionProtocol pdp = (ProteinDetectionProtocol) object;
					softwareRef = pdp.getAnalysisSoftwareRef();
					softwareXML = getSoftware(softwareRef);
					final ParamList analysisParams = pdp.getAnalysisParams();
					if (analysisParams != null)
						paramList.getParamGroup().addAll(
								analysisParams.getParamGroup());
					final ParamList threshold2 = pdp.getThreshold();
					if (threshold2 != null)
						paramList.getParamGroup().addAll(
								threshold2.getParamGroup());
				} else if (object instanceof SpectrumIdentificationProtocol) {
					SpectrumIdentificationProtocol sip = (SpectrumIdentificationProtocol) object;
					softwareRef = sip.getAnalysisSoftwareRef();
					softwareXML = getSoftware(softwareRef);
					final ParamList additionalSearchParams = sip
							.getAdditionalSearchParams();
					if (additionalSearchParams != null)
						paramList.getParamGroup().addAll(
								additionalSearchParams.getParamGroup());
					final ParamList threshold2 = sip.getThreshold();
					if (threshold2 != null)
						paramList.getParamGroup().addAll(
								threshold2.getParamGroup());
				}
			}

			// Only capture it if different from the software referenced by
			// the SpectrumIdentificationProtocol passed in the argument of
			// the constructor
			if (!softwareRef.equals(spectrumIdentificationSoftwareID)) {
				if (softwareXML != null) {
					AbstractContact abstractContact = null;
					if (softwareXML.getContactRole() != null)
						abstractContact = softwareXML.getContactRole()
								.getContact();

					validationSoftwares.add(new ValidationSoftwareImpl(
							softwareXML, abstractContact, paramList, threshold,
							cvManager));

				}
			}

		} else if (this.analysisSoftware != null) {
			AbstractContact abstractContact = null;
			if (this.analysisSoftware.getContactRole() != null)
				abstractContact = this.analysisSoftware.getContactRole()
						.getContact();
			validationSoftwares.add(new ValidationSoftwareImpl(
					this.analysisSoftware, abstractContact, null, null,
					cvManager));
		}

		if (!validationSoftwares.isEmpty())
			return validationSoftwares;
		return null;
	}

	private AnalysisSoftware getSoftware(String softwareRef) {
		if (this.analysisSoftwareList != null) {
			for (AnalysisSoftware analysisSoftware : analysisSoftwareList) {
				if (analysisSoftware.getId().equals(softwareRef))
					return analysisSoftware;
			}
		} else if (this.analysisSoftware != null
				&& this.analysisSoftware.getId().equals(softwareRef))
			return this.analysisSoftware;
		return null;
	}

	@Override
	public String getGlobalThresholds() {
		// take params from Threshold element
		StringBuilder sb = new StringBuilder();
		MzidentmlControlVocabularyXmlFactory cvUtil = new MzidentmlControlVocabularyXmlFactory(
				null, cvManager);
		if (this.list != null) {
			for (Object object : list) {
				if (object instanceof ProteinDetectionProtocol) {
					ProteinDetectionProtocol proteinDetectionProtocol = (ProteinDetectionProtocol) object;
					if (proteinDetectionProtocol != null
							&& proteinDetectionProtocol.getThreshold() != null) {
						for (AbstractParam param : proteinDetectionProtocol
								.getThreshold().getParamGroup()) {
							if (param instanceof CvParam) {
								if (cvUtil.isCV(
										new Accession(((CvParam) param)
												.getAccession()), ThresholdName
												.getInstance(cvManager))) {
									if (!"".equals(sb.toString()))
										sb.append(MiapeXmlUtil.TERM_SEPARATOR);
									sb.append(MzidentmlControlVocabularyXmlFactory
											.readEntireParam(param));
								}
							}
						}
					}
				} else if (object instanceof SpectrumIdentificationProtocol) {
					SpectrumIdentificationProtocol spectrumIdentificationProtocol = (SpectrumIdentificationProtocol) object;
					if (spectrumIdentificationProtocol.getThreshold() != null) {
						for (AbstractParam param : spectrumIdentificationProtocol
								.getThreshold().getParamGroup()) {
							if (param instanceof CvParam) {
								if (cvUtil.isCV(
										new Accession(((CvParam) param)
												.getAccession()), ThresholdName
												.getInstance(cvManager))) {
									if (!"".equals(sb.toString()))
										sb.append(MiapeXmlUtil.TERM_SEPARATOR);
									sb.append(MzidentmlControlVocabularyXmlFactory
											.readEntireParam(param));
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
