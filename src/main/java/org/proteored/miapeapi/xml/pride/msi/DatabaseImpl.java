package org.proteored.miapeapi.xml.pride.msi;

import org.proteored.miapeapi.interfaces.msi.Database;

public class DatabaseImpl implements Database {
	private final String databaseName;
	private final String version;
	
	public DatabaseImpl(String databaseName, String version) {
		this.databaseName = databaseName;
		this.version = version;
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
	public String getName() {
		return databaseName;
	}

	@Override
	public String getNumVersion() {
		return version;
	}

	@Override
	public String getSequenceNumber() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUri() {
		// TODO Auto-generated method stub
		return null;
	}

}
