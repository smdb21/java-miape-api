package org.proteored.miapeapi.interfaces.msi;

import java.util.List;
import java.util.Set;

/**
 * The output for the procedure: Information for the identified peptides.
 * 
 * @author Salvador
 * 
 */
public interface IdentifiedPeptide {

	/**
	 * Gets the identifier that can be referenced from {@link IdentifiedProtein}
	 * 
	 * @return the identifier
	 */
	public int getId();

	/**
	 * Sequence. <br>
	 * Primary sequence of the matched peptides (include number of missed
	 * cleavages or if the peptide is issued from semi-specific cleavage or
	 * fully unspecific cleavage).
	 * 
	 * @return the peptide sequence
	 */
	public String getSequence();

	/**
	 * Peptide scores values and any associated statistical information as
	 * available in the output.
	 * 
	 * @return the peptide scores
	 */
	public Set<PeptideScore> getScores();

	/**
	 * Chemical modifications (induced by experimental conditions) and
	 * modifications of biological source (naturally-occurring); amino acid
	 * sequence polymorphisms. <br>
	 * Occurrence and position of amino acid modifications, being induced by
	 * experimental conditions (such as oxidized Met, Carbamidomethylated Cys,
	 * iTRAQ modified residue, SILAC modified residue), by natural or induced
	 * biological process (such as phosphorylated, sulfonylated, acetylated
	 * amino acid), issued from a polymorphism (an amino acid mutation or a
	 * frame shift with respect to the sequence in the database).
	 * 
	 * @return @return the {@link PeptideModification}
	 */
	public Set<PeptideModification> getModifications();

	/**
	 * Charge state assumed for identification
	 * 
	 * @return the charge
	 */
	public String getCharge();

	/**
	 * Measurement of peptide mass error. <br>
	 * Description of the mass deviation (either expressed as m/z difference or
	 * as recalculated mass difference).
	 * 
	 * @return the peptide mass error
	 */
	public String getMassDesviation();

	/**
	 * Corresponding Spectrum locus. <br>
	 * Reference to the experimental spectrum (reference to a @link{
	 * SpectrumDescription} object from a MIAPE MS document).
	 * 
	 * @return the identifier of the SpectrumDescription object
	 */
	public String getSpectrumRef();

	/**
	 * Gets the input data from which this spectra comes from
	 * 
	 * @return the input data
	 */
	public InputData getInputData();

	/**
	 * Gets the rank number in the list of identified peptides that corresponds
	 * to the same spectrum
	 * 
	 * @return the rank number
	 */
	public int getRank();

	/**
	 * Gets the proteins that share this peptide.
	 * 
	 * @return
	 */
	public List<IdentifiedProtein> getIdentifiedProteins();

	/**
	 * Gets the retention time is seconds
	 */
	public String getRetentionTimeInSeconds();
}
