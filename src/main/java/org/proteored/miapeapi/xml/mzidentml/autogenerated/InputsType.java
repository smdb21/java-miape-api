//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-257 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.09.14 at 07:25:56 PM CEST 
//


package org.proteored.miapeapi.xml.mzidentml.autogenerated;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for InputsType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="InputsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SourceFile" type="{http://psidev.info/psi/pi/mzIdentML/1.0}PSI-PI.analysis.search.SourceFileType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="SearchDatabase" type="{http://psidev.info/psi/pi/mzIdentML/1.0}PSI-PI.analysis.search.SearchDatabaseType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="SpectraData" type="{http://psidev.info/psi/pi/mzIdentML/1.0}PSI-PI.spectra.SpectraDataType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InputsType", propOrder = {
    "sourceFile",
    "searchDatabase",
    "spectraData"
})
public class InputsType {

    @XmlElement(name = "SourceFile")
    protected List<PSIPIAnalysisSearchSourceFileType> sourceFile;
    @XmlElement(name = "SearchDatabase")
    protected List<PSIPIAnalysisSearchSearchDatabaseType> searchDatabase;
    @XmlElement(name = "SpectraData")
    protected List<PSIPISpectraSpectraDataType> spectraData;

    /**
     * Gets the value of the sourceFile property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the sourceFile property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSourceFile().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PSIPIAnalysisSearchSourceFileType }
     * 
     * 
     */
    public List<PSIPIAnalysisSearchSourceFileType> getSourceFile() {
        if (sourceFile == null) {
            sourceFile = new ArrayList<PSIPIAnalysisSearchSourceFileType>();
        }
        return this.sourceFile;
    }

    /**
     * Gets the value of the searchDatabase property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the searchDatabase property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSearchDatabase().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PSIPIAnalysisSearchSearchDatabaseType }
     * 
     * 
     */
    public List<PSIPIAnalysisSearchSearchDatabaseType> getSearchDatabase() {
        if (searchDatabase == null) {
            searchDatabase = new ArrayList<PSIPIAnalysisSearchSearchDatabaseType>();
        }
        return this.searchDatabase;
    }

    /**
     * Gets the value of the spectraData property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the spectraData property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSpectraData().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PSIPISpectraSpectraDataType }
     * 
     * 
     */
    public List<PSIPISpectraSpectraDataType> getSpectraData() {
        if (spectraData == null) {
            spectraData = new ArrayList<PSIPISpectraSpectraDataType>();
        }
        return this.spectraData;
    }

}
