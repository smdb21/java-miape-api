package org.proteored.miapeapi.cv;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.core.io.ClassPathResource;

import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;
import psidev.psi.tools.ontology_manager.OntologyManager;
import psidev.psi.tools.ontology_manager.impl.local.OntologyLoaderException;
import psidev.psi.tools.ontology_manager.interfaces.OntologyAccess;
import psidev.psi.tools.ontology_manager.interfaces.OntologyTermI;

public class OboControlVocabularyConnector {
	private static final boolean OBO_ONTOLOGIES_ACTIVATED = true;
	private static OboControlVocabularyConnector instance;
	private static OntologyManager om;
	private static boolean omAvailable = true;
	private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger("log4j.logger.org.proteored");
	private static boolean error;
	public final static String CONFIG_FILE = "ontologies.xml";
	public final static String LOCAL_CONFIG_FILE = "ontologies_local.xml";
	public static final String OLS_CONFIG_FILE = "ontologies_OLS.xml";
	public static final String LOCAL_TEST_CONFIG_FILE = "ontologies_test_local.xml";

	private OboControlVocabularyConnector(String configFile) {
		loadOntologyManager(configFile);
	}

	public static OboControlVocabularyConnector getInstance(boolean overrideInstance) {
		if (instance == null || overrideInstance)
			instance = new OboControlVocabularyConnector(null);
		return instance;
	}

	public static OboControlVocabularyConnector getInstance() {
		if (instance == null)
			instance = new OboControlVocabularyConnector(null);
		return instance;
	}

	public static OboControlVocabularyConnector getInstanceForLocalOBOFiles(boolean overrideInstance) {
		if (instance == null || overrideInstance)
			instance = new OboControlVocabularyConnector(LOCAL_CONFIG_FILE);
		return instance;
	}

	public static OboControlVocabularyConnector getInstanceForLocalTestOBOFiles(boolean overrideInstance) {
		if (instance == null || overrideInstance)
			instance = new OboControlVocabularyConnector(LOCAL_TEST_CONFIG_FILE);
		return instance;
	}

	public static OboControlVocabularyConnector getInstanceForLocalOBOFiles() {
		if (instance == null)
			instance = new OboControlVocabularyConnector(LOCAL_CONFIG_FILE);
		return instance;
	}

	public static OboControlVocabularyConnector getInstanceForOLSWebservice(boolean overrideInstance) {
		if (instance == null || overrideInstance)
			instance = new OboControlVocabularyConnector(OLS_CONFIG_FILE);
		return instance;
	}

	public static OboControlVocabularyConnector getInstanceForOLSWebservice() {
		if (instance == null)
			instance = new OboControlVocabularyConnector(OLS_CONFIG_FILE);
		return instance;
	}

	/**
	 * Initialize the {@link OboControlVocabularyConnector} with a config file.
	 * 
	 * @param configFile
	 *            put "ontologies_local.xml" to local OBO files
	 * @return
	 */
	public static OboControlVocabularyConnector getInstance(String configFile) {
		if (instance == null)
			instance = new OboControlVocabularyConnector(configFile);
		return instance;
	}

	private static OntologyManager loadOntologyManager(String configFile) {
		if (configFile == null)
			configFile = CONFIG_FILE; // by default

		// wait if there is not an om available but only if there is not errors
		// if error, it will try again to get it
		while (!omAvailable && !error) {
			try {
				Thread.sleep(1000);
				log.info("Waiting for the ontology loading");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		if (om == null && OBO_ONTOLOGIES_ACTIVATED) {
			try {
				omAvailable = false;
				log.info("Loading ontologies from " + configFile);
				long time1 = System.currentTimeMillis();
				om = new OntologyManager(getConfigFile(configFile));
				omAvailable = true;
				log.info("ontologies loaded from " + configFile);
				long time2 = System.currentTimeMillis();
				log.info("Ontologies loaded in " + (time2 - time1) / 1000 + " seconds");
				return om;

				// if fails retrieving remote ontologies, get local ontologies
			} catch (OntologyLoaderException e) {
				log.info("Error loading ontologies from " + configFile + ": " + e.getMessage());
				configFile = LOCAL_CONFIG_FILE;
				omAvailable = false;
				log.info("Loading ontologies from " + configFile);
				try {
					om = new OntologyManager(getConfigFile(configFile));
					omAvailable = true;
					log.info("ontologies loaded from " + configFile);
					return om;
				} catch (OntologyLoaderException e1) {
					error = true;
					e1.printStackTrace();
				} catch (IOException e1) {
					error = true;
					e1.printStackTrace();
				}
			} catch (IOException e) {
				configFile = LOCAL_CONFIG_FILE;
				omAvailable = false;
				log.info("Loading ontologies from " + configFile);
				try {
					om = new OntologyManager(getConfigFile(configFile));
					omAvailable = true;
					log.info("ontologies loaded from " + configFile);
					return om;
				} catch (OntologyLoaderException e1) {
					error = true;
					e1.printStackTrace();
				} catch (IOException e1) {
					error = true;
					e1.printStackTrace();
				}
			}
		} else {
			return om;
		}
		error = true;
		return null;
	}

	private static InputStream getConfigFile(String configFile) throws IOException {
		// Check the environment variable SERVER_TEST
		Map<String, String> env = System.getenv();
		ClassPathResource file = null;
		// if true, take local ontologies
		if (env.get("SERVER_TEST") != null && env.get("SERVER_TEST").equals("true")) {
			// file = new ClassPathResource("ontologies_local.xml");
			// file = new ClassPathResource("ontologies.xml");
			// if false, take remote ontologies
		} else {
			// file = new ClassPathResource("ontologies.xml");
		}
		file = new ClassPathResource(configFile);
		return file.getInputStream();
	}

	public OntologyTermI getTermForAccession(Accession accession) {

		if (om != null) {
			for (String ontologyID : om.getOntologyIDs()) {
				final OntologyAccess ontologyAccess = om.getOntologyAccess(ontologyID);

				if (ontologyAccess != null) {
					final OntologyTermI term = ontologyAccess.getTermForAccession(accession.toString());
					if (term != null)
						return term;
				}
			}
		}

		return null;
	}

	public Set<OntologyTermI> getAllChildren(Accession accession) {

		if (om != null) {
			for (String ontologyID : om.getOntologyIDs()) {
				final OntologyAccess ontologyAccess = om.getOntologyAccess(ontologyID);
				if (ontologyAccess != null) {
					final OntologyTermI term = ontologyAccess.getTermForAccession(accession.toString());
					if (term != null) {
						final Set<OntologyTermI> terms = ontologyAccess.getAllChildren(term);
						if (terms != null && terms.size() > 0)
							return terms;
					}
				}
			}
		}
		return null;
	}

	public Set<OntologyTermI> getTermParents(Accession accession) {
		if (om != null) {
			Set<OntologyTermI> ret = new THashSet<OntologyTermI>();

			for (String ontologyID : om.getOntologyIDs()) {
				final OntologyAccess ontologyAccess = om.getOntologyAccess(ontologyID);
				if (ontologyAccess != null) {
					final OntologyTermI term = ontologyAccess.getTermForAccession(accession.toString());
					if (term != null) {
						ret.addAll(ontologyAccess.getAllParents(term));
					}
				}
			}
			return ret;
		}
		return null;
	}

	public Set<OntologyTermI> getDirectTermParents(Accession accession) {
		if (om != null) {
			Set<OntologyTermI> ret = new THashSet<OntologyTermI>();

			for (String ontologyID : om.getOntologyIDs()) {
				final OntologyAccess ontologyAccess = om.getOntologyAccess(ontologyID);
				if (ontologyAccess != null) {
					final OntologyTermI term = ontologyAccess.getTermForAccession(accession.toString());
					if (term != null) {
						ret.addAll(ontologyAccess.getDirectParents(term));
					}
				}
			}
			return ret;
		}
		return null;
	}

	public boolean isSonOf(Accession accession, Accession potentialParent) {
		Map<?, ?> accesionParents = getAccesionParents(accession);
		if (accesionParents != null)
			return accesionParents.containsKey(potentialParent.toString());
		return false;
	}

	/**
	 * Gets a {@link HashMap} that contains the accession string of the term and
	 * the term
	 * 
	 * @param accession
	 * @return
	 */
	public Map<String, OntologyTermI> getAccesionParents(Accession accession) {
		final Set<OntologyTermI> termParents = getTermParents(accession);
		if (termParents != null) {
			Map<String, OntologyTermI> ret = new THashMap<String, OntologyTermI>();
			for (OntologyTermI ontologyTermI : termParents) {
				ret.put(ontologyTermI.getTermAccession(), ontologyTermI);
			}
			return ret;
		}
		return null;
	}

	public Map<String, OntologyTermI> getChildren(Accession accession) {
		final Set<OntologyTermI> allChildren = getAllChildren(accession);
		if (allChildren != null) {
			Map<String, OntologyTermI> ret = new THashMap<String, OntologyTermI>();
			for (OntologyTermI ontologyTermI : allChildren) {
				ret.put(ontologyTermI.getTermAccession(), ontologyTermI);
			}
			return ret;
		}
		return null;

	}

}
