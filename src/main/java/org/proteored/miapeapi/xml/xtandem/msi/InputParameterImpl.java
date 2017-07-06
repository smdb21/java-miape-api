package org.proteored.miapeapi.xml.xtandem.msi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.msi.SearchType;
import org.proteored.miapeapi.interfaces.Software;
import org.proteored.miapeapi.interfaces.msi.AdditionalParameter;
import org.proteored.miapeapi.interfaces.msi.Database;
import org.proteored.miapeapi.interfaces.msi.InputParameter;
import org.proteored.miapeapi.xml.util.MiapeXmlUtil;

import de.proteinms.xtandemparser.parser.XTandemParser;
import de.proteinms.xtandemparser.xtandem.InputParams;
import de.proteinms.xtandemparser.xtandem.PerformParams;
import de.proteinms.xtandemparser.xtandem.XTandemFile;
import gnu.trove.set.hash.THashSet;

public class InputParameterImpl implements InputParameter {

	private HashMap<String, String> inputParamMap = null;
	private InputParams inputParameters = null;
	private PerformParams performParameters = null;
	private final Set<Database> databases = new THashSet<Database>();
	private Integer identifier = null;
	private Software software = null;
	private final Set<AdditionalParameter> additionalParameters = new THashSet<AdditionalParameter>();
	private final ControlVocabularyManager cvManager;

	public InputParameterImpl(XTandemFile xFile, XTandemParser parser, Integer inputParamID, Software software,
			ControlVocabularyManager cvManager) {
		try {
			inputParamMap = parser.getInputParamMap();
			inputParameters = xFile.getInputParameters();
			performParameters = xFile.getPerformParameters();
			identifier = inputParamID;
			this.software = software;

		} catch (Exception ex) {
			// do nothing
		}
		this.cvManager = cvManager;
		processDatabases();

	}

	private void processDatabases() {
		if (performParameters != null) {
			final String sequenceSource_1 = performParameters.getSequenceSource_1();
			if (sequenceSource_1 != null && !"".equals(sequenceSource_1)) {
				databases.add(new DatabaseImpl(sequenceSource_1, performParameters.getSequenceSourceDescription_1()));
			}
			final String sequenceSource_2 = performParameters.getSequenceSource_2();
			if (sequenceSource_2 != null && !"".equals(sequenceSource_2)) {
				databases.add(new DatabaseImpl(sequenceSource_2, performParameters.getSequenceSourceDescription_2()));
			}
			final String sequenceSource_3 = performParameters.getSequenceSource_3();
			if (sequenceSource_3 != null && !"".equals(sequenceSource_3)) {
				databases.add(new DatabaseImpl(sequenceSource_3, performParameters.getSequenceSourceDescription_3()));
			}
		}

	}

	@Override
	public int getId() {
		if (identifier != null) {
			return identifier;
		}
		return -1;
	}

	@Override
	public String getName() {
		return "XTandem parameters";
	}

	@Override
	public String getTaxonomy() {
		if (inputParameters != null)
			return inputParameters.getProteinTaxon();
		return null;
	}

	@Override
	public String getCleavageRules() {
		if (inputParameters != null)
			return inputParameters.getProteinCleavageSite();
		return null;
	}

	@Override
	public String getMisscleavages() {
		if (inputParameters != null)
			return String.valueOf(inputParameters.getScoringMissCleavageSites());
		return null;
	}

	@Override
	public String getAdditionalCleavages() {
		if (inputParameters != null) {
			StringBuilder sb = new StringBuilder();
			final String proteinC_termCleavMassChange = inputParameters.getProteinC_termCleavMassChange();
			if (proteinC_termCleavMassChange != null && !"".equals(proteinC_termCleavMassChange)) {
				sb.append("protein, cleavage C-terminal mass change=" + proteinC_termCleavMassChange);
			}
			final String proteinN_termCleavMassChange = inputParameters.getProteinN_termCleavMassChange();
			if (proteinN_termCleavMassChange != null && !"".equals(proteinN_termCleavMassChange)) {
				if (!"".equals(sb.toString()))
					sb.append(MiapeXmlUtil.TERM_SEPARATOR);
				sb.append("protein, cleavage N-terminal mass change=" + proteinN_termCleavMassChange);
			}

			if (!"".equals(sb.toString()))
				return sb.toString();
		}
		return null;
	}

	@Override
	public String getAaModif() {

		if (inputParameters != null) {
			StringBuilder sb = new StringBuilder();

			// protein, C-terminal mass change
			final double proteinC_termResModMass = inputParameters.getProteinC_termResModMass();
			if (proteinC_termResModMass != 0) {
				sb.append("protein, C-terminal mass change=" + proteinC_termResModMass);
			}
			// protein, N-terminal mass change
			final double proteinN_termResModMass = inputParameters.getProteinN_termResModMass();
			if (proteinN_termResModMass != 0) {
				if (!"".equals(sb.toString()))
					sb.append(MiapeXmlUtil.TERM_SEPARATOR);
				sb.append("protein, N-terminal mass change=" + proteinN_termResModMass);
			}
			// residue, modification mass
			final ArrayList<String> residueModificationMass = inputParameters.getResidueModificationMass();
			if (residueModificationMass != null && !residueModificationMass.isEmpty()) {
				if (!"".equals(sb.toString()))
					sb.append(MiapeXmlUtil.TERM_SEPARATOR);
				sb.append("residue, modification mass=" + residueModificationMass);
			}
			// residue, potential modification mass
			final String residuePotentialModificationMass = inputParameters.getResiduePotModMass();
			if (residuePotentialModificationMass != null && !"".equals(residuePotentialModificationMass)) {
				if (!"".equals(sb.toString()))
					sb.append(MiapeXmlUtil.TERM_SEPARATOR);
				sb.append("residue, modification mass=" + residuePotentialModificationMass);
			}
			// residue, potential modification motif
			final String residuePotentialModificationMotif = inputParameters.getResiduePotModMotiv();
			if (residuePotentialModificationMotif != null && !"".equals(residuePotentialModificationMotif)) {
				if (!"".equals(sb.toString()))
					sb.append(MiapeXmlUtil.TERM_SEPARATOR);
				sb.append("residue, modification mass=" + residuePotentialModificationMotif);
			}
			// refine, modification mass
			final String refineModMass = inputParameters.getRefineModMass();
			if (!"".equals(sb.toString()))
				sb.append(MiapeXmlUtil.TERM_SEPARATOR);
			sb.append("refine, modification mass=" + refineModMass);

			// refine, potential modification mass
			final ArrayList<String> refinePotentialModificationMass = inputParameters
					.getRefinePotentialModificationMass();
			if (refinePotentialModificationMass != null && !refinePotentialModificationMass.isEmpty()) {
				for (String potentialModMass : refinePotentialModificationMass) {
					if (!"".equals(sb.toString()))
						sb.append(MiapeXmlUtil.TERM_SEPARATOR);
					sb.append("refine, potential modification mass=" + potentialModMass);
				}

			}
			// refine, potential modification motif
			final ArrayList<String> refinePotentialModificationMotif = inputParameters
					.getRefinePotentialModificationMotif();
			if (refinePotentialModificationMotif != null && !refinePotentialModificationMotif.isEmpty()) {
				for (String potentialModMotif : refinePotentialModificationMotif) {
					if (!"".equals(sb.toString()))
						sb.append(MiapeXmlUtil.TERM_SEPARATOR);
					sb.append("refine, potential modification motif=" + potentialModMotif);
				}
			}

			// refine, potential C-terminus modifications
			final String refinePotC_termMods = inputParameters.getRefinePotC_termMods();
			if (refinePotC_termMods != null) {
				if (!"".equals(sb.toString()))
					sb.append(MiapeXmlUtil.TERM_SEPARATOR);
				sb.append("refine, potential C-terminus modifications=" + refinePotC_termMods);
			}
			// refine, potential N-terminus modifications
			final String refinePotN_termMods = inputParameters.getRefinePotN_termMods();
			if (refinePotN_termMods != null) {
				if (!"".equals(sb.toString()))
					sb.append(MiapeXmlUtil.TERM_SEPARATOR);
				sb.append("refine, potential N-terminus modifications=" + refinePotN_termMods);
			}
			if (!"".equals(sb.toString()))
				return sb.toString();
		}
		return null;
	}

	@Override
	public String getMinScore() {
		return "e-value <= " + String.valueOf(inputParameters.getMaxValidExpectValue());
	}

	@Override
	public String getPrecursorMassTolerance() {
		if (inputParameters != null) {
			StringBuilder sb = new StringBuilder();
			final double spectrumParentMonoIsoMassErrorMinus = inputParameters.getSpectrumParentMonoIsoMassErrorMinus();
			if (spectrumParentMonoIsoMassErrorMinus != 0) {
				sb.append("Parent monoisotopic mass error minus=" + spectrumParentMonoIsoMassErrorMinus);
			}
			final double spectrumParentMonoIsoMassErrorPlus = inputParameters.getSpectrumParentMonoIsoMassErrorPlus();
			if (spectrumParentMonoIsoMassErrorPlus != 0) {
				if (!"".equals(sb.toString()))
					sb.append(MiapeXmlUtil.TERM_SEPARATOR);
				sb.append("Parent monoisotopic mass error plus=" + spectrumParentMonoIsoMassErrorPlus);
			}
			if (!"".equals(sb.toString()))
				return sb.toString();
		}
		return null;
	}

	@Override
	public String getPmfMassTolerance() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFragmentMassTolerance() {
		final double spectrumMonoIsoMassError = inputParameters.getSpectrumMonoIsoMassError();
		if (spectrumMonoIsoMassError != 0)
			return String.valueOf(spectrumMonoIsoMassError);
		return null;
	}

	@Override
	public String getPrecursorMassToleranceUnit() {
		if (inputParameters != null) {
			final String spectrumParentMonoIsoMassErrorUnit = inputParameters.getSpectrumParentMonoIsoMassErrorUnits();
			if (spectrumParentMonoIsoMassErrorUnit != null && !"".equals(spectrumParentMonoIsoMassErrorUnit)) {
				return spectrumParentMonoIsoMassErrorUnit;
			}
		}
		return null;
	}

	@Override
	public String getPmfMassToleranceUnit() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFragmentMassToleranceUnit() {
		final String spectrumParentMonoIsoMassErrorUnits = inputParameters.getSpectrumParentMonoIsoMassErrorUnits();
		if (spectrumParentMonoIsoMassErrorUnits != null && !"".equals(spectrumParentMonoIsoMassErrorUnits))
			return spectrumParentMonoIsoMassErrorUnits;
		return null;
	}

	@Override
	public String getNumEntries() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCleavageName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getScoringAlgorithm() {

		// TODO buscar (lo he sacado de
		// http://tools.proteomecenter.org/wiki/index.php?title=TPP:X!Tandem_and_the_TPP)
		// <note label="scoring, algorithm" type="input">k-score</note>
		// <note label="spectrum, use conditioning" type="input">no</note>
		// <note label="scoring, minimum ion count" type="input">1</note>

		return null;
	}

	@Override
	public Set<AdditionalParameter> getAdditionalParameters() {
		if (inputParamMap != null) {
			for (String param : inputParamMap.keySet()) {
				additionalParameters.add(new AdditionalParameterImpl(param, inputParamMap.get(param)));
			}
		}
		if (!additionalParameters.isEmpty())
			return additionalParameters;
		return null;
	}

	@Override
	public Set<Database> getDatabases() {
		if (!databases.isEmpty())
			return databases;
		return null;
	}

	@Override
	public String getSearchType() {
		return SearchType.getMSMSSearchTerm(cvManager).getPreferredName();
	}

	@Override
	public Software getSoftware() {
		return software;
	}

}
