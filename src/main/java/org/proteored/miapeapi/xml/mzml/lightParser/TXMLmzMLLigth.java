/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */

package org.proteored.miapeapi.xml.mzml.lightParser;

import java.util.Vector;

import org.proteored.miapeapi.exceptions.WrongXMLFormatException;
import org.w3c.dom.Node;

import uk.ac.ebi.jmzml.model.mzml.DataProcessingList;
import uk.ac.ebi.jmzml.model.mzml.FileDescription;
import uk.ac.ebi.jmzml.model.mzml.InstrumentConfigurationList;
import uk.ac.ebi.jmzml.model.mzml.MzML;
import uk.ac.ebi.jmzml.model.mzml.SoftwareList;

/**
 * 
 * @author Alberto
 * 
 *         Clase principal del parser. Almacenará todo lo relativo al mzML,
 *         pero sin los runs
 */
public class TXMLmzMLLigth extends TXMLNode {

	// vars
	private final String fileDescriptionTag = "fileDescription";
	private final String softwareListTag = "softwareList";
	private final String instrumentConfigurationListTag = "instrumentConfigurationList";
	private final String dataProcessingListTag = "dataProcessingList";
	private final String referenceableParamGroupListtag = "referenceableParamGroupList";
	private final String scanSettingsListtag = "scanSettingsList";
	private final String sampleListTag = "sampleList";
	private final String runTag = "run";

	// Ahora iremos insertando la complejidad del asunto.
	private TXMLfileDescription fileDescription;
	private TXMLsoftwareList softwareList;
	// private List<TXMLNode> softwareList;
	private TXMLinstrumentConfigurationList instrumentConfigurationList;
	private TXMLdataProcessingList dataProcessingList;
	private TXMLReferenceableParamGroupList referenceableParamGroupList;
	private TXMLscanSettingsList scanSettingsList;
	private TXMLsampleList sampleList;
	private TXMLNode run;
	// Vector de las cabeceras
	private Vector mzMLTags;

	public TXMLmzMLLigth(Vector inputtags) {
		super.setStartTag("mzML");
		// No tiene llave primaria...
		try {
			fileDescription = new TXMLfileDescription();

			softwareList = new TXMLsoftwareList();
			softwareList.createAttributeList();

			instrumentConfigurationList = new TXMLinstrumentConfigurationList();
			instrumentConfigurationList.createAttributeList();

			dataProcessingList = new TXMLdataProcessingList();
			dataProcessingList.createAttributeList();

			referenceableParamGroupList = new TXMLReferenceableParamGroupList();
			referenceableParamGroupList.createAttributeList();

			scanSettingsList = new TXMLscanSettingsList();
			scanSettingsList.createAttributeList();

			sampleList = new TXMLsampleList();
			sampleList.createAttributeList();

			run = new TXMLNode();

			mzMLTags = new Vector(inputtags.size());
			for (int i = 0; i < inputtags.size(); i++)
				mzMLTags.addElement(inputtags.elementAt(i));

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	// Getters
	// XMLNodes
	public TXMLReferenceableParamGroupList getreferenceableParamGroupList() {
		return referenceableParamGroupList;
	}

	public TXMLscanSettingsList getScanSettingsList() {
		return scanSettingsList;
	}

	public TXMLfileDescription getfileDescription() {
		return fileDescription;
	}

	public TXMLsampleList getSampleList() {
		return sampleList;
	}

	public TXMLsoftwareList getsoftwareList() {
		return softwareList;
	}

	public TXMLinstrumentConfigurationList getinstrumentList() {
		return instrumentConfigurationList;
	}

	public TXMLdataProcessingList getdataProcessingList() {
		return dataProcessingList;
	}

	// Print XML
	@Override
	public String getXMLEntry(int _index) {
		// REmodelar el método para que cargue directamente los nuevos métodos
		// de
		// cabecera y final-

		String _ret = getRootTag(super.getStartSection(), _index) + "\n";
		// Protocol. Cambiar. Luego vemos.
		_ret += this.getfileDescription().getXMLEntry(_index + 1);
		// referenceableParamGroupList
		_ret += getreferenceableParamGroupList().getXMLEntry(_index + 1);
		// sampleList
		_ret += getSampleList().getXMLEntry(_index + 1);
		// Software
		_ret += getsoftwareList().getXMLEntry(_index + 1);
		// scanSettingsList
		_ret += getScanSettingsList().getXMLEntry(_index + 1);
		// Instrumento
		_ret += getinstrumentList().getXMLEntry(_index + 1);
		// DataProcessing
		_ret += getdataProcessingList().getXMLEntry(_index + 1);
		_ret += getEndTag(super.getStartSection(), _index) + "\n";
		return _ret;
		// Faltaría la terminación de la lista.
	}

	// Parse Node
	public int ParseSAXNode(String _tag, Node _node) {
		// mis nuevas modificaciones.
		int _index = mzMLTags.indexOf(_tag);
		int _ret = _index;
		String _stringvalue;
		// Accession
		// Title
		if (_index == mzMLTags.indexOf(fileDescriptionTag)) {
			_ret = addFileDescriptionNode(_node);
			System.out.println("INFO: Adding fileDescription");
		}
		if (_index == mzMLTags.indexOf(softwareListTag)) {
			_ret = this.addSoftwareList(_node);
			System.out.println("INFO: Adding softwareList");
		}
		if (_index == mzMLTags.indexOf(scanSettingsListtag)) {
			_ret = this.addScanSettingsList(_node);
			System.out.println("INFO: Adding scanSettingsList");
		}
		if (_index == mzMLTags.indexOf(instrumentConfigurationListTag)) {
			_ret = addInstrumentConfigurationList(_node);
			System.out.println("INFO: Adding instrumentConfigurationList");
		}
		if (_index == mzMLTags.indexOf(dataProcessingListTag)) {
			_ret = addDataProcessingList(_node);
			System.out.println("INFO: Adding dataProcessingList");
		}
		if (_index == mzMLTags.indexOf(referenceableParamGroupListtag)) {
			_ret = addReferenceableParamGroupList(_node);
			System.out.println("INFO: Adding referenceableParamGroupList");
		}
		if (_index == mzMLTags.indexOf(sampleListTag)) {
			_ret = addSampleList(_node);
			System.out.println("INFO: Adding sampleList");
		}
		if (_index == mzMLTags.indexOf(runTag)) {
			// para el futuro, meter el add.
			System.out.println("INFO: Skipping run");
		}
		return _ret;
	}

	// insericón de nodo sencillo
	protected int addFileDescriptionNode(Node _node) {
		try {
			getfileDescription().parseNode(_node);
		} catch (Exception ex) {
			System.out.println("ERROR: Adding software element");
			// ex.printStackTrace();
		}
		return 1;
	}

	// inserción de nodos de lista
	protected int addSoftwareList(Node _node) {
		getsoftwareList().parseNode(_node);
		return getsoftwareList().getSoftwareElementsCount();
	}

	protected int addSampleList(Node _node) {
		getSampleList().parseNode(_node);
		return getSampleList().getSampleCount();
	}

	protected int addInstrumentConfigurationList(Node _node) {
		this.getinstrumentList().parseNode(_node);
		return getinstrumentList().getInstrumentElementsCount();
	}

	protected int addScanSettingsList(Node _node) {
		this.getScanSettingsList().parseNode(_node);
		return getScanSettingsList().getscanSettingsCount();
	}

	/*
	 * protected int addInstrumentConfigurationNode(Node _node) { try {
	 * //Tentemos que recorre el array TXMLinstrumentConfiguration Instance =
	 * new TXMLinstrumentConfiguration(); Instance.ActivateAttributes();
	 * Instance.CreateAttributeList(); Instance.ParseNode(_node);
	 * getinstrumentConfigurationList().add(Instance); Instance = null; }
	 * catch(Exception ex) {
	 * System.out.println("ERROR: Adding software element"); //
	 * ex.printStackTrace(); } return getinstrumentConfigurationCount(); }
	 */
	protected int addDataProcessingList(Node _node) {
		this.getdataProcessingList().parseNode(_node);
		return getdataProcessingList().getdataProcessingElementsCount();
	}

	protected int addReferenceableParamGroupList(Node _node) {
		this.getreferenceableParamGroupList().parseNode(_node);
		return getreferenceableParamGroupList().getreferenceableParamGroupCount();
	}

	/**
	 * Create a mzML object without Spectra and Chromatograms
	 * 
	 * @return the mzML object
	 */
	public MzML translate2jmzML() {
		MzML mzML = new MzML();

		mzML.setReferenceableParamGroupList(getreferenceableParamGroupList()
				.translate2ReferenceableParamGroup());
		FileDescription fileDescription = getfileDescription().translate2FileDescription();
		if (fileDescription == null || fileDescription.getFileContent() == null)
			throw new WrongXMLFormatException("Required fileDescription element is missing in mzML");
		mzML.setFileDescription(fileDescription);
		SoftwareList softwareList = getsoftwareList().translate2SoftwareList();
		if (softwareList == null || softwareList.getSoftware() == null
				|| softwareList.getSoftware().isEmpty())
			throw new WrongXMLFormatException("Required softwareList element is missing in mzML");
		mzML.setSoftwareList(softwareList);
		InstrumentConfigurationList instrumentConfigurationList = getinstrumentList()
				.translate2InstrumentConfigurationList();
		if (instrumentConfigurationList == null
				|| instrumentConfigurationList.getInstrumentConfiguration() == null
				|| instrumentConfigurationList.getInstrumentConfiguration().isEmpty())
			throw new WrongXMLFormatException(
					"Required instrumeConfigurationList element is missing in mzML");
		mzML.setInstrumentConfigurationList(instrumentConfigurationList);
		DataProcessingList dataProcessingList = getdataProcessingList()
				.translate2DataProcessingList();
		if (dataProcessingList == null || dataProcessingList.getDataProcessing() == null
				|| dataProcessingList.getDataProcessing().isEmpty())
			throw new WrongXMLFormatException(
					"Required dataProcessingList element is missing in mzML");
		mzML.setDataProcessingList(dataProcessingList);
		mzML.setSampleList(getSampleList().translate2SampleList());
		mzML.setScanSettingsList(getScanSettingsList().translate2ScanSettingsList());

		return mzML;
	}

	/*
	 * protected int addDataProcessingNode(Node _node) { try {
	 * TXMLdataProcessing Instance = new TXMLdataProcessing();
	 * Instance.ActivateAttributes(); Instance.CreateAttributeList();
	 * Instance.ParseNode(_node); this.getdataProcessingList().add(Instance);
	 * Instance = null; } catch(Exception ex) {
	 * System.out.println("ERROR: Adding software element"); //
	 * ex.printStackTrace(); } return this.getdataProcessingCount(); }
	 */
}
