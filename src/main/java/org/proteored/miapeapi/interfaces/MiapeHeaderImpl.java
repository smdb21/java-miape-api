package org.proteored.miapeapi.interfaces;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.proteored.miapeapi.exceptions.MiapeDatabaseException;
import org.proteored.miapeapi.exceptions.MiapeSecurityException;
import org.proteored.miapeapi.interfaces.ge.MiapeGEDocument;
import org.proteored.miapeapi.interfaces.gi.MiapeGIDocument;
import org.proteored.miapeapi.interfaces.ms.MiapeMSDocument;
import org.proteored.miapeapi.interfaces.msi.MiapeMSIDocument;
import org.proteored.miapeapi.interfaces.persistence.MiapeFile;
import org.proteored.miapeapi.validation.ValidationReport;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class MiapeHeaderImpl implements MiapeDocument {
	private MiapeDate date = null;
	private int id = -1;
	private Date modificationDate = null;
	private String name = null;
	private String attachedFileLocation = null;
	private Boolean template = null;
	private String version = null;
	private MiapeFile miapeFile;
	private String projectName = null;
	private int miapeReference = -1;
	private int idProject = -1;

	public MiapeHeaderImpl(MiapeGEDocument miape) {
		date = miape.getDate();
		id = miape.getId();
		modificationDate = miape.getModificationDate();
		name = miape.getName();
		attachedFileLocation = miape.getAttachedFileLocation();
		template = miape.getTemplate();
		version = miape.getVersion();
		Project project = miape.getProject();
		if (project != null) {
			idProject = project.getId();
			projectName = project.getName();
		}
	}

	public MiapeHeaderImpl(MiapeGIDocument miape) {
		date = miape.getDate();
		id = miape.getId();
		modificationDate = miape.getModificationDate();
		name = miape.getName();
		attachedFileLocation = miape.getAttachedFileLocation();
		template = miape.getTemplate();
		version = miape.getVersion();
		Project project = miape.getProject();
		if (project != null) {
			idProject = project.getId();
			projectName = project.getName();
		}
		miapeReference = miape.getGEDocumentReference();
	}

	public MiapeHeaderImpl(MiapeMSDocument miape) {
		date = miape.getDate();
		id = miape.getId();
		modificationDate = miape.getModificationDate();
		name = miape.getName();
		attachedFileLocation = miape.getAttachedFileLocation();
		template = miape.getTemplate();
		version = miape.getVersion();
		Project project = miape.getProject();
		if (project != null) {
			idProject = project.getId();
			projectName = project.getName();
		}
	}

	public MiapeHeaderImpl(MiapeMSIDocument miape) {
		date = miape.getDate();
		id = miape.getId();
		modificationDate = miape.getModificationDate();
		name = miape.getName();
		attachedFileLocation = miape.getAttachedFileLocation();
		template = miape.getTemplate();
		version = miape.getVersion();
		Project project = miape.getProject();
		if (project != null) {
			idProject = project.getId();
			projectName = project.getName();
		}
		miapeReference = miape.getMSDocumentReference();

	}

	/**
	 *
	 * @param bytes
	 *            byte array as: <?xml version="1.0" encoding="UTF-8"
	 *            standalone="no"?> <MiapeHeader> <ID value="514"/>
	 *            <Name value="ProteoRed (DIGE) (CNB)"/>
	 *            <Template value="false"/> <Date value="2008-10-01"/>
	 *            <ModificationDate value="2009-09-28 12:56:27.71"/>
	 *            <Version value="1"/> <PRIDEURI value=""/>
	 *            <ProjectName value="proyecto x"/> <ProjectID value="123"/>
	 *            </MiapeHeader>
	 */
	public MiapeHeaderImpl(byte[] bytes) {

		try {
			miapeFile = new MiapeFile(bytes);
			File file = miapeFile.toFile();
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document document = builder.parse(file);
			file.delete();
			readDocument(document);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public MiapeHeaderImpl(File file, boolean deleteAfterReading) {

		readDocumentLineally(file);
		if (deleteAfterReading)
			file.delete();

	}

	// BEFORE THE PREVIOUS ONE: On July 11 2016
	// public MiapeHeaderImpl(File file, boolean deleteAfterReading) {
	//
	// try {
	//
	// DocumentBuilder builder =
	// DocumentBuilderFactory.newInstance().newDocumentBuilder();
	// Document document = builder.parse(file);
	// if (deleteAfterReading)
	// file.delete();
	// readDocument(document);
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (ParserConfigurationException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (SAXException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }

	public MiapeHeaderImpl(Document document) {
		readDocument(document);
	}

	public void deleteFile() {
		if (miapeFile != null)
			miapeFile.delete();
	}

	private void readDocument(Document document) {
		NodeList childNodes = document.getChildNodes();
		Node rootElement = childNodes.item(0);
		for (int i = 0; i < rootElement.getChildNodes().getLength(); i++) {
			Node childNode = rootElement.getChildNodes().item(i);
			if (childNode.getNodeType() == Element.ELEMENT_NODE) {
				if (childNode.getNodeName().equals(MiapeHeader.NAME)) {
					NamedNodeMap attr = childNode.getAttributes();
					for (int j = 0; j < attr.getLength(); j++) {
						if (attr.item(j).getNodeName().equals(MiapeHeader.VALUE)) {
							name = attr.item(j).getNodeValue();
						}
					}
				}
				if (childNode.getNodeName().equals(MiapeHeader.DATE)) {
					NamedNodeMap attr = childNode.getAttributes();
					for (int j = 0; j < attr.getLength(); j++) {
						if (attr.item(j).getNodeName().equals(MiapeHeader.VALUE)) {
							String nodeValue = attr.item(j).getNodeValue();
							if (nodeValue != null && !nodeValue.equals(""))
								date = new MiapeDate(nodeValue);
						}
					}
				}
				if (childNode.getNodeName().equals(MiapeHeader.ID)) {
					NamedNodeMap attr = childNode.getAttributes();
					for (int j = 0; j < attr.getLength(); j++) {
						if (attr.item(j).getNodeName().equals(MiapeHeader.VALUE)) {
							String nodeValue = attr.item(j).getNodeValue();
							if (nodeValue != null && !nodeValue.equals(""))
								id = Integer.valueOf(nodeValue);
						}
					}
				}
				if (childNode.getNodeName().equals(MiapeHeader.MODIFICATION_DATE)) {
					NamedNodeMap attr = childNode.getAttributes();
					for (int j = 0; j < attr.getLength(); j++) {
						if (attr.item(j).getNodeName().equals(MiapeHeader.VALUE)) {
							String nodeValue = attr.item(j).getNodeValue();
							if (nodeValue != null && !nodeValue.equals(""))
								modificationDate = new MiapeDate(nodeValue).toDate();
						}
					}
				}
				if (childNode.getNodeName().equals(MiapeHeader.ATTACHED_FILE_LOCATION)) {
					NamedNodeMap attr = childNode.getAttributes();
					for (int j = 0; j < attr.getLength(); j++) {
						if (attr.item(j).getNodeName().equals(MiapeHeader.VALUE)) {
							String nodeValue = attr.item(j).getNodeValue();
							if (nodeValue != null && !nodeValue.equals(""))
								attachedFileLocation = nodeValue;
						}
					}
				}
				if (childNode.getNodeName().equals(MiapeHeader.TEMPLATE)) {
					NamedNodeMap attr = childNode.getAttributes();
					for (int j = 0; j < attr.getLength(); j++) {
						if (attr.item(j).getNodeName().equals(MiapeHeader.VALUE)) {
							String nodeValue = attr.item(j).getNodeValue();
							if (nodeValue != null && !nodeValue.equals(""))
								template = Boolean.valueOf(nodeValue);
						}
					}
				}
				if (childNode.getNodeName().equals(MiapeHeader.VERSION)) {
					NamedNodeMap attr = childNode.getAttributes();
					for (int j = 0; j < attr.getLength(); j++) {
						if (attr.item(j).getNodeName().equals(MiapeHeader.VALUE)) {
							String nodeValue = attr.item(j).getNodeValue();
							if (nodeValue != null && !nodeValue.equals(""))
								version = nodeValue;
						}
					}
				}
				if (childNode.getNodeName().equals(MiapeHeader.PROJECTID)) {
					NamedNodeMap attr = childNode.getAttributes();
					for (int j = 0; j < attr.getLength(); j++) {
						if (attr.item(j).getNodeName().equals(MiapeHeader.VALUE)) {
							String nodeValue = attr.item(j).getNodeValue();
							if (nodeValue != null && !nodeValue.equals(""))
								idProject = Integer.valueOf(nodeValue);
						}
					}
				}
				if (childNode.getNodeName().equals(MiapeHeader.PROJECTNAME)) {
					NamedNodeMap attr = childNode.getAttributes();
					for (int j = 0; j < attr.getLength(); j++) {
						if (attr.item(j).getNodeName().equals(MiapeHeader.VALUE)) {
							String nodeValue = attr.item(j).getNodeValue();
							if (nodeValue != null && !nodeValue.equals(""))
								projectName = nodeValue;
						}
					}
				}
				if (childNode.getNodeName().equals(MiapeHeader.MIAPE_REFERENCE)
						|| childNode.getNodeName().equals(MiapeHeader.MIAPE_MS_REFERENCE)
						|| childNode.getNodeName().equals(MiapeHeader.MIAPE_GE_REFERENCE)) {
					NamedNodeMap attr = childNode.getAttributes();
					if (attr.getLength() > 0) {
						for (int j = 0; j < attr.getLength(); j++) {
							if (attr.item(j).getNodeName().equals(MiapeHeader.VALUE)) {
								String nodeValue = attr.item(j).getNodeValue();
								if (nodeValue != null && !nodeValue.equals("")) {
									try {
										miapeReference = Integer.valueOf(nodeValue);
									} catch (NumberFormatException e) {

									}
								}
							}
						}
					} else if (childNode.getChildNodes() != null) {
						for (int j = 0; j < childNode.getChildNodes().getLength(); j++) {
							Node childchildNode = childNode.getChildNodes().item(j);
							if (childchildNode.getNodeType() == Element.TEXT_NODE) {
								String nodeValue = childchildNode.getNodeValue();
								if (nodeValue != null && !nodeValue.equals("")) {
									try {
										miapeReference = Integer.valueOf(nodeValue);
									} catch (NumberFormatException e) {

									}
								}
							}
						}
					}
				}
			}
		}

	}

	private void readDocumentLineally(File file) {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
			final Pattern idPattern = Pattern.compile("\"[-]?(\\d+)\"");
			String line = br.readLine();
			boolean inProject = false;
			while (line != null) {
				if (line.contains("<MIAPEProject")) {
					inProject = true;
					final Matcher matcher = idPattern.matcher(line);
					if (matcher.find()) {
						final String group = matcher.group(1);
						boolean isNegative = false;
						if (line.contains("\"-")) {
							isNegative = true;
						}
						idProject = Integer.valueOf(group);
						if (isNegative) {
							idProject = -idProject;
						}
					}
				} else if (line.contains("</MIAPEProject>")) {
					inProject = false;
				}
				if (line.contains("<Name>")) {
					if (!inProject) {
						if (name == null) {
							name = line.substring(line.indexOf(">") + 1, line.indexOf("</"));
						}
					} else {
						projectName = line.substring(line.indexOf(">") + 1, line.indexOf("</"));
					}
				}
				if (line.contains("<Date>") && !inProject) {
					if (date == null) {
						date = new MiapeDate(line.substring(line.indexOf(">") + 1, line.indexOf("</")));
					}
				}
				if (line.contains("id=") && !inProject) {
					final Matcher matcher = idPattern.matcher(line);
					if (matcher.find()) {
						final String group = matcher.group(1);
						boolean isNegative = false;
						if (line.contains("\"-")) {
							isNegative = true;
						}
						id = Integer.valueOf(group);
						if (isNegative) {
							id = -id;
						}
					}
				}
				if (line.contains("<Modification_Date>") && !inProject) {
					if (modificationDate == null) {
						modificationDate = new MiapeDate(line.substring(line.indexOf(">") + 1, line.indexOf("</")))
								.toDate();
					}
				}
				if (line.contains("<AttachedFileLocation>") && !inProject) {
					attachedFileLocation = line.substring(line.indexOf(">") + 1, line.indexOf("</"));
				}
				if (line.contains("<Template>") && !inProject) {
					template = Boolean.valueOf(line.substring(line.indexOf(">") + 1, line.indexOf("</")));
				}
				if (line.contains("<Version>") && !inProject) {
					version = line.substring(line.indexOf(">") + 1, line.indexOf("</"));
				}
				if (line.contains(MiapeHeader.MIAPE_REFERENCE) || line.contains(MiapeHeader.MIAPE_MS_REFERENCE)
						|| line.contains(MiapeHeader.MIAPE_GE_REFERENCE)) {
					miapeReference = Integer.valueOf(line.substring(line.indexOf(">") + 1, line.indexOf("</")));
					break;
				}
				if (line.contains("<MSI_Identified_Protein_Set>")) {
					break;
				}
				line = br.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null) {
					br.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public Contact getContact() {
		return null;
	}

	@Override
	public MiapeDate getDate() {
		return date;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public Date getModificationDate() {
		return modificationDate;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public User getOwner() {
		return null;
	}

	@Override
	public String getAttachedFileLocation() {
		return attachedFileLocation;
	}

	@Override
	public Project getProject() {
		return new ProjectHeaderImpl(idProject, projectName);
	}

	@Override
	public Boolean getTemplate() {
		return template;
	}

	@Override
	public ValidationReport getValidationReport() {
		return null;
	}

	@Override
	public String getVersion() {
		return version;
	}

	@Override
	public void delete(String user, String password) throws MiapeDatabaseException, MiapeSecurityException {
	}

	@Override
	public int store() throws MiapeDatabaseException, MiapeSecurityException {
		return -1;
	}

	public int getMiapeReference() {
		return miapeReference;
	}

}
