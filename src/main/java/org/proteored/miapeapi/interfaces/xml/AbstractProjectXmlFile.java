package org.proteored.miapeapi.interfaces.xml;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.proteored.miapeapi.exceptions.MiapeDatabaseException;
import org.proteored.miapeapi.exceptions.MiapeSecurityException;
import org.proteored.miapeapi.interfaces.Project;
import org.proteored.miapeapi.interfaces.persistence.ProjectFile;

public abstract class AbstractProjectXmlFile extends ProjectFile {

	public AbstractProjectXmlFile() {
		super();
	}

	public AbstractProjectXmlFile(File file) {
		super(file);
	}

	public AbstractProjectXmlFile(String name) {
		super(name);

	}

	public AbstractProjectXmlFile(byte[] bytes) throws IOException {
		super(bytes);
	}

	public abstract Project toProject() throws MiapeDatabaseException, MiapeSecurityException, JAXBException;

}
