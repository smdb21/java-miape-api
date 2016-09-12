package org.proteored.miapeapi.interfaces.ms;

import org.proteored.miapeapi.cv.ms.ContactPositionMS;
import org.proteored.miapeapi.interfaces.Contact;

public interface MSContact extends Contact {
	/**
	 * Gets the position of the contact. It should be one of the possible values
	 * from {@link ContactPositionMS}
	 */
	public String getPosition();
}
