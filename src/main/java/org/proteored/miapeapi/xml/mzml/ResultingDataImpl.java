package org.proteored.miapeapi.xml.mzml;

import java.util.List;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ms.ChromatogramAttribute;
import org.proteored.miapeapi.cv.ms.ChromatogramType;
import org.proteored.miapeapi.cv.ms.MSFileType;
import org.proteored.miapeapi.interfaces.ms.ResultingData;
import org.proteored.miapeapi.xml.mzml.util.MzMLControlVocabularyXmlFactory;

import uk.ac.ebi.jmzml.model.mzml.CVParam;
import uk.ac.ebi.jmzml.model.mzml.Chromatogram;
import uk.ac.ebi.jmzml.model.mzml.ParamGroup;
import uk.ac.ebi.jmzml.model.mzml.ReferenceableParamGroupList;
import uk.ac.ebi.jmzml.model.mzml.SourceFile;

public class ResultingDataImpl implements ResultingData {

	private String name = null;
	private String location = null;
	private String fileType = null;
	private String SRMDescriptor = null;
	private String SRMType = null;
	private String SRMURI = null;
	private String additionalURI = null;

	public ResultingDataImpl(SourceFile sourceFile,
			ReferenceableParamGroupList referenceableParamGroupList,
			ControlVocabularyManager cvManager) {
		setNull();
		if (sourceFile != null) {
			name = sourceFile.getName();
			if (name == null || "".equals(name))
				name = "Resulting data";
			location = sourceFile.getLocation();
			ParamGroup paramGroup = MzMLControlVocabularyXmlFactory.createParamGroup(
					sourceFile.getCvParam(), sourceFile.getUserParam(),
					sourceFile.getReferenceableParamGroupRef());
			fileType = MzMLControlVocabularyXmlFactory.getValueFromParamGroup(paramGroup,
					referenceableParamGroupList, MSFileType.getInstance(cvManager));
		}
	}

	public ResultingDataImpl(Chromatogram chromatogram,
			ReferenceableParamGroupList referenceableParamGroupList,
			ControlVocabularyManager cvManager) {
		setNull();
		if (chromatogram != null) {
			if (chromatogram.getId() != null && !"".equals(chromatogram.getId()))
				this.name = chromatogram.getId();
			else
				this.name = "chromatogram";
			// create paramGroup
			ParamGroup chromatogramParamGroup = MzMLControlVocabularyXmlFactory.createParamGroup(
					chromatogram.getCvParam(), chromatogram.getUserParam(),
					chromatogram.getReferenceableParamGroupRef());

			// chromatogram type
			final List<CVParam> chromatogramTypeCVs = MzMLControlVocabularyXmlFactory
					.getCvsFromParamGroup(chromatogramParamGroup, referenceableParamGroupList,
							ChromatogramType.getInstance(cvManager));
			if (chromatogramTypeCVs != null) {
				this.SRMType = MzMLControlVocabularyXmlFactory.parseAllCvParams(
						chromatogramTypeCVs, null);
			}
			// srm descriptor
			final List<CVParam> chromatogramAttrCVs = MzMLControlVocabularyXmlFactory
					.getCvsFromParamGroup(chromatogramParamGroup, referenceableParamGroupList,
							ChromatogramAttribute.getInstance(cvManager));
			if (chromatogramAttrCVs != null) {
				this.SRMDescriptor = MzMLControlVocabularyXmlFactory.parseAllCvParams(
						chromatogramAttrCVs, null);
			}
			// SRM URI point to the mzML file
			this.SRMURI = "See attached file in section 1";

		}
	}

	public void setName(String name) {
		this.name = name;
	}

	private void setNull() {
		this.name = null;
		this.location = null;
		this.additionalURI = null;
		this.fileType = null;
		this.SRMDescriptor = null;
		this.SRMType = null;
		this.SRMURI = null;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getDataFileUri() {
		return location;
	}

	@Override
	public String getDataFileType() {
		return fileType;
	}

	@Override
	public String getAdditionalUri() {
		return additionalURI;
	}

	@Override
	public String getSRMUri() {
		return SRMURI;
	}

	@Override
	public String getSRMType() {
		return SRMType;
	}

	@Override
	public String getSRMDescriptor() {
		return SRMDescriptor;
	}

	/*
	 * @Override public List<SpectrumDescription> getSpectrumDescriptions() { //
	 * TODO Auto-generated method stub return null; }
	 */

}
