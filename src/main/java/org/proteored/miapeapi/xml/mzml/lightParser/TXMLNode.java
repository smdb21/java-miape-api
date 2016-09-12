// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov Date: 12/14/2007
// 12:25:34 PM
// Home Page : http://members.fortunecity.com/neshkov/dj.html - Check often for
// new version!
// Decompiler options: packimports(3)
// Source File Name: TXMLNode.java

package org.proteored.miapeapi.xml.mzml.lightParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import uk.ac.ebi.jmzml.model.mzml.SourceFileRef;

// Referenced classes of package prideparser:
// TXMLAttributeList, TXMLElement

public class TXMLNode {
	private String start_tag;
	private String primary_key_tag;
	private int primary_key;
	private boolean primary_key_requiered;
	private List element_list;
	private TXMLAttributeList attributeList;
	private boolean attributelistcreated;
	private boolean nodeWithAttributes;

	public TXMLNode() {
		start_tag = "Start";
		primary_key_tag = "";
		primary_key = -1;
		attributelistcreated = false;
		primary_key_requiered = false;
		nodeWithAttributes = false;
		try {
			element_list = new ArrayList();
		} catch (Exception ex) {
			// ex.printStackTrace();
			System.out.println("ERROR: XML Node -Element list-  was not created.");
		}
	}

	public String getStartSection() {
		return start_tag;
	}

	public String getKeyTag() {
		return primary_key_tag;
	}

	private int getKeyPos() {
		return primary_key;
	}

	public String getKeyValue() {
		return getElement(getKeyPos()).GetValue();
	}

	public boolean isRequiredKey() {
		return primary_key_requiered;
	}

	public boolean containAttribute() {
		return attributelistcreated;
	}

	public String getAttributeTag() {
		return getAttributeList().getStartSection();
	}

	public TXMLAttributeList getAttributeList() {
		return attributeList;
	}

	public List getElementList() {
		return element_list;
	}

	public int getElementCount() {
		return getElementList().size();
	}

	public TXMLElement getElement(int _pos) {
		return (TXMLElement) getElementList().get(_pos);
	}

	public TXMLElement getElementFromTag(String _tag) {
		TXMLElement _ret = null;
		int _index = getIndexFromTag(_tag);
		if (_index != -1)
			_ret = getElement(_index);
		return _ret;
	}

	public String getElementValueFromTag(String _tag) {
		String _ret = "";
		int _index = getIndexFromTag(_tag);
		if (_index != -1)
			_ret = getElement(_index).GetValue();
		return _ret;
	}

	public int getIndexFromTag(String _tag) {
		int i = 0;
		int _index = -1;
		for (; i < getElementCount(); i++)
			if (getElement(i).GetTag().compareTo(_tag) == 0)
				_index = i;

		return _index;
	}

	public TXMLElement getElementFromTagNOCASE(String _tag) {
		TXMLElement _ret = null;
		int _index = getIndexFromTagNOCASE(_tag);
		if (_index != -1)
			_ret = getElement(_index);
		return _ret;
	}

	public String getElementValueFromTagNOCASE(String _tag) {
		String _ret = "";
		int _index = getIndexFromTagNOCASE(_tag);
		if (_index != -1)
			_ret = getElement(_index).GetValue();
		return _ret;
	}

	public int getIndexFromTagNOCASE(String _tag) {
		int i = 0;
		int _index = -1;
		for (; i < getElementCount(); i++)
			if (getElement(i).GetTag().compareToIgnoreCase(_tag) == 0)
				_index = i;

		return _index;
	}

	public int searchElementbyTag(String _arg) {
		int ret = -1;
		int i = 0;
		for (boolean _continue = true; i < getElementCount() && _continue; i++)
			if (getElement(i).GetTag().compareTo(_arg) == 0) {
				ret = i;
				_continue = false;
			}

		return ret;
	}

	// Nuevos métodos
	// Extraer todos los tags y values de los elements
	public List getElementTags() {
		List _ret = new ArrayList();
		int i;
		for (i = 0; i < this.getElementCount(); i++)
			_ret.add((this.getElement(i)).GetTag());
		return (_ret);
	}

	public List getElementValues() {
		List _ret = new ArrayList();
		int i;
		for (i = 0; i < this.getElementCount(); i++)
			_ret.add((this.getElement(i)).GetValue());
		return (_ret);
	}

	public boolean isNodeWithAttributes() {
		return nodeWithAttributes;
	}

	public void activateAttributes() {
		nodeWithAttributes = true;
	}

	public void setStartTag(String _tag) {
		start_tag = _tag;
	}

	public void setPrimaryTag(String _label) {
		primary_key_tag = _label;
	}

	public void setPrimaryKey(String _term, String _label, Node _attributes) {
		int _aux;
		if ((_aux = searchElementbyTag(_label)) != -1) {
			getElement(_aux).SetValue(_term);
			if (_attributes.hasAttributes())
				getElement(_aux).SetAttributes(_attributes);
		} else {
			if (_attributes.hasAttributes())
				_aux = addInformation(_term, _label, _attributes);
			else
				_aux = addInformation(_term, _label);
			primary_key_tag = _label;
			primary_key = _aux - 1;
			setRequiredKey(true);
		}
	}

	public void setPrimaryKey(String _term, String _label) {
		int _aux;
		if ((_aux = searchElementbyTag(_label)) != -1) {
			getElement(_aux).SetValue(_term);
		} else {
			_aux = addInformation(_term, _label);
			primary_key_tag = _label;
			primary_key = _aux - 1;
			setRequiredKey(true);
		}
	}

	private void setRequiredKey(boolean _flag) {
		primary_key_requiered = _flag;
	}

	public void createAttributeList() {
		try {
			attributeList = new TXMLAttributeList();
			attributelistcreated = true;
		} catch (Exception ex) {
			System.out.println("ERROR: XML Node -Attribute list-  was not created.");
			// ex.printStackTrace();
		}
	}

	protected int addInformation(String _term, String _label) {
		int _ret = -1;
		try {
			TXMLElement instance = new TXMLElement(_term, _label);
			getElementList().add(instance);
			instance = null;
			_ret = getElementList().size();
		} catch (Exception ex) {
			System.out.println("ERROR: XML Node was not created.");
			// ex.printStackTrace();
		}
		return _ret;
	}

	private int addInformation(String _term, String _label, Node _attributes) {
		int _ret = -1;
		try {
			TXMLElement Instance = new TXMLElement(_term, _label, _attributes);
			getElementList().add(Instance);
			Instance = null;
			_ret = getElementList().size();
		} catch (Exception ex) {
			System.out.println("ERROR: XML Node was not created.");
			// ex.printStackTrace();
		}
		return _ret;
	}

	protected int addInformation(boolean _overwrite, Vector _terms, Vector _labels) {
		int _ret = -1;
		if (_terms.size() == _labels.size()) {
			int i = 0;
			boolean _insert = false;
			for (; i < _terms.size(); i++) {
				int _index = searchElementbyTag((String) _labels.elementAt(i));
				if (_index != -1 && _overwrite) {
					getElement(_index).SetValue((String) _terms.elementAt(i));
					_insert = true;
					_ret = _index;
				}
				if (!_overwrite || !_insert)
					_ret = addInformation((String) _terms.elementAt(i), (String) _labels.elementAt(i));
			}

		}
		return _ret;
	}

	public String getRootTag(String _tag, int _level) {
		String _ret;
		if (isNodeWithAttributes())
			_ret = getStartTagWithAttributes(_tag, _level);
		else
			_ret = getStartTag(_tag, _level);
		return _ret;
	}

	public String getStartTag(String _tag, int _level) {
		String _ret = createIndexTab(_level);
		_ret += "<" + _tag + ">";
		return _ret;
	}

	public String getStartTagWithAttributes(String _tag, int _level) {
		String _ret = createIndexTab(_level);
		_ret = String.valueOf(String.valueOf(_ret)).concat("<");
		_ret = String.valueOf(_ret) + String.valueOf(_tag);
		if (getAttributeList().IsAttributeList()) {
			for (String attributeLabel : getAttributeList().getAttributes().keySet()) {
				_ret += " " + attributeLabel + "=\"" + getAttributeList().getAttributes().get(attributeLabel) + "\"";
			}
		}
		_ret += ">";
		return _ret;
	}

	public String getEndTag(String _tag, int _level) {
		String _ret = createIndexTab(_level);
		_ret += "</" + _tag + ">";
		return _ret;
	}

	protected String createIndexTab(int _level) {
		int i = 0;
		String _ret = "";
		for (; i < _level; i++)
			_ret += "\t";

		return _ret;
	}

	public int parseNode(Node _mainNode) {
		NodeList _nodeList = _mainNode.getChildNodes();
		String _stringlabel, _stringvalue;
		addRootAttributes(_mainNode);
		int i = 0;
		int _count_aux = 0;
		for (; i < _nodeList.getLength(); i++) {
			_stringlabel = _stringvalue = "";
			Node _labelNode = _nodeList.item(i);
			Node _dataNode = _labelNode.getFirstChild();
			_stringlabel = _labelNode.getNodeName();
			if (_dataNode != null)
				_stringvalue = _dataNode.getNodeValue();
			if (_stringlabel.compareTo(getKeyTag()) == 0)
				setPrimaryKey(_stringvalue, getKeyTag(), _labelNode);
			else if (!isComplexNode(_labelNode))
				if (_labelNode.hasAttributes())
					addInformation(_stringvalue, _stringlabel, _labelNode);
				else
					addInformation(_stringvalue, _stringlabel);
			_count_aux++;
			_labelNode = null;
			_dataNode = null;
		}

		_nodeList = null;
		return _count_aux;
	}

	public String getXMLEntry(int _index) {
		String _ret = getRootTag(getStartSection(), _index) + "\n";
		for (int i = 0; i < getElementCount(); i++)
			_ret += getElement(i).GetXMLEntry(_index + 1);

		_ret += getEndTag(getStartSection(), _index) + "\n";
		return _ret;
	}

	public boolean isComplexNode(Node _node) {
		boolean _ret = false;
		if (_node.getFirstChild() != _node.getLastChild())
			_ret = true;
		return _ret;
	}

	public void addRootAttributes(Node _node) {
		if (_node.hasAttributes()) {
			createAttributeList();
			getAttributeList().createAditionalList();
			getAttributeList().parseNode(_node);
			getAttributeList().DisableStartTag();
			activateAttributes();
		}
	}

	public void createRootAttribute(String _value, String _tag) {
		createAttributeList();
		getAttributeList().createAditionalList();
		getAttributeList().addAttribute(_tag, _value);
		getAttributeList().DisableStartTag();
		activateAttributes();
	}

	public void addRootAttribute(String _value, String _tag) {
		getAttributeList().addAttribute(_tag, _value);
		getAttributeList().DisableStartTag();
		activateAttributes();
	}

	public SourceFileRef translate2SourceFileRef() {
		SourceFileRef ret = new SourceFileRef();

		final String ref = this.getAttributeList().getAttributes().get("ref");
		if (ref != null)
			ret.setRef(ref);

		return ret;
	}

	public String translate2SoftwareRef() {
		return this.getAttributeList().getAttributes().get("ref");
	}

}