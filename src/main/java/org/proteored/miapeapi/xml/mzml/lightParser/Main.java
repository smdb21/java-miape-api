/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */

package org.proteored.miapeapi.xml.mzml.lightParser;

import org.proteored.miapeapi.xml.mzml.MiapeMSDocumentImpl;
import org.proteored.miapeapi.xml.mzml.lightParser.utils.MzMLLightParser;

import uk.ac.ebi.jmzml.model.mzml.MzML;

/**
 * 
 * @author Alberto
 */
public class Main {

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) {
		// TODO code application logic here
		// TMain myinterface = new TMain();
		// myinterface.setVisible(true);//
		// sin interface
		// MzMLLightParser myTestParser = new
		// MzMLLightParser("CNB_iTRAQ_MALDI_1.mzML");
		final String _filename = "MRM_example_1.1.0.mzML";
		MzMLLightParser myTestParser = new MzMLLightParser(_filename);
		MzML mzmlLight = myTestParser.ParseDocument(false);
		// Salida

		System.out.println("--------------------------------");
		System.out.println(new MiapeMSDocumentImpl(mzmlLight, null, _filename, "proyecto").toXml());

	}

}
