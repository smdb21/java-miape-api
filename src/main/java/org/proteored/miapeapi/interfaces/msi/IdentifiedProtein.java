package org.proteored.miapeapi.interfaces.msi;

import java.util.List;
import java.util.Set;

import org.proteored.miapeapi.cv.msi.ValidationType;

/**
 * The output for the procedure: information for the identified protein
 * 
 * @author Salvador
 * 
 */
public interface IdentifiedProtein {

	/**
	 * Gets the identifier that can be referenced from {@link IdentifiedPeptide}
	 * 
	 * @return the identifier
	 */
	public int getId();

	/**
	 * Gets the Accession code in the queried database. Protein Accession Code such as a
	 * Uniprot-SwissProt AC number (P34521) (not an ID such as YM45_CAEEL), or a ncbi gi number
	 * (12803681). In case of the concept of protein group is applied (i.e. a list of protein that
	 * share a number of identical peptides among those identified), the description of the protein
	 * group might replace the list of individual Accession Codes
	 * 
	 * @return the accession code
	 */
	public String getAccession();

	/**
	 * Gets the protein description field from the database
	 * 
	 * @return the protein description
	 */
	public String getDescription();

	/**
	 * Protein scores as reported by search engine
	 * 
	 * @return the protein scores
	 */
	public Set<ProteinScore> getScores();

	/**
	 * Number of different peptide sequences (without considering modifications) assigned to the
	 * protein. Do consider the number of different peptide sequences. A peptide identified as
	 * unmodified and for instance containing an oxidized Methionine is counted as one. A peptide
	 * with one missed cleavage and one included peptide with no missed cleavages are considered as
	 * two
	 * 
	 * @return the number of different peptide sequences
	 */
	public String getPeptideNumber();

	/**
	 * Percent peptide coverage of protein. Expressed as the number of amino acids spanned by the
	 * assigned peptides divided by the sequence length
	 * 
	 * @return the percent peptide coverage of protein
	 */
	public String getCoverage();

	/**
	 * In the case of PMF, number of matched peaks. Describe the number of matched m/z values
	 * associated to the identified protein
	 * 
	 * @return the number of matched peaks
	 */
	public String getPeaksMatchedNumber();

	/**
	 * The number of unmatched signals (or the total number of m/z values in the original
	 * spectrum/spectra).
	 * 
	 * @return the number of unmatched peaks
	 */
	public String getUnmatchedSignals();

	/**
	 * Other additional information, when used for evaluation of confidence This might include
	 * retention time, multiplicity of peptide sequence occurrences, flanking residues, etc
	 * 
	 * @return Other additional information.
	 */
	public String getAdditionalInformation();

	/**
	 * Validation status If accepted as valid (true) or as rejected (false).
	 * 
	 * @return the validation status
	 */
	public Boolean getValidationStatus();

	/**
	 * Validation type. It should be one of the possible values from {@link ValidationType}
	 * 
	 * @return the type of validation
	 */
	public String getValidationType();

	/**
	 * If accepted without post-processing of search engine/de-novo interpretation (accept raw
	 * output of identification software) or if manually accepted as valid or as rejected (false
	 * positive)
	 * 
	 * @return value of the validation status
	 */
	public String getValidationValue();

	/**
	 * Gets the identified peptides that contributed to this protein identification
	 * 
	 * @return a list of identified peptides.
	 */
	public List<IdentifiedPeptide> getIdentifiedPeptides();

}
