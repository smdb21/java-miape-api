package org.proteored.miapeapi.xml.msi.adapter;

import org.proteored.miapeapi.cv.ms.ContactPositionMS;
import org.proteored.miapeapi.interfaces.Adapter;
import org.proteored.miapeapi.interfaces.Contact;
import org.proteored.miapeapi.xml.msi.autogenerated.MIAPEContactType;
import org.proteored.miapeapi.xml.msi.autogenerated.ObjectFactory;
import org.proteored.miapeapi.xml.msi.util.MSIControlVocabularyXmlFactory;

public class ContactAdapter implements Adapter<MIAPEContactType> {
	private final Contact contact;
	private final ObjectFactory factory;
	private final MSIControlVocabularyXmlFactory cvFactory;

	public ContactAdapter(Contact contact, ObjectFactory factory,
			MSIControlVocabularyXmlFactory cvFactory) {
		this.contact = contact;
		this.factory = factory;
		this.cvFactory = cvFactory;
	}

	@Override
	public MIAPEContactType adapt() {

		if (contact == null)
			return null;
		MIAPEContactType contactXML = factory.createMIAPEContactType();
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
				ContactPositionMS.getInstance(cvFactory.getCvManager())));
		contactXML.setTelephone(contact.getTelephone());
		return contactXML;
	}

}
