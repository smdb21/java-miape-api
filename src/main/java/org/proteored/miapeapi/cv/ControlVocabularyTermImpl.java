package org.proteored.miapeapi.cv;

import psidev.psi.tools.ontology_manager.interfaces.OntologyTermI;

public class ControlVocabularyTermImpl implements ControlVocabularyTerm {
	private final String preferredName;
	private final Accession termAccession;

	public ControlVocabularyTermImpl(OntologyTermI oTerm) {
		if (oTerm == null) {
			this.preferredName = null;
			this.termAccession = null;
		} else {
			this.preferredName = oTerm.getPreferredName();
			this.termAccession = new Accession(oTerm.getTermAccession());
		}
	}

	public ControlVocabularyTermImpl(Accession accession, String preferredName) {

		this.preferredName = preferredName;
		this.termAccession = accession;

	}

	@Override
	public String getPreferredName() {
		return this.preferredName;
	}

	@Override
	public Accession getTermAccession() {
		return this.termAccession;
	}

	@Override
	public String getCVRef() {
		return this.termAccession.getCvRef();
	}

}
