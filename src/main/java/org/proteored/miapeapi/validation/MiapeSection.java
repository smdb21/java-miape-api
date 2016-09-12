package org.proteored.miapeapi.validation;

import org.proteored.miapeapi.interfaces.MiapeDocument;

public interface MiapeSection {
	public String getId();
	public String getDescription();
	public boolean validate(MiapeDocument miape);
}
