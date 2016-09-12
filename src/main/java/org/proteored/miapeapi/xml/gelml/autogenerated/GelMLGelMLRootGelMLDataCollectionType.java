//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-257 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.09.14 at 07:25:51 PM CEST 
//


package org.proteored.miapeapi.xml.gelml.autogenerated;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 *  Collection class for accessing all ExternalData instances in GelML.
 *    
 * 
 * <p>Java class for GelML.GelMLRoot.GelMLDataCollectionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GelML.GelMLRoot.GelMLDataCollectionType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.psidev.info/gelml/1_1candidate}ExternalData" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GelML.GelMLRoot.GelMLDataCollectionType", propOrder = {
    "externalData"
})
public class GelMLGelMLRootGelMLDataCollectionType {

    @XmlElementRef(name = "ExternalData", namespace = "http://www.psidev.info/gelml/1_1candidate", type = JAXBElement.class)
    protected List<JAXBElement<? extends FuGEBioDataExternalDataType>> externalData;

    /**
     * Gets the value of the externalData property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the externalData property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getExternalData().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link GelMLImageAcquisitionImageType }{@code >}
     * {@link JAXBElement }{@code <}{@link FuGEBioDataExternalDataType }{@code >}
     * 
     * 
     */
    public List<JAXBElement<? extends FuGEBioDataExternalDataType>> getExternalData() {
        if (externalData == null) {
            externalData = new ArrayList<JAXBElement<? extends FuGEBioDataExternalDataType>>();
        }
        return this.externalData;
    }

}
