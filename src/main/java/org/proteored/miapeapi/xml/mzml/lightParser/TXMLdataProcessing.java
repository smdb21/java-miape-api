// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov Date: 12/14/2007
// 12:23:09 PM
// Home Page : http://members.fortunecity.com/neshkov/dj.html - Check often for
// new version!
// Decompiler options: packimports(3)
// Source File Name: TXMLMzDataDescDataProcessing.java

package org.proteored.miapeapi.xml.mzml.lightParser;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import uk.ac.ebi.jmzml.model.mzml.DataProcessing;

// Referenced classes of package prideparser:
// TXMLNode, TXMLParamGroup, TXMLElement

public class TXMLdataProcessing extends TXMLNode {
	// private TXMLParamGroup processingMethod;
	private List<TXMLParamGroup> processingMethod;
	// Lo primero creamos las cadenas que vamos a necesitar
	private final String processingMethodtag = "processingMethod";

	public TXMLdataProcessing() {
		super.setStartTag("dataProcessing");
		try {
			processingMethod = new ArrayList<TXMLParamGroup>();
			// processingMethod = new TXMLParamGroup();
			// processingMethod.SetStartTag(processingMethodtag);
		} catch (Exception exception) {
		}
	}

	public List<TXMLParamGroup> getProcessingMethodList() {
		return processingMethod;
	}

	public TXMLParamGroup getProcessingMethod(int index) {
		return getProcessingMethodList().get(index);
	}

	public void addProcessingMethod(Node _newNode) {
		try {
			TXMLParamGroup Instance = new TXMLParamGroup(processingMethodtag);
			Instance.createAttributeList();
			Instance.parseNode(_newNode);
			getProcessingMethodList().add(Instance);
		} catch (Exception exception) {
		}

	}

	@Override
	public int parseNode(Node _mainNode) {
		NodeList _nodeList = _mainNode.getChildNodes();
		String _stringlabel, _stringvalue;
		int i = 0;
		int _count_aux = 0;
		for (; i < _nodeList.getLength(); i++) {
			_stringlabel = _stringvalue = "";
			Node _labelNode = _nodeList.item(i);
			Node _dataNode = _labelNode.getFirstChild();
			_stringlabel = _labelNode.getNodeName();
			if (_dataNode != null)
				_stringvalue = _dataNode.getNodeValue();
			if (_stringlabel.compareTo(super.getKeyTag()) == 0)
				super.setPrimaryKey(_stringvalue, _stringlabel);
			else if (_stringlabel.compareTo(processingMethodtag) == 0)
				// GetProcessingMethod().ParseNode(_labelNode);
				addProcessingMethod(_labelNode);
			else if (!super.isComplexNode(_labelNode))
				addInformation(_stringvalue, _stringlabel);
			_count_aux++;
		}

		return _count_aux;
	}

	@Override
	public String getXMLEntry(int _index) {
		String _ret = getRootTag(getStartSection(), _index) + "\n";
		// for(int i = 0; i < GetElementCount(); i++)
		// _ret = String.valueOf(_ret) +
		// String.valueOf(GetElement(i).GetXMLEntry(_index + 1));
		// if (getSoftware().GetElementCount() == 0)
		// this.CreateDefaultSoftware();
		// _ret = String.valueOf(_ret) +
		// String.valueOf(getSoftware().GetXMLEntry(_index + 1));
		for (int i = 0; i < this.getProcessingMethodList().size(); i++)
			_ret += getProcessingMethod(i).getXMLEntry(_index + 1);
		_ret += getEndTag(getStartSection(), _index) + "\n";
		return _ret;
	}

	public DataProcessing translate2DataProcessing() {
		DataProcessing ret = new DataProcessing();

		ret.setId(this.getAttributeList().getAttributes().get("id"));
		for (TXMLParamGroup paramGroup : this.getProcessingMethodList()) {
			ret.getProcessingMethod().add(paramGroup.translate2ProcessingMethod());
		}

		return ret;
	}
}