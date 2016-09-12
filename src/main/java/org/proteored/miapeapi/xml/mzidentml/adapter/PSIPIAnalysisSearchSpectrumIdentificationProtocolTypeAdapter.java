package org.proteored.miapeapi.xml.mzidentml.adapter;

import org.proteored.miapeapi.interfaces.Adapter;
import org.proteored.miapeapi.interfaces.msi.IdentifiedProteinSet;
import org.proteored.miapeapi.xml.mzidentml.autogenerated.ObjectFactory;
import org.proteored.miapeapi.xml.mzidentml.autogenerated.PSIPIAnalysisSearchSpectrumIdentificationProtocolType;
import org.proteored.miapeapi.xml.util.MiapeXmlUtil;

public class PSIPIAnalysisSearchSpectrumIdentificationProtocolTypeAdapter implements
Adapter<PSIPIAnalysisSearchSpectrumIdentificationProtocolType> {
	private final IdentifiedProteinSet proteinSet;
	private final ObjectFactory factory;
	public PSIPIAnalysisSearchSpectrumIdentificationProtocolTypeAdapter(
			IdentifiedProteinSet proteinSet, ObjectFactory factory) {
		this.proteinSet = proteinSet;
		this.factory = factory;
	}

	@Override
	public PSIPIAnalysisSearchSpectrumIdentificationProtocolType adapt() {
		PSIPIAnalysisSearchSpectrumIdentificationProtocolType searchSpectrumProtocol = factory.createPSIPIAnalysisSearchSpectrumIdentificationProtocolType();
		// Spectrum identification software
		if (proteinSet.getInputParameter() != null) {
			if (proteinSet.getInputParameter().getSoftware() != null) {
				searchSpectrumProtocol.setAnalysisSoftwareRef(MiapeXmlUtil.IdentifierPrefixes.SOFTWARE.getPrefix() + proteinSet.getInputParameter().getSoftware().getId());
			}
			//searchSpectrumProtocol.set
		}


		return searchSpectrumProtocol;
	}

}
