package org.proteored.miapeapi.xml.mzml.lightParser.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.apache.log4j.Logger;
import org.proteored.miapeapi.exceptions.IllegalMiapeArgumentException;
import org.proteored.miapeapi.exceptions.WrongXMLFormatException;
import org.proteored.miapeapi.xml.SchemaValidator;

import uk.ac.ebi.jmzml.model.mzml.MzML;

/**
 * 
 * @author Alberto
 */
public class MzMLLightParser {
	private static Logger log = Logger.getLogger("log4j.logger.org.proteored");
	protected String inputfilename;
	private TParser parser;

	public MzMLLightParser() {
		inputfilename = "";
	}

	public MzMLLightParser(String _filename) {
		inputfilename = _filename;
	}

	public MzML ParseDocument(boolean validate) {
		int _ret_aux = 0;

		File infile = new File(inputfilename);

		// validate file against schemas
		try {
			if (validate)
				SchemaValidator.validateXMLFile(infile, SchemaValidator.mzMLIdx);
		} catch (Exception ex) {
			if (validate)
				SchemaValidator.validateXMLFile(infile, SchemaValidator.mzML);
		}

		try {
			FileInputStream inStream = new FileInputStream(infile);
		} catch (FileNotFoundException e) {
			throw new WrongXMLFormatException("Error reading mzML file.");
		}
		// PRIDEInstance = new TParser(inStream);
		// _ret_aux = PRIDEInstance.readPRIDEXML(-1);
		// myelements.addElement("GelFreeIdentification");
		// myelements.addElement("GelFreeIdentification");
		try {
			parser = new TParser();
			parser.SetUri(inputfilename);
			parser.parseDocument();

			// TMixedParsing mymixed = new TMixedParsing();
			// mymixed.parseDocument(filename);
			log.info("MzML file EOF");
			log.info(" Entities count ");

		} catch (Exception ex) {
			// ex.printStackTrace();
			throw new IllegalMiapeArgumentException("Error reading mzML file.");
		}
		// PRIDESAXInstance = new TPRIDESAXparser(inStream);
		// PRIDESAXInstance.runExample();
		log.info(new StringBuilder().append("mzML parsed:\n")
				.append(parser.getmzML().getXMLEntry(0)).toString());
		MzML mzmlLight = parser.getmzML().translate2jmzML();
		return mzmlLight;
	}

	public String getInputFilename() {
		return (inputfilename);
	}

	public void setInputFilename(String _filename) {
		inputfilename = _filename;
	}

}
