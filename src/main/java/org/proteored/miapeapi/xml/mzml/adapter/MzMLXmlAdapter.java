package org.proteored.miapeapi.xml.mzml.adapter;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.PSIMassSpectrometryOntology;
import org.proteored.miapeapi.cv.UnitOntology;
import org.proteored.miapeapi.interfaces.Adapter;
import org.proteored.miapeapi.interfaces.ms.MiapeMSDocument;
import org.proteored.miapeapi.xml.mzidentml.util.Utils;

import uk.ac.ebi.jmzml.model.mzml.CV;
import uk.ac.ebi.jmzml.model.mzml.CVList;
import uk.ac.ebi.jmzml.model.mzml.MzML;

public class MzMLXmlAdapter implements Adapter<MzML> {
	private final MiapeMSDocument miapeMS;
	private final ControlVocabularyManager cvManager;

	public MzMLXmlAdapter(MiapeMSDocument miapeMS, ControlVocabularyManager controlVocabularyUtil) {
		this.miapeMS = miapeMS;
		this.cvManager = controlVocabularyUtil;
	}

	@Override
	public MzML adapt() {
		MzML mzML = new MzML();

		mzML.setAccession(this.miapeMS.getName());
		mzML.setVersion(this.miapeMS.getVersion());
		mzML.setId(Utils.getIdFromString(this.miapeMS.getName()));

		mzML.setCvList(createCVList());
		mzML.setFileDescription(new FileDescriptionAdapter(this.miapeMS).adapt());
		return mzML;
	}

	private CVList createCVList() {
		CVList cvList = new CVList();
		// PSI
		CV cv = new CV();
		cv.setFullName(PSIMassSpectrometryOntology.getFullName());
		cv.setURI(PSIMassSpectrometryOntology.getAddress());
		cv.setVersion(PSIMassSpectrometryOntology.getVersion());
		cv.setId(PSIMassSpectrometryOntology.getCVLabel());
		cvList.getCv().add(cv);

		// UNIT
		cv = new CV();
		cv.setFullName(UnitOntology.getFullName());
		cv.setURI(UnitOntology.getAddress());
		cv.setVersion(UnitOntology.getVersion());
		cv.setId(UnitOntology.getCVLabel());
		cvList.getCv().add(cv);
		return cvList;
	}

}
