package org.proteored.miapeapi.validation.ms;

import org.proteored.miapeapi.interfaces.MiapeDocument;
import org.proteored.miapeapi.interfaces.ms.MiapeMSDocument;
import org.proteored.miapeapi.validation.MiapeSection;

public enum MiapeMSSection implements MiapeSection {
//	TODO Add all the sections 

	MIAPE_CONTACT("MIAPE_CONTACT", "Contact") { 
		@Override
		public boolean validate(MiapeDocument miape) {
			MiapeMSDocument document = (MiapeMSDocument) miape;
			if (document.getContact() == null) return false;
			return document.getContact().getName() != null && document.getContact().getEmail() != null;
		}		
	};
	private final String description;
	private final String id;
	private MiapeMSSection(String id, String description) {
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
