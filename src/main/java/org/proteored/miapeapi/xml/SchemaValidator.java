package org.proteored.miapeapi.xml;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.log4j.Logger;
import org.proteored.miapeapi.exceptions.WrongXMLFormatException;
import org.xml.sax.SAXException;

public class SchemaValidator {
	private static Logger log = Logger.getLogger("log4j.logger.org.proteored");
	public static String mzMLIdx = "mzML1.1.0_idx.xsd";
	public static String mzML = "mzML1.1.0.xsd";
	public static String mzIdentML_1_0 = "mzIdentML1.0.0.xsd";
	public static String mzIdentML_1_1 = "mzIdentML1.1.0.xsd";
	public static String mzIdentML_1_2 = "mzIdentML1.2.0.xsd";
	public static String prideXML = "pride.xsd";
	public static String miapeMSXML = "MIAPE_MS_XML_v1.2.xsd";
	public static String miapeMSIXML = "MIAPE_MSI_XML_v1.1.2.xsd";

	/**
	 * Check if a file match with a certain XML schema
	 * 
	 * @param inputFile
	 * @param schemaFile
	 *            (it should be in the resources folder)
	 */
	public static void validateXMLFile(File inputFile, String schemaFile)
			throws WrongXMLFormatException {
		boolean retval;
		log.info("Validating file=" + inputFile + " against " + schemaFile
				+ " schema");

		// 1. Lookup a factory for the W3C XML Schema language
		SchemaFactory factory = SchemaFactory
				.newInstance("http://www.w3.org/2001/XMLSchema");

		// 2. Compile the schema.
		URL schemaLocation;
		// Note: not checking against external schema, because of performance
		// and availability (internet connection) issues
		// try {
		// if (indexed) {
		schemaLocation = SchemaValidator.class.getClassLoader().getResource(
				schemaFile);
		// schemaLocation = new
		// URL("http://psidev.cvs.sourceforge.net/*checkout*/psidev/psi/psi-ms/mzML/schema/mzML1.1.0_idx.xsd");
		// } else {
		// schemaLocation =
		// this.getClass().getClassLoader().getResource("mzML1.1.0.xsd");
		// schemaLocation = new
		// URL("http://psidev.cvs.sourceforge.net/*checkout*/psidev/psi/psi-ms/mzML/schema/mzML1.1.0.xsd");
		// }
		// } catch (MalformedURLException e) {
		// throw new
		// IllegalStateException("Could not load external schema location!", e);
		// }
		// assertNotNull(schemaLocation);

		Schema schema;
		try {
			schema = factory.newSchema(schemaLocation);
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Could not compile Schema for file: " + schemaLocation);
			throw new IllegalStateException(
					"Could not compile Schema for file: " + schemaLocation);
		}

		// 3. Get a validator from the schema.
		Validator validator = schema.newValidator();

		// 4. Parse the document you want to check.
		Source source = new StreamSource(inputFile);

		// 5. Check the document (throws an Exception if not valid)
		try {
			validator.validate(source);
			retval = true;
		} catch (SAXException ex) {
			log.info(inputFile.getName() + " is not valid because ");
			log.info(ex.getMessage());
			throw new WrongXMLFormatException(ex.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			log.info("Could not validate file because of file read problems for source: "
					+ inputFile.getAbsolutePath());
			throw new WrongXMLFormatException(
					"Could not validate file because of file read problems for source: "
							+ inputFile.getAbsolutePath());
		}

	}
}
