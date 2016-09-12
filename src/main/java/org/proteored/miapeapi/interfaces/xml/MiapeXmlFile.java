package org.proteored.miapeapi.interfaces.xml;

import java.io.File;
import java.io.IOException;

import org.proteored.miapeapi.exceptions.MiapeDatabaseException;
import org.proteored.miapeapi.exceptions.MiapeSecurityException;
import org.proteored.miapeapi.interfaces.MiapeDocument;
import org.proteored.miapeapi.interfaces.persistence.MiapeFile;

/**
 * Represents a MIAPE document in a XML file. That XML files will be according
 * to the schemas here {@link "http://proteo.cnb.csic.es/miape-api/schemas/"}
 * Provides the method toDocument(),that returns a MiapeDocument object from the
 * MIAPE XML file.
 *
 * @author Salva
 *
 * @param <T>
 *            a class that extends from {@link MiapeDocument}
 */
public abstract class MiapeXmlFile<T extends MiapeDocument> extends MiapeFile {
	public MiapeXmlFile() {
		super();
	}

	public MiapeXmlFile(File file) {
		super(file);
	}

	public MiapeXmlFile(String fileName) {
		super(fileName);

	}

	public MiapeXmlFile(byte[] bytes) throws IOException {
		super(bytes);
	}

	public abstract T toDocument() throws MiapeDatabaseException, MiapeSecurityException;

}
