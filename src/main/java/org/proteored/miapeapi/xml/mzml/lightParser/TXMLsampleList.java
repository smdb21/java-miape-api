package org.proteored.miapeapi.xml.mzml.lightParser;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import uk.ac.ebi.jmzml.model.mzml.SampleList;

public class TXMLsampleList extends TXMLNode {
	private List<TXMLParamGroup> sampleElements;
	private final String sampleTag = "sample";
	private final String sampleListTag = "sampleList";
	private final String count = "count";

	public TXMLsampleList() {
		super.setStartTag(sampleListTag);
		try {
			// Lo que hacemos es crear un vector de elementos adicionales, para
			// cada
			// uno de los pasos del Protocolo
			sampleElements = new ArrayList<TXMLParamGroup>();
		} catch (Exception exception) {
		}
	}

	// Matodos Get

	private List<TXMLParamGroup> getSampleElements() {
		return sampleElements;
	}

	public TXMLParamGroup getsampleElement(int _pos) {
		return getSampleElements().get(_pos);
	}

	public int getSampleCount() {
		return getSampleElements().size();
	}

	// nuevo método para que devuelva todos los CV de cada step

	public void addSampleElement(Node _mainNode) {
		try {
			TXMLParamGroup Instance = new TXMLParamGroup(sampleTag);
			Instance.createAttributeList();
			Instance.parseNode(_mainNode);
			getSampleElements().add(Instance);
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
			String _stringvalue = "";
			String _stringlabel = _labelNode.getNodeName();
			if (_dataNode != null)
				_stringvalue = _dataNode.getNodeValue();
			if (_stringlabel.compareTo(super.getKeyTag()) == 0)
				super.setPrimaryKey(_stringvalue, _stringlabel);
			else if (containAttribute() && _stringlabel.compareTo(getAttributeTag()) == 0)
				super.getAttributeList().parseNode(_labelNode);
			else if (_stringlabel.compareTo(sampleTag) == 0) {
				addSampleElement(_labelNode);
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
		_ret += getStartTag(sampleListTag + " " + count + "=\"" + getSampleCount() + "\"",
				_index + 1) + "\n";
		for (TXMLParamGroup sample : getSampleElements()) {
			_ret += sample.getXMLEntry(_index + 1);
		}

		_ret += getEndTag(sampleListTag, _index + 1) + "\n";
		if (containAttribute())
			_ret += getAttributeList().getXMLEntry(_index + 1);
		// }

		return _ret;
	}

	public SampleList translate2SampleList() {
		SampleList ret = new SampleList();

		ret.setCount(this.getSampleCount());

		for (TXMLParamGroup paramGroup : getSampleElements()) {
			ret.getSample().add(paramGroup.translate2Sample());
		}

		return ret;
	}
}
