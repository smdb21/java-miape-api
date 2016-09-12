package org.proteored.miapeapi.cv;


public class ControlVocabularyTermImpl2 implements ControlVocabularyTerm {
	private final String preferredName;
	private final Accession termAccession;
	private final String cvRef;

	public ControlVocabularyTermImpl2(Accession accession, String preferredName, String cvRef) {

		this.preferredName = preferredName;
		this.termAccession = accession;
		this.cvRef = cvRef;

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
		return this.cvRef;
	}

}
