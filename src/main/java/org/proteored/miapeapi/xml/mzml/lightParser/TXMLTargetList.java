package org.proteored.miapeapi.xml.mzml.lightParser;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import uk.ac.ebi.jmzml.model.mzml.TargetList;

public class TXMLTargetList extends TXMLNode {
	private List<TXMLParamGroup> targets;
	private final String targetTag = "target";
	private final String targetListTag = "targetList";
	private final String countTag = "count";

	public TXMLTargetList() {
		super.setStartTag(targetListTag);
		try {
			targets = new ArrayList<TXMLParamGroup>();

		} catch (Exception exception) {
		}
	}

	private List<TXMLParamGroup> getTargetElements() {
		return targets;
	}

	public TXMLNode getTargetElement(int _pos) {
		return getTargetElements().get(_pos);
	}

	public int getTargetCount() {
		return getTargetElements().size();
	}

	public void addTargetElement(Node _mainNode) {
		try {
			TXMLParamGroup target = new TXMLParamGroup(targetTag);
			target.createAttributeList();
			target.parseNode(_mainNode);
			getTargetElements().add(target);
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
			else if (_stringlabel.compareTo(targetTag) == 0) {
				this.addTargetElement(_labelNode);
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
		_ret += getStartTag(targetListTag + " " + countTag + "=\"" + getTargetCount() + "\"",
				_index + 1) + "\n";
		for (int i = 0; i < getTargetCount(); i++)
			_ret += getTargetElement(i).getXMLEntry(_index + 1);
		if (containAttribute())
			_ret += getAttributeList().getXMLEntry(_index + 1);
		_ret += getEndTag(targetListTag, _index + 1) + "\n";

		// }

		return _ret;
	}

	public TargetList translate2TargetList() {
		TargetList ret = new TargetList();

		ret.setCount(this.getTargetCount());
		for (TXMLParamGroup paramGroup : this.getTargetElements()) {
			ret.getTarget().add(paramGroup.translate2ParamGroup());
		}

		return ret;
	}
}
