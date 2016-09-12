// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov Date: 12/14/2007
// 12:17:46 PM
// Home Page : http://members.fortunecity.com/neshkov/dj.html - Check often for
// new version!
// Decompiler options: packimports(3)
// Source File Name: TParser.java

package org.proteored.miapeapi.xml.mzml.lightParser.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.log4j.Logger;
import org.proteored.miapeapi.xml.mzml.lightParser.TXMLNode;
import org.proteored.miapeapi.xml.mzml.lightParser.TXMLmzMLLigth;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.devsphere.xml.saxdomix.SDXBuilder;

public class TParser {
	private static Logger log = Logger.getLogger("log4j.logger.org.proteored");

	public TParser() {
		PARSING_MODE = SAX_VERSION;
		// Por ahora aín
		handler = new SAXOutputter();
		// Crontoller
		controller = new TMzMLOutputter() {
			@Override
			public void handleDOM(Element element) throws SAXException {
				setLevel(handler.getLevel());
				super.handleDOM(element);
			}
		};
		try {
			InicializeData();
			rootNode = new TXMLNode();
			rootNode.setStartTag("xml");
			rootNode.createRootAttribute(RootVersionCompliance, RootAttributeTag);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public TParser(int _mode) {
		PARSING_MODE = SAX_VERSION;
		// Por ahora aín
		handler = new SAXOutputter();
		// Crontoller
		controller = new TMzMLOutputter(_mode) {
			@Override
			public void handleDOM(Element element) throws SAXException {
				setLevel(handler.getLevel());
				super.handleDOM(element);
			}
		};
		try {
			InicializeData();
			rootNode = new TXMLNode();
			rootNode.setStartTag("xml");
			rootNode.createRootAttribute(RootVersionCompliance, RootAttributeTag);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public boolean isFinished() {
		return (READINGFINISH);
	}

	public void ForceFinished() {
		READINGFINISH = true;
		// Hacer limpieza de memoria
		rootNode = null;
	}

	protected void InicializeData() {
		try {
			mzml_root_labels = new Vector();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		// Aqua vemos e l orden. //metemos los elementos que necesitamos en la
		// raiz
		mzml_root_labels.addElement("fileDescription");
		mzml_root_labels.addElement("softwareList");
		mzml_root_labels.addElement("instrumentConfigurationList");
		mzml_root_labels.addElement("dataProcessingList");
		// try{
		// rootNode = new TXMLmzMLLigth(mzml_root_labels);
		// rootNode.SetStartTag("mzML");
		// rootNode.CreateRootAttribute(RootVersionCompliance,RootAttributeTag);
		// }
		// catch(Exception ex) {ex.printStackTrace();}

		READINGFINISH = false;
	}

	public String GetXMLHead() {
		return "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" standalone=\"yes\"?>";
	}

	public String GetRootHead() {
		return (rootNode.getRootTag(rootNode.getStartSection(), 0));
	}

	public String GetRootEnd() {
		return (rootNode.getEndTag(rootNode.getStartSection(), 0));
	}

	public TXMLmzMLLigth mzMLObject() {
		return this.controller.getmzML();
	}

	public void SetUri(String _newuri) {
		uri = _newuri;
	}

	public void CreateDOMParseDocument() throws SAXException, ParserConfigurationException,
			IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder = factory.newDocumentBuilder();
		if (inputmode == 1)
			document = builder.parse(uri);
		else if (inputmode == -1)
			document = builder.parse(PRIDEinputstream);
	}

	public int readPRIDEXML(int _mzdata, int _mzdata_description_mode)
	// throws SAXException, ParserConfigurationException, IOException
	{
		int _ret = -1;
		try {
			CreateDOMParseDocument();
			NodeList NodeList = document.getElementsByTagName("mzML");
			Node MainNode = NodeList.item(0);
			rootNode.addRootAttributes(MainNode);
			NodeList ElementList = MainNode.getChildNodes();
			int i = 0;
			for (; i < ElementList.getLength(); i++) {
				Node ExperimentNode = ElementList.item(i);
				Node ExperimentsubNode = ExperimentNode.getFirstChild();
				String _stringname = ExperimentNode.getNodeName();
				String _stringvalue = ExperimentsubNode.getNodeValue();
			}
			// Una vez leido. Se fija el mode.
			System.gc();
			READINGFINISH = true;
			return (_ret);
		} catch (Exception ex) {
			log.error("ERROR: DOM PARSER");
			ex.printStackTrace();
			return (-1);
		}
	}

	public String GetXMLEntry() {
		String _ret = String.valueOf(String.valueOf(GetXMLHead())).concat("\n");
		_ret = String.valueOf(_ret)
				+ String.valueOf(String.valueOf(
						String.valueOf(rootNode.getRootTag(rootNode.getStartSection(), 0))).concat(
						"\n"));
		_ret = String.valueOf(_ret) + String.valueOf(this.controller.getmzML().getXMLEntry(1));
		_ret = String.valueOf(_ret)
				+ String.valueOf(String.valueOf(
						String.valueOf(rootNode.getEndTag(rootNode.getStartSection(), 0))).concat(
						"\n"));
		return _ret;
	}

	public TXMLmzMLLigth getmzML() {
		return this.controller.getmzML();
	}

	public void parseDocument() {

		// get a factory
		saxBuilder = new SDXBuilder(handler, controller);
		SAXParserFactory spf = SAXParserFactory.newInstance();
		spf.setNamespaceAware(true);
		spf.setValidating(true);

		try {

			// get a new instance of parser
			sp = spf.newSAXParser();
			// parse the file and also register this class for call backs
			// myxmlReader =
			// XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
			myxmlReader = sp.getXMLReader();
			myxmlReader.setContentHandler(saxBuilder);
			// myxmlReader.setProperty("http://xml.org/sax/properties/lexical-handler",builder);
			myxmlReader.setErrorHandler(handler);
			// if(inputmode == 1)
			// sp.parse(uri,builder);
			myxmlReader.parse(uri);
			READINGFINISH = true;
			// else
			// if(inputmode == -1)
			// sp.parse(PRIDEinputstream,this);
			// myxmlReader.parse(PRIDEinputstream);

		} catch (SAXException se) {
			READINGFINISH = true;
			se.printStackTrace();
		} catch (ParserConfigurationException pce) {
			READINGFINISH = true;
			pce.printStackTrace();
		} catch (IOException ie) {
			READINGFINISH = true;
			ie.printStackTrace();
		}
		// catch (final java.lang.OutOfMemoryError ex)
		// {
		// READINGFINISH = true;
		// System.out.print("Out Memory Exception");
		// }

	}

	protected String uri;
	protected static InputStream PRIDEinputstream;
	protected Vector mzml_root_labels;
	// protected Vector experiments;
	protected int experiment_info_count;
	public final int PROCESSMZDATA = 1;
	public final int NONPROCESSMZDATA = -1;
	// protected int processmzdata;
	protected int inputmode;
	public final int INPUT_URI = 1;
	public final int INPUT_STREAM = -1;
	protected final String XMLHead = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" standalone=\"yes\"?>";
	protected TXMLNode rootNode;
	// Novedada. Prueba para crear el atributo del nodo raaz
	protected String RootAttributeTag = "version";
	protected String RootVersionCompliance = "2.1";
	// Metemos las variables que necesitamos para el parser de SAX
	protected Document document;
	protected SAXParser sp;
	protected ContentHandler saxBuilder;
	protected XMLReader myxmlReader;
	protected SAXOutputter handler;
	/** <CODE>DOMOutputter</CODE> used as controller */
	protected TMzMLOutputter controller;
	// Metemos el tipo
	public final int DOM_VERSION = 1;
	public final int SAX_VERSION = 2;
	protected int PARSING_MODE;

	// Nueva variable a ver si puedo hacer algo con hebras
	// aver si esto funciona
	private boolean READINGFINISH;
	// Para la description del MZDATA
	protected int SPECTRUM_DESCRIPTION_mode;
}