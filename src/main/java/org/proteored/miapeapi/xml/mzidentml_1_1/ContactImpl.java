package org.proteored.miapeapi.xml.mzidentml_1_1;

import java.util.List;

import org.proteored.miapeapi.interfaces.User;
import org.proteored.miapeapi.interfaces.ms.MSContact;

import uk.ac.ebi.jmzidml.model.mzidml.AbstractContact;
import uk.ac.ebi.jmzidml.model.mzidml.Affiliation;
import uk.ac.ebi.jmzidml.model.mzidml.AuditCollection;
import uk.ac.ebi.jmzidml.model.mzidml.ContactRole;
import uk.ac.ebi.jmzidml.model.mzidml.CvParam;
import uk.ac.ebi.jmzidml.model.mzidml.Organization;
import uk.ac.ebi.jmzidml.model.mzidml.Person;
import uk.ac.ebi.jmzidml.model.mzidml.Provider;

public class ContactImpl implements MSContact {
	private String email;
	private String institution;
	private final String position;
	private String address;
	private String fax;
	private String phone;
	private final NameCreator nameCreator;
	private static final String emailCV = "MS:1000589";
	private static final String addressCV = "MS:1000587";
	private static final String faxCV = "MS:1001756";
	private static final String phoneCV = "MS:1001755";

	public ContactImpl(Provider provider, AuditCollection auditCollection, List<AbstractContact> mzIdentContactList,
			User user) {

		ContactRole contactRole = provider.getContactRole();
		if (contactRole != null) {
			if (contactRole.getRole() != null) {
				position = contactRole.getRole().getCvParam().getName();
			} else {
				position = null;
			}
			if (contactRole.getRole() != null && auditCollection != null) {
				final AbstractContact abstractContact = contactRole.getContact();
				if (abstractContact != null) {
					if (abstractContact instanceof Person) {
						Person contactDetails = (Person) abstractContact;
						nameCreator = new NameCreator(contactDetails.getName(), contactDetails.getLastName(), user);
						for (CvParam cvParam : contactDetails.getCvParam()) {
							final String accession = cvParam.getAccession();
							if (accession.equals(emailCV))
								email = cvParam.getValue();
							if (accession.equals(addressCV))
								address = cvParam.getValue();
							if (accession.equals(faxCV))
								fax = cvParam.getValue();
							if (accession.equals(phoneCV))
								phone = cvParam.getValue();
							institution = getContactPosition(contactDetails);
						}
					} else if (abstractContact instanceof Organization) {
						Organization contactDetails = (Organization) abstractContact;
						nameCreator = new NameCreator(contactDetails.getName(), null, user);
						for (CvParam cvParam : contactDetails.getCvParam()) {
							final String accession = cvParam.getAccession();
							if (accession.equals(emailCV))
								email = cvParam.getValue();
							if (accession.equals(addressCV))
								address = cvParam.getValue();
							if (accession.equals(faxCV))
								fax = cvParam.getValue();
							if (accession.equals(phoneCV))
								phone = cvParam.getValue();
							institution = contactDetails.getName();
						}
					} else {
						nameCreator = null;
					}
				} else {
					nameCreator = new NameCreator(user);
					email = null;
					address = null;
					fax = null;
					phone = null;
					institution = null;
				}
			} else {
				nameCreator = new NameCreator(user);
				email = null;
				address = null;
				fax = null;
				phone = null;
				institution = null;
			}
		} else {
			position = null;
			nameCreator = new NameCreator(provider.getId(), null, null);
		}
	}

	@Override
	public String getAddress() {
		return address;
	}

	@Override
	public String getCP() {
		return null;
	}

	@Override
	public String getCountry() {
		return null;
	}

	@Override
	public String getDepartment() {
		return null;
	}

	@Override
	public String getEmail() {
		return email;
	}

	@Override
	public String getFax() {
		return fax;
	}

	@Override
	public String getInstitution() {
		return institution;
	}

	@Override
	public String getLastName() {
		return nameCreator.getLastName();
	}

	@Override
	public String getLocality() {
		return null;
	}

	@Override
	public String getName() {
		return nameCreator.getName();

	}

	@Override
	public String getPosition() {
		return position;
	}

	@Override
	public String getTelephone() {
		return phone;
	}

	private String getContactPosition(Person contactDetails) {
		List<Affiliation> affiliations = contactDetails.getAffiliation();
		if (contactDetails.getAffiliation() != null && !contactDetails.getAffiliation().isEmpty()) {
			Affiliation aff = affiliations.get(0);
			final Organization organization = aff.getOrganization();
			if (organization != null)
				return organization.getName();
		}
		return null;
	}

	// private String getInstitution(String orgRef) {
	// for (FuGECommonAuditContactType contact : listContacts) {
	// if (contact instanceof FuGECommonAuditOrganizationType) {
	// FuGECommonAuditOrganizationType organization =
	// (FuGECommonAuditOrganizationType) contact;
	// if (organization.getId().equals(orgRef)) {
	// return organization.getName();
	// }
	// }
	// }
	// return null;
	// }

	private static class NameCreator {
		private String name;
		private String lastName;
		private User user;

		public NameCreator(User user) {
			this.user = user;
		}

		public NameCreator(String name, String lastName, User user) {
			this(user);
			if (name != null) {
				this.name = name;
			}
			if (lastName != null) {
				this.lastName = lastName;
			}

		}

		public String getName() {
			if (name != null) {
				return name;
			}
			// else if (lastName != null) {
			// return lastName;
			// }
			if (user != null) {
				return user.getUserName();
			}
			return "Unknown";
		}

		public String getLastName() {
			return lastName;
		}

	}

}
