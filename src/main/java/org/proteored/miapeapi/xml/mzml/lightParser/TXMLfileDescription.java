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

import uk.ac.ebi.jmzml.model.mzml.FileDescription;
import uk.ac.ebi.jmzml.model.mzml.ParamGroup;
import uk.ac.ebi.jmzml.model.mzml.SourceFileList;

// Referenced classes of package prideparser:
// TXMLNode, TXMLParamGroup, TXMLElement

public class TXMLfileDescription extends TXMLNode {
	private TXMLParamGroup fileContent;
	// private TXMLParamGroup ProcessingMethod;
	private List<TXMLParamGroup> sourceFileList;
	// Lo primero creamos las cadenas que vamos a necesitar
	private final String filecontenttag = "fileContent";
	private final String sourcefiletag = "sourceFile";
	private final String sourcefileListtag = "sourceFileList";
	private final String sourcefileListcounttag = "count";
	private ArrayList<TXMLParamGroup> contactList;
	private final String contacttag = "contact";

	public TXMLfileDescription() {
		super.setStartTag("fileDescription");
		try {
			fileContent = new TXMLParamGroup(filecontenttag);
			fileContent.setPrimaryTag(filecontenttag);
			contactList = new ArrayList<TXMLParamGroup>();
			sourceFileList = new ArrayList<TXMLParamGroup>();
			// ProcessingMethod = new TXMLParamGroup();
			// ProcessingMethod.SetStartTag(processingtag);
		} catch (Exception exception) {
		}
	}

	public TXMLParamGroup getfileContent() {
		return fileContent;
	}

	public List<TXMLParamGroup> getContactList() {
		return contactList;
	}

	public TXMLNode getContactElement(int index) {
		return getContactList().get(index);
	}

	public int getContactListCount() {
		return getContactList().size();
	}

	public List<TXMLParamGroup> getsourceFileList() {
		return sourceFileList;
	}

	public TXMLParamGroup getsourceFileElement(int index) {
		return getsourceFileList().get(index);
	}

	public int getsourceFileListCount() {
		return getsourceFileList().size();
	}

	public void addsourceFile(Node _newNode) {
		try {
			TXMLParamGroup Instance = new TXMLParamGroup(sourcefiletag);
			Instance.createAttributeList();
			Instance.parseNode(_newNode);
			getsourceFileList().add(Instance);
		} catch (Exception exception) {
		}

	}

	public void addContactElement(Node _newNode) {
		try {
			TXMLParamGroup Instance = new TXMLParamGroup(contacttag);
			Instance.parseNode(_newNode);
			getContactList().add(Instance);
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
			else if (_stringlabel.compareTo(getfileContent().getStartSection()) == 0)
				getfileContent().parseNode(_labelNode);
			else if (_stringlabel.compareTo(contacttag) == 0)
				this.addContactElement(_labelNode);
			else if (_stringlabel.compareTo(sourcefileListtag) == 0)
			// GetProcessingMethod().ParseNode(_labelNode);
			{
				NodeList _auxnodeList = _labelNode.getChildNodes();
				int j = 0;
				_count_aux = 0;
				for (; j < _auxnodeList.getLength(); j++) {
					Node _auxNode = _auxnodeList.item(j);
					String _auxstringlabel = _auxNode.getNodeName();
					if (_auxstringlabel.compareTo(sourcefiletag) == 0)
						addsourceFile(_auxNode);
				}
			} else if (!super.isComplexNode(_labelNode))
				addInformation(_stringvalue, _stringlabel);
			_count_aux++;
		}

		return _count_aux;
	}

	@Override
	public String getXMLEntry(int _index) {
		String _ret = String.valueOf(String.valueOf(getRootTag(getStartSection(), _index))).concat(
				"\n");
		// for(int i = 0; i < GetElementCount(); i++)
		// _ret = String.valueOf(_ret) +
		// String.valueOf(GetElement(i).GetXMLEntry(_index + 1));
		// if (getSoftware().GetElementCount() == 0)
		// this.CreateDefaultSoftware();
		_ret = String.valueOf(_ret) + String.valueOf(getfileContent().getXMLEntry(_index + 1));
		_ret = String.valueOf(_ret)
				+ String.valueOf(String.valueOf(
						String.valueOf(getStartTag(String.valueOf(String.valueOf((new StringBuffer(
								String.valueOf(String.valueOf(sourcefileListtag)))).append(" ")
								.append(sourcefileListcounttag).append("=\"")
								.append(getsourceFileListCount()).append("\""))), _index + 1)))
						.concat("\n"));
		for (int i = 0; i < getsourceFileListCount(); i++)
			_ret = String.valueOf(_ret)
					+ String.valueOf(getsourceFileElement(i).getXMLEntry(_index + 1));
		_ret = String.valueOf(_ret)
				+ String.valueOf(String.valueOf(
						String.valueOf(getEndTag(sourcefileListtag, _index + 1))).concat("\n"));

		for (int i = 0; i < getContactListCount(); i++)
			_ret = String.valueOf(_ret)
					+ String.valueOf(getContactElement(i).getXMLEntry(_index + 1));
		_ret = String.valueOf(_ret)
				+ String.valueOf(String.valueOf(String.valueOf(getEndTag(contacttag, _index + 1)))
						.concat("\n"));

		_ret = String.valueOf(_ret)
				+ String.valueOf(String.valueOf(
						String.valueOf(getEndTag(getStartSection(), _index))).concat("\n"));
		return _ret;
	}

	public FileDescription translate2FileDescription() {
		FileDescription ret = new FileDescription();

		// fileContent
		ParamGroup fileContent = getfileContent().translate2ParamGroup();
		ret.setFileContent(fileContent);

		// sourceFileList
		final List<TXMLParamGroup> sourceFileList = getsourceFileList();
		if (sourceFileList != null) {
			SourceFileList jMzmLSourceFileList = new SourceFileList();
			for (TXMLParamGroup sourceFile : sourceFileList) {
				jMzmLSourceFileList.getSourceFile().add(sourceFile.translate2SourceFile());
			}
			jMzmLSourceFileList.setCount(sourceFileList.size());
			ret.setSourceFileList(jMzmLSourceFileList);
		}

		// Contact
		for (TXMLParamGroup contact : getContactList()) {
			ret.getContact().add(contact.translate2ParamGroup());
		}

		return ret;
	}
}