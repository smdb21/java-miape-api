package org.proteored.miapeapi.xml.mzidentml;

import java.util.ArrayList;
import java.util.List;

import org.proteored.miapeapi.cv.Accession;
import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.OLSOboControlVocabularyManager;
import org.proteored.miapeapi.cv.msi.PeptideModificationName;
import org.proteored.miapeapi.interfaces.msi.PeptideModification;
import org.proteored.miapeapi.xml.mzidentml.autogenerated.FuGECommonOntologyCvParamType;
import org.proteored.miapeapi.xml.mzidentml.autogenerated.FuGECommonOntologyParamType;
import org.proteored.miapeapi.xml.mzidentml.autogenerated.ObjectFactory;
import org.proteored.miapeapi.xml.mzidentml.autogenerated.PSIPIPolypeptideModificationType;
import org.proteored.miapeapi.xml.mzidentml.autogenerated.PSIPIPolypeptideSubstitutionModificationType;
import org.proteored.miapeapi.xml.mzidentml.util.MzidentmlControlVocabularyXmlFactory;
import org.proteored.miapeapi.xml.mzidentml.util.Utils;
import org.proteored.miapeapi.xml.util.CVUtils;

/**
 * Peptide modification from mzIdentML. It can be parsed from a
 * {@link PSIPIPolypeptideModificationType} or from a
 * {@link PSIPIPolypeptideSubstitutionModificationType}
 * 
 * @author Salvador
 * 
 */
public class PeptideModificationImpl implements PeptideModification {
	private final PSIPIPolypeptideModificationType xmlModification;
	private final PSIPIPolypeptideSubstitutionModificationType xmlSubstitutionModification;
	private final MzidentmlControlVocabularyXmlFactory cvUtil;

	public PeptideModificationImpl(PSIPIPolypeptideModificationType xmlModification,
			MzidentmlControlVocabularyXmlFactory cvUtil) {
		this.xmlModification = xmlModification;
		this.xmlSubstitutionModification = null;
		this.cvUtil = cvUtil;
	}

	public PeptideModificationImpl(
			PSIPIPolypeptideSubstitutionModificationType substitutionModification,
			MzidentmlControlVocabularyXmlFactory cvUtil) {
		this.xmlSubstitutionModification = substitutionModification;
		this.xmlModification = null;
		this.cvUtil = cvUtil;
	}

	@Override
	public Double getAvgDelta() {
		if (xmlModification != null)
			return xmlModification.getAvgMassDelta();
		if (xmlSubstitutionModification != null)
			return xmlSubstitutionModification.getAvgMassDelta();
		return null;
	}

	@Override
	public Double getMonoDelta() {
		if (xmlModification != null)
			return xmlModification.getMonoisotopicMassDelta();
		if (xmlSubstitutionModification != null)
			return xmlSubstitutionModification.getMonoisotopicMassDelta();
		return null;
	}

	@Override
	public String getName() {
		if (xmlModification != null) {
			return getModificationName(xmlModification.getParamGroup());
		}
		if (xmlSubstitutionModification != null) {
			return Utils.SUBSTITUTION_MODIFICATION;
		}
		return null;
	}

	public static void main(String[] args) {
		List<FuGECommonOntologyParamType> paramGroup = new ArrayList<FuGECommonOntologyParamType>();
		FuGECommonOntologyCvParamType cvParam = new FuGECommonOntologyCvParamType();
		cvParam.setName("Phospho");
		cvParam.setAccession("UNIMOD:21");
		cvParam.setCvRef("UNIMOD");
		paramGroup.add(cvParam);
		FuGECommonOntologyCvParamType cvParam2 = new FuGECommonOntologyCvParamType();
		cvParam2.setName("fragment neutral loss");
		cvParam2.setAccession("MS:1001524");
		cvParam2.setCvRef("PSI-MS");
		cvParam2.setUnitAccession("UO:0000221");
		cvParam2.setValue("0");
		cvParam2.setUnitCvRef("UO");
		cvParam2.setUnitName("dalton");
		paramGroup.add(cvParam2);

		System.out.println(getModificationNameTest(paramGroup));
	}

	private static String getModificationNameTest(List<FuGECommonOntologyParamType> paramGroup) {
		ControlVocabularyManager cvManager = new OLSOboControlVocabularyManager();
		MzidentmlControlVocabularyXmlFactory cvUtil = new MzidentmlControlVocabularyXmlFactory(
				new ObjectFactory(), cvManager);
		boolean isNeutralLoss = false;

		if (paramGroup != null && paramGroup.size() > 0) {
			for (FuGECommonOntologyParamType fuGECommonOntologyParamType : paramGroup) {

				if (fuGECommonOntologyParamType instanceof FuGECommonOntologyCvParamType) {
					FuGECommonOntologyCvParamType cvParam = (FuGECommonOntologyCvParamType) fuGECommonOntologyParamType;
					Accession accession = new Accession(cvParam.getAccession());
					// if it is a cv for neutral loss, only take the name if no
					// other cvTerm is available
					if (accession.equals(PeptideModificationName.FRAGMENT_NEUTRAL_LOSS_ACCESSION))
						isNeutralLoss = true;
					else {
						return cvParam.getName();
						// if (cvUtil.isCV(accession,
						// PeptideModificationName.getInstance(cvManager))) {
						// ControlVocabularyTerm termForAccession = cvManager
						// .getCVTermByAccession(accession,
						// PeptideModificationName.getInstance(cvManager));
						// if (termForAccession != null)
						// return termForAccession.getPreferredName();
						// return cvParam.getName();
						//
						// }
					}
				}
				if (cvUtil.isCV(fuGECommonOntologyParamType.getName(),
						PeptideModificationName.getInstance(cvManager))) {
					return fuGECommonOntologyParamType.getName();
				}
			}
			// the the code flow is here, check if there was a neutral loss and
			// no other cvTerm more, and return it
			if (isNeutralLoss)
				cvManager.getControlVocabularyName(
						PeptideModificationName.FRAGMENT_NEUTRAL_LOSS_ACCESSION,
						PeptideModificationName.getInstance(cvManager));
		}
		return null;

	}

	private String getModificationName(List<FuGECommonOntologyParamType> paramGroup) {
		boolean isNeutralLoss = false;
		ControlVocabularyManager cvManager = this.cvUtil.getCvManager();
		if (paramGroup != null && paramGroup.size() > 0) {
			for (FuGECommonOntologyParamType fuGECommonOntologyParamType : paramGroup) {

				if (fuGECommonOntologyParamType instanceof FuGECommonOntologyCvParamType) {
					FuGECommonOntologyCvParamType cvParam = (FuGECommonOntologyCvParamType) fuGECommonOntologyParamType;
					Accession accession = new Accession(cvParam.getAccession());
					// if it is a cv for neutral loss, only take the name if no
					// other cvTerm is available
					if (accession.equals(PeptideModificationName.FRAGMENT_NEUTRAL_LOSS_ACCESSION))
						isNeutralLoss = true;
					else {
						return cvParam.getName();
						// if (cvUtil.isCV(accession,
						// PeptideModificationName.getInstance(cvManager))) {
						// ControlVocabularyTerm termForAccession = cvManager
						// .getCVTermByAccession(accession,
						// PeptideModificationName.getInstance(cvManager));
						// if (termForAccession != null)
						// return termForAccession.getPreferredName();
						// return cvParam.getName();
						//
						// }
					}
				}
				if (cvUtil.isCV(fuGECommonOntologyParamType.getName(),
						PeptideModificationName.getInstance(cvManager))) {
					return fuGECommonOntologyParamType.getName();
				}
			}
			// the the code flow is here, check if there was a neutral loss and
			// no other cvTerm more, and return it
			if (isNeutralLoss)
				cvManager.getControlVocabularyName(
						PeptideModificationName.FRAGMENT_NEUTRAL_LOSS_ACCESSION,
						PeptideModificationName.getInstance(cvManager));
		}
		return null;

	}

	@Override
	public int getPosition() {
		if (xmlModification != null) {
			Integer location = xmlModification.getLocation();
			if (location != null)
				return location;
		}
		if (xmlSubstitutionModification != null) {
			Integer location = xmlSubstitutionModification.getLocation();
			if (location != null)
				return location;
		}
		return -1;
	}

	@Override
	public String getResidues() {
		StringBuilder sb = new StringBuilder();
		if (xmlModification != null) {
			List<String> residues = xmlModification.getResidues();
			if (residues != null) {
				for (String xmlResidue : residues) {
					sb.append(xmlResidue);
				}
				return sb.toString();
			}
		}
		if (xmlSubstitutionModification != null) {
			return xmlSubstitutionModification.getOriginalResidue();
		}
		return null;
	}

	@Override
	public String getReplacementResidue() {
		if (xmlSubstitutionModification != null)
			return xmlSubstitutionModification.getReplacementResidue();
		return null;
	}

	@Override
	public Double getNeutralLoss() {
		if (xmlModification != null) {
			final List<FuGECommonOntologyParamType> paramGroup = xmlModification.getParamGroup();
			if (paramGroup != null) {
				for (FuGECommonOntologyParamType fuGECommonOntologyParamType : paramGroup) {
					if (isNeutralLoss(fuGECommonOntologyParamType)) {
						try {
							return Double.valueOf(fuGECommonOntologyParamType.getValue());
						} catch (Exception e) {
							return null;
						}
					}
				}
			}
		}
		return null;

	}

	private boolean isNeutralLoss(FuGECommonOntologyParamType param) {
		return CVUtils.isThisCV(param,
				PeptideModificationName.FRAGMENT_NEUTRAL_LOSS_ACCESSION.toString());
	}

	@Override
	public String getModificationEvidence() {
		// TODO Auto-generated method stub
		return null;
	}

}
