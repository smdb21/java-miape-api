/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */

package org.proteored.miapeapi.xml.mzml.lightParser;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import uk.ac.ebi.jmzml.model.mzml.DataProcessingList;

/**
 * 
 * @author Alberto
 */
public class TXMLdataProcessingList extends TXMLNode {

	private List<TXMLdataProcessing> dataProcessingElements;
	private final String dataProcessingElementTag = "dataProcessing";
	private final String dataProcessingListTag = "dataProcessingList";
	private final String dataProcessingListCountTag = "count";

	public TXMLdataProcessingList() {
		super.setStartTag(dataProcessingListTag);
		try {
			// Lo que hacemos es crear un vector de elementos adicionales, para
			// cada
			// uno de los pasos del Protocolo
			dataProcessingElements = new ArrayList<TXMLdataProcessing>();

		} catch (Exception exception) {
		}
	}

	// Matodos Get

	private List<TXMLdataProcessing> getdataProcessingElements() {
		return dataProcessingElements;
	}

	public TXMLdataProcessing getdataProcessingElement(int _pos) {
		return getdataProcessingElements().get(_pos);
	}

	public int getdataProcessingElementsCount() {
		return getdataProcessingElements().size();
	}

	// nuevo método para que devuelva todos los CV de cada step

	public void adddataProcessingElement(Node _mainNode) {
		try {
			TXMLdataProcessing Instance = new TXMLdataProcessing();
			Instance.setStartTag(dataProcessingElementTag);
			Instance.createAttributeList();
			Instance.parseNode(_mainNode);
			getdataProcessingElements().add(Instance);
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
			else if (containAttribute() && _stringlabel.compareTo(getAttributeTag()) == 0)
				super.getAttributeList().parseNode(_labelNode);
			else if (_stringlabel.compareTo(dataProcessingElementTag) == 0) {
				adddataProcessingElement(_labelNode);
			} else if (!super.isComplexNode(_labelNode))
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
						.valueOf(String.valueOf(dataProcessingListTag)))).append(" ")
						.append(dataProcessingListCountTag).append("=\"")
						.append(getdataProcessingElementsCount()).append("\""))), _index + 1)))
				.concat("\n"));
		for (int i = 0; i < getdataProcessingElementsCount(); i++)
			_ret = String.valueOf(_ret)
					+ String.valueOf(getdataProcessingElement(i).getXMLEntry(_index + 1));
		_ret = String.valueOf(_ret)
				+ String.valueOf(String.valueOf(
						String.valueOf(getEndTag(dataProcessingListTag, _index + 1))).concat("\n"));
		if (containAttribute())
			_ret = String.valueOf(_ret)
					+ String.valueOf(getAttributeList().getXMLEntry(_index + 1));
		// }

		return _ret;
	}

	public DataProcessingList translate2DataProcessingList() {
		DataProcessingList ret = new DataProcessingList();

		ret.setCount(this.getdataProcessingElementsCount());

		for (TXMLdataProcessing dataProcessing : getdataProcessingElements()) {
			ret.getDataProcessing().add(dataProcessing.translate2DataProcessing());
		}
		return ret;
	}
}
