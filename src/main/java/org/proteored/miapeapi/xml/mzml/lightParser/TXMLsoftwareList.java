/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */

package org.proteored.miapeapi.xml.mzml.lightParser;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import uk.ac.ebi.jmzml.model.mzml.SoftwareList;

/**
 * 
 * @author Alberto
 */
public class TXMLsoftwareList extends TXMLNode {

	private List<TXMLParamGroup> softwareElements;
	private final String SoftwareElementTag = "software";
	private final String SoftwareListTag = "softwareList";
	private final String SoftwareListCountTag = "count";

	public TXMLsoftwareList() {
		super.setStartTag(SoftwareListTag);
		try {
			// Lo que hacemos es crear un vector de elementos adicionales, para
			// cada
			// uno de los pasos del Protocolo
			softwareElements = new ArrayList<TXMLParamGroup>();
		} catch (Exception exception) {
		}
	}

	// Matodos Get

	private List<TXMLParamGroup> getSoftwareElements() {
		return softwareElements;
	}

	public TXMLParamGroup getSoftwareElement(int _pos) {
		return getSoftwareElements().get(_pos);
	}

	public int getSoftwareElementsCount() {
		return getSoftwareElements().size();
	}

	// nuevo método para que devuelva todos los CV de cada step

	public void addSoftwareElement(Node _mainNode) {
		try {
			TXMLParamGroup Instance = new TXMLParamGroup(SoftwareElementTag);
			Instance.createAttributeList();
			Instance.getAttributeList().setPrimaryTag("id");
			Instance.parseNode(_mainNode);
			getSoftwareElements().add(Instance);
		} catch (Exception exception) {
		}
	}

	// Habra que revisarlo porque hay elementos que no ncesitamos

	@Override
	public int parseNode(Node _mainNode) {
		NodeList _nodeList = _mainNode.getChildNodes();
		int i = 0;
		int _count_aux = 0;
		String _stringlabel, _stringvalue = "";
		for (; i < _nodeList.getLength(); i++) {
			Node _labelNode = _nodeList.item(i);
			Node _dataNode = _labelNode.getFirstChild();
			_stringlabel = _labelNode.getNodeName();
			if (_dataNode != null)
				_stringvalue = _dataNode.getNodeValue();
			if (_stringlabel.compareTo(super.getKeyTag()) == 0)
				super.setPrimaryKey(_stringvalue, _stringlabel);
			if (containAttribute() && _stringlabel.compareTo(getAttributeTag()) == 0)
				super.getAttributeList().parseNode(_labelNode);
			else if (_stringlabel.compareTo(SoftwareElementTag) == 0)
				addSoftwareElement(_labelNode);
			else if (!super.isComplexNode(_labelNode))
				addInformation(_stringvalue, _stringlabel);
			_count_aux++;
		}

		return _count_aux;
	}

	@Override
	public String getXMLEntry(int _index) {
		String _ret = "";
		_ret = String.valueOf(String.valueOf(
				String.valueOf(getStartTag(String.valueOf(String.valueOf((new StringBuffer(String
						.valueOf(String.valueOf(SoftwareListTag)))).append(" ")
						.append(SoftwareListCountTag).append("=\"")
						.append(getSoftwareElementsCount()).append("\""))), _index + 1))).concat(
				"\n"));
		for (int i = 0; i < getSoftwareElementsCount(); i++)
			_ret = String.valueOf(_ret)
					+ String.valueOf(getSoftwareElement(i).getXMLEntry(_index + 1));
		_ret = String.valueOf(_ret)
				+ String.valueOf(String.valueOf(
						String.valueOf(getEndTag(SoftwareListTag, _index + 1))).concat("\n"));

		return _ret;
	}

	public SoftwareList translate2SoftwareList() {
		SoftwareList ret = new SoftwareList();

		final List<TXMLParamGroup> softwareElements = getSoftwareElements();
		for (TXMLParamGroup element : softwareElements) {
			ret.getSoftware().add(element.translate2Software());
		}
		ret.setCount(this.getSoftwareElementsCount());
		return ret;
	}

}
