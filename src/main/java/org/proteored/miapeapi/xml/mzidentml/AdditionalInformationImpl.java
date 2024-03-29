package org.proteored.miapeapi.xml.mzidentml;

import org.proteored.miapeapi.interfaces.msi.MSIAdditionalInformation;
import org.proteored.miapeapi.xml.mzidentml.autogenerated.FuGECommonReferencesBibliographicReferenceType;
import org.proteored.miapeapi.xml.mzidentml.autogenerated.SampleType;
import org.proteored.miapeapi.xml.mzidentml.util.MzidentmlControlVocabularyXmlFactory;
import org.proteored.miapeapi.xml.mzidentml.util.Utils;
import org.proteored.miapeapi.xml.util.MiapeXmlUtil;

public class AdditionalInformationImpl implements MSIAdditionalInformation {
	private String name="";
	private String value="";

	public AdditionalInformationImpl(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public AdditionalInformationImpl(SampleType sampleXML) {
		if (sampleXML != null) {
			if (sampleXML.getName() != null && !sampleXML.getName().equals("")) {
				name = sampleXML.getName();
			}else{
				name = sampleXML.getId();
			}
			if (sampleXML.getParamGroup() != null) {
				value = MzidentmlControlVocabularyXmlFactory.readEntireParamList(sampleXML.getParamGroup());
			}
		}
	}

	public AdditionalInformationImpl(
			FuGECommonReferencesBibliographicReferenceType referenceXML) {
		StringBuilder sb = new StringBuilder();
		if (referenceXML != null) {
			name = "Bibliographic reference";
			if (referenceXML.getAuthors() != null){
				if (!referenceXML.getAuthors().equals("")){
					sb.append(Utils.AUTHORS + "=" + referenceXML.getAuthors() + MiapeXmlUtil.TERM_SEPARATOR);
				}
			}
			if (referenceXML.getEditor() != null){
				if (!referenceXML.getEditor().equals("")){
					sb.append(Utils.EDITOR + "=" + referenceXML.getEditor() + MiapeXmlUtil.TERM_SEPARATOR);
				}
			}
			if (referenceXML.getId() != null){
				if (!referenceXML.getId().equals("")){
					sb.append(Utils.ID + "=" + referenceXML.getId() + MiapeXmlUtil.TERM_SEPARATOR);
				}
			}
			if (referenceXML.getName() != null){
				if (!referenceXML.getName().equals("")){
					sb.append(Utils.NAME + "=" + referenceXML.getName() + MiapeXmlUtil.TERM_SEPARATOR);
				}
			}
			if (referenceXML.getIssue() != null){
				if (!referenceXML.getIssue().equals("")){
					sb.append(Utils.ISSUE + "=" + referenceXML.getIssue() + MiapeXmlUtil.TERM_SEPARATOR);
				}
			}
			if (referenceXML.getPages() != null){
				if (!referenceXML.getPages().equals("")){
					sb.append(Utils.PAGES + "=" + referenceXML.getPages() + MiapeXmlUtil.TERM_SEPARATOR);
				}
			}
			if (referenceXML.getPublication() != null){
				if (!referenceXML.getPublication().equals("")){
					sb.append(Utils.PUBLICATION + "=" + referenceXML.getPublication() + MiapeXmlUtil.TERM_SEPARATOR);
				}
			}
			if (referenceXML.getVolume() != null){
				if (!referenceXML.getVolume().equals("")){
					sb.append(Utils.VOLUME + "=" + referenceXML.getVolume() + MiapeXmlUtil.TERM_SEPARATOR);
				}
			}
			if (referenceXML.getYear() != null){
				if (!referenceXML.getYear().equals("")){
					sb.append(Utils.YEAR + "=" + referenceXML.getYear() + MiapeXmlUtil.TERM_SEPARATOR);
				}
			}
			if (referenceXML.getPublisher() != null){
				if (!referenceXML.getPublisher().equals("")){
					sb.append(Utils.PUBLISHER + "=" + referenceXML.getPublisher() + MiapeXmlUtil.TERM_SEPARATOR);
				}
			}
			if (referenceXML.getTitle() != null){
				if (!referenceXML.getTitle().equals("")){
					sb.append(Utils.TITLE + "=" + referenceXML.getTitle() + MiapeXmlUtil.TERM_SEPARATOR);
				}
			}
			value = sb.toString();
		}
	}

	@Override
	public String getName() {
		if (name != null && !name.equals(""))
			return name;
		return null;
	}

	@Override
	public String getValue() {
		if (value != null && !value.equals(""))
			return value;
		return null;
	}

}
