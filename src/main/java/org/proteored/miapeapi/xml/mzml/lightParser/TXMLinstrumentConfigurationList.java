/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */

package org.proteored.miapeapi.xml.mzml.lightParser;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import uk.ac.ebi.jmzml.model.mzml.InstrumentConfigurationList;

/**
 * 
 * @author Alberto
 */
public class TXMLinstrumentConfigurationList extends TXMLNode {

	private List<TXMLinstrumentConfiguration> instrumentConfigurationElements;
	private final String instrumentConfigurationTag = "instrumentConfiguration";
	private final String instrumentConfigurationListTag = "instrumentConfigurationList";
	private final String InstrumentListCountTag = "count";

	public TXMLinstrumentConfigurationList() {
		super.setStartTag(instrumentConfigurationListTag);
		try {
			// Lo que hacemos es crear un vector de elementos adicionales, para
			// cada
			// uno de los pasos del Protocolo
			instrumentConfigurationElements = new ArrayList<TXMLinstrumentConfiguration>();
		} catch (Exception exception) {
		}
	}

	// Matodos Get

	private List<TXMLinstrumentConfiguration> getInstrumentElements() {
		return instrumentConfigurationElements;
	}

	public TXMLinstrumentConfiguration getInstrumentElement(int _pos) {
		return getInstrumentElements().get(_pos);
	}

	public int getInstrumentElementsCount() {
		return getInstrumentElements().size();
	}

	// nuevo método para que devuelva todos los CV de cada step

	public void addInstrumentConfigurationElement(Node _mainNode) {
		try {
			TXMLinstrumentConfiguration instrumentConfiguration = new TXMLinstrumentConfiguration();
			instrumentConfiguration.setStartTag(instrumentConfigurationTag);
			instrumentConfiguration.createAttributeList();
			instrumentConfiguration.parseNode(_mainNode);
			getInstrumentElements().add(instrumentConfiguration);
		} catch (Exception exception) {
		}
	}

	// Habra que revisarlo porque hay elementos que no ncesitamos

	@Override
	public int parseNode(Node _mainNode) {
		NodeList _nodeList = _mainNode.getChildNodes();
		int i = 0;
		String _stringlabel, _stringvalue = "";
		int _count_aux = 0;
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
			else if (_stringlabel.compareTo(instrumentConfigurationTag) == 0) {
				addInstrumentConfigurationElement(_labelNode);
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
		_ret += getStartTag(instrumentConfigurationListTag + " " + InstrumentListCountTag + "=\""
				+ getInstrumentElementsCount() + "\"", _index + 1)
				+ "\n";
		for (int i = 0; i < getInstrumentElementsCount(); i++)
			_ret += getInstrumentElement(i).getXMLEntry(_index + 1);
		if (containAttribute())
			_ret += getAttributeList().getXMLEntry(_index + 1);
		_ret += getEndTag(instrumentConfigurationListTag, _index + 1) + "\n";

		// }

		return _ret;
	}

	public InstrumentConfigurationList translate2InstrumentConfigurationList() {
		InstrumentConfigurationList ret = new InstrumentConfigurationList();
		ret.setCount(this.getInstrumentElementsCount());
		for (TXMLinstrumentConfiguration instrumentConfig : getInstrumentElements()) {
			ret.getInstrumentConfiguration().add(
					instrumentConfig.translate2InstrumentConfiguration());
		}
		return ret;

	}

}
