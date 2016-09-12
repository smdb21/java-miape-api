package org.proteored.miapeapi.interfaces;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.proteored.miapeapi.interfaces.ge.MiapeGEDocument;
import org.proteored.miapeapi.interfaces.gi.MiapeGIDocument;
import org.proteored.miapeapi.interfaces.ms.MiapeMSDocument;
import org.proteored.miapeapi.interfaces.msi.MiapeMSIDocument;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class MiapeHeader {
	public MiapeDocument getMiapeDocument() {
		return miapeDocument;
	}

	public static final String ID = "ID";
	public static final String NAME = "Name";
	public static final String ATTACHED_FILE_LOCATION = "AttachedFileLocation";
	public static final String VERSION = "Version";
	public static final String MODIFICATION_DATE = "Modification_Date";
	public static final String DATE = "Date";
	public static final String TEMPLATE = "Template";
	public static final String VALUE = "value";
	public static final String PROJECTNAME = "ProjectName";
	public static final String PROJECTID = "ProjectID";
	public static final String MIAPE_MS_REFERENCE = "MIAPE_MS_Ref";
	public static final String MIAPE_GE_REFERENCE = "MIAPE_GE_Ref";
	public static final String MIAPE_REFERENCE = "MIAPE_Ref";
	private File file = null;
	private final MiapeDocument miapeDocument;

	public File getFile() {
		return file;
	}

	public MiapeHeader(MiapeGEDocument document) {
		miapeDocument = new MiapeHeaderImpl(document);
		file = getMiapeHeaderFile(miapeDocument);
	}

	public MiapeHeader(MiapeGIDocument document) {
		miapeDocument = new MiapeHeaderImpl(document);
		file = getMiapeHeaderFile(miapeDocument);
	}

	public MiapeHeader(MiapeMSDocument document) {
		miapeDocument = new MiapeHeaderImpl(document);
		file = getMiapeHeaderFile(miapeDocument);
	}

	public MiapeHeader(MiapeMSIDocument document) {
		miapeDocument = new MiapeHeaderImpl(document);
		file = getMiapeHeaderFile(miapeDocument);
	}

	public MiapeHeader(Document document) {
		miapeDocument = new MiapeHeaderImpl(document);
		file = getMiapeHeaderFile(miapeDocument);
	}

	public MiapeHeader(byte[] bytes) {
		miapeDocument = new MiapeHeaderImpl(bytes);
		file = getMiapeHeaderFile(miapeDocument);
	}

	public MiapeHeader(File file, boolean deleteAfterReading) {
		miapeDocument = new MiapeHeaderImpl(file, deleteAfterReading);
		this.file = file;
	}

	private File getMiapeHeaderFile(MiapeDocument miape) {
		try {

			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document document = builder.newDocument();
			Element rootElement = document.createElement("MiapeHeader");
			document.appendChild(rootElement);

			if (miape.getId() > 0) {
				Element element = document.createElement(ID);
				element.setAttribute(VALUE, String.valueOf(miape.getId()));
				rootElement.appendChild(element);
			}
			if (miape.getName() != null) {
				Element element = document.createElement(NAME);
				element.setAttribute(VALUE, miape.getName());
				rootElement.appendChild(element);
			}
			if (miape.getTemplate() != null) {
				Element element = document.createElement(TEMPLATE);
				element.setAttribute(VALUE, String.valueOf(miape.getTemplate()));
				rootElement.appendChild(element);
			}
			if (miape.getDate() != null) {
				Element element = document.createElement(DATE);
				element.setAttribute(VALUE, miape.getDate().getValue());
				rootElement.appendChild(element);
			}
			if (miape.getModificationDate() != null) {
				Element element = document.createElement(MODIFICATION_DATE);
				element.setAttribute(VALUE, miape.getModificationDate().toString());
				rootElement.appendChild(element);
			}
			if (miape.getVersion() != null) {
				Element element = document.createElement(VERSION);
				element.setAttribute(VALUE, miape.getVersion());
				rootElement.appendChild(element);
			}
			if (miape.getAttachedFileLocation() != null) {
				Element element = document.createElement(ATTACHED_FILE_LOCATION);
				element.setAttribute(VALUE, miape.getAttachedFileLocation());
				rootElement.appendChild(element);
			}
			if (miape.getProject() != null) {
				if (miape.getProject().getName() != null) {
					Element element = document.createElement(PROJECTNAME);
					element.setAttribute(VALUE, miape.getProject().getName());
					rootElement.appendChild(element);
				}
				if (miape.getProject().getId() > 0) {
					Element element = document.createElement(PROJECTID);
					element.setAttribute(VALUE, String.valueOf(miape.getProject().getId()));
					rootElement.appendChild(element);
				}
			}
			// In case of MIAPE GI and MSI, add the MIAPE REFERENCE NODE
			if (miape instanceof MiapeHeaderImpl) {
				int miapeReference = ((MiapeHeaderImpl) miape).getMiapeReference();
				Element element = document.createElement(MIAPE_REFERENCE);
				element.setAttribute(VALUE, String.valueOf(miapeReference));
				rootElement.appendChild(element);
			}
			// transform the Document into a String
			DOMSource domSource = new DOMSource(document);
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			java.io.StringWriter sw = new java.io.StringWriter();
			StreamResult sr = new StreamResult(sw);
			transformer.transform(domSource, sr);
			String xml = sw.toString();
			sw.close();
			File file = File.createTempFile(this.getClass().getName(), "temp");
			FileWriter fos = new FileWriter(file);
			fos.write(xml);
			fos.close();
			file.deleteOnExit();
			return file;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public byte[] toBytes() {
		if (file != null) {
			try {
				return getBytesFromFile(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	private static byte[] getBytesFromFile(File file) throws IOException {
		InputStream is = new FileInputStream(file);

		// Get the size of the file
		long length = file.length();

		if (length > Integer.MAX_VALUE) {
			// File is too large
		}

		// Create the byte array to hold the data
		byte[] bytes = new byte[(int) length];

		// Read in the bytes
		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
			offset += numRead;
		}

		// Ensure all the bytes have been read in
		if (offset < bytes.length) {
			is.close();
			throw new IOException("Could not completely read file " + file.getName());
		}

		// Close the input stream and return bytes
		is.close();
		return bytes;
	}

	public int getMiapeRef() {
		if (miapeDocument instanceof MiapeHeaderImpl) {
			return ((MiapeHeaderImpl) miapeDocument).getMiapeReference();
		}
		return -1;
	}

}
