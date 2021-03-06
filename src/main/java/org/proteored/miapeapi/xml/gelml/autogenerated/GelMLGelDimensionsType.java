//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-257 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.09.14 at 07:25:51 PM CEST 
//


package org.proteored.miapeapi.xml.gelml.autogenerated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 *  The measurements must be in the form of the Cartesian Coordinate system
 *     (x,y,z). According to the standard image orientation described in section 7.1.6 of the MIAPE
 *     document x represents the distance from the anode (+) to the cathode (-). For example in an IPG
 *     strip x = the strip length, for a standard slab gel, x = the width and z = the matrix depth. All
 *     dimensions should be given in milimetres. 
 * 
 * <p>Java class for GelML.Gel.DimensionsType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GelML.Gel.DimensionsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="x" type="{http://www.psidev.info/gelml/1_1candidate}FuGE.Common.Ontology.PropertyValue"/>
 *         &lt;element name="y" type="{http://www.psidev.info/gelml/1_1candidate}FuGE.Common.Ontology.PropertyValue"/>
 *         &lt;element name="z" type="{http://www.psidev.info/gelml/1_1candidate}FuGE.Common.Ontology.PropertyValue"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GelML.Gel.DimensionsType", propOrder = {
    "x",
    "y",
    "z"
})
public class GelMLGelDimensionsType {

    @XmlElement(required = true)
    protected FuGECommonOntologyPropertyValue x;
    @XmlElement(required = true)
    protected FuGECommonOntologyPropertyValue y;
    @XmlElement(required = true)
    protected FuGECommonOntologyPropertyValue z;

    /**
     * Gets the value of the x property.
     * 
     * @return
     *     possible object is
     *     {@link FuGECommonOntologyPropertyValue }
     *     
     */
    public FuGECommonOntologyPropertyValue getX() {
        return x;
    }

    /**
     * Sets the value of the x property.
     * 
     * @param value
     *     allowed object is
     *     {@link FuGECommonOntologyPropertyValue }
     *     
     */
    public void setX(FuGECommonOntologyPropertyValue value) {
        this.x = value;
    }

    /**
     * Gets the value of the y property.
     * 
     * @return
     *     possible object is
     *     {@link FuGECommonOntologyPropertyValue }
     *     
     */
    public FuGECommonOntologyPropertyValue getY() {
        return y;
    }

    /**
     * Sets the value of the y property.
     * 
     * @param value
     *     allowed object is
     *     {@link FuGECommonOntologyPropertyValue }
     *     
     */
    public void setY(FuGECommonOntologyPropertyValue value) {
        this.y = value;
    }

    /**
     * Gets the value of the z property.
     * 
     * @return
     *     possible object is
     *     {@link FuGECommonOntologyPropertyValue }
     *     
     */
    public FuGECommonOntologyPropertyValue getZ() {
        return z;
    }

    /**
     * Sets the value of the z property.
     * 
     * @param value
     *     allowed object is
     *     {@link FuGECommonOntologyPropertyValue }
     *     
     */
    public void setZ(FuGECommonOntologyPropertyValue value) {
        this.z = value;
    }

}
