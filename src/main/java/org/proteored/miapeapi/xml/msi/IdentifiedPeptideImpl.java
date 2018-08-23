package org.proteored.miapeapi.xml.msi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.proteored.miapeapi.interfaces.msi.IdentifiedPeptide;
import org.proteored.miapeapi.interfaces.msi.IdentifiedProtein;
import org.proteored.miapeapi.interfaces.msi.InputData;
import org.proteored.miapeapi.interfaces.msi.PeptideModification;
import org.proteored.miapeapi.interfaces.msi.PeptideScore;
import org.proteored.miapeapi.xml.msi.autogenerated.MSIIdentifiedPeptide;
import org.proteored.miapeapi.xml.msi.autogenerated.MSIInputData;
import org.proteored.miapeapi.xml.msi.autogenerated.MSIPeptideModification;
import org.proteored.miapeapi.xml.msi.autogenerated.ParamType;
import org.proteored.miapeapi.xml.msi.autogenerated.Ref;
import org.proteored.miapeapi.xml.util.MiapeXmlUtil;

import gnu.trove.set.hash.THashSet;

public class IdentifiedPeptideImpl implements IdentifiedPeptide {
	private final MSIIdentifiedPeptide xmlIdentifiedPeptide;
	private final Map<String, MSIInputData> mapInputData;

	private final ArrayList<IdentifiedProtein> proteins = new ArrayList<IdentifiedProtein>();
	private int id = -1;

	public IdentifiedPeptideImpl(MSIIdentifiedPeptide xmlIdentifiedPeptide, Map<String, MSIInputData> mapInputData) {
		this.xmlIdentifiedPeptide = xmlIdentifiedPeptide;
		this.mapInputData = mapInputData;

		// this.id = MiapeXmlUtil.getIdFromXMLId(xmlIdentifiedPeptide.getId());
		// if (this.id == -1) {
		// this.id = MiapeXmlUtil.PeptideCounter.increaseCounter();
		// xmlIdentifiedPeptide.setId(MiapeXmlUtil.IdentifierPrefixes.PEPTIDE.getPrefix()
		// + this.id);
		// }

	}

	@Override
	public String getCharge() {
		return xmlIdentifiedPeptide.getCharge();
	}

	@Override
	public String getMassDesviation() {
		return xmlIdentifiedPeptide.getMassDeviation();
	}

	@Override
	public Set<PeptideScore> getScores() {
		final Set<PeptideScore> peptideScores = new THashSet<PeptideScore>();
		final List<ParamType> msiPeptideScore = xmlIdentifiedPeptide.getPeptideScore();
		if (msiPeptideScore != null) {
			for (final ParamType xmlScore : msiPeptideScore) {
				peptideScores.add(new PeptideScoreImpl(xmlScore));

			}
		}
		return peptideScores;
	}

	@Override
	public String getSequence() {
		return xmlIdentifiedPeptide.getSequence();
	}

	@Override
	public String getSpectrumRef() {
		return xmlIdentifiedPeptide.getSpectrumRef();
	}

	@Override
	public InputData getInputData() {
		if (mapInputData.containsKey(xmlIdentifiedPeptide.getInputDataRef())) {
			return new InputDataImpl(mapInputData.get(xmlIdentifiedPeptide.getInputDataRef()));
		}
		return null;
	}

	@Override
	public int getRank() {
		if (xmlIdentifiedPeptide.getRank() != null)
			return xmlIdentifiedPeptide.getRank();
		return -1;
	}

	@Override
	public Set<PeptideModification> getModifications() {
		final Set<PeptideModification> setOfPeptideModifications = new THashSet<PeptideModification>();
		final List<MSIPeptideModification> xmlModifications = xmlIdentifiedPeptide.getMSIPeptideModification();
		if (xmlModifications != null) {
			for (final MSIPeptideModification xmlModification : xmlModifications) {
				setOfPeptideModifications.add(new PeptideModificationImpl(xmlModification));
			}
			return setOfPeptideModifications;
		}
		return null;
	}

	public List<Ref> getProteinRefs() {
		if (xmlIdentifiedPeptide != null) {
			if (xmlIdentifiedPeptide.getProteinRefs() != null)
				return xmlIdentifiedPeptide.getProteinRefs().getRef();
		}
		return null;
	}

	@Override
	public List<IdentifiedProtein> getIdentifiedProteins() {
		// if (this.proteins == null) {
		// this.proteins = new ArrayList<IdentifiedProtein>();
		// List<MSIIdentifiedProtein> xmlProteins = mapPeptidesProteins
		// .get(xmlIdentifiedPeptide.getId());
		// if (xmlProteins != null) {
		// for (MSIIdentifiedProtein xmlIdentifiedProtein : xmlProteins) {
		// proteins.add(new IdentifiedProteinImpl(
		// xmlIdentifiedProtein, mapInputData,
		// mapProteinPeptides, mapPeptidesProteins));
		// }
		// }
		// }
		return proteins;
	}

	@Override
	public int getId() {
		if (id == -1) {
			id = MiapeXmlUtil.getIdFromXMLId(xmlIdentifiedPeptide.getId());
		}
		return id;
	}

	@Override
	public String getRetentionTimeInSeconds() {
		final Double rt = xmlIdentifiedPeptide.getRT();
		if (rt != null)
			return String.valueOf(rt);
		return null;
	}

	public void addProteinRelationship(IdentifiedProtein identifiedProtein) {
		if (identifiedProtein == null) {
			return;
		}
		for (final IdentifiedProtein protein : proteins) {
			if (protein.getId() == identifiedProtein.getId())
				return;
		}
		proteins.add(identifiedProtein);
	}

}
