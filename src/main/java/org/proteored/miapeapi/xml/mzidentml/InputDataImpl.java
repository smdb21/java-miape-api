package org.proteored.miapeapi.xml.mzidentml;

import org.proteored.miapeapi.interfaces.msi.InputData;
import org.proteored.miapeapi.xml.mzidentml.autogenerated.PSIPISpectraSpectraDataType;
import org.proteored.miapeapi.xml.mzidentml.util.MzidentmlControlVocabularyXmlFactory;
import org.proteored.miapeapi.xml.mzidentml.util.Utils;
import org.proteored.miapeapi.xml.util.MiapeXmlUtil;

public class InputDataImpl implements InputData {
	private final PSIPISpectraSpectraDataType spectraData;
	private final Integer identifier;
	public InputDataImpl(PSIPISpectraSpectraDataType spectraData, Integer identifier) {
		this.spectraData = spectraData;
		this.identifier = identifier;
	}

	@Override
	public String getDescription() {
		if (spectraData.getExternalFormatDocumentation() != null)
			return Utils.EXTERNAL_FORMAT_DOCUMENTATION + MiapeXmlUtil.TERM_SEPARATOR + spectraData.getExternalFormatDocumentation();
		return null;
	}

	@Override
	public String getMSFileType() {
		if (spectraData.getFileFormat() != null) {
			return MzidentmlControlVocabularyXmlFactory.readEntireParam(spectraData.getFileFormat().getCvParam());
		}
		return null;
	}

	@Override
	public String getName() {
		if (spectraData.getName() != null) {
			return spectraData.getName(); 
		}
		return spectraData.getId(); 
	}

	@Override
	public String getSourceDataUrl() {
		return spectraData.getLocation();
	}



	@Override
	public int getId() {
		if (identifier != null)
			return identifier;
		return -1;
	}

}
