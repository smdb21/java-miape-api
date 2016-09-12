// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov Date: 12/14/2007
// 12:21:00 PM
// Home Page : http://members.fortunecity.com/neshkov/dj.html - Check often for
// new version!
// Decompiler options: packimports(3)
// Source File Name: TXMLParamGroup.java

package org.proteored.miapeapi.xml.mzml.lightParser;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import uk.ac.ebi.jmzml.model.mzml.AnalyzerComponent;
import uk.ac.ebi.jmzml.model.mzml.CVParam;
import uk.ac.ebi.jmzml.model.mzml.DetectorComponent;
import uk.ac.ebi.jmzml.model.mzml.ParamGroup;
import uk.ac.ebi.jmzml.model.mzml.ProcessingMethod;
import uk.ac.ebi.jmzml.model.mzml.ReferenceableParamGroup;
import uk.ac.ebi.jmzml.model.mzml.ReferenceableParamGroupRef;
import uk.ac.ebi.jmzml.model.mzml.Sample;
import uk.ac.ebi.jmzml.model.mzml.Software;
import uk.ac.ebi.jmzml.model.mzml.SourceComponent;
import uk.ac.ebi.jmzml.model.mzml.SourceFile;
import uk.ac.ebi.jmzml.model.mzml.UserParam;

// Referenced classes of package prideparser:
// TXMLNode, TXMLUserAdditionalInfo, TXMLUserAdditionalGO

public class TXMLParamGroup extends TXMLNode {
	private TXMLUserAdditionalInfo cvParam;
	private TXMLUserAdditionalInfo userParam;
	private TXMLUserAdditionalInfo referenceableParamRef;
	public static final String ref_tag = "referenceableParamGroupRef";
	public static final String userParam_tag = "userParam";
	public static final String cvParam_tag = "cvParam";

	public TXMLParamGroup(String _starttag) {
		super.setStartTag(_starttag);
		try {
			cvParam = new TXMLUserAdditionalInfo();
			cvParam.setStartTag(_starttag);
			cvParam.setPrimaryTag(cvParam_tag);
			cvParam.createAttributeList();
			userParam = new TXMLUserAdditionalInfo();
			userParam.setStartTag(_starttag);
			userParam.setPrimaryTag(userParam_tag);
			userParam.createAttributeList();
			referenceableParamRef = new TXMLUserAdditionalInfo();
			referenceableParamRef.createAttributeList();
			referenceableParamRef.setPrimaryTag(ref_tag);
			referenceableParamRef.setStartTag(_starttag);
			// Vamos a probar quitarlo por defecto
			// CreateGOVector();
			// CreateKeywordsVector();
		} catch (Exception exception) {
		}
	}

	public TXMLUserAdditionalInfo GetCVParams() {
		return cvParam;
	}

	public TXMLUserAdditionalInfo GetUserParams() {
		return userParam;
	}

	public TXMLUserAdditionalInfo GetReferenceableParams() {
		return referenceableParamRef;
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
		_ret = String.valueOf(_ret)
				+ String.valueOf(String.valueOf(
						String.valueOf(getRootTag(getStartSection(), _index))).concat("\n"));
		if (GetCVParams().getParamsCount() > 0)
			_ret = String.valueOf(_ret)
					+ String.valueOf(GetCVParams().getXMLEntryWithoutHead(_index));
		if (GetUserParams().getParamsCount() > 0)
			_ret = String.valueOf(_ret)
					+ String.valueOf(GetUserParams().getXMLEntryWithoutHead(_index));
		if (GetReferenceableParams().getParamsCount() > 0)
			_ret = String.valueOf(_ret)
					+ String.valueOf(GetReferenceableParams().getXMLEntryWithoutHead(_index));
		_ret = String.valueOf(_ret)
				+ String.valueOf(String.valueOf(
						String.valueOf(getEndTag(getStartSection(), _index))).concat("\n"));
		return _ret;
	}

	public String getXMLEntry(int _index, String _sourcetag, String _sourcefile) {
		String _ret = "";
		_ret = String.valueOf(_ret)
				+ String.valueOf(String.valueOf(
						String.valueOf(getRootTag(getStartSection(), _index))).concat("\n"));
		if (GetCVParams().getParamsCount() > 0)
			_ret = String.valueOf(_ret)
					+ String.valueOf(GetCVParams().getXMLEntryWithoutHead(_index, _sourcetag,
							_sourcefile));
		if (GetUserParams().getParamsCount() > 0)
			_ret = String.valueOf(_ret)
					+ String.valueOf(GetUserParams().getXMLEntryWithoutHead(_index, _sourcetag,
							_sourcefile));
		if (GetReferenceableParams().getParamsCount() > 0)
			_ret = String.valueOf(_ret)
					+ String.valueOf(GetReferenceableParams().getXMLEntryWithoutHead(_index,
							_sourcetag, _sourcefile));
		_ret = String.valueOf(_ret)
				+ String.valueOf(String.valueOf(
						String.valueOf(getEndTag(getStartSection(), _index))).concat("\n"));
		return _ret;
	}

	/*
	 * // Matodos nuevos 20080627- Obtener toda la informacian adicional public
	 * Vector getAllAdditionalInformationEntryList() { // sOLO VALORES Vector
	 * _aux; int i; _aux = new Vector(); for (i = 0; i <
	 * GetCVParams().getParamsCount(); i++)
	 * _aux.addElement(GetCVParams().getCVGenericTag().elementAt(i)); for (i =
	 * 0; i < GetuserParams().getParamsCount(); i++)
	 * _aux.addElement(GetuserParams().getUserGenericTag().elementAt(i)); return
	 * (_aux); } public Vector getAllAdditionalInformationDefinitionList() { //
	 * sOLO VALORES Vector _aux; int i; _aux = new Vector(); for (i = 0; i <
	 * GetCVParams().getParamsCount(); i++)
	 * _aux.addElement(GetCVParams().getCVGenericKey().elementAt(i)); for (i =
	 * 0; i < GetuserParams().getParamsCount(); i++)
	 * _aux.addElement(GetuserParams().getUserGenericKey().elementAt(i)); return
	 * (_aux); }
	 */

	// Nuevo método que devuelve todo lo asociado a un CvParam o UserParam
	// List.
	public int GetParamsSize() {
		int _ret = 0;
		_ret = this.GetCVParams().getParamsCount() + this.GetUserParams().getParamsCount();
		return _ret;
	}

	public SourceFile translate2SourceFile() {
		SourceFile ret = new SourceFile();

		// parse attributes

		String id = this.getAttributeList().getAttributeValue("id");
		if (id != null) {
			ret.setId(id);
			String name = this.getAttributeList().getAttributeValue("name");
			if (name != null)
				ret.setName(name);
			String location = this.getAttributeList().getAttributeValue("location");
			if (location != null)
				ret.setLocation(location);
		}

		// parse child elements

		TXMLUserAdditionalInfo params = this.GetCVParams();
		if (params != null && params.getParamsList().size() > 0) {
			for (TXMLAttributeList attribute : params.getParamsList()) {
				CVParam cvParam = attribute.translate2CVParam();
				ret.getCvParam().add(cvParam);
			}
		}
		params = this.GetUserParams();
		if (params != null && params.getParamsList().size() > 0) {
			for (TXMLAttributeList attribute : params.getParamsList()) {
				UserParam userParam = attribute.translate2UserParam();
				ret.getUserParam().add(userParam);
			}
		}
		params = this.GetReferenceableParams();
		if (params != null && params.getParamsList().size() > 0) {
			for (TXMLAttributeList attribute : params.getParamsList()) {
				ReferenceableParamGroupRef paramRef = attribute.translate2RefParamGroup();
				ret.getReferenceableParamGroupRef().add(paramRef);
			}
		}

		return ret;

	}

	public Software translate2Software() {

		Software ret = new Software();
		final TXMLAttributeList attributeList = this.getAttributeList();
		String id = attributeList.getAttributeValue("id");
		if (!id.equals(""))
			ret.setId(id);
		String version = attributeList.getAttributeValue("version");
		if (!version.equals(""))
			ret.setVersion(version);

		TXMLUserAdditionalInfo params = this.GetCVParams();
		if (params != null && params.getParamsList().size() > 0) {
			for (TXMLAttributeList attribute : params.getParamsList()) {
				CVParam cvParam = attribute.translate2CVParam();
				ret.getCvParam().add(cvParam);
			}
		}
		params = this.GetUserParams();
		if (params != null && params.getParamsList().size() > 0) {
			for (TXMLAttributeList attribute : params.getParamsList()) {
				UserParam userParam = attribute.translate2UserParam();
				ret.getUserParam().add(userParam);
			}
		}
		params = this.GetReferenceableParams();
		if (params != null && params.getParamsList().size() > 0) {
			for (TXMLAttributeList attribute : params.getParamsList()) {
				ReferenceableParamGroupRef paramRef = attribute.translate2RefParamGroup();
				ret.getReferenceableParamGroupRef().add(paramRef);
			}
		}

		return ret;

	}

	public SourceComponent translate2Source() {
		SourceComponent ret = new SourceComponent();

		final TXMLAttributeList attributeList = this.getAttributeList();
		int order = Integer.valueOf(attributeList.getAttributeValue("order"));
		ret.setOrder(order);
		TXMLUserAdditionalInfo params = this.GetCVParams();
		if (params != null && params.getParamsList().size() > 0) {
			for (TXMLAttributeList attribute : params.getParamsList()) {
				CVParam cvParam = attribute.translate2CVParam();
				ret.getCvParam().add(cvParam);
			}
		}
		params = this.GetUserParams();
		if (params != null && params.getParamsList().size() > 0) {
			for (TXMLAttributeList attribute : params.getParamsList()) {
				UserParam userParam = attribute.translate2UserParam();
				ret.getUserParam().add(userParam);
			}
		}
		params = this.GetReferenceableParams();
		if (params != null && params.getParamsList().size() > 0) {
			for (TXMLAttributeList attribute : params.getParamsList()) {
				ReferenceableParamGroupRef paramRef = attribute.translate2RefParamGroup();
				ret.getReferenceableParamGroupRef().add(paramRef);
			}
		}

		return ret;
	}

	public AnalyzerComponent translate2Analyzer() {
		AnalyzerComponent ret = new AnalyzerComponent();

		final TXMLAttributeList attributeList = this.getAttributeList();
		int order = Integer.valueOf(attributeList.getAttributeValue("order"));
		ret.setOrder(order);
		TXMLUserAdditionalInfo params = this.GetCVParams();
		if (params != null && params.getParamsList().size() > 0) {
			for (TXMLAttributeList attribute : params.getParamsList()) {
				CVParam cvParam = attribute.translate2CVParam();
				ret.getCvParam().add(cvParam);
			}
		}
		params = this.GetUserParams();
		if (params != null && params.getParamsList().size() > 0) {
			for (TXMLAttributeList attribute : params.getParamsList()) {
				UserParam userParam = attribute.translate2UserParam();
				ret.getUserParam().add(userParam);
			}
		}
		params = this.GetReferenceableParams();
		if (params != null && params.getParamsList().size() > 0) {
			for (TXMLAttributeList attribute : params.getParamsList()) {
				ReferenceableParamGroupRef paramRef = attribute.translate2RefParamGroup();
				ret.getReferenceableParamGroupRef().add(paramRef);
			}
		}

		return ret;
	}

	public DetectorComponent translate2Detector() {
		DetectorComponent ret = new DetectorComponent();
		final TXMLAttributeList attributeList = this.getAttributeList();
		int order = Integer.valueOf(attributeList.getAttributeValue("order"));
		ret.setOrder(order);
		TXMLUserAdditionalInfo params = this.GetCVParams();
		if (params != null && params.getParamsList().size() > 0) {
			for (TXMLAttributeList attribute : params.getParamsList()) {
				CVParam cvParam = attribute.translate2CVParam();
				ret.getCvParam().add(cvParam);
			}
		}
		params = this.GetUserParams();
		if (params != null && params.getParamsList().size() > 0) {
			for (TXMLAttributeList attribute : params.getParamsList()) {
				UserParam userParam = attribute.translate2UserParam();
				ret.getUserParam().add(userParam);
			}
		}
		params = this.GetReferenceableParams();
		if (params != null && params.getParamsList().size() > 0) {
			for (TXMLAttributeList attribute : params.getParamsList()) {
				ReferenceableParamGroupRef paramRef = attribute.translate2RefParamGroup();
				ret.getReferenceableParamGroupRef().add(paramRef);
			}
		}

		return ret;
	}

	public ParamGroup translate2ParamGroup() {
		ParamGroup ret = new ParamGroup();

		TXMLUserAdditionalInfo params = this.GetCVParams();
		if (params != null && params.getParamsList().size() > 0) {
			for (TXMLAttributeList attribute : params.getParamsList()) {
				CVParam cvParam = attribute.translate2CVParam();
				ret.getCvParam().add(cvParam);
			}
		}
		params = this.GetUserParams();
		if (params != null && params.getParamsList().size() > 0) {
			for (TXMLAttributeList attribute : params.getParamsList()) {
				UserParam userParam = attribute.translate2UserParam();
				ret.getUserParam().add(userParam);
			}
		}
		params = this.GetReferenceableParams();
		if (params != null && params.getParamsList().size() > 0) {
			for (TXMLAttributeList attribute : params.getParamsList()) {
				ReferenceableParamGroupRef paramRef = attribute.translate2RefParamGroup();
				ret.getReferenceableParamGroupRef().add(paramRef);
			}
		}

		return ret;
	}

	public ReferenceableParamGroup translate2ReferenceableParamGroup() {
		ReferenceableParamGroup ret = new ReferenceableParamGroup();

		TXMLUserAdditionalInfo params = this.GetCVParams();
		if (params != null && params.getParamsList().size() > 0) {
			for (TXMLAttributeList attribute : params.getParamsList()) {
				CVParam cvParam = attribute.translate2CVParam();
				ret.getCvParam().add(cvParam);
			}
		}
		params = this.GetUserParams();
		if (params != null && params.getParamsList().size() > 0) {
			for (TXMLAttributeList attribute : params.getParamsList()) {
				UserParam userParam = attribute.translate2UserParam();
				ret.getUserParam().add(userParam);
			}
		}

		final String id = this.getAttributeList().getAttributes().get("id");
		if (id != null)
			ret.setId(id);

		return ret;
	}

	public ProcessingMethod translate2ProcessingMethod() {
		ProcessingMethod ret = new ProcessingMethod();

		final Integer order = Integer.valueOf(this.getAttributeList().getAttributes().get("order"));
		if (order != null)
			ret.setOrder(order);
		final String softwareRef = this.getAttributeList().getAttributes().get("softwareRef");
		if (softwareRef != null)
			ret.setSoftwareRef(softwareRef);

		TXMLUserAdditionalInfo params = this.GetCVParams();
		if (params != null && params.getParamsList().size() > 0) {
			for (TXMLAttributeList attribute : params.getParamsList()) {
				CVParam cvParam = attribute.translate2CVParam();
				ret.getCvParam().add(cvParam);
			}
		}
		params = this.GetUserParams();
		if (params != null && params.getParamsList().size() > 0) {
			for (TXMLAttributeList attribute : params.getParamsList()) {
				UserParam userParam = attribute.translate2UserParam();
				ret.getUserParam().add(userParam);
			}
		}
		params = this.GetReferenceableParams();
		if (params != null && params.getParamsList().size() > 0) {
			for (TXMLAttributeList attribute : params.getParamsList()) {
				ReferenceableParamGroupRef paramRef = attribute.translate2RefParamGroup();
				ret.getReferenceableParamGroupRef().add(paramRef);
			}
		}

		return ret;
	}

	public Sample translate2Sample() {
		Sample ret = new Sample();

		final String id = this.getAttributeList().getAttributes().get("id");
		if (id != null)
			ret.setId(id);
		final String name = this.getAttributeList().getAttributes().get("name");
		if (name != null)
			ret.setName(name);

		TXMLUserAdditionalInfo params = this.GetCVParams();
		if (params != null && params.getParamsList().size() > 0) {
			for (TXMLAttributeList attribute : params.getParamsList()) {
				CVParam cvParam = attribute.translate2CVParam();
				ret.getCvParam().add(cvParam);
			}
		}
		params = this.GetUserParams();
		if (params != null && params.getParamsList().size() > 0) {
			for (TXMLAttributeList attribute : params.getParamsList()) {
				UserParam userParam = attribute.translate2UserParam();
				ret.getUserParam().add(userParam);
			}
		}
		params = this.GetReferenceableParams();
		if (params != null && params.getParamsList().size() > 0) {
			for (TXMLAttributeList attribute : params.getParamsList()) {
				ReferenceableParamGroupRef paramRef = attribute.translate2RefParamGroup();
				ret.getReferenceableParamGroupRef().add(paramRef);
			}
		}
		return ret;
	}
}