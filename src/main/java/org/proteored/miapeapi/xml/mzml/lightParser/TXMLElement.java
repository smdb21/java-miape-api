// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov Date: 12/14/2007
// 12:21:34 PM
// Home Page : http://members.fortunecity.com/neshkov/dj.html - Check often for
// new version!
// Decompiler options: packimports(3)
// Source File Name: TXMLElement.java

package org.proteored.miapeapi.xml.mzml.lightParser;

import org.w3c.dom.Node;

import uk.ac.ebi.jmzml.model.mzml.CVParam;
import uk.ac.ebi.jmzml.model.mzml.ReferenceableParamGroupRef;
import uk.ac.ebi.jmzml.model.mzml.UserParam;

// Referenced classes of package prideparser:
// TXMLAttributeList

public class TXMLElement {
	String tag;
	String value;
	TXMLAttributeList attributeList;
	boolean attributelistcreated;

	public TXMLElement(String _value, String _tag) {
		tag = _tag;
		value = _value;
		attributelistcreated = false;
	}

	public TXMLElement(String _value, String _tag, Node _attribute) {
		tag = _tag;
		value = _value;
		try {
			attributeList = new TXMLAttributeList();
			attributeList.createAditionalList();
			attributeList.DisableStartTag();
			attributeList.parseNode(_attribute);
			attributelistcreated = true;
		} catch (Exception exception) {
		}
	}

	public String GetTag() {
		return tag;
	}

	public String GetValue() {
		return value;
	}

	public boolean AttributeListCreated() {
		return attributelistcreated;
	}

	public TXMLAttributeList GetAttributeList() {
		return attributeList;
	}

	public String GetXMLEntry(int _index) {
		String _ret = CreateIndexTab(_index);
		_ret = String.valueOf(_ret) + String.valueOf("<".concat(String.valueOf(String.valueOf(GetTag()))));
		if (AttributeListCreated())
			_ret = String.valueOf(_ret) + String.valueOf(GetAttributeList().getXMLEntry(0));
		_ret = String.valueOf(String.valueOf(_ret)).concat(">");
		_ret = String.valueOf(_ret) + String.valueOf(GetValue());
		_ret = String.valueOf(_ret)
				+ String.valueOf(String.valueOf(String.valueOf((new StringBuffer("</")).append(GetTag()).append(">\n"))));
		return _ret;
	}

	public void SetValue(String _newvalue) {
		value = _newvalue;
	}

	public void SetAttributes(Node _attribute) {
		try {
			attributeList = new TXMLAttributeList();
			attributeList.createAditionalList();
			attributeList.DisableStartTag();
			attributeList.parseNode(_attribute);
			attributelistcreated = true;
		} catch (Exception exception) {
		}
	}

	protected String CreateIndexTab(int _level) {
		int i = 0;
		String _ret = "";
		for (; i < _level; i++)
			_ret = String.valueOf(String.valueOf(_ret)).concat("\t");

		return _ret;
	}

	public CVParam translate2CVParam() {
		if (attributeList != null)
			return attributeList.translate2CVParam();
		return null;
	}

	public UserParam translate2UserParam() {
		if (attributeList != null)
			return attributeList.translate2UserParam();
		return null;
	}

	public ReferenceableParamGroupRef translate2RefParamGroup() {
		if (attributeList != null)
			return attributeList.translate2RefParamGroup();
		return null;
	}

}