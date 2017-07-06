package org.proteored.miapeapi.xml.mzidentml_1_1;

import java.util.List;
import java.util.Set;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.msi.AdditionalParameterName;
import org.proteored.miapeapi.cv.msi.SearchType;
import org.proteored.miapeapi.interfaces.Software;
import org.proteored.miapeapi.interfaces.msi.AdditionalParameter;
import org.proteored.miapeapi.interfaces.msi.Database;
import org.proteored.miapeapi.interfaces.msi.InputParameter;
import org.proteored.miapeapi.xml.mzidentml.util.Utils;
import org.proteored.miapeapi.xml.mzidentml_1_1.util.MzidentmlControlVocabularyXmlFactory;
import org.proteored.miapeapi.xml.util.MiapeXmlUtil;

import gnu.trove.set.hash.THashSet;
import uk.ac.ebi.jmzidml.model.mzidml.AbstractParam;
import uk.ac.ebi.jmzidml.model.mzidml.CvParam;
import uk.ac.ebi.jmzidml.model.mzidml.Enzyme;
import uk.ac.ebi.jmzidml.model.mzidml.Filter;
import uk.ac.ebi.jmzidml.model.mzidml.ProteinDetectionProtocol;
import uk.ac.ebi.jmzidml.model.mzidml.SearchDatabase;
import uk.ac.ebi.jmzidml.model.mzidml.SearchModification;
import uk.ac.ebi.jmzidml.model.mzidml.SpecificityRules;
import uk.ac.ebi.jmzidml.model.mzidml.SpectrumIdentificationProtocol;
import uk.ac.ebi.jmzidml.model.mzidml.UserParam;

public class InputParameterImpl implements InputParameter {
	private final SpectrumIdentificationProtocol sip;
	private final List<SearchDatabase> searchDatabaseList;
	private final Software msiSoftware;
	private final Integer identifier;
	private final Long numSeqSearched;
	private final ControlVocabularyManager cvManager;
	private final ProteinDetectionProtocol pdp;

	public InputParameterImpl(SpectrumIdentificationProtocol spectrumIdentProtocol, ProteinDetectionProtocol pdp,
			List<SearchDatabase> databaseListXML, Software msiSoftware, Integer identifier, Long numSeqSearched,
			ControlVocabularyManager cvManager) {
		this.sip = spectrumIdentProtocol;
		this.searchDatabaseList = databaseListXML;
		this.msiSoftware = msiSoftware;
		this.identifier = identifier;
		this.numSeqSearched = numSeqSearched;
		this.cvManager = cvManager;
		this.pdp = pdp;
	}

	@Override
	public String getAaModif() {
		if (sip != null && sip.getModificationParams() != null
				&& sip.getModificationParams().getSearchModification() != null) {
			List<SearchModification> modifications = sip.getModificationParams().getSearchModification();
			StringBuilder sb = new StringBuilder();
			// fix mode if false --> Variable true --> Fixed
			// Add CV

			for (SearchModification modification : modifications) {
				if (!sb.equals(""))
					sb.append(MiapeXmlUtil.TERM_SEPARATOR);
				final String modificationName = MzidentmlControlVocabularyXmlFactory
						.readEntireCVParamList(modification.getCvParam(), true);
				if (modificationName != null && !modificationName.equals(""))
					sb.append(modificationName + MiapeXmlUtil.TERM_SEPARATOR);

				if (modification.isFixedMod()) {
					sb.append(Utils.FIXED);
					sb.append(MiapeXmlUtil.TERM_SEPARATOR);
				} else {
					sb.append(Utils.VARIABLE);
					sb.append(MiapeXmlUtil.TERM_SEPARATOR);
				}
				if (modification.getMassDelta() != 0) {
					sb.append(Utils.MASS_DELTA + "=");
					sb.append(modification.getMassDelta());
					sb.append(MiapeXmlUtil.TERM_SEPARATOR);
				}

				if (modification.getResidues().size() > 0) {
					sb.append(Utils.RESIDUES + "=");
					int count = 0;
					for (String residue : modification.getResidues()) {
						sb.append(residue);
						count++;
						if (count < modification.getResidues().size()) {
							sb.append(", ");
						}
					}
					sb.append(MiapeXmlUtil.TERM_SEPARATOR);
				}
				if (modification.getSpecificityRules() != null) {
					for (SpecificityRules specifivityRule : modification.getSpecificityRules()) {
						sb.append(MzidentmlControlVocabularyXmlFactory
								.readEntireCVParamList(specifivityRule.getCvParam(), true));
					}

				}
			}
			return Utils.checkReturnedString(sb);
		}
		return null;
	}

	@Override
	public String getAdditionalCleavages() {
		StringBuilder ret = new StringBuilder();
		if (sip != null && sip.getEnzymes() != null && sip.getEnzymes().getEnzyme() != null) {
			Set<String> enzymeInfoSets = new THashSet<String>();
			for (Enzyme enzyme : sip.getEnzymes().getEnzyme()) {
				StringBuilder sb = new StringBuilder();
				// disabled on 28-May-2013: is already captured in
				// getMissedCleavages
				// if (enzyme.getMissedCleavages() != null) {
				// sb.append(Utils.MISSCLEAVAGES + "="
				// + enzyme.getMissedCleavages()
				// + MiapeXmlUtil.TERM_SEPARATOR);
				// }
				if (enzyme.isSemiSpecific() != null) {
					sb.append(Utils.SEMISPECIFIC + "= true" + MiapeXmlUtil.TERM_SEPARATOR);
				}
				if (enzyme.getNTermGain() != null) {
					sb.append(Utils.NTERM_GAIN + "=" + enzyme.getNTermGain() + MiapeXmlUtil.TERM_SEPARATOR);
				}
				if (enzyme.getCTermGain() != null) {
					sb.append(Utils.CTERM_GAIN + "=" + enzyme.getNTermGain() + MiapeXmlUtil.TERM_SEPARATOR);
				}
				if (enzyme.getMinDistance() != null) {
					sb.append(Utils.MIN_DISTANCE + "=" + enzyme.getMinDistance() + MiapeXmlUtil.TERM_SEPARATOR);
				}
				if (!enzymeInfoSets.contains(sb.toString())) {
					ret.append(sb.toString());
					enzymeInfoSets.add(sb.toString());
				}
			}
		}
		return Utils.checkReturnedString(ret);

	}

	@Override
	public Set<AdditionalParameter> getAdditionalParameters() {
		// from Additional Search Params
		Set<AdditionalParameter> addParameter = new THashSet<AdditionalParameter>();
		if (sip != null && sip.getAdditionalSearchParams() != null
				&& sip.getAdditionalSearchParams().getParamGroup() != null) {
			List<AbstractParam> parameters = sip.getAdditionalSearchParams().getParamGroup();
			for (AbstractParam param : parameters) {
				if (param != null) {
					StringBuilder wkName = new StringBuilder();
					StringBuilder wkValue = new StringBuilder();
					if (param.getValue() == null) {
						wkName.append(param.getName());
					} else {
						wkName.append(param.getName());
						wkValue.append(param.getValue());
					}
					if (param.getUnitName() != null) {
						wkValue.append(" " + param.getUnitName());
					}
					addParameter.add(new AdditionalParameterImpl(wkName.toString(), wkValue.toString()));
				}
			}
		}
		if (pdp != null && pdp.getAnalysisParams() != null) {
			List<AbstractParam> parameters = pdp.getAnalysisParams().getParamGroup();
			for (AbstractParam param : parameters) {
				if (param != null) {
					StringBuilder wkName = new StringBuilder();
					StringBuilder wkValue = new StringBuilder();
					if (param.getValue() == null) {
						wkName.append(param.getName());
					} else {
						wkName.append(param.getName());
						wkValue.append(param.getValue());
					}
					if (param.getUnitName() != null) {
						wkValue.append(" " + param.getUnitName());
					}
					addParameter.add(new AdditionalParameterImpl(wkName.toString(), wkValue.toString()));
				}
			}
		}
		if (addParameter.size() > 0)
			return addParameter;
		return null;
	}

	@Override
	public String getCleavageName() {
		StringBuilder sb = new StringBuilder();

		if (sip != null && sip.getEnzymes() != null && sip.getEnzymes().getEnzyme() != null) {
			List<Enzyme> enzymes = sip.getEnzymes().getEnzyme();
			Set<String> cleavageNames = new THashSet<String>();
			int counter = 1;
			for (Enzyme enzyme : enzymes) {
				if (enzyme.getEnzymeName() != null) {
					String enzymeNameCV = MzidentmlControlVocabularyXmlFactory
							.readEntireParamList(enzyme.getEnzymeName());
					if (!cleavageNames.contains(enzymeNameCV)) {
						cleavageNames.add(enzymeNameCV);
						sb.append(enzymeNameCV);
						if (counter < enzymes.size()) {
							sb.append(MiapeXmlUtil.TERM_SEPARATOR);
							counter++;
						}
					}
				}
			}
		}
		return Utils.checkReturnedString(sb);

	}

	@Override
	public String getCleavageRules() {
		StringBuilder sb = new StringBuilder();
		int counter = 1;
		Set<String> siteRegexp = new THashSet<String>();
		if (sip != null && sip.getEnzymes() != null && sip.getEnzymes().getEnzyme() != null) {
			for (Enzyme enzyme : sip.getEnzymes().getEnzyme()) {
				if (enzyme.getSiteRegexp() != null) {
					if (!siteRegexp.contains(enzyme.getSiteRegexp())) {
						siteRegexp.add(enzyme.getSiteRegexp());
						sb.append(Utils.SITEREGEXP + "=");
						sb.append(enzyme.getSiteRegexp());
						if (counter < sip.getEnzymes().getEnzyme().size()) {
							sb.append(MiapeXmlUtil.TERM_SEPARATOR);
							counter++;
						}
					}
				}
			}
		}
		return Utils.checkReturnedString(sb);

	}

	@Override
	public Set<Database> getDatabases() {
		if (this.searchDatabaseList != null) {
			Set<Database> databaseSet = new THashSet<Database>();
			for (SearchDatabase databaseXML : searchDatabaseList) {
				databaseSet.add(new DatabaseImpl(databaseXML));
			}
			return databaseSet;
		}
		return null;
	}

	@Override
	public String getFragmentMassTolerance() {
		if (sip != null && sip.getFragmentTolerance() != null) {
			return MzidentmlControlVocabularyXmlFactory.readEntireCVParamList(sip.getFragmentTolerance().getCvParam(),
					false);
		}
		return null;
	}

	@Override
	public String getFragmentMassToleranceUnit() {
		if (sip != null && sip.getFragmentTolerance() != null) {
			return MzidentmlControlVocabularyXmlFactory
					.readFirstUnitCVParamList(sip.getFragmentTolerance().getCvParam());
		}
		return null;
	}

	@Override
	public int getId() {
		if (identifier != null) {
			return identifier;
		}
		return -1;
	}

	@Override
	public String getMinScore() {
		if (sip != null && sip.getThreshold() != null) {
			return MzidentmlControlVocabularyXmlFactory.readEntireParamList(sip.getThreshold());
		}
		return null;
	}

	@Override
	public String getMisscleavages() {
		if (sip != null && sip.getEnzymes() != null && sip.getEnzymes().getEnzyme() != null
				&& !sip.getEnzymes().getEnzyme().isEmpty()) {
			if (sip.getEnzymes().getEnzyme().get(0).getMissedCleavages() != null) {
				return sip.getEnzymes().getEnzyme().get(0).getMissedCleavages().toString();
			}
		}
		return null;
	}

	@Override
	public String getName() {
		if (sip != null && sip.getName() != null && !"".equals(sip.getName()))
			return sip.getName();
		return sip.getId();
	}

	@Override
	public String getNumEntries() {
		if (numSeqSearched != null && numSeqSearched != -1)
			return numSeqSearched.toString();
		return null;
	}

	@Override
	public String getPmfMassTolerance() {
		if (sip != null && sip.getSearchType() != null && sip.getSearchType().getCvParam() != null) {
			if (SearchType.getPMFSearchTerm(cvManager).getTermAccession()
					.equals(sip.getSearchType().getCvParam().getAccession())
					|| SearchType.getCombinedPMFMSMSSearchTerm(cvManager).getTermAccession()
							.equals(sip.getSearchType().getCvParam().getAccession())) {
				if (sip.getParentTolerance() != null && sip.getParentTolerance().getCvParam() != null)
					return MzidentmlControlVocabularyXmlFactory
							.readEntireCVParamList(sip.getParentTolerance().getCvParam(), false);
			}
		}
		return null;
	}

	@Override
	public String getPmfMassToleranceUnit() {
		if (sip != null && sip.getSearchType() != null && sip.getSearchType().getCvParam() != null) {
			if (SearchType.getPMFSearchTerm(cvManager).getTermAccession()
					.equals(sip.getSearchType().getCvParam().getAccession())
					|| SearchType.getCombinedPMFMSMSSearchTerm(cvManager).getTermAccession()
							.equals(sip.getSearchType().getCvParam().getAccession())) {
				if (sip.getParentTolerance() != null && sip.getParentTolerance().getCvParam() != null)
					return MzidentmlControlVocabularyXmlFactory
							.readFirstUnitCVParamList(sip.getParentTolerance().getCvParam());
			}
		}
		return null;
	}

	@Override
	public String getPrecursorMassTolerance() {
		if (sip != null && sip.getParentTolerance() != null) {
			return MzidentmlControlVocabularyXmlFactory.readEntireCVParamList(sip.getParentTolerance().getCvParam(),
					false);
		}
		return null;
	}

	@Override
	public String getPrecursorMassToleranceUnit() {
		if (sip != null && sip.getParentTolerance() != null) {
			return MzidentmlControlVocabularyXmlFactory.readFirstUnitCVParamList(sip.getParentTolerance().getCvParam());
		}
		return null;
	}

	@Override
	public String getScoringAlgorithm() {

		if (sip != null && this.sip.getAdditionalSearchParams() != null) {
			for (AbstractParam paramType : this.sip.getAdditionalSearchParams().getParamGroup()) {
				// take <userParam name="Mascot Instrument Name"
				// value="MALDI-TOF-TOF"/>
				if (paramType instanceof UserParam) {
					if (paramType.getName().equals(MzidentmlControlVocabularyXmlFactory.MASCOT_INSTRUMENT_NAME)) {
						return MzidentmlControlVocabularyXmlFactory.readEntireParam(paramType);
					}
				}
				// take ("MS:1001376", "Phenyx:Scoring Model")
				if (paramType instanceof CvParam) {
					if (((CvParam) paramType).getAccession().equals(AdditionalParameterName.getInstance(cvManager)
							.getCVTermByAccession(AdditionalParameterName.PHENYX_SCORING_MODEL).getTermAccession())) {
						return MzidentmlControlVocabularyXmlFactory.readEntireParam(paramType);
					}
				}
			}
		}
		return null;
	}

	@Override
	public String getSearchType() {
		if (sip != null && sip.getSearchType() != null)
			return MzidentmlControlVocabularyXmlFactory.readEntireParam(sip.getSearchType());
		return null;
	}

	@Override
	public Software getSoftware() {
		return msiSoftware;
	}

	@Override
	public String getTaxonomy() {
		StringBuilder sb = new StringBuilder();
		if (sip == null || sip.getDatabaseFilters() == null || sip.getDatabaseFilters().getFilter() == null)
			return null;
		List<Filter> filters = sip.getDatabaseFilters().getFilter();
		for (Filter filter : filters) {
			if (filter.getFilterType() != null) {
				sb.append(Utils.FILTER_TYPE + "=");
				sb.append(MzidentmlControlVocabularyXmlFactory.readEntireParam(filter.getFilterType()));
				sb.append(MiapeXmlUtil.TERM_SEPARATOR);
			}

			if (filter.getExclude() != null) {
				sb.append(Utils.EXCLUDE + "=");
				sb.append(MzidentmlControlVocabularyXmlFactory.readEntireParamList(filter.getExclude()));
				sb.append(MiapeXmlUtil.TERM_SEPARATOR);
			}
			if (filter.getInclude() != null) {
				sb.append(Utils.INCLUDE + "=");
				sb.append(MzidentmlControlVocabularyXmlFactory.readEntireParamList(filter.getInclude()));
				sb.append(MiapeXmlUtil.TERM_SEPARATOR);
			}
		}

		return Utils.checkReturnedString(sb);

	}

}
