//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.2-7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.06.19 at 01:51:42 PM CEST 
//


package org.proteored.miapeapi.xml.ms.autogenerated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;extension base="{}MIAPE_Software_Type">
 *       &lt;sequence>
 *         &lt;element name="Parameter_File" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/>
 *         &lt;element name="TransitionList_File" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/>
 *         &lt;element name="TargetList" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "parameterFile",
    "transitionListFile",
    "targetList"
})
@XmlRootElement(name = "MS_Acquisition")
public class MSAcquisition
    extends MIAPESoftwareType
{

    @XmlElement(name = "Parameter_File")
    @XmlSchemaType(name = "anyURI")
    protected String parameterFile;
    @XmlElement(name = "TransitionList_File")
    @XmlSchemaType(name = "anyURI")
    protected String transitionListFile;
    @XmlElement(name = "TargetList")
    protected String targetList;

    /**
     * Gets the value of the parameterFile property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParameterFile() {
        return parameterFile;
    }

    /**
     * Sets the value of the parameterFile property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParameterFile(String value) {
        this.parameterFile = value;
    }

    /**
     * Gets the value of the transitionListFile property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransitionListFile() {
        return transitionListFile;
    }

    /**
     * Sets the value of the transitionListFile property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransitionListFile(String value) {
        this.transitionListFile = value;
    }

    /**
     * Gets the value of the targetList property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTargetList() {
        return targetList;
    }

    /**
     * Sets the value of the targetList property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTargetList(String value) {
        this.targetList = value;
    }

}
