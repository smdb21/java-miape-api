package org.proteored.miapeapi.xml.xtandem.msi;

import java.io.IOException;
import java.net.URL;

import org.proteored.miapeapi.cv.Accession;
import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularyTerm;
import org.proteored.miapeapi.cv.msi.PeptideModificationName;
import org.proteored.miapeapi.interfaces.msi.PeptideModification;
import org.springframework.core.io.ClassPathResource;

import uk.ac.ebi.pridemod.PrideModController;
import uk.ac.ebi.pridemod.slimmod.model.SlimModCollection;
import uk.ac.ebi.pridemod.slimmod.model.SlimModification;
import de.proteinms.xtandemparser.interfaces.Modification;

public class PeptideModificationImpl implements PeptideModification {
	private static SlimModCollection preferredModifications;
	private final Modification modification;
	private String residue;
	private final Integer domainStart;
	private final ControlVocabularyManager cvManager;
	private final ClassPathResource resource = new ClassPathResource(
			"modification_mappings.xml");

	public PeptideModificationImpl(Modification modification,
			Integer domainStart, ControlVocabularyManager cvManager) {
		this.modification = modification;

		this.domainStart = domainStart;
		this.cvManager = cvManager;
		if (modification != null) {
			if (modification.getName().contains("@")) {
				String tmp[] = modification.getName().split("@");
				residue = tmp[1];
			}
		}

	}

	@Override
	public String getName() {
		return getModificationNameFromResidueAndMass();
	}

	private String getModificationNameFromResidueAndMass() {
		try {
			// try first with the PRIDE mapping
			SlimModCollection modificationMapping = getModificationMapping();
			SlimModification slimMod = modificationMapping
					.getbyName(modification.getName());
			if (slimMod != null) {
				return slimMod.getPsiModDesc();
			}
			SlimModCollection slimMods = modificationMapping.getbyDelta(
					modification.getMass(), 0.001);
			if (slimMods != null && !slimMods.isEmpty()) {
				return slimMods.get(0).getPsiModDesc();
			}
			// TODO add more modifications!
			// read from a file?
			if (getResidues().equals("C")
					&& compareWithError(getMonoDelta(), 57.022)) {
				final ControlVocabularyTerm cvTerm = cvManager
						.getCVTermByAccession(new Accession(
								PeptideModificationName.UNIMOD4),
								PeptideModificationName.getInstance(cvManager));
				if (cvTerm != null)
					return cvTerm.getPreferredName();
				else
					return "Carbamidomethyl";
			}
			if (getResidues().equals("E")
					&& compareWithError(getMonoDelta(), -18.0106)) {
				final ControlVocabularyTerm cvTerm = cvManager
						.getCVTermByAccession(new Accession(
								PeptideModificationName.UNIMOD27),
								PeptideModificationName.getInstance(cvManager));
				if (cvTerm != null)
					return cvTerm.getPreferredName();
				else
					return "Glu->pyro-Glu";
			}
			final ControlVocabularyTerm pepModifDetailsTerm = PeptideModificationName
					.getPepModifDetailsTerm(cvManager);
			if (pepModifDetailsTerm != null)
				return pepModifDetailsTerm.getPreferredName();
		} catch (Exception e) {

		}
		return "peptide modification details";
	}

	private boolean compareWithError(double num1, double num2) {
		double tolerance = 0.001;
		if (num1 > num2)
			if (num1 - num2 < tolerance)
				return true;
		if (num2 > num1)
			if (num2 - num1 < tolerance)
				return true;
		if (num1 == num2)
			return true;
		return false;
	}

	@Override
	public int getPosition() {

		try {
			return Integer.parseInt(modification.getLocation()) - domainStart
					+ 1;
		} catch (NumberFormatException e) {
			return -1;
		}
	}

	@Override
	public String getResidues() {
		return residue;
	}

	@Override
	public Double getMonoDelta() {
		return modification.getMass();
	}

	@Override
	public Double getAvgDelta() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getReplacementResidue() {
		if (modification.isSubstitution())
			return modification.getSubstitutedAminoAcid();
		return null;
	}

	@Override
	public Double getNeutralLoss() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getModificationEvidence() {
		// TODO Auto-generated method stub
		return null;
	}

	private SlimModCollection getModificationMapping() {
		if (PeptideModificationImpl.preferredModifications == null) {
			URL url;
			try {
				url = resource.getURL();
				PeptideModificationImpl.preferredModifications = PrideModController
						.parseSlimModCollection(url);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return PeptideModificationImpl.preferredModifications;
	}
}
