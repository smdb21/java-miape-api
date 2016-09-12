// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov Date: 12/14/2007
// 12:21:18 PM
// Home Page : http://members.fortunecity.com/neshkov/dj.html - Check often for
// new version!
// Decompiler options: packimports(3)
// Source File Name: TXMLAttributeList.java

package org.proteored.miapeapi.xml.mzml.lightParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import uk.ac.ebi.jmzml.model.mzml.CVParam;
import uk.ac.ebi.jmzml.model.mzml.ReferenceableParamGroupRef;
import uk.ac.ebi.jmzml.model.mzml.UserParam;

// Referenced classes of package prideparser:
// TXMLNode, TXMLElement

public class TXMLAttributeList extends TXMLNode {
	private List<String> attribute_labels;
	private List<String> attribute_data;
	private final HashMap<String, String> attributes = new HashMap<String, String>();
	private boolean vectorcreated;
	private boolean usestarttag;

	public TXMLAttributeList() {
		super.setStartTag("attributeList");
		super.setPrimaryKey("", "Attribute");
		usestarttag = true;
		vectorcreated = false;
	}

	public void createAditionalList() {
		if (!vectorcreated) {
			vectorcreated = true;
			try {
				attribute_labels = new ArrayList<String>();
				attribute_data = new ArrayList<String>();
				attributes.clear();
			} catch (Exception exception) {
			}
		}
	}

	public List<String> GetAttributeDataList() {
		return attribute_data;
	}

	public List<String> getAttributeLabelsList() {
		return attribute_labels;
	}

	public HashMap<String, String> getAttributes() {
		return attributes;
	}

	public String getAttributeValue(String attributeLabel) {
		if (attributes.containsKey(attributeLabel))
			return attributes.get(attributeLabel);
		return null;
	}

	public String GetAttributeLabel(int _pos) {
		return getAttributeLabelsList().get(_pos);
	}

	public String GetAttributeData(int _pos) {
		return GetAttributeDataList().get(_pos);
	}

	/*
	 * public String GetAttributeData(String _label) { String _ret = null; int
	 * _aux = getAttributeLabelsList().indexOf(_label); if (_aux != -1) _ret =
	 * GetAttributeData(_aux); return _ret; }
	 */

	public int GetAttributeCount() {
		return attributes.size();
		/*
		 * int _ret; if (GetAttributeDataList().size() ==
		 * getAttributeLabelsList().size()) _ret =
		 * getAttributeLabelsList().size(); else _ret = 0; return _ret;
		 */
	}

	public boolean IsAttributeList() {
		return vectorcreated;
	}

	public void DisableStartTag() {
		usestarttag = false;
	}

	public void EnableStartTag() {
		usestarttag = true;
	}

	public void addAttribute(String label, String value) {
		if (!getAttributes().containsKey(label))
			getAttributes().put(label, value);
	}

	@Override
	public String getXMLEntry(int _index) {
		String _ret = "";
		if (IsAttributeList()) {
			if (GetAttributeCount() > 0) {
				if (usestarttag)
					_ret += createIndexTab(_index + 1) + "<" + super.getKeyTag();
				else
					_ret = "";
				for (String attibuteLabel : getAttributes().keySet()) {
					_ret += " " + attibuteLabel + "=\"" + getAttributes().get(attibuteLabel) + "\"";
				}

				if (usestarttag)
					_ret = String.valueOf(String.valueOf(_ret)).concat("/>");
			}
		} else if (super.getElementCount() > 0) {
			if (usestarttag)
				_ret = getStartTag(super.getStartSection(), _index) + "\n";
			for (int i = 0; i < super.getElementCount(); i++) {
				_ret += createIndexTab(_index + 1) + "<" + super.getKeyTag();
				_ret += " name=\"" + super.getElement(i).GetTag() + "\">";
				_ret += super.getElement(i).GetValue() + getEndTag(super.getKeyTag(), 0) + "\n";
			}

			if (usestarttag)
				_ret += getEndTag(super.getStartSection(), _index) + "\n";
		}
		return _ret;
	}

	@Override
	public int parseNode(Node _mainNode) {
		int _count_aux;
		if (!IsAttributeList()) {
			NodeList _attributeList = _mainNode.getChildNodes();
			int i = 1;
			_count_aux = 0;
			for (; i < _attributeList.getLength(); i += 2) {
				Node _attributeNode = _attributeList.item(i);
				NamedNodeMap _attributeNodeMap = _attributeNode.getAttributes();
				String _stringlabel = _attributeNodeMap.item(0).getNodeValue();
				String _stringvalue = _attributeNode.getFirstChild().getNodeValue();
				super.addInformation(_stringvalue, _stringlabel);
			}

			_count_aux++;
		} else {
			NamedNodeMap _attributeNodeMap = _mainNode.getAttributes();
			int i = 0;
			_count_aux = 0;
			for (; i < _attributeNodeMap.getLength(); i++) {
				String _stringlabel = _attributeNodeMap.item(i).getNodeName();
				String _stringvalue = _attributeNodeMap.item(i).getNodeValue();
				addAttribute(_stringlabel, _stringvalue);
			}

			_count_aux++;
		}
		return _count_aux;
	}

	public UserParam translate2UserParam() {

		if (this.getKeyTag().equals("userParam")) {
			UserParam userParam = new UserParam();
			String name = this.getAttributeValue("name");
			if (name != null)
				userParam.setName(name);
			String value = this.getAttributeValue("value");
			if (value != null)
				userParam.setValue(value);
			String unitAccesion = this.getAttributeValue("unitAccesion");
			if (unitAccesion != null)
				userParam.setUnitAccession(unitAccesion);
			String unitName = this.getAttributeValue("unitName");
			if (unitName != null)
				userParam.setUnitName(unitName);
			String unitCvRef = this.getAttributeValue("unitCvRef");
			if (unitCvRef != null)
				userParam.setUnitCvRef(unitCvRef);

			return userParam;
		}
		return null;
	}

	public CVParam translate2CVParam() {
		if (this.getKeyTag().equals("cvParam")) {
			CVParam cvParam = new CVParam();
			String cvRef = this.getAttributeValue("cvRef");
			if (cvRef != null)
				cvParam.setCvRef(cvRef);
			String accession = this.getAttributeValue("accession");
			if (accession != null)
				cvParam.setAccession(accession);
			String name = this.getAttributeValue("name");
			if (name != null)
				cvParam.setName(name);
			String value = this.getAttributeValue("value");
			if (value != null)
				cvParam.setValue(value);
			String unitAccesion = this.getAttributeValue("unitAccesion");
			if (unitAccesion != null)
				cvParam.setUnitAccession(unitAccesion);
			String unitName = this.getAttributeValue("unitName");
			if (unitName != null)
				cvParam.setUnitName(unitName);
			String unitCvRef = this.getAttributeValue("unitCvRef");
			if (unitCvRef != null)
				cvParam.setUnitCvRef(unitCvRef);

			return cvParam;
		}
		return null;
	}

	public ReferenceableParamGroupRef translate2RefParamGroup() {
		if (this.getKeyTag().equals("referenceableParamGroupRef")) {
			ReferenceableParamGroupRef ret = new ReferenceableParamGroupRef();
			String ref = this.getAttributeValue("ref");
			if (ref != null)
				ret.setRef(ref);
			else
				return null;
			return ret;
		}
		return null;
	}

}