package org.proteored.miapeapi.xml;

import org.proteored.miapeapi.interfaces.ge.MiapeGEDocument;
import org.proteored.miapeapi.interfaces.gi.MiapeGIDocument;
import org.proteored.miapeapi.interfaces.ms.MiapeMSDocument;
import org.proteored.miapeapi.interfaces.msi.MiapeMSIDocument;
import org.proteored.miapeapi.interfaces.xml.PrideXmlFactory;
import org.proteored.miapeapi.interfaces.xml.XmlManager;
import org.proteored.miapeapi.interfaces.xml.XmlMiapeFactory;
import org.proteored.miapeapi.interfaces.xml.XmlProjectFactory;
import org.proteored.miapeapi.xml.ge.MiapeGEXmlFactory;
import org.proteored.miapeapi.xml.gi.MiapeGIXmlFactory;
import org.proteored.miapeapi.xml.ms.MiapeMSXmlFactory;
import org.proteored.miapeapi.xml.msi.MiapeMSIXmlFactory;
import org.proteored.miapeapi.xml.mzidentml.MzIdentmlXmlFactory;
import org.proteored.miapeapi.xml.pride.PrideXmlFactoryImpl;

/**
 * Implementation of the {@link XmlManager} that provides the XmlMiapeFactory
 * objects for each one of the MIAPE document type.
 * 
 * @author Salva
 * 
 */
public class DefaultXmlManager implements XmlManager {
	private static DefaultXmlManager instance;

	public static DefaultXmlManager getInstance() {
		if (instance == null) {
			return instance = new DefaultXmlManager();
		}
		return instance;
	}

	@Override
	public XmlMiapeFactory<MiapeGEDocument> getMiapeGEXmlFactory() {
		return MiapeGEXmlFactory.getFactory();
	}

	@Override
	public XmlMiapeFactory<MiapeGIDocument> getMiapeGIXmlFactory() {
		return MiapeGIXmlFactory.getFactory();
	}

	@Override
	public XmlMiapeFactory<MiapeMSDocument> getMiapeMSXmlFactory() {
		return MiapeMSXmlFactory.getFactory();
	}

	@Override
	public XmlMiapeFactory<MiapeMSIDocument> getMiapeMSIXmlFactory() {
		return MiapeMSIXmlFactory.getFactory();
	}

	@Override
	public XmlProjectFactory getProjectXmlFactory() {
		return ProjectXmlFactory.getFactory();
	}

	@Override
	public PrideXmlFactory getPrideFactory() {
		return PrideXmlFactoryImpl.getFactory();
	}

	@Override
	public XmlMiapeFactory<MiapeMSIDocument> getMzIdentMLFactory() {
		return MzIdentmlXmlFactory.getFactory();
	}

}
