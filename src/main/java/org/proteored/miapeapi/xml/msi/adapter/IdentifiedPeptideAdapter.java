package org.proteored.miapeapi.xml.msi.adapter;

import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.proteored.miapeapi.cv.msi.Score;
import org.proteored.miapeapi.interfaces.Adapter;
import org.proteored.miapeapi.interfaces.msi.IdentifiedPeptide;
import org.proteored.miapeapi.interfaces.msi.IdentifiedProtein;
import org.proteored.miapeapi.interfaces.msi.InputData;
import org.proteored.miapeapi.interfaces.msi.PeptideModification;
import org.proteored.miapeapi.interfaces.msi.PeptideScore;
import org.proteored.miapeapi.xml.msi.autogenerated.MSIIdentifiedPeptide;
import org.proteored.miapeapi.xml.msi.autogenerated.ObjectFactory;
import org.proteored.miapeapi.xml.msi.autogenerated.ProteinRefs;
import org.proteored.miapeapi.xml.msi.autogenerated.Ref;
import org.proteored.miapeapi.xml.msi.util.MSIControlVocabularyXmlFactory;
import org.proteored.miapeapi.xml.util.MiapeIdentifierCounter;
import org.proteored.miapeapi.xml.util.MiapeXmlUtil;

public class IdentifiedPeptideAdapter implements Adapter<MSIIdentifiedPeptide> {
	private final IdentifiedPeptide identifiedPeptide;
	// private final int proteinRef; // -1 if the peptide has no protein
	// reference
	private final ObjectFactory factory;
	private final MSIControlVocabularyXmlFactory cvFactory;
	private static Logger log = Logger.getLogger("log4j.logger.org.proteored");

	public IdentifiedPeptideAdapter(IdentifiedPeptide identifiedPeptide, ObjectFactory factory,
			MSIControlVocabularyXmlFactory cvFactory) {
		this.factory = factory;
		this.identifiedPeptide = identifiedPeptide;
		// this.proteinRef = proteinRef;
		this.cvFactory = cvFactory;
	}

	@Override
	public MSIIdentifiedPeptide adapt() {
		// log.info("Adapting peptide: " + identifiedPeptide.getSequence());

		final MSIIdentifiedPeptide identifiedPeptideXML = factory.createMSIIdentifiedPeptide();
		String idString = "";
		int id = identifiedPeptide.getId();
		if (id == -1) {
			id = MiapeIdentifierCounter.increaseCounter();
			idString = MiapeXmlUtil.IdentifierPrefixes.PEPTIDE.getPrefix() + id;
		} else {
			idString = MiapeXmlUtil.IdentifierPrefixes.PEPTIDE.getPrefix() + identifiedPeptide.getId();
		}
		identifiedPeptideXML.setId(idString);
		identifiedPeptideXML.setCharge(identifiedPeptide.getCharge());
		identifiedPeptideXML.setMassDeviation(identifiedPeptide.getMassDesviation());
		final Set<PeptideModification> modifications = identifiedPeptide.getModifications();
		if (modifications != null) {
			for (final PeptideModification modification : modifications) {
				identifiedPeptideXML.getMSIPeptideModification()
						.add(new PeptideModificationAdapter(modification, factory, cvFactory).adapt());
			}
		}

		final int rank = identifiedPeptide.getRank();
		if (rank > 0) {
			identifiedPeptideXML.setRank(rank);
		}
		// PRotein References
		final List<IdentifiedProtein> identifiedProteins = identifiedPeptide.getIdentifiedProteins();
		if (identifiedProteins != null && !identifiedProteins.isEmpty()) {
			// log.info("Adapting peptide " + identifiedPeptide.getSequence() +
			// " with "
			// + identifiedPeptide.getIdentifiedProteins().size() +
			// " proteins");

			final ProteinRefs protRefs = factory.createProteinRefs();

			int i = 1;
			for (final IdentifiedProtein protein : identifiedProteins) {
				final Ref ref = factory.createRef();
				ref.setId(MiapeXmlUtil.IdentifierPrefixes.PROTEIN.getPrefix() + protein.getId());
				log.debug("Adapting protein (" + i + "/" + identifiedProteins.size() + ") " + protein.getAccession());
				protRefs.getRef().add(ref);
				i++;
			}
			if (!protRefs.getRef().isEmpty()) {
				identifiedPeptideXML.setProteinRefs(protRefs);
			} else {
				log.warn("Peptide with no proteins!");
			}
		}

		final String spectrumRef = identifiedPeptide.getSpectrumRef();
		if (spectrumRef != null) {
			identifiedPeptideXML.setSpectrumRef(spectrumRef);
		}

		final Set<PeptideScore> scores = identifiedPeptide.getScores();
		if (scores != null) {
			for (final PeptideScore peptideScore : scores) {
				identifiedPeptideXML.getPeptideScore().add(cvFactory.createCV(peptideScore.getName(),
						peptideScore.getValue(), Score.getInstance(cvFactory.getCvManager())));
			}
		}

		identifiedPeptideXML.setSequence(identifiedPeptide.getSequence());
		final InputData inputData = identifiedPeptide.getInputData();
		if (inputData != null) {
			identifiedPeptideXML
					.setInputDataRef(MiapeXmlUtil.IdentifierPrefixes.INPUTDATA.getPrefix() + inputData.getId());
		}
		try {
			identifiedPeptideXML.setRT(Double.valueOf(identifiedPeptide.getRetentionTimeInSeconds()));
		} catch (final Exception e) {
			// do nothing
		}
		return identifiedPeptideXML;
	}

}
