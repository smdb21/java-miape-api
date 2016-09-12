//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.2-7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.04.11 at 04:01:56 PM CEST 
//


package org.proteored.miapeapi.xml.msi.autogenerated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 *  The equipment (hardware) used in the Protocol. Examples include: computers, scanners, wash stations etc...   Equipment is abstract and should either be extended by subclassing or the GenericEquipment class, a functional version of Equipment, should be used. 
 * 
 * <p>Java class for FuGE.Common.Protocol.EquipmentType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FuGE.Common.Protocol.EquipmentType">
 *   &lt;complexContent>
 *     &lt;extension base="{}FuGE.Common.IdentifiableType">
 *       &lt;sequence>
 *         &lt;element name="make" type="{}ParamType" minOccurs="0"/>
 *         &lt;element name="model" type="{}ParamType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FuGE.Common.Protocol.EquipmentType", propOrder = {
    "make",
    "model"
})
@XmlSeeAlso({
    FuGECommonProtocolGenericEquipmentType.class
})
public abstract class FuGECommonProtocolEquipmentType
    extends FuGECommonIdentifiableType
{

    protected ParamType make;
    protected ParamType model;

    /**
     * Gets the value of the make property.
     * 
     * @return
     *     possible object is
     *     {@link ParamType }
     *     
     */
    public ParamType getMake() {
        return make;
    }

    /**
     * Sets the value of the make property.
     * 
     * @param value
     *     allowed object is
     *     {@link ParamType }
     *     
     */
    public void setMake(ParamType value) {
        this.make = value;
    }

    /**
     * Gets the value of the model property.
     * 
     * @return
     *     possible object is
     *     {@link ParamType }
     *     
     */
    public ParamType getModel() {
        return model;
    }

    /**
     * Sets the value of the model property.
     * 
     * @param value
     *     allowed object is
     *     {@link ParamType }
     *     
     */
    public void setModel(ParamType value) {
        this.model = value;
    }

}
