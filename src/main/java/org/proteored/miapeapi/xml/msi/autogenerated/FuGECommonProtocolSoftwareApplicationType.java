//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.2-7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.04.11 at 04:01:56 PM CEST 
//


package org.proteored.miapeapi.xml.msi.autogenerated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 *  The use of a piece of software with the requisite Parameters and ParameterValues. 
 * 
 * <p>Java class for FuGE.Common.Protocol.SoftwareApplicationType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FuGE.Common.Protocol.SoftwareApplicationType">
 *   &lt;complexContent>
 *     &lt;extension base="{}FuGE.Common.Protocol.ParameterizableApplicationType">
 *       &lt;attribute name="Software_ref" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FuGE.Common.Protocol.SoftwareApplicationType")
public class FuGECommonProtocolSoftwareApplicationType
    extends FuGECommonProtocolParameterizableApplicationType
{

    @XmlAttribute(name = "Software_ref", required = true)
    protected String softwareRef;

    /**
     * Gets the value of the softwareRef property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSoftwareRef() {
        return softwareRef;
    }

    /**
     * Sets the value of the softwareRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSoftwareRef(String value) {
        this.softwareRef = value;
    }

}
