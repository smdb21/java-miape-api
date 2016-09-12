/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */

package org.proteored.miapeapi.xml.mzml.lightParser;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import uk.ac.ebi.jmzml.model.mzml.ScanSettingsList;

/**
 * 
 * @author Alberto
 */
public class TXMLscanSettingsList extends TXMLNode {

	private List<TXMLscanSettings> scanSettingsList;
	private final String scanSettingsTag = "scanSettings";
	private final String scanSettingsListTag = "scanSettingsList";
	private final String count = "count";

	public TXMLscanSettingsList() {
		super.setStartTag(scanSettingsListTag);
		try {
			// Lo que hacemos es crear un vector de elementos adicionales, para
			// cada
			// uno de los pasos del Protocolo
			scanSettingsList = new ArrayList<TXMLscanSettings>();
		} catch (Exception exception) {
		}
	}

	// Matodos Get

	private List<TXMLscanSettings> getscanSettings() {
		return scanSettingsList;
	}

	public TXMLscanSettings getscanSettingsElement(int _pos) {
		return getscanSettings().get(_pos);
	}

	public int getscanSettingsCount() {
		return getscanSettings().size();
	}

	// nuevo método para que devuelva todos los CV de cada step

	public void addScanSettingsElement(Node _mainNode) {
		try {
			TXMLscanSettings Instance = new TXMLscanSettings();
			Instance.setStartTag(scanSettingsTag);
			Instance.createAttributeList();
			Instance.parseNode(_mainNode);
			getscanSettings().add(Instance);
		} catch (Exception exception) {
		}
	}

	// Habra que revisarlo porque hay elementos que no ncesitamos

	@Override
	public int parseNode(Node _mainNode) {
		NodeList _nodeList = _mainNode.getChildNodes();
		int i = 0;
		int _count_aux = 0;
		for (; i < _nodeList.getLength(); i++) {
			Node _labelNode = _nodeList.item(i);
			Node _dataNode = _labelNode.getFirstChild();
			String _stringlabel = _labelNode.getNodeName();
			String _stringvalue = _dataNode.getNodeValue();
			if (_stringlabel.compareTo(super.getKeyTag()) == 0)
				super.setPrimaryKey(_stringvalue, _stringlabel);
			else if (containAttribute() && _stringlabel.compareTo(getAttributeTag()) == 0)
				super.getAttributeList().parseNode(_labelNode);
			else if (_stringlabel.compareTo(scanSettingsTag) == 0) {
				addScanSettingsElement(_labelNode);
			} else if (!super.isComplexNode(_labelNode))
				addInformation(_stringvalue, _stringlabel);
			_count_aux++;
		}

		return _count_aux;
	}

	@Override
	public String getXMLEntry(int _index) {
		String _ret = "";
		// if ( getSoftwareCvTerms() > 0)
		// {
		_ret += getStartTag(scanSettingsListTag + " " + count + "=\"" + getscanSettingsCount()
				+ "\"", _index + 1)
				+ "\n";
		for (TXMLscanSettings scanSettings : getscanSettings()) {
			_ret += scanSettings.getXMLEntry(_index + 1);
		}

		_ret += getEndTag(scanSettingsListTag, _index + 1) + "\n";
		if (containAttribute())
			_ret += getAttributeList().getXMLEntry(_index + 1);
		// }

		return _ret;
	}

	public ScanSettingsList translate2ScanSettingsList() {
		ScanSettingsList ret = new ScanSettingsList();

		ret.setCount(this.getscanSettingsCount());
		for (TXMLscanSettings scanSettings : this.getscanSettings()) {
			ret.getScanSettings().add(scanSettings.translate2ScanSettings());
		}

		return ret;
	}

}
