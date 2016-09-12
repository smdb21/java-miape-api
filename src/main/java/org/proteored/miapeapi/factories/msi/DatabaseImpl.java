package org.proteored.miapeapi.factories.msi;

import org.proteored.miapeapi.interfaces.msi.Database;

public class DatabaseImpl implements Database {
	private final String name;
	private final String numVersion;
	private final String date;
	private final String description;
	private final String Sequence_Number;
	private final String URI;

	public DatabaseImpl (DatabaseBuilder databaseBuilder){
		this.name = databaseBuilder.name;
		this.date = databaseBuilder.date;
		this.description = databaseBuilder.description;
		this.numVersion = databaseBuilder.numVersion;
		this.Sequence_Number = databaseBuilder.Sequence_Number;
		this.URI = databaseBuilder.URI;
	}
	@Override
	public String getDate() {
		return this.date;
	}

	@Override
	public String getDescription() {
		return this.description;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getNumVersion() {
		return this.numVersion;
	}

	@Override
	public String getSequenceNumber() {
		return this.Sequence_Number;
	}

	@Override
	public String getUri() {
		return this.URI;
	}

}
