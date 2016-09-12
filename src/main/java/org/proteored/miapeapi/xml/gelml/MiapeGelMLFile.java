package org.proteored.miapeapi.xml.gelml;
import java.io.File;
import java.io.IOException;

import org.proteored.miapeapi.exceptions.MiapeDatabaseException;
import org.proteored.miapeapi.exceptions.MiapeSecurityException;
import org.proteored.miapeapi.interfaces.ge.MiapeGEDocument;
import org.proteored.miapeapi.interfaces.xml.MiapeXmlFile;


public class MiapeGelMLFile extends MiapeXmlFile<MiapeGEDocument> {
	public MiapeGelMLFile(byte[] bytes) throws IOException {
		super(bytes);
	}
	public MiapeGelMLFile(File file) {
		super(file);
	}

	public MiapeGelMLFile(String name) {
		super(name);
	}
	@Override
	public MiapeGEDocument toDocument() throws MiapeDatabaseException,
	MiapeSecurityException {
		// TODO
		throw new UnsupportedOperationException("Not implemented yet");
	}
}
