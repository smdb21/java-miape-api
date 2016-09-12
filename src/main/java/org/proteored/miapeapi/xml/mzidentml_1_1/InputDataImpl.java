package org.proteored.miapeapi.xml.mzidentml_1_1;

import org.proteored.miapeapi.interfaces.msi.InputData;
import org.proteored.miapeapi.xml.mzidentml.util.Utils;
import org.proteored.miapeapi.xml.mzidentml_1_1.util.MzidentmlControlVocabularyXmlFactory;
import org.proteored.miapeapi.xml.util.MiapeXmlUtil;

import uk.ac.ebi.jmzidml.model.mzidml.SpectraData;

public class InputDataImpl implements InputData {
	private final SpectraData spectraData;
	private final Integer identifier;

	public InputDataImpl(SpectraData spectraDataXML, Integer identifier) {
		this.spectraData = spectraDataXML;
		this.identifier = identifier;
	}

	@Override
	public String getDescription() {
		if (spectraData.getExternalFormatDocumentation() != null)
			return Utils.EXTERNAL_FORMAT_DOCUMENTATION + MiapeXmlUtil.TERM_SEPARATOR
					+ spectraData.getExternalFormatDocumentation();
		return null;
	}

	@Override
	public String getMSFileType() {
		if (spectraData.getFileFormat() != null) {
			return MzidentmlControlVocabularyXmlFactory.readEntireParam(spectraData.getFileFormat()
					.getCvParam());
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
