// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov Date: 12/14/2007
// 12:23:24 PM
// Home Page : http://members.fortunecity.com/neshkov/dj.html - Check often for
// new version!
// Decompiler options: packimports(3)
// sourceList File Name: TXMLMzDataDescInstrument.java

package org.proteored.miapeapi.xml.mzml.lightParser;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import uk.ac.ebi.jmzml.model.mzml.ComponentList;

// Referenced classes of package prideparser:
// TXMLNode, TXMLParamGroup, TXMLElement

public class TXMLcomponentList extends TXMLNode {
	private ArrayList<TXMLParamGroup> sourceList;
	private ArrayList<TXMLParamGroup> analyzerList;
	private ArrayList<TXMLParamGroup> detectorList;
	// private TXMLParamGroup Additionalinfo;
	// metemos el tag de nombre de instrumento y un nombre por defecto
	// private final String instrumentNameTag = "instrumentName";
	// private final String instrumentNameValue = "Not provided";
	// private final String analyzerListTag = "analyzerList";
	private final String analyzerTag = "analyzer";
	private final String sourceTag = "source";
	private final String detectorTag = "detector";
	private final String countTag = "count";

	// Para el analizador
	// private final String defaultanalyzervalue =
	// "No analyzerList Components Listed";
	// private final String defaultanalyzertag = "name";
	public TXMLcomponentList() {
		super.setStartTag("componentList");
		// super.SetPrimaryTag(instrumentNameTag);
		// super.SetPrimaryKey(instrumentNameValue,instrumentNameTag);
		// analyzerListTag = "analyzerList";
		// analyzerTag = "analyzer";
		// countTag = "count";
		try {
			sourceList = new ArrayList<TXMLParamGroup>();
			analyzerList = new ArrayList<TXMLParamGroup>();
			detectorList = new ArrayList<TXMLParamGroup>();
			// Additionalinfo = new TXMLParamGroup();
			// Additionalinfo.SetStartTag("additional");
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public int getComponentListCount() {
		return getDetectorListCount() + getAnalyzerListCount() + getSourceListCount();
	}

	public List<TXMLParamGroup> getSourceList() {
		return sourceList;
	}

	public TXMLParamGroup getSourceElement(int index) {
		return getSourceList().get(index);
	}

	public int getSourceListCount() {
		return getSourceList().size();
	}

	public List<TXMLParamGroup> getDetectorList() {
		return detectorList;
	}

	public TXMLParamGroup getDetectorElement(int index) {
		return getDetectorList().get(index);
	}

	public int getDetectorListCount() {
		return getDetectorList().size();
	}

	/*
	 * public TXMLParamGroup getAdditional() { return Additionalinfo; }
	 */
	public List<TXMLParamGroup> getAnalyzerList() {
		return analyzerList;
	}

	public TXMLParamGroup getAnalyzerElement(int index) {
		return getAnalyzerList().get(index);
	}

	public int getAnalyzerListCount() {
		return getAnalyzerList().size();
	}

	public void addAnalyzer(Node _mainNode) {
		try {
			TXMLParamGroup Instance = new TXMLParamGroup(analyzerTag);
			// Instance.setPrimaryTag("cvParam");
			Instance.createAttributeList();
			Instance.parseNode(_mainNode);
			getAnalyzerList().add(Instance);
		} catch (Exception exception) {
		}
	}

	public void addSource(Node _mainNode) {
		try {
			TXMLParamGroup Instance = new TXMLParamGroup(sourceTag);
			// Instance.setPrimaryTag("cvParam");
			Instance.createAttributeList();
			Instance.parseNode(_mainNode);
			getSourceList().add(Instance);
		} catch (Exception exception) {
		}
	}

	public void addDetector(Node _mainNode) {
		try {
			TXMLParamGroup Instance = new TXMLParamGroup(detectorTag);
			// Instance.setPrimaryTag("cvParam");
			Instance.createAttributeList();
			Instance.parseNode(_mainNode);
			getDetectorList().add(Instance);
		} catch (Exception exception) {
		}
	}

	@Override
	public int parseNode(Node _mainNode) {
		NodeList _nodeList = _mainNode.getChildNodes();
		int i = 0;
		int _count_aux = 0;
		String _stringlabel, _stringvalue;
		for (; i < _nodeList.getLength(); i++) {
			_stringlabel = _stringvalue = "";
			Node _labelNode = _nodeList.item(i);
			Node _dataNode = _labelNode.getFirstChild();
			_stringlabel = _labelNode.getNodeName();
			if (_dataNode != null)
				_stringvalue = _dataNode.getNodeValue();
			if (_stringlabel.compareTo(super.getKeyTag()) == 0)
				super.setPrimaryKey(_stringvalue, _stringlabel);
			else if (_stringlabel.compareTo(analyzerTag) == 0)
				this.addAnalyzer(_labelNode);
			else if (_stringlabel.compareTo(detectorTag) == 0)
				this.addDetector(_labelNode);
			else if (_stringlabel.compareTo(sourceTag) == 0)
				this.addSource(_labelNode);
			else if (!super.isComplexNode(_labelNode))
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
		// Lo primero el nombre del instrumento
		// _ret +=
		// this.GetElementFromTag(this.instrumentNameTag).GetXMLEntry(_index +
		// 1);
		// sourceList list
		for (int i = 0; i < getSourceListCount(); i++)
			_ret = String.valueOf(_ret)
					+ String.valueOf(getSourceElement(i).getXMLEntry(_index + 1));

		// analyzerList List
		for (int i = 0; i < getAnalyzerListCount(); i++)
			_ret = String.valueOf(_ret)
					+ String.valueOf(getAnalyzerElement(i).getXMLEntry(_index + 1));
		// detector
		for (int i = 0; i < getDetectorListCount(); i++)
			_ret = String.valueOf(_ret)
					+ String.valueOf(getDetectorElement(i).getXMLEntry(_index + 1));
		// Adicional
		_ret = String.valueOf(_ret)
				+ String.valueOf(String.valueOf(
						String.valueOf(getEndTag(getStartSection(), _index))).concat("\n"));
		return _ret;
	}

	public ComponentList translate2ComponentList() {
		ComponentList ret = new ComponentList();

		final List<TXMLParamGroup> sources = getSourceList();
		if (sources != null) {
			for (TXMLParamGroup source : sources) {
				ret.getComponents().add(source.translate2Source());
			}
		}
		final List<TXMLParamGroup> analyzers = getAnalyzerList();
		if (analyzers != null) {
			for (TXMLParamGroup analyzer : analyzers) {
				ret.getComponents().add(analyzer.translate2Analyzer());
			}
		}
		final List<TXMLParamGroup> detectors = getDetectorList();
		if (detectors != null) {
			for (TXMLParamGroup detector : detectors) {
				ret.getComponents().add(detector.translate2Detector());
			}
		}
		ret.setCount(getComponentListCount());
		return ret;
	}
}