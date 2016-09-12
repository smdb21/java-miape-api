package org.proteored.miapeapi.interfaces.xml;

import org.proteored.miapeapi.interfaces.ge.MiapeGEDocument;
import org.proteored.miapeapi.interfaces.gi.MiapeGIDocument;
import org.proteored.miapeapi.interfaces.ms.MiapeMSDocument;
import org.proteored.miapeapi.interfaces.msi.MiapeMSIDocument;

/**
 * Provide the getMIAPE??XmlFactory() functions.
 * 
 * @author Salva
 * 
 */
public interface XmlManager {
	public XmlMiapeFactory<MiapeMSDocument> getMiapeMSXmlFactory();

	public XmlMiapeFactory<MiapeMSIDocument> getMiapeMSIXmlFactory();

	public XmlMiapeFactory<MiapeGEDocument> getMiapeGEXmlFactory();

	public XmlMiapeFactory<MiapeGIDocument> getMiapeGIXmlFactory();

	public XmlProjectFactory getProjectXmlFactory();

	public PrideXmlFactory getPrideFactory();

	public XmlMiapeFactory<MiapeMSIDocument> getMzIdentMLFactory();
}
