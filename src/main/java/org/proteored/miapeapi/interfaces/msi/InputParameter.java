package org.proteored.miapeapi.interfaces.msi;

import java.util.Set;

import org.proteored.miapeapi.cv.ms.MassToleranceUnit;
import org.proteored.miapeapi.cv.msi.SearchType;
import org.proteored.miapeapi.interfaces.Software;

/**
 * Input data and parameters: input parameters
 * 
 * @author Salvador
 * 
 */
public interface InputParameter {

	/**
	 * Gets the identifier that can be referenced from IdentifiedProteinSet
	 * 
	 * @return the identifier
	 */
	public int getId();

	/**
	 * Descriptive name of the parameters
	 * 
	 * @return the name
	 */
	public String getName();

	/**
	 * Specify the selection of the subset of the database(s) searched (for instance, mammals, a
	 * NCBI TaxId, a list of accession numbers).
	 * 
	 * @return the taxonomy
	 */
	public String getTaxonomy();

	/**
	 * The cleavage agent as available on the search engine.
	 * 
	 * @return cleavage agent
	 */
	public String getCleavageName();

	/**
	 * Description of cleavage agent rules that have been defined by the user
	 * 
	 * @return cleavage rules
	 */
	public String getCleavageRules();

	/**
	 * Allowed maximum number of cleavage sited missed by the specified agent during the in-silico
	 * cleavage process.
	 * 
	 * @return number of misscleavages
	 */
	public String getMisscleavages();

	/**
	 * Additional parameters related to cleavage: This includes, for instance the consideration of
	 * semi-specific cleavages (occurring on only one terminus), or other parameter that is relevant
	 * to the generation of peptides.
	 * 
	 * @return additional parameters
	 */
	public String getAdditionalCleavages();

	/**
	 * Specify the amino acid or peptide terminal modifications that have to be considered in the
	 * search, according to the search engine list and mode of consideration (fixed or variable for
	 * instance). If the user has added custom modification to the predefined lists, specify the
	 * modification nomenclature, expected induced mass deviation and the associated rules.
	 * 
	 * @return Permissible amino acids modifications
	 */
	public String getAaModif();

	/**
	 * Thresholds; minimum scores for peptides, proteins. the parameters associated to the selection
	 * of peptide and protein hits retained in the output. These might include, for instance, a
	 * minimal score, a maximum p-value, a maximum number of proteins in the output, a minimum
	 * number of peptides to match a protein, etc.
	 * 
	 * @return thresholds
	 */
	public String getMinScore();

	/**
	 * For tandem MS queries, specify the mass tolerance applied to precursor ions submitted to the
	 * search engine (when applicable).
	 * 
	 * @return Precursor-ion mass tolerance
	 */
	public String getPrecursorMassTolerance();

	/**
	 * Mass tolerance for PMF (when applicable) For PMF and other MS queries, specify the mass
	 * tolerance applied to the search engine (when applicable)
	 * 
	 * @return Mass tolerance
	 */
	public String getPmfMassTolerance();

	/**
	 * Fragment-ion mass tolerance for tandem MS (when applicable): For tandem MS queries, specify
	 * the mass tolerance applied to fragment ions submitted to the search engine (when applicable).
	 * 
	 * @return Fragment-ion mass tolerance
	 */
	public String getFragmentMassTolerance();

	/**
	 * Precursor-ion mass tolerance units. It should be one of the possible values from
	 * {@link MassToleranceUnit}
	 * 
	 * @return Precursor-ion mass tolerance units
	 */
	public String getPrecursorMassToleranceUnit();

	/**
	 * Mass tolerance units for PMF. It should be one of the possible values from
	 * {@link MassToleranceUnit}
	 * 
	 * @return Mass tolerance units
	 */
	public String getPmfMassToleranceUnit();

	/**
	 * Fragment-ion mass tolerance units. It should be one of the possible values from
	 * {@link MassToleranceUnit}
	 * 
	 * @return Fragment-ion mass tolerance units
	 */
	public String getFragmentMassToleranceUnit();

	/**
	 * Number of sequences searched.
	 * 
	 * @return the num of entries
	 */
	public String getNumEntries();

	/**
	 * Descriptor of the selected scoring scheme in the search engine. This software search
	 * parameter is often expressed as an instrument type (such as ESI-TRAP in Mascot, ESI - ion
	 * trap HCTultra in Phenyx, ESI-ION-TRAP in MS-Tag, Ion Trap (4 Da) in X !Tandem, etc)
	 * 
	 * @return the scoring algorithm
	 */
	public String getScoringAlgorithm();

	/**
	 * Any other relevant parameters Any application-specific parameters that are to be set for a
	 * search and that has an impact on the interpretation of the result for that experiment.
	 * 
	 * @return Any other relevant parameters
	 */
	public Set<AdditionalParameter> getAdditionalParameters();

	/**
	 * Database queried<br>
	 * The description and version of the sequence databank(s) queried, If the databank(s) is/are
	 * not available on the web, describe the content of this/these databank(s), including the
	 * number of sequences.
	 * 
	 * @return a set of {@link Database} objects
	 */
	public Set<Database> getDatabases();

	/**
	 * Search type: ms-ms, pmf, de-novo... It should be one of the possible values from
	 * {@link SearchType}
	 * 
	 * @return the search type
	 */
	public String getSearchType();

	/**
	 * Gets the software in which this parameters have been applied
	 * 
	 * @return the software
	 */
	public Software getSoftware();

}
