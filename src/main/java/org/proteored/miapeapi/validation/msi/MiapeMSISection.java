package org.proteored.miapeapi.validation.msi;

import org.proteored.miapeapi.interfaces.MiapeDocument;
import org.proteored.miapeapi.interfaces.msi.MiapeMSIDocument;
import org.proteored.miapeapi.validation.MiapeSection;

public enum MiapeMSISection implements MiapeSection {
//	TODO Add all the sections 

	MIAPE_CONTACT("MIAPE_CONTACT", "Contact") { 
		@Override
		public boolean validate(MiapeDocument miape) {
			MiapeMSIDocument document = (MiapeMSIDocument) miape;
			if (document.getContact() == null) return false;
			return document.getContact().getName() != null && document.getContact().getEmail() != null;
		}		
	};
	private final String description;
	private final String id;
	private MiapeMSISection(String id, String description) {
		this.description = description;
		this.id = id;
	}
	@Override
	public String getDescription() {
 		return description;
	}

	@Override
	public String getId() {
		return id;
	}
	
}

