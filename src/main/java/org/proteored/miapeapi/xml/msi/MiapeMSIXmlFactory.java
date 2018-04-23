package org.proteored.miapeapi.xml.msi;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;

import org.apache.log4j.Logger;
import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.exceptions.IllegalMiapeArgumentException;
import org.proteored.miapeapi.exceptions.MiapeDatabaseException;
import org.proteored.miapeapi.exceptions.MiapeSecurityException;
import org.proteored.miapeapi.exceptions.WrongXMLFormatException;
import org.proteored.miapeapi.interfaces.msi.MiapeMSIDocument;
import org.proteored.miapeapi.interfaces.persistence.PersistenceManager;
import org.proteored.miapeapi.interfaces.xml.MiapeXmlFile;
import org.proteored.miapeapi.interfaces.xml.XmlMiapeFactory;
import org.proteored.miapeapi.xml.msi.adapter.MiapeMsiXMLAdapter;
import org.proteored.miapeapi.xml.msi.autogenerated.MSIMIAPEMSI;

import edu.scripps.yates.utilities.files.ZipManager;

/**
 * Class that provides the methods to convert a MIAPE MSI XML to the MIAPE MSI
 * model and vice versa.
 *
 * @author Salvador
 *
 */
public class MiapeMSIXmlFactory implements XmlMiapeFactory<MiapeMSIDocument> {
	private static MiapeMSIXmlFactory instance;
	private static Logger log = Logger.getLogger("log4j.logger.org.proteored");

	private JAXBContext jc;
	private boolean processInParallel = true;

	private MiapeMSIXmlFactory() {
		try {
			jc = JAXBContext.newInstance("org.proteored.miapeapi.xml.msi.autogenerated");
		} catch (final JAXBException e) {
			e.printStackTrace();
		}
	}

	public MiapeMSIXmlFactory setProcessInParallel(boolean processInParallel) {
		this.processInParallel = processInParallel;
		return this;
	}

	private MiapeMSIXmlFactory(boolean processInParallel) {
		this();
		this.processInParallel = processInParallel;
	}

	public static MiapeMSIXmlFactory getFactory(boolean processInParallel) {
		if (instance == null) {
			instance = new MiapeMSIXmlFactory(processInParallel);
		}
		instance.processInParallel = processInParallel;
		return instance;
	}

	public static MiapeMSIXmlFactory getFactory() {
		if (instance == null) {
			instance = new MiapeMSIXmlFactory();
		}
		return instance;
	}

	private MiapeMSIDocument create(MiapeXmlFile<MiapeMSIDocument> xmlFile, PersistenceManager databaseManager,
			ControlVocabularyManager cvManager, String userName, String password, boolean processPeptidesInParallel)
			throws MiapeDatabaseException, MiapeSecurityException {
		MiapeMSIDocument result = null;
		try {
			log.debug("Before Unmarshalling the document from XML");
			final File file = ZipManager.decompressFileIfNeccessary(xmlFile.toFile(), false);
			final MSIMIAPEMSI miapeMSI = (MSIMIAPEMSI) jc.createUnmarshaller().unmarshal(file);
			if (databaseManager == null) {
				result = new MiapeMSIDocumentImpl(miapeMSI, null, cvManager, userName, password,
						processPeptidesInParallel);
			} else {
				result = new MiapeMSIDocumentImpl(miapeMSI, cvManager, databaseManager, userName, password,
						processPeptidesInParallel);
			}

			log.info("the document is unmarshalled");

		} catch (final JAXBException e) {
			e.printStackTrace();
			throw new WrongXMLFormatException("The files does not seen to have MIAPE MSI format");
		} catch (final Exception e) {
			e.printStackTrace();
			throw new WrongXMLFormatException(e);
		}
		return result;
	}

	private MiapeXmlFile<MiapeMSIDocument> create(MiapeMSIDocument document, ControlVocabularyManager cvManager,
			boolean processPeptidesInParallel) {
		File xmlFile = null;
		try {
			xmlFile = File.createTempFile("msiXML", ".temp");
			log.debug("Before marshalling the document to XML");
			log.info("The id before marshalling the document to XML is " + document.getId());

			final MSIMIAPEMSI miapeMSI = new MiapeMsiXMLAdapter(document, cvManager, processPeptidesInParallel).adapt();
			final Marshaller marshaller = jc.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, new Boolean(true));
			try {
				marshaller.setProperty("com.sun.xml.bind.indentString", "\t");
			} catch (final PropertyException e) {
				marshaller.setProperty("com.sun.xml.internal.bind.indentString", "\t");
			}
			marshaller.marshal(miapeMSI, xmlFile);
			final MIAPEMSIXmlFile miapemsiXmlFile = new MIAPEMSIXmlFile(xmlFile);
			miapemsiXmlFile.setCvUtil(cvManager);
			log.debug("the document is marshalled");
			return miapemsiXmlFile;

		} catch (final JAXBException e) {
			e.printStackTrace();
			log.error(e);
		} catch (final IOException e) {
			e.printStackTrace();
			log.error(e);

		}
		return null;
	}

	@Override
	public MiapeXmlFile<MiapeMSIDocument> toXml(MiapeMSIDocument document, ControlVocabularyManager cvManager) {
		if (cvManager == null)
			throw new IllegalMiapeArgumentException("ControlVocabularyManager is not set");
		return getFactory().create(document, cvManager, true);
	}

	public MiapeXmlFile<MiapeMSIDocument> toXml(MiapeMSIDocument document, ControlVocabularyManager cvManager,
			boolean processPeptidesInParallel) {
		if (cvManager == null)
			throw new IllegalMiapeArgumentException("ControlVocabularyManager is not set");
		return getFactory().create(document, cvManager, processPeptidesInParallel);
	}

	@Override
	public MiapeMSIDocument toDocument(MiapeXmlFile<MiapeMSIDocument> xmlFile, ControlVocabularyManager cvManager,
			PersistenceManager db, String user, String password) throws MiapeDatabaseException, MiapeSecurityException {
		if (cvManager == null)
			throw new IllegalMiapeArgumentException("ControlVocabularyManager is not set");
		return getFactory().create(xmlFile, db, cvManager, user, password, processInParallel);
	}

}
