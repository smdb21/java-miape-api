package org.proteored.miapeapi.xml.mzml.adapter;

import java.util.List;

import org.proteored.miapeapi.interfaces.Adapter;
import org.proteored.miapeapi.interfaces.ms.MSAdditionalInformation;
import org.proteored.miapeapi.interfaces.ms.MiapeMSDocument;

import uk.ac.ebi.jmzml.model.mzml.FileDescription;
import uk.ac.ebi.jmzml.model.mzml.ParamGroup;

public class FileDescriptionAdapter implements Adapter<FileDescription> {
	private final MiapeMSDocument miapeMS;

	public FileDescriptionAdapter(MiapeMSDocument miapeMS) {
		this.miapeMS = miapeMS;
	}

	@Override
	public FileDescription adapt() {
		FileDescription ret = new FileDescription();

		ret.setFileContent(createFileContent());

		return ret;
	}

	private ParamGroup createFileContent() {
		ParamGroup ret = new ParamGroup();

		if (this.miapeMS.getAdditionalInformations() != null) {
			parseAdditionalInformations(ret, this.miapeMS.getAdditionalInformations());
		}

		return ret;
	}

	private void parseAdditionalInformations(ParamGroup ret, List<MSAdditionalInformation> additionalInformations) {
		// if I found
		/*
		 * OntologyManager om = new OntologyManager(); String ontologyID; final
		 * OntologyAccess ontologyAccess = om.getOntologyAccess(ontologyID);
		 * ontologyAccess.g
		 */
	}
}
