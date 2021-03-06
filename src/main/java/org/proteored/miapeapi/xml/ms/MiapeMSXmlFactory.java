package org.proteored.miapeapi.xml.ms;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.exceptions.IllegalMiapeArgumentException;
import org.proteored.miapeapi.exceptions.MiapeDatabaseException;
import org.proteored.miapeapi.exceptions.MiapeSecurityException;
import org.proteored.miapeapi.exceptions.WrongXMLFormatException;
import org.proteored.miapeapi.interfaces.ms.MiapeMSDocument;
import org.proteored.miapeapi.interfaces.persistence.PersistenceManager;
import org.proteored.miapeapi.interfaces.xml.MiapeXmlFile;
import org.proteored.miapeapi.interfaces.xml.XmlMiapeFactory;
import org.proteored.miapeapi.xml.ms.adapter.MiapeMsXMLAdapter;
import org.proteored.miapeapi.xml.ms.autogenerated.MSMIAPEMS;

import edu.scripps.yates.utilities.files.ZipManager;

/**
 * Class that provides the methods to convert a MIAPE MS XML to the MIAPE MS
 * model and vice versa.
 * 
 * @author Salvador
 * 
 */
public class MiapeMSXmlFactory implements XmlMiapeFactory<MiapeMSDocument> {
	private static MiapeMSXmlFactory instance;
	private JAXBContext jc;

	private MiapeMSXmlFactory() {
		try {
			jc = JAXBContext.newInstance("org.proteored.miapeapi.xml.ms.autogenerated");

		} catch (final JAXBException e) {
			throw new IllegalMiapeArgumentException(e);
		}
	}

	public static MiapeMSXmlFactory getFactory() {
		if (instance == null) {
			instance = new MiapeMSXmlFactory();
		}
		return instance;

	}

	private MiapeMSDocument create(MiapeXmlFile<MiapeMSDocument> xmlFile, PersistenceManager databaseManager,
			ControlVocabularyManager cvManager, String userName, String password)
			throws MiapeDatabaseException, MiapeSecurityException, IllegalMiapeArgumentException {
		MiapeMSDocument result = null;
		try {
			final Unmarshaller unmarshaller = jc.createUnmarshaller();
			final File file = ZipManager.decompressFileIfNeccessary(xmlFile.toFile(), false);
			final MSMIAPEMS miapeMS = (MSMIAPEMS) unmarshaller.unmarshal(file);
			if (databaseManager == null) {
				result = new MiapeMSDocumentImpl(miapeMS, null, cvManager, userName, password);
			} else {
				result = new MiapeMSDocumentImpl(miapeMS, cvManager, databaseManager, userName, password);
			}
		} catch (final JAXBException e) {
			e.printStackTrace();
			throw new WrongXMLFormatException("The file does not seem to have MIAPE XML MS format");
		} catch (final Exception e) {
			e.printStackTrace();
			throw new WrongXMLFormatException(e);
		}

		return result;
	}

	private MiapeXmlFile<MiapeMSDocument> create(MiapeMSDocument document, ControlVocabularyManager cvManager) {
		File xmlFile = null;

		try {
			final Marshaller marshaller = jc.createMarshaller();
			xmlFile = File.createTempFile("msXML", ".temp");
			xmlFile.deleteOnExit();
			final MSMIAPEMS miapeMS = new MiapeMsXMLAdapter(document, cvManager).adapt();

			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, new Boolean(true));
			try {
				marshaller.setProperty("com.sun.xml.bind.indentString", "\t");
			} catch (final PropertyException e) {
				marshaller.setProperty("com.sun.xml.internal.bind.indentString", "\t");
			}
			marshaller.marshal(miapeMS, xmlFile);
			final MIAPEMSXmlFile miapemsXmlFile = new MIAPEMSXmlFile(xmlFile);
			miapemsXmlFile.setCvUtil(cvManager);
			return miapemsXmlFile;
		} catch (final JAXBException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return null;

	}

	@Override
	public MiapeXmlFile<MiapeMSDocument> toXml(MiapeMSDocument document, ControlVocabularyManager cvManager) {
		if (cvManager == null)
			throw new IllegalMiapeArgumentException("ControlVocabularyManager is not set");
		return getFactory().create(document, cvManager);
	}

	@Override
	public MiapeMSDocument toDocument(MiapeXmlFile<MiapeMSDocument> xmlFile, ControlVocabularyManager cvManager,
			PersistenceManager db, String userName, String password)
			throws MiapeDatabaseException, MiapeSecurityException {
		if (cvManager == null)
			throw new IllegalMiapeArgumentException("ControlVocabularyManager is not set");
		return getFactory().create(xmlFile, db, cvManager, userName, password);
	}

}
