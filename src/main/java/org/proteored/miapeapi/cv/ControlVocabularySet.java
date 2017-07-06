package org.proteored.miapeapi.cv;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import gnu.trove.map.hash.THashMap;
import psidev.psi.tools.ontology_manager.impl.OntologyTermImpl;

/**
 * This class represents a set of controlled vocabularies that are associated
 * with a certain information, normally requested by MIAPEs. It is associated
 * with a {@link ControlVocabularyManager}
 *
 * @author Salva
 *
 */
public class ControlVocabularySet {
	private static ControlVocabularySet instance;
	private static Logger log = Logger.getLogger("log4j.logger.org.proteored");
	protected String[] parentAccessions;
	protected String[] explicitAccessions;
	protected int miapeSection = -1;
	private List<ControlVocabularyTerm> cachedPossibleValues = null;
	private boolean excludeParents = false;

	public List<ControlVocabularyTerm> getCachedPossibleValues() {
		return cachedPossibleValues;
	}

	public void setCachedPossibleValues(List<ControlVocabularyTerm> cachedPossibleValues) {
		this.cachedPossibleValues = cachedPossibleValues;
	}

	private final Map<String, ControlVocabularyTerm> termCacheByAccession = new THashMap<String, ControlVocabularyTerm>();
	private final Map<String, ControlVocabularyTerm> termCacheByName = new THashMap<String, ControlVocabularyTerm>();

	private ControlVocabularyManager cvManager = null;

	protected ControlVocabularySet(ControlVocabularyManager cvManager) {
		this.cvManager = cvManager;
	}

	// public static ControlVocabularySet getInstance() {
	// if (instance == null)
	// instance = new ControlVocabularySet();
	// return instance;
	// }

	public static ControlVocabularySet getInstance(ControlVocabularyManager cvManager) {
		if (instance == null)
			instance = new ControlVocabularySet(cvManager);
		return instance;

	}

	public void setExcludeParents(boolean value) {
		excludeParents = value;
	}

	public boolean isExcludeParents() {
		return excludeParents;
	}

	protected ControlVocabularyManager getCvManager() {
		return cvManager;
	}

	/**
	 * Gets the identifier of the MIAPE section that the CV set is associated
	 *
	 * @return the identifier of the section in the database
	 */
	public Integer getAssociatedMiapeSection() {
		return miapeSection;
	}

	public List<Accession> getParentAccessions() {

		List<Accession> ret = new ArrayList<Accession>();
		if (parentAccessions != null)
			for (int i = 0; i < parentAccessions.length; i++) {
				ret.add(new Accession(parentAccessions[i]));
			}
		return ret;
	}

	public List<Accession> getExplicitAccessions() {

		List<Accession> ret = new ArrayList<Accession>();
		if (explicitAccessions != null)
			for (int i = 0; i < explicitAccessions.length; i++) {
				ret.add(new Accession(explicitAccessions[i]));
			}
		return ret;
	}

	public List<ControlVocabularyTerm> getPossibleValues() {

		if (cvManager != null) {
			// Cache
			if (cachedPossibleValues != null)
				return cachedPossibleValues;
			cachedPossibleValues = cvManager.getAllCVsFromCVSet(this);
			if (excludeParents && parentAccessions != null) {
				List<ControlVocabularyTerm> ret = new ArrayList<ControlVocabularyTerm>();
				for (ControlVocabularyTerm cvTerm : cachedPossibleValues) {
					boolean include = true;
					for (String parentAcc : parentAccessions) {
						if (cvTerm.getTermAccession().toString().equalsIgnoreCase(parentAcc))
							include = false;
					}
					if (include)
						ret.add(cvTerm);
				}
				cachedPossibleValues.clear();
				cachedPossibleValues.addAll(ret);
			}
			return cachedPossibleValues;
		}
		return null;
	}

	public synchronized ControlVocabularyTerm getCVTermByAccession(Accession accession) {
		if (cvManager != null) {
			// check if it is already available in the cache
			if (termCacheByAccession.containsKey(accession.toString())) {
				// log.info("Term " + accession + " already cached");
				return termCacheByAccession.get(accession.toString());
			} else {
				ControlVocabularyTerm cvTermByAccession = cvManager.getCVTermByAccession(accession, this);
				if (cvTermByAccession != null) {
					// Add to cache
					log.info("Accession: " + accession + " added to cache");
					termCacheByAccession.put(accession.toString(), cvTermByAccession);
				} else {
					log.debug("Accession: " + accession + " not found ontology for " + this.getClass().getSimpleName());
				}
				return cvTermByAccession;
			}
		}
		return null;
	}

	/**
	 * Gets a ControlVocabularyTerm from the set searching by name
	 *
	 * @param name
	 * @return
	 */
	public synchronized ControlVocabularyTerm getCVTermByPreferredName(String name) {
		if (name == null || "".equals(name))
			return null;
		// check if available in the cache
		if (termCacheByName.containsKey(name)) {
			// log.info("Term name " + name + " already cached");
			return termCacheByName.get(name);
		}

		List<ControlVocabularyTerm> possibleValues = getPossibleValues();
		if (possibleValues != null)
			for (ControlVocabularyTerm cvTerm : possibleValues) {
				final String preferredName = cvTerm.getPreferredName();
				if (cvTerm != null && preferredName != null)
					if (preferredName.equalsIgnoreCase(name)) {
						// add to cache
						log.info("Term " + cvTerm.getTermAccession() + " " + cvTerm.getPreferredName()
								+ " added to cacheByName");
						termCacheByName.put(name, cvTerm);
						return cvTerm;
					}
			}
		return null;
	}

	/**
	 * Gets the first ControlVocabularyTerm of the set
	 *
	 * @return
	 */
	public ControlVocabularyTerm getFirstCVTerm() {
		final List<ControlVocabularyTerm> possibleValues = getPossibleValues();
		if (possibleValues != null && possibleValues.size() > 0)
			return possibleValues.get(0);
		return new ControlVocabularyTermImpl(new OntologyTermImpl("0000", ""));
	}
}
