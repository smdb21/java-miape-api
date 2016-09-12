//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-257 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.09.14 at 07:25:51 PM CEST 
//


package org.proteored.miapeapi.xml.gelml.autogenerated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 *  An action to represent the use of buffers in the Protocol by reference to a
 *     SubstanceMixtureProtocol that represents the buffer and optionally the method of producing the
 *     buffer. 
 * 
 * <p>Java class for GelML.Electrophoresis.AddBufferActionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GelML.Electrophoresis.AddBufferActionType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.psidev.info/gelml/1_1candidate}FuGE.Common.Protocol.ActionType">
 *       &lt;attribute name="SubstanceMixtureProtocol_ref" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GelML.Electrophoresis.AddBufferActionType")
public class GelMLElectrophoresisAddBufferActionType
    extends FuGECommonProtocolActionType
{

    @XmlAttribute(name = "SubstanceMixtureProtocol_ref", required = true)
    protected String substanceMixtureProtocolRef;

    /**
     * Gets the value of the substanceMixtureProtocolRef property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubstanceMixtureProtocolRef() {
        return substanceMixtureProtocolRef;
    }

    /**
     * Sets the value of the substanceMixtureProtocolRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubstanceMixtureProtocolRef(String value) {
        this.substanceMixtureProtocolRef = value;
    }

}
