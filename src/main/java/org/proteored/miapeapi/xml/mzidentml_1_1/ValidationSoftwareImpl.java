package org.proteored.miapeapi.xml.mzidentml_1_1;

import org.proteored.miapeapi.cv.Accession;
import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ms.SoftwareName;
import org.proteored.miapeapi.interfaces.Software;
import org.proteored.miapeapi.xml.mzidentml_1_1.util.MzidentmlControlVocabularyXmlFactory;
import org.proteored.miapeapi.xml.util.MiapeXmlUtil;

import uk.ac.ebi.jmzidml.model.mzidml.AbstractContact;
import uk.ac.ebi.jmzidml.model.mzidml.AbstractParam;
import uk.ac.ebi.jmzidml.model.mzidml.AnalysisSoftware;
import uk.ac.ebi.jmzidml.model.mzidml.CvParam;
import uk.ac.ebi.jmzidml.model.mzidml.ParamList;
import uk.ac.ebi.jmzidml.model.mzidml.UserParam;

public class ValidationSoftwareImpl implements Software {
	private final AnalysisSoftware softwareXML;
	private final AbstractContact abstractContact;
	private final ParamList analysisParams;
	private final ParamList thresholds;
	private final ControlVocabularyManager cvManager;

	public ValidationSoftwareImpl(AnalysisSoftware softwareXML, AbstractContact abstractContact,
			ParamList analysisParamList, ParamList thresholdParamList,
			ControlVocabularyManager cvManager) {
		this.abstractContact = abstractContact;
		this.softwareXML = softwareXML;
		this.analysisParams = analysisParamList;
		this.thresholds = thresholdParamList;
		this.cvManager = cvManager;
	}

	@Override
	public String getCatalogNumber() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getComments() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCustomizations() {
		return softwareXML.getCustomizations();
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getManufacturer() {
		if (this.abstractContact != null)
			return abstractContact.getName();
		return null;
	}

	@Override
	public String getModel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		if (softwareXML.getSoftwareName() != null) {
			CvParam cvParam = softwareXML.getSoftwareName().getCvParam();
			if (cvParam != null) {
				if (cvManager.isSonOf(new Accession(cvParam.getAccession()),
						SoftwareName.CV_SOFTWARE)) {
					return cvParam.getName();
				}
			}
			UserParam userParam = softwareXML.getSoftwareName().getUserParam();
			if (userParam != null) {
				return userParam.getName();
			}
		}
		return softwareXML.getName();
	}

	@Override
	public String getParameters() {
		StringBuilder sb = new StringBuilder();
		// Analysis params
		if (analysisParams != null) {
			for (AbstractParam param : analysisParams.getParamGroup()) {
				sb.append(MzidentmlControlVocabularyXmlFactory.readEntireParam(param));
				sb.append(MiapeXmlUtil.TERM_SEPARATOR);
			}
		}
		if (thresholds != null) {
			for (AbstractParam param : thresholds.getParamGroup()) {
				sb.append(MzidentmlControlVocabularyXmlFactory.readEntireParam(param));
				sb.append(MiapeXmlUtil.TERM_SEPARATOR);
			}
		}
		return sb.toString();
	}

	@Override
	public String getURI() {
		return softwareXML.getUri();
	}

	@Override
	public String getVersion() {
		return softwareXML.getVersion();
	}

}
