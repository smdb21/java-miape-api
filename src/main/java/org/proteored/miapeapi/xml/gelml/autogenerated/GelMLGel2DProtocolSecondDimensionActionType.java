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
 *  An action to represent the order of the referenced ElectrophoresisProtocol,
 *     with respect to other Actions performed within this parent protocol. 
 * 
 * <p>Java class for GelML.Gel2DProtocol.SecondDimensionActionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GelML.Gel2DProtocol.SecondDimensionActionType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.psidev.info/gelml/1_1candidate}FuGE.Common.Protocol.ActionType">
 *       &lt;attribute name="ElectrophoresisProtocol_ref" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GelML.Gel2DProtocol.SecondDimensionActionType")
public class GelMLGel2DProtocolSecondDimensionActionType
    extends FuGECommonProtocolActionType
{

    @XmlAttribute(name = "ElectrophoresisProtocol_ref", required = true)
    protected String electrophoresisProtocolRef;

    /**
     * Gets the value of the electrophoresisProtocolRef property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getElectrophoresisProtocolRef() {
        return electrophoresisProtocolRef;
    }

    /**
     * Sets the value of the electrophoresisProtocolRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setElectrophoresisProtocolRef(String value) {
        this.electrophoresisProtocolRef = value;
    }

}
