// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov Date: 12/14/2007
// 12:23:00 PM
// Home Page : http://members.fortunecity.com/neshkov/dj.html - Check often for
// new version!
// Decompiler options: packimports(3)
// Source File Name: TXMLMzDataDescAdmin.java

package org.proteored.miapeapi.xml.mzml.lightParser;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import uk.ac.ebi.jmzml.model.mzml.ScanSettings;

// Referenced classes of package prideparser:
// TXMLNode, TXMLParamGroup, TXMLElement

public class TXMLscanSettings extends TXMLParamGroup {
	private static String scanSettingsTag = "scanSettings";
	private final String sourceFileRefListTag = "sourceFileRefList";
	private TXMLSourceFileRefList sourceFileRefList;
	private TXMLTargetList targeList;
	private final String targetListTag = "targetList";

	// Metemos las etiquetas

	public TXMLscanSettings() {
		super(scanSettingsTag);
		try {
			targeList = new TXMLTargetList();
			sourceFileRefList = new TXMLSourceFileRefList();

		} catch (Exception exception) {
		}
	}

	public TXMLSourceFileRefList getSourceFileRefList() {
		return sourceFileRefList;
	}

	public TXMLTargetList getTargetList() {
		return targeList;
	}

	@Override
	public int parseNode(Node _mainNode) {
		int _count_aux = 0;
		boolean _userparam;
		boolean _refParam;
		boolean _cvparam = _userparam = _refParam = false;
		NodeList _nodeList = _mainNode.getChildNodes();
		super.addRootAttributes(_mainNode);
		int i = 0;
		_count_aux = 0;
		for (; i < _nodeList.getLength(); i++) {
			Node _labelNode = _nodeList.item(i);
			String _stringlabel = _labelNode.getNodeName();
			if (_stringlabel.compareTo(GetCVParams().getKeyTag()) == 0)
				_cvparam = true;
			if (_stringlabel.compareTo(GetUserParams().getKeyTag()) == 0)
				_userparam = true;
			if (_stringlabel.compareTo(GetReferenceableParams().getKeyTag()) == 0)
				_refParam = true;
			if (_stringlabel.compareTo(sourceFileRefListTag) == 0)
				this.getSourceFileRefList().parseNode(_labelNode);
			if (_stringlabel.compareTo(targetListTag) == 0)
				this.getTargetList().parseNode(_labelNode);
		}

		if (_cvparam)
			_count_aux = GetCVParams().parseNode(_mainNode);
		if (_userparam)
			_count_aux = GetUserParams().parseNode(_mainNode);
		if (_refParam)
			_count_aux = GetReferenceableParams().parseNode(_mainNode);
		return _count_aux;
	}

	@Override
	public String getXMLEntry(int _index) {
		String _ret = "";
		_ret += getRootTag(getStartSection(), _index) + "\n";
		if (GetCVParams() != null) {
			_ret += GetCVParams().getXMLEntryWithoutHead(_index);
		}
		if (GetUserParams() != null) {
			_ret += GetUserParams().getXMLEntryWithoutHead(_index);
		}
		if (GetReferenceableParams() != null) {
			_ret += GetReferenceableParams().getXMLEntryWithoutHead(_index);
		}
		_ret += this.getSourceFileRefList().getXMLEntry(_index);
		_ret += this.getTargetList().getXMLEntry(_index);
		_ret += getEndTag(getStartSection(), _index) + "\n";
		return _ret;
	}

	public ScanSettings translate2ScanSettings() {
		ScanSettings ret = new ScanSettings();

		final String id = this.getAttributeList().getAttributes().get("id");
		if (id != null)
			ret.setId(id);

		ret.setSourceFileRefList(this.sourceFileRefList.translate2SourceFileRefList());
		ret.setTargetList(this.getTargetList().translate2TargetList());
		return ret;
	}
}