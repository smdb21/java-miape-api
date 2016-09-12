package org.proteored.miapeapi.interfaces.gi;

import java.util.Set;

import org.proteored.miapeapi.cv.ge.ElectrophoresisType;
import org.proteored.miapeapi.interfaces.MiapeDocument;
import org.proteored.miapeapi.interfaces.ge.GEContact;
import org.proteored.miapeapi.interfaces.xml.XmlEntity;

/**
 * Interface for the MIAPE Gel Informatics document
 * 
 * @author Salvador
 * 
 */
public interface MiapeGIDocument extends MiapeDocument,
		XmlEntity<MiapeGIDocument> {

	/**
	 * Reference to the MIAPE GE document The project or experiment for which
	 * the present analysis was initiated. A reference to the relevant instance
	 * of the MIAPE Gel Electrophoresis documents should be given here if
	 * available.
	 * 
	 * @return the identifier of the MIAPE GE document
	 */
	public int getGEDocumentReference();

	/**
	 * Electrophoresis type. A description of the gel electrophoresis experiment
	 * type; such as a two-dimensional gel, a two-dimensional differential
	 * electrophoresis gel. The value should be one of the possible values in
	 * {@link ElectrophoresisType}
	 * 
	 * @return the electrophoresis type
	 */

	public String getElectrophoresisType();

	/**
	 * Reference. Reference to the project or experiment for which the present
	 * analysis was initiated
	 * 
	 * @return the reference
	 */
	public String getReference();

	/**
	 * The (stable) primary contact person for this data set; this could be the
	 * experimenter, lab head, line manager etc. Where responsibility rests with
	 * an institutional role (e.g. one of a number of duty officers) rather than
	 * a person, give the official name of the role rather than any one person.
	 * In all cases give affiliation and stable contact information.
	 */
	@Override
	public GEContact getContact();

	/**
	 * Indicate image names and identifiers. Describe the type of input image(s)
	 * (for example visible light scan, synthetic image or fluorescent scans).
	 * Information related to the image acquisition is covered in the related
	 * MIAPE Gel Electrophoresis document (Section 7). If not, provide details
	 * as per Section 7 of MIAPE Gel Electrophoresis
	 * 
	 */
	public Set<ImageGelInformatics> getImages();

	/**
	 * gets the image analysis software
	 * 
	 * @return the image analysis software
	 */
	public Set<ImageAnalysisSoftware> getImageAnalysisSoftwares();

	/**
	 * The design with respect to number and type of gels, such as directed or
	 * blind, to capture the overall design of the experiment
	 * 
	 */
	public Set<AnalysisDesign> getAnalysisDesigns();

	/**
	 * Software used for the manipulation, if different from the analysis
	 * software defined in Section 1. Software name and version number, vendor
	 * (or if not available, provide a literature reference or a URI).
	 * 
	 */
	public Set<ImageAnalysisSoftware> getImagePreparationSoftwares();

	/**
	 * The editing steps (usually untraceable) performed on the image.
	 * 
	 */
	public Set<ImagePreparationStep> getImagePreparationSteps();

	/**
	 * Any processing performed by the bioinformatics software to prepare the
	 * images for data extraction. List the image processing steps performed
	 * with parameters and their relative order.
	 * 
	 */
	public Set<ImageProcessing> getImageProcessings();

	/**
	 * State the name of the data extraction process, including the steps and
	 * order. For the following possible processes, describe whenever used.
	 * 
	 */
	public Set<DataExtraction> getDataExtractions();

	/**
	 * analysis protocol used, repeat the steps as necessary, that created the
	 * data reported in Section 7.
	 * 
	 */
	public Set<DataAnalysis> getDataAnalysises();

	/**
	 * List of image features, matches and analysis results
	 * 
	 */
	public Set<DataReporting> getDataReportings();

	/**
	 * Gets the additional information: Any other relevant information that you
	 * want to add to the document, and that anyhow has an impact on the
	 * interpretation of the result for that experiment.
	 * 
	 * @return the additional information
	 */
	public Set<GIAdditionalInformation> getAdditionalInformations();

}
