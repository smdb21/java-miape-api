package org.proteored.miapeapi.validation.gi;

import org.proteored.miapeapi.interfaces.MiapeDocument;
import org.proteored.miapeapi.interfaces.gi.MiapeGIDocument;
import org.proteored.miapeapi.validation.MiapeSection;

public enum MiapeGISection implements MiapeSection {
//	TODO Add all the sections 
	MIAPE_CONTACT("MIAPE_CONTACT", "Contact") { 
		@Override
		public boolean validate(MiapeDocument miape) {
			MiapeGIDocument document = (MiapeGIDocument) miape;
			if (document.getContact() == null) return false;
			return document.getContact().getName() != null && document.getContact().getEmail() != null;
		}		
	};
	private final String description;
	private final String id;
	private MiapeGISection(String id, String description) {
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

