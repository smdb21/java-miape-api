package org.proteored.miapeapi.interfaces.ge;

import org.proteored.miapeapi.cv.ge.ContactPositionGE;
import org.proteored.miapeapi.interfaces.Contact;

public interface GEContact extends Contact {
	/**
	 * Gets the position of the contact. It should be one of the possible values
	 * from {@link ContactPositionGE}
	 */
	public String getPosition();
}
