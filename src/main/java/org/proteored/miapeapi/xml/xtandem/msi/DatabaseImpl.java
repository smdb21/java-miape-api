package org.proteored.miapeapi.xml.xtandem.msi;

import org.proteored.miapeapi.interfaces.msi.Database;

public class DatabaseImpl implements Database {

	private final String description;
	private final String uri;

	public DatabaseImpl(String sequenceSource_1, String sequenceSourceDescription_1) {
		if (sequenceSourceDescription_1 != null && !"".equals(sequenceSourceDescription_1)
				&& !"no description".equals(sequenceSourceDescription_1))
			this.description = sequenceSourceDescription_1;
		else
			this.description = "Database";
		this.uri = sequenceSource_1;
	}

	@Override
	public String getName() {
		return description;
	}

	@Override
	public String getNumVersion() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSequenceNumber() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUri() {
		return uri;
	}

}
