// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov Date: 12/14/2007
// 12:26:58 PM
// Home Page : http://members.fortunecity.com/neshkov/dj.html - Check often for
// new version!
// Decompiler options: packimports(3)
// Source File Name: TXMLUserAdditionalInfo.java

package org.proteored.miapeapi.xml.mzml.lightParser;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

// Referenced classes of package prideparser:
// TXMLNode, TXMLUserAdditionalGO, TXMLAttributeList

public class TXMLUserAdditionalInfo extends TXMLNode {
	private List<TXMLAttributeList> paramList;
	private final String cvParam = "cvParam";
	private final String userParam = "userParam";
	private final String cvLabelTag = "cvLabel";
	private final String cvAccesionTag = "accession";
	private final String cvNameTag = "name";
	private final String cvValueTag = "value";
	// Nuevas variables para devolver el nombre del parametro de forma genarica
	// Para user params
	private final String GenericUserTag = "name";
	private final String GenericUserKey = "value";

	// Para CV params. Usamos los definidos

	public TXMLUserAdditionalInfo() {
		super.setStartTag("additional");
		super.setPrimaryKey("", "userParam");
		try {
			paramList = new ArrayList<TXMLAttributeList>();
		} catch (Exception exception) {
		}
	}

	public List<TXMLAttributeList> getParamsList() {
		return paramList;
	}

	public TXMLAttributeList getParamsElement(int _pos) {
		return getParamsList().get(_pos);
	}

	public int getParamsCount() {
		return getParamsList().size();
	}

	public void addParameter(Node _mainNode) {
		try {
			TXMLAttributeList Instance = new TXMLAttributeList();
			Instance.setPrimaryKey("", super.getKeyTag());
			Instance.createAditionalList();
			Instance.parseNode(_mainNode);
			getParamsList().add(Instance);
		} catch (Exception exception) {
		}
	}

	@Override
	public int parseNode(Node _mainNode) {
		super.addRootAttributes(_mainNode);
		NodeList _nodeList = _mainNode.getChildNodes();
		int i = 0;
		int _count_aux = 0;
		for (; i < _nodeList.getLength(); i++) {
			Node _labelNode = _nodeList.item(i);
			Node _dataNode = _labelNode.getFirstChild();
			String _stringlabel = _labelNode.getNodeName();
			if (_stringlabel.compareTo(super.getKeyTag()) == 0) {
				addParameter(_labelNode);
				_count_aux++;
			}
		}

		return _count_aux;
	}

	@Override
	public String getXMLEntry(int _index) {
		String _ret = "";
		if (getParamsCount() > 0) {
			_ret += getRootTag(getStartSection(), _index) + "\n";
			for (int i = 0; i < getParamsCount(); i++)
				_ret += getParamsElement(i).getXMLEntry(_index + 1) + "\n";

			_ret += getEndTag(getStartSection(), _index) + "\n";
		}
		return _ret;
	}

	public String getXMLEntryWithoutHead(int _index) {
		String _ret = "";
		if (getParamsCount() > 0) {
			for (int i = 0; i < getParamsCount(); i++)
				_ret = String.valueOf(_ret)
						+ String.valueOf(String.valueOf(String.valueOf(getParamsElement(i).getXMLEntry(_index + 1)))
								.concat("\n"));

		}
		return _ret;
	}

	public String getXMLEntryWithoutHead(int _index, String _tag, String _value) {
		String _ret = "";
		int aux_params_pos;
		String aux_value;
		if (getParamsCount() > 0) {
			for (int i = 0; i < getParamsCount(); i++) {
				aux_params_pos = -1;
				aux_value = "";
				for (int j = 0; j < getParamsElement(i).GetAttributeCount(); j++)
					if (getParamsElement(i).GetAttributeData(j).compareTo(_tag) == 0) {
						aux_params_pos = i;
						aux_value = getParamsElement(i).getAttributeValue(GenericUserKey);
					}
				if (aux_params_pos == -1)
					_ret = String.valueOf(_ret)
							+ String.valueOf(String
									.valueOf(String.valueOf(getParamsElement(i).getXMLEntry(_index + 1))).concat("\n"));
				else {
					if (aux_value.compareTo(_value) == 0)
						_ret = String.valueOf(_ret)
								+ String.valueOf(String.valueOf(
										String.valueOf(getParamsElement(i).getXMLEntry(_index + 1))).concat("\n"));
				}
			}
		}
		return _ret;
	}

	public int getInsertPosition(String _tag) {
		int _ret = getParamsCount();
		for (int i = 0; i < getParamsCount(); i++)
			if (getParamsElement(i).GetAttributeDataList().indexOf(_tag) != -1)
				_ret = i + 1;

		return _ret;
	}

}