package org.proteored.miapeapi.xml.mzml;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.proteored.miapeapi.cv.Accession;
import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ms.EsiName;
import org.proteored.miapeapi.cv.ms.IonSourceName;
import org.proteored.miapeapi.interfaces.Equipment;
import org.proteored.miapeapi.interfaces.MatchMode;
import org.proteored.miapeapi.interfaces.ms.Esi;
import org.proteored.miapeapi.xml.mzml.util.MzMLControlVocabularyXmlFactory;

import uk.ac.ebi.jmzml.model.mzml.CVParam;
import uk.ac.ebi.jmzml.model.mzml.ParamGroup;
import uk.ac.ebi.jmzml.model.mzml.ReferenceableParamGroup;
import uk.ac.ebi.jmzml.model.mzml.ReferenceableParamGroupList;
import uk.ac.ebi.jmzml.model.mzml.ReferenceableParamGroupRef;
import uk.ac.ebi.jmzml.model.mzml.SourceComponent;

public class EsiImpl implements Esi {

	private String name = null;
	private String parameters = null;
	private String supplyType = null;
	private final String[] SUPPLY_TYPE_TEXT_LIST = { "fed", "static" };
	private final String[] NAME_TEXT_LIST = { "name" };
	private final Set<Equipment> interfaces = new HashSet<Equipment>();
	private final Set<Equipment> sprayers = new HashSet<Equipment>();
	private final ControlVocabularyManager cvManager;

	public EsiImpl(SourceComponent sourceComponent,
			ReferenceableParamGroupList referenceableParamGroupList,
			ControlVocabularyManager cvManager) {
		HashMap<String, String> dicc = new HashMap<String, String>();
		this.cvManager = cvManager;

		if (sourceComponent != null) {
			// Create a paramGroup
			ParamGroup paramGroup = MzMLControlVocabularyXmlFactory.createParamGroup(
					sourceComponent.getCvParam(), sourceComponent.getUserParam(),
					sourceComponent.getReferenceableParamGroupRef());

			if (this.isESIFromParamGroup(paramGroup, referenceableParamGroupList)) {

				// name
				this.name = MzMLControlVocabularyXmlFactory.getValueFromParamGroup(paramGroup,
						referenceableParamGroupList, EsiName.getInstance(cvManager));
				if (name == null)
					this.name = MzMLControlVocabularyXmlFactory.getValueFromParamGroupByName(
							paramGroup, referenceableParamGroupList, NAME_TEXT_LIST,
							MatchMode.ANYWHERE);
				if (name == null)
					this.name = "ESI source";
				else
					dicc.put(name, name);

				// supply type
				// TODO add parsing when new CV TERM is available
				this.supplyType = MzMLControlVocabularyXmlFactory.getFullCVFromParamGroupByName(
						paramGroup, referenceableParamGroupList, SUPPLY_TYPE_TEXT_LIST,
						MatchMode.ANYWHERE);
				dicc.put(supplyType, supplyType);

				// sprayers
				processSprayers(paramGroup, referenceableParamGroupList, cvManager);

				// interfaces
				processInterfaces(paramGroup, referenceableParamGroupList);

				// Get all params that are not in the dicc and store in
				// "parameters"
				this.parameters = MzMLControlVocabularyXmlFactory.parseAllParams(paramGroup,
						referenceableParamGroupList, dicc);
			}
		}
	}

	private void processInterfaces(ParamGroup paramGroup,
			ReferenceableParamGroupList referenceableParamGroupList) {

		InterfaceImpl interfaze = new InterfaceImpl(paramGroup, referenceableParamGroupList);

		if (interfaze != null && interfaze.getName() != null
				&& !"".equals(interfaze.getFullDescription()))
			interfaces.add(interfaze);

	}

	private void processSprayers(ParamGroup paramGroup,
			ReferenceableParamGroupList referenceableParamGroupList,
			ControlVocabularyManager cvManager) {
		SprayerImpl sprayer = new SprayerImpl(paramGroup, referenceableParamGroupList, cvManager);
		if (sprayer != null && sprayer.getName() != null
				&& !"".equals(sprayer.getFullDescription()))
			sprayers.add(sprayer);
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getParameters() {
		return parameters;
	}

	@Override
	public String getSupplyType() {
		return this.supplyType;
	}

	@Override
	public Set<Equipment> getInterfaces() {
		if (this.interfaces != null && this.interfaces.size() > 0)
			return this.interfaces;
		return null;
	}

	@Override
	public Set<Equipment> getSprayers() {
		if (this.sprayers != null && this.sprayers.size() > 0)
			return this.sprayers;
		return null;
	}

	/**
	 * Check if a ParamGroup contains any of the
	 * 
	 * @param paramGroup
	 * @param referenceableParamGroupList
	 * @return
	 */
	private boolean isESIFromParamGroup(ParamGroup paramGroup,
			ReferenceableParamGroupList referenceableParamGroupList) {
		if (paramGroup != null) {
			if (this.isESIFromCVParams(paramGroup.getCvParam()))
				return true;
			for (ReferenceableParamGroupRef refParam : paramGroup.getReferenceableParamGroupRef()) {
				ReferenceableParamGroup refParamGroup = MzMLControlVocabularyXmlFactory
						.searchParamGroupInReferenceableParamGroupList(refParam.getRef(),
								referenceableParamGroupList);

				if (this.isESIFromCVParams(refParamGroup.getCvParam()))
					return true;
			}
		}
		return false;
	}

	private boolean isESIFromCVParams(List<CVParam> listParam) {
		for (CVParam cvParam : listParam) {
			// check if found <cvParam name="Ionization type" cvId="MS:1000008"
			// value="electrospray"/>
			if (IonSourceName.IONIZATION_TYPE_ACC.equals(cvParam.getAccession())) {
				if (IonSourceName.isEsiFromDescription(cvParam.getValue(), this.cvManager))
					return true;
			} else {
				if (IonSourceName.isEsiFromAccession(new Accession(cvParam.getAccession())))
					return true;
			}
		}
		return false;
	}

}
