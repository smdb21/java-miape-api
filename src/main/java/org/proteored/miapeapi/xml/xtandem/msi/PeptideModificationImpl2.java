package org.proteored.miapeapi.xml.xtandem.msi;

import java.math.BigDecimal;
import java.util.Map;
import java.util.StringTokenizer;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.interfaces.msi.PeptideModification;

import de.proteinms.xtandemparser.xtandem.FixedModification;
import de.proteinms.xtandemparser.xtandem.InputParams;
import de.proteinms.xtandemparser.xtandem.VariableModification;

public class PeptideModificationImpl2 implements PeptideModification {

	private PeptideModificationImpl modificationImpl;
	private final InputParams iInputParams;

	public PeptideModificationImpl2(String modKey, Map<String, String> rawModMap, Integer domainStart,
			InputParams iInputParams, ControlVocabularyManager cvManager) {
		this.iInputParams = iInputParams;
		// Get the specific parameters for the modification
		String rawModString = rawModMap.get("name" + modKey);
		double modMass = Double.parseDouble(rawModMap.get("modified" + modKey).toString());
		String modLocation = rawModMap.get("at" + modKey).toString();
		String aminoAcidSubstituted = null;

		if (rawModMap.get("pm" + modKey) != null) {
			aminoAcidSubstituted = rawModMap.get("pm" + modKey).toString();
		}
		modificationImpl = null;
		// Check for fixed modification
		if (isFixedModificationInput(modMass)) {

			// Create an instance of a fixed modification.
			FixedModification fixedMod = new FixedModification(rawModString, modMass, modLocation, 0,
					aminoAcidSubstituted != null, aminoAcidSubstituted);
			modificationImpl = new PeptideModificationImpl(fixedMod, domainStart, cvManager);

		} else if (isVariableModificationInput(modMass)) {

			// The rest will be assumed to be variable modifications.
			VariableModification varMod = new VariableModification(rawModString, modMass, modLocation, 0,
					aminoAcidSubstituted != null, aminoAcidSubstituted);
			modificationImpl = new PeptideModificationImpl(varMod, domainStart, cvManager);

		} else {

			// not found as fixed or variable, assumed variable. means that it's
			// in the residue, modification mass [1-n]

			// The rest will be assumed to be variable modifications.
			VariableModification varMod = new VariableModification(rawModString, modMass, modLocation, 0,
					aminoAcidSubstituted != null, aminoAcidSubstituted);
			modificationImpl = new PeptideModificationImpl(varMod, domainStart, cvManager);

		}

	}

	@Override
	public String getName() {
		return modificationImpl.getName();
	}

	@Override
	public int getPosition() {
		return modificationImpl.getPosition();
	}

	@Override
	public String getResidues() {
		return modificationImpl.getResidues();
	}

	@Override
	public Double getMonoDelta() {
		return modificationImpl.getMonoDelta();
	}

	@Override
	public Double getAvgDelta() {
		return modificationImpl.getAvgDelta();
	}

	@Override
	public String getReplacementResidue() {
		return modificationImpl.getReplacementResidue();
	}

	@Override
	public Double getNeutralLoss() {
		return modificationImpl.getNeutralLoss();
	}

	@Override
	public String getModificationEvidence() {
		return modificationImpl.getModificationEvidence();
	}

	/**
	 * Checks if a given modification mass is given in the fixed modification
	 * input parameter section: --> label="residue, modification mass">
	 * 
	 * @param aModMass
	 * @return boolean
	 */
	private boolean isFixedModificationInput(double aModMass) {

		BigDecimal modMass = new BigDecimal(aModMass);
		modMass = modMass.setScale(2, BigDecimal.ROUND_HALF_UP);
		String modificationMasses = iInputParams.getResidueModMass();

		if (modificationMasses != null) {

			StringTokenizer tokenizer = new StringTokenizer(modificationMasses, ",");

			while (tokenizer.hasMoreTokens()) {
				String[] tokens = tokenizer.nextToken().split("@");
				BigDecimal inputMass = new BigDecimal(new Double(tokens[0]));
				inputMass = inputMass.setScale(2, BigDecimal.ROUND_HALF_UP);
				if (modMass.equals(inputMass)) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Checks if a given modification mass is given in the variable modification
	 * input parameter section: --> label="residue, potential modification
	 * mass", or in the refine section: label="refine, potential modification
	 * mass"
	 * 
	 * @param aModMass
	 * @return boolean
	 */
	private boolean isVariableModificationInput(double aModMass) {

		BigDecimal modMass = new BigDecimal(aModMass);
		modMass = modMass.setScale(3, BigDecimal.ROUND_HALF_UP);
		String modificationMasses = iInputParams.getResiduePotModMass();
		String refineModificationMasses = iInputParams.getRefineModMass();

		if (modificationMasses != null) {

			StringTokenizer tokenizer = new StringTokenizer(modificationMasses, ",");
			while (tokenizer.hasMoreTokens()) {
				String[] tokens = tokenizer.nextToken().split("@");
				BigDecimal inputMass = new BigDecimal(new Double(tokens[0]));
				inputMass = inputMass.setScale(3, BigDecimal.ROUND_HALF_UP);
				if (modMass.equals(inputMass)) {
					return true;
				}
			}
		}

		if (refineModificationMasses != null) {

			StringTokenizer tokenizer2 = new StringTokenizer(refineModificationMasses, ",");

			while (tokenizer2.hasMoreTokens()) {
				String[] tokens = tokenizer2.nextToken().split("@");
				BigDecimal inputMass = new BigDecimal(new Double(tokens[0]));
				inputMass = inputMass.setScale(2, BigDecimal.ROUND_HALF_UP);
				if (modMass.equals(inputMass)) {
					return true;
				}
			}
		}

		return false;
	}

}
