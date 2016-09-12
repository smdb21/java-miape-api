package org.proteored.miapeapi.xml.xtandem.msi;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.interfaces.msi.IdentifiedPeptide;
import org.proteored.miapeapi.interfaces.msi.IdentifiedProtein;
import org.proteored.miapeapi.interfaces.msi.IdentifiedProteinSet;
import org.proteored.miapeapi.interfaces.msi.InputDataSet;
import org.proteored.miapeapi.interfaces.msi.InputParameter;

public class IdentifiedProteinSetImpl implements IdentifiedProteinSet {

	private final Set<InputDataSet> inputDataSets;
	private final InputParameter inputParameter;
	private final String fileLocation;
	private final HashMap<String, IdentifiedProtein> identifiedProteinList;
	private HashMap<String, IdentifiedPeptide> peptideSet;
	private final ControlVocabularyManager cvManager;

	public IdentifiedProteinSetImpl(Set<InputDataSet> inputDataSets, InputParameter inputParameter,
			String fileLocation, Collection<IdentifiedProtein> proteinCollection,
			ControlVocabularyManager cvManager) {

		this.inputDataSets = inputDataSets;
		this.inputParameter = inputParameter;
		this.fileLocation = fileLocation;

		this.cvManager = cvManager;

		this.identifiedProteinList = new HashMap<String, IdentifiedProtein>();
		for (IdentifiedProtein identifiedProtein : proteinCollection) {
			this.identifiedProteinList.put(identifiedProtein.getAccession(), identifiedProtein);
		}

	}

	// private void processProteins() {
	// // create first the peptide protein mapping
	// createPeptideHash();
	// final ProteinMap proteinMap = xfile.getProteinMap();
	// final ModificationMap modificationsMap = xfile.getModificationMap();
	// Iterator<String> proteinIDIterator = proteinMap.getProteinIDIterator();
	// while (proteinIDIterator.hasNext()) {
	// String proteinID = proteinIDIterator.next();
	// Protein protein = proteinMap.getProtein(proteinID);
	//
	// if (protein != null) {
	// int idProtein = MiapeXmlUtil.ProteinCounter.increaseCounter();
	// IdentifiedProteinImpl identifiedProtein = new
	// IdentifiedProteinImpl(protein,
	// idProtein, modificationsMap, cvManager);
	// identifiedProteinList.put(identifiedProtein.getAccession(),
	// identifiedProtein);
	// }
	// }
	// }

	@Override
	public String getName() {
		return "protein set";
	}

	@Override
	public String getFileLocation() {
		return this.fileLocation;
	}

	@Override
	public HashMap<String, IdentifiedProtein> getIdentifiedProteins() {
		if (!identifiedProteinList.isEmpty())
			return identifiedProteinList;
		return null;
	}

	@Override
	public InputParameter getInputParameter() {
		return this.inputParameter;
	}

	@Override
	public Set<InputDataSet> getInputDataSets() {
		return inputDataSets;
	}

}
