package org.proteored.miapeapi.xml.mzml.lightParser;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import uk.ac.ebi.jmzml.model.mzml.ReferenceableParamGroupList;

public class TXMLReferenceableParamGroupList extends TXMLNode {
	private TXMLNode fileContent;
	// private TXMLParamGroup ProcessingMethod;
	// Lo primero creamos las cadenas que vamos a necesitar
	public static final String referenceableParamGroupTag = "referenceableParamGroup";

	private List<TXMLParamGroup> referenceableParamGroupList;
	private final String referenceableParamGroupListTag = "referenceableParamGroupList";
	private final String countTag = "count";

	public TXMLReferenceableParamGroupList() {
		super.setStartTag(referenceableParamGroupListTag);
		try {
			referenceableParamGroupList = new ArrayList<TXMLParamGroup>();

		} catch (Exception exception) {
		}
	}

	private List<TXMLParamGroup> getreferenceableParamGroupList() {
		return referenceableParamGroupList;
	}

	public void addreferenceableParamGroupElement(Node _mainNode) {
		try {
			TXMLParamGroup referenceableParamGroup = new TXMLParamGroup(referenceableParamGroupTag);
			referenceableParamGroup.createAttributeList();
			referenceableParamGroup.parseNode(_mainNode);
			getreferenceableParamGroupList().add(referenceableParamGroup);
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
			else if (_stringlabel.compareTo(referenceableParamGroupTag) == 0) {
				addreferenceableParamGroupElement(_labelNode);
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
		_ret = getStartTag(referenceableParamGroupListTag + " " + countTag + "=\"" + getreferenceableParamGroupCount()
				+ "\"", _index + 1)
				+ "\n";
		for (TXMLParamGroup paramGroup : getreferenceableParamGroupList()) {
			_ret += paramGroup.getXMLEntry(_index + 1);
		}
		if (containAttribute())
			_ret += getAttributeList().getXMLEntry(_index + 1);
		_ret += getEndTag(referenceableParamGroupListTag, _index + 1) + "\n";

		// }

		return _ret;
	}

	public int getreferenceableParamGroupCount() {
		return this.getreferenceableParamGroupList().size();
	}

	public ReferenceableParamGroupList translate2ReferenceableParamGroup() {
		ReferenceableParamGroupList ret = new ReferenceableParamGroupList();

		for (TXMLParamGroup refParamGroup : this.getreferenceableParamGroupList()) {
			ret.getReferenceableParamGroup().add(refParamGroup.translate2ReferenceableParamGroup());
		}

		return ret;
	}

}
