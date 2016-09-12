/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */

package org.proteored.miapeapi.xml.mzml.lightParser.utils;

/**
 * 
 * @author Alberto Clase que se encarga de todo lo relativo al Outputter
 * 
 * 
 */
// quitar cuando tengamos las clases lectoras

import java.util.Vector;

import org.proteored.miapeapi.xml.mzml.lightParser.TXMLmzMLLigth;
import org.w3c.dom.CDATASection;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Comment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.devsphere.xml.saxdomix.SDXController;

public class TMzMLOutputter extends OutputterBase implements SDXController {
	/** The names of the elements that will be the roots of the DOM sub-trees */
	// Variables copiadas desde fuera
	protected Vector mzml_root_labels;
	// protected Vector experiments;
	protected TXMLmzMLLigth mzMLobject;
	protected int inputmode;
	public final int INPUT_URI = 1;
	public final int INPUT_STREAM = -1;
	protected final String XMLHead = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" standalone=\"yes\"?>";
	// Novedada. Prueba para crear el atributo del nodo ra�z
	protected String RootAttributeTag = "version";
	protected String RootVersionCompliance = "2.1";
	// Novedad. Para saber en todo momento que tipo de separaci�n tiene el
	// experimento. Suponemos todos los iguales en el experimento. Si no habr�
	// que implementar otra cosa.
	/**
	 * Creates a new outputter and takes as parameter a vector containing the
	 * element names for which <CODE>wantDOM()</CODE> will return
	 * <CODE>true</CODE>.
	 */
	// Ahora mis variables
	public final int SAX_VERSION = 2;

	public TMzMLOutputter() { // quitar elements
		super();
		this.inputmode = this.INPUT_URI; // si es fichero o uri
		this.InicializeSAXData();
	}

	public TMzMLOutputter(int _mode) { // quitar elements
		super();
		this.inputmode = this.INPUT_URI; // si es fichero o uri
		this.InicializeIDData();
	}

	/**
	 * Returns <CODE>true</CODE> when the application wants to receive a DOM
	 * sub-tree for handling, that is the <CODE>elements</CODE> vector contains
	 * the qualified name of the current element
	 */
	public boolean wantDOM(String namespaceURI, String localName, String qualifiedName, Attributes attributes)
			throws SAXException {
		return mzml_root_labels.contains(qualifiedName);
	}

	/**
	 * Outputs the constructed DOM sub-tree
	 */
	/*
	 * public void handleDOM(Element element) throws SAXException { blankLine();
	 * output("DOM Tree"); incLevel(); output(element); decLevel(); }
	 */
	public void handleDOM(Element element) throws SAXException {
		blankLine();
		output("DOM Tree. From root node");
		incLevel();
		ParseNode(element);
		decLevel();
	}

	/**
	 * Outputs a DOM node and is called recursively for all children
	 */
	public void output(Node node) {
		if (node instanceof Element) {
			String tagName = ((Element) node).getTagName();
			output("Element " + tagName);
			incLevel();
			Node child = node.getFirstChild();
			while (child != null) {
				output(child);
				child = child.getNextSibling();
			}
			decLevel();
		} else if (node instanceof CharacterData) {
			String data = ((CharacterData) node).getData();
			if (node instanceof CDATASection)
				output("CDATASection", data);
			else if (node instanceof Text)
				output("Text", data);
			else if (node instanceof Comment)
				output("Comment", data);
		} else if (node instanceof ProcessingInstruction) {
			String target = ((ProcessingInstruction) node).getTarget();
			output("ProcessingInstruction " + target);
		}
	}

	// El elemento real del parseo. Aquí ya entran los hijos... bien.
	public void ParseNode(Node node) {
		// Lo primero es saber si
		String _value;
		mzMLobject.ParseSAXNode(((Element) node).getTagName(), node);
	}

	// Copiados desde TPRIDEPArser
	protected void InicializeSAXData() {
		try {
			mzml_root_labels = new Vector();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		// Aqu�ivemos e l orden. //metemos los elementos que necesitamos en la
		// r�iz
		mzml_root_labels.addElement("fileDescription");
		mzml_root_labels.addElement("softwareList");
		mzml_root_labels.addElement("instrumentConfigurationList");
		mzml_root_labels.addElement("dataProcessingList");
		mzml_root_labels.addElement("referenceableParamGroupList");
		mzml_root_labels.addElement("scanSettingsList");
		mzml_root_labels.addElement("sampleList");
		try {
			mzMLobject = new TXMLmzMLLigth(mzml_root_labels);
			mzMLobject.createAttributeList();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	// Copiados desde TPRIDEPArser
	protected void InicializeIDData() {
		try {
			mzml_root_labels = new Vector();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		// Aqu�ivemos e l orden. //metemos los elementos que necesitamos en la
		// r�iz
		mzml_root_labels.addElement("fileDescription");
		mzml_root_labels.addElement("softwareList");
		mzml_root_labels.addElement("instrumentConfigurationList");
		mzml_root_labels.addElement("dataProcessingList");
		mzml_root_labels.addElement("referenceableParamGroupList");
		mzml_root_labels.addElement("scanSettingsList");
	}

	// Y ahora obtener el mzML
	/*
	 * public Vector GetExperimentVector() { return experiments; }
	 */
	/*
	 * public int GetExperimentCount() { return GetExperimentVector().size(); }
	 */
	public TXMLmzMLLigth getmzML() {
		return mzMLobject;
	}

}
