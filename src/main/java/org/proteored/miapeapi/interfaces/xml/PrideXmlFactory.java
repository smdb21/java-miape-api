package org.proteored.miapeapi.interfaces.xml;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.interfaces.ms.MiapeMSDocument;
import org.proteored.miapeapi.interfaces.msi.MiapeMSIDocument;

public interface PrideXmlFactory {

	/**
	 * Function that converts a MIAPE MS and MIAPE MSI documents in a PRIDE
	 * file.
	 * 
	 * @param miapeMS
	 * @param miapeMSI
	 * @param controlVocabularyUtil
	 * @param xmlFile
	 * @param parseResultingData
	 * @return a MiapeXmlFile<MiapeDocument>, usually a MiapeFullPrideXMLFile
	 *         (in XML module)
	 */
	MiapePrideXmlFile toXml(MiapeMSDocument miapeMS, MiapeMSIDocument miapeMSI,
			ControlVocabularyManager controlVocabularyUtil, MiapePrideXmlFile xmlFile,
			boolean parseResultingData);

}
