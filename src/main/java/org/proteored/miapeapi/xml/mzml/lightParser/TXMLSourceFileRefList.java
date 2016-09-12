package org.proteored.miapeapi.xml.mzml.lightParser;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import uk.ac.ebi.jmzml.model.mzml.SourceFileRefList;

public class TXMLSourceFileRefList extends TXMLNode {
	private List<TXMLNode> sourceFileRefElements;
	private final String sourceFileRefTag = "sourceFileRef";
	private final String sourceFileRefListTag = "sourceFileRefList";
	private final String countTag = "count";

	public TXMLSourceFileRefList() {
		super.setStartTag(sourceFileRefListTag);
		try {
			sourceFileRefElements = new ArrayList<TXMLNode>();

		} catch (Exception exception) {
		}
	}

	private List<TXMLNode> getsourceFileRefElements() {
		return sourceFileRefElements;
	}

	public TXMLNode getsourceFileRefElement(int _pos) {
		return getsourceFileRefElements().get(_pos);
	}

	public int getsourceFileRefCount() {
		return getsourceFileRefElements().size();
	}

	public void addsourceFileRefElement(Node _mainNode) {
		try {
			TXMLNode sourceFileRef = new TXMLNode();
			sourceFileRef.setStartTag(sourceFileRefTag);
			sourceFileRef.createAttributeList();
			sourceFileRef.parseNode(_mainNode);
			getsourceFileRefElements().add(sourceFileRef);
		} catch (Exception exception) {
		}
	}

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
			else if (_stringlabel.compareTo(sourceFileRefTag) == 0) {
				this.addsourceFileRefElement(_labelNode);
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
		_ret += getStartTag(sourceFileRefListTag + " " + countTag + "=\"" + getsourceFileRefCount()
				+ "\"", _index + 1)
				+ "\n";
		for (int i = 0; i < getsourceFileRefCount(); i++)
			_ret += getsourceFileRefElement(i).getXMLEntry(_index + 1);
		if (containAttribute())
			_ret += getAttributeList().getXMLEntry(_index + 1);
		_ret += getEndTag(sourceFileRefListTag, _index + 1) + "\n";

		// }

		return _ret;
	}

	public SourceFileRefList translate2SourceFileRefList() {
		SourceFileRefList ret = new SourceFileRefList();

		ret.setCount(this.getsourceFileRefCount());

		for (TXMLNode node : this.getsourceFileRefElements()) {
			ret.getSourceFileRef().add(node.translate2SourceFileRef());
		}

		return ret;
	}

}
