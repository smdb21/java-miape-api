package org.proteored.miapeapi.validation.ge;

import org.proteored.miapeapi.interfaces.MiapeDocument;
import org.proteored.miapeapi.interfaces.ge.MiapeGEDocument;
import org.proteored.miapeapi.validation.MiapeSection;

public enum MiapeGESection implements MiapeSection {
//	TODO Add all the sections 

	MIAPE_CONTACT("MIAPE_CONTACT", "Contact") { 
		@Override
		public boolean validate(MiapeDocument miape) {
			MiapeGEDocument document = (MiapeGEDocument) miape;
			if (document.getContact() == null) return false;
			return document.getContact().getName() != null && document.getContact().getEmail() != null;
		}		
	};
	
	private final String description;
	private final String id;
	private MiapeGESection(String id, String description) {
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


