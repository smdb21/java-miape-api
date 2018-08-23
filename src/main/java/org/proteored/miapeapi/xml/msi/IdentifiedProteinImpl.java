package org.proteored.miapeapi.xml.msi;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.proteored.miapeapi.interfaces.msi.IdentifiedPeptide;
import org.proteored.miapeapi.interfaces.msi.IdentifiedProtein;
import org.proteored.miapeapi.interfaces.msi.ProteinScore;
import org.proteored.miapeapi.xml.msi.autogenerated.MSIIdentifiedProtein;
import org.proteored.miapeapi.xml.msi.autogenerated.ParamType;
import org.proteored.miapeapi.xml.msi.autogenerated.Ref;
import org.proteored.miapeapi.xml.msi.util.MSIControlVocabularyXmlFactory;
import org.proteored.miapeapi.xml.util.MiapeXmlUtil;

import gnu.trove.set.hash.THashSet;

public class IdentifiedProteinImpl implements IdentifiedProtein {
	private final MSIIdentifiedProtein identifiedProteinXML;
	private String description;
	private int id = -1;
	private final ArrayList<IdentifiedPeptide> peptides = new ArrayList<IdentifiedPeptide>();
	// private final int id;

	public IdentifiedProteinImpl(MSIIdentifiedProtein protein) {
		identifiedProteinXML = protein;
		description = identifiedProteinXML.getDescription();
		// id=MiapeXmlUtil.getIdFromXMLId(identifiedProteinXML.getId());'
		// id = MiapeXmlUtil.ProteinCounter.increaseCounter();
		// protein.setId(MiapeXmlUtil.IdentifierPrefixes.PROTEIN.getPrefix() +
		// this.id);
	}

	@Override
	public String getAccession() {
		return identifiedProteinXML.getAC();
	}

	@Override
	public String getAdditionalInformation() {
		return identifiedProteinXML.getAInformation();
	}

	@Override
	public String getCoverage() {
		return identifiedProteinXML.getCoverage();
	}

	@Override
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public List<IdentifiedPeptide> getIdentifiedPeptides() {
		// if (this.peptides == null) {
		// this.peptides = new ArrayList<IdentifiedPeptide>();
		// List<MSIIdentifiedPeptide> xmlPeptides = mapProteinPeptides
		// .get(identifiedProteinXML.getId());
		// if (xmlPeptides != null) {
		// for (MSIIdentifiedPeptide xmlIdentifiedPeptide : xmlPeptides) {
		// peptides.add(new IdentifiedPeptideImpl(
		// xmlIdentifiedPeptide, mapInputData,
		// mapPeptideProteins, mapProteinPeptides));
		// }
		// }
		// }
		return peptides;
	}

	@Override
	public String getPeaksMatchedNumber() {
		return identifiedProteinXML.getMatched();
	}

	@Override
	public String getPeptideNumber() {
		return identifiedProteinXML.getPeptideNumber();
	}

	@Override
	public Set<ProteinScore> getScores() {
		final Set<ProteinScore> proteinScores = new THashSet<ProteinScore>();
		final List<ParamType> msiproteinScore = identifiedProteinXML.getProteinScore();
		if (msiproteinScore != null) {
			for (final ParamType xmlScore : msiproteinScore) {
				proteinScores.add(new ProteinScoreImpl(xmlScore));

			}
		}
		return proteinScores;
	}

	@Override
	public String getUnmatchedSignals() {
		return identifiedProteinXML.getUnmatched();
	}

	@Override
	public Boolean getValidationStatus() {
		if (identifiedProteinXML.isValidationStatus() == null) {
			return Boolean.valueOf(null);
		}
		return identifiedProteinXML.isValidationStatus();

	}

	@Override
	public String getValidationType() {
		if (identifiedProteinXML == null)
			return null;
		return MSIControlVocabularyXmlFactory.getName(identifiedProteinXML.getValidationType());
	}

	@Override
	public String getValidationValue() {
		return identifiedProteinXML.getValidationValue();
	}

	@Override
	public int getId() {
		if (id == -1) {
			id = MiapeXmlUtil.getIdFromXMLId(identifiedProteinXML.getId());
		}
		return id;
	}

	public List<Ref> getPeptideRefs() {
		if (identifiedProteinXML != null) {
			if (identifiedProteinXML.getPeptideRefs() != null)
				return identifiedProteinXML.getPeptideRefs().getRef();
		}
		return null;
	}

	public void addPeptideRelationship(IdentifiedPeptide identifiedPeptide) {
		if (identifiedPeptide == null) {
			return;
		}
		for (final IdentifiedPeptide peptide : peptides) {
			if (peptide.getId() == identifiedPeptide.getId())
				return;
		}
		peptides.add(identifiedPeptide);

	}
}
