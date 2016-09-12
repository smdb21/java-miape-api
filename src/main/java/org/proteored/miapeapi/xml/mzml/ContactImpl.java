package org.proteored.miapeapi.xml.mzml;

import java.util.List;

import org.proteored.miapeapi.cv.Accession;
import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ms.ContactPositionMS;
import org.proteored.miapeapi.interfaces.MatchMode;
import org.proteored.miapeapi.interfaces.User;
import org.proteored.miapeapi.interfaces.ms.MSContact;
import org.proteored.miapeapi.xml.mzml.util.MzMLControlVocabularyXmlFactory;

import uk.ac.ebi.jmzml.model.mzml.ParamGroup;
import uk.ac.ebi.jmzml.model.mzml.ReferenceableParamGroupList;

public class ContactImpl implements MSContact {
	private static final String[] FAX_TEXT = { "fax" };
	private static final String[] PHONE_TEXT = { "phone", "telephone" };
	private static final String[] COUNTRY_TEXT = { "country" };
	private static final String[] LOCALITY_TEXT = { "locality", "city", "town", "village" };
	private static final String[] DEPARTMENT_TEXT = { "department", "laboratory" };
	private static final String[] CP_TEXT = { "cp", "postal code" };
	private String email = null;
	private String institution = null;
	private String name = null;
	private String lastname = null;
	private String position = null;
	private String address = null;
	private String fax = null;
	private String phone = null;
	private String locality = null;
	private String country = null;
	private String cp = null;
	private String department = null;
	private String url = null;

	private final Accession CONTACT_NAME_CV_ID = new Accession("MS:1000586");
	private final Accession CONTACT_ADDRESS_CV_ID = new Accession("MS:1000587");
	private final Accession CONTACT_URL_CV_ID = new Accession("MS:1000588");
	private final Accession CONTACT_EMAIL_CV_ID = new Accession("MS:1000589");
	private final Accession CONTACT_ORGANIZATION_CV_ID = new Accession("MS:1000590");

	/**
	 * @param mzMLContacts
	 * @param user
	 */
	public ContactImpl(List<ParamGroup> mzMLContacts,
			ReferenceableParamGroupList referenceableParamGroupList, User user,
			ControlVocabularyManager cvManager) {

		for (ParamGroup paramGroup : mzMLContacts) {
			if (email == null)
				email = MzMLControlVocabularyXmlFactory.getValueFromParamGroupByAccession(
						paramGroup, referenceableParamGroupList, CONTACT_EMAIL_CV_ID);
			if (institution == null)
				institution = MzMLControlVocabularyXmlFactory.getValueFromParamGroupByAccession(
						paramGroup, referenceableParamGroupList, CONTACT_ORGANIZATION_CV_ID);
			if (name == null)
				name = MzMLControlVocabularyXmlFactory.getValueFromParamGroupByAccession(
						paramGroup, referenceableParamGroupList, CONTACT_NAME_CV_ID);
			if (position == null)
				position = MzMLControlVocabularyXmlFactory.getValueFromParamGroup(paramGroup,
						referenceableParamGroupList, ContactPositionMS.getInstance(cvManager));
			if (address == null)
				address = MzMLControlVocabularyXmlFactory.getValueFromParamGroupByAccession(
						paramGroup, referenceableParamGroupList, CONTACT_ADDRESS_CV_ID);
			if (url == null)
				url = MzMLControlVocabularyXmlFactory.getValueFromParamGroupByAccession(paramGroup,
						referenceableParamGroupList, CONTACT_URL_CV_ID);
			if (fax == null)
				fax = MzMLControlVocabularyXmlFactory.getValueFromParamGroupByName(paramGroup,
						referenceableParamGroupList, FAX_TEXT, MatchMode.EXACT);
			if (phone == null)
				phone = MzMLControlVocabularyXmlFactory.getValueFromParamGroupByName(paramGroup,
						referenceableParamGroupList, PHONE_TEXT, MatchMode.EXACT);
			if (locality == null)
				locality = MzMLControlVocabularyXmlFactory.getValueFromParamGroupByName(paramGroup,
						referenceableParamGroupList, LOCALITY_TEXT, MatchMode.EXACT);
			if (country == null)
				country = MzMLControlVocabularyXmlFactory.getValueFromParamGroupByName(paramGroup,
						referenceableParamGroupList, COUNTRY_TEXT, MatchMode.EXACT);
			if (cp == null)
				cp = MzMLControlVocabularyXmlFactory.getValueFromParamGroupByName(paramGroup,
						referenceableParamGroupList, CP_TEXT, MatchMode.EXACT);
			if (department == null)
				department = MzMLControlVocabularyXmlFactory.getValueFromParamGroupByName(
						paramGroup, referenceableParamGroupList, DEPARTMENT_TEXT, MatchMode.EXACT);
		}
		if (name == null) {
			if (user != null) {

				// get from user
				name = user.getName();
				lastname = user.getLastName();
			} else {
				name = "Unknown";
			}
		}
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getLastName() {
		return lastname;
	}

	@Override
	public String getTelephone() {
		return phone;
	}

	@Override
	public String getFax() {
		return fax;
	}

	@Override
	public String getEmail() {
		return email;
	}

	@Override
	public String getAddress() {
		return address;
	}

	@Override
	public String getLocality() {
		return locality;
	}

	@Override
	public String getCountry() {
		return country;
	}

	@Override
	public String getCP() {
		return cp;
	}

	@Override
	public String getDepartment() {
		String ret = department;
		if (url != null) {
			if (ret != null)
				ret = ret + "\n" + url;
			else
				ret = url;
		}
		return ret;
	}

	@Override
	public String getInstitution() {
		return institution;
	}

	@Override
	public String getPosition() {
		return position;
	}

}
