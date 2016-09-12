package org.proteored.miapeapi.xml.mzidentml;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.log4j.Logger;
import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.exceptions.MiapeDatabaseException;
import org.proteored.miapeapi.exceptions.MiapeSecurityException;
import org.proteored.miapeapi.exceptions.WrongXMLFormatException;
import org.proteored.miapeapi.interfaces.msi.MiapeMSIDocument;
import org.proteored.miapeapi.interfaces.persistence.PersistenceManager;
import org.proteored.miapeapi.interfaces.xml.MiapeXmlFile;
import org.proteored.miapeapi.interfaces.xml.XmlMiapeFactory;
import org.proteored.miapeapi.xml.mzidentml.adapter.MzIdentmlXMLAdapter;
import org.proteored.miapeapi.xml.mzidentml.autogenerated.MzIdentML;

public class MzIdentmlXmlFactory implements XmlMiapeFactory<MiapeMSIDocument> {
	private static MzIdentmlXmlFactory instance;
	private static Logger log = Logger.getLogger("log4j.logger.org.proteored");
	private JAXBContext jc;

	private MzIdentmlXmlFactory() {
		try {
			jc = JAXBContext.newInstance("org.proteored.miapeapi.xml.mzidentml.autogenerated");

		} catch (JAXBException e) {
			e.printStackTrace();
			log.error(e);
			throw new WrongXMLFormatException("The files does not seem to have MSMiape Format");
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);

			throw new WrongXMLFormatException(e);
		}
	}

	public static MzIdentmlXmlFactory getFactory() {
		if (instance == null) {
			instance = new MzIdentmlXmlFactory();
		}
		return instance;
	}

	@Override
	public MiapeXmlFile<MiapeMSIDocument> toXml(MiapeMSIDocument miapeMSI,
			ControlVocabularyManager controlVocabularyUtil) {
		MiapeMzIdentMLFile xmlFile = new MiapeMzIdentMLFile("mzIdentML_temp");
		try {
			log.info("creating XML");
			MzIdentML mzIdentML = new MzIdentmlXMLAdapter(miapeMSI, controlVocabularyUtil).adapt();
			Marshaller marshaller = jc.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, new Boolean(true));
			log.info("adapted to XML: " + mzIdentML);
			marshaller.marshal(mzIdentML, xmlFile.toFile());
			log.info("created file");
		} catch (JAXBException e) {
			e.printStackTrace();
			log.error(e);
			throw new WrongXMLFormatException("The files does not seem to have mzIdentML Format");
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
			throw new WrongXMLFormatException(e);
		}
		return xmlFile;
	}

	@Override
	public MiapeMSIDocument toDocument(MiapeXmlFile<MiapeMSIDocument> xmlFile,
			ControlVocabularyManager controlVocabularyUtil, PersistenceManager db, String userName,
			String password) throws MiapeDatabaseException, MiapeSecurityException {
		return xmlFile.toDocument();

	}

}
