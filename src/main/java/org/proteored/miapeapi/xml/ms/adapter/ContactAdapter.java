package org.proteored.miapeapi.xml.ms.adapter;

import org.apache.log4j.Logger;
import org.proteored.miapeapi.cv.ge.ContactPositionGE;
import org.proteored.miapeapi.cv.ms.ContactPositionMS;
import org.proteored.miapeapi.interfaces.Adapter;
import org.proteored.miapeapi.interfaces.Contact;
import org.proteored.miapeapi.xml.ms.autogenerated.MIAPEContactType;
import org.proteored.miapeapi.xml.ms.autogenerated.ObjectFactory;
import org.proteored.miapeapi.xml.ms.util.MsControlVocabularyXmlFactory;

public class ContactAdapter implements Adapter<MIAPEContactType> {
	private final Contact contact;
	private final ObjectFactory factory;
	private final MsControlVocabularyXmlFactory cvFactory;
	private static Logger log = Logger.getLogger("log4j.logger.org.proteored");

	public ContactAdapter(Contact contact, ObjectFactory factory,
			MsControlVocabularyXmlFactory cvFactory) {
		this.contact = contact;
		this.factory = factory;
		this.cvFactory = cvFactory;
	}

	@Override
	public MIAPEContactType adapt() {
		log.info("adapting Contact");
		if (contact == null)
			return null;
		MIAPEContactType contactXML = factory.createMIAPEContactType();
		log.info("XML contact");
		contactXML.setAddress(contact.getAddress());
		contactXML.setCountry(contact.getCountry());
		contactXML.setCP(contact.getCP());
		contactXML.setDepartment(contact.getDepartment());
		contactXML.setEmail(contact.getEmail());
		contactXML.setFax(contact.getFax());
		contactXML.setInstitution(contact.getInstitution());
		contactXML.setLastName(contact.getLastName());
		contactXML.setLocality(contact.getLocality());
		contactXML.setName(contact.getName());
		contactXML.setPosition(cvFactory.createCV(contact.getPosition(), null,
				ContactPositionMS.getInstance(cvFactory.getCvManager()),
				ContactPositionGE.getInstance(cvFactory.getCvManager())));
		contactXML.setTelephone(contact.getTelephone());
		log.info("return XML contact");
		return contactXML;
	}

}
