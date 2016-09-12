package org.proteored.miapeapi.interfaces.xml;

import org.proteored.miapeapi.interfaces.MiapeDocument;

/**
 * Interface that provides the toXml() method that returns an
 * {@link MiapeXmlFile}. This interface will be implemented by classes that
 * extends from {@link MiapeDocument}
 * 
 * @author Salva
 * 
 * @param <T>
 *            a class that extends from {@link MiapeDocument}
 */
public interface XmlEntity<T extends MiapeDocument> {
	public MiapeXmlFile<T> toXml();
}
