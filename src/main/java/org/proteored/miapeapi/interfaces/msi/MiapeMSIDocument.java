package org.proteored.miapeapi.interfaces.msi;

import java.util.List;
import java.util.Set;

import org.proteored.miapeapi.interfaces.MiapeDocument;
import org.proteored.miapeapi.interfaces.Software;
import org.proteored.miapeapi.interfaces.ms.MSContact;
import org.proteored.miapeapi.interfaces.xml.XmlEntity;

/**
 * Interface for the MIAPE Mass Spectrometry Informatics document
 *
 * @author Salvador
 *
 */
public interface MiapeMSIDocument extends MiapeDocument, XmlEntity<MiapeMSIDocument> {

	/**
	 * Reference to the Mass Spectrometry (MS) MIAPE document
	 *
	 * @return the identifier of the MS document
	 */
	public int getMSDocumentReference();

	public void setReferencedMSDocument(int id);

	public void setId(int id);

	/**
	 * The location of the data generated by the procedure and the metadata if
	 * captured in an external file. If made available in a public repository,
	 * describe the URI (for instance a url, or the url of the repository and
	 * the information on how to retrieve the data).
	 *
	 * @return the URI
	 */
	public String getGeneratedFilesURI();

	/**
	 * The contact person reference or source and the internal coordinates of
	 * the data if not made available for public access.
	 *
	 * @return the description
	 */
	public String getGeneratedFilesDescription();

	/**
	 * The software used for identifying peptides and proteins.<br>
	 * For each software package used: The trade name of the software used for
	 * the identification and/or characterisation work, together with the
	 * version name or number according to the vendor's nomenclature and vendor
	 * name. If additional software has been used for re-scoring peptide/protein
	 * identifications or modifications, this should also be reported here.
	 * Software name, version and manufacturer.
	 *
	 * @return the information of the software
	 */
	public Set<Software> getSoftwares();

	/**
	 * Gets the input data sets with the input data used in the experiment
	 *
	 * @return a set of input data sets
	 */
	public Set<InputDataSet> getInputDataSets();

	/**
	 * Input parameters
	 *
	 * @return the information of the input parameters
	 */
	public Set<InputParameter> getInputParameters();

	/**
	 * Interpretation and validation
	 *
	 * @return the information of the interpretation and validation of the
	 *         results
	 */
	public Set<Validation> getValidations();

	/**
	 * The output for the procedure: information for the identified protein
	 *
	 * @return the information of a set of identified proteins (one set for each
	 *         RUN)
	 */
	public Set<IdentifiedProteinSet> getIdentifiedProteinSets();

	/**
	 * The output for the procedure: information for the identified peptides
	 *
	 * @return the information of a set of identified peptides
	 */
	public List<IdentifiedPeptide> getIdentifiedPeptides();

	/**
	 * Responsible person or role. The (stable) primary contact person for this
	 * data set; this could be the experimenter, lab head, line manager etc..
	 * Where responsibility rests with an institutional role (e.g. one of a
	 * number of duty officers) rather than a person, give the official name of
	 * the role rather than any one person. In all cases give the affiliation
	 * and stable contact information.
	 *
	 * @return the responsible person or role.
	 */
	@Override
	public MSContact getContact();

	/**
	 * Gets the additional information: Any other relevant information that you
	 * want to add to the document, and that anyhow has an impact on the
	 * interpretation of the result for that experiment.
	 *
	 * @return the additional information
	 */
	public Set<MSIAdditionalInformation> getAdditionalInformations();
}
