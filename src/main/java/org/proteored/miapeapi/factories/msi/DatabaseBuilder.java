package org.proteored.miapeapi.factories.msi;

import org.proteored.miapeapi.cv.msi.DatabaseName;
import org.proteored.miapeapi.interfaces.msi.Database;

public class DatabaseBuilder {
	String name;
	String numVersion;
	String date;
	String description;
	String Sequence_Number;
	String URI;

	/**
	 * Sets a string value to the name of the database. It should be a possible
	 * value of the {@link DatabaseName}
	 * 
	 * @param databaseName
	 */
	DatabaseBuilder(String databaseName) {
		this.name = databaseName;
	}

	public DatabaseBuilder numVersion(String value) {
		this.numVersion = value;
		return this;
	}

	public DatabaseBuilder date(String value) {
		this.date = value;
		return this;
	}

	public DatabaseBuilder description(String value) {
		this.description = value;
		return this;
	}

	public DatabaseBuilder Sequence_Number(String value) {
		this.Sequence_Number = value;
		return this;
	}

	public DatabaseBuilder URI(String value) {
		this.URI = value;
		return this;
	}

	public Database build() {
		return new DatabaseImpl(this);
	}
}