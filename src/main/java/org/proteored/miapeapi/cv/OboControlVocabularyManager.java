package org.proteored.miapeapi.cv;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import gnu.trove.map.hash.THashMap;
import psidev.psi.tools.ontology_manager.interfaces.OntologyTermI;

/**
 * Ontology manager that reads the control vocabularies from OBO files located
 * in the official repositories (so they are up to date). To instantiate it, may
 * take a long time.
 * 
 * @author Salva
 * 
 */
public class OboControlVocabularyManager implements ControlVocabularyManager {
	protected static final int LOCAL_OBO = 1;
	protected static final int OLS_OBO = 2;
	protected static final int REMOTE_OBO = 3;
	protected static final int LOCAL_OBO_TEST = 4;

	private static OboControlVocabularyConnector cvOboManager;

	public OboControlVocabularyManager(boolean overrideInstance) {
		cvOboManager = OboControlVocabularyConnector.getInstance(overrideInstance);
	}

	public OboControlVocabularyManager() {
		cvOboManager = OboControlVocabularyConnector.getInstance(false);
	}

	protected OboControlVocabularyManager(int mode, boolean overrideInstance) {
		if (mode == LOCAL_OBO)
			cvOboManager = OboControlVocabularyConnector.getInstanceForLocalOBOFiles(overrideInstance);
		else if (mode == OLS_OBO)
			cvOboManager = OboControlVocabularyConnector.getInstanceForOLSWebservice(overrideInstance);
		else if (mode == LOCAL_OBO_TEST)
			cvOboManager = OboControlVocabularyConnector.getInstanceForLocalTestOBOFiles(overrideInstance);
		else
			cvOboManager = OboControlVocabularyConnector.getInstance(overrideInstance);
	}

	protected OboControlVocabularyManager(int mode) {
		if (mode == LOCAL_OBO)
			cvOboManager = OboControlVocabularyConnector.getInstanceForLocalOBOFiles(false);
		else if (mode == OLS_OBO)
			cvOboManager = OboControlVocabularyConnector.getInstanceForOLSWebservice(false);
		else if (mode == LOCAL_OBO_TEST)
			cvOboManager = OboControlVocabularyConnector.getInstanceForLocalTestOBOFiles(false);
		else

			// REMOTE OBO
			cvOboManager = OboControlVocabularyConnector.getInstance(false);
	}

	@Override
	public Accession getControlVocabularyId(String name, ControlVocabularySet cvSet) {
		if (cvSet == null) {
			return null;
		}
		List<ControlVocabularyTerm> cvList = this.getAllCVsFromCVSet(cvSet);
		for (ControlVocabularyTerm controlVocabularyTerm : cvList) {
			if (controlVocabularyTerm.getPreferredName() != null)
				if (controlVocabularyTerm.getPreferredName().equalsIgnoreCase(name)) {
					return controlVocabularyTerm.getTermAccession();
				}
		}

		return null;
	}

	@Override
	public String getControlVocabularyName(Accession accession, ControlVocabularySet cvSet) {

		List<ControlVocabularyTerm> cvList = this.getAllCVsFromCVSet(cvSet);
		for (ControlVocabularyTerm controlVocabularyTerm : cvList) {
			if (accession.equals(controlVocabularyTerm.getTermAccession())) {
				return controlVocabularyTerm.getPreferredName();
			}
		}
		return null;
	}

	@Override
	public String getCVRef(Accession id, ControlVocabularySet cvSet) {
		if (id != null) {
			List<ControlVocabularyTerm> cvList = this.getAllCVsFromCVSet(cvSet);
			for (ControlVocabularyTerm controlVocabularyTerm : cvList) {
				if (id.equals(controlVocabularyTerm.getTermAccession())) {
					return controlVocabularyTerm.getCVRef();
				}
			}
		}
		return null;
	}

	@Override
	public boolean isCV(String cvDescription, ControlVocabularySet cvSet) {
		return this.getControlVocabularyId(cvDescription, cvSet) != null;
	}

	@Override
	public List<ControlVocabularyTerm> getAllCVsFromCVSet(ControlVocabularySet cvSet) {

		List<ControlVocabularyTerm> ret = new ArrayList<ControlVocabularyTerm>();
		if (cvSet == null)
			return ret;

		// if this request has been setted before
		if (cvSet.getCachedPossibleValues() != null)
			return cvSet.getCachedPossibleValues();

		Map<String, String> dicc = new THashMap<String, String>();

		List<Accession> parentAccessions = cvSet.getParentAccessions();
		if (parentAccessions != null)
			for (Accession accession : parentAccessions) {
				final Set<OntologyTermI> allChildren = cvOboManager.getAllChildren(accession);
				if (allChildren != null)
					for (OntologyTermI ontologyTermI : allChildren) {
						if (!dicc.containsKey(ontologyTermI.getTermAccession().toLowerCase())) {
							dicc.put(ontologyTermI.getTermAccession().toLowerCase(),
									ontologyTermI.getTermAccession().toLowerCase());
							ret.add(new ControlVocabularyTermImpl(ontologyTermI));
						}
					}
				// TODO control the addition of the parent or not
				if (!dicc.containsKey(accession.toLowerCase())) {
					dicc.put(accession.toLowerCase(), accession.toLowerCase());
					OntologyTermI parentCV = cvOboManager.getTermForAccession(accession);
					if (parentCV != null)
						ret.add(new ControlVocabularyTermImpl(parentCV));
				}
			}

		List<Accession> explicitAccessions = cvSet.getExplicitAccessions();
		if (explicitAccessions != null) {
			for (Accession accession : explicitAccessions) {
				final OntologyTermI term = cvOboManager.getTermForAccession(accession);
				if (term != null && !dicc.containsKey(term.getTermAccession().toLowerCase())) {
					dicc.put(term.getTermAccession().toLowerCase(), term.getTermAccession().toLowerCase());
					ret.add(new ControlVocabularyTermImpl(term));
				}
			}
		}
		cvSet.setCachedPossibleValues(ret);
		return ret;
	}

	@Override
	public ControlVocabularyTerm getCVTermByAccession(Accession accession, ControlVocabularySet cvSet) {
		if (accession == null)
			return null;
		if (cvSet == null)
			return null;

		List<ControlVocabularyTerm> cvList = this.getAllCVsFromCVSet(cvSet);
		for (ControlVocabularyTerm controlVocabularyTerm : cvList) {
			if (controlVocabularyTerm.getTermAccession().equals(accession))
				return controlVocabularyTerm;
		}

		return null;

		// comented: if there is not in the cvSet, return null;
		// OntologyTermI cvTerm = cvOboManager.getTermForAccession(accession);
		// return new ControlVocabularyTermImpl(cvTerm);

	}

	@Override
	public Map<String, ControlVocabularyTerm> getAccesionParents(Accession accession)
			throws UnsupportedOperationException {
		if (accession == null)
			return null;
		Map<String, OntologyTermI> accesionParents = cvOboManager.getAccesionParents(accession);
		Map<String, ControlVocabularyTerm> ret = new THashMap<String, ControlVocabularyTerm>();
		for (String accession_string : accesionParents.keySet()) {
			ret.put(accession_string, new ControlVocabularyTermImpl(accesionParents.get(accession_string)));
		}
		return ret;
	}

	@Override
	public boolean isSonOf(Accession accession, Accession potentialParent) throws UnsupportedOperationException {
		if (accession == null || potentialParent == null)
			return false;
		return cvOboManager.isSonOf(accession, potentialParent);
	}
}